package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entity.User;
import DAO.UserDAO;
import java.util.Date;
public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;
    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
        userDAO = new UserDAO(jdbcURL, jdbcUsername, jdbcPassword);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       String action = request.getServletPath();
       System.out.println("connected");
        try {
            switch (action) {
                case "/insert_user":
                    insertUser(request, response);
                    break;
                case "/update_user":
                    updateUser(request,response);
                    break;
                case "/login":
                    checkUser(request,response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }

   //     response.getWriter().write("now time:" + new Date().toLocaleString());
    }
    private void checkUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
      String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User(username,password);
        User true_user = userDAO.getUser(username);
        String p1 = user.getPassword();
        String p2 = true_user.getPassword();

        if(p1.equals(p2)){
            response.getOutputStream().write(1);
            System.out.println("login success");
        }else{
            response.getOutputStream().write(0);
            System.out.println("login failure");
        }

    }
    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User(username,password);
        userDAO.updateUer(user);
        response.sendRedirect("list_user");
    }
    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        System.out.println("register");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User test = userDAO.getUser(username);
        if(test == null) {
            User newUser = new User(username, password);
            userDAO.insertUser(newUser);
            response.getOutputStream().write(1);
            System.out.println("register success");
        }else{
            response.getOutputStream().write(0);
            System.out.println("register failure");
        }

    }
}
