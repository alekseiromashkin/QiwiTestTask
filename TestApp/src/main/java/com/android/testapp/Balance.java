package com.android.testapp;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

@SuppressWarnings("unused")
public class Balance extends Item {

    private String currency;
    private BigDecimal amount;

    public Balance(int id, String currency, BigDecimal amount) {
        this.id = id;
        this.currency = currency;
        this.amount = amount;
    }

    @Override
    public String toString() {

        Currency cur = Currency.getInstance(this.currency);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setCurrency(cur);

        return numberFormat.format(this.amount);

    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Balance && this.id == (((Balance) object).getId());
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

}
