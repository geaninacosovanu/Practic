package repository;

import model.Joc;
import model.JocUser;
import model.User;
import org.springframework.stereotype.Component;
import utils.JdbcUtils;
import utils.Pair;
import utils.RepositoryException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class JocUserDBRepository implements IJocUserRepository {
    private Connection connection;

    public JocUserDBRepository(String propFile) {
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
    public JocUser save(JocUser entity) {
        try (PreparedStatement s = connection.prepareStatement("insert into JocUser(IdJoc, IdJucator, Numere,UltimaPozitie) VALUES (?,?,?,?)")) {
            s.setInt(1, entity.getIdJoc());
            s.setString(2, entity.getIdUser());
            s.setString(3, entity.getNumere());
            s.setInt(4, entity.getLast());
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Inscrierea exista deja!");
        }
        return entity;
    }

    @Override
    public void delete(Pair<String, Integer> stringIntegerPair) {

    }

    @Override
    public void update(Pair<String, Integer> stringIntegerPair, JocUser entity) {

    }

    @Override
    public JocUser findOne(Pair<String, Integer> stringIntegerPair) {
        return null;
    }

    @Override
    public List<JocUser> findAll() {
        return null;
    }

    @Override
    public List<JocUser> getDetalii(Integer idJoc,String user) {
        List<JocUser> all = new ArrayList<>();
        try (PreparedStatement s = connection.prepareStatement("SELECT * FROM  JocUser  WHERE JocUser.IdJoc=? AND JocUser.IdJucator=?")) {
            s.setInt(1,idJoc);
            s.setString(2,user);
            ResultSet resultSet = s.executeQuery();
            while (resultSet.next()) {
                all.add(new JocUser(resultSet.getString("IdJucator"),resultSet.getInt("IdJucator"),resultSet.getString("Numere"),resultSet.getInt("UltimaPozitie")));
            }
        }
        catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return all;
    }
}