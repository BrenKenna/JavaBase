/*
 * Copyright 2023 kenna.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package datasetTests;


import converter.ModelDatapointConverter;
import datapoint.Datapoint;
import dataset.Dataset;
import dataset.Results;
import dataset.Table;
import exampleModel_Tests.model.Person;
import exceptions.DataTypeException;
import testUtils.ExampleModel_Utility;


import appLogging.AppLogging;
import org.apache.logging.log4j.Logger;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Order;



/**
 * Class for testing the Dataset package
 * 
 * @author kenna
 */
public class Dataset_Tests {
    
    // Attributes for testing & logging
    private static final Random rand = new Random();
    private static final Logger logger = AppLogging.logger;
    private static final ModelDatapointConverter<Person> personConverter = new ModelDatapointConverter<>(Person.class);
    
    
    /**
     * Helper method to create Person Attribute-Alias Map
     * 
     * @return - Attribute Alias Map 
     */
    private static Map<String, String> personAliasMap() {
        
        // Initialize output
        Map<String, String> aliasUpdateMap = new HashMap<>();
        
        // Put mappings
        aliasUpdateMap.put("fName", "First Name");
        aliasUpdateMap.put("lName", "Last Name");
        aliasUpdateMap.put("mNameInitial", "Middle Initial");
        aliasUpdateMap.put("age", "Age");
        aliasUpdateMap.put("state", "State");
        
        // Return alias map
        return aliasUpdateMap;
    }
    
    
    /**
     * Helper method to construct list of valid Person Datapoints
     * 
     * @param data
     * @return - List Datapoint
     */
    private static List<Datapoint> personConverter(List<Person> data) {
        
        // Initialize vars
        List<Datapoint> output = new ArrayList<>();
        Map<String, String> aliasMap = Dataset_Tests.personAliasMap();
        
        // Pass input list through the converter
        for ( Person i : data ) {
            Datapoint record = personConverter.convertModel(i, aliasMap);
            if ( record != null ) {
                output.add(record);
            }
        }
        
        // Return Datapoint list
        return output;
    }
    
    
    /**
     * Helpder method to create a list of Datapoints from Person
     * 
     * @param size
     * @return - List Datapoint
     */
    public static List<Datapoint> fetchPersonDatapoints(int size) {
        List<Person> input = ExampleModel_Utility.makeRandomPersonList(size);
        return Dataset_Tests.personConverter(input);
    }
    
    
    public Dataset_Tests() {
        System.out.println("\n\n---------------- Dataset Tests ----------------\n");
    }
    

    /**
     * Test Results-Dataset construction
     */
    @Test
    @Order(1)
    public void resultsConstruction_Test() {
    
        // Initialize vars
        System.out.println("\n================ Dataset-Construction: Results Test ================\n");
        Dataset dataset;
        List<Datapoint> data;
        boolean assertionState;
        
        // Try create random data
        try {
            
            // Dataset created
            data = ExampleModel_Utility.makePersonDPList(30);
            dataset = new Results(data);
            
            // Fetch properties
            int size = dataset.getSize();
            int activeSize = dataset.getActiveSize();
            int droppedSize = dataset.getDroppedSize();
            int expectedSize = data.size();
            
            // Display properties
            System.out.println("Dataset created successfully, displaying properties:\n");
            System.out.println("Dataset Type:\t\t" + dataset.getType());
            System.out.println("Datapoint Counter:\t" + size);
            System.out.println("Datapoint Active:\t\t" + activeSize);
            System.out.println("Datapoint Dropped:\t" + droppedSize);
            System.out.println("Expected Size:\t\t" + expectedSize);
            System.out.println("First Record:\n\n" + dataset.getByIndex(0));
            
            // Measure dataset size
            System.out.println("\nValidating Size metrics");
            if ( size == expectedSize) {
                
                // Check Parallel Stream count
                if ( activeSize == size ) {
                    
                    // Check dropped size
                    assertionState = droppedSize == 0;
                }
                
                // Otherwise Fail
                else {
                    System.out.println("\nError Active Datapoint Size Count does not match Dataset Size");
                    System.out.println("\nDataset size:\t" + size);
                    System.out.println("Active size:\t" + activeSize);
                    System.out.println("Discordance:\t" + ( (size - activeSize)/size ));
                    assertionState = false;
                }
            }
            
            // Fail if different
            else {
                System.out.println("\nError Size of Dataset does not match List");
                System.out.println("List size:\t" + expectedSize);
                System.out.println("Dataset size:\t" + size);
                System.out.println("Discordance:\t" + ( (expectedSize - size)/expectedSize ));
                assertionState = false;
            }
        }
        
        // Otherwise log and fail test
        catch ( DataTypeException ex ) {
            System.out.println("Error creating Peron list:\n\n" + ex);
            assertionState = false;
        }
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n\n================ Dataset-Construction: Results Dataset Test ================\n");
        assertTrue(assertionState);
    }
    
    
    /**
     * Test Table-Dataset construction
     */
    @Test
    @Order(2)
    public void tableConstruction_Test() {
    
        // Initialize vars
        System.out.println("\n================ Dataset-Construction: Table Dataset Test ================\n");
        Dataset dataset;
        List<Datapoint> data;
        boolean assertionState;
        
        // Create random test data
        System.out.println("\nCreating random data");
        data = Dataset_Tests.fetchPersonDatapoints(30);

        // Create Table dataset
        System.out.println("\nCreating Dataset from Person Datapoint List:\tN=" + data.size());
        dataset = new Table(data, "Person", "Random Person");
        
        // Fetch properties
        int size = dataset.getSize();
        int activeSize = dataset.getActiveSize();
        int droppedSize = dataset.getDroppedSize();
        int expectedSize = data.size();
        
        
        // Display properties
        System.out.println("Dataset created successfully, displaying properties:\n");
        System.out.println("Dataset Type:\t\t" + dataset.getType());
        System.out.println("Datapoint Counter:\t" + size);
        System.out.println("Datapoint Active:\t\t" + activeSize);
        System.out.println("Datapoint Dropped:\t" + droppedSize);
        System.out.println("Expected Size:\t\t" + expectedSize);
        System.out.println("First Record:\n\n" + dataset.getByIndex(0));
            
        
        // Validate size
        System.out.println("\nValidating Size metrics");
        if ( size == expectedSize ) {
            
            // Validate active count
            if ( activeSize == size ) {
                assertionState = droppedSize == 0;
            }
            
            // Otherwise fail
            else {
                System.out.println("\nError Active Datapoint Size Count does not match Dataset Size");
                System.out.println("\nDataset size:\t" + size);
                System.out.println("Active size:\t" + activeSize);
                System.out.println("Discordance:\t" + ((size - activeSize) / size));
                assertionState = false;
            }
        }
        
        // Fail if different
        else {
            System.out.println("\nError Size of Dataset does not match List");
            System.out.println("List size:\t" + expectedSize);
            System.out.println("Dataset size:\t" + size);
            System.out.println("Discordance:\t" + ((expectedSize - size) / expectedSize));
            assertionState = false;
        }
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n\n================ Dataset-Construction: Table Test ================\n");
        assertTrue(assertionState);
    }
    
    
    /**
     * Test querying
     */
    @Test
    @Order(3)
    public void queryIntoResults_Test() {
    
        // Initialize vars
        System.out.println("\n================ Dataset-Query: Table Dataset Test ================\n");
        Dataset dataset;
        List<Datapoint> data;
        double nowTime;
        double queryTime, scanTime;
        boolean assertionState;
        
        // Create random test data
        System.out.println("\nCreating random data");
        data = Dataset_Tests.fetchPersonDatapoints(50000);

        // Create Table dataset
        System.out.println("\nCreating Dataset from Person Datapoint List:\tN=" + data.size());
        dataset = new Table(data, "Person", "Random Person");
        
        // Test with random record
        int index = rand.nextInt( dataset.getSize()-1 );
        if ( index == 0) { index = 1; }
        Datapoint record = dataset.getByIndex(index);
        
        // Log record retrieved
        System.out.println("\nTesting Table Queries on Element at Index:\t" + index);
        System.out.println("\nDatapoint Data:\n\n" + record);
        
        // Fetch Results set for each field
        String alias = "First Name";
        String query = (String) record.getAttribute(alias).getAttribValue();
        nowTime = System.nanoTime()/ 1e6;
        Dataset fNameQuery = dataset.query(alias, query, false);
        queryTime = (System.nanoTime()/ 1e6) - nowTime; 
        System.out.println("\nQueried '" + alias + "' of '" + query + "' results:\t N = " + fNameQuery.getSize());
        
        // Verify
        nowTime = System.nanoTime()/ 1e6;
        int nMatching = dataset.scan(alias, query).size();
        scanTime = (System.nanoTime()/ 1e6) - nowTime; 
        assertionState = fNameQuery.getSize() == nMatching;
        System.out.println("\nQuery Time Taken:\t" + queryTime);
        System.out.println("Scan Time Taken:\t" + scanTime);
        System.out.println("Speed Up:\t\t" + scanTime / queryTime);
                
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n\n================ Dataset-Query: Table Test ================\n");
        assertTrue(assertionState);
    }
}
