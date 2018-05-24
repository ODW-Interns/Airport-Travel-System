package airlinesystemcontroller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airlinesystemmodel.FlightList;

import java.io.IOException;

public class ReadModelDataIntoState {
	
	private static final String DELIM = "|";
	
	/*
	 * Read information from the input file then sends that information properly
	 * formatted to be added to the FlightList 
	 * */
	public void ReadFileInputIntoFlightList(FlightList listOfFlights_, String fileToRead_) throws IOException {
		Logger consoleLogger = LoggerFactory.getLogger("consoleLogger");
		consoleLogger.debug("Reading input file");
		
		String _source;
		String _destination;
		int _distanceTravelled;
		char _aircraftSize;
		int _maxSeatsPerSection[] = new int [4];
		int _seatsFilledPerSection[] = new int [4];
		BigDecimal _seatCostPerSection[] = new BigDecimal [4];
		
		try (InputStream _is = ReadModelDataIntoState.class.getResourceAsStream(fileToRead_)) {
			InputStreamReader _sr = new InputStreamReader(_is);
			BufferedReader reader = new BufferedReader(_sr);
			StringTokenizer tokenizer;
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll("\\s", "");
				if ("".equals(line)) {
					continue;
				}
				
				tokenizer = new StringTokenizer(line, DELIM);
				_source = tokenizer.nextToken();
				_destination = tokenizer.nextToken();
				_distanceTravelled = setDistanceTravelled(tokenizer.nextToken());
				_aircraftSize = setAircraftSize(tokenizer.nextToken());
				
				for (int i = 0; i < 4; i++) {
					_maxSeatsPerSection[i] = setMaxSeatsPerSection(tokenizer.nextToken());
				}
				for (int i = 0; i < 4; i++) {
					_seatsFilledPerSection[i] = setSeatsFilledPerSection(tokenizer.nextToken());
				}
				for (int i = 0; i < 4; i++) {
					_seatCostPerSection[i] = setSeatCostPerSection(tokenizer.nextToken());
				}
				listOfFlights_.addFlightToList(_aircraftSize, _maxSeatsPerSection,
						_seatsFilledPerSection, _seatCostPerSection, _source,
						_destination, _distanceTravelled);
			}
			consoleLogger.debug("Successfully read file");
			reader.close();
		}
	}
	
	public void ReadSingleFlightIntoFlightList(FlightList listOfFlights_, String flightInformation_) {
		flightInformation_ = flightInformation_.replaceAll("\\s", "");
		String _source;
		String _destination;
		int _distanceTravelled;
		char _aircraftSize;
		int _maxSeatsPerSection[] = new int [4];
		int _seatsFilledPerSection[] = new int [4];
		BigDecimal _seatCostPerSection[] = new BigDecimal [4];
		StringTokenizer tokenizer = new StringTokenizer(flightInformation_, DELIM);
		
		_source = tokenizer.nextToken();
		_destination = tokenizer.nextToken();
		_distanceTravelled = setDistanceTravelled(tokenizer.nextToken());
		_aircraftSize = setAircraftSize(tokenizer.nextToken());
		
		for (int i = 0; i < 4; i++) {
			_maxSeatsPerSection[i] = setMaxSeatsPerSection(tokenizer.nextToken());
		}
		for (int i = 0; i < 4; i++) {
			_seatsFilledPerSection[i] = setSeatsFilledPerSection(tokenizer.nextToken());
		}
		for (int i = 0; i < 4; i++) {
			_seatCostPerSection[i] = setSeatCostPerSection(tokenizer.nextToken());
		}
		
		listOfFlights_.addFlightToList(_aircraftSize, _maxSeatsPerSection,
				_seatsFilledPerSection, _seatCostPerSection, _source,
				_destination, _distanceTravelled);
	}
	
	public int setDistanceTravelled(String distanceTravelled_) {
		return Integer.parseInt(distanceTravelled_);
	}
	
	public char setAircraftSize(String aircraftSize_) {
		
		return aircraftSize_.charAt(0);
	}
	
	public int setMaxSeatsPerSection(String maxSeatsInSection_) {
		return Integer.parseInt(maxSeatsInSection_);
	}
	
	public int setSeatsFilledPerSection(String seatsFilledInSection_) {
		return Integer.parseInt(seatsFilledInSection_);
	}
	
	public BigDecimal setSeatCostPerSection(String seatCostInSection_) {
		BigDecimal _decimalReturn = new BigDecimal(seatCostInSection_);
		return _decimalReturn;
	}
}