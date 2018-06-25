package model;

import utils.HasId;
import utils.Pair;

import java.io.Serializable;

public class Rezultat implements HasId<Pair<String,Integer>>,Serializable {
    private String idUser;
    private Integer idParticipant;
    private Float nota;

    public Rezultat(String idUser, Integer idParticipant, Float nota) {
        this.idUser = idUser;
        this.idParticipant = idParticipant;
        this.nota = nota;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Integer getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(Integer idParticipant) {
        this.idParticipant = idParticipant;
    }

    public Float getNota() {
        return nota;
    }

    public void setNota(Float nota) {
        this.nota = nota;
    }

    @Override
    public Pair<String, Integer> getId() {
        return new Pair<>(idUser,idParticipant);
    }

    @Override
    public void setId(Pair<String, Integer> stringIntegerPair) {
        this.idUser=stringIntegerPair.getFirst();
        this.idParticipant=stringIntegerPair.getSecond();
    }
}
