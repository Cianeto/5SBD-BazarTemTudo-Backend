package com.sbd.bazartemtudo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

import com.sbd.bazartemtudo.enums.OrderStatus;

@Entity
@Table(name = "tb_orders")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Order {

    @Id
    @Column(name = "order_id", nullable = false, length = 30)
    private String id;

    @Column(name = "purchase_date", nullable = false)
    private Date purchaseDate;

    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;

    @Column(name = "currency", nullable = false, length = 3, columnDefinition = "CHAR(3) DEFAULT 'BRL'")
    private String currency;

    @Column(name = "price_sum", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceSum;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('PENDING', 'SENT') DEFAULT 'PENDING'")
    private OrderStatus status;

    @Column(name = "customer_id", nullable = false)
    private int customerId;
    
}