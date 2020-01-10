package me.muhammadfaisal.mycarta.home.fragment.card.model;

public class Card {
    String name, descripton, type, key, exp, numberCard, cvv;

    public Card(String name, String descripton, String type, String exp, String numberCard, String cvv) {
        this.name = name;
        this.descripton = descripton;
        this.type = type;
        this.exp = exp;
        this.numberCard = numberCard;
        this.cvv = cvv;
    }

    public Card() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripton() {
        return descripton;
    }

    public void setDescripton(String descripton) {
        this.descripton = descripton;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getNumberCard() {
        return numberCard;
    }

    public void setNumberCard(String numberCard) {
        this.numberCard = numberCard;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}