import model.Joc;
import model.User;
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
    private Map<String, IObserver> loggedClients;
    private Map<String, IObserver> jucatoriAsteptare;
    private List<Joc> jocuri;

    public Service(IUserRepository userRepository) {
        this.userRepository = userRepository;
        loggedClients = new ConcurrentHashMap<>();
        jucatoriAsteptare = new ConcurrentHashMap<>();
        jocuri = new ArrayList<>();
    }

    private void notificaAsteptare(String msg, List<IObserver> u) {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for (IObserver obs : u) {
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    obs.notificareAsteptare(msg);
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });

        }
        executor.shutdown();
    }

    private void notificaStart(String msg, List<IObserver> u) {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    u.get(0).notificareStart(msg,1);
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });

            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    u.get(1).notificareStart(msg,0);
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
    public synchronized void start(User u, IObserver client) {
        List<IObserver> all=new ArrayList<>();
        if (jucatoriAsteptare.keySet().size() != 0) {
            int i =-1;
            String user=null;
            int randomNum = ThreadLocalRandom.current().nextInt(0, jucatoriAsteptare.keySet().size());
            Iterator<String> it = jucatoriAsteptare.keySet().iterator();
            while (it.hasNext() && i<randomNum) {
                user = it.next();
                i++;
            }

            IObserver o =jucatoriAsteptare.remove(user);
            jocuri.add(new Joc(u.getId(), user));
            all.add(client);
            all.add(o);
            notificaStart(user+":X "+u.getId()+":O ", all);
        } else {
            jucatoriAsteptare.put(u.getId(),client);
            all.add(client);
            notificaAsteptare("Sunteti in asteptare. Veti fi notificat cand va exista un jucator!", all);
        }
    }

    @Override
    public void muta(User user, Integer i, Integer j, Integer semn) {
        for (Joc joc:jocuri){
            if(joc.getIdUser1().equals(user.getId()) || joc.getIdUser2().equals(user.getId())){
                Integer[][] matrix=joc.getMatrix();
                if(matrix[i][j]==-1)
                    matrix[i][j]=semn;
                List<IObserver> all= new ArrayList<>();
                all.add(loggedClients.get(joc.getIdUser1()));
                all.add(loggedClients.get(joc.getIdUser1()));
                notificaMutare(all,i,j,semn);
            }
        }
    }

    private void notificaMutare(List<IObserver> all, Integer i, Integer j,Integer semn) {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for (IObserver obs : all) {
            executor.execute(() -> {
                System.out.println("Notifying client...");
                try {
                    System.out.println("Notificare adaugata!");
                    obs.notificareMutare(i,j,semn);
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