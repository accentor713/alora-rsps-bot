package org.iyamjeremy.alorarspsbot.api.ui;

import java.util.function.Consumer;

public class Checkbox extends UIElement {
	
	private String label;
	private boolean isChecked = false;
	private Consumer<Boolean> onchange;
	
	public Checkbox(String label, boolean isChecked, Consumer<Boolean> onchange) {
		this.label = label;
		this.isChecked = isChecked;
		this.onchange = onchange;
		this.setDimensions(20, 20);
	}
	
	public boolean isChecked() {
		return isChecked;
	}

	@Override
	protected void drawSelf() {
		UIUtil.fillRect(0, 0, 20, 20, 0x000000, 255);
		int color;
		
		if (isChecked) {
			color = 0x00CC00;
			if (isMouseDown()) {
				color = 0x005500;
			}
			else if (isMouseOver()) {
				color = 0x009900;
			}
		}
		else {
			color = 0xCC0000;
			if (isMouseDown()) {
				color = 0x550000;
			}
			else if (isMouseOver()) {
				color = 0x990000;
			}
		}
		UIUtil.fillRect(2, 2, 20-4, 20-4, color, 255);
		
		UIUtil.drawText(label, 24, 14);
	}

	@Override
	public void onMousePressed(int x, int y) {
		super.onMousePressed(x, y);
		isChecked = !isChecked;
		onchange.accept(isChecked);
	}

}
