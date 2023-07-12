/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testUtils;

import appLogging.AppLogging;
import dataType.DataTypeEnum;
import exampleModel_Tests.model.NameGenerator;
import exceptions.DataTypeException;
import indexInterface.BinarySearchTree;
import indexInterface.IndexEntry;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;


/**
 * Utility class to support testing IndexInterface
 * 
 * @author kenna
 */
public class Index_Utility {
    
    // Set logger
    private static final Logger logger = AppLogging.logger;
    private static final NameGenerator nameGen = new NameGenerator();
    
    
    /**
     * Helper method to generate string dataset
     * 
     * @param datasetSize
     * @return - List of string
     */
    public static List<String> generateStringData(int datasetSize) {
    
        // Add data points
        List<String> dataset = new ArrayList<>();
        for ( int primaryKey = 0; primaryKey < datasetSize; primaryKey++ ) {
            
            // Create record and tree node
            String name = nameGen.getRandomData().get("Last Name");
            if (primaryKey % 10 == 0 ) {
                logger.debug("Constructed new dataset & tree record:\n\nDataset Record = " + name);
            }
            
            // Add records
            dataset.add(name);
        }
        return dataset;
    }
    
    
    /**
     * Create a BinarySearchTree for a list of strings
     * 
     * @param dataset
     * @return - BinarySearchTree
     * @throws exceptions.DataTypeException
     * 
     */
    public static BinarySearchTree generateStringBST(List<String> dataset) throws DataTypeException {
        
        // Initialize output & primary key
        BinarySearchTree output = new BinarySearchTree();
        
        // Populate BST
        for ( int primaryKey = 0; primaryKey < dataset.size(); primaryKey++ ) {
        
            // Create and add an index entry
            Object value = dataset.get(primaryKey);
            IndexEntry treeNode = new IndexEntry(primaryKey, value, DataTypeEnum.getValue(value));
            
            // Add node to tree
            output.add(treeNode);
        }
        
        // Return tree
        return output;
    }
}
