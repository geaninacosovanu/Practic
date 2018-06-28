import model.Joc;
import model.JocUser;
import model.User;
import repository.IJocRepository;
import repository.IJocUserRepository;
import repository.IUserRepository;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Service implements IService {
    private final int nrThreaduri = 10;
    private IUserRepository userRepository;
    private IJocRepository jocRepository;
    private IJocUserRepository jocUserRepository;
    private Map<String, IObserver> loggedClients;
    private Map<String, Integer> puncte;
    private Map<String, IObserver> players;
    private Map<String, List<Integer>> numereParticipanti;
    private Map<String, List<Integer>> numereServer;
    private boolean started = false;

    public Service(IUserRepository userRepository, IJocRepository jocRepository, IJocUserRepository jocUserRepository) {
        this.userRepository = userRepository;
        this.jocRepository = jocRepository;
        this.jocUserRepository = jocUserRepository;
        this.loggedClients = new ConcurrentHashMap<>();
    }

    private void notificaParticipanti(Map<String, IObserver> all) {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for (IObserver obs : all.values()) {
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    obs.notificareParticipanti(all.keySet());
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });

        }
        executor.shutdown();
    }

    private void notificaNr(Map<IObserver, List<Integer>> all) {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for (IObserver obs : all.keySet()) {
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    obs.notificareNumereProprii(all.get(obs));
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
    public synchronized void logout(User user, IObserver client) {
        loggedClients.remove(user.getId());
        System.out.println("Logout");

    }

    @Override
    public synchronized void start() throws ServiceException {
        if (started)
            throw new ServiceException("Un joc este inceput deja!");
        else {
            started = true;
            numereParticipanti = new ConcurrentHashMap<>();
            numereServer = new ConcurrentHashMap<>();
            puncte = new ConcurrentHashMap<>();
            Map<IObserver, List<Integer>> all = new ConcurrentHashMap<>();
            players = new ConcurrentHashMap<>();
            for (String user : loggedClients.keySet()) {
                puncte.put(user, 0);
                players.put(user, loggedClients.get(user));
                List<Integer> nr = getRandomNumber();
                numereServer.put(user, nr);
                all.put(loggedClients.get(user), nr);
                numereParticipanti.put(user, new ArrayList<>());
            }
            notificaParticipanti(players);
            notificaNr(all);
        }
    }

    public boolean verificaPrimitToti() {
        int size = numereParticipanti.values().iterator().next().size();
        boolean ok = true;

        for (List<Integer> nr : numereParticipanti.values()) {
            if (nr.size() != size)
                ok = false;
        }
        return ok;
    }

    private boolean verificaDone() {
        boolean ok = true;
        for (List<Integer> n : numereServer.values()) {
            if (n.size() != 0)
                ok = false;
        }
        return ok;
    }

    private void updatePuncte() {
        int max = 0;
        int s = 0;
        String user = null;
        for (String u : players.keySet()) {
            Integer n = numereParticipanti.get(u).get(numereParticipanti.get(u).size() - 1);
            if (n > max) {
                max = n;
                user = u;
            }
            s += n;
        }
        int p = puncte.get(user) + s;
        puncte.put(user, p);
    }

    @Override
    public void addNumar(String user, Integer nr) throws ServiceException {
        numereParticipanti.get(user).add(nr);
        if (numereServer.get(user).remove(nr)) {
            if (verificaPrimitToti() && !verificaDone()) {
                Map<String, IObserver> all = new ConcurrentHashMap<>();
                for (String u : players.keySet()) {
                    Integer n = numereParticipanti.get(u).get(numereParticipanti.get(u).size() - 1);
                    all.put(u + " " + n, players.get(u));
                }
                notificaParticipanti(all);
                updatePuncte();
                for (Integer i : puncte.values())
                    System.out.println(i);


                Map<IObserver, List<Integer>> map = new ConcurrentHashMap<>();
                for (String u : players.keySet()) {
                    map.put(players.get(u), numereServer.get(u));
                }
                notificaNr(map);
            } else if (verificaPrimitToti()) {
                updatePuncte();
                Integer max = 0;
                Map<String, IObserver> map = new ConcurrentHashMap<>();
                for (String u : players.keySet()) {
                    if (puncte.get(u) > max)
                        max = puncte.get(u);
                    map.put(u + " " + puncte.get(u), players.get(u));
                }
                notificaParticipanti(map);
                Map<IObserver, List<Integer>> map1 = new ConcurrentHashMap<>();
                for (String u : players.keySet()) {
                    map1.put(players.get(u), numereServer.get(u));
                }
                notificaNr(map1);

                started = false;

                Joc joc = jocRepository.save(new Joc(max));
                for (String u : players.keySet())
                    jocUserRepository.save(new JocUser(u, joc.getId(), numereServer.get(u).toString()));
            }
        } else throw new ServiceException("Numarul nu este in lista primita de server");
    }


    private List<Integer> getRandomNumber() {
        List<Integer> nr = new ArrayList<>();
        nr.add(ThreadLocalRandom.current().nextInt(1, 11));
        nr.add(ThreadLocalRandom.current().nextInt(1, 11));
        nr.add(ThreadLocalRandom.current().nextInt(1, 11));
        return nr;
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