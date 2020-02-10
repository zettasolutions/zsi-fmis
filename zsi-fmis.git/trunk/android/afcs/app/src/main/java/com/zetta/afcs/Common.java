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

    public static class BundleExtras {
        public static String Username = "username";
        public static String IsAuthenticated = "isAuthenticated";
        public static String FirstName = "firstName";
        public static String LastName = "lastName";
        public static String Position = "position";
        public static String Driver = "driver";
        public static String Pao = "pao";
    }
}
