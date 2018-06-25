import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver  extends Remote {
    void notificare() throws ServiceException, RemoteException;
}