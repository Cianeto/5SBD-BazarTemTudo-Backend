package com.sbd.bazartemtudo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_item")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Item {
    @Id
    @Column(nullable = false, length = 32)
    private String sku;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, columnDefinition = "int DEFAULT 0")
    private Integer inventory = 0;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    /* @OneToMany(mappedBy = "item")
    private List<Purchase> purchases = new ArrayList<Purchase>(); */

    public Item(String sku, String name) {
        this.sku = sku;
        this.name = name;
    }
}
