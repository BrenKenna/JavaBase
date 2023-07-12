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
import dataset.Results;

import exampleModel_Tests.model.Person;

import java.util.Random;
import org.apache.logging.log4j.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import testUtils.DatasetUtility_ExampleModel;


/**
 * Class for testing Drop/Restore operations
 * 
 * @author kenna
 */
public class DatasetDropRestore_Tests {
    
    // Attributes for testing & logging
    private static final Random rand = new Random();
    private static final Logger logger = AppLogging.logger;
    private static final ModelDatapointConverter<Person> personConverter = new ModelDatapointConverter<>(Person.class);
    
    public DatasetDropRestore_Tests() {
        System.out.println("\n\n---------------- Dataset Drop/Restore Tests ----------------\n");
    }
    
    
    /**
     * Test metrics following dropping a single record
     */
    @Test
    @Order(1)
    public void dropOne_MetricsTest() {
    
        // Initialize vars
        System.out.println("\n================ Dataset-Drop/Restore: Table Drop Test ================\n");
        Dataset dataset;
        Datapoint droppedRecord;
        int sizeBefore, sizeAfter, droppedSize;
        boolean assertionState;
        
        // Create dataset
        dataset = DatasetUtility_ExampleModel.fetchTable("Person", "Random People", 500);
        sizeBefore = dataset.getSize();
        System.out.println("\nCreated dataset for testing, displaying size metrics");
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        
        // Drop first record
        System.out.println("\nDropping first record from View:\n");
        droppedRecord = dataset.getByIndex(0);
        dataset.dropByIndex(0);
        System.out.println(droppedRecord);
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        sizeAfter = dataset.getActiveSize();
        
        // Verify dropping
        assertionState = sizeAfter == (sizeBefore -1);
        System.out.println("\nChecking Active size after, is less 1:\t" + assertionState);
        droppedSize = dataset.getDroppedSize();
        assertionState = droppedSize == 1;
        System.out.println("Checking Dropped size is 1:\t\t" + assertionState);
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n\n================ Dataset-Drop/Restore: Table Drop Test ================\n");
        assertTrue(assertionState);
    }
    
    
    /**
     * Test metrics following the restoration of a dropped datapoint
     */
    @Test
    @Order(2)
    public void restoreDatapoint_MetricsTest() {
    
        // Initialize vars
        System.out.println("\n================ Dataset-Drop/Restore: Table Restore Drop Test ================\n");
        Dataset dataset;
        Datapoint droppedRecord;
        int sizeBefore, sizeAfter, droppedSize;
        boolean assertionState;
        
        // Create dataset
        dataset = DatasetUtility_ExampleModel.fetchTable("Person", "Random People", 500);
        sizeBefore = dataset.getSize();
        System.out.println("\nCreated dataset for testing, displaying size metrics");
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        
        // Drop first record
        System.out.println("\nDropping first record from View:\n");
        droppedRecord = dataset.getByIndex(0);
        dataset.dropByIndex(0);
        System.out.println(droppedRecord);
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        
        // Restore record
        System.out.println("\nRestoring Record:\n");
        dataset.restoreDatapoint(0);
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        
        // Verify size metrics
        System.out.println("\nVerifying the Size Metrics Following Restoration");
        sizeAfter = dataset.getActiveSize();
        droppedSize = dataset.getDroppedSize();
        assertionState = sizeAfter == sizeBefore;
        System.out.println("Verifying Size After == Size Before:\t" + assertionState);
        assertionState = droppedSize == 0;
        System.out.println("Veriying that the Dropped Size is 0:\t" + assertionState);
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n\n================ Dataset-Drop/Restore: Table Restore Drop Test ================\n");
        assertTrue(assertionState);
    }
    
    
    /**
     * Test that the Results Dataset is smaller following deletion of first record
     * 
     */
    @Test
    @Order(3)
    public void viewFollowingDrop_Test() {
    
        // Initialize vars
        System.out.println("\n================ Dataset-Drop/Restore: Results Drop View ================\n");
        Dataset dataset;
        Datapoint droppedRecord;
        Results queriedResults;
        String field = "First Name";
        Object queryVal;
        int sizeBefore, sizeAfter;
        boolean assertionState;
        
        // Configure test data
        dataset = DatasetUtility_ExampleModel.fetchTable("Person", "Random People", 500);
        System.out.println("\nCreated dataset for testing");
        
        // Configure dropped record
        System.out.println("\nQuerying first record of data into a Results-Dataset:\n");
        droppedRecord = dataset.getByIndex(0);
        queryVal = droppedRecord.getAttribute(field).getAttribValue();
        queriedResults = dataset.query(field, queryVal, true);
        sizeBefore = queriedResults.getSize();
        DatasetUtility_ExampleModel.printDatasetStats(queriedResults);
        
        // Drop record
        System.out.println("\nSummarizing Same Query Following First Record Drop");
        dataset.dropByIndex(0);
        queriedResults = dataset.query(field, queryVal, true);
        sizeAfter = queriedResults.getSize();
        DatasetUtility_ExampleModel.printDatasetStats(queriedResults);
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        
        // Verify size metrics
        System.out.println("\nVerifying Size Metrics Following Drop");
        assertionState = sizeAfter == (sizeBefore-1);
        System.out.println("Size of Results before Drop:\t" + sizeBefore);
        System.out.println("Size after:\t\t\t" + sizeAfter);
        System.out.println("Size after == size before less 1:\t" + assertionState);
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n\n================ Dataset-Drop/Restore: Results Drop View ================\n");
        assertTrue(assertionState);
    }
    
    
    /**
     * View following dropping all records matching query
     * 
     */
    @Test
    @Order(4)
    public void viewFollowingQueryDrop_Test() {
    
        // Initialize vars
        System.out.println("\n================ Dataset-Drop/Restore: Results Drop from Query View ================\n");
        Dataset dataset;
        Datapoint droppedRecord;
        Results queriedResults;
        String field = "First Name";
        Object queryVal;
        int sizeBefore, sizeAfter;
        boolean assertionState;
    
        // Configure test data
        dataset = DatasetUtility_ExampleModel.fetchTable("Person", "Random People", 500);
        System.out.println("\nCreated dataset for testing");
        
        // Configure dropped record
        System.out.println("\nQuerying first record of data into a Results-Dataset:\n");
        droppedRecord = dataset.getByIndex(0);
        queryVal = droppedRecord.getAttribute(field).getAttribValue();
        queriedResults = dataset.query(field, queryVal, true);
        sizeBefore = queriedResults.getSize();
        DatasetUtility_ExampleModel.printDatasetStats(queriedResults);
        
        // Drop all records matching first
        System.out.println("\nFetching Table View after Dropping all Records matching first name");
        assertionState = dataset.dropDatapoint(field, queryVal);
        System.out.println("Drop state:\t" + assertionState);
        queriedResults = dataset.query(field, queryVal, true);
        sizeAfter = queriedResults.getSize();
        
        // Verify metrics following drop
        System.out.println("\nVerifying Size Metrics Following Drop");
        assertionState = sizeAfter == 0;
        System.out.println("Size of Results before Drop:\t" + sizeBefore);
        System.out.println("Size after:\t\t\t" + sizeAfter);
        System.out.println("Size after is 0:\t\t\t" + assertionState);
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n\n================ Dataset-Drop/Restore: Results Drop from Query View ================\n");
        assertTrue(assertionState);
    }
    
    
    /**
     * Test the restoration of records that were dropped for having matched query
     * 
     */
    @Test
    @Order(5)
    public void restoreDroppedByQuery_Test() {
    
        // Initialize vars
        System.out.println("\n================ Dataset-Drop/Restore: Restore Dropped from Query, Results View ================\n");
        Dataset dataset;
        Datapoint droppedRecord;
        Results queriedResults;
        String field = "First Name";
        Object queryVal;
        int sizeBefore, sizeAfter, sizeRestored;
        boolean assertionState;
        
        // Configure test data
        dataset = DatasetUtility_ExampleModel.fetchTable("Person", "Random People", 500);
        System.out.println("\nCreated dataset for testing");
        
        // Configure dropped record
        System.out.println("\nQuerying first record of data into a Results-Dataset:\n");
        droppedRecord = dataset.getByIndex(0);
        queryVal = droppedRecord.getAttribute(field).getAttribValue();
        queriedResults = dataset.query(field, queryVal, true);
        sizeBefore = queriedResults.getSize();
        DatasetUtility_ExampleModel.printDatasetStats(queriedResults);
        
        // Issue drop
        System.out.println("\nFetching Table View after Dropping all Records matching first name");
        assertionState = dataset.dropDatapoint(field, queryVal);
        System.out.println("Drop state:\t" + assertionState);
        queriedResults = dataset.query(field, queryVal, true);
        sizeAfter = queriedResults.getSize();
        
        // Restore
        System.out.println("\nRestoring deleted datapoints:");
        assertionState = dataset.restoreDatapoint(field, queryVal);
        System.out.println("\nRestore state:\t" + assertionState);
        queriedResults = dataset.query(field, queryVal, true);
        sizeRestored = queriedResults.getSize();
        DatasetUtility_ExampleModel.printDatasetStats(queriedResults);
        
        // Verify restoration metrics
        System.out.println("\nVerifying Restoration Metrics:");
        assertionState = sizeRestored == sizeBefore;
        System.out.println("Size of Results before Drop:\t" + sizeBefore);
        System.out.println("Size after:\t\t\t" + sizeAfter);
        System.out.println("Size following restoration:\t" + sizeRestored);
        System.out.println("Restored size == size Before:\t" + assertionState);
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n\n================ Dataset-Drop/Restore: Restore Dropped from Query, Results View ================\n");
        assertTrue(assertionState);
    }
}
