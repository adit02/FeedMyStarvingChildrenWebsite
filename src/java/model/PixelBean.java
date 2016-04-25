/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author it3530108
 */
public class PixelBean {
    // These correspond to the form elements

    private String pixelNumber;
    private String displayName;

    public PixelBean() {
        
    }

    public PixelBean(String pixelNumber) {
        this.pixelNumber = pixelNumber;
    }

    public PixelBean(String dName, String pNum) {
        this.displayName = dName;
        this.pixelNumber = pNum;
    }

    /**
     * @return the pixelNumber
     */
    public String getPixelNumber() {
        return pixelNumber;
    }

    /**
     * @param pixelNumber the pixelNumber to set
     */
    public void setPixelNumber(String pixelNumber) {
        this.pixelNumber = pixelNumber;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
