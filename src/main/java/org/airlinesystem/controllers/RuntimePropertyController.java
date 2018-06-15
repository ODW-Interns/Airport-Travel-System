/**
 * RuntimePropertyController class
 *  	Manages the loading of the properties file that are specific
 *  	to this program.
 */

package org.airlinesystem.controllers;

import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

import org.airlinesystem.controllers.logging.FullLogging;

public class RuntimePropertyController {

	private FullLogging propertyControllerLog = FullLogging.getInstance();

	/**
	 * Loads the default.properties file and throws exceptions
	 * if it is unable to.
	 * 
	 * @return the Properties object with the defaults loaded
	 */
	public Properties loadDefaultProperties() {
		Properties _defaultProperties = new Properties();

		try (InputStream _is = this.getClass().getResourceAsStream("/default.properties")) {
			_defaultProperties.load(_is);
			propertyControllerLog.debugDebug("defaults loaded...");
		} catch(NullPointerException e_){
			propertyControllerLog.consoleError("Could not find default.properties file");
		} catch(IOException e_) {
			propertyControllerLog.consoleError("Error reading default.properties file");
		} catch (Exception e_) {
			propertyControllerLog.consoleError("Error reading default.properties file");

		}	
		return _defaultProperties;
	}

	/**
	 *  Loads in the runtime properties for a specified properties file and
	 *  reverts to default if it is not readable
	 *  
	 *  @param the string filename/path of the custom properties file
	 *  @return the loaded properties file, default or custom
	 */
	public Properties createRuntimeProperties(File file_) {
		Properties _properties = new Properties();

		try (InputStream _is = new FileInputStream(file_)) {
			_properties.load(_is);
		} catch(IOException e_){
			_properties = loadDefaultProperties();
			propertyControllerLog.consoleError("Unable to use " + file_ + 
					", reverting to default properties. " + e_.getMessage());
		} catch(NullPointerException e_) {
			propertyControllerLog.consoleError("Unable to use " + file_ +
					", reverting to default properties. " + e_.getMessage());
			_properties = loadDefaultProperties();	
		}
		
		return _properties;
	}

	/**
	 *  Decides whether to load the defaults or use the custom properties 
	 *  file, then calls the appropriate method for loading.
	 *  
	 *  @param the string filename/path of the properties file
	 *  @return the loaded Properties object
	 */
	public Properties loadRuntimeProperties(File file_) {
		Properties _returnProperties;

		if(file_.getName().matches("default.properties")) {
			_returnProperties = loadDefaultProperties();
		} else {
			_returnProperties = createRuntimeProperties(file_);
		}
		return _returnProperties;
	}
}

