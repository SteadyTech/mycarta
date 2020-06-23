package me.muhammadfaisal.mycarta.v1.home.fragment.money.model;

public class MoneyManager{
    String name, category;
    String date, description, key;
    Long income, expense, totalMoney;

    public MoneyManager(String name, String category, String date, Long income,Long expense , String description) {
        this.name = name;
        this.category = category;
        this.date = date;
        this.income = income;
        this.expense = expense;
        this.description = description;
    }

    public MoneyManager() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getExpense() {
        return expense;
    }

    public void setExpense(Long expense) {
        this.expense = expense;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getIncome() {
        return income;
    }

    public void setIncome(Long income) {
        this.income = income;
    }

    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }
}
