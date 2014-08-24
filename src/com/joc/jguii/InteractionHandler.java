package com.joc.jguii;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Used for generating dummy input.
 */
public class InteractionHandler extends Robot{

	public InteractionHandler() throws AWTException{
		super();
	}
	
	/**
	 * Type the given string
	 * <p>
	 * Types the given string. If <code>ret</code> is true, enter/return will be pressed
	 * after the string is typed.
	 * </p>
	 * @param t String to type
	 * @param ret If true enter/return will be pressed
	 */
	public void type(String t, boolean ret){
		for(int i = 0; i < t.length(); i++){
			keyPress(KeyEvent.getExtendedKeyCodeForChar((int)t.charAt(i)));
			keyRelease(KeyEvent.getExtendedKeyCodeForChar((int)t.charAt(i)));
		}
		if(ret){
			keyPress(KeyEvent.VK_ENTER);
			keyRelease(KeyEvent.VK_ENTER);
		}
		return;
	}
	/**
	 * Clicks on a point then types to allow selection of text areas followed by text input
	 * <p>
	 * Clicks the given point, types the given string and, if ret is true, then presses enter/return
	 * @param p Point to click on
	 * @param t String to type
	 * @param ret If true enter/return will be pressed
	 */
	public void typeInArea(Point p, String t, boolean ret){
		leftClick(p);
		type(t, ret);
		return;
	}
	
	/**
	 * Presses and releases the up key.
	 */
	public void pressUp(){
		keyPress(KeyEvent.VK_UP);
		keyRelease(KeyEvent.VK_UP);
		return;
	}
	/**
	 * Presses and releases the down key.
	 */
	public void pressDown(){
		keyPress(KeyEvent.VK_DOWN);
		keyRelease(KeyEvent.VK_DOWN);
		return;
	}
	/**
	 * Presses and releases the left key.
	 */
	public void pressLeft(){
		keyPress(KeyEvent.VK_LEFT);
		keyRelease(KeyEvent.VK_LEFT);
		return;
	}
	/**
	 * Presses and releases the right key.
	 */
	public void pressRight(){
		keyPress(KeyEvent.VK_RIGHT);
		keyPress(KeyEvent.VK_RIGHT);
		return;
	}
	/**
	 * Repositions the mouse.
	 * <p>
	 * Repositions the mouse at the absolute coordinates <code>(x, y)</code>.
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public void moveMouseTo(int x, int y){
		mouseMove(x, y);
		return;
	}
	/**
	 * Repostions the mouse.
	 * <p>
	 * Repositions the mouse at the absolute coordinates <code>(p.x, p.y)</code>.
	 * @param p The x and y coordinates
	 */
	public void moveMouseTo(Point p){
		mouseMove(p.x,  p.y);
		return;
	}
	/**
	 * Gets the absolute position of the mouse.
	 * @return The absolute position of the mouse
	 */
	public Point getMousePos(){
		return MouseInfo.getPointerInfo().getLocation();
	}
	
	/**
	 * Presses and releases the specified mouse button
	 * <p>
	 * Presses and releases the specified mouse button. Requires that button masks be used.
	 * For example, InputEvent.BUTTON1_DOWN_MASK
	 * </p>
	 * @param button The button to be clicked
	 * @see InputEvent
	 */
	private void click(int button){
		mousePress(button);
		mouseRelease(button);
		return;
	}
	
	/**
	 * Clicks the left mouse button at the point p
	 * <p>
	 * Moves the mouse to p and clicks the left mouse button (InputEvent.BUTTON1_DOWN_MASK) 
	 * </p>
	 * @param p Point to click
	 */
	public void leftClick(Point p){
		moveMouseTo(p);
		click(InputEvent.BUTTON1_DOWN_MASK);
	}
	/**
	 * Clicks the right mouse button at the point p
	 * <p>
	 * Moves the mouse to p and clicks the right mouse button (InputEvent.BUTTON2_DONW_MASK)
	 * </p>
	 * @param p Point to click
	 */
	public void rightClick(Point p){
		moveMouseTo(p);
		click(InputEvent.BUTTON2_DOWN_MASK);
	}
	
	/**
	 * Clicks the mouse wheel at the point p
	 * <p>
	 * Moves the mouse to p and clicks the mouse wheel (InputEvent.BUTTON3_DOWN_MASK)
	 * </p>
	 * @param p Point to click
	 */
	public void mouseWheelClick(Point p){
		moveMouseTo(p);
		click(InputEvent.BUTTON3_DOWN_MASK);
	}
}
