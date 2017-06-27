package action;

import entity.User;
import dao.UserDAO;


@SuppressWarnings("serial")
public class RegistAction {
    private String name;
    private String password;
    private UserDAO userDAO;
    boolean flag = false;
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
    public boolean isLegalStr(String str){
        if(str.contains("\'") || str.contains("\""))
            return false;
        else
            return true;
    }
    public String execute(){

        if(!isLegalStr(name) || !isLegalStr(password)){

            return "fail";
        }
        User user = new User();

        user.setUsername(name);
        user.setUserpwd(password);
        flag = userDAO.registerUser(user);
        if(!flag){

            return "fail";
        }
        return "success";
    }
}