/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexInterface;


import exceptions.DataTypeException;
import java.util.ArrayList;
import java.util.List;


/**
 * BinarySearchTree of built on Generic IndexEntry<>
 *  
 * @author kenna
 * @param <T>
 */
public class BinarySearchTree<T> {
    
    // Root node
    private IndexEntry<T> root;
    private int size = 0;
    private int duplicates = 0;
    private int counter = 0;
    
    
    /**
     * 
     */
    public BinarySearchTree() {
        this.root = null;
    }
    
    
    
    /**
     * Recursively add a new element to tree
     * 
     * @param current
     * @param newNode
     * @return - IndexEntry
     * 
     * @throws DataTypeException
     * @throws DataTypeException 
     */
    private IndexEntry addRecursive(IndexEntry current, IndexEntry newNode) throws DataTypeException {
    
        // Make root if null
        if ( current == null ) {
            return newNode;
        }
        
        // Otherwise measure distance, and place node left if smaller
        int distance = current.compareData(newNode);
        if ( distance > 0 ) {
            current.setLeft( addRecursive(current.getLeft(), newNode) );
        }
        
        // Right if larger than reference
        else if ( distance < 0) {
            current.setRight( addRecursive(current.getRight(), newNode) );
        }
        
        // Otherwise are equal
        else if (distance == 0) {
            duplicates++;
            current.addDuplicate(newNode);
        }
        return current;
    }
    
    
    /**
     * Exposed method for adding element to tree
     * 
     * @param node
     * @throws DataTypeException 
     */
    public void add(IndexEntry node) throws DataTypeException {
        root = addRecursive(root, node);
        size++;
    }
    
    
    /**
     * Recursively query if tree contains a value
     * 
     * @param current
     * @param query
     * @return - True/False
     * 
     * @throws DataTypeException 
     */
    private boolean containsRecursive(IndexEntry current, Object query) throws DataTypeException {
        
        // False if root node is null/tree is empty
        counter++;
        if ( current == null ) {
            return false;
        }
        
        // True if values are same
        int distance = current.compareData(query);
        // System.out.println("\nCurrent Search:\nReference = " + current + "\nQuery = " + query + "\nDistance = " + distance);
        if ( distance == 0) {
            return true;
        }
        
        // Left if smaller than reference
        if ( distance > 0 ) {
            // System.out.println("Moving Left onto:\t" + current.getLeft() + "\n");
            return containsRecursive(current.getLeft(), query);
        }
        
        // Otherwise right if larger than reference
        else {
            // System.out.println("Moving Right onto:\t" + current.getRight() + "\n");
            return containsRecursive(current.getRight(), query);
        }
    }
    
    
    /**
     * Check if tree contains queried value
     * 
     * @param query
     * @return - True/False
     * 
     * @throws DataTypeException
     */
    public boolean contains(Object query) throws DataTypeException {
        this.counter = 0;
        boolean result = containsRecursive(root, query);
        //System.out.println("Iteration count = " + counter);
        this.counter = 0;
        return result;
    }
    
    
    
    /**
     * Recursively navigate tree for node matching query
     * 
     * @param current
     * @param query
     * @return - IndexEntry
     * 
     * @throws DataTypeException
     */
    private IndexEntry getRecursive(IndexEntry current, Object query) throws DataTypeException {
        
        // Null if tree is empty
        if ( current == null ) {
            return null;
        }
        
        // Measure distance, return node if equal
        int distance = current.compareData(query);
        if (distance == 0) {
            return current;
        }
        
        // Left if reference is larger than query
        if ( distance > 0 ) {
            return getRecursive(current.getLeft(), query);
        }
        
        // Right if reference is smaller than query
        else {
            return getRecursive(current.getRight(), query);
        }
    }
    
    
    /**
     * Fetch index entry matching query
     * 
     * @param query
     * @return - IndexEntry
     * @throws DataTypeException 
     */
    public IndexEntry get(Object query) throws DataTypeException {
        return getRecursive(root, query);
    }
    
    
    /**
     * Fetch all records matching querying
     * 
     * @param query
     * @return - List IndexEntry
     * @throws DataTypeException
     */
    public List<IndexEntry> getAll(Object query) throws DataTypeException {
        
        // Initialize output and fetch tree componenet
        List<IndexEntry> output = new ArrayList<>();
        IndexEntry starter = get(query);
        
        // Handle null/no result
        if ( starter == null ) { return output; }
        output = starter.getWithDups();
        
        // Displaying dups
        /*System.out.println("\n\nBinarySearchTree-getAll(): Displaying Query Results from Tree");
        for ( IndexEntry i : output ) {
            System.out.println(i);
        }
        System.out.println("\n\nBinarySearchTree-getAll(): Displaying Query Results from Tree\n\n");
        */
        return output;
    }
    
    
    /**
     * Get the primary key pointers for query
     * 
     * @param query
     * @return - List Integer = Primary Keys
     * @throws DataTypeException
     */
    public List<Integer> getPrimaryKeys(Object query) throws DataTypeException {
        
        // Initialize output & run query
        List<Integer> output = new ArrayList<>();
        List<IndexEntry> results = getAll(query);
        
        // Handle null/ no results
        if ( results == null ) { return output;}
        if ( results.isEmpty() ) { return output; }
        
        // Fetch primary keys
        for ( IndexEntry i : results ) {
            output.add( i.getPrimaryKey() );
        }
        return output;
    }
    
    
    /**
     * Return whether nodes in tree point to the same primary key
     * 
     * @param ref
     * @param query
     * @return - True/False
     */
    private boolean matchPrimaryKeys(IndexEntry ref, IndexEntry query) {
        
        // Get primary keys
        int refPK = ref.getPrimaryKey();
        int queryPK = query.getPrimaryKey();
        
        // Compare keys
        return refPK == queryPK;
    }
    
    
    /**
     * Determine where/whether queried treeNode places on its neighbour
     * 
     * @param neighbour
     * @param query
     * @return - NodePlacementEnum Left, Right or Null
     */
    public NodePlacementEnum getPlacement(IndexEntry neighbour, IndexEntry query) {
    
        // Handle query being root
        if ( neighbour == null ) {
            return NodePlacementEnum.ROOT;
        }
        
        // Initialize neighbours left/right
        IndexEntry neighbourLeft = neighbour.getLeft();
        IndexEntry neighbourRight = neighbour.getRight();
        
        // Handle null child
        if ( neighbourLeft == null) {
            return NodePlacementEnum.RIGHT;
        }
        if ( neighbourRight == null) {
            return NodePlacementEnum.LEFT;
        }
        
        // Match left
        if ( matchPrimaryKeys(neighbourLeft, query) ) {
            return NodePlacementEnum.LEFT;
        }
        
        // Match right
        else if ( matchPrimaryKeys(neighbourRight, query) ) {
            return NodePlacementEnum.RIGHT;
        }
        
        // Otherwise null
        else {
            return NodePlacementEnum.NULL;
        }
    }

    
    /**
     * Filter querying a value by primary key
     * 
     * @param value
     * @param primaryKey
     * @return - IndexEntry
     * 
     * @throws DataTypeException
     */
    public IndexEntry filterPrimaryKey(Object value, int primaryKey) throws DataTypeException {
        
        // Initialize and manage null records
        List<IndexEntry> candidates = this.getAll(value);
        if ( candidates.isEmpty() ) {
            return null;
        }

        // Otherwise scan candidates for first matching primary key
        for ( int i = 0; i < candidates.size(); i++ ) {
            IndexEntry candidate = candidates.get(i);
            if ( candidate.getPrimaryKey() == primaryKey ) {
                return candidate;
            }
        }
        
        // Otherwise no reoord <- Be a weird one
        return null;
    }
    
    
    /**
     * Recursively fetch parent whose child will match a query
     * 
     * @param current
     * @param query
     * @return
     * @throws DataTypeException
     */
    private IndexEntry getParentRecursive(IndexEntry current, Object query) throws DataTypeException {
    
        // Return null if root
        if ( current == null) {
            return null;
        }
        
        // Measure distance
        int distance = current.compareData(query);
        if ( distance > 0) {
            IndexEntry nextNode = current.getLeft();
            if ( nextNode.compareData(query) == 0 ) {
                return current;
            }
            return getParentRecursive(nextNode, query);
        }
        
        // Handle right
        if (distance < 0) {
            IndexEntry nextNode = current.getRight();
            if ( nextNode.compareData(query) == 0 ) {
                return current;
            }
            return getParentRecursive(nextNode, query);
        }
        
        // Return null
        return null;
    }
    
    
    /**
     * Get parent node matching query
     * 
     * @param query
     * @return - IndexEntry
     * @throws DataTypeException
     */
    public IndexEntry getParent(Object query) throws DataTypeException {
        return getParentRecursive(root, query);
    }
    
    
    /**
     * Find smallest value from node in tree
     * 
     * @param root
     * @return - Object
     */
    private Object getSmallestValue(IndexEntry root) {
        return root.getLeft() == null ? root.getData() : getSmallestValue(root.getLeft());
    }
    
    
    /**
     * Recursively
     * 
     * @param current
     * @param query
     * @return
     * 
     * @throws DataTypeException
     */
    private IndexEntry dropRecursive(IndexEntry current, Object query) throws DataTypeException {
        
        // Handle root
        if (current == null) {
            return null;
        }
        
        // Handle when found
        int distance = current.compareData(query);
        if (distance == 0) {
            
            // Either handleDeletingNode, or update the right for other
            return handleDropping(current);
        }
        
        // Handle left
        if ( distance > 0 ) {
            current.setLeft( dropRecursive(current.getLeft(), query) );
            return current;
        }
        
        // Handle right
        current.setRight( dropRecursive(current.getRight(), query) );
        return current;
    }
    
    
    /**
     * Handle deleting node once found
     * 
     * @param current
     * @return - IndexEntry
     * 
     * @throws DataTypeException
     */
    private IndexEntry handleDropping(IndexEntry current) throws DataTypeException {
        
        // Handle leaf node
        if ( current.getLeft() == null && current.getRight() == null ) {
            return null;
        }
        
        // Handle one child
        if ( current.getRight() == null ) {
            return current.getLeft();
        }
        if ( current.getLeft() == null ) {
            return current.getRight();
        }
        
        // Handle two children
        Object minVal = getSmallestValue(current.getRight());
        current.setData(minVal);
        current.setRight( dropRecursive(current.getRight(), minVal) );
        return current;
    }

    
    /**
     * Delete node matching query
     * 
     * @param query
     * 
     * @throws DataTypeException
     */
    public void drop(Object query) throws DataTypeException {
        root = dropRecursive(root, query);
        size--;
    }
    
    
    /**
     * Handle replacing the current node, for the replacement node on Parent
     * 
     * @param current
     * @param replacement
     * @throws DataTypeException 
     */
    private NodePlacementEnum handleReplacementNodeParent(IndexEntry current, IndexEntry replacement) throws DataTypeException {
        
        // Get PK & parent node
        int primaryKey = replacement.getPrimaryKey();
        IndexEntry parentNode = getParent(current.getData());
        
        // Handle when current is root
        if ( parentNode == null ) { return NodePlacementEnum.ROOT; }
        
        // Handle replacing the parent node to point to replacement
        if (parentNode.getLeft() != null) {
            if (parentNode.getLeft().getPrimaryKey() == primaryKey) {
                parentNode.setLeft(replacement);
                return NodePlacementEnum.LEFT;
            }
        } 
        
        // Handle right
        else {
            if (parentNode.getRight().getPrimaryKey() == primaryKey) {
                parentNode.setRight(replacement);
                return NodePlacementEnum.RIGHT;
            }
        }
        
        // Otherwise null
        return NodePlacementEnum.NULL;
    }
    
    
    /**
     * Handle deleting a duplicate, by replacing current with first. Or, dropping dup from list
     * 
     * @param current
     * @param primaryKey
     * @param indexOfDup
     * @return - IndexEntry
     * 
     * @throws DataTypeException 
     */
    private IndexEntry handleDuplicate(IndexEntry current, int primaryKey) throws DataTypeException {
    
        // Handle current node being the target
        int indexOfDup = current.indexOfDup(primaryKey);
        if ( indexOfDup < 0 ) {
            
            // Get dups & pop first
            List<IndexEntry> dups = current.getDuplicates();
            IndexEntry replacement = dups.remove(0);
            
            // Assign dup list
            replacement.setDuplicates(dups);
            
            // Take current nodes Left and Right
            replacement.setLeft(current.getLeft());
            replacement.setRight(current.getRight());
            
            // Handle parent pointig to current
            handleReplacementNodeParent(current, replacement);
            return replacement;
        }
        
        // Otherwise just drop from list
        current.dropDuplicate(indexOfDup);
        return current;
    }

    
    /**
     * Internal helper method to drop node by query value & primary key
     * 
     * @param current
     * @param query
     * @param primaryKey
     * @return
     * @throws DataTypeException 
     */
    private IndexEntry dropByPrimaryKeyRecursive(IndexEntry current, Object query, int primaryKey) throws DataTypeException {
    
        // Handle root
        if (current == null) {
            return null;
        }
        
        // Handle when queried value is found
        int distance = current.compareData(query);
        if (distance == 0) {
            
            // Drop normally if no duplicates
            if ( !current.hasDuplicate() ) {
                return handleDropping(current);
            }
            
            // Otherwise handle duplicates
            else {
                IndexEntry replacement = handleDuplicate(current, primaryKey);
                return replacement;
            }
            
        }
        
        // Handle left
        if ( distance > 0 ) {
            current.setLeft( dropRecursive(current.getLeft(), query) );
            return current;
        }
        
        // Handle right
        current.setRight( dropRecursive(current.getRight(), query) );
        return current;
    }
    
    
    /**
     * Drop node from tree matching query & primary key
     * 
     * @param query
     * @param primaryKey
     * @throws DataTypeException 
     */
    public void dropByPrimaryKey(Object query, int primaryKey) throws DataTypeException {
        root = dropByPrimaryKeyRecursive(root, query, primaryKey);
        size--;
    }
    
    
    /**
     * Get tree size
     * 
     * @return - int 
     */
    public int getTreeSize() {
        return size;
    }
    
    
    /**
     * Get duplicate count
     * 
     * @return - int 
     */
    public int getDuplicateCount() {
        return duplicates;
    }
}
