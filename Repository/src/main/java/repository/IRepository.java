package repository;

import java.util.List;

public interface IRepository<ID,T> {
    Integer size();
    T save(T entity);
    void delete(ID id);
    void update(ID id, T entity);
    T findOne(ID id);
    List<T> findAll();
}
