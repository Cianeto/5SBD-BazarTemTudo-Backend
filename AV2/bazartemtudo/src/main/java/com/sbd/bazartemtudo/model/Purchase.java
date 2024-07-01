package com.sbd.bazartemtudo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_purchase", uniqueConstraints = {@UniqueConstraint(columnNames = {"order_id", "item_id"})})
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDING', 'RECEIVED')", nullable = false)
    private PurchaseStatus status;

    @Column(name = "order_id", nullable = false, length = 30)
    private String orderId;

    @Column(name = "item_id", nullable = false, length = 32)
    private String itemId;

}

enum PurchaseStatus {
    PENDING, RECEIVED
}