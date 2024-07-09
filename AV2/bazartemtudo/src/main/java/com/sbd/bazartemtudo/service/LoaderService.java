package com.sbd.bazartemtudo.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbd.bazartemtudo.enums.OrderStatus;
import com.sbd.bazartemtudo.model.Customer;
import com.sbd.bazartemtudo.model.Item;
import com.sbd.bazartemtudo.model.Load;
import com.sbd.bazartemtudo.model.Order;
import com.sbd.bazartemtudo.model.OrderItem;
import com.sbd.bazartemtudo.repository.CustomerRepo;
import com.sbd.bazartemtudo.repository.ItemRepo;
import com.sbd.bazartemtudo.repository.LoadRepo;
import com.sbd.bazartemtudo.repository.OrderItemRepo;
import com.sbd.bazartemtudo.repository.OrderRepo;

@Service
public class LoaderService {
    @Autowired
    private LoadRepo loadRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;

    public void transferLoadToTables() { // REPASSAR A TABELA CARGA PARA AS DEMAIS TABELAS
        List<Load> loads = loadRepo.findAll();
        for (Load load : loads) {

            Customer customer = customerRepo.findByCpf(load.getCpf())
                    .orElseGet(() -> customerRepo.findByEmail(load.getBuyerEmail())
                            .orElse(customerRepo.save(new Customer(load.getBuyerName(), load.getBuyerPhoneNumber(),
                                    load.getBuyerEmail(), load.getCpf()))));

            Item item = itemRepo.findById(load.getSku())
                    .orElse(itemRepo.save(new Item(load.getSku(), load.getProductName())));

            Order order = orderRepo.findById(load.getOrderId())
                    .orElse(orderRepo.save(new Order(load.getOrderId(), convertStringToDate(load.getPurchaseDate()),
                            convertStringToDate(load.getPaymentsDate()),
                            calcPriceSum(load.getOrderId()), OrderStatus.PENDING, customer)));

            orderItemRepo.findById(load.getOrderItemId())
                    .orElse(orderItemRepo.save(new OrderItem(load.getOrderItemId(), load.getQuantityPurchased(),
                            load.getItemPrice(), order, item)));

        }
    }

    public BigDecimal calcPriceSum(String orderId) { // CALCULAR PREÇO TOTAL DO PEDIDO
        Double pricesum = 0.0;

        List<Load> loads = loadRepo.findAll();
        for (Load load : loads) {
            if (load.getOrderId().equals(orderId)) {
                pricesum += load.getItemPrice().doubleValue() * load.getQuantityPurchased();
            }
        }

        return BigDecimal.valueOf(pricesum);
    }

    public Date convertStringToDate(String dateString) { // CONVERTER STRING PARA O TIPO DATE
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
