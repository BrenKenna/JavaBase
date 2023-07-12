/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package exampleModel_Tests;

import appLogging.AppLogging;
import datapoint.Datapoint;
import exampleModel_Tests.model.Person;
import exampleModel_Tests.model.Square;
import exceptions.DataTypeException;
import java.util.List;
import testUtils.ExampleModel_Utility;


import org.apache.logging.log4j.Logger;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


/**
 * Class for testing the Datapoint package
 * 
 * @author kenna
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ModelTest {
    
    // Attributes for testing & logging
    private static final Logger logger = AppLogging.logger;

    public ModelTest(){
        System.out.println("\n\n---------------- Model Tests ----------------\n");
    }

    /**
     * Test models
     * 
     */
    @Test
    @Order(1)
    public void testModels() {
    
        // Test square
        boolean assertionState;
        Square mySquare = ExampleModel_Utility.makeRandomSquare();
        System.out.println("Constructed a " + mySquare.getClass().getName() + ":\n\n" + mySquare.toString() + "\n");
        Object test = (Object) mySquare;
        assertionState = ( test.getClass().getName() == mySquare.getClass().getName() );
        System.out.println("Testing Object reference of Square matches original:\t" + assertionState);
        
        
        // Test person
        Person myPerson = ExampleModel_Utility.makeRandomPerson();
        System.out.println("Constructed a " + myPerson.getClass().getName() + ":\n\n" + myPerson.toString() + "\n");
        test = (Object) myPerson;
        assertionState = ( test.getClass().getName() == myPerson.getClass().getName() );
        System.out.println("Displaying Object reference for myPerson: " + test.getClass().getName());
        assertTrue(assertionState);
    }
    
    
    /**
     * Test Datapoint construction from Square
     * 
     */
    @Test
    @Order(2)
    public void testDatapoint_Square() {
    
        // Initialize params
        boolean assertionState;
        Square mySquare;
        Datapoint mySquareDP;
        
        // Construct square
        mySquare = ExampleModel_Utility.makeRandomSquare();
        try {
            mySquareDP = ExampleModel_Utility.makeSquareDatapoint(mySquare);
            assertionState = true;
            System.out.println("Create Datapoint from Square:\n\n" + mySquareDP);
        } catch (DataTypeException ex) {
            assertionState = false;
            System.out.println("Unable to create Datapoint from Square:\n" + ex);
        }
        assertTrue(assertionState);
    }
    
    
    /**
     * Test Datapoint construction from Person
     * 
     */
    @Test
    @Order(3)
    public void testDatapoint_Person() {
    
        // Initialize params
        boolean assertionState;
        Person myPerson;
        Datapoint myPersonDP;
        
        // Construct person
        myPerson = ExampleModel_Utility.makeRandomPerson();
        try {
            myPersonDP = ExampleModel_Utility.makePersonDatapoint(myPerson);
            assertionState = true;
            System.out.println("Create Datapoint from Person:\n\n" + myPersonDP);
        } catch (DataTypeException ex) {
            assertionState = false;
            System.out.println("Unable to create Datapoint from Person:\n" + ex);
        }
        assertTrue(assertionState);
    }
    
    
    /**
     * Test making 5 million people from a parallel stream
     */
    @Test
    @Order(4)
    public void makeFiveMillion_Test() {
        
        // Initialize params
        System.out.println("\nTesting time taken for creating list of 5M Persons");
        List<Person> input;
        List<Datapoint> output;
        double startTime, endTime, duration;
        int sizeOf = 5000000;
        boolean assertionState;
        
        // Create person
        System.out.println("\nCreating Person List:\tN = " + sizeOf);
        startTime = System.nanoTime() / 1e6;
        input = ExampleModel_Utility.makeRandomPersonList(sizeOf);
        endTime = System.nanoTime() / 1e6;
        duration = (endTime - startTime) / 1e3;
        System.out.println("Start Time (ms):\t\t" + startTime);
        System.out.println("End Time (ms):\t\t" + endTime);
        System.out.println("Time taken (s):\t\t" + duration);
        
        // Try run conversion
        try {
            
            // Create datapoints
            System.out.println("\nTesting time taken for converting Person list");
            startTime = System.nanoTime() / 1e6;
            output = ExampleModel_Utility.makePersonDPList(sizeOf);
            endTime = System.nanoTime() / 1e6;
            duration = (endTime - startTime) / 1e3;
            assertionState = true;
            
            // Log time taken
            System.out.println("Start Time (ms):\t\t" + startTime);
            System.out.println("End Time (ms):\t\t" + endTime);
            System.out.println("Time taken (s):\t\t" + duration);
        }
        
        // Otherwise fail test
        catch ( DataTypeException ex ) {
            assertionState = false;
        }
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        assertTrue(assertionState);
    }
}
