package org.iyamjeremy.alorarspsbot.api.ui;


public class Button extends UIElement {
	
	private String text;
	private Runnable clickHandler;
	
	public Button(String text, Runnable clickHandler, int width) {
		this.text = text;
		this.clickHandler = clickHandler;
		this.setDimensions(width, 20);
	}
	
	public void setText(String s) {
		this.text = s;
	}

	@Override
	protected void drawSelf() {
		UIUtil.fillRect(0, 0, getWidth(), getHeight(), 0x000000, 255);
		int color = 0x9D7E4B;
		if (isMouseDown()) {
			color = 0x5E4213;
		}
		else if (isMouseOver()) {
			color = 0x80612D;
		}
		UIUtil.fillRect(2, 2, getWidth()-4, getHeight()-4, color, 255);
		UIUtil.drawText(text, 4, 14);
	}

	@Override
	public void onMousePressed(int x, int y) {
		super.onMousePressed(x, y);
		this.clickHandler.run();
	}

}
