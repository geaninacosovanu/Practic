import model.Participant;
import model.Rezultat;
import model.User;
import repository.IParticipantRepository;
import repository.IRezultatRepository;
import repository.IUserRepository;
import repository.RepositoryException;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IService {
    private final int nrThreaduri = 10;
    private IUserRepository userRepository;
    private IParticipantRepository participantRepository;
    private IRezultatRepository rezultatRepository;
    private Map<String, IObserver> loggedClients;

    public Service(IUserRepository userRepository, IParticipantRepository participantRepository, IRezultatRepository rezultatrRepository) {
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.rezultatRepository = rezultatrRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    private void notifyNotaAdded() {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for (IObserver obs : loggedClients.values()) {
            executor.execute(() -> {
                System.out.println("Notifying client nota added...");
                try {
                    System.out.println("Notificare nota adaugata!");
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
    public synchronized void logout(User user, IObserver client) {
        loggedClients.remove(user.getId());
        System.out.println("Logout");

    }

    @Override
    public synchronized List<ParticipantDTO> getAllParticipanti() {
        List<Participant> all = participantRepository.findAll();
        List<ParticipantDTO> allP = new ArrayList<>();
        for (Participant p : all) {
            Integer nr = rezultatRepository.getRezultate(p.getId());
            String status = null;
            if (nr == 0)
                status = "NO JUMPS";
            else if (nr <= 2)
                status = "PENDING";
            else if (nr == 3) {
                status = rezultatRepository.getNota(p.getId());
            }
            allP.add(new ParticipantDTO(p, status));
        }
        return allP;
    }

    @Override
    public synchronized List<Participant> getParticipantiUndone() {
        List<Participant> all = participantRepository.findAll();
        List<Participant> allP = new ArrayList<>();
        for (Participant p : all) {
            Integer nr = rezultatRepository.getRezultate(p.getId());
            if (nr <= 2)
                allP.add(p);
        }
        return allP;
    }

    @Override
    public synchronized void saveNota(Integer id, String id1, Float nota) throws ServiceException {
        try {
            rezultatRepository.save(new Rezultat(id1, id, nota));
            notifyNotaAdded();
        }catch(RepositoryException e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public synchronized boolean login(String username, String parola, IObserver client) {
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
    /*private repository.IParticipantRepository participantRepository;
    private IProbaRepository probaRepository;
    private IInscriereRepository inscriereRepository;
    private repository.IUserRepository userRepository;
    //private List<Observer<Inscriere>> inscriereObservers = new ArrayList<>();

    public InscriereService(repository.IParticipantRepository participantRepository, IProbaRepository probaRepository, IInscriereRepository inscriereRepository, repository.IUserRepository userRepository) {
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
            throw new repository.RepositoryException("Participantul nu exista!");


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