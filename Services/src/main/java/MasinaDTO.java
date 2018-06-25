import java.io.Serializable;
import java.util.Date;

public class MasinaDTO implements Serializable {
    private Masina masina;
    private Integer punctControl;
    private String ora;

    public MasinaDTO(Masina masina, Integer punctControl, String ora) {
        this.masina = masina;
        this.punctControl = punctControl;
        this.ora = ora;
    }

    public Masina getMasina() {
        return masina;
    }

    public void setMasina(Masina masina) {
        this.masina = masina;
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

    public String getNume() {
        return masina.getNume();
    }



    public Integer getId() {
        return masina.getId();
    }


}
