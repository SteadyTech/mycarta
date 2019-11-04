package me.muhammadfaisal.mycarta.home.fragment.card.model;

public class Card {
    String name, descripton, type, key, exp;
    int pin, cvv;
    long numberCard;

    public Card(String name, String descripton, String type, Long numberCard, int pin, int cvv, String exp) {
        this.name = name;
        this.descripton = descripton;
        this.type = type;
        this.pin = pin;
        this.cvv = cvv;
        this.numberCard = numberCard;
        this.exp = exp;
    }

    public Card() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public Long getNumberCard() {
        return numberCard;
    }

    public void setNumberCard(Long numberCard) {
        this.numberCard = numberCard;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }
}
