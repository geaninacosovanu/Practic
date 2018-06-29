import model.User;
import repository.IUserRepository;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IService {
    private final int nrThreaduri = 10;
    String[] pozitii;
    Map<String, Integer> pozitiiCurente;
    private IUserRepository userRepository;
    private Map<String, IObserver> loggedClients;
    private Map<String, IObserver> players = new ConcurrentHashMap<>();
    private boolean started = false;
    private int nrJocuri;

    public Service(IUserRepository userRepository) {
        this.userRepository = userRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    private void notificaMutare(Integer p) {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for (IObserver obs : players.values()) {
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    obs.notificareMutare(p, pozitii);
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });

        }
        executor.shutdown();
    }

    private void notificaAsteptare(IObserver client) {
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

    private void notificaStart(IObserver client, String id) {
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
    public synchronized void logout(User user, IObserver client) {
        loggedClients.remove(user.getId());
        System.out.println("Logout");

    }

    @Override
    public synchronized void start(User user, IObserver client) {
        if (players.keySet().size() < 2 && started == false) {
            players.put(user.getId(), client);
        }
        if (players.keySet().size() == 2 && started == false) {
            pozitii = new String[9];
            for (int i = 0; i < 9; i++)
                pozitii[i] = null;
            started = true;
            Iterator<IObserver> iterator = players.values().iterator();
            IObserver o1 = iterator.next();
            IObserver o2 = iterator.next();
            Iterator<String> it = players.keySet().iterator();
            String id1 = it.next();
            String id2 = it.next();
            notificaStart(o1, id2);
            notificaStart(o2, id1);
            pozitiiCurente = new ConcurrentHashMap<>();
            pozitiiCurente.put(id1, -1);
            pozitiiCurente.put(id2, -1);
            nrJocuri = 0;
        } else if (started) {
            notificaAsteptare(client);
        }
    }

    @Override
    public synchronized Integer addPozitie(User u, Integer nr, Integer curent) {
        Integer p=null;
        if (nrJocuri < 6) {
            if (pozitii[nr + curent] == null) {
                p = nr + curent;
                pozitiiCurente.put(u.getId(), nr + curent);
                pozitii[nr + curent] = u.getId();
            } else {
                p = findPrimaLibera();
                pozitii[p] = u.getId();
            }
            notificaMutare(nr);
            nrJocuri++;
        }
        if(nrJocuri==6){
            int i=8;
            String c=null;
            while(i>=0) {
                if (pozitii[i] != null) {
                    c = pozitii[i];
                    i = -1;
                } else
                    i--;
            }
            notificaCastig(c);}

        return p;

    }

    private void notificaCastig(String c) {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for (IObserver obs : players.values()) {
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    obs.notificareAsteptare("Juctot castigator "+c);
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });

        }
        executor.shutdown();
    }

    @Override
    public synchronized boolean canMutare(User u, String oponent) {
        int us = 0;
        int nrB = 0;

        for (int i = 0; i < 9; i++) {
            if (pozitii[i] != null) {
                if (pozitii[i].equals(u.getId()))
                    us++;
                if (pozitii[i].equals(oponent))
                    nrB++;
            }
        }
        if (nrB == us)
            return true;
        if (us == nrB - 1)
            return true;
        else return false;
    }


    private Integer findPrimaLibera() {
        int i = 0;
        while (pozitii[i] != null) {
            i++;
        }
        return i;
    }

    @Override
    public synchronized boolean login(String username, String parola, IObserver client) {
        loggedClients.put(username, client);
        System.out.println("Login");
        return userRepository.exists(new User(username, parola));
    }

    @Override
    public synchronized User getUser(String username) {
        return userRepository.findOne(username);
    }

}