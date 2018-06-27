package model;

import utils.HasId;
import utils.Pair;

import java.io.Serializable;

public class Joc implements HasId<Pair<String,String>>,Serializable {
    private String idUser1;
    private String idUser2;

    public Joc(String idUser1, String idUser2) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
    }

    public String getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(String idUser1) {
        this.idUser1 = idUser1;
    }

    public String getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(String idUser2) {
        this.idUser2 = idUser2;
    }



    @Override
    public Pair<String, String> getId() {
        return new Pair<>(idUser1,idUser2);
    }

    @Override
    public void setId(Pair<String, String> stringIntegerPair) {
        this.idUser1=stringIntegerPair.getFirst();
        this.idUser2=stringIntegerPair.getSecond();
    }
}
