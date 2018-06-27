import model.Copil;
import model.CopilDTO;
import model.User;

import java.util.List;


public interface IService {
    boolean login(String username, String parola,IObserver client) throws ServiceException;
    User getUser(String username);
    void logout(User user, IObserver client) throws ServiceException;
    List<CopilDTO> getAllCopii(Integer punct);
    List<Copil> getAllCopil(Integer punct);
    void add(Integer id, Integer punctControl, String s) throws ServiceException;
}
