package org.iyamjeremy.alorarspsbot.api.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class UI {
	
	private static List<UIElement> uiElements = new ArrayList<>();
	
	public static void draw() {
		for (UIElement element : uiElements) {
			element.draw();
		}
	}
	
	public static boolean onMousePressed(int x, int y) {
		ListIterator<UIElement> li = uiElements.listIterator(uiElements.size());

		while(li.hasPrevious()) {
			UIElement element = li.previous();
			if (element.contains(x, y)) {
				element.onMousePressed(x - element.getX(), y - element.getY());
				return true;
			}
		}
		
		return false;
	}
	
	public static void onMouseReleased(int x, int y) {
		for (UIElement element : uiElements) {
			element.onMouseReleased(x, y);
		}
	}
	
	public static boolean onMouseMoved(int x, int y) {
		ListIterator<UIElement> li = uiElements.listIterator(uiElements.size());

		boolean mousedOver = false;
		while(li.hasPrevious()) {
			UIElement element = li.previous();
			if (element.contains(x, y)) {
				element.onMouseOver();
				mousedOver = true;
			}
			else {
				element.onMouseOut();
			}
		}
		
		return mousedOver;
	}
	
	public static void add(UIElement element) {
		uiElements.add(element);
	}
	
	public static void remove(UIElement element) {
		uiElements.remove(element);
	}

}
