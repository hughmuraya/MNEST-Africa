package com.mnestafrica.android.models;

public class WalletTransaction {

    private int id;
    private int wallet_id;
    private String amount;
    private String previous_balance;
    private String transaction_type;
    private String source;
    private String trx_id;
    private String narration;
    private String created_at;
    private String updated_at;


    public WalletTransaction(int id, int wallet_id,String amount, String previous_balance, String transaction_type, String source, String trx_id, String narration, String created_at, String updated_at) {

        this.id = id;
        this.wallet_id = wallet_id;
        this.amount = amount;
        this.previous_balance = previous_balance;
        this.transaction_type = transaction_type;
        this.source = source;
        this.trx_id = trx_id;
        this.narration = narration;
        this.created_at = created_at;
        this.updated_at = updated_at;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrevious_balance() {
        return previous_balance;
    }

    public void setPrevious_balance(String previous_balance) {
        this.previous_balance = previous_balance;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTrx_id() {
        return trx_id;
    }

    public void setTrx_id(String trx_id) {
        this.trx_id = trx_id;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

}

