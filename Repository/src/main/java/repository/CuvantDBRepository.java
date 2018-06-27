package repository;

import model.Cuvant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtils;

import java.util.ArrayList;
import java.util.List;

public class CuvantDBRepository implements ICuvantRepository {
    String confFile;
    public CuvantDBRepository(String conf) {
        this.confFile = conf;
    }

    @Override
    public Integer size() {
        return null;
    }

    @Override
    public Cuvant save(Cuvant entity) {
return null;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Cuvant entity) {

    }

    @Override
    public Cuvant findOne(Integer integer) {
        return null;
    }

    @Override
    public List<Cuvant> findAll() {
        List<Cuvant> all = new ArrayList<>();
        try (Session s = HibernateUtils.getSessionFactory(confFile).openSession()) {
            Transaction t = null;
            try {
                t = s.beginTransaction();
                all = s.createQuery("from Cuvant").list();
                t.commit();
            } catch (RuntimeException ex) {
                if (t != null)
                    t.rollback();

            }

        }
        return all;
    }
}
