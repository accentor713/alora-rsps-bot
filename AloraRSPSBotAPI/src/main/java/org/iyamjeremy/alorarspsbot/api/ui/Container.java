package org.iyamjeremy.alorarspsbot.api.ui;

public class Container extends UIElement {
	
	private boolean hasBorder;
	
	public Container(int width, int height) {
		this(width, height, false);
	}
	
	public Container(int width, int height, boolean hasBorder) {
		this.hasBorder = hasBorder;
		this.setDimensions(width, height);
	}

	@Override
	protected void drawSelf() {
		if (hasBorder) {
			UIUtil.fillRect(0, 0, getWidth(), getHeight(), 0, 255);
			UIUtil.fillRect(2, 2, getWidth()-4, getHeight()-4, 0x9D7E4B, 255);
		}
		else {
			UIUtil.fillRect(0, 0, getWidth(), getHeight(), 0x9D7E4B, 255);
		}
	}

}
