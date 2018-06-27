package model;

import utils.HasId;

import java.io.Serializable;

public class Joc implements HasId<Integer>,Serializable{
    private Integer id;
    private Integer idCuvant;

    public Joc(Integer id, Integer idCuvant) {
        this.id = id;
        this.idCuvant = idCuvant;
    }

    public Joc(Integer idCuvant) {
        this.idCuvant = idCuvant;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCuvant() {
        return idCuvant;
    }

    public void setIdCuvant(Integer idCuvant) {
        this.idCuvant = idCuvant;
    }
}
