import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class MasinaDBRepository implements IMasinaRepository {
    String confFile;

    public MasinaDBRepository( String conf) {
        this.confFile = conf;
    }
    @Override
    public Integer size() {
        return null;
    }

    @Override
    public void save(Masina entity) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Masina entity) {

    }

    @Override
    public Masina findOne(Integer integer) {
        return null;
    }

    @Override
    public List<Masina> findAll() {
        List<Masina> all = new ArrayList<>();
        try (Session s = HibernateUtils.getSessionFactory(confFile).openSession()) {
            Transaction t = null;
            try {
                t = s.beginTransaction();
                all = s.createQuery("from Masina").list();
                t.commit();
            } catch (RuntimeException ex) {
                if (t != null)
                    t.rollback();

            }

        }
        return all;
    }
}
