package me.muhammadfaisal.mycarta.v2.model;

import java.io.Serializable;

public class TransactionModel implements Serializable  {

    private String cardNumber;
    private String category;
    private String date;
    private String type;
    private String name;
    private String description;
    private String amount;

    public TransactionModel(String cardNumber, String category, String date, String type, String name, String description, String amount) {
        this.cardNumber = cardNumber;
        this.category = category;
        this.date = date;
        this.type = type;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    public TransactionModel() {

    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
