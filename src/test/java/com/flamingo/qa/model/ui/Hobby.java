package com.flamingo.qa.model.ui;

/** Maps to the "hobbies-checkbox-N" input ids on the DemoQA practice form. */
public enum Hobby {
    SPORTS(1),
    READING(2),
    MUSIC(3);

    private final int checkboxIndex;

    Hobby(int checkboxIndex) {
        this.checkboxIndex = checkboxIndex;
    }

    public int getCheckboxIndex() {
        return checkboxIndex;
    }
}
