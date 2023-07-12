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
package dataset;


/**
 * Enum to support Dataset specifc operations
 * 
 * @author kenna
 */
public enum DatasetType {
    
    TABLE {
        @Override
        public String toString() {
            return "Table";
        }

        @Override
        public boolean isType(String query) {
            return "table".equals( query.toLowerCase() );
        }

        @Override
        public boolean isType(DatasetType query) {
            return TABLE == query;
        }
    },
    
    RESULT {
        @Override
        public String toString() {
            return "Result";
        }

        @Override
        public boolean isType(String query) {
            return "result".equals( query.toLowerCase() );
        }

        @Override
        public boolean isType(DatasetType query) {
            return RESULT == query;
        }
    }
    ;
    
    
    /**
     * Abstract method to represent value as string
     * 
     * @return - String
     */
    @Override
    public abstract String toString();
    
    
    /**
     * Abstract method to validate query string against value
     * 
     * @param query
     * @return - True/False
     */
    public abstract boolean isType(String query);
    
    
    /**
     * Abstract method to validate query Value against value
     * 
     * @param query
     * @return - True/False
     */
    public abstract boolean isType(DatasetType query);
    
    
    /**
     * Return index of query
     * 
     * @param query
     * @return - Index
     */
    public static int indexOf(String query) {
        
        // Initialize vars
        int output = -1;
        int counter = 0;
        boolean found = false;
        DatasetType[] arr = DatasetType.values();
        
        // Search until found
        while ( !found && counter < arr.length ) {
            
            // Fetch current iter from array
            DatasetType i = arr[counter];
            
            // If type matches note as found
            if ( i.isType(query) ) {
                output = counter;
                found = true;
            }
            
            // Otherwise move to next
            else {
                counter++;
            }
        }
        return output;
    }
    
    
    /**
     * Fetch DatasetType matching query, or return null
     * 
     * @param query
     * @return - DatasetType, null
     */
    public static DatasetType getType(String query) {
        int index = DatasetType.indexOf(query);
        if ( index > -1 ) {
            return DatasetType.values()[index];
        }
        else {
            return null;
        }
    }
}
