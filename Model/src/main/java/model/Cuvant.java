package model;

import utils.HasId;
import utils.Status;

import java.io.Serializable;

public class Cuvant implements HasId<Integer>,Serializable {
    private Integer id;
    private String cuvant;

    public Cuvant() {
    }

    public Cuvant(Integer id, String cuvant) {
        this.id = id;
        this.cuvant = cuvant;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCuvant() {
        return cuvant;
    }

    public void setCuvant(String cuvant) {
        this.cuvant = cuvant;
    }
}
