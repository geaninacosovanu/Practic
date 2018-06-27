package repository;

import model.Copil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtils;

import java.util.ArrayList;
import java.util.List;

public class CopilDBRepository implements ICopilRepository {
    String confFile;
    public CopilDBRepository( String conf) {
        this.confFile = conf;
    }
    @Override
    public Integer size() {
        return null;
    }

    @Override
    public void save(Copil entity) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Copil entity) {

    }

    @Override
    public Copil findOne(Integer integer) {
        return null;
    }

    @Override
    public List<Copil> findAll() {
        List<Copil> all = new ArrayList<>();
        try (Session s = HibernateUtils.getSessionFactory(confFile).openSession()) {
            Transaction t = null;
            try {
                t = s.beginTransaction();
                all = s.createQuery("from Copil").list();
                t.commit();
            } catch (RuntimeException ex) {
                if (t != null)
                    t.rollback();

            }

        }
        return all;
    }

}
