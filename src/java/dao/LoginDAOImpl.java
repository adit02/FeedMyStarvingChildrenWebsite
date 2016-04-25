/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.LoginBean;
import model.PixelBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author admin
 */
public class LoginDAOImpl implements LoginDAO {

    private PixelBean thePixel;

    @Override
    public int signUp(LoginBean aProfile) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        int rowCount = 0;
        try {
            String Project353 = "jdbc:derby://localhost:1527/Project353";
            Connection DBConn = DriverManager.getConnection(Project353, "itkstu", "student");
            String insertString;
            Statement stmt = DBConn.createStatement();
            insertString = "INSERT INTO Project353.Users VALUES ('"
                    + aProfile.getFirstName()
                    + "','" + aProfile.getLastName()
                    + "','" + aProfile.getUserID()
                    + "','" + aProfile.getPassword()
                    + "','" + aProfile.getConfirmPwd()
                    + "','" + aProfile.getEmailID()
                    + "','" + aProfile.getStreet()
                    + "','" + aProfile.getCity()
                    + "','" + aProfile.getState()
                    + "','" + aProfile.getCountry()
                    + "','" + aProfile.getSecurityQue()
                    + "','" + aProfile.getSecurityAns()
                    + "','" + aProfile.getSecurityQue1()
                    + "','" + aProfile.getSecurityAns1()
                    + "')";
            rowCount = stmt.executeUpdate(insertString);
            System.out.println("insert string =" + insertString);
            String query = "INSERT INTO Project353.LoginInfo VALUES ('"
                    + aProfile.getUserID()
                    + "','" + aProfile.getPassword()
                    + "')";
            stmt.executeUpdate(query);
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        // if insert is successful, rowCount will be set to 1 (1 row inserted successfully). Else, insert failed.
        return rowCount;
    }
//
//    @Override
//    public int Purchase() {                     // pixel number storage in database
//        try {
//            Class.forName("org.apache.derby.jdbc.ClientDriver");
//        } catch (ClassNotFoundException e) {
//            System.err.println(e.getMessage());
//            System.exit(0);
//        }
//
//        int rowCount = 0;
//        try {
//            String Project353 = "jdbc:derby://localhost:1527/Project353";
//            Connection DBConn = DriverManager.getConnection(Project353, "itkstu", "student");
//            String insertString;
//            HttpServletRequest httpServletRequest
//                = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        HttpSession session = httpServletRequest.getSession(true);
//            
//            String dName = (String) session.getAttribute("login");
//            int pNum = (int) session.getAttribute("pNum");
//            Statement stmt = DBConn.createStatement();
//            insertString = "INSERT INTO project353.Pixelinfo VALUES ('"
//                    + dName
//                    + "','" + pNum
//                    + "')";
//            rowCount = stmt.executeUpdate(insertString);
//            System.out.println("insert string =" + insertString);
//            DBConn.close();
//        } catch (SQLException e) {
//            System.err.println(e.getMessage());
//        }
//        // if insert is successful, rowCount will be set to 1 (1 row inserted successfully). Else, insert failed.
//        return rowCount;
//    }

    @Override
    public ArrayList findAll() {

        String query = "SELECT * FROM Project353.Users";
        ArrayList aLoginCollection = selectLoginFromDB(query);
        return aLoginCollection;

    }

    private ArrayList selectLoginFromDB(String query) {
        ArrayList aLoginBeanCollection = new ArrayList();
        Connection DBConn = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String Project353 = "jdbc:derby://localhost:1527/Project353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            DBConn = DBHelper.connect2DB(Project353, "itkstu", "student");

            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String fname, lname, userID, password, confirmpwd, email, strt, city, state, country, secque, secans, secq1, seca1;
            LoginBean aLoginBean;
            while (rs.next()) {
                // 1. if a float (say PRICE) is to be retrieved, use rs.getFloat("PRICE");
                // 2. Instead of using column name, can alternatively use: rs.getString(1); // not 0
                fname = rs.getString("firstName");
                lname = rs.getString("lastName");

                userID = rs.getString("UserID");
                password = rs.getString("Password");
                confirmpwd = rs.getString("Password2");
                email = rs.getString("EmailID");
                strt = rs.getString("street");
                city = rs.getString("city");
                state = rs.getString("state");
                country = rs.getString("country");
                secque = rs.getString("SECURITYQUESTION");
                secans = rs.getString("securityAnswer");
                secq1 = rs.getString("SecurityQuestion1");
                seca1 = rs.getString("SecurityAnswer1");

                // make a ProfileBean object out of the values
                aLoginBean = new LoginBean(fname, lname, userID, password, confirmpwd, email, strt, city, state, country, secque, secans, secq1, seca1);
                // add the newly created object to the collection
                aLoginBeanCollection.add(aLoginBean);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("ERROR: Problems with SQL select");
            e.printStackTrace();
        }
        try {
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return aLoginBeanCollection;
    }

    @Override
    public ArrayList findById(String uid) {
        // if interested in matching wild cards, use: LIKE and '%" + aName + "%'";
        String query = "SELECT * FROM Project353.Users ";
        query += "WHERE UserID = '" + uid + "'";

        ArrayList aLoginCollection = selectLoginFromDB(query);
        return aLoginCollection;
    }

    @Override
    public ArrayList findByNumber() {
        // if interested in matching wild cards, use: LIKE and '%" + aName + "%'";
        String query = "SELECT * FROM Project353.PixelInfo ";

        ArrayList aLoginCollection = selectpNumFromDB(query);
        return aLoginCollection;
    }

    @Override
    public ArrayList findByIdUpdate(String uid) {
        // if interested in matching wild cards, use: LIKE and '%" + aName + "%'";
        String query = "SELECT * FROM Project353.Users ";
        query += "WHERE UserID = '" + uid + "'";

        ArrayList aLoginCollection = selectLoginFromDB(query);
        return aLoginCollection;
    }

    private String selectFromDB(String query) {
        //ArrayList aLoginBeanCollection = new ArrayList();
        String userid = "";
        Connection DBConn = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String Project353 = "jdbc:derby://localhost:1527/Project353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            DBConn = DBHelper.connect2DB(Project353, "itkstu", "student");

            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                // 1. if a float (say PRICE) is to be retrieved, use rs.getFloat("PRICE");
                // 2. Instead of using column name, can alternatively use: rs.getString(1); // not 0
                userid = rs.getString("UserID");
                // make a ProfileBean object out of the values
            }
            rs.close();
            stmt.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return userid;
    }

    private ArrayList selectpNumFromDB(String query) {             // retieve number of pixels
        //ArrayList aLoginBeanCollection = new ArrayList();
        ArrayList aPixelBeanCollection = new ArrayList();
        Connection DBConn = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String Project353 = "jdbc:derby://localhost:1527/Project353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            DBConn = DBHelper.connect2DB(Project353, "itkstu", "student");

            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            PixelBean aPixelBean;
            String dName, pNum;
            while (rs.next()) {
                // 1. if a float (say PRICE) is to be retrieved, use rs.getFloat("PRICE");
                // 2. Instead of using column name, can alternatively use: rs.getString(1); // not 0
                pNum = rs.getString("PNUMBER");
                dName = rs.getString("DNAME");
                // make a ProfileBean object out of the values
                aPixelBean = new PixelBean(dName, pNum);
                // add the newly created object to the collection
                aPixelBeanCollection.add(aPixelBean);
            }
            rs.close();
            stmt.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return aPixelBeanCollection;
    }

    private String selectPassFromDB(String query) {
        //ArrayList aLoginBeanCollection = new ArrayList();
        String password = "";
        Connection DBConn = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String Project353 = "jdbc:derby://localhost:1527/Project353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            DBConn = DBHelper.connect2DB(Project353, "itkstu", "student");

            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                // 1. if a float (say PRICE) is to be retrieved, use rs.getFloat("PRICE");
                // 2. Instead of using column name, can alternatively use: rs.getString(1); // not 0
                password = rs.getString("Password");
                // make a ProfileBean object out of the values
            }
            rs.close();
            stmt.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return password;
    }

    @Override
    public String findByIdLogin(String uid) {
        // if interested in matching wild cards, use: LIKE and '%" + aName + "%'";
        String query = "SELECT UserID FROM Project353.LoginInfo ";
        query += "WHERE UserID = '" + uid + "'";

        String aLoginCollection = selectFromDB(query);
        return aLoginCollection;
    }

    @Override
    public String findByPass(String uid) {
        // if interested in matching wild cards, use: LIKE and '%" + aName + "%'";
        String query = "SELECT Password FROM Project353.LoginInfo ";
        query += "WHERE UserID = '" + uid + "'";

        String aLoginCollection = selectPassFromDB(query);
        return aLoginCollection;
    }

    @Override
    public int updateProfile(LoginBean pro) {
        Connection DBConn = null;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        int rowCount = 0;
        try {
            String Project353 = "jdbc:derby://localhost:1527/Project353";
            DBConn = DriverManager.getConnection(Project353, "itkstu", "student");
            String updateString;
            Statement stmt = DBConn.createStatement();

            updateString = "UPDATE Project353.Users SET "
                    + "FirstName = '" + pro.getFirstName() + "', "
                    + "LastName = '" + pro.getLastName() + "', "
                    + "Password = '" + pro.getPassword() + "', "
                    + "Password2 = '" + pro.getConfirmPwd() + "', "
                    + "EmailID = '" + pro.getEmailID() + "', "
                    + "SECURITYQUESTION = '" + pro.getSecurityQue() + "', "
                    + "SecurityAnswer = '" + pro.getSecurityAns() + "' "
                    + "WHERE UserID = '" + pro.getUserID() + "'";
            rowCount = stmt.executeUpdate(updateString);
            System.out.println("updateString =" + updateString);
            String query = "UPDATE Project353.LoginInfo SET "
                    + "Password = '" + pro.getPassword() + "'"
                    + "WHERE UserID = '" + pro.getUserID() + "'";
            stmt.executeUpdate(query);
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        // if insert is successful, rowCount will be set to 1 (1 row inserted successfully). Else, insert failed.
        return rowCount;

    }

    private String selectPixelFromDB(String query) {
        //ArrayList aLoginBeanCollection = new ArrayList();
        String dName = "";
        Connection DBConn = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String Project353 = "jdbc:derby://localhost:1527/Project353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            DBConn = DBHelper.connect2DB(Project353, "itkstu", "student");

            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                // 1. if a float (say PRICE) is to be retrieved, use rs.getFloat("PRICE");
                // 2. Instead of using column name, can alternatively use: rs.getString(1); // not 0
                dName = rs.getString("DName");
                // make a ProfileBean object out of the values
            }
            rs.close();
            stmt.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return dName;
    }

    @Override
    public String findByDName(String dName) {
        // if interested in matching wild cards, use: LIKE and '%" + aName + "%'";
        String query = "SELECT * FROM Project353.PixelInfo ";
        query += "WHERE DName = '" + dName + "'";

        String aPixelCollection = selectPixelFromDB(query);
        return aPixelCollection;
    }

    @Override
    public void middle() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

//        int rowCount = 0;
        try {
            String Project353 = "jdbc:derby://localhost:1527/Project353";
            Connection DBConn = DriverManager.getConnection(Project353, "itkstu", "student");
            String insertString;
            HttpServletRequest httpServletRequest
                    = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session = httpServletRequest.getSession(true);

            String dName = (String) session.getAttribute("login");
            String pNum = (String) session.getAttribute("pNum");
            Statement stmt = DBConn.createStatement();
            insertString = "INSERT INTO project353.Pixelinfo VALUES ('"
                    + pNum
                    + "','" + dName
                    + "')";
            stmt.executeUpdate(insertString);
            System.out.println("insert string =" + insertString);
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        // if insert is successful, rowCount will be set to 1 (1 row inserted successfully). Else, insert failed.
//        return rowCount;
        System.out.println("Values has been saved successfully");
    }

    /**
     * @return the thePixel
     */
    public PixelBean getThePixel() {
        return thePixel;
    }

    /**
     * @param thePixel the thePixel to set
     */
    public void setThePixel(PixelBean thePixel) {
        this.thePixel = thePixel;
    }

}
