package com.joc.jguii;

import java.awt.Point;

/**
 * Stores a {@link Point} and a {@link String}.
 */
public class NamedPoint extends Point{
	/**
	 * Name of this point.
	 */
	private String name;
	public NamedPoint(int x, int y, String name){
		super(x, y);
		this.name = name;
	}
	
	/**
	 * Gets the name of this point.
	 * @return The name of this point.
	 */
	public String getName(){
		return name;
	}
	/**
	 * Sets the name of this point.
	 * @param name The new name.
	 */
	public void setName(String name){
		this.name = name;
	}
}
