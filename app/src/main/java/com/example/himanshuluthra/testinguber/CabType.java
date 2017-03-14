package com.example.himanshuluthra.testinguber;

/**
 * Created by himanshuluthra on 07/03/17.
 */

public enum CabType {
    UBER("Uber"),
    OLA("Ola");

    String mValue;
    CabType(String value) {
        mValue = value;
    }
    public String getValue() {
        return mValue;
    }
}
