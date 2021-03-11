package com.naharavirtualwallet.virtualwallet.service;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodCreateParams;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Log4j2
public class StripePaymentService {
    private static final String STRIPE_API_KEY = ""+System.getenv("STRIPE_API_KEY");

    public String cardPaymentCapabilities(String stripeId) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        Account account =
                Account.retrieve(stripeId);
        Capability capability =
                account.capabilities().retrieve(
                        "card_payments");

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
    public PaymentMethod clonePaymentMethod(String paymentMethod, String customerId, String connectedAccountId) throws StripeException {
        PaymentMethodCreateParams paymentMethodParams =
                PaymentMethodCreateParams.builder()
                        .setCustomer(customerId)
                        .setPaymentMethod(paymentMethod)
                        .build();

        RequestOptions requestOptions =
                RequestOptions.builder()
                        .setStripeAccount(connectedAccountId)
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
    //clones customer attaching to connected account
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
    public String chargeSimulation(String customerId, String connectedAccountId, Long amount, String paymentMethodToClone) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;

        Customer temp=Customer.retrieve(customerId);
        Map<String, Object> methods = new HashMap<>();
        methods.put("customer", temp.getId());
        methods.put("type", "card");

        PaymentMethod paymentMethod = clonePaymentMethod(paymentMethodToClone,customerId,connectedAccountId );
        log.info(paymentMethod.getId());
        CustomerCreateParams paramsCustomer =
                CustomerCreateParams.builder()
                        .setPaymentMethod(paymentMethod.getId())
                        .build();

        RequestOptions requestOptions3 =
                RequestOptions.builder()
                        .setStripeAccount(connectedAccountId)
                        .build();

        Customer customer =  Customer.create(paramsCustomer, requestOptions3);

        log.info(paramsCustomer.getPaymentMethod());

        PaymentIntentCreateParams paramsPayment =
                PaymentIntentCreateParams.builder()
                        .setAmount(1099L)
                        .setCurrency("usd")
                        .addPaymentMethodType("card")
                        .setPaymentMethod(paymentMethod.getId())
                        .setCustomer(customer.getId())
                        .build();


        RequestOptions requestOptions1 =
                RequestOptions.builder()
                        .setStripeAccount(connectedAccountId)
                        .build();
        try {
            PaymentIntent paymentIntent = PaymentIntent.create(paramsPayment, requestOptions1);
            log.info(paymentIntent.getId());
            paymentIntent.confirm(requestOptions1);

        } catch (Exception e) {
            e.printStackTrace();
            return "payment unsuccessful";
        }
        return "payment successful";
    }
    public String addCard(String customerId, Card card) throws StripeException {

        Stripe.apiKey = STRIPE_API_KEY;
        Map<String, Object> params = new HashMap<>();
                params.put("type", "card");
                params.put("card", getCard(card));
        try {
            PaymentMethod paymentMethod = PaymentMethod.create(params);
            //attach to customer
            Map<String, Object> attach = new HashMap<>();
                attach.put("customer", customerId);
            paymentMethod.attach(attach);
        } catch (Exception e) {
            e.printStackTrace();
            return "card attachment unsuccessful";
        }
        return "card attachment successful";
    }
    public Map<String, Object> getCard(Card card){
        //in production number and cvc is taken or implemented on client side
        //creates 3digit cvc number
        Random rand = new Random();
        StringBuilder cvc = new StringBuilder();
        cvc.append(rand.nextInt(10));
        cvc.append(rand.nextInt(10));
        cvc.append(rand.nextInt(10));

        Map<String, Object> addCard = new HashMap<>();
        addCard.put("number", "4242424242424242");
        addCard.put("exp_month", card.getExpMonth());
        addCard.put("exp_year", card.getExpYear());
        addCard.put("cvc", cvc.toString());

        return addCard;
    }

    public String addBankAccount(String stripeId) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;
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
}
