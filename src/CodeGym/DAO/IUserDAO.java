package CodeGym.DAO;

import CodeGym.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    void insertUser(String name,String email,String country) throws SQLException;
    User selectUser(int id) throws SQLException;
    List<User> selectAllUser() throws SQLException;
    boolean deleteUser(int id) throws SQLException;
    boolean updateUser(User user) throws SQLException;
    User getUserById(int id);
    void insertUserStore(String name, String email, String country);
    void addUserTransaction(String name, String email,String country,int[]permision) throws SQLException;
}
