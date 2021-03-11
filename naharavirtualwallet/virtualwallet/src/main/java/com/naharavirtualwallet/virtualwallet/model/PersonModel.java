package com.naharavirtualwallet.virtualwallet.model;

import com.stripe.model.Person;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PersonModel {
    private Person person;
    private Id id;
    private Id personId;

    public PersonModel(){}
}
