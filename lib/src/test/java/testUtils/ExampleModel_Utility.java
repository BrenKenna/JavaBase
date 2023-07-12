/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testUtils;

import converter.ModelDatapointConverter;
import datapoint.Datapoint;
import datapoint.ModelAttribute;
import exampleModel_Tests.model.NameGenerator;
import exampleModel_Tests.model.Person;
import exampleModel_Tests.model.Square;
import exceptions.DataTypeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Utility class for testing library components with example models
 * 
 * @author kenna
 */
public class ExampleModel_Utility {
    
    private static final Random rand = new Random();
    private static final ModelDatapointConverter<Person> personConverter = new ModelDatapointConverter<>(Person.class);
    
    /**
     * Make random square
     * @return - Square
     */
    public static Square makeRandomSquare() {
        int size = rand.nextInt(29)+1;
        return new Square(size, size);
    }
    
   
    /**
     * Return random person
     * @return - Person
     */
    public static Person makeRandomPerson() {
        Map<String, String> name = NameGenerator.getRandomData();
        int age = rand.nextInt(59) + 1;
        char mInitial = name.get("Last Name").charAt(0);
        boolean state = rand.nextBoolean();
        return makePerson(name.get("First Name"), name.get("Last Name"), mInitial, age, state);
    }
    
    
    
    /**
     * Return square
     * 
     * @param length
     * @param width
     * @return - Square
     */
    public static Square makeSquare(int length, int width) {
        return new Square(length, width);
    }
    
    
    /**
     * Return Person from input
     * 
     * @param fName
     * @param lName
     * @param mInitial
     * @param age
     * @param state
     * @return - Person
     */
    public static Person makePerson(String fName, String lName, char mInitial, int age, boolean state) {
        return new Person(fName, lName, mInitial, age, state);
    }
    
    
    /**
     * Make ModelAttribute Map from Person Model
     * 
     * @param input
     * @return 
     * @throws exceptions.DataTypeException 
     */
    public static Map<String, ModelAttribute> makePersonMap(Person input) throws DataTypeException {
        
        // Initialize vars
        Map<String, ModelAttribute> output = new HashMap<>();
        ModelAttribute attrib;
        
        // First Name
        attrib = new ModelAttribute("fName", "First Name", input.getfName());
        output.put(attrib.getAlias(), attrib);
        
        // Last Name
        attrib = new ModelAttribute("lName", "Last Name", input.getlName());
        output.put(attrib.getAlias(), attrib);
        
        // Middle Initial
        attrib = new ModelAttribute("mNameInitial", "Middle Initial", input.getmNameInitial());
        output.put(attrib.getAlias(), attrib);
        
        // Age
        attrib = new ModelAttribute("age", "Age", input.getAge());
        output.put(attrib.getAlias(), attrib);
        
        // State
        attrib = new ModelAttribute("iState", "State", input.getState());
        output.put(attrib.getAlias(), attrib);
        
        // Return map
        return output;
    }
    
    
    /**
     * Return Datapoint from Person
     * 
     * @param input
     * @return - Datapoint
     * @throws exceptions.DataTypeException
     */
    public static Datapoint makePersonDatapoint(Person input) throws DataTypeException {
        
        // Fetch Model Attribute Map
        Map<String, ModelAttribute> attribMap = makePersonMap(input);
        return new Datapoint(attribMap, Person.class);
    }
    
    
    
    /**
     * Return ModelAttribute Map from Square Model
     * 
     * @param input
     * @return
     * @throws DataTypeException 
     */
    public static Map<String, ModelAttribute> makeSquareMap(Square input) throws DataTypeException {
    
        // Initialize vars
        Map<String, ModelAttribute> output = new HashMap<>();
        ModelAttribute attrib;
        
        // Length
        attrib = new ModelAttribute("length", "Length", input.getLength());
        output.put(attrib.getAlias(), attrib);
        
        // Last Name
        attrib = new ModelAttribute("width", "Width", input.getWidth());
        output.put(attrib.getAlias(), attrib);
        
        // Middle Initial
        attrib = new ModelAttribute("area", "Area", input.getArea());
        output.put(attrib.getAlias(), attrib);
        
        // Return map
        return output;
    }
    
    
    /**
     * Make Datapoint from Sqaure Model
     * 
     * @param input
     * @return
     * @throws DataTypeException 
     */
    public static Datapoint makeSquareDatapoint(Square input) throws DataTypeException {
    
        // Fetch Model Attribute Map
        Map<String, ModelAttribute> attribMap = makeSquareMap(input);
        return new Datapoint(attribMap, Square.class);
    }
    
    
    /**
     * Make random Datapoint from Square Model
     * 
     * @return - Datapoint from Square Model
     * @throws DataTypeException 
     */
    public static Datapoint makeRandomSquareDP() throws DataTypeException {
        Square model = ExampleModel_Utility.makeRandomSquare();
        return makeSquareDatapoint(model); 
    }
    
    
    /**
     * Make random Datapoint from Person Model
     * 
     * @return
     * @throws DataTypeException 
     */
    public static Datapoint makeRandomPersonDP() throws DataTypeException {
        Person model = ExampleModel_Utility.makeRandomPerson();
        return makePersonDatapoint(model); 
    }
    
    
    /**
     * Make a list of size N, of Random Person Objects
     * 
     * @param size
     * @return - List Person
     */
    public static List<Square> makeRandomSquareList(int size) {
        List<Square> output = new ArrayList<>();
        for ( int i = 0; i < size; i++ ) {
            output.add( makeRandomSquare() );
        }
        return output;
    }
    
    
    /**
     * Make a list of size N, of Random Person Objects
     * 
     * @param size
     * @return - List Person
     */
    public static List<Person> makeRandomPersonList(int size) {
        List<Person> output = new ArrayList<>();
        for ( int i = 0; i < size; i++ ) {
            output.add( makeRandomPerson() );
        }
        return output;
    }
    
    
    /**
     * Make a list N random Datapoints from Person objects
     * 
     * @param nPersons
     * @return - List of Datapoint from Person
     * @throws DataTypeException 
     */
    public static List<Datapoint> makePersonDPList(int nPersons) throws DataTypeException {
        List<Datapoint> output = new ArrayList<>();
        for ( int i = 0; i < nPersons; i++ ) {
            Datapoint person = makeRandomPersonDP();
            output.add(person);
        }
        return output;
    }
    
    
    /**
     * Make a list N random Datapoints from Square objects
     * 
     * @param nPersons
     * @return - List of Datapoint from Square
     * @throws DataTypeException 
     */
    public static List<Datapoint> makeSquareDPList(int nPersons) throws DataTypeException {
        List<Datapoint> output = new ArrayList<>();
        for ( int i = 0; i < nPersons; i++ ) {
            Datapoint square = makeRandomSquareDP();
            output.add(square);
        }
        return output;
    }
    
    
    /**
     * Convert list of person to list of datapoints in parallel stream
     * 
     * @param input
     * @return
     * @throws DataTypeException 
     */
    public static List<Datapoint> makePersonDpList_para(List<Person> input) throws DataTypeException {
        return input.parallelStream()
            .map( person -> personConverter.convertModel(person) )
            .toList()
        ;
    }
}
