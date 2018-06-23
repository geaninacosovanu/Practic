public interface IUserRepository extends IRepository<String, User> {
    boolean exists(User u);
}
