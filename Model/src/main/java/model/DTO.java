package model;

import java.io.Serializable;
import java.util.List;

public class DTO implements Serializable {
    private Integer idJoc;
    private List<JocUser> jucatori;
    private Integer punctaj;

    public DTO(Integer idJoc, List<JocUser> jucatori, Integer punctaj) {
        this.idJoc = idJoc;
        this.jucatori = jucatori;
        this.punctaj = punctaj;
    }

    public Integer getIdJoc() {
        return idJoc;
    }

    public void setIdJoc(Integer idJoc) {
        this.idJoc = idJoc;
    }

    public List<JocUser> getJucatori() {
        return jucatori;
    }

    public void setJucatori(List<JocUser> jucatori) {
        this.jucatori = jucatori;
    }

    public Integer getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(Integer punctaj) {
        this.punctaj = punctaj;
    }
}
