package com.naharavirtualwallet.virtualwallet.controller;

import com.naharavirtualwallet.virtualwallet.model.FiatTransaction;
import com.naharavirtualwallet.virtualwallet.model.Id;
import com.naharavirtualwallet.virtualwallet.service.StripePaymentService;
import com.naharavirtualwallet.virtualwallet.archived.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.PaymentIntent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    StripeService stripeService;
    @Autowired
    StripePaymentService stripePaymentService;
    @PostMapping("/addCard")
    public String addCard(@RequestBody Card card, @RequestBody Id id) throws StripeException {

       log.info(".card set");
       return stripeService.addCard(id.getId(), card);
    }
    @PostMapping("/addBankAccount")
    public String addBankAccount(@RequestBody Id id) throws StripeException {
    return stripeService.addBankAccount(id.getId());
    }

    @PostMapping("/paymentCardIntent")
    public void createPaymentCardIntent(@RequestBody Id id) throws StripeException {
        PaymentIntent paymentIntent=stripeService.createPaymentIntentCard(id.getId());

    }
    @PostMapping("/industry")
   public String industry(@RequestBody Id id) throws StripeException{
        return stripeService.updateAccount(id.getId());
   }

    @PostMapping("/cardPayment")
    public String cardPayment(@RequestBody FiatTransaction.transaction transaction) throws StripeException {
        return stripePaymentService.chargeSimulation(transaction.getCustomerId(), transaction.getConnectedAccountId(), transaction.getAmount(), transaction.getPaymentMethod());
    }
    @PostMapping("/cardCapability")
    public String cardPaymentCapability(@RequestBody Id id) throws StripeException {
        return stripeService.cardPaymentCapabilities(id.getId());

    }

}
