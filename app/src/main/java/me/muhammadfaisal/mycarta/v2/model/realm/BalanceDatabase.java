package me.muhammadfaisal.mycarta.v2.model.realm;

import com.google.gson.annotations.Expose;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BalanceDatabase extends RealmObject {

    @PrimaryKey
    private String cardID;

    private String balance;

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
