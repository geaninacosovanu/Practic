import java.io.Serializable;

public class Masina implements HasId<Integer>,Serializable {
    private Integer id;
    private String nume;

    public Masina(Integer id, String nume) {
        this.id = id;
        this.nume = nume;
    }

    public Masina() {
    }


    public String getNume() {
        return nume;
    }


    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        id=integer;
    }

    @Override
    public String toString() {
        return id+" : "+nume;
    }
}
