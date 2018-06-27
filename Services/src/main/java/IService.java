import model.Cuvant;
import model.Joc;
import model.User;

public interface IService {
    boolean login(String username, String parola,IObserver client) throws ServiceException;
    User getUser(String username);
    void logout(User user, IObserver client) throws ServiceException;
    boolean start();
    void addLitera(User user,Cuvant c,Character s);
    void addJocJucator(String user,Integer idJoc,String litere);
    Joc addJoc();
}
