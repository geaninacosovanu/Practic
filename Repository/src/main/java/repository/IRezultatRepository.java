package repository;

import model.Pair;
import model.Participant;
import model.Rezultat;

import java.util.List;

public interface IRezultatRepository extends repository.IRepository<Pair<String,Integer>, Rezultat> {
    Integer getRezultate(Integer id);
    String getNota(Integer id);
    List<Participant> getParticipantiByUser(String user);
}
