import model.Cuvant;
import model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver  extends Remote {
    void notificareStart(Cuvant c) throws ServiceException, RemoteException;
    void notificareMutare(Cuvant c,Character litera) throws ServiceException, RemoteException;
    void notificaGresit(Character litera) throws RemoteException;
    void notificaCastigator(User u) throws RemoteException;
    void notificaPierdere() throws RemoteException;
}