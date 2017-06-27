package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import entity.User;



@SuppressWarnings("serial")
public class LoginAction {
    private String name;
    private String password;
    private UserDAO userDAO;

    public void setName(String name){
        this.name = name;
        return;
    }
    public String getName(){
        return this.name;
    }
    public void setPassword(String password){
        this.password = password;
        return;
    }
    public String getPassword(){
        return this.password;
    }
    public void setUserManager(UserDAO userDAO){
        this.userDAO = userDAO;
        return;
    }
    public String execute(){

        User user = userDAO.checkUser(name, password);
        if(user==null) return "fail";
        return "success";
    }
}
