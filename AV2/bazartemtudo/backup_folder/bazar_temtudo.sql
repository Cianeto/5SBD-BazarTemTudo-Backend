-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Tempo de geração: 14/05/2024 às 09:23
-- Versão do servidor: 8.0.31
-- Versão do PHP: 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "-03:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Banco de dados: `bazar_temtudo`
--

DELIMITER $$
--
-- Procedimentos
--
DROP PROCEDURE IF EXISTS `db_TruncateAll`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `db_TruncateAll` ()   BEGIN

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE tb_order_item;
TRUNCATE tb_purchase;
TRUNCATE tb_order;
TRUNCATE tb_item;
TRUNCATE tb_customer;
TRUNCATE tb_loader;

SET FOREIGN_KEY_CHECKS = 1;

END$$

DROP PROCEDURE IF EXISTS `db_TruncateAllExceptLoader`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `db_TruncateAllExceptLoader` ()   BEGIN
	
    SET FOREIGN_KEY_CHECKS = 0;

    TRUNCATE tb_order_item;
    TRUNCATE tb_purchase;
    TRUNCATE tb_order;
    TRUNCATE tb_item;
    TRUNCATE tb_customer;

    SET FOREIGN_KEY_CHECKS = 1;
    
END$$

DROP PROCEDURE IF EXISTS `db_TruncateLoader`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `db_TruncateLoader` ()   BEGIN

	TRUNCATE TABLE tb_loader;

END$$

DROP PROCEDURE IF EXISTS `loader_InsertIntoCustomer`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `loader_InsertIntoCustomer` ()   BEGIN

    INSERT INTO tb_customer (name, phone, email, cpf)
    SELECT DISTINCT
       	lo.`buyer-name`, 
        lo.`buyer-phone-number`, 
        lo.`buyer-email`, 
        lo.`cpf`
    FROM tb_loader lo
    LEFT JOIN tb_customer cu ON (cu.`cpf` = lo.`cpf`)
    WHERE cu.cpf IS NULL;
END$$

DROP PROCEDURE IF EXISTS `loader_InsertIntoItem`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `loader_InsertIntoItem` ()   BEGIN

    INSERT INTO tb_item (sku, name, inventory)
    SELECT DISTINCT
        lo.`sku`, 
        lo.`product-name`, 
        0
    FROM tb_loader lo
    LEFT JOIN tb_item it ON (it.`sku` = lo.`sku`)
    WHERE it.`sku` IS NULL;

END$$

DROP PROCEDURE IF EXISTS `loader_InsertIntoOrder`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `loader_InsertIntoOrder` ()   BEGIN

    INSERT INTO tb_order (id, purchase_date, payment_date, currency, price_sum, customer_id)
    SELECT DISTINCT
        lo.`order-id`, 
        lo.`purchase-date`, 
        lo.`payments-date`,
        lo.`currency`,
        0,
        (SELECT id FROM tb_customer cu WHERE cu.`cpf` = lo.`cpf`)
    FROM tb_loader lo
    LEFT JOIN tb_order ord ON (ord.`id` = lo.`order-id`)
    WHERE ord.`id` IS NULL;

END$$

DROP PROCEDURE IF EXISTS `loader_InsertIntoOrderItem`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `loader_InsertIntoOrderItem` ()   BEGIN

    INSERT INTO tb_order_item (id, quantity, price, order_id, item_id)
    SELECT
        lo.`order-item-id`,
        lo.`quantity-purchased`,
        lo.`item-price`,
        lo.`order-id`,
        lo.`sku`
    FROM tb_loader lo
	LEFT JOIN tb_order_item oi ON (oi.`id` = lo.`order-item-id`)
    WHERE oi.`id` IS NULL;

END$$

DROP PROCEDURE IF EXISTS `loader_UpdateOrderPriceSum`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `loader_UpdateOrderPriceSum` ()   BEGIN

    UPDATE tb_order ord SET `price_sum` = (
        SELECT SUM(oi.`price` * oi.`quantity`) 
        FROM tb_order_item oi
        WHERE oi.`order_id` = ord.`id`
    );

END$$

DROP PROCEDURE IF EXISTS `loader_zRunAllProcedures`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `loader_zRunAllProcedures` ()   BEGIN
	
    CALL loader_InsertIntoCustomer();
    CALL loader_InsertIntoItem();
    CALL loader_InsertIntoOrder();
    CALL loader_InsertIntoOrderItem();
    CALL loader_UpdateOrderPriceSum();
    
END$$

DROP PROCEDURE IF EXISTS `order_UpdateOrderAndItemInventory`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `order_UpdateOrderAndItemInventory` ()   BEGIN
    DECLARE done BOOLEAN DEFAULT FALSE;
    DECLARE myOrder VARCHAR(30);
    DECLARE myItem VARCHAR(32);
    DECLARE inventoryAvailable INT;
    DECLARE inventoryNeeded INT;
    DECLARE inventoryMissing INT;
    DECLARE stat VARCHAR(8);
    DECLARE purchasesReceived BOOLEAN DEFAULT FALSE;

    DECLARE cursorOrders CURSOR FOR
        SELECT id, `status`
        FROM tb_order
	WHERE `status` = 'PENDING'
        ORDER BY price_sum DESC;
	
    DECLARE cursorItems CURSOR FOR
    	SELECT item_id
        FROM tb_order_item
        WHERE order_id = myOrder;
        
    DECLARE cursorItems2 CURSOR FOR
    	SELECT item_id
        FROM tb_order_item
        WHERE order_id = myOrder;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cursorOrders;
    order_loop: LOOP
        FETCH cursorOrders INTO myOrder, stat;
        IF done THEN
            LEAVE order_loop;
        END IF;
		
        
        OPEN cursorItems;
        item_loop: LOOP
        	FETCH cursorItems INTO myItem;
                IF done THEN
                    LEAVE item_loop;
                END IF;

                SELECT `inventory` INTO inventoryAvailable
                FROM tb_item
                WHERE `sku` = myItem;

                SELECT `quantity` INTO inventoryNeeded
                FROM tb_order_item
                WHERE `item_id` = myItem;
                
                IF inventoryAvailable < inventoryNeeded THEN
                
                    SET inventoryMissing = InventoryNeeded - inventoryAvailable;
                    
                    INSERT IGNORE INTO tb_purchase (quantity, item_id, order_id)
                    VALUES (inventoryMissing, myItem, myOrder);
        	END IF;
				
                
    END LOOP;
    CLOSE cursorItems;
    SET done = FALSE;
            
    IF NOT EXISTS (SELECT * FROM tb_purchase WHERE order_id = myOrder AND `status` = 'PENDING') THEN
    	SET purchasesReceived = TRUE;
        ELSE
	SET purchasesReceived = FALSE;
    END IF;
            
    IF purchasesReceived = TRUE THEN
    	OPEN cursorItems2;
    	item2_loop: LOOP
    		FETCH cursorItems2 INTO myItem;
		IF done THEN
	    		LEAVE item2_loop;
        	END IF;
                        
		UPDATE tb_item
		SET `inventory` = inventoryAvailable - inventoryNeeded
        	WHERE `sku` = myItem;
    	END LOOP;
    	CLOSE cursorItems2;
                
    	UPDATE tb_order
	SET `status` = 'SENT'
        WHERE id = myOrder;
                
    END IF;
    
    SET purchasesReceived = FALSE;
            
    END LOOP;
    CLOSE cursorOrders;

END$$

DROP PROCEDURE IF EXISTS `purchase_UpdatePurchaseAndOrderManually`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `purchase_UpdatePurchaseAndOrderManually` (IN `idOrder` VARCHAR(30), IN `idItem` VARCHAR(32))   BEGIN
	
    DECLARE inventoryBought INT DEFAULT 0;
    
    	SELECT `quantity` INTO inventoryBought
        FROM tb_purchase
        WHERE `status` = 'PENDING' AND `order_id` = idOrder AND `item_id` = idItem;

		UPDATE tb_item
        SET `inventory` = `inventory` + inventoryBought
        WHERE `sku` = idItem;
    
		UPDATE tb_purchase
        SET `status` = 'RECEIVED'
        WHERE `status` = 'PENDING' AND `order_id` = idOrder AND `item_id` = idItem;
		
END$$

DROP PROCEDURE IF EXISTS `purchase_UpdatePurchaseAndOrderUnqueue`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `purchase_UpdatePurchaseAndOrderUnqueue` ()   BEGIN
	
    DECLARE myOrder VARCHAR(30);
    DECLARE myItem VARCHAR(32);
    
    SELECT order_id, item_id INTO myOrder, myItem
    FROM tb_purchase_pending_queue
    WHERE `status` = 'PENDING'
    LIMIT 1;
	
    CALL purchase_UpdatePurchaseAndOrderManually(myOrder, myItem);
		
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estrutura para tabela `tb_customer`
--

DROP TABLE IF EXISTS `tb_customer`;
CREATE TABLE IF NOT EXISTS `tb_customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `phone` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '(99) 99999-9999',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cpf` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpf` (`cpf`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `tb_item`
--

DROP TABLE IF EXISTS `tb_item`;
CREATE TABLE IF NOT EXISTS `tb_item` (
  `sku` varchar(32) NOT NULL,
  `name` varchar(255) NOT NULL,
  `inventory` int NOT NULL,
  PRIMARY KEY (`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `tb_loader`
--

DROP TABLE IF EXISTS `tb_loader`;
CREATE TABLE IF NOT EXISTS `tb_loader` (
  `order-id` varchar(30) NOT NULL,
  `order-item-id` varchar(30) NOT NULL,
  `purchase-date` varchar(10) NOT NULL,
  `payments-date` varchar(10) NOT NULL,
  `buyer-email` varchar(255) NOT NULL,
  `buyer-name` varchar(255) NOT NULL,
  `cpf` varchar(14) NOT NULL,
  `buyer-phone-number` varchar(18) DEFAULT NULL,
  `sku` varchar(32) NOT NULL,
  `product-name` varchar(255) NOT NULL,
  `quantity-purchased` int NOT NULL,
  `currency` varchar(3) NOT NULL,
  `item-price` decimal(10,2) NOT NULL,
  `ship-service-level` varchar(20) NOT NULL,
  `recipient-name` varchar(255) NOT NULL,
  `ship-address-1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ship-address-2` varchar(255) DEFAULT NULL,
  `ship-address-3` varchar(255) DEFAULT NULL,
  `ship-city` varchar(30) NOT NULL,
  `ship-state` varchar(30) NOT NULL,
  `ship-postal-code` varchar(30) NOT NULL,
  `ship-country` varchar(30) NOT NULL,
  `ioss-number` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `tb_order`
--

DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE IF NOT EXISTS `tb_order` (
  `id` varchar(30) NOT NULL,
  `purchase_date` date NOT NULL,
  `payment_date` date NOT NULL,
  `currency` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'BRL',
  `price_sum` decimal(10,2) NOT NULL,
  `status` enum('PENDING','SENT') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'PENDING',
  `customer_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_customer` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `tb_order_item`
--

DROP TABLE IF EXISTS `tb_order_item`;
CREATE TABLE IF NOT EXISTS `tb_order_item` (
  `id` varchar(30) NOT NULL,
  `quantity` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `order_id` varchar(30) NOT NULL,
  `item_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_orderitem_order` (`order_id`),
  KEY `fk_orderitem_item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `tb_purchase`
--

DROP TABLE IF EXISTS `tb_purchase`;
CREATE TABLE IF NOT EXISTS `tb_purchase` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `status` enum('PENDING','RECEIVED') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'PENDING',
  `order_id` varchar(30) NOT NULL,
  `item_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`,`item_id`),
  KEY `fk_purchase_item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------


--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `tb_order`
--
ALTER TABLE `tb_order`
  ADD CONSTRAINT `fk_order_customer` FOREIGN KEY (`customer_id`) REFERENCES `tb_customer` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Restrições para tabelas `tb_order_item`
--
ALTER TABLE `tb_order_item`
  ADD CONSTRAINT `fk_orderitem_item` FOREIGN KEY (`item_id`) REFERENCES `tb_item` (`sku`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `fk_orderitem_order` FOREIGN KEY (`order_id`) REFERENCES `tb_order` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Restrições para tabelas `tb_purchase`
--
ALTER TABLE `tb_purchase`
  ADD CONSTRAINT `fk_purchase_item` FOREIGN KEY (`item_id`) REFERENCES `tb_item` (`sku`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `fk_purchase_order` FOREIGN KEY (`order_id`) REFERENCES `tb_order` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
