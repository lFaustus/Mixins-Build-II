package com.faustus.mixins.build2;

/**
 * Created by flux on 6/19/15.
 */
public enum Bottle {
    BOTTLE1(1),
    BOTTLE4(4),
    BOTTLE2(2),
    BOTTLE5(5),
    BOTTLE3(3),
    BOTTLE6(6);

    private final int mBottleValue;

    Bottle(int val) {
        mBottleValue = val;
    }

    public int getBottleValue() {
        return mBottleValue;
    }

}
