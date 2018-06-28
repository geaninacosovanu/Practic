import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver  extends Remote {
    void notificareAsteptare(String msg) throws ServiceException, RemoteException;
    void notificareStart(String msg, Integer semn) throws ServiceException, RemoteException;
    void notificareMutare(Integer i, Integer j, Integer semn)throws ServiceException, RemoteException;
}