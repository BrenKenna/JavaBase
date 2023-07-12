/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exampleModel_Tests.model;

/**
 * Example Person model class
 * 
 * @author kenna
 */
public class Person {
    
    // Attributes
    private String fName, lName;
    private char mNameInitial;
    private int age;
    private boolean state;
    
    public Person(){}
    
    
    /**
     * Construct person
     * 
     * @param fName
     * @param lName
     * @param mNameInitial
     * @param age 
     * @param state 
     */
    public Person(String fName, String lName, char mNameInitial, int age, boolean state) {
        this.fName = fName;
        this.lName = lName;
        this.mNameInitial = mNameInitial;
        this.age = age;
        this.state = state;
    }

    
    /**
     * Get first name
     * 
     * @return - String
     */
    public String getfName() {
        return fName;
    }

    
    /**
     * Set first name
     * 
     * @param fName 
     */
    public void setfName(String fName) {
        this.fName = fName;
    }

    
    /**
     * Get last name
     * 
     * @return - String
     */
    public String getlName() {
        return lName;
    }

    
    /**
     * Set last name
     * 
     * @param lName 
     */
    public void setlName(String lName) {
        this.lName = lName;
    }

    
    /**
     * Get age
     * 
     * @return - int 
     */
    public int getAge() {
        return age;
    }

    
    /**
     * Set age
     * @param age 
     */
    public void setAge(int age) {
        this.age = age;
    }

    
    /**
     * Get initial of middle name
     * 
     * @return - char
     */
    public char getmNameInitial() {
        return mNameInitial;
    }

    
    /**
     * Set initial of middle name
     * @param mNameInitial 
     */
    public void setmNameInitial(char mNameInitial) {
        this.mNameInitial = mNameInitial;
    }

    
    /**
     * Get current state
     * 
     * @return - True/False
     */
    public boolean getState() {
        return state;
    }

    
    /**
     * Set state
     * 
     * @param state 
     */
    public void setState(boolean state) {
        this.state = state;
    }


    /**
     * Represent person as String
     * 
     * @return - String
     */
    @Override
    public String toString() {
        return "Person{"
            + "fName=" + fName
            + ", lName=" + lName
            + ", mNameInitial=" + mNameInitial
            + ", state=" + state
            + ", age=" + age
        + '}';
    }
}
