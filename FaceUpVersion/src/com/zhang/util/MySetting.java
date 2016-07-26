package com.zhang.util;

import android.graphics.drawable.Drawable;


public class MySetting {
	private Drawable src;
	private String text;
	
	public MySetting(){
		
	}
	
	public MySetting(Drawable src, String text){
		this.src = src;
		this.text = text;
	}

	public Drawable getSrc() {
		return src;
	}

	public void setSrc(Drawable src) {
		this.src = src;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
