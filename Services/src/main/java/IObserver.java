import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface IObserver  extends Remote {
    void notificareParticipanti(Set<String> participanti) throws ServiceException, RemoteException;
    void notificareNumereProprii(List<Integer> nr) throws ServiceException, RemoteException;
}