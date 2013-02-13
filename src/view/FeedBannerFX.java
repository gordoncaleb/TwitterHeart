package view;

import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;

import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import model.TwitterFeed;

public class FeedBannerFX extends Pane {

	private TwitterFeed tf;
	private ArrayList<String> tweets = new ArrayList<String>();

	private Font tweetFont;

	private FontMetrics fm;

	private Vector<TweetFX> tweetfxs = new Vector<TweetFX>();

	private float leftOffset = 0;

	private double h, w;
	private double speed, yPos;

	public FeedBannerFX(TwitterFeed tf, double h, double w, double yPos, double speed) {
		this.h = h;
		this.w = w;
		this.tf = tf;
		this.speed = speed;
		this.yPos = yPos;

		selectFont();

		TweetFX tweet = new TweetFX(this);
		tweet.setTweet(tf.getNewTweet());

		tweetfxs.add(tweet);
		this.getChildren().add(tweet);

		tweet.doScroll();

		// TranslateTransition trans1 = new
		// TranslateTransition(Duration.seconds(tweet.getWidth() / speed),
		// tweetText);
		// trans1.setFromX(w);
		// trans1.setToX(w-tweet.getWidth());
		// trans1.setInterpolator(Interpolator.LINEAR);
		//
		// trans1.setOnFinished(new EventHandler<ActionEvent>() {
		//
		// @Override
		// public void handle(ActionEvent arg0) {
		//
		// }
		//
		// });

	}

	public void queueNewTweetAnimation() {
		TweetFX nextTweetFx = null;

		for (TweetFX tweetfx : tweetfxs) {
			if (!tweetfx.isRunning()) {
				nextTweetFx = tweetfx;
			}
		}

		if (nextTweetFx == null) {
			nextTweetFx = new TweetFX(this);
			tweetfxs.add(nextTweetFx);
			this.getChildren().add(nextTweetFx);

			System.out.println("TweetFX vectorsize = " + tweetfxs.size());
		}

		nextTweetFx.setTweet(tf.getNewTweet());

		nextTweetFx.doScroll();
	}

	// public TweetFX getFullLine() {
	//
	// StringBuilder sb = new StringBuilder();
	//
	// int lw = 0;
	//
	// while (lw < w) {
	// sb.append(tf.getNewTweet());
	// lw = fm.stringWidth(sb.toString());
	// }
	//
	// return new TweetFX(sb.toString(), lw);
	//
	// }

	public void selectFont() {

		// int[] styles = new int[] { Font.BOLD, Font.ITALIC, Font.BOLD |
		// Font.ITALIC };
		// Font info is obtained from the current graphics environment.
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		// --- Get an array of font names (smaller than the number of fonts)
		String[] fontNames = ge.getAvailableFontFamilyNames();

		// String[] fontNames = new String[] { "Arial Black", "SansSerif",
		// "Monospaced" };

		// String fontName = fontNames[(int) (Math.random() *
		// fontNames.length)];

		String fontName = "Monospaced";
		// int style = styles[(int) (Math.random() * 3)];

		System.out.println(fontName);

		tweetFont = new Font(fontName, h);

		java.awt.Font awtTweetFont = new java.awt.Font(fontName, java.awt.Font.PLAIN, (int) h);

		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		fm = img.createGraphics().getFontMetrics(awtTweetFont);

		// int x = 0;
		//
		// while (fm.getHeight() > h) {
		// x++;
		// tweetFont = new Font(fontName, style, h - x);
		// fm = g2.getFontMetrics(tweetFont);
		// }

	}

	// public void drawOnCanvas(GraphicsContext gc, int yPos) {
	//
	// // g2.setColor(Color.WHITE);
	// // g2.clearRect(0, 9, w, h);
	//
	// // gc.setColor(Color.BLACK);
	// gc.setFont(tweetFont);
	// gc.setStroke(Color.BLACK);
	//
	// // FontMetrics fm = g2.getFontMetrics();
	//
	// int bannerLength = (int) -leftOffset;
	//
	// // make sure you have a full row of text
	// int t = 0;
	// while (bannerLength < w) {
	// if (t < tweets.size()) {
	// bannerLength += fm.stringWidth(tweets.get(t));
	// } else {
	// String newTweet = tf.getNewTweet();
	// if (newTweet == null) {
	// break;
	// } else {
	// tweets.add(newTweet);
	// bannerLength += fm.stringWidth(newTweet);
	// }
	// }
	//
	// t++;
	// }
	//
	// float pos = -leftOffset;
	// for (String tweet : tweets) {
	//
	// // System.out.println("drawing \"" + tweet + "\" at pos= " + pos +
	// // " yPos= " + yPos);
	// gc.strokeText(tweet, pos, yPos);
	// pos += fm.stringWidth(tweet);
	// }
	//
	// leftOffset += speed; // * 1000.0 / (System.currentTimeMillis() -
	// // lastPaint);
	//
	// if (!tweets.isEmpty()) {
	// String headTweet = tweets.get(0);
	// if (leftOffset >= fm.stringWidth(headTweet)) {
	// tweets.remove(0);
	// leftOffset = 0;
	// }
	//
	// } else {
	// leftOffset = ((float) w) / 2.0f;
	// }
	//
	// }

	public double getH() {
		return h;
	}

	public void setWH(double w, double h) {
		this.h = h;
		this.w = w;
		selectFont();
	}

	public double getW() {
		return w;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public double getSpeed() {
		return speed;
	}

	public double getYPos() {
		return yPos;
	}

	public Font getFont() {
		return tweetFont;
	}

	public FontMetrics getFontMetrics() {
		return fm;
	}

}
