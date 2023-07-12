/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datapoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A Datapoint is a HashMap of ModelAttributes
 * 
 * @author kenna
 */
public class Datapoint {
    
    // Is a Map of Model Atrributes
    private final Map<String, ModelAttribute> datapoint;
    private String canonicalClass;
    private DatapointState state;
    
    
    /**
     * Default constructor
     */
    public Datapoint(){
        this.datapoint = new HashMap<>();
        this.canonicalClass = null;
        this.state = DatapointState.ACTIVE;
    };
    
    
    /**
     * Construct Datapoint from Map
     * 
     * @param datapoint 
     */
    public Datapoint(Map<String, ModelAttribute> datapoint){
        this.datapoint = datapoint;
        this.canonicalClass = null;
        this.state = DatapointState.ACTIVE;
    }

    
    /**
     * Costruct with canonical class, storing its name
     * 
     * @param datapoint
     * @param canonicalClass 
     */
    public Datapoint(Map<String, ModelAttribute> datapoint, Class canonicalClass){
        this.datapoint = datapoint;
        this.canonicalClass = canonicalClass.getName();
        this.state = DatapointState.ACTIVE;
    }
    
    
    /**
     * Construct with changeable canonical class reference
     * 
     * @param datapoint
     * @param canonicalClass 
     */
    public Datapoint(Map<String, ModelAttribute> datapoint, String canonicalClass){
        this.datapoint = datapoint;
        this.canonicalClass = canonicalClass;
        this.state = DatapointState.ACTIVE;
    }
    
    
    /**
     * Get Datapoint Map
     * 
     * @return - Datapoint Map
     */
    public Map<String, ModelAttribute> getDatapoint() {
        return datapoint;
    }

    
    /**
     * Add datapoint if not present
     * 
     * @param label
     * @param data
     * @return - True/False
     */
    public boolean addAttribute(String label, ModelAttribute data) {
        if ( datapoint.containsKey(label) ) {
            return false;
        }
        datapoint.put(label, data);
        return true;
    }
    
    
    /**
     * Replace ModelAttribute with new value if present
     * 
     * @param label
     * @param data
     * @return - True/False
     */
    public boolean updateAttribute(String label, ModelAttribute data) {
        if ( !datapoint.containsKey(label) ) {
            return false;
        }
        ModelAttribute old = datapoint.get(label);
        datapoint.replace(label, old, data);
        return true;
    }
    
    
    /**
     * Drop attribute if present
     * 
     * @param label
     * @return - True/False
     */
    public boolean dropAttribute(String label) {
        if ( !datapoint.containsKey(label) ) {
            return false;
        }
        datapoint.remove(label);
        return true;
    }
    
    
    /**
     * Check if queried value for field is valid
     * 
     * @param label
     * @param query
     * @return - True/False
     */
    public boolean isValid(String label, Object query) {
        ModelAttribute refAttrib = datapoint.get(label);
        if (refAttrib == null) {
            return false;
        }
        return refAttrib.getAttribDataType().isType(query);
    }
    
    
    /**
     * Get ModelAttribute if present
     * 
     * @param label
     * @return - ModelAttribute 
     */
    public ModelAttribute getAttribute(String label) {
        if ( !datapoint.containsKey(label) ) {
            return null;
        }
        return datapoint.get(label);
    }

    
    /**
     * Get current model canonical class reference
     * 
     * @return - Canonical Class 
     */
    public String getCanonicalClass() {
        return canonicalClass;
    }

    
    /**
     * Set new canonical class reference
     * 
     * @param ofClass 
     */
    public void setCanonicalClass(String ofClass) {
        this.canonicalClass = ofClass;
    }

    
    /**
     * Get Active, Dropped state of Datapoint
     * 
     * @return - DatapointState 
     */
    public DatapointState getState() {
        return this.state;
    }
    
    
    /**
     * Set new Active, Dropped state of Datapoint
     * 
     * @param newState 
     */
    public void setState(DatapointState newState) {
        this.state = newState;
    }
    
    
    /**
     * Mark Datapoint as dropped if Active
     * 
     */
    public void dropDatapoint() {
        if ( this.state == DatapointState.ACTIVE ) {
            this.state = DatapointState.DROPPED;
        }
    }
    
    
    /**
     * Restore datapoint if marked as dropped
     * 
     */
    public void restoreDatapoint() {
        if ( this.state == DatapointState.DROPPED ) {
            this.state = DatapointState.ACTIVE;
        }
    }
    
    
    /**
     * Check whether Datapoint is in an Active state
     * 
     * @return - True/False
     */
    public boolean isActive() {
        return this.state == DatapointState.ACTIVE;
    }
    
    
    /**
     * Check whether Datapoint is in a Dropped state
     * 
     * @return - True/False
     */
    public boolean isDropped() {
        return this.state == DatapointState.DROPPED;
    }
    
    
    /**
     * Get Datapoint attribute aliases
     * 
     * @return - Set String
     */
    public Set<String> getAliases() {
        return this.datapoint.keySet();
    }

    
    /**
     * Return a copy of the Datapoint Map
     * 
     * @return - copy of the Map-String, ModelAttribute
     */
    public Map<String, ModelAttribute> copyMap() {
        Map<String, ModelAttribute> output = new HashMap<>();
        for ( Entry<String, ModelAttribute> i : datapoint.entrySet() ) {
            ModelAttribute copy = i.getValue().copyOf();
            output.put(copy.getAlias(), copy);
        }
        return output;
    }
    
    
    /**
     * Return a copy of this Datapoint
     * 
     * @return - Datapoint
     */
    public Datapoint copyOf() {
        Map<String, ModelAttribute> dpCopy = copyMap();
        String classCP = getCanonicalClass();
        return new Datapoint(dpCopy, classCP);
    
    } 
    
    
    /**
     * Represent Datapoint as String
     * 
     * @return - String
     */
    @Override
    public String toString() {
        return "Datapoint{" + 
            "canonicalClass=" + canonicalClass +
            ", state=" + state +
            ", datapoint=" + datapoint +
        '}';
    }
}
