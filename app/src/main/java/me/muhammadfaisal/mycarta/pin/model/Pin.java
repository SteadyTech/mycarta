package me.muhammadfaisal.mycarta.pin.model;

public class Pin {
    String key, pin;

    public Pin() {
    }

    public Pin(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
