package repository;

import model.Copil;
import model.CopilDTO;
import model.Trecere;
import org.springframework.stereotype.Component;
import utils.JdbcUtils;
import utils.Pair;
import utils.RepositoryException;

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
public class TrecereDBRepository implements ITrecereRepository {
    private Connection connection;

    public TrecereDBRepository(String propFile) {
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
    public void save(Trecere entity) {
        try (PreparedStatement s = connection.prepareStatement("insert into Trecere(IdCopil,PunctControl,Ora) values (?,?,?)")) {
            s.setInt(1, entity.getIdCopil());
            s.setInt(2, entity.getPunctControl());
            s.setString(3, entity.getOra());
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Ati adaugat deja o nota exista deja!");
        }
    }

    @Override
    public void delete(Pair<Integer, Integer> integerIntegerPair) {

    }

    @Override
    public void update(Pair<Integer, Integer> integerIntegerPair, Trecere entity) {

    }

    @Override
    public Trecere findOne(Pair<Integer, Integer> integerIntegerPair) {
        return null;
    }

    @Override
    public List<Trecere> findAll() {
        return null;
    }

    @Override
    public Trecere getByCopil(Integer id) {
        try (PreparedStatement s = connection.prepareStatement("SELECT *  FROM Trecere  T WHERE T.IdCopil=? ORDER BY TIME(T.Ora) DESC LIMIT 1 ")) {
            Trecere all = null;
            s.setInt(1, id);
            ResultSet resultSet = s.executeQuery();
            if (resultSet.next()) {
                all = new Trecere(resultSet.getInt("PunctControl"), resultSet.getInt("IdCopil"), resultSet.getString("Ora"));
            }
            return all;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<CopilDTO> getCopiiByPunct(Integer punct) {
        try (PreparedStatement s = connection.prepareStatement("SELECT *  FROM Trecere  T INNER JOIN Copil C2 ON T.IdCopil = C2.Id WHERE T.PunctControl=? ORDER BY TIME(T.Ora) ASC ")) {
            List<CopilDTO> all = new ArrayList<>();
            s.setInt(1, punct);
            ResultSet resultSet = s.executeQuery();
            while (resultSet.next()) {
                all.add(new CopilDTO(new Copil(resultSet.getInt("IdCopil"), resultSet.getString("Nume")),resultSet.getInt("PunctControl"), resultSet.getString("Ora")));
            }
            return all;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
    @Override
    public List<Copil> getCopiiNetrecuti(Integer punct) {
        try (PreparedStatement s = connection.prepareStatement("SELECT Id,Nume  FROM Copil C1 EXCEPT SELECT Id,Nume FROM Copil C INNER JOIN Trecere T ON T.IdCopil = C.Id WHERE T.PunctControl=? ")) {
            List<Copil> all = new ArrayList<>();
            s.setInt(1, punct);
            ResultSet resultSet = s.executeQuery();
            while (resultSet.next()) {
                all.add(new Copil(resultSet.getInt("Id"), resultSet.getString("Nume")));
            }
            return all;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}
