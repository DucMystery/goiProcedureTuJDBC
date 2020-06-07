package CodeGym.DAO;

import CodeGym.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {
    private String jdbcURL ="jdbc:mysql://localhost:3306/demov2";
    private String jdbcUserName = "mystery1309";
    private String jdbcPassword ="13091997";

    private static final String INSERT_USERS_SQL ="INSERT INTO user(name,email,country)" +
            "VALUES (?,?,?);";
    private static final String SELECT_USER_BY_ID="SELECT id,name,email,country FROM user WHERE id=?;";
    private static final String SELECT_ALL_USERS ="SELECT*FROM user";
    private static final String DELETE_USERS_SQL ="DELETE FROM user WHERE id=?;";
    private static final String UPDATE_USERS_SQL = "update user set name=?, email=?, country=? where id = ?;";

    private static final String GET_USER_BY_ID ="CALL get_user_by_id(?);";
    private static final String INSERT_USER_PROCEDURE ="CALL insert_user(?,?,?);";
    public UserDAO() {
    }

    protected Connection getConnection(){
        Connection connection=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection= DriverManager.getConnection(jdbcURL,jdbcUserName,jdbcPassword);
            System.out.println("Done");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void insertUser(String name,String email,String country) throws SQLException {
     Connection connection = getConnection();
     PreparedStatement statement = connection.prepareStatement(INSERT_USERS_SQL);
     statement.setString(1,name);
     statement.setString(2,email);
     statement.setString(3,country);
     statement.executeUpdate();

    }

    @Override
    public User selectUser(int id) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
        preparedStatement.setInt(1,id);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()){
            String name = rs.getString("name");
            String email = rs.getString("email");
            String country = rs.getString("country");

            User user = new User(id,name,email,country);
        }
        return null;
    }

    @Override
    public List<User> selectAllUser() throws SQLException {
        List<User> ListUser =new ArrayList<>();
        Connection connection =getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()){
            int id = rs.getInt("id");
            String name =rs.getString("name");
            String email =rs.getString("email");
            String country =rs.getString("country");
            ListUser.add(new User(id,name,email,country));
        }
        return ListUser;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        Connection connection =getConnection();
        PreparedStatement preparedStatement =connection.prepareStatement(DELETE_USERS_SQL);
        preparedStatement.setInt(1,id);
        rowDeleted =preparedStatement.executeUpdate()>0;
        return rowDeleted;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL);
        preparedStatement.setString(1,user.getName());
        preparedStatement.setString(2,user.getEmail());
        preparedStatement.setString(3,user.getCountry());
        preparedStatement.setInt(4,user.getId());

        rowUpdated =preparedStatement.executeUpdate()>0;
        return rowUpdated;
    }

    @Override
    public User getUserById(int id) {
        User user = null;
        Connection connection = getConnection();
        try {
            CallableStatement statement = connection.prepareCall(GET_USER_BY_ID);
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                String name =rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user =new User(id,name,email,country);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void insertUserStore(String name,String email,String country) {
        Connection connection = getConnection();
        try {
            CallableStatement statement = connection.prepareCall(INSERT_USER_PROCEDURE);
            statement.setString(1,name);
            statement.setString(2,email);
            statement.setString(3,country);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addUserTransaction(String name,String email,String country, int[] permisions) throws SQLException {
        Connection connection =null;

        PreparedStatement preparedStatement =null;

        PreparedStatement pstAssignment =null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            preparedStatement =connection.prepareStatement(INSERT_USERS_SQL,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,country);
            int rowAffected = preparedStatement.executeUpdate();

            rs =preparedStatement.getGeneratedKeys();
            int user_Id = 0;
            if (rs.next()){
                user_Id =rs.getInt(1);
            }
            if (rowAffected==1){
                String sqlPivot = "INSERT INTO user_permision(permision_id,user_id) VALUES (?,?);";
                pstAssignment =connection.prepareStatement(sqlPivot);
                for (int permisionId :permisions){
                    pstAssignment.setInt(1,user_Id);
                    pstAssignment.setInt(2,permisionId);
                    pstAssignment.executeUpdate();
                }
                connection.commit();
            }else {
                connection.rollback();
            }
        } catch (SQLException e) {
            try {
                if (connection!= null){
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(e.getMessage());
        }finally {
            if (rs!= null)rs.close();
            if (preparedStatement!= null)preparedStatement.close();
            if (pstAssignment !=null) pstAssignment.close();
            if (connection != null) connection.close();
        }
    }
}
