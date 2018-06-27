package repository;

import model.Joc;
import utils.JdbcUtils;
import utils.RepositoryException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class JocDBRepository implements IJocRepository {

    private Connection connection;

    public JocDBRepository(String propFile) {
        Properties prop = new Properties();
        try {
            prop.load(new FileReader(new File(propFile).getAbsolutePath()));
            JdbcUtils jdbc = new JdbcUtils(prop);
            connection = jdbc.getConnection();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public Integer size() {
        return null;
    }

    @Override
    public Joc save(Joc entity) {
        try (PreparedStatement s = connection.prepareStatement("insert into Joc(IdCuvant) values (?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, entity.getIdCuvant());
            s.execute();
            ResultSet rs = s.getGeneratedKeys();
            if (rs.next())
                entity.setId(rs.getInt(1));
            return entity;
        } catch (SQLException e) {
            throw new RepositoryException("Joc exista deja!");
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Joc entity) {

    }

    @Override
    public Joc findOne(Integer integer) {
        return null;
    }

    @Override
    public List<Joc> findAll() {
        return null;
    }
}
