package service;

import service.UserService;
import entity.User;
import dao.UserDAO;

public class UserServiceImpl implements UserService {
    private UserDAO userDAO;
    public boolean checkUser(User user){
        String username = user.getUsername();
        String password = user.getUserpwd();
        User res = userDAO.get(username);
        if (res.getUserpwd() == password)
            return true;
        else
            return false;
    }
}
