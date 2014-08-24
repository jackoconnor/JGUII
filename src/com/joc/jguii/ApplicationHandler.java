package com.joc.jguii;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Object for interacting with {@link Application}s.
 */
public class ApplicationHandler{
	/**
	 * Operating system, not currently used.
	 */
	private String osName;
	/**
	 * Path to and name of config file.
	 */
	private String config;
	/**
	 * ConfigHandler to retrieve info from the config file.
	 */
	private ConfigHandler reader;
	/**
	 * InputHandler to generate dummy input.
	 */
	private InteractionHandler ih;
	/**
	 * List of all Applications, obtained from config file.
	 */
	private Application appsList[];
	/**
	 * The window id of the currently focused Application.
	 */
	private int currApp;		//index of currently focused app in appsList[]
	
	/**
	 * Searches for a window with <code>search</code> in its title.
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
	 * Gets the id of the currently focused application.
	 * @return The window id of the currently focused application.
	 */
	private native int getWindowFocus();			//returns id of currently focused window
	
	/*
	 * Load libs
	 */
	static{
		System.loadLibrary("xwindowtools");
	}
	/**
	 * Object for interacting with Applications.
	 * <p>
	 * Used for opening and controlling all Applications specified
	 * in config file.
	 * </p>
	 * @param config Path to config file.
	 * @see	  ConfigHandler
	 * @see   Application
	 */
	public ApplicationHandler(String config){
		osName = System.getProperty("os.name");
		this.config = config;
		try {
			reader = new ConfigHandler(config);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		appsList = new Application[reader.getAppNamesList().size()];
		initAppsList();
		
		try {
			ih = new InteractionHandler();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		return;
	}
	/**
	 * Opens the Application at index <code>index</code>.
	 * <p>
	 * If the specified Application is already open,
	 * it focuses it, otherwise it opens and focuses it.
	 * </p>
	 * @param index Index of Application to be opened.
	 * @see #open(String name)
	 */
	public void open(int index){
		if(!isOpen(index))
			appsList[index] = new Application(reader.getApplicationName(index), reader.getPoints(index));
		appsList[index].focus();
		currApp = index;
		return;
	}
	/**
	 * Opens application with name <code>name</code>.
	 * <p>
	 * Gets the index of the Application with name <code>name</code>
	 * and calls <code>open(int index)</code>.
	 * </p>
	 * @param name Name of Application to be opened.
	 * @see Application
	 * @see #open(int index)
	 */
	public void open(String name){
		open(reader.getApplicationIndex(name));
		return;
	}
	
	/**
	 * Focuses and raises the Application at index <code>index</code>.
	 * @param index Index of Application to focus.
	 */
	public void focus(int index){
		appsList[index].focus();
		currApp = index;
		return;
	}
	/**
	 * Focuses and raises the Application with name <code>name</code>.
	 * <p>
	 * Gets the index of the Application with name <code>name</code>
	 * and calls <code>focus(int index)</code>.
	 * @param name Name of Application to focus.
	 * 
	 */
	public void focus(String name){
		focus(reader.getApplicationIndex(name));
		return;
	}
	
	/**
	 * True if specified Application is open.
	 * @param index Index of Application to be checked.
	 * @return True if Application is open, false otherwise.
	 */
	public boolean isOpen(int index){
		if(appsList[index] != null)
			return true;
		else
			return false;
	}
	/**
	 * True if specified Application is open.
	 * @param name Name of Application to be checked.
	 * @return True if Application is open, false otherwise.
	 */
	public boolean isOpen(String name){
		return isOpen(reader.getApplicationIndex(name));
	}
	
	/**
	 * Closes the Application at the specified index.
	 * @param index Index of Application to close.
	 */
	public void close(int index){
		if(appsList[index] == null)
			return;
		appsList[index].close();
		appsList[index] = null;
		currApp = getCurrentApp(false);
		return;
	}
	/**
	 * Closes the Application with the specified name.
	 * @param name Name of Application to close.
	 */
	public void close(String name){
		close(reader.getApplicationIndex(name));
	}
	
	/**
	 * Returns true if an Application with name <code>name</code> is specified in the config file.
	 * @param name Name of Application to search for .
	 * @return True if Application exists.
	 */
	public boolean isApplication(String name){
		if(reader.getApplicationIndex(name) == -1)
			return false;
		return true;
	}
	/**
	 * Wrapper for the InteractionHandler method typeInArea.
	 * @param pointIndex Point to click on.
	 * @param t	Text to type.
	 * @param ret	True if enter/return should be pressed after t is typed.
	 * @see InteractionHandler
	 * @see InteractionHandler#typeInArea(java.awt.Point, String, boolean)
	 */
	public void typeInBoxI(int pointIndex, String t, boolean ret){
		ih.typeInArea(appsList[getCurrentApp(true)].getPoint(pointIndex), t, ret);
		return;
	}
	/**
	 * Wrapper for the InteractionHandler method typeInArea.
	 * @param pointName	Name of point to click on.
	 * @param t 	Text to type.
	 * @param ret	True if enter/return should be pressed after t is typed.
	 * @see InteractionHandler
	 * @see InteractionHandler#typeInArea(java.awt.Point, String, boolean)
	 */
	public void typeInBox(String pointName, String t, boolean ret){
		ih.typeInArea(appsList[getCurrentApp(true)].getPoint(pointName), t, ret);
		return;
	}
	/**
	 * Wrapper for the InteractionHandler method leftClick.
	 * @param pointIndex Point to click on.
	 * @see InteractionHandler
	 * @see InteractionHandler#leftClick(java.awt.Point)
	 */
	public void leftClickI(int pointIndex){
		ih.leftClick(appsList[getCurrentApp(true)].getPoint(pointIndex));
		return;
	}
	/**
	 * Wrapper for the InteractionHandler method leftClick.
	 * @param pointName Name of point to click on.
	 * @see InteractionHandler
	 * @see InteractionHandler#leftClick(java.awt.Point)
	 */
	public void leftClick(String pointName){
		ih.leftClick(appsList[getCurrentApp(true)].getPoint(pointName));
		return;
	}
	/**
	 * Wrapper for the InteractionHandler method rightClick.
	 * @param pointIndex Point to click on.
	 * @see InteractionHandler
	 * @see InteractionHandler#rightClick(java.awt.Point)
	 */
	public void rightClickI(int pointIndex){
		ih.rightClick(appsList[getCurrentApp(true)].getPoint(pointIndex));
		return;
	}
	/**
	 * Wrapper for the InteractionHandler method rightClick.
	 * @param pointName Name of point to click on.
	 * @see InteractionHandler
	 * @see InteractionHandler#rightClick(java.awt.Point)
	 */
	public void rightClick(String pointName){
		ih.rightClick(appsList[getCurrentApp(true)].getPoint(pointName));
		return;
	}
	/**
	 * Wrapper for the InteractionHandler method mouseWheelClick.
	 * @param pointIndex Point to click on.
	 * @see InteractionHandler
	 * @see InteractionHandler#mouseWheelClick(java.awt.Point)
	 */
	public void mouseWheelClickI(int pointIndex){
		ih.mouseWheelClick(appsList[getCurrentApp(true)].getPoint(pointIndex));
		return;
	}
	/**
	 * Wrapper for the InteractionHandler method mouseWheelClick.
	 * @param pointName Name of point to click on.
	 * @see InteractionHandler
	 * @see InteractionHandler#mouseWheelClick(java.awt.Point)
	 */
	public void mouseWheelClick(String pointName){
		ih.mouseWheelClick(appsList[getCurrentApp(true)].getPoint(pointName));
		return;
	}
	/**
	 * Wrapper for the InteractionHandler method pressUp.
	 * @see InteractionHandler
	 * @see InteractionHandler#pressUp()
	 */
	public void pressUp(){
		ih.pressUp();
		return;
	}
	/**
	 * Wrapper for the InteractionHandler method pressDown.
	 * @see InteractionHandler
	 * @see InteractionHandler#pressDown()
	 */
	public void pressDown(){
		ih.pressDown();
		return;
	}
	/**
	 * Wrapper for the InteractionHandler method pressLeft.
	 * @see InteractionHandler
	 * @see InteractionHandler#pressLeft()
	 */
	public void pressLeft(){
		ih.pressLeft();
		return;
	}
	/**
	 * Wrapper for the InteractionHandler method pressRight.
	 * @see InteractionHandler
	 * @see InteractionHandler#pressRight()
	 */
	public void pressRight(){
		ih.pressRight();
		return;
	}
	/**
	 * Gets the id of the currently focused application.
	 * <p>
	 * Gets the window id of the currently focused application.
	 * <code>check</code> should be true if the value retrieved is
	 * not expected to be different from {@link ApplicationHandler#currApp},
	 * that is, when no Application has been closed. If an {@link Application}
	 * has just been closed and {@link ApplicationHandler#currApp} needs to be
	 * updated then <code>check</code> should be false.
	 * Returns the index of the focused {@link Application} or -1 if it's 
	 * not specified in the config file.
	 * </p>
	 * @param check True if the value of {@link ApplicationHandler#currApp} is not expected to be different
	 * @return The index of the focused {@link Application} or -1 if it's not specified in config file
	 * @see Application
	 * @see ApplicationHandler
	 * @see ApplicationHandler#currApp
	 */
	private int getCurrentApp(boolean check){
		int currID = getWindowFocus();
		
		if(check && currID != currApp)
			focus(currApp);
		
		for(int i = 0; i < appsList.length; i++){
			if(appsList[i] != null)
				if(currID == appsList[i].getId())
				return i;
		}
		return -1;
	}
	/**
	 * Initialises all objects in {@link #appsList} to <code>null</code>.
	 */
	private void initAppsList(){
		for(int i = 0; i < appsList.length; i++){
			//if(windowSearch(reader.getAppNamesList().get(i)) == -1)
				appsList[i] = null;
			//else
			//	appsList[i] = new Application(reader.getApplicationName(i), reader.getPoints(i));
		}
		return;
	}
}