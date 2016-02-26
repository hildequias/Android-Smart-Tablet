package com.pixonsoft.crud_financeiro.model;

import java.util.Date;

/**
 * Created by mobile6 on 1/26/16.
 */
public class Finance {

    private int _id;
    private String description;
    private String date;
    private String type_finance;
    private Double value;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType_finance() {
        return type_finance;
    }

    public void setType_finance(String type_finance) {
        this.type_finance = type_finance;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
