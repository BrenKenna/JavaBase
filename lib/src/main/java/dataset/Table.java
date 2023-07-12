/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataset;

import datapoint.Datapoint;
import exceptions.DataTypeException;
import indexInterface.BinarySearchTree;
import indexInterface.Index;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Class to model a Collection of Datapoints as Table, which has an Index
 *  allowing ModelAttribute values to be stored in a BinarySearch
 * 
 * @author kenna
 */
public class Table extends Dataset {

    // Attributes
    private final Index index;
    
    
    /**
     * Default constructor
     */
    public Table() {
        super(DatasetType.TABLE);
        this.index = new Index();
    }
    
    
    /**
     * Construct with table Identifiers
     * 
     * @param name
     * @param alias
     */
    public Table(String name, String alias) {
        super(DatasetType.TABLE, name, alias);
        this.index = new Index();
    }
    
    
    /**
     * Construct with dataset, name, alias and class
     * 
     * @param dataset
     * @param name
     * @param alias 
     */
    public Table(List<Datapoint> dataset, String name, String alias) {
        super(DatasetType.TABLE, dataset, name, alias);
        this.index = new Index();
        initIndex();
    }
    
    
    /**
     * Initalize index
     */
    private void initIndex() {
        createIndexes();
    }
    
    
    /**
     * Create index
     * 
     * @return 
     */
    public List<String> createIndexes() {
        List<String> failed = new ArrayList<>();
        Datapoint ref = dataset.get(0);
        for ( String field : ref.getAliases() ) {
            try {
                index.addIndex(dataset, field, field);
            }
            catch (DataTypeException ex) {
                failed.add(field);
            }
        }
        return failed;
    }

    
    /**
     * Validate if Datapoint is non null, and in an active state
     * 
     * @param query
     * @return - True/False
     */
    private boolean isActive(Datapoint query) {
        boolean isValid = false;
        if ( query != null ) {
            if ( query.isActive() ) {
                isValid = true;
            }
        }
        return isValid;
    } 
    
    
    /**
     * Helpder method to handle whether a Datapoint can be added to
     *  client Dataset-Result View
     * 
     * @param record
     * @param onlyActive
     * @return - True/False
     */
    private boolean canAdd(Datapoint record, boolean onlyActive) {
        
        // Validate existing record
        if ( record == null ) {
            return false;
        }
        
        // Verify active state if required
        if ( onlyActive ) {
            return record.isActive();
        }
        
        // Otherwise can add, because record exists
        // and do not care about state
        else {
            return true;
        }
    }
    
    
    /**
     * Helper method to provide Datapoints for Primary Key list.<br><br>
     * Datapoints can be restricted by their state if required.
     * 
     * @param primaryKeys
     * @param onlyActive
     * @return - List Datapoint
     */
    private List<Datapoint> getDatapoints(List<Integer> primaryKeys, boolean onlyActive) {
        
        // Initialize output
        List<Datapoint> output = new ArrayList<>();
        if ( primaryKeys == null ) {
            return output;
        }
        
        // Add Datapoint if state is active
        for ( int i : primaryKeys ) {
            
            // Fetch Datapoint
            Datapoint record = dataset.get(i);
            
            // Handle if record can be added to view
            if ( canAdd(record, onlyActive) ) {
                output.add(record);
            }
            
        }
        
        // Return result
        return output;
    }
    
    
    /**
     * Query table for Datapoints matching input. Only Active records are returned
     * 
     * @param alias
     * @param query
     * @return - Results
     */
    @Override
    public Results query(String alias, Object query, boolean onlyActive) {
        
        // Initalize vars
        Results output = null;
        List<Integer> primaryKeys;
        List<Datapoint> activeMatches;
                
        // Fetch primary keys of query
        primaryKeys = index.query(alias, query);
        if ( primaryKeys == null ) { return output; }
        
        // Convert to Datapoints
        activeMatches = getDatapoints(primaryKeys, onlyActive);
        if ( activeMatches == null ) { return output; }
        
        // Return results
        return new Results(activeMatches);
    }

    
    /**
     * Extend a dataset with another, updating index
     * 
     * @param input 
     */
    @Override
    public void extend(List<Datapoint> input) {
        
        // Add each datapoint
        for ( Datapoint i : input ) {
            this.dataset.add(i);
        }
        
        // Re-create Index
        dropIndexes();
        createIndexes();
    }
    
    
    /**
     * Fetch the Table Index, Map of BinarySearchTrees
     * 
     * @return - Index
     */
    public Index getIndex() {
        return this.index;
    }

    
    /**
     * Create index returning whether operation was successful
     * 
     * @param alias
     * @param indexName
     * @return - True/False
     */
    public boolean createIndex(String alias, String indexName) {
        try {
            index.addIndex(dataset, alias, indexName);
            return true;
        }
        catch (DataTypeException ex) {
            return false;
        }
    }

    
    /**
     * Drop a BinarySearchTree from Index
     * 
     * @param indexName 
     */
    public void dropIndex(String indexName) {
        index.dropBST(indexName);
    }

    
    /**
     * Clear Index
     */
    public void dropIndexes() {
        index.getIndex().clear();
    }
    
    
    /**
     * Remove a Datapoint from List and Index
     * 
     * @param index
     * @return 
     */
    @Override
    public Datapoint removeByIndex(int index) {
        if ( index > dataset.size() -1 | index < 0 ) { return null;}
        Datapoint toDrop = this.dataset.remove(index);
        this.index.dropDatapoint(toDrop, index);
        return toDrop;
    }

    
    /**
     * Add a new Datapoint into Datapoint List, and Index
     * 
     * @param input
     * @return - True/False
     */
    @Override
    public boolean addNewDatapoint(Datapoint input) {
        
        // Insert into dataset, and get PK 
        this.dataset.add(input);
        int indexOf = this.dataset.size() - 1;
        
        // Try insert record into index
        return this.index.insertDatapoint(input, indexOf);
    }

    
    /**
     * Remove all records matching a querys
     * 
     * @param alias
     * @param query
     * @return - True/False for all trees
     */
    @Override
    public boolean removeByQuery(String alias, Object query) {
        
        // Query data, handle null/no records
        int primaryKey = index.getNodePrimaryKey(alias, query);
        if ( primaryKey < 0 ) { return false; }
        List<Integer> toDelete = index.query(alias, query);
        Collections.sort(toDelete);
        
        // Fetch datapoint, and remove from tree
        Datapoint treeNode = dataset.get(primaryKey);
        List<String> removeState = index.removeAll(treeNode);
        
        // Delete all records from dataset in reverse order
        for ( int i = toDelete.size()-1; i > -1; i-- ) {
            int key = toDelete.get(i);
            dataset.remove(key);
        }
        
        // Re-create indexes
        this.dropIndexes();
        this.createIndexes();
        return removeState.isEmpty();
    }
}
