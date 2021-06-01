package com.mnestafrica.android.dependancies;

public class Constants {

    //AUTH TOKEN
    public static String AUTH_TOKEN = "";
    public static String PASS = "";

    /*ENDPOINT*/
    public static String ENDPOINT = "https://mnestafrica.com/";


    //AUTH
    public static String LOGIN = "auth/token/login/";
    public static String LOGOUT = "auth/token/logout/";
    public static String CURRENT_USER = "auth/users/me/";
    public static String RESET_PASSWORD = "api/reset_password";
    public static String UPDATE_USER = "api/update/user";


    //PROPERTY DETAILS
    public static String CURRENT_UNIT = "api/tenant/current";
    public static String PREVIOUS_UNITS = "api/tenant/previous";
    public static String VACATE_UNIT = "api/tenant/vacate";
    public static String VACANT_UNITS = "api/vacant/within";
    public static String VACANT_APARTMENTS = "api/vacant";
    public static String ENQUIRE_UNIT = "api/vacancy/enquire";


    //WALLET
    public static String WALLET_DETAILS = "api/wallet/details";
    public static String WALLET_TRANSACTIONS = "api/wallet/trans";
    public static String WALLET_TOP_UP = "api/wallet/mpesa/stktopup";
    public static String WALLET_WITHDRAW = "api/wallet/mpesa/withdraw";
    public static String PAY_RENT = "api/inv_pay/wallet";
    public static String PAY_INVOICE = "api/inv_pay/wallet";


    //INVOICES
    public static String OPEN_RENT = "api/rent_invoices/open";
    public static String OPEN_INVOICES = "api/invoices/open";
    public static String CLOSED_RENT = "api/rent_invoices/closed";
    public static String CLOSED_INVOICES = "api/invoices/closed";


    //DOCUMENTS
    public static String DOCUMENT = "api/documents/";

}
