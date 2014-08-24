package com.joc.jguii;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.Line;

/**
 * Reads config files and retrieves information from them.
 */
public class ConfigHandler extends BufferedReader{
	/**
	 * Path to and name of config file
	 */
	private String config;
	/**
	 * All {@link NamedPoint}s for each {@link Application} defined in config file
	 */
	private ArrayList<ArrayList<NamedPoint>> pointsList= new ArrayList<ArrayList<NamedPoint>>();
	/**
	 * The names for each {@link Application} defined in config file
	 */
	private ArrayList<String> appNamesList = new ArrayList<String>();
	
	/**
	 * @param config Path to config file
	 * @throws FileNotFoundException
	 */
	public ConfigHandler(String config) throws FileNotFoundException{
		super(new FileReader(config));
		this.config = config;
		try {
			updateConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Gets the path to the config file.
	 * @return Path to config file
	 * @see Application
	 */
	public String getConfig(){
		return config;
	}
	/**
	 * Gets the names of all {@link Application}s specified in config file.
	 * @return Names of all {@link Application}s
	 * @see Application
	 */
	public ArrayList<String> getAppNamesList(){
		return appNamesList;
	}
	/**
	 * Gets the list of points for the {@link Application} at the specified index.
	 * @param index Index of {@link Application}
	 * @return Points from specified {@link Application}
	 * @see Application
	 */
	public ArrayList<NamedPoint> getPoints(int index){
		return pointsList.get(index);
	}
	/**
	 * Gets the index of the {@link Application} with the given name.
	 * @param s Name to search for
	 * @return The index of the {@link Application} with the given name or -1 if none is found
	 */
	public int getApplicationIndex(String s){
		for(int i = 0; i < appNamesList.size(); i++){
			if(s.equals(appNamesList.get(i))){
				return i;
			}
		}
		return -1;
	}
	/**
	 * Gets the name of the {@link Application} at the given index.
	 * @param index Index to query
	 * @return The name of the {@link Application} at the given index
	 */
	public String getApplicationName(int index){
		return appNamesList.get(index);
	}
	/**
	 * Reads the info from the config file and loads it into local {@link ArrayList}s.
	 * @throws IOException
	 * @see ArrayList
	 */
	public void updateConfig() throws IOException{
		String line;		//Buffer for holding the most recently read line
		String pointName;	//Buffer for holding name of most recent point
		int n, np;			//Numbers of arguments read from config file, no. of applications and points respectively
		int x, y;			//Buffers for holding x and y co-ords of most recent point
		ArrayList<NamedPoint> currentPointsList = null;
		
		//skip whitespace and comments to beginning of next section
		while(!(line = readLine()).equals("[applications]"))
			;
		line = readLine();
		n = Integer.parseInt(String.valueOf(nextAppropriate(line, '=')));
		
		for(int i = 0; i < n; i++){
			//skip whitespace and comments to beginning of next section
			while(!(line = readLine()).equals("[application" + i + "]"))
				;
			appNamesList.add(getBetween(readLine(), '"', '"'));
			
			np = Integer.parseInt(String.valueOf(nextAppropriate(readLine(), '=')));
			
			currentPointsList = new ArrayList<NamedPoint>();
			for(int j = 0; j < np; j++){
				pointName = getBetween(readLine(), '"', '"');
				x = Integer.parseInt(String.valueOf(nextAppropriate(readLine(), '=')));
				y = Integer.parseInt(String.valueOf(nextAppropriate(readLine(), '=')));
				currentPointsList.add(new NamedPoint(x, y, pointName));
			}
			pointsList.add(currentPointsList);
			currentPointsList = null;
		}
	}
	
	// Setting config is pointless. A new Reader is required so close this from ApplicationHandler and call a new one
	
	/**
	 * Gets a substring beginning from the first occurrence of <code>begin</code> or null if no occurrence is found.
	 * @param line Line to be searched
	 * @param begin Char to be searched for in line
	 * @return A substring beginning at first occurrence of <code>begin</code> or null if no occurrence of it is found
	 */
	private String nextAppropriate(String line, char begin){
		for(int i = 0; i < line.length(); i++){
			if(line.charAt(i) == begin)
				return line.substring(i+1);
		}
		return null;
	}
	
	/**
	 * Gets a substring starting from the first occurrence of <code>begin</code> and ending at first occurrence of 
	 * <code>end</code>.
	 * <p>
	 * Gets a substring starting from the first occurrence of <code>begin</code> and ending at first occurrence of
	 * <code>end</code> after <code>begin</code>. The characters <code>begin</code> and <code>end</code> are 
	 * not included in substring. Returns null if both <code>begin</code> and <code>end</code> are not found.
	 * </p>
	 * @param line Line to be searched
	 * @param begin Char substring will begin from 
	 * @param end Char substring will end at
	 * @return A substring beginning at first occurrence of <code>begin</code> and ending at first occurrence of
	 * <code>end</code> after <code>begin</code>
	 */
	private String getBetween(String line, char begin, char end){
		int a = -1, b = -1;
		for(int i = 0; i < line.length(); i++){
			if(line.charAt(i) == begin){
				a = i+1;
				break;
			}
		}
		if(a == -1)
			return null;
		for(int i = a; i < line.length(); i++){
			if(line.charAt(i) == end){
				b = i;
				break;
			}
		}
		if(b == -1)
			return null;
		else
			return line.substring(a, b);
	}
	
}