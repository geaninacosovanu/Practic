package repository;

import model.JocUser;
import utils.DTO;
import utils.Pair;

public interface IJocUserRepository extends IRepository<Pair<String,Integer>, JocUser> {
    DTO getDTO(Integer idJoc, String user);
}
