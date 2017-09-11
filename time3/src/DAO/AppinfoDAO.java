package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import entity.Record;
import entity.Time_record;
import entity.Weight;
import net.sf.json.JSONArray;

import entity.Appinfo;
import sun.security.krb5.internal.APOptions;


public class AppinfoDAO {
    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private Connection jdbcConnection;

    public AppinfoDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(
                    jdbcURL, jdbcUsername, jdbcPassword);
        }
    }

    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }
    public boolean insertOrUpdateWeight(Weight w) throws SQLException {//每次插入/更新weight，要检查appinfo里是否有这个appinfo
        boolean flag = false;
        String sql = "SELECT * FROM appinfo WHERE packagename = ?";
        connect();
        String packagename = w.getPackagename();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, packagename);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            flag = true;
        }
        if(!flag){
            System.out.println("insert appinfo");
            String sql1 = "INSERT INTO appinfo (packagename,appname,category,weight,installnum,minutes,image) VALUES (?, ?, ?, ? ,?, ?, ?)";
            PreparedStatement statement1 = jdbcConnection.prepareStatement(sql1);
            statement1.setString(1, w.getPackagename());
            statement1.setString(2, w.getAppname());
            statement1.setInt(3, 0);
            statement1.setDouble(4,w.getWeight());
            statement1.setInt(5, 1);
            statement1.setInt(6, w.minutes);
            statement1.setString(7, "null");
            statement1.executeUpdate();
            statement1.close();
        }

        //接下来是insert or update weight
        String sql2  = "update weight set weight = ? where packagename = ? and email = ?";
        PreparedStatement statement2 = jdbcConnection.prepareStatement(sql2);
        statement2.setInt(1,w.getWeight());
        statement2.setString(2,w.getPackagename());
        statement2.setString(3,w.getEmail());
        boolean rowUpdated = statement2.executeUpdate() > 0 ;

        if(!rowUpdated){
            System.out.println("insert weight");
            String sql3 = "insert into weight(email,packagename,weight) values(?,?,?)";
            PreparedStatement statement3 = jdbcConnection.prepareStatement(sql3);
            statement3.setString(1,w.getEmail());
            statement3.setString(2,w.getPackagename());
            statement3.setInt(3,w.getWeight());
            statement3.executeUpdate();

            statement3.close();
        }

        resultSet.close();
        statement.close();

        statement2.close();


        return true;
    }

    public boolean insertRecord(Record r) throws SQLException {
        connect();
        boolean rowInserted = false;
        java.sql.Date d = new Date(r.getDay().getTime());
        String sql2  = "update record set duration = ?,frequency = ? where packagename = ? and email = ? and day = ?";
        PreparedStatement statement2 = jdbcConnection.prepareStatement(sql2);
        statement2.setInt(1,r.getDuration());
        statement2.setInt(2,r.getDuration());
        statement2.setString(3,r.getPackageName());
        statement2.setString(4,r.getEmail());
        statement2.setDate(5,d);
        boolean rowUpdated = statement2.executeUpdate() > 0 ;
        if(!rowUpdated){
            System.out.println("insert record");
            String sql = "INSERT INTO record(email,packagename,day,frequency,duration) VALUES (?,?,?,?,?)";
            java.sql.Date day = new java.sql.Date(r.getDay().getTime());
            PreparedStatement statement = jdbcConnection.prepareStatement(sql);
            statement.setString(1,r.getEmail());
            statement.setString(2, r.getPackageName());
            statement.setDate(3,day);
            statement.setInt(4, r.getFrequency());
            statement.setInt(5,r.getDuration());
            rowInserted = statement.executeUpdate() > 0;
            statement.close();
        }



        disconnect();
        return rowInserted;
    }

    public List<Appinfo> listAllAppinfo() throws SQLException {
        List<Appinfo> listAppinfo = new ArrayList<Appinfo>();

        String sql = "SELECT * FROM appinfo";

        connect();

        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            String appname = resultSet.getString("appname");
            int category = resultSet.getInt("category");
            int weight = resultSet.getInt("weight");
            String packagename = resultSet.getString("packagename");
            String image = resultSet.getString("iamge");
            int installnum = resultSet.getInt("installnum");
            Appinfo appinfo;
            appinfo = new Appinfo(packagename,appname,category,weight,image,installnum);
            listAppinfo.add(appinfo);
        }
        resultSet.close();
        statement.close();
        disconnect();
        return listAppinfo;
    }

    // 删除一个appinfo的感觉用不上，暂时没写

    public boolean updateAppinfo(Appinfo app) throws SQLException {
        String sql = "UPDATE appinfo SET weight = ?";
        sql += " WHERE appname = ?";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setDouble(1, app.getWeight());
        statement.setString(2, app.getAppname());
        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;
    }

    public List<Appinfo> searchAppinfo(String appname)throws SQLException{
        List<Appinfo> list = new ArrayList<Appinfo>();
        String sql = "SELECT * FROM appinfo where appname like '%" + appname + "%'";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            //把resultset里的转换成json传回客户端
            String pack = resultSet.getString("packagename");
            String app = resultSet.getString("appname");
            int weight = resultSet.getInt("weight");
            int install = resultSet.getInt("installnum");
            int cate = resultSet.getInt("category");
            String image = resultSet.getString("image");
            int min =resultSet.getInt("minutes");
            Appinfo tmp = new Appinfo(pack,app,cate,weight,image,install,min);
            list.add(tmp);
          //  tmp.print();
        }

        resultSet.close();
        statement.close();
        disconnect();
        System.out.println("disconnect");
        return list;
    }
    public Appinfo getAppinfo(String packagename) throws SQLException {
        Appinfo appinfo = null;
        String sql = "SELECT * FROM appinfo WHERE packagename = ?";
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, packagename);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String appname = resultSet.getString("appname");
            int category = resultSet.getInt("category");
            int weight = resultSet.getInt("weight");
            String image = resultSet.getString("iamge");
            int installnum = resultSet.getInt("installnum");
            int min =resultSet.getInt("minutes");
            appinfo = new Appinfo(packagename,appname,category,weight,image,installnum,min);
        }

        resultSet.close();
        statement.close();

        return appinfo;
    }

    public double getAvgMin(String email)throws SQLException{
        connect();
        double min = 0;
        String sql ="select avg_min from stat where email =\'"+email + "\'";

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        ResultSet r = statement.executeQuery();
        while(r.next()){
            min = r.getDouble("avg_min");

        }
        return min;
    }
    public double getAvgAT(String email)throws SQLException{
        connect();
        double at = 0;
        String sql ="select avg_at from stat where email =\'"+email + "\'";
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        ResultSet r = statement.executeQuery();
        while(r.next()){
            at = r.getDouble("avg_at");
        }
        return at;
    }

    public List<Time_record> getTime(String email)throws SQLException{
        List<Time_record> list = new ArrayList<Time_record>();
        String sql = "select * from time_record where email =\'"+email+"\' order by minutes DESC ";
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){

            String pack = resultSet.getString("packagename");
            String app = resultSet.getString("appname");
            int min =resultSet.getInt("minutes");
            Time_record tmp = new Time_record(email,pack,app,min);
            list.add(tmp);
        }
        resultSet.close();
        statement.close();
        return list;

    }
    public List<Appinfo> listByInstall()throws SQLException{
        connect();
        List<Appinfo> list = new ArrayList<Appinfo>();//返回值
        String sql = "select * from rank_install order by installnum DESC";
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        int i =0;
        while(resultSet.next() && i < 50){
            //把resultset里的转换成json传回客户端
            String pack = resultSet.getString("packagename");
            String app = resultSet.getString("appname");
            int weight = resultSet.getInt("weight");
            int install = resultSet.getInt("installnum");
            int cate = resultSet.getInt("category");
            String image = resultSet.getString("image");
            int min =resultSet.getInt("minutes");
            Appinfo tmp = new Appinfo(pack,app,cate,weight,image,install,min);
            list.add(tmp);
            i++;
            //  tmp.print();
        }

        resultSet.close();
        statement.close();
        return list;
    }

    public List<Appinfo> listByMin()throws SQLException{
        connect();
        List<Appinfo> list = new ArrayList<Appinfo>();//返回值
        String sql = "select * from rank_minutes order by minutes DESC";
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        resultSet.setFetchSize(50);
        while(resultSet.next()){
            //把resultset里的转换成json传回客户端
            String pack = resultSet.getString("packagename");
            String app = resultSet.getString("appname");
            int weight = resultSet.getInt("weight");
            int install = resultSet.getInt("installnum");
            int cate = resultSet.getInt("category");
            String image = resultSet.getString("image");
            int min =resultSet.getInt("minutes");
            Appinfo tmp = new Appinfo(pack,app,cate,weight,image,install,min);
            list.add(tmp);
            //  tmp.print();
        }
        resultSet.close();
        statement.close();
        return list;
    }
    public List<Appinfo> recommendApp(String email)throws SQLException{
        connect();
        List<Appinfo> list = new ArrayList<Appinfo>();
        List<String> packagenameList = new ArrayList<String>();
        String sql = "select distinct packagename from weight where email in (select email from users where classification = (select classification from users where email ='" +email+ " ')) and packagename in (select packagename from feature)" ;
        System.out.println(sql);
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        int i = 0;
        while(resultSet.next() && i < 50){
            String tmp = resultSet.getString("packagename");
            packagenameList.add(tmp);
            i++;
        }

        i =0;
        while(i < packagenameList.size()){//遍历packagenameList
            sql = "SELECT  * FROM appinfo WHERE packagename = '"+ packagenameList.get(i) + "'" ; //条件语句有问题
            statement = jdbcConnection.prepareStatement(sql);
            ResultSet r = statement.executeQuery();
            while(r.next()){
                String pack = r.getString("packagename");
                String app = r.getString("appname");
                int weight = r.getInt("weight");
                int install = r.getInt("installnum");
                int cate = r.getInt("category");
                int min =r.getInt("minutes");
                Appinfo tmp = new Appinfo(pack,app,cate,weight,"null",install,min);
                list.add(tmp);
            }
            i = i + 1;

            r.close();
        }
        resultSet.close();

        statement.close();
        disconnect();
        System.out.println("disconnect");
        return list;
    }
    public List<Appinfo> featureAppinfo(String email) throws SQLException{
        List<Appinfo> list = new ArrayList<Appinfo>();
        List<String> packagenameList = new ArrayList<String>();
        String sql = "SELECT packagename from weight WHERE email = " + email;
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            String tmp = resultSet.getString("packagenmae");
            packagenameList.add(tmp);
        }
        int i =0;
        while(i < packagenameList.size()){//遍历packagenameList
            sql = "SELECT  * FROM appinfo WHERE packagename = "+ packagenameList.get(i) +"AND packagename in (select packagename from feature)"; //条件语句有问题
            statement = jdbcConnection.prepareStatement(sql);
           ResultSet r = statement.executeQuery();
           while(r.next()){
               String pack = resultSet.getString("packagename");
               String app = resultSet.getString("appname");
               int weight = resultSet.getInt("weight");
               int install = resultSet.getInt("installnum");
               int cate = resultSet.getInt("category");
               String image = resultSet.getString("image");
               Appinfo tmp = new Appinfo(pack,app,cate,weight,image,install);
               list.add(tmp);
           }
           r.close();
        }
        resultSet.close();

        statement.close();
        disconnect();
        System.out.println("disconnect");
        return list;
    }
}
