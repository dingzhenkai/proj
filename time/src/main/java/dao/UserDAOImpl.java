package dao;


import entity.User;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Session;
import service.UserService;
import util.HibernateUtil;
import org.hibernate.Hibernate;
import java.util.List;

public class UserDAOImpl implements UserDAO{
    public User get(String username){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        User res = session.get(User.class, username);

        session.getTransaction().commit();
        return res;
    }

    public boolean registerUser(User user){
        boolean flag = false;
        try{
            this.getHibernateTemplate().save(user);
            flag = true;
        }catch(RuntimeException re){
            throw re;
        }
        return flag ;
    }

    public User checkUser(String name,String password){
        String hql = "from User as item where item.username = '"+name+"' and item.password='"+password+"'";
        User user = null;
        try{
            List<User> userList = this.getHibernateTemplate().find(hql);
            if(userList.size() > 0){
                user = new User();
                user = userList.get(0);
            }
        }catch(RuntimeException re){
            throw re;
        }
        return user;
    }
}
