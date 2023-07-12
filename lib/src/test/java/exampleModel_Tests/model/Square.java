/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exampleModel_Tests.model;

/**
 * Example Square model class
 * 
 * @author kenna
 */
public class Square {
    
    // Attributes
    private int length, width;
    private double area;
    
    
    /**
     * Construct square
     * 
     * @param length
     * @param width 
     */
    public Square(int length, int width) {
        this.length = length;
        this.width = width;
        this.area = (length * width);
    }

    
    /**
     * Get length
     * 
     * @return - int 
     */
    public int getLength() {
        return length;
    }

    
    /**
     * Set length
     * @param length 
     */
    public void setLength(int length) {
        this.length = length;
    }

    
    /**
     * Get width
     * 
     * @return - int 
     */
    public int getWidth() {
        return width;
    }

    
    /**
     * Set width
     * 
     * @param width 
     */
    public void setWidth(int width) {
        this.width = width;
    }

    
    /**
     * Get area
     * 
     * @return - double 
     */
    public double getArea() {
        return area;
    }

    
    /**
     * Set area
     * 
     * @param area 
     */
    public void setArea(double area) {
        this.area = area;
    }

    
    /**
     * Represent square as string
     * 
     * @return - String
     */
    @Override
    public String toString() {
        return "Square{"
            + "length=" + length
            + ", width=" + width
            + ", area=" + area
        + '}';
    }
}
