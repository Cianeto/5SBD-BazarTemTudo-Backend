package com.sbd.bazartemtudo.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    @Column(name = "id", nullable = false, length = 30)
    private String orderItemId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_orderitem_order"))
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false, foreignKey = @ForeignKey(name = "fk_orderitem_item"))
    private Item item;

    @OneToOne(mappedBy = "orderItem")
    private Purchase purchase;

}
