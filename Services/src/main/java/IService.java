import model.Participant;
import model.User;

import java.util.List;

public interface IService {
    boolean login(String username, String parola,IObserver client) throws ServiceException;
    void logout(User user, IObserver client) throws ServiceException;
    List<ParticipantDTO> getAllParticipanti();
    List<Participant> getParticipantiUndone();

    void saveNota(Integer id, String id1, Float nota) throws ServiceException;
}
