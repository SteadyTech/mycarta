package me.muhammadfaisal.mycarta.home.fragment.money.model;

public class MoneyManagerDate implements MoneyManagerViewType {

    private String date;

    public MoneyManagerDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int getType() {
        return TYPE_DATE;
    }
}
