/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package converter.supporting;

import datapoint.Datapoint;
import datapoint.ModelAttribute;
import exceptions.DataTypeException;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Class to support conversions between an input Model into a Datapoint
 * 
 * @author kenna
 * @param <T>
 */
public class ModelToDatapointConverter<T> {
    
    // Hold reference class
    private final Class refClass;
    
    
    /**
     * Construct with reference class
     * 
     * @param refClass 
     */
    public ModelToDatapointConverter(Class refClass){
        this.refClass = refClass;
    }

    
    /**
     * Invoke supplied Getter Method on supplied Model Class
     * 
     * @param input
     * @param getter
     * @return - Value of Attribute as Object
     */
    public Object getModelValue(Object input, Method getter) {
        
        // Initialize output
        Object output;
        
        // Invoke getter
        try {
            // System.out.println("\nInvoking:\t" + getter + "\nOver input:\t" + input);
            output = getter.invoke(input);
        }
        
        // Otherwise null
        catch (Exception ex) {
            output = null;
        }
        
        // Return output
        return output;
    }
    
    
    /**
     * Fetch ConverterModel Array for converting an input Model object into a Datapoint
     * 
     * @param input
     * @return - ModelDatapointridge[]
     * @throws exceptions.DataTypeException
     */
    public ConverterModel[] fetchAttribueGetters(Object input) throws DataTypeException {
        
        // Initialize vars
        ConverterModel[] output = new ConverterModel[input.getClass().getDeclaredFields().length];
        
        // Add model datapoint bridge element for each field
        int counter = 0;
        for ( Field field : input.getClass().getDeclaredFields() ) {
            
            // Instantiate output array element
            ConverterModel getterHolder = new ConverterModel();
            
            // Set canonical class property
            getterHolder.setCanonicalClass(input.getClass().getClass());
            
            // Fetch getter for field
            Method getter = fetchAttributeGetter(input, field.getName());
            getterHolder.setGetter(getter);
            
            // Invoke getter on model object
            // System.out.println("Args for getModelValue:\nInput:\t" + input + "\nGetter:\t" + getterHolder.getGetter());
            Object attribValue = getModelValue(input, getter);
            getterHolder.setRef(attribValue);
            
            // Initialize its ModelAttribute: Using field name as the alias
            // System.out.println("\nInputs for ModelAttriute Construction:\nField:\t" + field.getName() + "\nAttribute:\t" + attribValue);
            // System.out.println("\nModelDatapointBridge:\n" + getterHolder);
            ModelAttribute modelAttrib = new ModelAttribute(field.getName(), attribValue);
            modelAttrib.setAlias(field.getName());
            getterHolder.setModelAttrib(modelAttrib);
            
            // Append and increment
            output[counter] = getterHolder;
            counter++;
        }
        
        // Return output
        return output;
    }
    
    
    /**
     * Fetch the getter for attribute name
     * 
     * @param input
     * @param attribute
     * @return - Method
     */
    public Method fetchAttributeGetter(Object input, String attribute) {
        
        // Initalize vars
        String attribGetter = "get" + attribute;
        
        // Scan methods for attribute
        for ( Method method : input.getClass().getDeclaredMethods() ) {
            String methodString = method.toString().toLowerCase();
            if ( methodString.contains(attribGetter.toLowerCase()) ) {
                return method;
            }
        }
        return null;
    }
    
    
    /**
     * Make ModelAttribute from input params from Model
     * 
     * @param input
     * @param alias
     * @param attribute
     * @return - ModelAttribute
     */
    public ModelAttribute fetchModelAttribute(Object input, String alias, String attribute) {
        
        // Initalize output
        ModelAttribute output = new ModelAttribute();
        output.setAlias(alias);
        output.setAttribName(attribute);
        
        // Return output
        return output;
    }
        
    
    /**
     * Update alias on datapoint
     * 
     * @param attribName
     * @param alias
     * @param input 
     */
    public void updateAttributeAlias(String attribName, String alias, Datapoint input) {
        ModelAttribute modelAttrib = input.getDatapoint().remove(attribName);
        modelAttrib.setAlias(alias);
        input.addAttribute(alias, modelAttrib);
    }
    
    
    /**
     * Set ModelAttribute aliases of Datapoint according to input map
     * 
     * @param input
     * @param fieldAliasMap - Key = FieldName, Value = Alias
     */
    public void updateAttributeAliases(Datapoint input, Map<String, String> fieldAliasMap) {
        String attribName, alias;
        for ( Entry<String, String> i : fieldAliasMap.entrySet() ) {
            attribName = i.getKey();
            alias = i.getValue();
            updateAttributeAlias(attribName, alias, input);
        }
    }
    
    
    /**
     * Fetch Datapoint from input Model Object. Alias intialized to field name
     * 
     * @param input
     * @return 
     */
    public Datapoint datapointFromModel(Object input) {
    
        // Initialize output
        Datapoint output = new Datapoint();
        
        // Fetch Model -> Datapoint Bridge array
        try {
            
            // Set class property
            ConverterModel[] getterHolder = fetchAttribueGetters(input);
            output.setCanonicalClass(input.getClass().getName());
            
            // Iteratively ModelAttributes
            for ( ConverterModel attribGetter : getterHolder ) {
                output.addAttribute(attribGetter.getModelAttrib().getAttribName(), attribGetter.getModelAttrib());
            }
        }
        catch (Exception ex) {
            output = null;
        }
        
        // Return datapoint
        return output;
    }
    
    
    /**
     * Return Datapoint from ModelAttribute Map
     * 
     * @param modelAttribMap
     * @return - Datapoint
     */
    public Datapoint fetchDatapoint(Map<String, ModelAttribute> modelAttribMap) {
        return new Datapoint(modelAttribMap, refClass);
    }
    
    
    /**
     * Get reference class
     * 
     * @return - Class
     */
    public Class getRefClass() {
        return refClass;
    }
}
