package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import model.TwitterFeed;

public class FeedBanner {

	private Font tweetFont;

	private ArrayList<String> tweets = new ArrayList<String>();

	private TwitterFeed tf;

	private float leftOffset = 0;

	private int imgNum = 0;

	private long lastPaint;

	private int h;
	private int w;
	private float speed;

	public FeedBanner(TwitterFeed tf, int h, int w, float speed) {
		this.h = h;
		this.w = w;
		this.tf = tf;
		this.speed = speed;

		selectFont();

	}

	public void selectFont() {

		int[] styles = new int[] { Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC };
		// Font info is obtained from the current graphics environment.
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		// --- Get an array of font names (smaller than the number of fonts)
		String[] fontNames = ge.getAvailableFontFamilyNames();

		// String[] fontNames = new String[] { "Arial Black", "SansSerif",
		// "Monospaced" };

		String fontName = "Monospaced";// fontNames[(int) (Math.random() *
										// fontNames.length)];

		int style = styles[(int) (Math.random() * 3)];

		System.out.println(fontName);

		tweetFont = new Font(fontName, style, h);

		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = img.createGraphics();
		FontMetrics fm = g2.getFontMetrics(tweetFont);

		int x = 0;

		while (fm.getHeight() <= h) {
			x++;
			tweetFont = new Font(fontName, style, h + x);
			fm = g2.getFontMetrics(tweetFont);
		}

		x = 0;

		while (fm.getHeight() > h) {
			x++;
			tweetFont = new Font(fontName, style, h - x);
			fm = g2.getFontMetrics(tweetFont);
		}

	}

	public void drawOnCanvas(Graphics2D g2, int yPos) {

		// g2.setColor(Color.WHITE);
		// g2.clearRect(0, 9, w, h);

		g2.setColor(Color.BLACK);
		g2.setFont(tweetFont);

		FontMetrics fm = g2.getFontMetrics();

		int bannerLength = (int) -leftOffset;

		// make sure you have a full row of text
		int t = 0;
		while (bannerLength < w) {
			if (t < tweets.size()) {
				bannerLength += fm.stringWidth(tweets.get(t));
			} else {
				String newTweet = tf.getNewTweet();
				if (newTweet == null) {
					break;
				} else {
					tweets.add(newTweet);
					bannerLength += fm.stringWidth(newTweet);
				}
			}

			t++;
		}

		float pos = -leftOffset;
		for (String tweet : tweets) {

			// System.out.println("drawing \"" + tweet + "\" at pos= " + pos
			// +
			// " yPos= " + yPos);
			g2.drawString(tweet, pos, yPos);
			pos += fm.stringWidth(tweet);
		}

		leftOffset += speed; // * 1000.0 / (System.currentTimeMillis() -
								// lastPaint);

		if (!tweets.isEmpty()) {
			String headTweet = tweets.get(0);
			if (leftOffset >= fm.stringWidth(headTweet)) {
				tweets.remove(0);
				leftOffset = 0;
			}

		} else {
			leftOffset = ((float) w) / 2.0f;
		}

	}

	public int getH() {
		return h;
	}

	public void setWH(int w, int h) {
		this.h = h;
		this.w = w;
		selectFont();
	}

	public int getW() {
		return w;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
