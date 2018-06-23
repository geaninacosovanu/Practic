package repository;

import java.util.List;

public interface IRepository<ID,T> {
    Integer size();
    void save(T entity);
    void delete(ID id);
    void update(ID id, T entity);
    T findOne(ID id);
    List<T> findAll();
}
