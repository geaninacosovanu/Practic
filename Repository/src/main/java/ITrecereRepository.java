import java.util.List;

public interface ITrecereRepository extends IRepository<Pair<Integer,Integer>,Trecere> {
    Trecere getLastTrecere(Integer idMasina);
    List<MasinaDTO> getMasiniByPunctControl(Integer punctControl);
    List<Masina> getMasiniNetrecute(Integer punctControl);
}
