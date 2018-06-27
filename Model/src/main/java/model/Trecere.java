package model;

import utils.HasId;
import utils.Pair;

import java.io.Serializable;

public class Trecere implements HasId<Pair<Integer,Integer>>,Serializable {
    private Integer punctControl;
    private Integer idCopil;
    private String ora;


    public Trecere(Integer punctControl, Integer idCopil, String ora) {
        this.punctControl = punctControl;
        this.idCopil = idCopil;
        this.ora = ora;
    }

    public Integer getPunctControl() {
        return punctControl;
    }

    public void setPunctControl(Integer punctControl) {
        this.punctControl = punctControl;
    }

    public Integer getIdCopil() {
        return idCopil;
    }

    public void setIdCopil(Integer idCopil) {
        this.idCopil = idCopil;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    @Override
    public Pair<Integer, Integer> getId() {
        return new Pair<>(punctControl,idCopil);
    }

    @Override
    public void setId(Pair<Integer, Integer> stringIntegerPair) {
        this.punctControl=stringIntegerPair.getFirst();
        this.idCopil=stringIntegerPair.getSecond();
    }
}
