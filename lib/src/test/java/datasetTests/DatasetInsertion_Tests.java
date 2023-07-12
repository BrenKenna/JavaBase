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

import appLogging.AppLogging;
import converter.ModelDatapointConverter;
import datapoint.Datapoint;
import dataset.Dataset;
import dataset.Table;
import exampleModel_Tests.model.Person;
import exceptions.DataTypeException;
import indexInterface.BinarySearchTree;
import indexInterface.Index;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import org.apache.logging.log4j.Logger;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Order;
import testUtils.DatasetUtility_ExampleModel;
import testUtils.ExampleModel_Utility;

/**
 *
 * @author kenna
 */
public class DatasetInsertion_Tests {
    
    // Attributes for testing & logging
    private static final Random rand = new Random();
    private static final Logger logger = AppLogging.logger;
    private static final ModelDatapointConverter<Person> personConverter = new ModelDatapointConverter<>(Person.class);
    
    public DatasetInsertion_Tests() {
        System.out.println("\n\n---------------- Dataset Insertion Tests ----------------\n");
    }
    
    
    /**
     * Test adding a new record
     * 
     */
    @Test
    @Order(1)
    public void insertDatapoint_test() {
    
        // Initialize vars
        System.out.println("\n================ Dataset-Insertion: Add Single Record ================\n");
        int sizeBefore, sizeAfter;
        Datapoint newRecord;
        boolean assertionState;
        Dataset dataset = DatasetUtility_ExampleModel.fetchTable("Person", "Random People", 500);
        sizeBefore = dataset.getSize();
        System.out.println("\nCreated dataset for testing, displaying size metrics");
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        
        
        // Add a new datapoint
        try {
            
            // Create and log
            newRecord = ExampleModel_Utility.makeRandomPersonDP();
            System.out.println("\nCreated new person record for insertion:\n\n" + newRecord);
            
            // Insert record
            assertionState = dataset.addNewDatapoint(newRecord);
            System.out.println("\nNew record insertion state:\t" + assertionState);
            DatasetUtility_ExampleModel.printDatasetStats(dataset);
            sizeAfter = dataset.getSize();
            assertionState = sizeAfter == (sizeBefore + 1); 
        }
        
        // Otherwise fail test
        catch (DataTypeException ex) {
            System.out.println("\nError creating a new Person record:\n" + ex);
            assertionState = false;
        }
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n\n================ Dataset-Insertion: Add Single Record ================\n");
        assertTrue(assertionState);
    }
    
    
    
    @Test
    @Order(2)
    public void insertDatapoint_IndexTest() {
    
        // Initialize vars
        System.out.println("\n================ Dataset-Insertion: Index Sizes ================\n");
        Datapoint newRecord;
        boolean assertionState;
        Dataset dataset = DatasetUtility_ExampleModel.fetchTable("Person", "Random People", 500);
        System.out.println("\nCreated dataset for testing, displaying size metrics");
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
    
        // Add a new datapoint
        try {
            
            // Insert record
            newRecord = ExampleModel_Utility.makeRandomPersonDP();
            assertionState = dataset.addNewDatapoint(newRecord);
            
            // Iteratively check index sizes
            System.out.println("\nDisplaying the Size of BST after Insertion:");
            Index myIndex = ((Table) dataset).getIndex();
            for (Entry<String, BinarySearchTree> tree : myIndex.getIndex().entrySet() ) {
                System.out.println("Size of '" + tree.getKey() + "' tree after insertion:\t" + tree.getValue().getTreeSize());
            }
        }
        
        // Otherwise fail test
        catch (DataTypeException ex) {
            System.out.println("\nError creating a new Person record:\n" + ex);
            assertionState = false;
        }
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n\n================ Dataset-Insertion: Index Sizes ================\n");
        assertTrue(assertionState);
    }
    
    
    /**
     * Test extending a Dataset with a list of Datapoints
     */
    @Test
    @Order(3)
    public void extendDataset_Test() {
    
        
        // Initialize vars
        System.out.println("\n================ Dataset-Insertion: Extend Table ================\n");
        int sizeBefore, sizeAfter;
        List<Datapoint> newData;
        boolean assertionState = true;
        Dataset dataset = DatasetUtility_ExampleModel.fetchTable("Person", "Random People", 500);
        sizeBefore = dataset.getSize();
        System.out.println("\nCreated dataset for testing, displaying size metrics");
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        
        // Extend Dataset with new data
        System.out.println("\nExtending Dataset with an Additional 30 records");
        newData = DatasetUtility_ExampleModel.fetchPersonDatapoints(30);
        dataset.extend(newData);
        
        // Display state after update
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        sizeAfter = dataset.getSize();
        assertionState = sizeAfter == (sizeBefore + 30);
        System.out.println("\nVerifying size after is 30 higher than before:\t" + assertionState);
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n\n================ Dataset-Insertion: Extend Table ================\n");
        assertTrue(assertionState);
    }
}
