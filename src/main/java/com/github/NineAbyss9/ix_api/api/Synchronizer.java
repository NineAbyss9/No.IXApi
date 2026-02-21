
package com.github.NineAbyss9.ix_api.api;

public class Synchronizer {
    private Object narrowMessage;
    private String message;

    public Synchronizer() {
    }

    public Synchronizer(String pMessage) {
        message = pMessage;
    }

    public Synchronizer pickKey(Object pKey) {
        this.narrowMessage = pKey;
        return this;
    }

    public void pickMessage(String s) {
        this.message = s;
    }

    public String getMessage() {
        return message;
    }

    public float getFloatMessage() {
        return (float)narrowMessage;
    }

    public double getDoubleMessage() {
        return (double)narrowMessage;
    }

    public int getIntMessage() {
        return (int)narrowMessage;
    }
}
