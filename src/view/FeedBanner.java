package view;

import java.awt.Font;
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

		if (!tweets.isEmpty()) {

			tweets.add(tf.getNewTweet());
		}

		String tweet = tweets.get(0);

	}

	public int getH() {
		return h;
	}

}
