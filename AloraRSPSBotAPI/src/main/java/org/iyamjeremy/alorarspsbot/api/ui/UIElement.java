package org.iyamjeremy.alorarspsbot.api.ui;

import java.util.ArrayList;
import java.util.List;

public abstract class UIElement {
	
	private List<UIElement> children = new ArrayList<>();
	private int x, y;
	private int width, height;
	private UIElement parent = null;
	private boolean mouseDown = false;
	private boolean mouseOver = false;
	
	public void addChild(UIElement element) {
		element.setParent(this);
		this.children.add(element);
	}
	
	public void removeChild(UIElement element) {
		element.setParent(null);
		this.children.remove(element);
	}
	
	public void setParent(UIElement parent) {
		this.parent = parent;
	}
	
	public int getX() {
		return ((this.parent != null) ? this.parent.getX() : 0) + this.x;
	}
	
	public int getY() {
		return ((this.parent != null) ? this.parent.getX() : 0) + this.y;
	}
	
	public boolean isMouseDown() {
		return mouseDown;
	}
	
	public boolean isMouseOver() {
		return mouseOver;
	}
	
	public boolean contains(int x, int y) {
		int thisX = this.getX();
		int thisY = this.getY();
		return (thisX <= x && thisY <= y &&
				x <= thisX + width && y <= thisY + height);
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setDimensions(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	public void onMousePressed(int x, int y) {
		this.mouseDown = true;
	}
	
	public void onMouseReleased(int x, int y) {
		this.mouseDown = false;
	}
	
	public void onMouseOver() {
		this.mouseOver = true;
	}
	
	public void onMouseOut() {
		this.mouseOver = false;
	}
	
	protected abstract void drawSelf();
	
	public final void draw() {
		UIUtil.translateBy(this.x, this.y);
		this.drawSelf();	
		for (UIElement child : children) {
			child.draw();
		}
		UIUtil.translateBy(-this.x, -this.y);
	}

}
