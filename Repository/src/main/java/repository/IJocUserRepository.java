package repository;

import model.JocUser;
import utils.Pair;

import java.util.List;

public interface IJocUserRepository extends IRepository<Pair<String,Integer>, JocUser> {
    List<JocUser> getDetalii(Integer idJoc,String user);
}