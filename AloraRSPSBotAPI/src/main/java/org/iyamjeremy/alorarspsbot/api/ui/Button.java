package org.iyamjeremy.alorarspsbot.api.ui;


public class Button extends UIElement {
	
	private String text;
	private Runnable clickHandler;
	private int width;
	
	public Button(String text, Runnable clickHandler, int width) {
		this.text = text;
		this.clickHandler = clickHandler;
		this.width = width;
		this.setDimensions(width, 20);
	}

	@Override
	protected void drawSelf() {
		UIUtil.fillRect(0, 0, width, 20, 0x000000, 255);
		int color = 0x9D7E4B;
		if (isMouseDown()) {
			color = 0x5E4213;
		}
		else if (isMouseOver()) {
			color = 0x80612D;
		}
		UIUtil.fillRect(2, 2, width-4, 16, color, 255);
		UIUtil.drawText(text, 4, 14);
	}

	@Override
	public void onMousePressed(int x, int y) {
		super.onMousePressed(x, y);
		this.clickHandler.run();
	}

}
