package com.naharavirtualwallet.virtualwallet.model;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import com.stripe.model.Account;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.http.ResponseEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RedisHash("User")
@Data
public class User implements Serializable {
    @Id
    private String userId=NanoIdUtils.randomNanoId();
    private String stripeId;
    private String userEmail;
    private String userName;
    private String name;
    private String phone;
    private String country;
    private Wallet wallet;

    private List<com.naharavirtualwallet.virtualwallet.model.Account> accounts= new ArrayList<>();
    private List<Card> cards;
    public User(){}
}
