public interface IService {
    boolean login(String username, String parola,IObserver client) throws ServiceException;
    void logout(User user, IObserver client) throws ServiceException;
}
