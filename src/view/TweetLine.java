package view;

public class TweetLine {

	private String line;
	private int width;

	public TweetLine(String line, int width) {
		this.line = line;
		this.width = width;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
