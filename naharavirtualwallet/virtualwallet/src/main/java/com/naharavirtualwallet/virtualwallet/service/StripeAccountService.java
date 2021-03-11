package com.naharavirtualwallet.virtualwallet.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class StripeAccountService {
    private static final String STRIPE_API_KEY = ""+System.getenv("STRIPE_API_KEY");
    public String getAccountLink(String stripeId) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        Map<String, Object> params = new HashMap<>();
        AccountLink accountLink;

        params.put("account", stripeId);
        params.put("refresh_url", "https://example.com/reauth");
        params.put("return_url", "https://example.com/return");
        params.put("type", "account_onboarding");

        try {
            accountLink= AccountLink.create(params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return accountLink.toString();
    }
    public String deletePerson(String personId, String stripeId) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;

        Account account =
                Account.retrieve(stripeId);

        Person person =
                account.persons().retrieve(
                        personId
                );
        try{
            Person deletedPerson = person.delete();
            return "person deletion successful";
        }catch (Exception e){
            e.printStackTrace();
            return "person deletion unsuccessful";
        }
    }
    public PersonCollection listPersons(String stripeId) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        Account account =
                Account.retrieve(stripeId);

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 3);

        PersonCollection persons =
                account.persons().list(params);
        log.info(persons);
        return persons;
    }
    public String updatePerson(Person person1, String personId) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        Person updatedPerson;


        Account account =
                Account.retrieve("acct_1ISaOkPrUjBOS1ai");
        Person person =
                account.persons().retrieve(
                        "person_J55y8bC04Uh32g"
                );

        Map<String, Object> dob = new HashMap<>();
            dob.put("day",person1.getDob().getDay());
            dob.put("month",person1.getDob().getMonth());
            dob.put("year",person1.getDob().getYear());

        Map<String, Object> updateAddress = new HashMap<>();
            updateAddress.put("city", person1.getAddress().getCity());
            updateAddress.put("country", person1.getAddress().getCountry());
            updateAddress.put("line1",person1.getAddress().getLine1());
            updateAddress.put("state", person1.getAddress().getState());
            updateAddress.put("postal_code", person1.getAddress().getPostalCode());

        Map<String, Object> params = new HashMap<>();


        params.put("email", person1.getEmail());
        params.put("phone", person1.getPhone());
        params.put("dob", dob);
        params.put("address", updateAddress);

        RequestOptions requestOptions =
                RequestOptions.builder()
                        .setStripeAccount("acct_1ISaOkPrUjBOS1ai")
                        .build();
        try{
            updatedPerson=person.update(params);
            log.info("person update successful");
            log.info(updatedPerson);
            return "person update successful";
        }catch(Exception e){
            e.printStackTrace();
            return "person update unsuccessful";
        }
    }
    public Person addPerson(Person person, String stripeId) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        Account account =
                Account.retrieve(stripeId);

        Map<String, Object> dob = new HashMap<>();
            dob.put("day",person.getDob().getDay());
            dob.put("month",person.getDob().getMonth());
            dob.put("year",person.getDob().getYear());

        Map<String, Object> updateAddress = new HashMap<>();
        updateAddress.put("city", person.getAddress().getCity());
        updateAddress.put("country", person.getAddress().getCountry());
        updateAddress.put("line1",person.getAddress().getLine1());
        updateAddress.put("state", person.getAddress().getState());
        updateAddress.put("postal_code", person.getAddress().getPostalCode());

        Person newPerson;
        Map<String, Object> params = new HashMap<>();
            params.put("first_name","Jane");
            params.put("last_name", "Diaz");
        RequestOptions requestOptions =
                RequestOptions.builder()
                        .setStripeAccount(stripeId)
                        .build();
        try{
            newPerson = account.persons().create(params, requestOptions);
            log.info(newPerson);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return newPerson;
    }
}
