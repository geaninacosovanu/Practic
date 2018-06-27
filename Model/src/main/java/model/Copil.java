package model;

import utils.HasId;
import utils.Status;

import java.awt.font.NumericShaper;
import java.io.Serializable;

public class Copil implements HasId<Integer>,Serializable {
    private Integer id;
    private String nume;

    public Copil(Integer id, String nume) {
        this.id = id;
        this.nume = nume;
    }

    public Copil() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public String toString() {
        return id+" : "+ nume;
    }
}
