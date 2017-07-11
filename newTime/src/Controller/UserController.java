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
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final static String DES = "DES";
    private final static String KEY = "12345678";
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
    }
    public static byte[] de(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        // 正式执行解密操作
        return cipher.doFinal(src);
    }

    public final static String decrypt(String data, String key) {
        try {
            return new String(de(String2byte(data.getBytes()), key.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] String2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }



private void checkUser(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {
        String encryptUsername = request.getParameter("username");
        String encryptPassword = request.getParameter("password");
        String username = decrypt(encryptUsername,KEY);
        String password = decrypt(encryptPassword,KEY);

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
        String encryptUsername = request.getParameter("username");
        String encryptPassword = request.getParameter("password");
        String username = decrypt(encryptUsername,KEY);
        String password = decrypt(encryptPassword,KEY);
        User user = new User(username,password);
        userDAO.updateUer(user);

        }
private void insertUser(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {

        String encryptUsername = request.getParameter("username");
        String encryptPassword = request.getParameter("password");
        String username = decrypt(encryptUsername,KEY);
        String password = decrypt(encryptPassword,KEY);
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