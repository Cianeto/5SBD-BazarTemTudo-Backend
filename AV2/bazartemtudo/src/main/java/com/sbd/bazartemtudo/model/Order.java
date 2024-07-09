package com.sbd.bazartemtudo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sbd.bazartemtudo.enums.OrderStatus;

@Entity
@Table(name = "tb_order")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Order {

    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String orderId;

    @Column(name = "purchase_date", nullable = false)
    private Date purchaseDate;

    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;

    @Column(name = "price_sum", nullable = false, precision = 10, scale = 2)
    private Double priceSum;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('PENDING', 'SENT') DEFAULT 'PENDING'")
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    @OneToMany(mappedBy = "order")
    private List<Purchase> purchases = new ArrayList<Purchase>();

    public Order(String orderId, Date purchaseDate, Date paymentDate, Double priceSum,
            OrderStatus status, Customer customer) {
        this.orderId = orderId;
        this.purchaseDate = purchaseDate;
        this.paymentDate = paymentDate;
        this.priceSum = priceSum;
        this.status = status;
        this.customer = customer;
    }
}
