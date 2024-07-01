package com.sbd.bazartemtudo.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_order_item")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class OrderItem {
    @Id
    @Column(name = "orderitem_id", nullable = false, length = 30)
    private String id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "order_id", nullable = false, length = 30)
    private String orderId;

    @Column(name = "item_id", nullable = false, length = 32)
    private String itemId;
}
