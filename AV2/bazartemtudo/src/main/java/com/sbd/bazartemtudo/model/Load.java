package com.sbd.bazartemtudo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_loader")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Load {

    @Id
    @Column(name = "order_id", nullable = false, length = 30)
    private String orderId;

    @Column(name = "order_item_id", nullable = false, length = 30)
    private String orderItemId;

    @Column(name = "purchase_date", nullable = false, length = 10)
    private String purchaseDate;

    @Column(name = "payments_date", nullable = false, length = 10)
    private String paymentsDate;

    @Column(name = "buyer_email", nullable = false, length = 255)
    private String buyerEmail;

    @Column(name = "buyer_name", nullable = false, length = 255)
    private String buyerName;

    @Column(name = "cpf", nullable = false, length = 14)
    private String cpf;

    @Column(name = "buyer_phone_number", length = 18)
    private String buyerPhoneNumber;

    @Column(name = "sku", nullable = false, length = 32)
    private String sku;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "quantity_purchased", nullable = false)
    private Integer quantityPurchased;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "item_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal itemPrice;

    @Column(name = "ship_service_level", nullable = false, length = 20)
    private String shipServiceLevel;

    @Column(name = "recipient_name", nullable = false, length = 255)
    private String recipientName;

    @Column(name = "ship_address_1", nullable = false, length = 255)
    private String shipAddress1;

    @Column(name = "ship_address_2", length = 255)
    private String shipAddress2;

    @Column(name = "ship_address_3", length = 255)
    private String shipAddress3;

    @Column(name = "ship_city", nullable = false, length = 30)
    private String shipCity;

    @Column(name = "ship_state", nullable = false, length = 30)
    private String shipState;

    @Column(name = "ship_postal_code", nullable = false, length = 30)
    private String shipPostalCode;

    @Column(name = "ship_country", nullable = false, length = 30)
    private String shipCountry;

    @Column(name = "ioss_number", nullable = false, length = 30)
    private String iossNumber;

}