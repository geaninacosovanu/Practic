import java.io.Serializable;

public class Participant implements HasId<Integer>,Serializable {
    private Integer id;
    private String nume;
    private Status status;

    public Participant(String nume, Status status) {
        this.nume = nume;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
