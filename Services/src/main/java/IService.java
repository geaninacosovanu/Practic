import java.util.List;

public interface IService {
    boolean login(String username, String parola,IObserver client) throws ServiceException;
    User getUser(String username);
    void logout(User user, IObserver client) throws ServiceException;
    List<MasinaDTO> getMasini(Integer punct);

    List<Masina> getMasiniNetrecute(Integer punctControl);
    void add(Integer idMasina,Integer punct,String ora);
}
