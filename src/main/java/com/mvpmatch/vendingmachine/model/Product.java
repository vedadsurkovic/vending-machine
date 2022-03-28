package com.mvpmatch.vendingmachine.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Created by vedadsurkovic on 3/28/22
 **/
@RequiredArgsConstructor
@Entity(name = "Product")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String productName;

    private Integer amountAvailable;

    private Long cost;

    @OneToOne(cascade = CascadeType.ALL)
    private User seller;
}
