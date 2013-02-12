package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import model.TwitterFeed;

public class FeedBanner {

	private Font tweetFont;

	private ArrayList<String> tweets = new ArrayList<String>();

	private TwitterFeed tf;

	private float leftOffset = 0;

	private BufferedImage[] images = new BufferedImage[2];
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

		int[] styles = new int[] { Font.BOLD, Font.ITALIC, Font.PLAIN };

		tweetFont = new Font("Monospaced", styles[(int) (Math.random() * 3)], h);

		images[0] = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		images[1] = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);

	}

	public void renderLine() {

		// images[imgNum] = new BufferedImage(w, h,
		// BufferedImage.TYPE_4BYTE_ABGR);
		BufferedImage img = images[imgNum];

		synchronized (img) {
			Graphics2D g2 = img.createGraphics();

			// g2.setColor(Color.WHITE);
			g2.clearRect(0, 9, w, h);

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
				g2.drawString(tweet, pos, h);
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

			imgNum ^= 1;

		}
	}

	public void drawOnCanvas(Graphics2D g2, int yPos) {

		BufferedImage img = images[imgNum ^ 1];

		synchronized (img) {
			g2.drawImage(img, 0, yPos, null);
		}

	}

	public int getH() {
		return h;
	}

	public void setWH(int w, int h) {
		this.h = h;
		this.w = w;

		tweetFont = new Font("Monospaced", tweetFont.getStyle(), h);

		images[0] = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		images[1] = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
	}

	public int getW() {
		return w;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
