/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataset;

import dataType.DataTypeEnum;
import datapoint.Datapoint;
import datapoint.ModelAttribute;
import exceptions.DataTypeException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * A Dataset is an abstract collection of Datapoints
 * 
 * @author kenna
 */
public abstract class Dataset {
     
    // Dataset is a list of Datapoints
    protected final List<Datapoint> dataset;
    private final DatasetType type;
    protected final Map<String, DatasetIterator> datasetIterators;
    
    // Table meta data
    protected String name, alias;
    
    
    /**
     * Construct empty dataset
     * 
     * @param type
     */
    public Dataset(DatasetType type) {
        this.dataset = new ArrayList<>();
        this.name = null;
        this.alias = null;
        this.type = type;
        this.datasetIterators = new HashMap<>();
    }
    
    
    /**
     * Construct from datapoint list
     * 
     * @param type
     * @param dataset 
     */
    public Dataset(DatasetType type, List<Datapoint> dataset) {
        this.dataset = dataset;
        this.name = null;
        this.alias = null;
        this.type = type;
        this.datasetIterators = new HashMap<>();
    }
    
    
    /**
     * Construct with meta data and data
     * 
     * @param type
     * @param dataset
     * @param name
     * @param alias 
     */
    public Dataset(DatasetType type, List<Datapoint> dataset, String name, String alias) {
        this.dataset = dataset;
        this.name = name;
        this.alias = alias;
        this.type = type;
        this.datasetIterators = new HashMap<>();
    }
    
    
    /**
     * Construct with meta data
     * 
     * @param type
     * @param name
     * @param alias 
     */
    public Dataset(DatasetType type, String name, String alias) {
        this.dataset = new ArrayList<>();
        this.name = name;
        this.alias = alias;
        this.type = type;
        this.datasetIterators = new HashMap<>();
    }

        
    /**
     * Permanently remove a Datapoint by its index
     * 
     * @param index
     * @return 
     */
    public abstract Datapoint removeByIndex(int index);
    
    
    /**
     * Abstract method to extend Datapoint list
     * 
     * @param input 
     */
    public abstract void extend(List<Datapoint> input);
    
    
    /**
     * Abstract method to a new Datapoint
     * 
     * @param input 
     * @return  
     */
    public abstract boolean addNewDatapoint(Datapoint input);
    
    
    
    /**
     * Set all Datapoints whose matches query to a Dropped State 
     * 
     * @param alias
     * @param query
     * @return - True/False
     */
    public boolean dropDatapoint(String alias, Object query) {
        Dataset toDrop = this.query(alias, query, true);
        if ( toDrop.asList().isEmpty() ) { return false; }
        toDrop.asList()
            .parallelStream()
            .forEach( Datapoint::dropDatapoint );
        return true;
    }
    
    
    /**
     * Restore all Dropped Datapoints with a field matching query
     * 
     * @param alias
     * @param query
     * @return - True/False
     */
    public boolean restoreDatapoint(String alias, Object query) {
        Dataset toRestore = this.query(alias, query, false);
        if ( toRestore.asList().isEmpty() ) { return false; }
        toRestore.asList()
            .parallelStream()
            .forEach( Datapoint::restoreDatapoint );
        return true;
    }
    
    
    /**
     * Restore datapoint at index if valid
     * 
     * @param indexOf
     * @return - Index of Datapoint
     */
    public boolean restoreDatapoint(int indexOf) {
        
        // Handle input
        if ( this.dataset.isEmpty() | indexOf > this.dataset.size()) { return false; }
        
        // Handle datapoint
        Datapoint record = dataset.get(indexOf);
        if ( record == null ) { return false; }
        
        else {
            record.restoreDatapoint();
            return true;
        }
    }
    
    
    /**
     * Set the Datapoint at the specified index to a dropped state
     * 
     * @param primaryKey
     * @return - True/False
     */
    public boolean dropDatapoint(int primaryKey) {
        if ( primaryKey > dataset.size() -1 | primaryKey < 0 ) { return false; }
        Datapoint toDrop = dataset.get(primaryKey);
        toDrop.dropDatapoint();
        return true;
    }
    
    
    /**
     * Abstract method to query a Dataset for a value
     * 
     * @param alias
     * @param query
     * @param onlyActive
     * @return - Results, List of Datapoint
     */
    public abstract Results query(String alias, Object query, boolean onlyActive);
    
    
    /**
     * Abstract method to remove all Datapoint matching query on field
     * 
     * @param alias
     * @param query
     * @return - True/False
     */
    public abstract boolean removeByQuery(String alias, Object query);
    
    
    /**
     * Validate if index is bounds
     * 
     * @param index
     * @return - True/False
     */
    protected boolean validateIndex(int index) {
        return (index < 0) | (index > this.dataset.size()-1);
    }
    
    
    /**
     * Validate query against first datapoint
     * 
     * @param fieldAlias
     * @param fieldQuery
     * @return - True/False
     */
    protected boolean validateQuery(String fieldAlias, Object fieldQuery) {
        Datapoint ref = dataset.get(0);
        return ref.isValid(fieldAlias, fieldQuery);
    }
    
    
    /**
     * Validate a queried Datapoint against a reference in Dataset.
     * 
     * @param query
     * @return - True/False
     */
    protected boolean validateDatapoint(Datapoint query) {
        
        // Initalize output
        boolean output;
        Datapoint ref = dataset.get(0);
        
        // Iteratively validate Entry Set
        for(Entry<String, ModelAttribute> queryAttrib : query.getDatapoint().entrySet()) {
            
            // Validate attribute of query against reference
            output = ref.isValid(queryAttrib.getKey(), queryAttrib.getValue());
            
            // Strict mode fails validation on one invalid field
            if ( !output) {
                return false;
            }

        }
        
        // Otherwise valid
        return true;
    }
    
    
    /**
     * Compare queried record against reference
     * 
     * @param ref
     * @param alias
     * @param query
     * @return - Distance
     * 
     * @throws DataTypeException 
     */
    protected int matchRecord(Datapoint ref, String alias, Object query) throws DataTypeException {
        ModelAttribute modelAttrib = ref.getAttribute(alias);
        return DataTypeEnum.compare(modelAttrib.getAttribValue(), query);
    }

    
    /**
     * Fetch record by index
     * 
     * @param index
     * @return - Datapoint
     */
    public Datapoint getDatapoint(int index) {
        if ( !validateIndex(index) ) {
            return null;
        }
        else {
            return this.dataset.get(index);
        }
    }

    
    /**
     * Scan dataset for matching record
     * 
     * @param alias
     * @param query
     * @return - List of Datapoints matching query on attribute field
     */
    public List<Datapoint> scan(String alias, Object query)  {
        
        // Validate type
        if ( !validateQuery(alias, query) ) {
            return null;
        }
        
        // Scan linearly
        int counter = 0;
        List<Datapoint> output = new ArrayList<>();
        for ( Datapoint i : dataset ) {
            try {
                if (matchRecord(i, alias, query) == 0) {
                    output.add(i);
                }
                counter++;
            }
            catch(DataTypeException ex) {
                counter++;
            }
        }
        
        // Return output
        return output;
    }

        
    /**
     * Get Dataset name
     * 
     * @return - String
     */
    public String getName() {
        return name;
    }

    
    /**
     * Set new name for Dataset
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /**
     * Get Dataset alias
     * 
     * @return - String
     */
    public String getAlias() {
        return alias;
    }

    
    /**
     * Update alias for Dataset
     * 
     * @param alias 
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    
    /**
     * Return dataset as List
     * 
     * @return - List of Datapoint
     */
    public List<Datapoint> asList() {
        return dataset;
    }
    
    
    /**
     * Return the Dataset Type of child class
     * 
     * @return - DatasetType
     */
    public DatasetType getType() {
        return this.type;
    }
    
    
    /**
     * Return Datapoint at index, else null
     * 
     * @param index
     * @return - Datapoint
     */
    public Datapoint getByIndex(int index) {
        if ( dataset.isEmpty() | index > dataset.size() ) { return null; }
        return dataset.get(index);
    }
    
    
    /**
     * Drop Datapoint at index from View
     * 
     * @param index 
     */
    public void dropByIndex(int index) {
        Datapoint toDrop = dataset.get(index);
        if ( toDrop != null ) {
            toDrop.dropDatapoint();
        }
    }
    
    
    /**
     * Return the size of dataset
     * 
     * @return - int 
     */
    public int getSize() {
        return dataset.size();
    }
    
    
    /**
     * Get count of Datapoints in Active state
     * 
     * @return - int
     */
    public int getActiveSize() {
        
        // Intialize vars
        if ( dataset.isEmpty() ) { return 0; }
        
        // Increment counter
        long count = dataset
            .parallelStream()
            .filter( record -> record.isActive() )
            .count()
        ;
        //System.out.println("Active Size count as long:\t" + count);
        return (int) count;
    }
    
    
    /**
     * Count Datapoints in Dropped State
     * 
     * @return - int
     */
    public int getDroppedSize() {
        // Intialize vars
        if ( dataset.isEmpty() ) { return 0; }
        
        // Increment counter
        return (int) dataset
            .parallelStream()
            .filter( record -> record.isDropped() )
            .count()
        ;
    }
}
