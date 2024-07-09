package com.sbd.bazartemtudo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_customer", uniqueConstraints = {
        @UniqueConstraint(name = "uk_customer_email", columnNames = { "email" }),
        @UniqueConstraint(name = "uk_customer_cpf", columnNames = { "cpf" })
})
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer customerId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 18)
    private String phone;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(unique = true, nullable = false, length = 14)
    private String cpf;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<Order>();

    public Customer(String name, String phone, String email, String cpf) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.cpf = cpf;
    }
}
