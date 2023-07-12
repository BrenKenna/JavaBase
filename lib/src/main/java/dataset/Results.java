/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataset;

import datapoint.Datapoint;
import exceptions.DataTypeException;
import java.util.ArrayList;

import java.util.List;


/**
 * A Results class is an instance of Dataset without an IndexCollection
 * 
 * @author kenna
 */
public class Results extends Dataset {
    
    
    /**
     * Construct empty result set
     */
    public Results() {
        super(DatasetType.RESULT);
    }
    
    
    /**
     * Construct from a list of datapoints
     * 
     * @param dataset 
     */
    public Results(List<Datapoint> dataset) {
        super(DatasetType.RESULT, dataset);
    }
    

    /**
     * Drop index from result set
     * 
     * @param index 
     */
    @Override
    public void dropByIndex(int index) {
        if ( this.dataset.size() > index ) {
            this.dataset.remove(index);
        }
    }

    
    /**
     * Results do not have an index, so records can just be appended
     * 
     * @param input 
     */
    @Override
    public void extend(List<Datapoint> input) {
        for ( Datapoint i : input ) {
            addNewDatapoint(i);
        }
    }
    
    
    /**
     * Result set is scaned linerarly for all records matching query
     * 
     * @param alias
     * @param query
     * @return - Results
     */
    @Override
    public Results query(String alias, Object query, boolean onlyActive) {
        
        // Initialize output
        Results output = new Results();
        
        // Search self for records
        List<Datapoint> results = scan(alias, query);
        output.extend(results);
        
        // Provide as Results set
        return output;
    }

    
    /**
     * Provide the Datapoint removed by its index
     * 
     * @param index
     * @return - Datapoint
     */
    @Override
    public Datapoint removeByIndex(int index) {
        if ( index > dataset.size() -1 | index < 0 ) { return null;}
        Datapoint output = getDatapoint(index);
        return output;
    }

    
    /**
     * Add a new datapoint
     * 
     * @param input
     * @return 
     */
    @Override
    public boolean addNewDatapoint(Datapoint input) {
        this.dataset.add(input);
        return true;
    }

    
    /**
     * Internal method to fetch primary keys matching query
     * 
     * @param alias
     * @param query
     * @return - List integer
     */
    private List<Integer> fetchKeys(String alias, Object query) {
        List<Integer> primaryKeys = new ArrayList<>();
        boolean state = false;
        int counter = 0;
        
        for ( Datapoint i : this.dataset ) {
            try {
                if ( this.matchRecord(i, alias, query) == 0 ) {
                    primaryKeys.add(counter);
                }
            }
            catch(DataTypeException ex) {
                state = false;
            }
            counter++;
        }
        return primaryKeys;
    }

    
    /**
     * Remove all Datapoint with a field matching query
     * 
     * @param alias
     * @param query
     * @return - True/False
     */
    @Override
    public boolean removeByQuery(String alias, Object query) {
        
        // Fetch primary keys matching query
        List<Integer> primaryKeys = fetchKeys(alias, query);
        if ( primaryKeys.isEmpty() ) { return false; }
        
        // Drop elements from datast
        for ( int primaryKey : primaryKeys ) {
            this.dataset.remove(primaryKey);
        }
        return true;
    }
}
