package com.weibo;

public class ImageAndText {

	private String imageUrl;
	private String text;
	private String letter;

	public ImageAndText(String imageUrl, String text, String letter) {
		// TODO Auto-generated constructor stub
		this.setImageUrl(imageUrl);
		this.setText(text);
		this.setLetter(letter);
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	/**
	 * @return the letter
	 */
	public String getLetter() {
		return letter;
	}

	/**
	 * @param letter the letter to set
	 */
	public void setLetter(String letter) {
		this.letter = letter;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
