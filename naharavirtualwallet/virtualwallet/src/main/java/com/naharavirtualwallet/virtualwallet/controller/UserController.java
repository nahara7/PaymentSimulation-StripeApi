package com.naharavirtualwallet.virtualwallet.controller;
import com.naharavirtualwallet.virtualwallet.model.Id;
import com.naharavirtualwallet.virtualwallet.model.User;
import com.naharavirtualwallet.virtualwallet.model.Wallet;
import com.naharavirtualwallet.virtualwallet.repository.UserRepository;
import com.naharavirtualwallet.virtualwallet.archived.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Person;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@Log4j2
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    StripeService stripeService;

    @PostMapping("/addCustomer")
    public User addCustomer(@RequestBody User user) throws StripeException {
        /*create stripe customer*/
         stripeService.createCustomer(user);
         user.setWallet(new Wallet());
        (user.getWallet()).setWalletUserId(user.getUserId());
        return userRepository.save(user);
    }
    @PostMapping("/addConnectAccount")
    public User addConnectAccount(@RequestBody User user) throws StripeException {
        /*create stripe connected account that gets added to user in service class*/
        try{
            stripeService.createConnectAccount(user);
            return userRepository.save(user);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    @GetMapping("/{id}")
    public User findById(@PathVariable("id")String id){
        Optional<User> optUser=userRepository.findById(id);
        return optUser.orElse(null);
    }

    @PostMapping("/deleteUser")
        public String deleteUser( @RequestBody User userId){
            log.info(userId.getUserId());
            try{
                userRepository.deleteById(userId.getUserId());
            }catch(Exception e) {
                e.printStackTrace();
                return "deletion unsuccessful";
            }
            return "user successfully deleted";
    }
     @PostMapping("/deleteCustomer")
     public String deleteStripeCustomer(@RequestBody Id stripeId){
        log.info(stripeId.getId());
        return stripeService.deleteCustomer(stripeId.getId());
    }

    @PostMapping("/deleteAccount")
    public String deleteStripeAccount(@RequestBody Id stripeId){
        log.info(stripeId.getId());
        return stripeService.deleteAccount(stripeId.getId());
    }
     @PostMapping("/onboarding")
    public String onboarding(@RequestBody Id stripeId) throws StripeException {
        return stripeService.getAccountLink(stripeId.getId());
     }
}
