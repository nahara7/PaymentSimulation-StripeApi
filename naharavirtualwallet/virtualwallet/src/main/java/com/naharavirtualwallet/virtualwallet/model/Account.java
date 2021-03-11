package com.naharavirtualwallet.virtualwallet.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    /*Account does not have one hash because it is contained by the user
    so does not  have a repository either*/

    private static int ACCOUNTNUMBER=100000;
    @Id
    @Indexed
    private int accountNumber;
    private Wallet walletToAccount;
    private float balance;
    private String email;
    private String country;
    private String type;
    private String businessType;
    private List<FiatTransaction> fiatTransactions=new ArrayList<>();

    public Account( Wallet wallet) {
        ACCOUNTNUMBER++;
        this.accountNumber = ACCOUNTNUMBER;
        this.walletToAccount = wallet;
    }

    public Wallet getWalletToAccount() {
        return walletToAccount;
    }

    public void setWalletToAccount(Wallet walletToAccount) {
        this.walletToAccount = walletToAccount;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setFiatTransactions(List<FiatTransaction> fiatTransactions){
        this.fiatTransactions=fiatTransactions;
    }

    public void addFiatTransaction(FiatTransaction fiatTransaction){
        this.fiatTransactions.add(fiatTransaction);
    }



}
