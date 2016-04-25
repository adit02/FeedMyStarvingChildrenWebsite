/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import model.LoginBean;
import model.PixelBean;

/**
 *
 * @author admin
 */
public interface LoginDAO {
    
    public int signUp(LoginBean aProfile);
    public ArrayList findAll();
    public ArrayList findById(String theModel);   
    public ArrayList findByNumber();   
    public String findByIdLogin(String theModel);
    public String findByPass(String pswd);
    public int updateProfile(LoginBean pro);
    public ArrayList findByIdUpdate(String uid);
    public void middle();

    public String findByDName(String dName);

    
}
