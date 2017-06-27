package dao;

import entity.User;

public interface UserDAO {
    User get(String username);
    boolean registerUser(User user);
    User checkUser(String name,String password);
}
