import model.Cuvant;
import model.Joc;
import model.JocUser;
import model.User;
import repository.ICuvantRepository;
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
    private boolean inceput = false;
    private IUserRepository userRepository;
    private IJocRepository jocRepository;
    private IJocUserRepository jocUserRepository;
    private ICuvantRepository cuvantRepository;
    private Map<String, IObserver> loggedClients;
    private List<IObserver> jucatori;
    private Cuvant cuvant;
    private Integer gresite;
    private Joc currentJoc;

    public Service(IUserRepository userRepository, IJocRepository jocRepository, IJocUserRepository jocUserRepository, ICuvantRepository cuvantRepository) {
        this.userRepository = userRepository;
        this.jocRepository = jocRepository;
        this.jocUserRepository = jocUserRepository;
        this.cuvantRepository = cuvantRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    private  void notifica(Cuvant c) {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for (IObserver obs : loggedClients.values()) {
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    jucatori.add(obs);
                    obs.notificareStart(c);
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });

        }
        executor.shutdown();
    }

    private  void notificaMutare(Cuvant c, Character litera) {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for (IObserver obs : jucatori) {
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    obs.notificareMutare(c, litera);
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });

        }
        executor.shutdown();
    }

    private  void notificaGresit(Character c) {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for (IObserver obs : jucatori) {
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    obs.notificaGresit(c);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });

        }
        executor.shutdown();
    }

    private  void notificaPierdere() {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for (IObserver obs : jucatori) {
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    obs.notificaPierdere();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });

        }
        executor.shutdown();
    }

    private  void notificaCastigare(User u) {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for (IObserver obs : jucatori) {
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    obs.notificaCastigator(u);
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
        jucatori.remove(user);
        System.out.println("Logout");

    }

    public synchronized void addJocJucator(String user,Integer idJoc,String litere)
    {
        jocUserRepository.save(new JocUser(user,idJoc,litere));
    }

    public synchronized Joc addJoc() {
        currentJoc=jocRepository.save(new Joc(cuvant.getId()));
        return currentJoc;
    }
    @Override
    public synchronized boolean start() {
        boolean in = inceput;
        if (!inceput) {
            List<Cuvant> all = cuvantRepository.findAll();
            Integer randomNum = ThreadLocalRandom.current().nextInt(0, all.size());
            cuvant = all.get(randomNum);

            String s = "";
            for (int i = 0; i < cuvant.getCuvant().length(); i++)
                s += "_";
            notifica(new Cuvant(cuvant.getId(), s));
            jucatori = new ArrayList<>();
            inceput = true;
            gresite = 0;
        }
        return in;
    }

    @Override
    public synchronized void addLitera(User user, Cuvant c, Character s) {
        String newCuvant = "";
        System.out.println(c.getCuvant());
        System.out.println(cuvant.getCuvant());
        for (int i = 0; i < c.getCuvant().length(); i++) {
            if (s == cuvant.getCuvant().charAt(i)) {
                newCuvant += s;
            } else
                newCuvant += c.getCuvant().charAt(i);
        }
        if (newCuvant.equals(c.getCuvant()) && gresite < 6) {
            notificaGresit(s);
            gresite++;
        } else {
            notificaMutare(new Cuvant(cuvant.getId(), newCuvant), s);
        }
        if (newCuvant.equals(cuvant.getCuvant()) && gresite < 6) {
            notificaCastigare(user);
            inceput=false;
        }
        if (gresite == 6) {
            inceput=false;
            notificaPierdere();
        }
//        else if (newCuvant.equals(cuvant.getCuvant()) && gresite<6)
//            notificaCastigare(user);
//        else if(gresite<6)
//            notificaMutare(new Cuvant(cuvant.getId(), newCuvant), s);
//        else
//            notificaGresit();
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
//    @Override
//    public synchronized List<ProbaDTO> getAllProba() {
//        List<ProbaDTO> all = new LinkedList<>();
//        probaRepository.findAll().forEach(e -> all.add(new ProbaDTO(e, inscriereRepository.getNrParticipantiProba(e.getId()))));
//        System.out.println("Get all probe");
//
//        return all;
//    }
//
//
//    @Override
//    public synchronized List<ParticipantProbeDTO> getParticipanti(Integer idProba) {
//        List<ParticipantProbeDTO> all = new LinkedList<>();
//        List<Proba> probe;
//        for (model.Participant e : inscriereRepository.getParticipantiPtProba(idProba)) {
//            probe = new ArrayList<>();
//            for (Proba p : inscriereRepository.getProbePtParticipant(e.getId()))
//                probe.add(p);
//            all.add(new ParticipantProbeDTO(e, probe));
//        }
//        System.out.println("Get participanti");
//        return all;
//    }
//
//    @Override
//    public synchronized void saveInscriere(String nume, Integer varsta, List<Proba> probe,boolean existent) throws InscriereServiceException {
//        model.Participant p = null;
//        if (existent == false && getParticipant(nume, varsta) == null) {
//            Integer id;
//            Random rand = new Random();
//            do {
//                id = rand.nextInt(200) + 1;
//            } while (participantRepository.findOne(id.toString()) != null);
//            p = new model.Participant(id.toString(), nume, varsta);
//            try {
//                participantRepository.save(p);
//            } catch (ValidationException e) {
//                throw new InscriereServiceException(e.getMessage());
//            }
//
//        } else if (existent == true && getParticipant(nume, varsta) != null)
//            p = getParticipant(nume, varsta);
//        else if (existent == true && getParticipant(nume, varsta) == null)
//            throw new InscriereServiceException("Participantul nu exista!");
//
//
//        for (Proba pr : probe) {
//            try {
//                inscriereRepository.save(new Inscriere(p.getId(), pr.getId()));
//                System.out.println("Adaugare inscriere");
//
//            } catch (ValidationException e) {
//                throw new InscriereServiceException(e.getMessage());
//            } finally {
//                notifyInscriereAdded();
//            }
//        }
//
//    }
//
//
//
//
//    public synchronized model.Participant getParticipant(String nume, Integer varsta) {
//        return participantRepository.getParticipant(nume, varsta);
//    }
    /*private IParticipantRepository participantRepository;
    private IProbaRepository probaRepository;
    private IInscriereRepository inscriereRepository;
    private repository.IUserRepository userRepository;
    //private List<Observer<Inscriere>> inscriereObservers = new ArrayList<>();

    public InscriereService(IParticipantRepository participantRepository, IProbaRepository probaRepository, IInscriereRepository inscriereRepository, repository.IUserRepository userRepository) {
        this.participantRepository = participantRepository;
        this.probaRepository = probaRepository;
        this.inscriereRepository = inscriereRepository;
        this.userRepository = userRepository;
    }


    public boolean login(String username, String parola) {
        model.User user = new model.User(username, parola);
        return userRepository.exists(user);
    }

    public List<Proba> getAllProba() {
        List<Proba> all = new LinkedList<>();
        probaRepository.findAll().forEach(e -> all.add(e));
        return all;
    }

    public List<model.Participant> getAllParticipant() {
        List<model.Participant> all = new LinkedList<>();
        participantRepository.findAll().forEach(e -> all.add(e));
        return all;
    }

    public Integer getNrParticipantiProba(Integer idproba) {
        return inscriereRepository.getNrParticipantiProba(idproba);
    }

    public List<Proba> getProbeParticipant(String idParticipant) {
        List<Proba> all = new LinkedList<>();
        inscriereRepository.getProbePtParticipant(idParticipant).forEach(e -> all.add(e));
        return all;
    }

    public List<model.Participant> getParticipanti(Integer idProba) {
        List<model.Participant> all = new LinkedList<>();
        inscriereRepository.getParticipantiPtProba(idProba).forEach(e -> all.add(e));
        return all;
    }

    public void saveInscriere(String nume, Integer varsta, List<Proba> probe, boolean existent) throws ValidationException {
        model.Participant p = null;
        if (existent == false && getParticipant(nume, varsta) == null) {
            Integer id;
            Random rand = new Random();
            do {
                id = rand.nextInt(200) + 1;
            } while (participantRepository.findOne(id.toString()) != null);
            p = new model.Participant(id.toString(), nume, varsta);
            participantRepository.save(p);

        } else if (existent == true && getParticipant(nume, varsta) != null)
            p = getParticipant(nume, varsta);
        else if (existent == true && getParticipant(nume, varsta) == null)
            throw new utils.RepositoryException("Participantul nu exista!");


        for (Proba pr : probe) {
            try {
                inscriereRepository.save(new Inscriere(p.getId(), pr.getId()));

            } catch (ValidationException e) {
                //ShowMessage.showMessage(Alert.AlertType.ERROR, "Eroare", e.getMessage());
            } finally {
                //notifyObservers();
            }
        }


    }
    public model.Participant getParticipant(String nume, Integer varsta) {
        return participantRepository.getParticipant(nume, varsta);
    }*/
}