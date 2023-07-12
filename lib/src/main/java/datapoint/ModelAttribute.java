/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datapoint;

import dataType.DataTypeEnum;
import exceptions.DataTypeException;


/**
 * Model attributes store their value, and meta data
 * 
 * @author kenna
 * @param <T>
 */
public class ModelAttribute<T> {
    
    // Attribute meta data
    private String attribName, alias;
    private DataTypeEnum attribDataType;
    
    // Attribute value
    private T attribValue;
    
    
    /**
     * Default constructor
     */
    public ModelAttribute(){}
    
    
    /**
     * Construct ModelAttribute with Reference name and Value
     * 
     * @param attribName
     * @param attribValue
     * @throws DataTypeException 
     */
    public ModelAttribute(String attribName, T attribValue) throws DataTypeException {
        this.attribName = attribName;
        this.attribValue = attribValue;
        this.attribDataType = DataTypeEnum.getValue(attribValue);
        this.alias = null;
    }
    
    
    /**
     * Construct ModelAttribute from name, alias and value
     * 
     * @param attribName
     * @param alias
     * @param value
     * @throws DataTypeException 
     */
    public ModelAttribute(String attribName, String alias, T value) throws DataTypeException {
        this.attribName = attribName;
        this.alias = alias;
        this.attribValue = value;
        this.attribDataType = DataTypeEnum.getValue(value);
    }
    
    
    /**
     * Full model attribute constructor
     * 
     * @param value
     * @param attribName
     * @param alias
     * @param attribDataType 
     */
    public ModelAttribute(T value, String attribName, String alias, DataTypeEnum attribDataType) {
        this.attribValue = value;
        this.attribName = attribName;
        this.alias = alias;
        this.attribDataType = attribDataType;
    }

    
    /**
     * Check if value of self is the same as the query
     * 
     * @param query
     * @return - True/False
     */
    public boolean isEqual(ModelAttribute query) {
        return this.attribDataType.equals( query.getAttribValue() );
    }
    
    
    /**
     * Compare self to a query
     * 
     * @param query
     * @return - Distance as integer
     * @throws exceptions.DataTypeException 
     */
    public int compareTo(ModelAttribute query) throws DataTypeException {
        return this.attribDataType.compareTo(attribValue, query.getAttribValue());
    }
    
    
    /**
     * Return a copy of this ModelAttribute
     * 
     * @return - ModelAttribute
     */
    public ModelAttribute copyOf() {
        T val = getAttribValue();
        String attrib = getAttribName();
        String name = getAlias();
        DataTypeEnum typeOf = getAttribDataType();
        return new ModelAttribute(val, attrib, name, typeOf);
    }
    
    
    /**
     * 
     * Getters & Setters Zone
     * 
     */
    
    
    /**
     * Represent class as a string
     * 
     * @return 
     */
    @Override
    public String toString() {
        return "ModelAttribute{" +
            "attribName=" + attribName +
            ", alias=" + alias +
            ", attribDataType=" + attribDataType +
            ", attribValue=" + attribValue +
        '}';
    }

    public String getAttribName() {
        return attribName;
    }

    public void setAttribName(String attribName) {
        this.attribName = attribName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public DataTypeEnum getAttribDataType() {
        return attribDataType;
    }

    public void setAttribDataType(DataTypeEnum attribDataType) {
        this.attribDataType = attribDataType;
    }

    public T getAttribValue() {
        return attribValue;
    }

    public void setAttribValue(T attribValue) {
        this.attribValue = attribValue;
    }
}
