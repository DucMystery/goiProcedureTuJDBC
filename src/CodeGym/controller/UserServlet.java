package CodeGym.controller;

import CodeGym.DAO.UserDAO;
import CodeGym.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID=1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO=new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action =req.getParameter("action");
        if (action==null){
            action="";
        }
        switch (action){
            case "create":
                showNewForm(req,resp);
                break;
            case "edit":
                try {
                    showEditForm(req,resp);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "delete":
                deleteUser(req,resp);
                break;
            case "permision":
                addUserPermision(req,resp);
                break;
            default:
                try {
                    listUser(req,resp);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    private void addUserPermision(HttpServletRequest req, HttpServletResponse resp) {
        String name = "kien";
        String email = "kien69@gamil.com";
        String country = "VN";
        int [] permision = {1,2,4};
        try {
            userDAO.addUserTransaction(name, email,country,permision);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        try {
            userDAO.deleteUser(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            List<User> listUser =userDAO.selectAllUser();
            req.setAttribute("ListUser",listUser);
            RequestDispatcher dispatcher =req.getRequestDispatcher("users/list.jsp");
            try {
                dispatcher.forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(req.getParameter("id"));
        User existingUser = userDAO.getUserById(id);
        req.setAttribute("user",existingUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("users/edit.jsp");
        dispatcher.forward(req,resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("users/create.jsp");
        dispatcher.forward(req,resp);
    }

    private void listUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        List<User> ListUser =new ArrayList<>();
        ListUser = userDAO.selectAllUser();
        req.setAttribute("ListUser",ListUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("users/list.jsp");
        dispatcher.forward(req,resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    insertUser(request, response);
                    break;
                case "edit":
                    updateUser(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
        response.setContentType("text/html;charset=UTF-8");
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        int id =Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String country = req.getParameter("country");

        User user = new User(id,name,email,country);
        userDAO.updateUser(user);
        RequestDispatcher dispatcher = req.getRequestDispatcher("users/edit.jsp");
        dispatcher.forward(req,resp);
    }

    private void insertUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, SQLException, IOException {
        String name =req.getParameter("name");
        String email = req.getParameter("email");
        String country = req.getParameter("country");
        userDAO.insertUserStore(name,email,country);
        listUser(req,resp);
    }
}
