/**
 * AirportGraph class
 *		Creates an undirected weighted graph that allows 
 *		changes to the graph such as adding airports (vertexes),
 *		adding flights (edges), removing airports or flights.
 *		and finding information about the graph
 */
package org.airlinesystem.graphdb.impl;

import org.airlinesystem.graphdb.AirportGraphInterface;

import org.airlinesystem.model.Airport;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Comparator;


public class AirportGraph implements AirportGraphInterface {
	
	private Graph<String, DefaultEdge> graphOfAirports;
	private HashMap<String, Airport> mapAirportToName;

	/**
	 * Constructor, initializes graph
	 */
	public AirportGraph() {
		graphOfAirports = new SimpleWeightedGraph<String, DefaultEdge>(DefaultEdge.class);
		mapAirportToName = new HashMap<String, Airport>();
	}
	
	/**
	 * Add a new airport by creating a new vertex on the graph
	 * based on the airport object input
	 * 
	 * @param airport_
	 * 		Airport object to be added to the graph and mapped with
	 * 		its name
	 * @return
	 * 		N/A
	 */
	public void addAirport(Airport airport_) {
		graphOfAirports.addVertex(airport_.getName());
		mapAirportToName.put(airport_.getName(), airport_);
	}
	
	/**
	 * Add a new edge (flight) between vertices (airports) if they are not
	 * connected, not the same airport, and the distance is positive
	 * 
	 * @param source_
	 * 		String that represents name of first airport
	 * @param destination_
	 * 		String that represents name of second airport
	 * @param distance_
	 * 		double that represents distance between airports (weight)
	 * @return
	 * 		true if successfully created, false otherwise
	 */
	public boolean createEdge(String source_, String destination_, double distance_) {	
		Logger _debugLogger = LoggerFactory.getLogger("debugLogger");
		
		if (source_.equals(destination_) || areAirportsConnected(source_, destination_)
				|| distance_ <= 0) {
			_debugLogger.debug("Invalid graph input found, input ignored.");
			return false;
		}
		graphOfAirports.addEdge(source_, destination_);
		graphOfAirports.setEdgeWeight(graphOfAirports.getEdge(source_, destination_), distance_);
		return true;
	}
	
	/**
	 * Finds the distance between two airports based on their names
	 * 
	 *  @param source_
	 *  	String of the first airport name
	 *  @param destination_
	 *  	String of the second airport name
	 *  @return
	 *  	double of the weight (distance) between two airports \
	 *  	if they are connected, otherwise 0
	 */
	public double getDistance(String source_, String destination_) {
		if (areAirportsConnected(source_, destination_)) {
			return graphOfAirports.getEdgeWeight(graphOfAirports.getEdge(source_, destination_));
		}
		return 0;
	}

	/**
	 * Remove a connection between two airports
	 * only if there exists a connection
	 * 
	 * @param source_
	 * 		String of the first airport name
	 * @param destination_
	 * 		String of the second airport name
	 * @return
	 * 		N/A
	 */
	public void removeEdge(String source_, String destination_) {
		graphOfAirports.removeEdge(source_, destination_);
	}

	/**
	 * Remove an airport from the graph and all connections
	 * only if it exists
	 * 
	 * @param aiport_
	 * 		String of the name of an airport to remove
	 * @return
	 * 		N/A
	 */
	public void removeAirport(String airport_) {
		graphOfAirports.removeVertex(airport_);
		mapAirportToName.remove(airport_);
	}

	/**
	 * Find if an airport is present in the graph
	 * 
	 * @param airport_
	 * 		String of the name of the airport to search for
	 * @return
	 * 		true if aiprot found, false otherwise
	 */
	public boolean isAirportInGraph(String airport_) {
		return graphOfAirports.containsVertex(airport_);
	}
	
	/**
	 * Find if two airports are directly connected
	 * 
	 * @param source_
	 * 		String of the name of the first airport
	 * @param destination_
	 * 		String of the name of the second airport
	 * @return
	 * 		true, if airports are connected, false otherwise
	 */
	public boolean areAirportsConnected(String source_, String destination_) {
		return graphOfAirports.containsEdge(source_, destination_);
	}
	
	/**
	 * Returns airport object from hash map based on name
	 * 
	 * @param airportName_
	 * 		name of the airport wanted
	 * @return
	 * 		Airport object that corresponds to the name given
	 */
	public Airport getAirport(String airportName_) {
		return mapAirportToName.get(airportName_);
	}

	
	/**
	 * 	Sorts edges in ascending order
	 * 
	 * @return
	 * 		ArrayList<DefaultEdge> that is all edges sorted in ascending order
	 */
	public ArrayList<DefaultEdge> getSortedListOfEdges() {
		ArrayList<DefaultEdge> _sortedEdges = new ArrayList<DefaultEdge>();

		Comparator<DefaultEdge> _compareEdges = new Comparator<DefaultEdge>() {
			public int compare(DefaultEdge e1, DefaultEdge e2) {
				if(graphOfAirports.getEdgeWeight(e1) == graphOfAirports.getEdgeWeight(e2)) {
					return 0;
				}
				return graphOfAirports.getEdgeWeight(e1) < graphOfAirports.getEdgeWeight(e2) ? -1 : 1;
			}
		};
		
		_sortedEdges.addAll(graphOfAirports.edgeSet());
		_sortedEdges.sort(_compareEdges);
		
		return _sortedEdges;
	}
	
	/**
	 * Prints the current graph of the airports by iterating
	 * through the set of vertices and each of their edges
	 * 
	 * @return
	 * 		N/A
	 */
	public void printGraph() {
		Logger _consoleLogger = LoggerFactory.getLogger("consoleLogger");
		
		Iterator<String> _vertexItr = graphOfAirports.vertexSet().iterator();
		Iterator<DefaultEdge> _edgeItr;
		String _vertex;
		String _destinationVertex;
		DefaultEdge _edgeTracker;
		while(_vertexItr.hasNext()) {
			_vertex = _vertexItr.next();
			_consoleLogger.info("Vertex: {}", _vertex);
			_edgeItr = graphOfAirports.edgesOf(_vertex).iterator();
			while (_edgeItr.hasNext()) {
				_edgeTracker = _edgeItr.next();
				_destinationVertex = graphOfAirports.getEdgeTarget(_edgeTracker);
				if(_destinationVertex.equals(_vertex)) {
					_destinationVertex = graphOfAirports.getEdgeSource(_edgeTracker);
				}
				_consoleLogger.info("-> {}({})", _destinationVertex, 
						graphOfAirports.getEdgeWeight(_edgeTracker));
			}
		}
	}
	
	/**
	 * Completely empties the graph and any airport mappings
	 * 
	 * @return
	 * 		N/A
	 */
	public void clearGraph() {
		mapAirportToName.clear();
		graphOfAirports = new SimpleWeightedGraph<String, DefaultEdge>(DefaultEdge.class);
	}
	
	public Graph<String, DefaultEdge> getGraphOfAirports() {
		return graphOfAirports;
	}

	public HashMap<String, Airport> getMapAirportToName() {
		return mapAirportToName;
	}
}