package com.sbd.bazartemtudo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbd.bazartemtudo.enums.OrderStatus;
import com.sbd.bazartemtudo.enums.PurchaseStatus;
import com.sbd.bazartemtudo.model.Item;
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

        List<Order> orders = orderRepo.findByStatusOrderByPriceSumDesc(OrderStatus.PENDING);
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemRepo.findByOrder(order);
            for (OrderItem orderItem : orderItems) {
                if (itemRepo.findById(orderItem.getItem().getSku()).get().getInventory()
                        - orderItem.getQuantity() < 0) { // NÃO HÁ ESTOQUE SUFICIENTE?

                    purchaseRepo.findByOrderItem(orderItem) // VERIFICAR SE JÁ EXISTE PEDIDO DE COMPRA COM FK ORDERITEM
                            .orElse(purchaseRepo
                                    .save(new Purchase(orderItem.getQuantity(), PurchaseStatus.PENDING, orderItem)));

                }
            }
        }
    }

    public void unqueuePurchase() {
        Optional<Order> order = orderRepo.findFirstByStatusOrderByPriceSumDesc(OrderStatus.PENDING);
        if (order.isPresent()) {
            List<OrderItem> orderItems = orderItemRepo.findByOrderOrderByPriceDesc(order.get());
            for (OrderItem orderItem : orderItems) {
                Purchase purchase = purchaseRepo.findByOrderItem(orderItem).orElse(null);
                if (purchase != null && purchase.getStatus().equals(PurchaseStatus.PENDING)) {

                    Item item = itemRepo.findById(orderItem.getItem().getSku()).get();

                    item.setInventory(item.getInventory() + purchase.getQuantity());

                    itemRepo.save(item);

                    purchase.setStatus(PurchaseStatus.RECEIVED);
                    purchaseRepo.save(purchase);
                    break;
                }
            }
        }
    }

    public void unqueueOrder() {
        List<Order> orders = orderRepo.findByStatusOrderByPriceSumDesc(OrderStatus.PENDING);
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemRepo.findByOrder(order);
            boolean allItemsAvailable = true;
            for (OrderItem orderItem : orderItems) {
                Item item = itemRepo.findById(orderItem.getItem().getSku()).get();
                if (item.getInventory() - orderItem.getQuantity() < 0) {
                    allItemsAvailable = false;
                    break;
                }
            }
            if (allItemsAvailable) {
                order.setStatus(OrderStatus.SENT);
                orderRepo.save(order);
                break;
            }
        }
    }

    public String updateItemInventory(String sku, Integer addInventory) {
        Optional<Item> itemOptional = itemRepo.findById(sku);
        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            item.setInventory(item.getInventory() + addInventory);
            itemRepo.save(item);

            return "Inventory updated successfully.";
        } else {
            return "Item with SKU " + sku + " not found.";
        }
    }

}
// CRIAR PEDIDOS DE COMPRA
// EM ORDEM DECRESCENTE
// (VALOR TOTAL DO PEDIDO)