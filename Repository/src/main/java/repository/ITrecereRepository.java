package repository;

import model.Copil;
import model.CopilDTO;
import model.Trecere;
import utils.Pair;

import java.util.List;

public interface ITrecereRepository extends IRepository<Pair<Integer,Integer>, Trecere> {
    Trecere getByCopil(Integer id);
    List<CopilDTO> getCopiiByPunct(Integer punct);
    List<Copil> getCopiiNetrecuti(Integer punct);

}
