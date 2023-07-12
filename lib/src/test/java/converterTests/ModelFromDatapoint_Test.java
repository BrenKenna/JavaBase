/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package converterTests;

import converter.supporting.ModelToDatapointConverter;
import converter.supporting.DatapointToModelConverter;
import converter.supporting.ConverterModel;
import appLogging.AppLogging;
import converter.*;
import datapoint.Datapoint;

import exampleModel_Tests.model.Person;


import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import testUtils.ExampleModel_Utility;




/**
 * Class for testing DatapointToModelConverter use of ConverterModel
 * 
 * @author kenna
 */
public class ModelFromDatapoint_Test {
    
    private static final Logger logger = AppLogging.logger;
    private static final ModelToDatapointConverter<Person> getterConverter = new ModelToDatapointConverter<>(Person.class);
    private static final DatapointToModelConverter<Person> setterConverter = new DatapointToModelConverter<>(Person.class);
    
    
    public ModelFromDatapoint_Test() {
         System.out.println("\n\n---------------- Converter Tests: Model From Datapoint ----------------\n");
    }
    

    /**
     * Test construction of Mediator Class that supports converting 
     *  Model class to Datapoint, and vice versa
     * 
     */
    @Test
    @Order(1)
    public void modelDatapointBridge_Test() {
    
        // Create Person
        System.out.println("\n============== Datapoint to Model: ModelDatapointBridge Test ==============\n");
        Person myPerson = ExampleModel_Utility.makeRandomPerson();
        Datapoint record = getterConverter.datapointFromModel(myPerson);
        System.out.println("Created Person for Import:\n\n" + myPerson);
        System.out.println("Created Datapoint from Person:\n\n" + record);
        
        
        // Fetch ConverterModel arr
        System.out.println("\nTesting fetching of ModelDatapointBridge");
        ConverterModel[] dataBridge = null;
        int lengthExpected, lengthGot;
        boolean assertionState;
        dataBridge = setterConverter.fetchSetterHolder(record);
        if ( dataBridge != null ) {
            
            // Print data
            System.out.println("\nPrinting Data Bridge:\n");
            for ( ConverterModel i : dataBridge ) {
                System.out.println(i);
            }
            
            // Set test state
            lengthExpected = myPerson.getClass().getDeclaredFields().length;
            lengthGot = dataBridge.length;
            assertionState = lengthExpected == lengthGot;
        }
        else {
            assertionState = false;
        }
        System.out.println("Test status:\t" + assertionState);
        System.out.println("\n============== Datapoint to Model: ModelDatapointBridge Test ==============\n");
        assertTrue(assertionState);
    }
    
    
    /**
     * Test the Reconstruction of Original Model, following Conversion to Datapoint. All attribute values compared
     * 
     */
    @Test
    @Order(2)
    public void modelFromDatapoint_Test() {
        
        // Fetch model and datapoint
        boolean assertionState;
        System.out.println("\n============== Datapoint to Model: Model From Datapoint Test ==============\n");
        Person myPerson = ExampleModel_Utility.makeRandomPerson();
        Datapoint record = getterConverter.datapointFromModel(myPerson);
        System.out.println("Created Person for Import:\n\n" + myPerson);
        System.out.println("\nCreated Datapoint from Person:\n\n" + record);
        
        
        // Update Aliases
        Map<String, String> aliasUpdateMap = new HashMap<>();
        aliasUpdateMap.put("fName", "First Name");
        aliasUpdateMap.put("lName", "Last Name");
        aliasUpdateMap.put("mNameInitial", "Middle Initial");
        aliasUpdateMap.put("age", "Age");
        aliasUpdateMap.put("state", "State");
        getterConverter.updateAttributeAliases(record, aliasUpdateMap);
        
        
        // Convert datapoint to model
        Object reflectedObj = (Object) setterConverter.convertToModel(record);
        System.out.println(reflectedObj);
        Person reflectedPerson = Person.class.cast(reflectedObj);
        
        
        // Compare attribute values
        System.out.println("\nComparing properties of Reflected Person:\n\n" + reflectedPerson);
        assertionState = myPerson.getfName().equals(reflectedPerson.getfName());
        System.out.println("\nFirst Name Match:\t\t" + assertionState);
        assertionState = myPerson.getlName().equals(reflectedPerson.getlName());
        System.out.println("Last Name Match:\t\t" + assertionState);
        assertionState = myPerson.getmNameInitial() == reflectedPerson.getmNameInitial();
        System.out.println("Middle Initial Match:\t" + assertionState);
        assertionState = myPerson.getAge() == reflectedPerson.getAge();
        System.out.println("Age Match:\t\t" + assertionState);
        assertionState = myPerson.getState() == reflectedPerson.getState();
        System.out.println("State Match:\t\t" + assertionState);
        
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n============== Datapoint to Model: Model From Datapoint Test ==============\n");
        assertTrue(assertionState);
    }
    
    
    /**
     * Test using a Generic-Person Instance of DatapointToModelConverter to cast the reflected  Object into Model Class
     * 
     */
    @Test
    @Order(3)
    public void conversionToModel() {
    
        // Fetch model and datapoint
        boolean assertionState;
        System.out.println("\n============== Datapoint to Model: Conversion to Model Test ==============\n");
        Person myPerson = ExampleModel_Utility.makeRandomPerson();
        Datapoint record = getterConverter.datapointFromModel(myPerson);
        System.out.println("Created Person for Import:\n\n" + myPerson);
        System.out.println("\nCreated Datapoint from Person:\n\n" + record);
        
        
        // Update Aliases
        Map<String, String> aliasUpdateMap = new HashMap<>();
        aliasUpdateMap.put("fName", "First Name");
        aliasUpdateMap.put("lName", "Last Name");
        aliasUpdateMap.put("mNameInitial", "Middle Initial");
        aliasUpdateMap.put("age", "Age");
        aliasUpdateMap.put("state", "State");
        getterConverter.updateAttributeAliases(record, aliasUpdateMap);
        
        
        // Convert Datapoint back into Person
        Person reflectedPerson = setterConverter.convert(record);
        if ( reflectedPerson != null ) {
            System.out.println("\nReflected Datapoint back into Person:\n\n" + reflectedPerson);
            assertionState = true;
        }
        else {
            assertionState = false;
        }
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n============== Datapoint to Model: Conversion to Model Test ==============\n");
        assertTrue(assertionState);
    }
}
