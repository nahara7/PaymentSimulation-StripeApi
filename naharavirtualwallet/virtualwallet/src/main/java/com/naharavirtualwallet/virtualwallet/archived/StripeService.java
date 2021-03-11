package com.naharavirtualwallet.virtualwallet.archived;

import com.naharavirtualwallet.virtualwallet.model.FiatTransaction;
import com.naharavirtualwallet.virtualwallet.model.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import com.stripe.param.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Log4j2
public class StripeService {
    private static final String STRIPE_API_KEY = ""+System.getenv("STRIPE_API_KEY");

    FiatTransaction fiatTransaction;

    /**
     * * Stripe Payment Implementation
     **/
    public void createConnectAccount(User user) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        Map<String, Object> params = new HashMap<>();
        params.put("type", "express");
        params.put("country", user.getCountry());
        params.put("email", user.getUserEmail());

        log.info("creating account");
        try {
            Account account = Account.create(params);
            /*attribute account to user*/
            //user.addAccount(account);
            user.setStripeId(account.getId());
            log.info("account created");
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
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
    public String createCustomer(User user) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        Customer customer;
        Map<String, Object> params = new HashMap<>();
        params.put("email", user.getUserEmail());
        log.info("creating customer");
        try {
            RequestOptions requestOptions = RequestOptions.builder().setStripeAccount("acct_1IOuzTQ64TmL3Lgk").build();
            customer = Customer.create(params, requestOptions);
            user.setStripeId(customer.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return "customer creation unsuccessful";

        }
        return "customer creation successful " + customer.getId();
    }

    public PaymentIntent createPaymentIntentCard(String stripeId) {
        Stripe.apiKey = STRIPE_API_KEY;

        ArrayList paymentMethodTypes = new ArrayList();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method_types", paymentMethodTypes);
        /*simulate amount in smallest currency unit*/
        params.put("amount", 100);
        params.put("currency", "usd");
        PaymentIntent paymentIntent;
        try {
            RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(stripeId).build();
            paymentIntent = PaymentIntent.create(params, requestOptions);
            log.info("payment Intent created");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return paymentIntent;
    }



    /**
     * Stripe User Implementation
     **/
    public String deleteAccount(String stripeId) {
        Stripe.apiKey = STRIPE_API_KEY;
        log.info(stripeId);
        Account deletedAccount;
        try {
            Account account = Account.retrieve(stripeId.toString());
            /*how is this going into the redis cache???*/
            deletedAccount = account.delete();

        } catch (StripeException e) {
            e.printStackTrace();
            return "Account deletion unsuccessful";
        }
        return "Account Successfully deleted from Stripe";
    }
    public String deleteCustomer(String stripeId) {
        Stripe.apiKey = STRIPE_API_KEY;
        log.info(stripeId);
        Customer deletedCustomer;
        try {
           Customer customer = Customer.retrieve(stripeId);
            /*how is this going into the redis cache???*/
            deletedCustomer = customer.delete();

        } catch (StripeException e) {
            e.printStackTrace();
            return "Customer deletion unsuccessful";
        }
        return "Customer Successfully deleted from Stripe";
    }

    /**
     * Stripe Card Implementation
     **/

    public String updateAccount(String stripeId) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;


        Account account=Account.retrieve(stripeId);
        Account updatedAccount;
        AccountUpdateParams params= AccountUpdateParams.builder()
                .setBusinessProfile(
                 AccountUpdateParams.BusinessProfile.builder()
                .setMcc("5734")
                .build())
        .build();

       try{
           updatedAccount=account.update(params);

       }catch(Exception e){
           e.printStackTrace();
           return "update unsuccessful";
       }
       return "update successful";
    }
    public String addCard(String stripeId, Card card) throws StripeException {
        /**in production number and cvc is taken or implemented on client side**/

        Stripe.apiKey = STRIPE_API_KEY;
        /*create random 3digit number for cvc*/
        Random rand = new Random();
        StringBuilder cvc = new StringBuilder();
        cvc.append(rand.nextInt(10));
        cvc.append(rand.nextInt(10));
        cvc.append(rand.nextInt(10));
        log.info("cvc: " + cvc);

        Map<String, Object> addCard = new HashMap<>();
        addCard.put("number", "4242424242424242");
        addCard.put("exp_month", card.getExpMonth());
        addCard.put("exp_year", card.getExpYear());
        addCard.put("cvc", cvc.toString());
        Map<String, Object> params = new HashMap<>();
        params.put("type", "card");
        params.put("card", addCard);
        try {
            PaymentMethod paymentMethod = PaymentMethod.create(params);
            /**attach to customer**/
            Map<String, Object> attach = new HashMap<>();
            attach.put("customer", stripeId);
            paymentMethod.attach(attach);

        } catch (Exception e) {
            e.printStackTrace();
            return "card attachment unsuccessful";
        }
        return "card attachment successful";
    }

    public String addBankAccount(String stripeId) throws StripeException {
        Stripe.apiKey =STRIPE_API_KEY;
        Account account = Account.retrieve(stripeId);

        Map<String, Object> params = new HashMap<>();
        params.put("external_account",
                "btok_us_verified"
        );
        try {
            BankAccount bankAccount = (BankAccount) account
                    .getExternalAccounts()
                    .create(params);
        } catch (Exception e) {
            e.printStackTrace();
            return "bank account creation unsuccessful";
        }
        return "bank account creation successful";
    }



   public String cardPaymentCapabilities(String stripeId) throws StripeException {

       Stripe.apiKey = STRIPE_API_KEY;

       Account account =
               Account.retrieve(stripeId);

       Capability capability =
               account.capabilities().retrieve(
                       "card_payments"

               );

       Map<String, Object> params = new HashMap<>();
       params.put("requested", true);

       RequestOptions requestOptions =
               RequestOptions.builder()
                       .setStripeAccount(stripeId)
                       .build();
       try {
           Capability updatedCapability =
                   capability.update(params, requestOptions);
       } catch (Exception e) {
           e.printStackTrace();
           return "capability update unsuccessful";
       }
       return "capability update successful";
   }
   public PaymentMethod clonePaymentMethod(String paymentMethod, String stripeId) throws StripeException {
        PaymentMethodCreateParams paymentMethodParams =
               PaymentMethodCreateParams.builder()
                       .setCustomer(stripeId)
                       .setPaymentMethod(paymentMethod)
                       .build();

       RequestOptions requestOptions =
               RequestOptions.builder()
                       .setStripeAccount("acct_1ISaOkPrUjBOS1ai")
                       .build();
       try{
          PaymentMethod clonedPaymentMethod= PaymentMethod.create(paymentMethodParams, requestOptions);
          log.info(clonedPaymentMethod.getCustomer());
          return clonedPaymentMethod;
      }catch(Exception e){
          e.printStackTrace();
          return null;
      }
   }
   public Customer cloneCustomer(String paymentMethodId, String stripeId) throws StripeException {
       CustomerCreateParams paramsCustomer =
               CustomerCreateParams.builder()
                       .setPaymentMethod(paymentMethodId)
                       .build();

       RequestOptions requestOptions3 =
               RequestOptions.builder()
                       .setStripeAccount(stripeId)
                       .build();
       try{
          Customer customer = Customer.create(paramsCustomer, requestOptions3);
          return customer;
      }catch(Exception e){
          e.printStackTrace();
          return null;
      }
   }



}
