BEGIN
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
                
                SET inventoryMissing = inventoryNeeded - inventoryAvailable;
                    
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
            OPEN cursorItems;
            item_loop: LOOP
                FETCH cursorItems INTO myItem;
                IF done THEN
                    LEAVE item_loop;
                END IF;
            
                SELECT `quantity` INTO inventoryNeeded
                FROM tb_order_item
                WHERE `item_id` = myItem;

                SELECT `inventory` INTO inventoryAvailable
                FROM tb_item
                WHERE `sku` = myItem;
            
                UPDATE tb_item
                SET `inventory` = inventoryAvailable - inventoryNeeded
                WHERE `sku` = myItem;

            END LOOP;
            CLOSE cursorItems;
                
    	    UPDATE tb_order
	        SET `status` = 'SENT'
            WHERE id = myOrder;
                
        END IF;
    
    SET purchasesReceived = FALSE;
            
    END LOOP;
    CLOSE cursorOrders;

END