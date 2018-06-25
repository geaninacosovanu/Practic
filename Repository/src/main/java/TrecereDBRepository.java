import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
        try (PreparedStatement s = connection.prepareStatement("INSERT  INTO  Trecere(IdMasina, PunctControl, Ora) VALUES (?,?,?)")) {
            s.setInt(1, entity.getIdMasina());
            s.setInt(2, entity.getPunctControl());
            s.setString(3, entity.getOra());
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
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
    public Trecere getLastTrecere(Integer idMasina) {
        try (PreparedStatement s = connection.prepareStatement("SELECT *  FROM Trecere T WHERE T.IdMasina=? ORDER BY Ora DESC")) {
            s.setInt(1, idMasina);
            Trecere t = null;
            ResultSet resultSet = s.executeQuery();
            if (resultSet.next())
                t = new Trecere(Integer.parseInt(resultSet.getString("IdMasina")), Integer.parseInt(resultSet.getString("IdMasina")), resultSet.getString("Ora"));
            return t;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

    }

    @Override
    public List<MasinaDTO> getMasiniByPunctControl(Integer punctControl) {
        try (PreparedStatement s = connection.prepareStatement("SELECT IdMasina,Nume,Ora FROM Trecere T INNER JOIN Masina M ON T.IdMasina = M.Id WHERE T.PunctControl=? ORDER BY Ora ASC ")) {
            s.setInt(1, punctControl);
            List<MasinaDTO> all = new ArrayList<>();
            ResultSet resultSet = s.executeQuery();
            while (resultSet.next())
                all.add(new MasinaDTO(new Masina(Integer.parseInt(resultSet.getString("IdMasina")), resultSet.getString("Nume")), punctControl, resultSet.getString("Ora")));
            return all;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<Masina> getMasiniNetrecute(Integer punctControl) {
        try (PreparedStatement s = connection.prepareStatement("SELECT DISTINCT IdMasina,Nume FROM Trecere T INNER JOIN Masina M ON T.IdMasina = M.Id WHERE T.PunctControl<>? GROUP BY IdMasina")) {
            s.setInt(1, punctControl);
            List<Masina> all = new ArrayList<>();
            ResultSet resultSet = s.executeQuery();
            while (resultSet.next())
                all.add(new Masina(Integer.parseInt(resultSet.getString("IdMasina")), resultSet.getString("Nume")));
            return all;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}
