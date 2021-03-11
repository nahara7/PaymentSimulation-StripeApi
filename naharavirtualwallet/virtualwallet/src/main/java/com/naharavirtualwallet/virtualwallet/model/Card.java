package com.naharavirtualwallet.virtualwallet.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class Card implements Serializable {
    @Id
    /* to be set by stripe*/
    private String id;
    /*to be set by stripe*/
    private String object;
    private String brand;
    private String expMonth;
    private String expYear;
    private String funding;
    private String last4;
    private String cvc;

}
