package me.muhammadfaisal.mycarta.v2.model.firebase;

import java.io.Serializable;

public class CardModel implements Serializable {
    private String cardNumber;
    private String cardOwner;
    private String cardType;
    private String cardDescription;
    private String cardName;
    private String isPrioritize;

    public CardModel(String cardNumber, String cardOwner, String cardType, String cardDescription, String cardName, String isPrioritize) {
        this.cardNumber = cardNumber;
        this.cardOwner = cardOwner;
        this.cardType = cardType;
        this.cardDescription = cardDescription;
        this.cardName = cardName;
        this.isPrioritize = isPrioritize;
    }

    public CardModel() {

    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardOwner() {
        return cardOwner;
    }

    public void setCardOwner(String cardOwner) {
        this.cardOwner = cardOwner;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String isPrioritize() {
        return isPrioritize;
    }

    public void setPrioritize(String prioritize) {
        isPrioritize = prioritize;
    }
}
