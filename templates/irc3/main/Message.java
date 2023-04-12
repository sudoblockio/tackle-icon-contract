package io.contractdeployer.generics.irc3;

import static io.contractdeployer.generics.irc3.Constant.TAG;

public class  Message {

    public static class Found{

        public static String zeroAddr(String type) {
            return TAG + " :: "+type +" cannot be Zero Address.";

        }

        public static String token() {
            return TAG + " :: "+" Token already exists.";

        }

        public static String negative(String value) {
            return TAG + " :: "+ value+" cannot be negative.";

        }

        public static String zeroNeg(String value) {
            return TAG + " :: "+ value+" cannot be zero or negative.";

        }
    }

    public static class Not{

        public static String owner() {
            return TAG+" :: Only owner can perform this action.";
        }

        public static String tokenOwner() {
            return TAG+" :: Only token owner can perform this action.";
        }

        public static String admin() {
            return TAG+" :: Only admin/owner can perform this action.";
        }

        public static String operatorApproved() {
            return TAG + " :: Need operator approval for 3rd party transfers.";
        }

    }

    public static class Exceeded{

        public static String cap() {
            return TAG+" :: Cap Exceeded.";
        }

    }

    public static String noToken() {
        return TAG+" :: "+" Non-existent token.";
    }

    public static String ownerApproval() {
        return TAG+" :: "+" Cannot approve owner.";
    }

    public static String noOwnerTokens() {
        return TAG+" :: "+" No tokens exist for the owner.";
    }

    public static String empty(String type) {
        return TAG+" :: "+type+" is null or empty.";
    }

    public static String priceMismatch() {
        return TAG+" :: "+" Price Mismatch.";
    }


}
