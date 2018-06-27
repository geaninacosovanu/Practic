package model;

import java.io.Serializable;

public class CopilDTO implements Serializable {
    private Copil copil;
    private Integer punctControl;
    private String ora;

    public CopilDTO(Copil copil, Integer punctControl, String ora) {
        this.copil = copil;
        this.punctControl = punctControl;
        this.ora = ora;
    }

    public String getNume() {
        return copil.getNume();
    }

    public Integer getId() {
        return copil.getId();
    }

    public Copil getCopil() {
        return copil;
    }

    public void setCopil(Copil copil) {
        this.copil = copil;
    }

    public Integer getPunctControl() {
        return punctControl;
    }

    public void setPunctControl(Integer punctControl) {
        this.punctControl = punctControl;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }
}
