package me.muhammadfaisal.mycarta.v2.model.firebase;

public class PinModel {
    String key, pin;

    public PinModel() {
    }

    public PinModel(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
