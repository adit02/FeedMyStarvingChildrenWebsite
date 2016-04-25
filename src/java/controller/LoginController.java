/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import dao.LoginDAO;
import dao.LoginDAOImpl;
import java.util.ArrayList;
import javax.faces.application.ConfigurableNavigationHandler;
import mailapp.JavaMailApp;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.LoginBean;
import model.PixelBean;

/**
 *
 * @author it3530108
 */
@ManagedBean
@SessionScoped
public class LoginController {
// This is the model that captures the user profile info

    private LoginBean theModel;
    private PixelBean thePixel;
// This corresponds to the response to be sent back to the client
    private String response;            //display echo page on singup
    private String name;            // display name on login
    private String updateStatus;
    private String availableStat;
    private String printJson;
    private int count = 0;
    private String flag = null;

    /**
     * Creates a new instance of ProfileController
     */
    public LoginController() {
        theModel = new LoginBean(); // There is a better way to handle this; Later ...
        thePixel = new PixelBean();
    }

    /**
     * @return the theModel
     */
    public LoginBean getTheModel() {
        return theModel;
    }

    /**
     * @param theModel the theModel to set
     */
    public void setTheModel(LoginBean theModel) {
        this.theModel = theModel;
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

    /**
     * @return the response
     */
    public String getResponse() {
        String resultStr = "";
        resultStr += "Welcome " + theModel.getFirstName() + "," + theModel.getLastName() + "<br/>";
        resultStr += "Your account has been created successfully with the user ID:" + theModel.getUserID() + "<br/>";
        resultStr += "The security question you have chosen is: " + theModel.getSecurityQue() + "<br/>";
        resultStr += "The security answer provided by you is: " + theModel.getSecurityAns() + "<br/><br/>";
        response = resultStr;
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

    public String getName() {                       // Welcome "User"
        String resultStrng = "";
        LoginDAO aFName = new LoginDAOImpl();
        String usrid = theModel.getUserID();
        String fName = aFName.findByIdLogin(usrid);

        resultStrng += "Welcome " + fName + "<br/>";
        name = resultStrng;
        return name;
    }

    /**
     * @param response the response to set
     */
    public void setName(String response) {
        this.response = response;
    }

    public void checkUserID() {                       //check if user id is available
        LoginDAO aUserID = new LoginDAOImpl();
        String usrid = theModel.getUserID();
        ArrayList check = aUserID.findById(usrid);
        if (check.isEmpty()) {
            availableStat = "User ID available";
        } else {
            availableStat = "User ID not available";
        }
    }

    public String signUp() {

        LoginDAO aUserID = new LoginDAOImpl();
        String usrid = theModel.getUserID();
        ArrayList check = aUserID.findById(usrid);

        if (check.isEmpty()) {

            LoginDAO aProfileDAO = new LoginDAOImpl();    // Creating a new object each time.

            int rowCount = aProfileDAO.signUp(theModel);  // Doing anything with the object after this?

            if (rowCount == 1) {

                JavaMailApp sendMail = new JavaMailApp();
                sendMail.MailApp(theModel.getEmailID());
                return "echo.xhtml?faces-redirect=true"; // navigate to "response.xhtml"
            } else {
                return "error.xhml?faces-redirect=true";
            }
        } else {
            return "notavailable.xhtml?faces-redirect=true";
        }
    }

    public String purchase() {                   // purchase the pixel

        LoginDAO aPixel = new LoginDAOImpl();
        ArrayList pix = aPixel.findByNumber();
        String json = new Gson().toJson(pix);
        printJson = json;
        return printJson;

    }

    public String authenticate() {          //login authentication

        LoginDAO aUserName = new LoginDAOImpl();
        String usrid = theModel.getUserID();
        String check1 = aUserName.findByIdLogin(usrid);

        LoginDAO aNameID = new LoginDAOImpl();
        String check2 = aNameID.findByPass(usrid);

        String usName = theModel.getUserID();
        String psword = theModel.getPassword();
        if ((usName).equals(check1) && psword.equals(check2)) {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletResponse res = (HttpServletResponse) fc.getExternalContext().getResponse();
            Cookie userNameCookie = null;

            try {
                userNameCookie = new Cookie("userName", theModel.getUserID());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            userNameCookie.setMaxAge(-1);
            userNameCookie.setPath("/");

            res.addCookie(userNameCookie);

            return "userPage.xhtml?faces-redirect=true";

        } else {
            count++;
            if (count <= 3) {
                return "LoginBad.xhtml?faces-redirect=true";
            } else {
                return "accountLocked.xhtml?faces-redirect=true";
            }

        }
    }

    public String retrieveProfile() {
        LoginDAO aLoginDAO = new LoginDAOImpl();    // Creating a new object each time.
        String uid = theModel.getUserID();
        ArrayList result = aLoginDAO.findByIdUpdate(uid); // Doing anything with the object after this?
        theModel = (LoginBean) result.get(0); // if multiple found, just pick the 1st one. If none?
        if (theModel != null) {
            return "update.xhtml?faces-redirect=true"; // navigate to "update.xhtml"
        } else {
            return "error.xhtml?faces-redirect=true";
        }
    }

    public void updateProfile() {                     // update profile

        LoginDAO aLoginDAO = new LoginDAOImpl();    // Creating a new object each time.

        int rowCount = aLoginDAO.updateProfile(theModel);  // Doing anything with the object after this?

        if (rowCount == 1) {
            updateStatus = "Updated successfully"; // navigate to "echo.xhtml"
        } else {
            updateStatus = "Error in updation";
        }
    }

    public String isAdmin(ComponentSystemEvent event) {
//        String ck=null;
        HttpServletRequest httpServletRequest
                = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userName")) {
                    String ck = cookie.getValue();
                    if (!ck.equals(null)) {
                        return flag;
//                        FacesContext fc = FacesContext.getCurrentInstance();
//                        ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
//                        nav.performNavigation("pleaseLogin.xhtml?faces-redirect=true");

                    }
                }
            }
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
        nav.performNavigation("pleaseLogin.xhtml?faces-redirect=true");
        return flag;
    }

    public String payNow() {
        String sName = thePixel.getDisplayName();
        String pNum = thePixel.getPixelNumber();
        HttpServletRequest httpServletRequest
                = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("login", sName);
        session.setAttribute("pNum", pNum);

        return "confirmPayment.xhtml";

    }

    public void middle() {
        HttpServletRequest httpServletRequest
                = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = httpServletRequest.getSession(false);
        session.getAttribute("login");
        session.getAttribute("pNum");

        LoginDAO aDName = new LoginDAOImpl();
        String dName = (String) session.getAttribute("login");
//        int pNum = (int) session.getAttribute("pNum");
        String check = aDName.findByDName(dName);

        if (check.isEmpty()) {

            LoginDAO aDNameDAO = new LoginDAOImpl();    // Creating a new object each time.

            aDNameDAO.middle();  // Doing anything with the object after this?

//            if (rowCount == 1) {
//                
//                return "echo.xhtml?faces-redirect=true"; // navigate to "response.xhtml"
//            } else {
//                return "error.xhml?faces-redirect=true";
//            }
        }
//        return "notavailable.xhtml?faces-redirect=true";
    }

    /**
     * @return the updateStatus
     */
    public String getUpdateStatus() {
        return updateStatus;
    }

    /**
     * @param updateStatus the updateStatus to set
     */
    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    /**
     * @return the availableStat
     */
    public String getAvailableStat() {
        return availableStat;
    }

    /**
     * @param availableStat the availableStat to set
     */
    public void setAvailableStat(String availableStat) {
        this.availableStat = availableStat;
    }

    /**
     * @return the printJson
     */
    public String getPrintJson() {
        return printJson;
    }

    /**
     * @param printJson the printJson to set
     */
    public void setPrintJson(String printJson) {
        this.printJson = printJson;
    }

}
