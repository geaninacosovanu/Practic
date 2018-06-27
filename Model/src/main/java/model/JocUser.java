package model;

import utils.HasId;
import utils.Pair;

import java.io.Serializable;

public class JocUser implements HasId<Pair<String,Integer>>,Serializable {
    private String idUser;
    private Integer idJoc;
    private String litere;

    public JocUser(String idUser, Integer idJoc, String litere) {
        this.idUser = idUser;
        this.idJoc = idJoc;
        this.litere = litere;
    }


    public String getLitere() {
        return litere;
    }

    public void setLitere(String litere) {
        this.litere = litere;
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

    public void setIdJoc(Integer id) {
        this.idJoc = id;
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
