package repository;

import model.Joc;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import utils.HibernateUtils;

import java.util.List;

@Component
public class JocDBRepository implements IJocRepository {
    String confFile;

    public JocDBRepository(String conf) {
        this.confFile = conf;
    }

    @Override
    public Integer size() {
        return null;
    }

    @Override
    public Joc save(Joc entity) {
        try (Session session = HibernateUtils.getSessionFactory(confFile).openSession()) {
            Transaction t = null;
            try {
                t = session.beginTransaction();
                Integer id =(Integer)session.save(entity);
                entity.setId(id);
                t.commit();
            } catch (RuntimeException ex) {
                if (t != null)
                    t.rollback();

            }
        }
        return entity;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Joc entity) {

    }

    @Override
    public Joc findOne(Integer integer) {

        Joc joc=null;
        try (Session s =HibernateUtils.getSessionFactory(confFile).openSession()){
            Transaction t =null;
            try {
                t =s.beginTransaction();
                joc= s.createQuery("from Joc j where j.id=:id",Joc.class).setInteger("id",integer).uniqueResult();
                t.commit();
            }catch (RuntimeException ex) {
                if (t != null)
                    t.rollback();
            }
        }
        return joc;
    }

    @Override
    public List<Joc> findAll() {
        return null;
    }
}
