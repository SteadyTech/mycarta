package me.muhammadfaisal.mycarta.pin.model;

public class Pin {
    Long pin;
    String key;

    public Pin() {
    }

    public Pin(Long pin) {
        this.pin = pin;
    }

    public Long getPin() {
        return pin;
    }

    public void setPin(Long pin) {
        this.pin = pin;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
