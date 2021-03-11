package com.naharavirtualwallet.virtualwallet.controller;
import com.naharavirtualwallet.virtualwallet.model.Id;
import com.naharavirtualwallet.virtualwallet.model.PersonModel;
import com.naharavirtualwallet.virtualwallet.service.StripeAccountService;
import com.stripe.exception.StripeException;
import com.stripe.model.Person;
import com.stripe.model.PersonCollection;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/connect")
@Log4j2
public class ConnectController {
    @Autowired
    StripeAccountService stripeAccountService;

    @PostMapping("/person/add")
    public Person addPerson(@RequestBody PersonModel person) throws StripeException {
        log.info(person.getId().getId());
        return stripeAccountService.addPerson(person.getPerson(), person.getId().getId());


    }
    @PostMapping("/deletePerson")
    public String deletePerson(@RequestBody PersonModel person) throws StripeException{
        return stripeAccountService.deletePerson(person.getPersonId().getId(), person.getId().getId());
    }
    @PostMapping("/person/update")
    public String updatePerson(@RequestBody PersonModel person) throws StripeException {
        return stripeAccountService.updatePerson(person.getPerson(), person.getPersonId().getId());
    }
    @PostMapping("/person/list")
    public PersonCollection listPersons(@RequestBody Id id) throws StripeException {
        return stripeAccountService.listPersons(id.getId());
    }
    @PostMapping("/onboarding")
    public String onboarding(@RequestBody Id id) throws StripeException {
        return stripeAccountService.getAccountLink(id.getId());
    }
}
