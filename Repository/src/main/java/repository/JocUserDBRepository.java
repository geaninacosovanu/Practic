package repository;

import model.JocUser;
import org.springframework.stereotype.Component;
import utils.DTO;
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
        try (PreparedStatement s = connection.prepareStatement("insert into JocUser(IdJoc, IdUser, Litere) values (?,?,?)")) {
            s.setInt(1, entity.getIdJoc());
            s.setString(2, entity.getIdUser());
            s.setString(3, entity.getLitere());
            s.execute();
            return entity;
        } catch (SQLException e) {
            throw new RepositoryException("JocUser exista deja!");
        }
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
        List<JocUser> all = new ArrayList<>();
        try (Statement s = connection.createStatement()) {
            try (ResultSet result = s.executeQuery("SELECT * FROM  JocUser")) {
                while (result.next()) {
                    all.add(new JocUser(result.getString("IdUser"), result.getInt("IdJoc"), result.getString("Litere")));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return all;
    }

    @Override
    public DTO getDTO(Integer idJoc, String user) {
        DTO all = null;
        try (PreparedStatement st = connection.prepareStatement("SELECT * FROM  JocUser JU  INNER JOIN Joc J ON JU.IdJoc = J.Id INNER JOIN Cuvant C2 ON J.IdCuvant = C2.Id WHERE JU.IdJoc=? AND JU.IdUser=?")) {
            {
                st.setInt(1, idJoc);
                st.setString(2, user);
                ResultSet result = st.executeQuery();
                if (result.next()) {
                    all = new DTO(result.getString("Litere"),result.getString("Cuvant"), result.getInt("IdJoc"),result.getString("IdUser"));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return all;
    }
}
