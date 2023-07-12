/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exampleModel_Tests.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author kenna
 */
public class NameGenerator {
    
    // To generate first & last name
    private static final String[] first_names = {
        "Alex", "Michael", "Tom", "Sarah", "Lisa", "Michelle", "Alice", "Martin", "Martina",
        "Bill", "Mary", "Tim", "Bob", "Jane", "John", "Elizabeth", "Danielle", "Amy", "Peter",
        "Penelope", "Dick", "Harry"
    };
    private static final String[] last_names = {
        "Jones", "Doe", "Berkhiem", "Montgomery", "Simpson", "Sizlack", "Burns", "Ziff", "Baggins",
        "Skywalker", "Bond", "Bathory", "Doe", "East", "Collins", "Bakker", "Hamilton", "Harris"
    };

    
    /**
     * Generate random data for constructing student
     * 
     * @return String[ UUID, First Name, Last Name ]
     */
    public static Map<String, String> getRandomData() {
    
        // Initialize vars
        Map<String, String> output = new HashMap<>();
        Random r = new Random();
        
        // Put uuid, first name, and last name
        output.put("UUID", UUID.randomUUID().toString());
        output.put("First Name", first_names[ r.nextInt(first_names.length) ]);
        output.put("Last Name", last_names[ r.nextInt(last_names.length) ]);
        
        // Return output
        return output;
    }
}
