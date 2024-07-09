package com.sbd.bazartemtudo.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    public String transferLoadToTables(){
        List<Load> loads = loadRepo.findAll();
        for(Load load : loads){

            Optional<Customer> customer = customerRepo.findByCpf(load.getCpf());
            if(customer.isEmpty()){
                customer = customerRepo.findByEmail(load.getCpf());
                if(customer.isEmpty()){
                    customerRepo.save(new Customer(load.getBuyerName(), load.getBuyerPhoneNumber(), load.getBuyerEmail(), load.getCpf()));
                }
            }

            Optional<Item> item = itemRepo.findBySku(load.getSku());
            if(item.isEmpty()){
                itemRepo.save(new Item(load.getSku(), load.getProductName()));
            }

            Optional<Order> order = orderRepo.findById(load.getOrderId());
            if(order.isEmpty()){
                orderRepo.save(new Order(load.getOrderId(), load.getPurchaseDate(), load.getPaymentsDate(), /*price-sum*/, OrderStatus.PENDING, customerRepo.findByCpf(load.getCpf()).getCustomerId()); // falta preencher ali
            }

            Optional<OrderItem> orderItem = orderItemRepo.findById(load.getOrderItemId());
            if(orderItem.isEmpty()){
                orderItemRepo.save(new OrderItem()); // falta preencher ali
            }
        }
    }

    public Double calcPriceSum(String orderId){
        Double pricesum = 0.0;

        List<Load> loads = loadRepo.findAll();
        for(Load load : loads){
            if(load.getOrderId() == orderId){

            }
        }
        return pricesum;
    }

}
