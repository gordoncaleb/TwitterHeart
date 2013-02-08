package view;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import model.TwitterFeed;

public class FeedBanner {

	private Font tweetFont = new Font("Monospaced", Font.BOLD, 18);

	private ArrayList<String> tweets = new ArrayList<String>();

	private TwitterFeed tf;

	private double leftOffset = 0;

	private int h;
	private double speed;

	public FeedBanner(TwitterFeed tf, int h, double speed) {
		this.h = h;
		this.tf = tf;
		this.speed = speed;

		tweetFont = new Font("Monospaced", Font.BOLD, h);
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
		}

		int pos = (int) -leftOffset;
		for (String tweet : tweets) {
			g2.drawString(tweet, pos, yPos);
			pos += fm.stringWidth(tweet);
		}

		leftOffset += speed;

		if (!tweets.isEmpty()) {
			String headTweet = tweets.get(0);
			if (leftOffset >= fm.stringWidth(headTweet)) {
				tweets.remove(0);
			}

		}

	}

	public int getH() {
		return h;
	}

}
