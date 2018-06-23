import model.Participant;

import java.io.Serializable;

public class ParticipantDTO implements Serializable {
    Participant p;
    String status;

    public ParticipantDTO(Participant p, String status) {
        this.p = p;
        this.status = status;
    }

    public Participant getP() {
        return p;
    }

    public void setP(Participant p) {
        this.p = p;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Integer getId(){
        return p.getId();
    }

    public String getNume(){
        return p.getNume();
    }
}
