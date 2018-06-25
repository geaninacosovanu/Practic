import java.io.Serializable;

public class User implements HasId<String>, Serializable {
    private String userId;
    private String parola;
    private Integer punctControl;

    public User(String userId, String parola) {
        this.userId = userId;
        this.parola = parola;
    }

    public User(String userId, String parola, Integer punctControl) {
        this.userId = userId;
        this.parola = parola;
        this.punctControl = punctControl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getPunctControl() {
        return punctControl;
    }

    public void setPunctControl(Integer punctControl) {
        this.punctControl = punctControl;
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public void setId(String s) {
        this.userId = s;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String toString() {
        return "model.User{" +
                "userId='" + userId + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }
}