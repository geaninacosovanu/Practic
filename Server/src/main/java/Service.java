

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IService {
    private IUserRepository userRepository;
    private Map<String, IObserver> loggedClients;

    private final int nrThreaduri = 10;

    public Service(IUserRepository userRepository) {
        this.userRepository = userRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    private void notifyInscriereAdded() {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for(IObserver obs:loggedClients.values()){
            executor.execute(() -> {
                System.out.println("Notifying client inscriere added...");
                try {
                    System.out.println("Notificare inscriere adaugata!");
                    obs.rezultatAdded();
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
    public synchronized void logout(User user, IObserver client)  {
        loggedClients.remove(user.getId());
        System.out.println("Logout");

    }

    @Override
    public synchronized boolean login(String username, String parola,IObserver client) {
        loggedClients.put(username, client);
        System.out.println("Login");
        return userRepository.exists(new User(username, parola));
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
//        for (Participant e : inscriereRepository.getParticipantiPtProba(idProba)) {
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
//        Participant p = null;
//        if (existent == false && getParticipant(nume, varsta) == null) {
//            Integer id;
//            Random rand = new Random();
//            do {
//                id = rand.nextInt(200) + 1;
//            } while (participantRepository.findOne(id.toString()) != null);
//            p = new Participant(id.toString(), nume, varsta);
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
//    public synchronized Participant getParticipant(String nume, Integer varsta) {
//        return participantRepository.getParticipant(nume, varsta);
//    }
    /*private IParticipantRepository participantRepository;
    private IProbaRepository probaRepository;
    private IInscriereRepository inscriereRepository;
    private IUserRepository userRepository;
    //private List<Observer<Inscriere>> inscriereObservers = new ArrayList<>();

    public InscriereService(IParticipantRepository participantRepository, IProbaRepository probaRepository, IInscriereRepository inscriereRepository, IUserRepository userRepository) {
        this.participantRepository = participantRepository;
        this.probaRepository = probaRepository;
        this.inscriereRepository = inscriereRepository;
        this.userRepository = userRepository;
    }


    public boolean login(String username, String parola) {
        User user = new User(username, parola);
        return userRepository.exists(user);
    }

    public List<Proba> getAllProba() {
        List<Proba> all = new LinkedList<>();
        probaRepository.findAll().forEach(e -> all.add(e));
        return all;
    }

    public List<Participant> getAllParticipant() {
        List<Participant> all = new LinkedList<>();
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

    public List<Participant> getParticipanti(Integer idProba) {
        List<Participant> all = new LinkedList<>();
        inscriereRepository.getParticipantiPtProba(idProba).forEach(e -> all.add(e));
        return all;
    }

    public void saveInscriere(String nume, Integer varsta, List<Proba> probe, boolean existent) throws ValidationException {
        Participant p = null;
        if (existent == false && getParticipant(nume, varsta) == null) {
            Integer id;
            Random rand = new Random();
            do {
                id = rand.nextInt(200) + 1;
            } while (participantRepository.findOne(id.toString()) != null);
            p = new Participant(id.toString(), nume, varsta);
            participantRepository.save(p);

        } else if (existent == true && getParticipant(nume, varsta) != null)
            p = getParticipant(nume, varsta);
        else if (existent == true && getParticipant(nume, varsta) == null)
            throw new RepositoryException("Participantul nu exista!");


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
    public Participant getParticipant(String nume, Integer varsta) {
        return participantRepository.getParticipant(nume, varsta);
    }*/
}