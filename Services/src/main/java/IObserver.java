import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IObserver  extends Remote {
    void notificareMutare(Integer pozitie, String[] joc) throws ServiceException, RemoteException;
    void notificareAsteptare(String msg) throws ServiceException, RemoteException;
    void notificareStart(String id) throws ServiceException, RemoteException;

}