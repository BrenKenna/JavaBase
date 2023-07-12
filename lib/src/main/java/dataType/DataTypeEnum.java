/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package dataType;

import exceptions.DataTypeException;


/**
 * Enum view on valid data types
 * 
 * @author kenna
 */
public enum DataTypeEnum {
    
    STRING_DATA_TYPE {
        @Override
        public boolean isType(Object query) {
            // System.out.println("String.class = " + String.class);
            // System.out.println("Query class = " + query.getClass());
            return String.class.equals(query.getClass());
        }

        @Override
        public int compareTo(Object ref, Object query) throws DataTypeException {
            
            // Validate inputs
            if ( !isType(ref) || !isType(query) ) {
                // System.out.println("Enum-Val-CompareTo: " + ref.getClass() + ", " + query.getClass());
                throw new DataTypeException("Error, both the query and the reference inputs must be String objects");
            }
            
            // Compare
            String refString = (String) ref;
            String queryString = (String) query;
            return refString.compareTo(queryString);
        }

        @Override
        public DataTypeEnum whichType() {
            return STRING_DATA_TYPE;
        }

        @Override
        public String toString() {
            return "String";
        }
        
        @Override
        public boolean isType(String query) {
            return STRING_DATA_TYPE.toString().toLowerCase().equals(query.toLowerCase());
        }
    },
    
    INT_DATA_TYPE {
        @Override
        public boolean isType(Object query) {
            // System.out.println(Integer.class + ", " + query.getClass());
            return Integer.class.equals(query.getClass());
        }

        @Override
        public int compareTo(Object ref, Object query) throws DataTypeException {
            
            // Validate inputs
            if ( !isType(ref) || !isType(query) ) {
                throw new DataTypeException("Error, both the query and the reference inputs must be Int objects");
            }
            
            // Compare
            int refInt = (int) ref;
            int queryInt = (int) query;
            return refInt - queryInt;
        }

        @Override
        public DataTypeEnum whichType() {
            return INT_DATA_TYPE;
        }

        @Override
        public String toString() {
            return "Integer";
        }
        
        @Override
        public boolean isType(String query) {
            return INT_DATA_TYPE.toString().toLowerCase().equals(query.toLowerCase());
        }
    },
    
    DOUBLE_DATA_TYPE {
        @Override
        public boolean isType(Object query) {
            return Double.class.equals(query.getClass());
        }

        @Override
        public int compareTo(Object ref, Object query) throws DataTypeException {
                        
            // Validate inputs
            if ( !isType(ref) || !isType(query) ) {
                throw new DataTypeException("Error, both the query and the reference inputs must be Double objects");
            }
            
            // Compare
            double refDouble = (double) ref;
            double queryDouble = (double) query;
            return (int) (refDouble - queryDouble);
        }

        @Override
        public DataTypeEnum whichType() {
            return DOUBLE_DATA_TYPE;
        }

        @Override
        public String toString() {
            return "Double";
        }
        
        @Override
        public boolean isType(String query) {
            return DOUBLE_DATA_TYPE.toString().toLowerCase().equals(query.toLowerCase());
        }
    },
    
    CHAR_DATA_TYPE {
        @Override
        public boolean isType(Object query) {
            return Character.class.equals(query.getClass());
        }

        @Override
        public int compareTo(Object ref, Object query) throws DataTypeException {
            
            // Validate inputs
            if ( !isType(ref) || !isType(query) ) {
                throw new DataTypeException("Error, both the query and the reference inputs must be Char objects");
            }
            
            // Compare
            char refChar = (char) ref;
            char queryChar = (char) query;
            return Character.compare(refChar, queryChar);
        }

        @Override
        public DataTypeEnum whichType() {
            return CHAR_DATA_TYPE;
        }

        @Override
        public String toString() {
            return "Character";
        }
        
        @Override
        public boolean isType(String query) {
            return CHAR_DATA_TYPE.toString().toLowerCase().equals(query.toLowerCase());
        }
    },
    
    BOOL_DATA_TYPE {
        @Override
        public boolean isType(Object query) {
            return Boolean.class == query.getClass();
        }

        @Override
        public int compareTo(Object ref, Object query) throws DataTypeException {
            
            // Validate inputs
            if ( !isType(ref) || !isType(query) ) {
                throw new DataTypeException("Error, both the query and the reference inputs must be Boolean objects");
            }
            
            // Config compare
            boolean refBool = (boolean) ref;
            boolean queryBool = (boolean) query;

            // 1 if query is false
            if ( refBool && !queryBool ) {
                return 1;
            }

            // Otherwise equal
            else if ( (refBool && queryBool) || (!refBool && !queryBool) ) {
                return 0;
            }

            // 1 if query is true
            else {
                return -1;
            }
        }

        @Override
        public DataTypeEnum whichType() {
            return BOOL_DATA_TYPE;
        }

        @Override
        public String toString() {
            return "Boolean";
        }

        @Override
        public boolean isType(String query) {
            return BOOL_DATA_TYPE.toString().toLowerCase().equals(query.toLowerCase());
        }
    }
    ;

    
    /**
     * Abstract method to check if Class types match
     * 
     * @param query
     * @return - True/False
     */
    public abstract boolean isType(Object query);
    
    
    /**
     * Abstract method to compare enum val as string
     * @param query
     * @return - True/False
     */
    public abstract boolean isType(String query);
    
    
    /**
     * Abstract method to numerically compare to objects.Since use case is with binary search, precision not that meaningful 
     * 
     * @param ref
     * @param query
     * @return - Distance of query from ref
     * @throws exceptions.DataTypeException
     */
    public abstract int compareTo(Object ref, Object query) throws DataTypeException;
    

    /**
     * Abstract method to allow an Enum value to return itself for a comparison
     * 
     * @return - DataTypeEnum
     */
    public abstract DataTypeEnum whichType();
    
    
    /**
     * Abstract method to return enum value as string
     * 
     * @return - String
     */
    @Override
    public abstract String toString();
    
    
    
    /**
     * Fetch index of query in Enum Values
     * 
     * @param query
     * @return - int, null = -1
     */
    public static int indexOfQuery(Object query) {
        
        // Scan for matching type
        int result = -1;
        int counter = 0;
        for( DataTypeEnum val : DataTypeEnum.values() ) {
            
            // Set array position
            if ( val.isType(query) ) {
                result = counter;
            }
            
            // Otherwise increment
            else {
                counter++;
            }
        }
        
        // Return result
        return result;
    }
    
    
    /**
     * Check if queried type is allowed
     * 
     * @param query
     * @return - True/False
     */
    public static boolean hasType(Object query) {
        return DataTypeEnum.indexOfQuery(query) >= 0;
    }
    
    
    /**
     * Return valid data type at index
     * 
     * @param index
     * @return - DataTypeEnum
     * @throws DataTypeException
     */
    public static DataTypeEnum getIndex(int index) throws DataTypeException {
        if (index >= 0) {
            return DataTypeEnum.values()[index];
        }
        throw new DataTypeException("Error, input provided is an invalid data type");
    }
    
    
    /**
     * Calculate the distance of the queried object to a supplied reference object of same type
     * 
     * @param ref
     * @param query
     * @return - int
     * @throws DataTypeException 
     */
    public static int compare(Object ref, Object query) throws DataTypeException {
        
        // Fetch index of both, could be -1
        //  which is handled next
        int refInd = DataTypeEnum.indexOfQuery(ref);
        int queryInd = DataTypeEnum.indexOfQuery(query);
        // System.out.println("Compare-Method: Index of Query same  = " + (refInd == queryInd));
        
        // Validate types
        if ( refInd != queryInd ) {
            throw new DataTypeException("Error, both the reference and query objects must be of the same type");
        }

        // Get reference type
        DataTypeEnum refEnum;
        try {
            refEnum = DataTypeEnum.getIndex(refInd);
        } catch (DataTypeException ex) {
            throw new DataTypeException("Error, input provided is an invalid data type");
        }
        
        // Compare type
        return refEnum.compareTo(ref, query);
    }
    
    
    /**
     * Represent enum as string
     * 
     * @return - String
     */
    public static String asString() {
        String output = "DataTypeEnum{";
        for (DataTypeEnum val : DataTypeEnum.values()) {
            output += val.toString() + ",";
        }
        output = output.replace(",$", "");
        return output;
    }

    
    /**
     * Query enum for a value
     * 
     * @param query
     * @return - DataTypeEnum
     * @throws DataTypeException 
     */
    public static DataTypeEnum getValue(Object query) throws DataTypeException {
        int index = DataTypeEnum.indexOfQuery(query);
        if (index >= 0) {
            return DataTypeEnum.values()[index];
        }
        else {
            String msg = "Error, unsupported data type was provided. Allowed values are: " + DataTypeEnum.asString();
            msg += "\n\nRecieved:\t" + query.getClass().getClass();
            throw new DataTypeException(msg);
        }
    }
    
    
    /**
     * Query DataType by the string representation
     * 
     * @param query
     * @return - DataTypeEnum
     * @throws DataTypeException 
     */
    public static DataTypeEnum getValue(String query) throws DataTypeException {
        for( DataTypeEnum val : DataTypeEnum.values()) {
            if ( val.isType(query) ) {
                return val;
            }
        }
        throw new DataTypeException("Error, unsupported data type was provided. Allowed values are: " + DataTypeEnum.asString());
    }
}
