import java.io.Serializable;
import java.util.Date;

public class Trecere implements HasId<Pair<Integer,Integer>>,Serializable {
    private Integer idMasina;
    private Integer punctControl;
    private String ora;

    public Trecere(Integer idMasina, Integer punctControl, String ora) {
        this.idMasina = idMasina;
        this.punctControl = punctControl;
        this.ora = ora;
    }

    public Integer getPunctControl() {
        return punctControl;
    }

    public void setPunctControl(Integer punctControl) {
        this.punctControl = punctControl;
    }

    public Integer getIdMasina() {
        return idMasina;
    }

    public void setIdMasina(Integer idMasina) {
        this.idMasina = idMasina;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    @Override
    public Pair<Integer, Integer> getId() {
        return new Pair<>(punctControl, idMasina);
    }

    @Override
    public void setId(Pair<Integer, Integer> stringIntegerPair) {
        this.punctControl=stringIntegerPair.getFirst();
        this.idMasina =stringIntegerPair.getSecond();
    }
}
