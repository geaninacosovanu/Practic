package repository;

import model.Participant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.IParticipantRepository;

import java.util.ArrayList;
import java.util.List;

public class ParticipantDBRepository implements IParticipantRepository {
    String confFile;

    public ParticipantDBRepository( String conf) {
        this.confFile = conf;
    }

    @Override
    public Integer size() {
        return null;
    }

    @Override
    public void save(Participant entity) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Participant entity) {

    }

    @Override
    public Participant findOne(Integer integer) {
        return null;
    }

    @Override
    public List<Participant> findAll() {
        List<Participant> all = new ArrayList<>();
        try (Session s = HibernateUtils.getSessionFactory(confFile).openSession()) {
            Transaction t = null;
            try {
                t = s.beginTransaction();
                all = s.createQuery("from Participant").list();
                t.commit();
            } catch (RuntimeException ex) {
                if (t != null)
                    t.rollback();

            }

        }
        return all;
    }
}
