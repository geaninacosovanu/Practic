import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver  extends Remote {
    void notificare() throws ServiceException, RemoteException;
    void notificareAsteptare(String msg) throws ServiceException, RemoteException;
    void notificareStart(String id) throws ServiceException, RemoteException;

}