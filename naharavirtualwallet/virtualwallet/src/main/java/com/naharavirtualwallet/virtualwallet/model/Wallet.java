package com.naharavirtualwallet.virtualwallet.model;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import com.stripe.model.Account;

import java.io.Serializable;
import java.util.List;

@RedisHash("wallet")
public class Wallet implements Serializable {
    @Id
    private String walletId=NanoIdUtils.randomNanoId();
    private List<Account>walletAccounts;
    private String walletUserId;

    public Wallet(){}




    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getWalletUserId() {
        return walletUserId;
    }

    public void setWalletUserId(String walletUserId) {
        this.walletUserId = walletUserId;
    }
}
