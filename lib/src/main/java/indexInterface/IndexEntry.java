/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexInterface;


import dataType.DataTypeEnum;
import exceptions.DataTypeException;
import java.util.ArrayList;
import java.util.List;


/**
 * Class containing value and a pointer to a primary key in a dataset.Class exposes methods for comparing index entries, or the value of the 
 concrete index entry type to a string/int. 
 * Collectively allowing a list 
 of these IndexEntries to be searchable/sortable, regardless of the 
 wider data structure
 * 
 * @author kenna
 * @param <T>
 */
public class IndexEntry<T> {
    
    // Attributes
    private int primaryKey;
    private T data;
    private DataTypeEnum dataType;
    private IndexEntry left, right;
    private final List<IndexEntry> equalNodes;
    
    
    /**
     * Construct index entry
     * 
     * @param primaryKey
     * @param data
     * @param dataType 
     */
    public IndexEntry(int primaryKey, T data, DataTypeEnum dataType) {
        this.primaryKey = primaryKey;
        this.data = data;
        this.dataType = dataType;
        this.left = null;
        this.right = null;
        this.equalNodes = new ArrayList<>();
    }

    
    /**
     * Compare self against query
     * 
     * @param query
     * @return - int
     * @throws DataTypeException 
     */
    public int compareData(IndexEntry query) throws DataTypeException {
        return DataTypeEnum.compare(data, query.getData());
    }

    
    /**
     * Compare index entry against value
     * 
     * @param query
     * @return
     * @throws DataTypeException 
     */
    public int compareData(Object query) throws DataTypeException {
        return DataTypeEnum.compare(data, query);
    }

    
    /**
     * Check if query is same
     * 
     * @param query - IndexEntry
     * @return - boolean
     * @throws DataTypeException 
     */
    public boolean isEqual(IndexEntry query) throws DataTypeException {
        return compareData(query) == 0;
    }
    
    
    /**
     * Check if query is same
     * 
     * @param query - Object
     * @return
     * @throws DataTypeException 
     */
    public boolean isEqual(Object query) throws DataTypeException {
        return compareData(query) == 0;
    }

    
    /**
     * Check if queried data type matches
     * 
     * @param query
     * @return - True/False
     */
    public boolean isType(IndexEntry query) {
        return dataType == query.getDataType();
    }
    
    
    /**
     * Get primary key
     * 
     * @return - int
     */
    public int getPrimaryKey() {
        return primaryKey;
    }

    
    /**
     * Set primary key
     * 
     * @param primaryKey 
     */
    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    
    /**
     * Get data
     * 
     * @return - Data as Object
     */
    public T getData() {
        return data;
    }

    
    /**
     * Set data
     * 
     * @param data 
     */
    public void setData(T data) {
        this.data = data;
    }

    
    /**
     * Get data type
     * 
     * @return - DataTypeEnum 
     */
    public DataTypeEnum getDataType() {
        return dataType;
    }

    
    /**
     * Set data type
     * 
     * @param dataType 
     */
    public void setDataType(DataTypeEnum dataType) {
        this.dataType = dataType;
    }

    
    /**
     * Get left node of smaller value
     * 
     * @return - IndexEntry
     */
    public IndexEntry getLeft() {
        return left;
    }

    
    /**
     * Set left node of smaller value
     * 
     * @param left 
     */
    public void setLeft(IndexEntry left) {
        this.left = left;
    }

    
    /**
     * Get right node of larger value
     * 
     * @return - IndexEntry
     */
    public IndexEntry getRight() {
        return right;
    }

    
    /**
     * Set right node of larger value
     * @param right 
     */
    public void setRight(IndexEntry right) {
        this.right = right;
    }

    
    /**
     * Get duplicate nodes
     * 
     * @return - List IndexEntry 
     */
    public List<IndexEntry> getDuplicates() {
        return this.equalNodes;
    }

    
    /**
     * Provide self with dups, or self
     * 
     * @return - List Index Entry
     */
    public List<IndexEntry> getWithDups() {
        List<IndexEntry> output = new ArrayList<>();
        output.add(this);
        if ( !this.hasDuplicate() ) { return output; }
        for (IndexEntry i : this.equalNodes ) {
            output.add(i);
        }
        return output;
    }
    
    
    /**
     * Add a duplicate
     * 
     * @param duplicate 
     */
    public void addDuplicate(IndexEntry duplicate) {
        this.equalNodes.add(duplicate);
    }
    
    
    /**
     * Check whether or not a node has any duplicates
     * 
     * @return - True/False
     */
    public boolean hasDuplicate() {
        return this.equalNodes.size() >= 1;
    }
    
    
    /**
     * Clear and consume the new list of duplicates
     * 
     * @param duplicates 
     */
    public void setDuplicates(List<IndexEntry> duplicates) {
        
        // Clear list
        this.equalNodes.clear();
        
        // Consume if elements in list
        if ( duplicates.size() >= 1 ) {
            for (int i = 0; i < duplicates.size(); i++) {
                this.equalNodes.add( duplicates.remove(i) );
            }
        }
    } 
    
    
    /**
     * Return index of Duplicate, or -1 if holder is dup
     * 
     * @param queryKey
     * @return - Int
     */
    public int indexOfDup(int queryKey) {
        int output = -1;
        int counter = 0;
        boolean found = false;
        if ( this.primaryKey == queryKey ) {
            return output;
        }
        else {
            while ( !found & counter < this.equalNodes.size() ) {
                IndexEntry node = this.equalNodes.get(counter);
                if ( node.getPrimaryKey() == queryKey ) {
                    found = true;
                    output = counter;
                }
                counter++;
            }
        }
        return output;
    }
    
    
    /**
     * Drop index of duplicate at position
     * 
     * @param indexOf 
     */
    public void dropDuplicate(int indexOf) {
        this.equalNodes.remove(indexOf);
    }
    
    
    /**
     * Represent object as string
     * 
     * @return - String 
     */
    @Override
    public String toString() {
        return "IndexEntry{"
           + "primaryKey=" + primaryKey
           + ", data=" + data
           + ", dataType=" + dataType
           + ", left=" + left
           + ", right=" + right
           + ",equalNodes=" + this.equalNodes.size()
        + "}";
    }
}
