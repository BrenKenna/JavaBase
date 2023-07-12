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

import java.util.UUID;


/**
 * Class to provide an iterator for a Dataset. View is that a Dataset
 *  knows about the client-side iterators, and can be dropped based on a
 *  system constant, or client provided time frame
 * 
 * @author kenna
 */
public class DatasetIterator {
    
    // Attributes
    private String id;
    private boolean hasNext;
    private int startIndex, currentIndex, endIndex;
    
    
    /**
     * Default consructor
     */
    public DatasetIterator() {
        this.id = UUID.randomUUID().toString();
        this.hasNext = false;
        this.startIndex = -1;
        this.currentIndex = -1;
        this.endIndex = -1;
    }
    
    
    /**
     * Construct with params
     * 
     * @param hasNext
     * @param startIndex
     * @param currentIndex
     * @param endIndex 
     */
    public DatasetIterator(boolean hasNext, int startIndex, int currentIndex, int endIndex) {
        this.id = UUID.randomUUID().toString();
        this.hasNext = hasNext;
        this.startIndex = startIndex;
        this.currentIndex = currentIndex;
        this.endIndex = endIndex;
    }
    
    
    /**
     * Return whether another index can be fetched
     * 
     * @return - True/False
     */
    public boolean hasNext() {
        return currentIndex <= endIndex;
    }
    
    
    /**
     * Increment current index if possible
     */
    public void next() {
        if ( hasNext() ) {
            currentIndex++;
        }
    }
    
    
    /**
     * Return whether previous index can be fetched
     * 
     * @return - True/False 
     */
    public boolean hasPrevious() {
        return currentIndex >= startIndex;
    }
    
    
    /**
     * Decrement current index if possible
     */
    public void previous() {
        if ( hasPrevious() ) {
            currentIndex--;
        }
    }
    
    
    /**
     * Return the number of the remaining next indexes
     * 
     * @return - int
     */
    public int remainingNext() {
        return endIndex - currentIndex;
    }
    
    
    /**
     * Return he number of the remaining previous indexes
     * 
     * @return - int
     */
    public int remainingPrevious() {
        return currentIndex - startIndex;
    }
    
    
    /**
     * Return the current index
     * 
     * @return - int 
     */
    public int getCurrent() {
        return this.currentIndex;
    }
    
    
    /**
     * Return the starting index
     * 
     * @return - int 
     */
    public int getStart() {
        return this.startIndex;
    }
    
    
    /**
     * Return the last index
     * 
     * @return 
     */
    public int getEnd() {
        return this.endIndex;
    }
    
    
    /**
     * Return the size of the Iterator
     * 
     * @return - int
     */
    public int getSpan() {
        return endIndex - startIndex;
    }
    
    
    /**
     * Return the Iterator Id
     * 
     * @return - String 
     */
    public String getId() {
        return this.id;
    }
    
    
    /**
     * Represent the DatasetIterator as string
     * 
     * @return - String
     */
    @Override
    public String toString() {
        return "DatasetIterator{" +
            "id=" + id +
            ", hasNext=" + hasNext +
            ", startIndex=" + startIndex +
            ", currentIndex=" + currentIndex +
            ", endIndex=" + endIndex +
        '}';
    }
}
