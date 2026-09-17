package com.flamingo.qa.model.ui;

/** Maps to the "gender-radio-N" input ids on the DemoQA practice form. */
public enum Gender {
    MALE(1),
    FEMALE(2),
    OTHER(3);

    private final int radioIndex;

    Gender(int radioIndex) {
        this.radioIndex = radioIndex;
    }

    public int getRadioIndex() {
        return radioIndex;
    }
}
