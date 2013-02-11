package view;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import model.TwitterFeed;

public class FeedBanner {

	private Font tweetFont;

	private ArrayList<String> tweets = new ArrayList<String>();

	private TwitterFeed tf;

	private float leftOffset = 0;

	private long lastPaint;

	private int h;
	private float speed;

	public FeedBanner(TwitterFeed tf, int h, float speed) {
		this.h = h;
		this.tf = tf;
		this.speed = speed;

		int[] styles = new int[] { Font.BOLD, Font.ITALIC, Font.PLAIN };

		tweetFont = new Font("Monospaced", styles[(int) (Math.random() * 3)], h);
	}

	public void drawBanner(Graphics2D g2, int yPos, int width) {

		g2.setFont(tweetFont);

		FontMetrics fm = g2.getFontMetrics();

		int bannerLength = (int) -leftOffset;

		// make sure you have a full row of text
		int t = 0;
		while (bannerLength < width) {
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

			// System.out.println("drawing \"" + tweet + "\" at pos= " + pos +
			// " yPos= " + yPos);
			g2.drawString(tweet, pos, (float) yPos);
			pos += fm.stringWidth(tweet);
		}

		leftOffset += speed; //* 1000.0 / (System.currentTimeMillis() - lastPaint);

		if (!tweets.isEmpty()) {
			String headTweet = tweets.get(0);
			if (leftOffset >= fm.stringWidth(headTweet)) {
				tweets.remove(0);
				leftOffset = 0;
			}

		} else {
			leftOffset = ((float) width) / 2.0f;
		}

		lastPaint = System.currentTimeMillis();

	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;

		tweetFont = new Font("Monospaced", tweetFont.getStyle(), h);
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
