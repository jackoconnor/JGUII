package com.joc.jguii;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Object for manipulating external applications.
 * <p>
 * Used to open, focus and close external applications as 
 * well as click on any of its points. Usually only used 
 * through <code>ApplicationHandler</code>.
 * </p>
 */
public class Application {
	/**
	 * Window id for this Application.
	 */
	private int id;
	/**
	 * Name of the bin that is executed and name in titlebar of window that is used in windowSearch.
	 */
	private String name;
	/**
	 * Points specified for this Application in the config file.
	 */
	private ArrayList<NamedPoint>points;
	/**
	 * This Application's Process.
	 * @see Process
	 */
	private Process proc;
	/**
	 * Search for a window with <code>search</code> in its title.
	 * <p>
	 * Native method to return the window id of 
	 * a window that has the <code>String search</code>
	 * in its title.
	 * </p>
	 * @param search	The string to search for.
	 * @return 			The id of the window or -1 if none is found.
	 */
	private native int windowSearch(String search);
	
	/**
	 * Sets the currently focused application to id.
	 * <p>
	 * Sets the currently focused application to 
	 * the window that has id <code>id</code>.
	 * </p>
	 * @param id 	The id of the window to focus.
	 */
	private native void setWindowFocus(int id);		//focuses and raises the specified window

	/*
	 * Load native libs
	 */
	static{
		System.loadLibrary("xwindowtools");
	}
	
	/**
	 * Object for manipulating external applications.
	 * <p>
	 * Used to open, focus and close external applications as 
	 * well as click on any of its points. Usually only used 
	 * through <code>ApplicationHandler</code>.
	 * Calls {@link #start()} and {@link #getInitId()}
	 * </p>
	 * @param name   The name of the bin that is executed and the string that is used to search for the window.
	 * @param points The list of points that can be clicked on.
	 * @see   ApplicationHandler
	 */
	public Application(String name, ArrayList<NamedPoint>points){
		this.name = name;
		this.points = points;
		start();
		id = getInitId();
	}
	/**
	 * Starts the Application.
	 * <p>
	 * Starts the Application by
	 * executing the binary located at: "/usr/bin/" 
	 * with the name <code>name</code>.
	 * </p>
	 */
	private void start(){
		try{
			proc = Runtime.getRuntime().exec("/usr/bin/" + name);
		}catch(IOException e){
			e.printStackTrace();
		}
		return;
	}
	
	/**
	 * Closes the application.
	 * <p>
	 * Closes the Application by
	 * destroying its process.
	 * </p>
	 */
	public void close(){
		proc.destroy();
	}
	
	/**
	 * Focuses and raises the application.
	 */
	public void focus(){
		setWindowFocus(id);
	}
	/**
	 * Retrieves the window id of this application just after it has been initially started.
	 * <p>
	 * Searches for it every second until it is found.
	 * </p>
	 * @return the window id of this Application.
	 */
	private int getInitId(){
		int id = -1;
		try{
			while((id = windowSearch(name)) == -1)
				Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		return id;
	}
	/**
	 * Gets the window id of this application.
	 * @return The window id of this Application.
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * Gets the point at index <code>i</code>.
	 * @param i The index to get.
	 * @return  The point at index <code>i</code>.
	 */
	public Point getPoint(int i){
		return points.get(i);
	}
	/**
	 * Gets the point with name <code>name</code>.
	 * @param name The name to search for.
	 * @return The point with name <code>name</code> or null if none is found.
	 */
	public Point getPoint(String name){
		for(int i = 0; i < points.size(); i++){
			if(name.equals(points.get(i).getName())){
				return points.get(i);
			}
		}
		return null;
	}
}