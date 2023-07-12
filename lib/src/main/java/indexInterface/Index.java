/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexInterface;


import dataType.DataTypeEnum;
import datapoint.Datapoint;
import datapoint.ModelAttribute;
import exceptions.DataTypeException;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Class to support a collection of BinarySearchTrees as an Index
 * 
 * @author kenna
 */
public class Index {
    
    // An index has a map of indexes
    private final Map<String, BinarySearchTree> index;
    
    
    /**
     * Initialize with empty BST Map
     */
    public Index() {
        this.index = new HashMap<>();
    }

    
    /**
     * Construct index from BST_Index map
     * 
     * @param index 
     */
    public Index(Map<String, BinarySearchTree> index) {
        this.index = index;
    }
    
    
    /**
     * Get a BST by its alias
     * 
     * @param alias
     * @return - BinarySearchTree
     */
    public BinarySearchTree getIndex(String alias) {
        return index.get(alias);
    }
    
    
    /**
     * Return index
     * 
     * @return - Map<String, BinarySearchTree>
     */
    public Map<String, BinarySearchTree> getIndex() {
        return index;
    }
    
    
    /**
     * Add an index for a field from a list of datapoints
     * 
     * @param dataset
     * @param field
     * @param indexName 
     * @throws exceptions.DataTypeException 
     */
    public void addIndex(List<Datapoint> dataset, String field, String indexName) throws DataTypeException {
        BinarySearchTree bST = createBST(dataset, field);
        addTree(indexName, bST);
    }
    
    /**
     * Add a new BST for faster queries
     * 
     * @param alias
     * @param bST 
     */
    public void addTree(String alias, BinarySearchTree bST) {
        if ( ! index.containsKey(alias) ) {
            index.put(alias, bST);
        }
    }
    
    
    /**
     * Private method to create BinarySearchTree from list of Datapoints
     * 
     * @param dataset
     * @param field
     * @param indexName
     * @return - BinarySearchTree
     * 
     * @throws DataException 
     */
    private BinarySearchTree createBST(List<Datapoint> dataset, String field) throws DataTypeException {
        
        // Initialize tree and primary key pointer
        BinarySearchTree bST = new BinarySearchTree();
        int primaryKey = 0;
        
        // Add each node
        for (Datapoint data : dataset ) {
            
            // Create treeNode
            ModelAttribute model = data.getAttribute(field);
            DataTypeEnum dataType = DataTypeEnum.getValue(model.getAttribValue());
            IndexEntry treeNode = new IndexEntry(primaryKey, model.getAttribValue(), dataType);
            
            // Add node
            bST.add(treeNode);
            primaryKey++;
        }
        
        // Return tree
        return bST;
    }

    
    /**
     * Drop BinarySearchTree from collection
     * 
     * @param indexName
     * @return - Dropped BST
     */
    public BinarySearchTree dropBST(String indexName) {
        if ( index.containsKey(indexName) ) {
            return index.remove(indexName);
        }
        else {
            return null;
        }
    }
    
    
    /**
     * Return Primary Keys matching input query
     * 
     * @param indexName
     * @param query
     * @return - Primary Keys, Null
     */
    public List<Integer> query(String indexName, Object query) {
        List<Integer> results;
        try {
            results = index.get(indexName).getPrimaryKeys(query);
            // System.out.println("\n\nIndex-Query: Results size = " + results.size() + "\n\n");
        }
        catch ( DataTypeException ex ) {
            results = null;
        }
        return results;
    }
    
    
    /**
     * Insert new node to a tree from ModelAttribute
     * 
     * @param label
     * @param data
     * @param primaryKey 
     * @return - True/False
     */
    public boolean insertNewNode(String label, ModelAttribute data, int primaryKey) {
        
        // Fetch corresponding tree and initialize new node
        BinarySearchTree tree = index.get(label);
        IndexEntry newNode;
        
        // Fetch the data for the new node
        DataTypeEnum dataType = data.getAttribDataType();
        newNode = new IndexEntry(primaryKey, data.getAttribValue(), dataType);
        
        // Add node to tree
        try {
            tree.add(newNode);
            return true;
        }
        catch ( DataTypeException ex ) {
            return false;
        }
    }
    
    
    /**
     * Insert a new Datapoint record into each Index.<br>
     *  Returning a list of successful inserts, in case of rollback
     * 
     * @param input
     * @param primaryKey
     * @return - List String
     */
    private List<String> insertRecord(Datapoint input, int primaryKey) {
        
        // Initialize vars
        List<String> output = new ArrayList<>();
        boolean inserted;
        
        // Insert each attribute
        for ( String label : index.keySet() ) {
            
            // Fetch data
            ModelAttribute data = input.getAttribute(label);
            
            // Insert new node
            inserted = insertNewNode(label, data, primaryKey);
            
            // Append for managing rollbacks
            if ( inserted ) {
                output.add(label);
            }
        }
        
        // Return result
        return output;
    }
    
    
    /**
     * Drop element by its primary key from a BinarySearchTree
     * 
     * @param label
     * @param input
     * @param primaryKey
     * @return - True/False
     */
    public boolean dropFrom(String label, ModelAttribute input, int primaryKey) {
        BinarySearchTree tree = index.get(label);
        try {
            tree.dropByPrimaryKey(input.getAttribValue(), primaryKey);
            return true;
        }
        catch ( DataTypeException ex ) {
            return false;
        }
    }
    
    
    /**
     * Drop record from tree matching query
     * 
     * @param label
     * @param query
     * @return - Primary Key, -1
     */
    public int getNodePrimaryKey(String label, Object query) {
        int primaryKey;
        try {
            
            // Fetch lead node primary key
            IndexEntry node = index.get(label).get(query);
            if ( node == null ) { return -1; }
            primaryKey = node.getPrimaryKey();
            return primaryKey;
            
        } catch (DataTypeException ex) {
            return -1;
        }
    }
    
    /**
     * Drop a datapoint by its primary key fro each BST
     * 
     * @param toDrop
     * @param primaryKey 
     * @return - List of failed deletionss
     */
    public List<String> dropDatapoint(Datapoint toDrop, int primaryKey) {
        List<String> failed = new ArrayList<>();
        for ( String label : index.keySet() ) {
            ModelAttribute field = toDrop.getAttribute(label);
            boolean state = dropFrom(label, field, primaryKey);
            if ( !state ) {
                failed.add(label);
            }
        }
        return failed;
    }
    
    
    /**
     * Add a new Datapoint to each BinarySearchTree
     * 
     * @param input
     * @param primaryKey
     * @return - True/False
     */
    public boolean insertDatapoint(Datapoint input, int primaryKey) {
        
        // Initialize vars for rollback on a failed insert
        int nBST = index.size();
        List<String> rollbackOnFail = insertRecord(input, primaryKey);
        
        
        // Handle rollback
        if ( rollbackOnFail.size() != nBST ) {
            if ( !rollbackOnFail.isEmpty() ) {
                for ( String label : rollbackOnFail ) {
                    ModelAttribute field = input.getAttribute(label);
                    dropFrom(label, field, primaryKey);
                }
            }
            return false;
        }
        return true;
    }
    
    
    /**
     * Remove all records from index matching attribute value
     * 
     * @param label
     * @param data
     * @return - True/False
     */
    public boolean removeAll(String label, ModelAttribute data) {
        BinarySearchTree tree = index.get(label);
        try {
            tree.drop(data.getAttribValue());
            return true;
        }
        catch ( DataTypeException ex ) {
            return false;
        }
    }
    
    
    /**
     * Remove a datapoint from tree entirely
     * 
     * @param input
     * @return - List failed indexes
     */
    public List<String> removeAll(Datapoint input) {
        List<String> output = new ArrayList<>();
        for (String label : index.keySet()) {
            ModelAttribute data = input.getAttribute(label);
            boolean state = removeAll(label, data);
            if ( !state ) { output.add(label); }
        }
        return output;
    }
}
