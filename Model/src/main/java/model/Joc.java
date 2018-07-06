package model;

import utils.HasId;
import utils.Status;

import java.io.Serializable;

public class Joc implements HasId<Integer>,Serializable {
    private Integer id;
    private String castigator;


    public Joc(String castigator) {
        this.castigator = castigator;
    }

    public String getCastigator() {
        return castigator;
    }

    public void setCastigator(String castigator) {
        this.castigator = castigator;
    }

    public Joc() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        id=integer;
    }
}
