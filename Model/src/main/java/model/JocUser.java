package model;

import utils.HasId;
import utils.Pair;

import java.io.Serializable;

public class JocUser implements HasId<Pair<String,Integer>>,Serializable {
    private String idUser;
    private Integer idJoc;
    private String numere;


    public JocUser(String idUser, Integer idJoc, String numere) {
        this.idUser = idUser;
        this.idJoc = idJoc;
        this.numere = numere;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Integer getIdJoc() {
        return idJoc;
    }

    public void setIdJoc(Integer idJoc) {
        this.idJoc = idJoc;
    }

    public String getNumere() {
        return numere;
    }

    public void setNumere(String numere) {
        this.numere = numere;
    }

    @Override
    public Pair<String, Integer> getId() {
        return new Pair<>(idUser,idJoc);
    }

    @Override
    public void setId(Pair<String, Integer> stringIntegerPair) {
        this.idUser=stringIntegerPair.getFirst();
        this.idJoc=stringIntegerPair.getSecond();
    }
}
