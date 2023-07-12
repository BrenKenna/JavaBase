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
package datapoint;

/**
 * Enum to support Active, Dropped states of Datapoints
 * 
 * @author kenna
 */
public enum DatapointState {
    
    ACTIVE {
        @Override
        public String toString() {
            return "Active";
        }

        @Override
        public boolean isType(String query) {
            return "active".equals( query.toLowerCase() );
        }

        @Override
        public boolean isType(DatapointState query) {
            return ACTIVE == query;
        }
    },
    
    DROPPED {
        @Override
        public String toString() {
            return "Dropped";
        }

        @Override
        public boolean isType(String query) {
            return "dropped".equals( query.toLowerCase() );
        }

        @Override
        public boolean isType(DatapointState query) {
            return DROPPED == query;
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
    public abstract boolean isType(DatapointState query);
    
    
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
        DatapointState[] arr = DatapointState.values();
        
        // Search until found
        while ( !found && counter < arr.length ) {
            
            // Fetch current iter from array
            DatapointState i = arr[counter];
            
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
     * Fetch DatapointState matching query, or return null
     * 
     * @param query
     * @return - DatapointState, null
     */
    public static DatapointState getState(String query) {
        int index = DatapointState.indexOf(query);
        if ( index > -1 ) {
            return DatapointState.values()[index];
        }
        else {
            return null;
        }
    }
}
