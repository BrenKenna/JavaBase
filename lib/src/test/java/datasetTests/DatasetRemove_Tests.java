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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Order;
import testUtils.DatasetUtility_ExampleModel;

/**
 * Class to test deleting/removing records
 * 
 * @author kenna
 */
public class DatasetRemove_Tests {
    
    // Attributes for testing & logging
    private static final Random rand = new Random();
    private static final Logger logger = AppLogging.logger;
    
    public DatasetRemove_Tests() {
        System.out.println("\n\n---------------- Dataset Deletion Tests ----------------\n");
    }
    
    
    /**
     * Test deleting one record
     */
    @Test
    @Order(1)
    public void deleteOne_Tests() {
    
        // Initialize vars
        System.out.println("\n================ Dataset-Deletion: Table Delete First Record ================\n");
        Dataset dataset;
        Datapoint deletedRecord;
        int sizeBefore, sizeAfter;
        boolean assertionState;
        
        // Create dataset
        dataset = DatasetUtility_ExampleModel.fetchTable("Person", "Random People", 500);
        sizeBefore = dataset.getSize();
        System.out.println("\nCreated dataset for testing, displaying size metrics");
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        
        // Delete first record
        System.out.println("\nDeleting first record from Table:\n");
        deletedRecord = dataset.removeByIndex(0);
        System.out.println("Delete operation state:\t" + (deletedRecord != null));
        sizeAfter = dataset.getSize();
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        assertionState = sizeAfter == (sizeBefore - 1);
        System.out.println("Size after = size before less 1:\t\t" + assertionState);
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n================ Dataset-Deletion: Table Delete First Record ================\n");
        assertTrue(assertionState);
    }
    
    
    /**
     * Test querying following deletion of one record
     * 
     */
    @Test
    @Order(2)
    public void deleteOneQuery_Test() {
    
        // Initialize vars
        System.out.println("\n================ Dataset-Deletion: Table Delete First Record, Query Test ================\n");
        Dataset dataset;
        Datapoint deletedRecord;
        Results queriedResults;
        String field = "First Name";
        Object queryVal;
        int datasetSize, sizeBefore, sizeAfter, datasetSizeAfter;
        boolean assertionState;
        
        // Create dataset
        dataset = DatasetUtility_ExampleModel.fetchTable("Person", "Random People", 500);
        datasetSize = dataset.getSize();
        System.out.println("\nCreated dataset for testing, displaying size metrics");
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        
        // Fetch result set matching query
        deletedRecord = dataset.getByIndex(0);
        queryVal = deletedRecord.getAttribute(field).getAttribValue();
        queriedResults = dataset.query(field, queryVal, true);
        sizeBefore = queriedResults.getSize();
        DatasetUtility_ExampleModel.printDatasetStats(queriedResults);
        
        // Delete record
        assertionState = dataset.removeByQuery(field, queryVal);
        System.out.println("\nDelete operation state:\t" + assertionState);
        queriedResults = dataset.query(field, queryVal, true);
        sizeAfter = queriedResults.getSize();
        
        // Display Table & Query Results
        System.out.println("\n############## Displaying Table & Query Results Following Deletion ##############\n");
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        DatasetUtility_ExampleModel.printDatasetStats(queriedResults);
        System.out.println("\n############## Displaying Table & Query Results Following Deletion ##############\n");
        
        // Verifying metrics following record deletion
        System.out.println("\nValidating Dataset Size Metrics Following Deletion");
        datasetSizeAfter = dataset.getSize();
        sizeAfter = datasetSize - sizeBefore;
        assertionState = sizeAfter == datasetSizeAfter;
        System.out.println("Dataset Size:\t\t\t" + datasetSize);
        System.out.println("Size Before:\t\t\t" + sizeBefore);
        System.out.println("Size After:\t\t\t" + sizeAfter);
        System.out.println("Dataset Size After:\t\t" + datasetSizeAfter);
        System.out.println("Size After Matches Expected:\t" + assertionState);
        
        // Log test status
        System.out.println("\nTest status:\t" + assertionState);
        System.out.println("\n================ Dataset-Deletion: Table Delete First Record, Query Test ================\n");
        assertTrue(assertionState);
    }
    
    
    /**
     * Test querying Last Name, following a Deletion on a First Name Query.<br><br>
     * 
     * The test is performed on the first record in the table, because it could
     * break the entire Table if indexes are not handled correctly. As its deletion,
     *  causes the position of every Datapoint in the Dataset (Array List of Datapoint) to decrease by one.
     * 
     * <br><br>
     * 
     * Currently takes ~1.3s to query a Table of 50k Datapoints. Where first name is queried (~2.2k),
     *  then last name (~2.7k). Then delete records from a query (~2.2k), re-create all table indexes,
     *  then query last name again. Scanning for Active, Dropped size counts in parallel on those ~2.2k, ~2.7k
     *  Results 4 times.
     * 
     * <br><br>Test shows that the performance of this in memory database is very good.
     *  As their is still some simple things that can be done to further improve.
     */
    @Test
    @Order(3)
    public void queryOtherField_PostDeletion_Test() {
    
        // Initialize vars
        System.out.println("\n================ Dataset-Deletion: Test Querying Last Name, Following Deletion on First Name ================\n");
        double startTime, endTime, duration;
        Dataset dataset;
        Datapoint deletedRecord;
        Results queriedResults, otherQueryResults;
        String field = "First Name";
        String otherField = "Last Name";
        Object queryVal, queryValOther;
        int sizeBefore, sizeAfter;
        boolean assertionState;
        
        // Create dataset
        startTime = System.nanoTime() / 1e6;
        dataset = DatasetUtility_ExampleModel.fetchTable("Person", "Random People", 1000000);
        System.out.println("\nCreated dataset for testing, displaying size metrics");
        DatasetUtility_ExampleModel.printDatasetStats(dataset);
        
        // Fetch result set matching query
        deletedRecord = dataset.getByIndex(0);
        queryVal = deletedRecord.getAttribute(field).getAttribValue();        
        queryValOther = deletedRecord.getAttribute(otherField).getAttribValue();
        otherQueryResults = dataset.query(otherField, queryValOther, true);
        queriedResults = dataset.query(field, queryVal, true);
        sizeBefore = otherQueryResults.getSize();
        System.out.println("Number of records to be deleted from Query on '" + field + "':\t" + queriedResults.getSize());
        System.out.println("Fetching summary for Query on '" + otherField + "'");
        DatasetUtility_ExampleModel.printDatasetStats(otherQueryResults);
        
        // Check that the query can still run
        assertionState = dataset.removeByQuery(field, queryVal);
        System.out.println("\nDeletion status:\t\t\t\t\t\t" + assertionState);
        otherQueryResults = dataset.query(otherField, queryValOther, true);
        
        // Displaying results metrics following deletion
        assertionState = (otherQueryResults != null);
        sizeAfter = otherQueryResults.getSize();
        System.out.println("Verifying that Query of another field is not null:\t\t" + assertionState);
        System.out.println("Dataset size following deletion:\t\t\t\t" + dataset.getSize());
        System.out.println("'" + otherField + "' records lost as consequence of the deletion:\t" + ( sizeBefore - sizeAfter ));
        DatasetUtility_ExampleModel.printDatasetStats(otherQueryResults);
        
        // Log test status
        endTime = System.nanoTime() / 1e6;
        duration = (endTime - startTime) / 1e3;
        System.out.println("\nTest start time (ms):\t" + startTime);
        System.out.println("Test end time (ms):\t" + endTime);
        System.out.println("Time taken (s):\t\t" + duration);
        System.out.println("Test status:\t\t" + assertionState);
        System.out.println("\n================ Dataset-Deletion: Test Querying Last Name, Following Deletion on First Name ================\n");
        assertTrue(assertionState);
    }
}
