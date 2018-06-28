package model;

import org.omg.PortableInterceptor.INACTIVE;
import utils.HasId;
import utils.Status;
import java.io.Serializable;

public class Joc implements HasId<Integer>,Serializable {

    private Integer id;
    private Integer punctaj;

    public Joc() {
    }

    public Joc(Integer id, Integer punctaj) {
        this.id = id;
        this.punctaj = punctaj;
    }

    public Joc(Integer punctaj) {
        this.punctaj = punctaj;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(Integer punctaj) {
        this.punctaj = punctaj;
    }
}
