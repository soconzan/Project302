package org.example.project302.product.entity;

import lombok.Getter;

@Getter
public enum DealStatus {
    SELL(1),
    RESV(2),
    DEP(2),
    BUY(2),
    SEND(2),
    SOLD(3),
    CANC(-1),
    HIDE(0);

    private final int value;

    DealStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
