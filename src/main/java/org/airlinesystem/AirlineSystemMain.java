package org.airlinesystem;

import java.text.NumberFormat;
import java.io.File;
import java.io.IOException;

import org.airlinesystem.controller.ConsoleViewController;
import org.airlinesystem.helpers.AirlineSimulationBuilder;
import org.airlinesystem.helpers.DefaultsLoader;
import org.airlinesystem.model.AirlineSimulation;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirlineSystemMain {

	public static void main(String[] args_) {
		File _propertiesFile = new File(System.getProperty("user.dir") + "/airlinesystem-defaults/default.properties");
		File _graphFile = new File(System.getProperty("user.dir") + "/airlinesystem-defaults/default-graph");
		File _dataFile = new File(System.getProperty("user.dir") + "/airlinesystem-defaults/default-data");
		boolean _menuFlag = false;
		boolean _dataFileFlag = false;
		
		Logger _consoleLogger = LoggerFactory.getLogger("consoleLogger");
		
		CommandLineParser _parser = new DefaultParser();
		HelpFormatter _formatter = new HelpFormatter();
		AirlineSimulationBuilder _simulator = new AirlineSimulationBuilder();
		AirlineSimulation _simulation = new AirlineSimulation();
		ConsoleViewController _consoleOut = new ConsoleViewController();
		DefaultsLoader _loadDefaults = new DefaultsLoader();
		
		try {
			_loadDefaults.createDefaultsInUserFilesystem();
		} catch(IOException _e) {
			_e.getMessage();
		}

		/*
		 *  Handles the parsing of command line arguments passed to the main
		 */
		Options _options = new Options();
		
		_options.addOption("p", "properties", true, "Properties file");
		_options.addOption("g", "graph", true, "Graph file");
		_options.addOption("d", "data", true, "Data file");
		_options.addOption("m", "menu", false, "Load terminal menu");
		_options.addOption("h", "help", false, "Outputs the help descriptions");
		
		try {
			CommandLine _cl = _parser.parse(_options, args_);
			
			if(_cl.hasOption("h")) {
				_formatter.printHelp("airporttravel", _options, true);
				return;
			}
		
			if(_cl.hasOption('p')) {
				_propertiesFile = new File("/" + _cl.getOptionValue('p'));
			}
		
			if(_cl.hasOption('g')) {
				_graphFile = new File("/" + _cl.getOptionValue('g'));
			}
			
			if(_cl.hasOption('d')) {
				_dataFile = new File("/" + _cl.getOptionValue('d'));
				_dataFileFlag = true;
			}
			
			if(_cl.hasOption('m')) {
				_menuFlag = true;
			}
			
		} catch(ParseException _e) {
			_e.getMessage();
		}

		/*
		 *  Decides whether or not a console menu was requested
		 */
		if(!_menuFlag) {
			if(_dataFileFlag) {
				try {
					_simulator.runFromDataFile(_propertiesFile, _dataFile, _simulation);
				} 
				catch (Exception e_) {
					_consoleLogger.error("Error reading data, cannot run simulation\n");
					return;
				}
			}
			else {
			_simulator.runSimulation(_propertiesFile, _graphFile, _simulation);
			}
			NumberFormat _numberFormatter = NumberFormat.getInstance();
			_consoleLogger.info("Total Profit = $" + _numberFormatter.format(_simulation.getTotalProfit()));
			return;
		}
		
		/*
		 *  Runs the console menu if it is applicable
		 */
		String _fileNameList[] = {_propertiesFile.getAbsolutePath(), _graphFile.getAbsolutePath()};
		_consoleOut.menuController(_consoleLogger, _fileNameList, _simulation, _simulator, _dataFile);
	}
}
