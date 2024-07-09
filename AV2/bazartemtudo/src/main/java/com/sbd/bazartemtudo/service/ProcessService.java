package com.sbd.bazartemtudo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbd.bazartemtudo.enums.OrderStatus;
import com.sbd.bazartemtudo.enums.PurchaseStatus;
import com.sbd.bazartemtudo.model.Order;
import com.sbd.bazartemtudo.model.OrderItem;
import com.sbd.bazartemtudo.model.Purchase;
import com.sbd.bazartemtudo.repository.ItemRepo;
import com.sbd.bazartemtudo.repository.OrderItemRepo;
import com.sbd.bazartemtudo.repository.OrderRepo;
import com.sbd.bazartemtudo.repository.PurchaseRepo;

@Service
public class ProcessService {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private PurchaseRepo purchaseRepo;

    public void processPendingOrdersByPriceSum() { // VERIFICAR SE PEDIDOS ESTÃO INCOMPLETOS (FALTANDO ESTOQUE)

        List<Order> orders = orderRepo.findByStatusOrderByPriceSumDesc(OrderStatus.PENDING); // CRIAR PEDIDOS DE COMPRA
                                                                                             // EM ORDEM DECRESCENTE
                                                                                             // (VALOR TOTAL DO PEDIDO)
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemRepo.findByOrder(order);
            for (OrderItem orderItem : orderItems) {
                if (itemRepo.findById(orderItem.getItem().getSku()).get().getInventory()
                        - orderItem.getQuantity() < 0) { // NÃO HÁ ESTOQUE SUFICIENTE?

                    purchaseRepo.findByOrderItem(orderItem) // VERIFICAR SE JÁ EXISTE PEDIDO DE COMPRA COM FK ORDERITEM
                            .orElse(purchaseRepo.save(new Purchase(orderItem.getQuantity(), PurchaseStatus.PENDING, orderItem)));

                }
            }
        }
    }
}
