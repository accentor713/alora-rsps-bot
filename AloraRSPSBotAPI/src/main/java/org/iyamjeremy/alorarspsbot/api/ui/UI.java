package org.iyamjeremy.alorarspsbot.api.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class UI {

	private static List<UIElement> uiElements = new ArrayList<>();

	public static void draw() {
		synchronized (uiElements) {
			for (UIElement element : uiElements) {
				if (element.isTopLevelElement()) {
					element.draw();
				}
			}
		}
	}

	public static boolean onMousePressed(int x, int y) {
		synchronized (uiElements) {
			ListIterator<UIElement> li = uiElements.listIterator(uiElements.size());

			while(li.hasPrevious()) {
				UIElement element = li.previous();
				if (element.contains(x, y)) {
					element.onMousePressed(x, y);
					return true;
				}
			}
		}

		return false;
	}

	public static void onMouseReleased(int x, int y) {
		synchronized (uiElements) {
			for (UIElement element : uiElements) {
				element.onMouseReleased(x, y);
			}
		}
	}

	public static boolean onMouseMoved(int x, int y) {
		boolean mousedOver = false;
		synchronized (uiElements) {
			ListIterator<UIElement> li = uiElements.listIterator(uiElements.size());

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
		}

		return mousedOver;
	}

	public static void add(UIElement element) {
		synchronized (uiElements) {
			uiElements.add(element);
			for (UIElement child : element.getChildren()) {
				add(child);
			}
		}
	}

	public static void remove(UIElement element) {
		synchronized (uiElements) {
			uiElements.remove(element);
			for (UIElement child : element.getChildren()) {
				remove(child);
			}
		}
	}

}
