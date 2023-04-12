package io.contractdeployer.generics.irc31;

import static io.contractdeployer.generics.irc31.Constant.TAG;

public class Message {

    public static class Found{

        public static String zeroAddr(String type) {
            return TAG + " :: "+type +" cannot be Zero Address.";

        }
    }

    public static class Not{

        public static String enoughBalance() {
            return TAG+" :: Not enough balance";
        }

        public static String owner() {
            return TAG+" :: Only owner can perform this action.";
        }

        public static String admin() {
            return TAG+" :: Only admin/owner can perform this action.";
        }

        public static String sameSize(String arr1, String arr2) {
            return TAG + " :: "+arr1+" and "+arr2+" arrays do not have the same length.";
        }

        public static String operatorApproved() {
            return TAG + " :: Need operator approval for 3rd party transfers.";
        }

    }

    public static class Exceeded{

        public static String cap() {
            return TAG+" :: Cap Exceeded.";
        }

        public static String nftCountPerTxRange() {
            return TAG+" :: NFT count per transaction exceeded.";
        }
    }

    public static String greaterThanZero(String type) {
        return TAG+" :: "+type+" must be greater than zero.";
    }
    public static String empty(String type) {
        return TAG+" :: "+type+" is null or empty.";
    }

}
