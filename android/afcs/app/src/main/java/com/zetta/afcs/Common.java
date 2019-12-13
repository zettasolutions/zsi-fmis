package com.zetta.afcs;

public class Common {
    public enum Discount {
        REGULAR("Regular", 0),
        STUDENT("Student", 1),
        SENIOR("Senior", 2),
        PWD("PWD", 3);

        private String stringValue;
        private int intValue;
        Discount(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }
}
