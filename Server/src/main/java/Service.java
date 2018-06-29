

import model.User;
import repository.IUserRepository;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IService {
    private IUserRepository userRepository;
    private Map<String, IObserver> loggedClients;
    private Map<String, IObserver> players=new ConcurrentHashMap<>();
    private boolean started=false;

    private final int nrThreaduri = 10;

    public Service(IUserRepository userRepository) {
        this.userRepository = userRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    private void notifica() {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for(IObserver obs:loggedClients.values()){
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    obs.notificare();
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });

        }
        executor.shutdown();
    }

    private void notificaAsteptare(IObserver client){
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    client.notificareAsteptare("Un joc e inceput!");
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });


        executor.shutdown();
    }
    private void notificaStart(IObserver client, String id){
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        executor.execute(() -> {
            System.out.println("Notifying client...");
            try {
                System.out.println("Notificare adaugata!");
                client.notificareStart(id);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });


        executor.shutdown();
    }

    @Override
    public synchronized void logout(User user, IObserver client)  {
        loggedClients.remove(user.getId());
        System.out.println("Logout");

    }

    @Override
    public synchronized void start(User user, IObserver client) {
        if(players.keySet().size()<2 && started==false){
            players.put(user.getId(),client);
        }
        if(players.keySet().size()==2 && started==false){
            started=true;
            Iterator<IObserver> iterator = players.values().iterator();
            iterator.hasNext() ;
            IObserver o1 = iterator.next();
            iterator.hasNext() ;
            IObserver o2 = iterator.next();
            Iterator<String> it = players.keySet().iterator();
            iterator.hasNext() ;
            String id1 = it.next();
            iterator.hasNext() ;
            String id2 = it.next();
            notificaStart(o1,id2);
            notificaStart(o2,id1);
        }
        else if(started)
        {
            notificaAsteptare(client);
        }
    }

    @Override
    public synchronized boolean login(String username, String parola,IObserver client) {
        loggedClients.put(username, client);
        System.out.println("Login");
        return userRepository.exists(new User(username, parola));
    }

    @Override
    public synchronized User getUser(String username) {
        return userRepository.findOne(username);
    }

}