import model.User;

public interface IService {
    boolean login(String username, String parola,IObserver client) throws ServiceException;
    User getUser(String username);
    void logout(User user, IObserver client) throws ServiceException;
    void start() throws ServiceException;
    void addNumar(String user, Integer nr) throws ServiceException;
}
