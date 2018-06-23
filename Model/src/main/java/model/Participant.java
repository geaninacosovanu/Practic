import java.io.Serializable;

public class Participant implements HasId<Integer>,Serializable {
    private Integer id;
    private String nume;

    public Participant(String nume) {
        this.nume = nume;
    }

    public Participant() {
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
        return id + " : "+nume;
    }
}
