/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package dataType_Tests;

import appLogging.AppLogging;
import dataType.DataTypeEnum;
import exceptions.DataTypeException;


import org.apache.logging.log4j.Logger;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


/**
 * Class for testing the DataType package
 * 
 * @author kenna
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataTypeEnumTest {
    
    // Attributes for testing & logging
    private static final Logger logger = AppLogging.logger;
    
    public DataTypeEnumTest() {
        System.out.println("\n\n---------------- DataType Tests ----------------\n");
    }
    
    
    /**
     * Helper method for repetitive tests
     * 
     * @param assertionState
     * @param label 
     */
    private static void handleState(boolean assertionState, String label) {
    
        // Handle test state
        if ( !assertionState ) {
            System.out.println("Test failed '" + label + "' validation");
        }
        else{
            System.out.println("Validation state of '" + label + "' test: " + assertionState);
        }
        assertTrue(assertionState);
    }
    
    
    /**
     * Helper method to test enum validation
     * 
     * @param label
     * @param test
     * @param ref
     * @param query 
     */
    private static boolean testEnum(String label, Object test, Object ref, Object query) {
        
        // Query test case
        boolean assertionState;
        System.out.println("Testing if " + label + " is valid: " + DataTypeEnum.hasType(test));
        int index = DataTypeEnum.indexOfQuery(test);
        System.out.println("Index of " + label + " type is: " + index);
        try {
            DataTypeEnum val = DataTypeEnum.getIndex(index);
            assertionState = true;
            System.out.println("Enum value at that " + label + " is: " + val);
        } catch (DataTypeException ex) {
            assertionState = false;
            System.out.println("Error, unable to find test '" + test + "' in Enum:\n" + ex);
        }
        
        // Compare reference to query
        try {
            int distance = DataTypeEnum.compare(ref, query);
            System.out.println("Distance from '" + query + "', to '" + ref + "' is: " + distance);
            distance = DataTypeEnum.compare(query, ref);
            System.out.println("Distance from '" + ref + "', to '" + query + "' is: " + distance);
            assertionState = true;
        }
        catch (DataTypeException ex) {
            System.out.println("Error, unable to calculate object distance:\n" + ex);
            assertionState = false;
        }
        return assertionState;
    }
    

    /**
     * Test the data type enum
     * 
     */
    @Test
    @Order(1)
    public void testDataTypeEnum() {
    
        // String type validation
        System.out.println("\n====== Testing String Type ======\n");
        boolean assertionState = testEnum("String", "", "def", "abc");
        System.out.println("\n====== Testing String Type ======\n");
        
        
        // Integer type validation
        System.out.println("\n\n====== Testing Int Type ======\n");
        assertionState = testEnum("Int", -1, 20, 8);
        System.out.println("\n====== Testing Int Type ======\n");
        
        
        // Double type validation
        System.out.println("\n====== Testing Double Type ======\n");
        assertionState = testEnum("Double", -1.9, 20.8, 8.2);
        System.out.println("\n====== Testing Double Type ======\n");
        
        
        // Boolean type validation
        System.out.println("\n\n====== Testing Boolean Type ======\n");
        assertionState = testEnum("Boolean", false, true, false);
        System.out.println("\n====== Testing Boolean Type ======\n");
        
        
        // Character type validation
        System.out.println("\n\n====== Testing Character Type ======\n");
        assertionState = testEnum("Character", 'a', '9', '#');
        System.out.println("\n====== Testing Character Type ======\n");
        assertTrue(assertionState);
    }
}
