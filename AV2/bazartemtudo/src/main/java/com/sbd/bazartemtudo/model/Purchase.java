package com.sbd.bazartemtudo.model;

import com.sbd.bazartemtudo.enums.PurchaseStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_purchase")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer purchaseId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDING', 'RECEIVED')", nullable = false)
    private PurchaseStatus status;

    @OneToOne
    @JoinColumn(name = "order_item_id", nullable = false, foreignKey = @ForeignKey(name = "fk_purchase_orderitem"))
    private OrderItem orderItem;

    public Purchase(Integer quantity, PurchaseStatus status, OrderItem orderItem) {
        this.quantity = quantity;
        this.status = status;
        this.orderItem = orderItem;
    }

}