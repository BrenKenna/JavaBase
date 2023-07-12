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
package database;

import dataset.Dataset;
import dataset.Table;

import java.util.HashMap;
import java.util.Map;


/**
 * A Database is a Set of named Table-Dataset
 * 
 * @author kenna
 */
public class Database {
    
    // Database
    private String databaseName;
    private final Map<String, Table> database;
    
    
    /**
     * Default constructor
     */
    public Database() {
        this.database = new HashMap<>();
    }
    
    
    /**
     * Construct named database
     * 
     * @param name 
     */
    public Database(String name) {
        this.databaseName = name;
        this.database = new HashMap<>();
    }
    
    
    /**
     * Construct named database with data
     * 
     * @param name
     * @param database 
     */
    public Database(String name, Map<String, Table> database) {
        this.databaseName = name;
        this.database = database;
    }

    
    /**
     * Get table if exists
     * 
     * @param tableName
     * @return - Dataset-Table
     */
    public Dataset getTable(String tableName) {
        if ( database.isEmpty() | !database.containsKey(tableName) ) {
            return null;
        }
        else {
            return database.get(tableName);
        }
    }
    
    
    /**
     * Add Table if non existent
     * 
     * @param tableName
     * @param dataset
     * @return - True/False
     */
    public boolean addTable(String tableName, Table dataset) {
        if ( database.containsKey(tableName) ) {
            return false;
        }
        else {
            database.put(tableName, dataset);
            return true;
        }
    }
    
    
    /**
     * Create a table if non-existent
     * 
     * @param tableName
     * @param tableAlias
     * @return - True/False
     */
    public boolean createTable(String tableName, String tableAlias) {
        if ( database.containsKey(tableName) ) {
            return false;
        }
        else {
            Table dataset = new Table(tableName, tableAlias);
            database.put(tableName, dataset);
            return true;
        }
    }
    
    
    /**
     * Remove table by name if present
     * 
     * @param tableName
     * @return - Dataset
     */
    public Dataset removeTable(String tableName) {
        if ( database.containsKey(tableName) ) {
            return null;
        }
        else {
            return database.remove(tableName);
        }
    }
    
    
    /**
     * Delete table
     * 
     * @param tableName
     * @return - True/False
     */
    public boolean deleteTable(String tableName) {
        return removeTable(tableName) != null;
    }
    
    
    /**
     * Check if table exists
     * 
     * @param tableName
     * @return - True/False
     */
    public boolean hasTable(String tableName) {
        return database.containsKey(tableName);
    }
    
    
    /**
     * Clear the database
     * 
     */
    public void clearDatabase() {
        database.clear();
    }
    
    
    /**
     * Get name
     * 
     * @return - String
     */
    public String getDatabaseName() {
        return databaseName;
    }

    
    /**
     * Change name
     * 
     * @param databaseName 
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    
    /**
     * Fetch database
     * 
     * @return 
     */
    public Map<String, Table> getDatabase() {
        return database;
    }

    
    /**
     * Represent database as string
     * 
     * @return - String
     */
    @Override
    public String toString() {
        return "CustomDatabase{" + "databaseName=" + databaseName + ", database=" + database + '}';
    }
}
