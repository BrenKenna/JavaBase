/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appLogging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * Class to hold log4j logger, long-term view offer methods to adjust properties
 * 
 * @author kenna
 */
public class AppLogging {
    
    public static final Logger logger = LogManager.getLogger(AppLogging.class.getName()); 
}