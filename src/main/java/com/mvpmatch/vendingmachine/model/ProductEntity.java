package com.mvpmatch.vendingmachine.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created by vedadsurkovic on 3/28/22
 **/
@RequiredArgsConstructor
@Entity
@Table(name = "Product")
@Data
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String productName;

    private Integer amountAvailable;

    private Long cost;

    @OneToOne(cascade = CascadeType.ALL)
    private UserEntity seller;
}
