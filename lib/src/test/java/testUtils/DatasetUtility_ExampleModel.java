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
package testUtils;

import appLogging.AppLogging;
import converter.ModelDatapointConverter;
import datapoint.Datapoint;
import dataset.Dataset;
import dataset.Table;
import datasetTests.Dataset_Tests;
import exampleModel_Tests.model.Person;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author kenna
 */
public class DatasetUtility_ExampleModel {
    
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
    private static List<Datapoint> convertPersons(List<Person> data) {
        
        // Initialize vars
        List<Datapoint> output = new ArrayList<>();
        Map<String, String> aliasMap = DatasetUtility_ExampleModel.personAliasMap();
        
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
        return DatasetUtility_ExampleModel.convertPersons(input);
    }
    
    
    /**
     * Fetch a Table Dataset of required size
     * 
     * @param tableName
     * @param aliasFor
     * @param tableSize
     * @return - Dataset, Table
     */
    public static Dataset fetchTable(String tableName, String aliasFor, int tableSize) {
        List<Datapoint> data = fetchPersonDatapoints(tableSize);
        return new Table(data, tableName, aliasFor);
    }
    
    
    /**
     * Summarize the size metrics for a dataset
     * 
     * @param dataset 
     */
    public static void printDatasetStats(Dataset dataset) {
        
        // Get stats
        int size = dataset.getSize();
        int activeSize = dataset.getActiveSize();
        int droppedSize = dataset.getDroppedSize();
        
        // Display properties
        System.out.println("\nDisplaying stats for '" + dataset.getType() + "' Dataset:\t" + dataset.getName());
        System.out.println("Dataset Size:\t\t\t\t" + size);
        System.out.println("Dataset Active Size:\t\t\t" + activeSize);
        System.out.println("Dataset Dropped Size:\t\t\t" + droppedSize);
    }
}
