/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package converterTests;

import converter.supporting.ModelToDatapointConverter;
import converter.supporting.ConverterModel;
import appLogging.AppLogging;
import converter.*;
import datapoint.*;

import exampleModel_Tests.model.Person;
import exceptions.DataTypeException;

import java.util.HashMap;
import java.util.Map;


import org.apache.logging.log4j.Logger;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import testUtils.ExampleModel_Utility;


/**
 * Class for testing ModelToDatapointConverter use of ConverterModel
 * 
 * @author kenna
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatapointFromModel_Test {
    
    
    // Attributes for testing & logging
    private static final Logger logger = AppLogging.logger;
    private static final ModelToDatapointConverter<Person> getterConverter = new ModelToDatapointConverter<>(Person.class);
    
    
    public DatapointFromModel_Test() {
        System.out.println("\n\n---------------- Converter Tests: Datapoint From Model ----------------\n");
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
        System.out.println("\n============== Model to Datapoint: ModelDatapointBridge Test ==============\n");
        Person myPerson = ExampleModel_Utility.makeRandomPerson();
        System.out.println("Created Person for Import:\n\n" + myPerson);
        
        
        // Fetch ConverterModel arr
        ConverterModel[] dataBridge = null;
        int lengthExpected, lengthGot;
        boolean assertionState;
        try {
            System.out.println("\nTesting fetching of ModelDatapointBridge");
            dataBridge = getterConverter.fetchAttribueGetters(myPerson);
            System.out.println("\nPrinting Data Bridge:\n");
            for ( ConverterModel i : dataBridge ) {
                System.out.println(i);
            }
        } catch (DataTypeException ex) {
            System.out.println("\nError fetching data bridge:\n" + ex);
        }
        
        // Test state
        if ( dataBridge != null ) {
            lengthExpected = myPerson.getClass().getDeclaredFields().length;
            lengthGot = dataBridge.length;
            assertionState = lengthExpected == lengthGot;
        }
        else {
            assertionState = false;
        }
        System.out.println("Test status:\t" + assertionState);
        System.out.println("\n============== Model to Datapoint: ModelDatapointBridge Test ==============\n");
        assertTrue(assertionState);
    }
    
    
    /**
     * Test conversion of Model class into Datapoint
     * 
     */
    @Test
    @Order(2)
    public void datapointFromModel_Test() {
        
        // Create person
        System.out.println("\n============== Model to Datapoint: Conversion Test ==============\n");
        boolean assertionState;
        Person myPerson = ExampleModel_Utility.makeRandomPerson();
        System.out.println("\nCreated Person for Import:\n\n" + myPerson);
        
        
        // Convert to datapoint
        Datapoint record = getterConverter.datapointFromModel(myPerson);
        System.out.println("\nCreated Datapoint from Person:\n\n" + record);
        if ( record != null ) {
            assertionState = true;
        }
        else {
            assertionState = false;
        }
        
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n============== Model to Datapoint: Conversion Test ==============\n");
        assertTrue(assertionState);
    }
    
    
    
    @Test
    @Order(3)
    public void datapointAliasUpdate_Test() {
    
        // Create person
        System.out.println("\n============== Model to Datapoint: Alias Update Test ==============\n");
        ModelAttribute test = null;
        boolean assertionState;
        Person myPerson = ExampleModel_Utility.makeRandomPerson();
        System.out.println("\nCreated Person for Import:\n\n" + myPerson);
        
        
        // Create datapoint
        Datapoint record = getterConverter.datapointFromModel(myPerson);
        System.out.println("\nCreated Datapoint from Person:\n\n" + record);
        if ( record != null ) {
            assertionState = true;
        }
        else {
            assertionState = false;
        }
        
        
        // Update alias using map
        System.out.println("\nUpdating Datapoint Aliases");
        Map<String, String> aliasUpdateMap = new HashMap<>();
        aliasUpdateMap.put("fName", "First Name");
        aliasUpdateMap.put("lName", "Last Name");
        aliasUpdateMap.put("mNameInitial", "Middle Initial");
        aliasUpdateMap.put("age", "Age");
        aliasUpdateMap.put("state", "State");
        getterConverter.updateAttributeAliases(record, aliasUpdateMap);
        test = record.getAttribute("Last Name");
        System.out.println("\nAttribute Recieved for 'Last Name':\n\n" + test);
        assertionState = test != null;
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n============== Model to Datapoint: Alias Update Test ==============\n");
        assertTrue(assertionState);
    }
}
