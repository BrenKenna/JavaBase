/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package converter.supporting;

import datapoint.Datapoint;
import datapoint.ModelAttribute;
import java.lang.reflect.Method;
import java.util.Map.Entry;


/**
 * Class to support converting between Models and Datapoints
 * 
 * @author kenna
 * @param <T>
 */
public class DatapointToModelConverter<T> {
    
    // Hold reference class
    private final Class refClass;
    
    
    /**
     * Construct with reference class
     * 
     * @param refClass 
     */
    public DatapointToModelConverter(Class refClass){
        this.refClass = refClass;
    }

    
    /**
     * Get the setter method for ModelAttribute
     * 
     * @param modelAttrib
     * @return - Method
     */
    public Method getAttributeSetter(ModelAttribute modelAttrib) {
        
        // Initalize vars
        String setter = "set" + modelAttrib.getAttribName();
        
        // Scan methods until found
        for (Method method : refClass.getDeclaredMethods()) {
            String methodString = method.toString().toLowerCase();
            if ( methodString.contains(setter.toLowerCase()) ) {
                return method;
            }
        }
        
        // Return 
        return null;
    }
    
    
    /**
     * Fetch the Setter Method array for input datapoint
     * 
     * @param input
     * @return - Method[]
     */
    public Method[] fetchSetterdArray(Datapoint input) {
    
        // Initialize to expectation
        Method[] setters = new Method[input.getDatapoint().keySet().size()];
        
        // Populate array
        int counter = 0;
        for(Entry<String, ModelAttribute> modelAttrib : input.getDatapoint().entrySet() ) {
            Method setter = getAttributeSetter(modelAttrib.getValue());
            setters[counter] = setter;
        }
        
        // Return result
        return setters;
    }
    
    
    /**
     * Fetch ConverterModel for setting attributes of Model reflected from Datapoint
     * 
     * @param input
     * @return - ConverterModel[]
     */
    public ConverterModel[] fetchSetterHolder(Datapoint input) {
    
        // Initialize to expectation
        ConverterModel[] setters = new ConverterModel[input.getDatapoint().keySet().size()];
        
        // Populate array
        int counter = 0;
        for(Entry<String, ModelAttribute> modelAttrib : input.getDatapoint().entrySet() ) {
            
            // Initialize setter holder
            ConverterModel setterHolder = new ConverterModel();
            setterHolder.setCanonicalClass(refClass);
            setterHolder.setModelAttrib(modelAttrib.getValue());
            setterHolder.setRef( modelAttrib.getValue().getAttribValue() );
            
            // Fetch setter
            Method setter = getAttributeSetter(modelAttrib.getValue());
            setterHolder.setSetter(setter);
            
            // Add setter
            // System.out.println("Adding Setter Holder:\n\n" + setterHolder);
            setters[counter] = setterHolder;
            counter++;
        }
        
        // Return result
        return setters;
    }
    
    
    /**
     * Reflect Datapoint to original Model Object
     * 
     * @param input
     * @return - Object
     */
    public Object convertToModel(Datapoint input) {
        
        // Initalize output
        Object output;
        Method[] setters = new Method[input.getDatapoint().keySet().size()];
        
        
        // Construct and apply setters
        try {
            
            // Construct Model class
            // System.out.println("\nAttempting Constructor Execution:\n" + input.getCanonicalClass().getConstructor());
            output = refClass.getConstructor().newInstance();
            
            // Fetch setters, and invoke on model
            ConverterModel[] settersHolder = fetchSetterHolder(input);
            for (ConverterModel setterHolder : settersHolder  ) {
                Method setter = setterHolder.getSetter();
                setter.invoke(output, setterHolder.getRef());
            }
            // System.out.println("Object after construction:\n\n" + output);

        } catch (Exception ex) {
            System.out.println("Error encountered during reflective construction:\n");
            ex.printStackTrace();
            output = null;
        }
        
        
        // Return object
        return output;
    }
    
    
    /**
     * Convert Datapoint back into Model Object, represent as type
     * 
     * @param input - Datapoint
     * @return 
     */
    public T convert(Datapoint input) {
        Object model = convertToModel(input);
        return (T) refClass.cast(model);
    }
    
    
    /**
     * Get reference class
     * 
     * @return 
     */
    public Class getRefClass() {
        return refClass;
    }
}
