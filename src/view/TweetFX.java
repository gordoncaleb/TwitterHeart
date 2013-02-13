package view;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TweetFX extends Text {

	Timeline timeline;

	private String tweet;
	private double width;

	private int phase = 0;

	private FeedBannerFX banner;

	public TweetFX(FeedBannerFX banner) {
		this.banner = banner;
		this.setY(banner.getYPos());
		this.setX(banner.getW());
	}

	public void doScroll() {
		this.setText(tweet);
		this.setFont(banner.getFont());
		this.setY(banner.getYPos());

		System.out.println("Scrolling :" + tweet);

		phase = 0;

		timeline = new Timeline();

		timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(this.translateXProperty(), banner.getW(), Interpolator.LINEAR)),
				new KeyFrame(Duration.seconds(width / banner.getSpeed()), new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						// txt fully visable

						if (phase < 1) {
							System.out.println("phase 1");

							phase = 1;
							banner.queueNewTweetAnimation();
						}

					}
				}, new KeyValue(this.translateXProperty(), banner.getW() - width, Interpolator.LINEAR)),
				new KeyFrame(Duration.seconds(banner.getW() / banner.getSpeed()), new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						// txt scroll done

						if (phase < 2) {
							System.out.println("phase 2");

							phase = 2;

							timeline.stop();
						}

					}
				}, new KeyValue(this.translateXProperty(), -width, Interpolator.LINEAR)));

		// timeline.playFromStart();
		timeline.play();
	}

	public boolean isRunning() {
		// System.out.println(timeline.getStatus());

		return (timeline.getStatus() == Animation.Status.RUNNING);
	}

	public int getPhase() {
		return phase;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {

		if (tweet == null) {
			tweet = "No TWEET";
		}
		
		this.setX(banner.getW());

		this.tweet = tweet;
		this.width = banner.getFontMetrics().stringWidth(tweet);
	}

	public double getWidth() {
		return width;
	}

}
