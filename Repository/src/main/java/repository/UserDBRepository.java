package repository;

import model.User;
import utils.JdbcUtils;
import utils.RepositoryException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class UserDBRepository implements IUserRepository {
    private Connection connection;

    public UserDBRepository(String propFile) {
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
    public boolean exists(User u) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(u.getParola().getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try (PreparedStatement s = connection.prepareStatement("SELECT COUNT (*) AS Nr FROM User U WHERE U.userId=? AND U.parola=?")) {
            s.setString(1, u.getId());
            s.setString(2, generatedPassword);
            ResultSet resultSet = s.executeQuery();
            resultSet.next();
            if (resultSet.getInt("Nr") == 0)
                return false;
            return true;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

    }

    @Override
    public Integer size() {
        return null;
    }

    @Override
    public void save(User entity) {

    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void update(String s, User entity) {

    }

    @Override
    public User findOne(String st) {
        try (PreparedStatement s = connection.prepareStatement("SELECT *  FROM User U WHERE U.userId=? ")) {
            s.setString(1, st);
            ResultSet resultSet = s.executeQuery();
            resultSet.next();
            return new User(resultSet.getString("userId"), resultSet.getString("parola"));
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
