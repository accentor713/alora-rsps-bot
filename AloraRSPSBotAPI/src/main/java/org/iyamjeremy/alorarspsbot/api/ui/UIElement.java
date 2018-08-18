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
		UI.remove(element);
	}
	
	public List<UIElement> getChildren() {
		return children;
	}
	
	public void setParent(UIElement parent) {
		this.parent = parent;
	}
	
	public int getAbsoluteX() {
		return ((this.parent != null) ? this.parent.getAbsoluteX() : 0) + this.x;
	}
	
	public int getAbsoluteY() {
		return ((this.parent != null) ? this.parent.getAbsoluteY() : 0) + this.y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean isMouseDown() {
		return mouseDown;
	}
	
	public boolean isMouseOver() {
		return mouseOver;
	}
	
	public boolean contains(int x, int y) {
		int thisX = this.getAbsoluteX();
		int thisY = this.getAbsoluteY();
		return (thisX <= x && thisY <= y &&
				x <= thisX + getWidth() && y <= thisY + getHeight());
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

	public boolean isTopLevelElement() {
		return parent == null;
	}

}
