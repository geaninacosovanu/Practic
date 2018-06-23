package repository;

import model.Pair;
import model.Participant;
import model.Rezultat;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


@Component
public class RezultatDBRepository implements IRezultatRepository {

    private Connection connection;

    public RezultatDBRepository(String propFile) {
        Properties prop = new Properties();
        try {
            prop.load(new FileReader(new File(propFile).getAbsolutePath()));
            JdbcUtils jdbcUtils = new JdbcUtils(prop);
            connection = jdbcUtils.getConnection();
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Integer getRezultate(Integer id) {
        Integer nr = 0;
        try (PreparedStatement s = connection.prepareStatement("SELECT COUNT(*) AS Nr FROM Rezultat R INNER JOIN Participant P ON R.IdParticipant = P.IdParticipant WHERE P.IdParticipant=?")) {
            s.setInt(1, id);
            ResultSet resultSet = s.executeQuery();
            while (resultSet.next()) {
                nr = Integer.parseInt(resultSet.getString("Nr"));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return nr;
    }

    @Override
    public String getNota(Integer id) {
        String nr=null;
        try (PreparedStatement s = connection.prepareStatement("SELECT SUM(R.Nota) AS Nr FROM  Rezultat R INNER JOIN Participant P ON R.IdParticipant = P.IdParticipant WHERE P.IdParticipant=?")) {
            s.setInt(1, id);
            ResultSet resultSet = s.executeQuery();
            while (resultSet.next()) {
                nr = resultSet.getString("Nr");
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return nr;
    }

    @Override
    public List<Participant> getParticipantiByUser(String user) {
       List<Participant> all =new ArrayList<>();
        try (PreparedStatement s = connection.prepareStatement("SELECT P.IdParticipant ,Nume  FROM Rezultat R INNER JOIN Participant P ON R.IdParticipant = P.IdParticipant WHERE R.IdUser=?")) {
            s.setString(1, user);
            ResultSet resultSet = s.executeQuery();
            while (resultSet.next()) {
                all.add(new Participant(Integer.parseInt(resultSet.getString("IdParticipant")),resultSet.getString("Nume")));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return all;
    }

    @Override
    public Integer size() {
        return null;
    }

    @Override
    public void save(Rezultat entity) {
        try (PreparedStatement s = connection.prepareStatement("insert into Rezultat(IdParticipant, IdUser,nota) values (?,?,?)")) {
            s.setInt(1, entity.getIdParticipant());
            s.setString(2, entity.getIdUser());
            s.setFloat(3, entity.getNota());
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Ati adaugat deja o nota exista deja!");
        }
    }

    @Override
    public void delete(Pair<String, Integer> stringIntegerPair) {

    }

    @Override
    public void update(Pair<String, Integer> stringIntegerPair, Rezultat entity) {

    }

    @Override
    public Rezultat findOne(Pair<String, Integer> stringIntegerPair) {
        return null;
    }

    @Override
    public List<Rezultat> findAll() {
        return null;
    }
}
