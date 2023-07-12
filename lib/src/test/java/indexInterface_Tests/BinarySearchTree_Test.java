/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package indexInterface_Tests;


import appLogging.AppLogging;
import dataType.DataTypeEnum;
import exceptions.DataTypeException;
import indexInterface.BinarySearchTree;
import indexInterface.IndexEntry;
import indexInterface.NodePlacementEnum;

import java.util.List;
import org.apache.logging.log4j.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import testUtils.Index_Utility;


/**
 * Test BinarySearchTree & IndexEntry classes supporting the IndexInterface package
 * 
 * @author kenna
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BinarySearchTree_Test {
    
    // Logging util
    private static final Logger logger = AppLogging.logger;
    
    public BinarySearchTree_Test() {
        System.out.println("\n\n---------------- Binary Search Tree Tests ----------------\n");
    }
    
    /**
     * Test basics of BST for node creation, insertion, and BST construction
     * 
     */
    @Test
    @Order(1)
    public void binarySearchTreeProperty_Test() {
    
        // Initalize variables
        System.out.println("\n====== BST: Properties Tests ======\n");
        int datasetSize = 40;
        List<String> dataset = null;
        BinarySearchTree dataset_index = null;
        
        // Create dataset, and 
        boolean sizeAssertion;
        try {
            dataset = Index_Utility.generateStringData(datasetSize);
            dataset_index = Index_Utility.generateStringBST(dataset);
            System.out.println("Constructed Datasets:\n\nDataset Size = " + dataset.size() + "\nBST Size = " + dataset_index.getTreeSize() + "\nBST Dupes = " + dataset_index.getDuplicateCount());
            sizeAssertion = true;
        }
        catch ( DataTypeException ex ) {
            System.out.println("Error creating dataset & binary search tree:\n" + ex);
            sizeAssertion = false;
        }
        
        finally {
        
            // Handle null
            System.out.println("Asserting sizes equal");
            if ( dataset != null & dataset_index != null ) {
                
                // Validate sizes
                sizeAssertion = dataset.size() == datasetSize;
                System.out.println("Created dataset is :\t" + sizeAssertion);
                sizeAssertion = dataset_index.getTreeSize() == datasetSize;
                System.out.println("Created dataset index is valid:\t" + sizeAssertion);
            }
            else {
                sizeAssertion = false;
                System.out.println("Created dataset & index are invalid. Please above error log");
            }
            System.out.println("Test Passed:\t" + sizeAssertion);
        }
        System.out.println("\n====== BST: Properties Tests ======\n");
        assertTrue(sizeAssertion);
    }
    
    
    /**
     * Test querying a BST, and compare to linear search
     * @throws DataTypeException
     */
    @Test
    @Order(2)
    public void bst_QueryTest() throws DataTypeException {
        
        // Create dataset, and BST
        System.out.println("\n====== BST: Query Tests ======\n");
        int datasetSize = 40;
        boolean assertionState = false;
        List<String> dataset = Index_Utility.generateStringData(datasetSize);
        BinarySearchTree dataset_index = Index_Utility.generateStringBST(dataset);
        
        
        // Test querying
        int queryInd = dataset.size() - 5;
        String testName = dataset.get(queryInd);
        System.out.println("Testing whether Tree contains test name:\t" + testName);
        assertionState = dataset_index.contains(testName);
        if (!assertionState) {
            System.out.println("Name test failed:\t" + assertionState);
        }
        else {
            System.out.println("Name test successful contains output from BST:\t" + assertionState);
        }
        System.out.println("\n====== BST: Query Tests ======\n");
        assertTrue(assertionState);
    }
    
    
    /**
     * Duplicates test
     * 
     * @throws DataTypeException 
     */
    @Test
    @Order(3)
    public void bst_DuplicatesAllowedTest() throws DataTypeException {
    
        
        // Create dataset, and BST
        System.out.println("\n====== BST: Allow Duplicates Tests ======\n");
        int datasetSize = 40;
        boolean assertionState = false;
        List<String> dataset = Index_Utility.generateStringData(datasetSize);
        BinarySearchTree dataset_index = Index_Utility.generateStringBST(dataset);
        
        
        // Spike in 2 duplicates
        int queryInd = 0;
        String testName = dataset.get(queryInd);
        dataset.add(testName);
        dataset.add(testName);
        dataset_index.add( new IndexEntry(40, testName, DataTypeEnum.STRING_DATA_TYPE));
        dataset_index.add( new IndexEntry(41, testName, DataTypeEnum.STRING_DATA_TYPE));
        
        
        // Test querying all matches
        System.out.println("Testing query of all records matching the same:\t" + testName);
        List<IndexEntry> matches = dataset_index.getAll(testName);
        assertionState = matches.size() > 0;
        if (!assertionState) {
            System.out.println("Name test failed:\t" + assertionState);
        }
        else {
            System.out.println("Displaying results for all names matching term:\t" + matches.size() + "\n");
            for (IndexEntry i : matches ) {
                System.out.println(i);
            }
            System.out.println("\n");
        }
        
        
        // Compare to linear search
        System.out.println("Comparing results to a linear search");
        int counter = 0;
        for (IndexEntry i : matches ) {
            String ref = (String) i.getData();
            if ( !ref.equals(testName) ) {
                counter++;
            }
        }
        assertionState = (counter == queryInd);
        if (!assertionState) {
            System.out.println("Testing state of linear & binary search scan results:\t" + assertionState + "\nQueryInd = " + queryInd + "\nFound at = " + counter);
        }
        else {
            System.out.println("Queried Index of '" + queryInd + "', Equals Linear Search Index of '" + counter + "'");
        }
        System.out.println("\n====== BST: Allow Duplicates Tests ======\n");
        assertTrue(assertionState);
    }
    
    
    
    /**
     * Test that a parent node can be fetched
     * 
     * @throws DataTypeException 
     */
    @Test
    @Order(4)
    public void bst_ParentNode_Test() throws DataTypeException {
    
        // Create dataset, and BST
        System.out.println("\n====== BST: Parent Lookup Tests ======\n");
        int datasetSize = 40;
        List<String> dataset = Index_Utility.generateStringData(datasetSize);
        BinarySearchTree dataset_index = Index_Utility.generateStringBST(dataset);
        
        
        // Fetch test record
        int queryInd = dataset.size() - 5;
        String testName = dataset.get(queryInd);

        
        // Get parent node, log childs placement relative to it
        boolean assertionState = false;
        System.out.println("Fetching parent node for testQuery:\t" + testName);
        IndexEntry parentNode = dataset_index.getParent(testName);
        IndexEntry matchingNode = dataset_index.get(testName);
        
        // Check datta
        assertionState = (parentNode == null);
        if (!assertionState) {
            NodePlacementEnum placement = dataset_index.getPlacement(parentNode, matchingNode);
            if (placement == NodePlacementEnum.NULL) {
                System.out.println("Unable to fetch the parent node for query:\t" + testName);
            }
            else {
                assertionState = true;
                System.out.println("Test successful Child is Root Node:\t" + placement);
            }
        }
        else {
            System.out.println("Got parent and child nodes, checking null status:\n\nParent = " + ( parentNode == null )+ "\nChild = " + (matchingNode == null) + "\n");
        }
        
        // Log assertion state
        System.out.println("\n====== BST: Parent Lookup Tests ======\n");
        assertTrue(assertionState);
    }
    
    
    
    /**
     * Test relative postion of child to parent
     * 
     * @throws DataTypeException 
     */
    @Test
    @Order(5)
    public void bst_ChildParentPosition_Test() throws DataTypeException {
    
        
        // Fetch data
        System.out.println("\n====== BST: Parent Child Placement Tests ======\n");
        int datasetSize = 40;
        List<String> dataset = Index_Utility.generateStringData(datasetSize);
        BinarySearchTree dataset_index = Index_Utility.generateStringBST(dataset);
        
        
        // Fetch test record
        int queryInd = dataset.size() - 5;
        String testName = dataset.get(queryInd);

        
        // Get parent node, log childs placement relative to it
        IndexEntry parentNode = dataset_index.getParent(testName);
        IndexEntry matchingNode = dataset_index.get(testName);
        NodePlacementEnum placement = dataset_index.getPlacement(parentNode, matchingNode);
        
        
        // Determine whether this placement is correct
        System.out.println("Determining where the matchingNode places on the parent:\t" + placement);
        boolean assertionState = false;
        Object valFromParent;
        switch (placement) {
            case LEFT -> {
                valFromParent = parentNode.getLeft().getData();
                assertionState = ( (String) valFromParent == testName );
                System.out.println("Comparing values:\n\nMatchingNode = " + testName + "\nValueFromParent = " + valFromParent + "\n");
            }
            case RIGHT -> {
                valFromParent = parentNode.getRight().getData();
                assertionState = ( (String) valFromParent == testName );
                System.out.println("Comparing values:\n\nMatchingNode = " + testName + "\nValueFromParent = " + valFromParent + "\n");
            }
            case ROOT -> {
                Object valFromSelf = matchingNode.getLeft().getData();
                System.out.println("Comparing values:\n\nMatchingNode = " + testName + "\nValueFromSelf (Left) = " + valFromSelf + "\n");
                valFromSelf = matchingNode.getRight().getData();
                System.out.println("Comparing values:\n\nMatchingNode = " + testName + "\nValueFromSelf (Right) = " + valFromSelf + "\n");
                valFromSelf = matchingNode.getData();
                System.out.println("Comparing values:\n\nMatchingNode = " + testName + "\nValueFromSelf (Self) = " + valFromSelf + "\n");
            }
            default -> {
                logger.warn("Issue fetching child:\tPlacement = " + placement + "\n");
                assertionState = false;
            }
        }
        
        // Handle test
        if ( !assertionState ) {
            System.out.println("Unable to match value of Child Node to internal from Parent Node given Placement:\t" + placement);
            assertTrue(assertionState);
        }
        else {
            System.out.println("Succesfully queried Child Node from Parent given Placement:\t" + placement);
            assertTrue(assertionState);
        }
        System.out.println("\n====== BST: Parent Child Placement Tests ======\n");
    }
}
