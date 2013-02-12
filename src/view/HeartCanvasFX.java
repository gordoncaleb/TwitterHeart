package view;

import java.util.ArrayList;
import java.util.Vector;

import model.TwitterFeed;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class HeartCanvasFX extends Pane {
	private final AnimationTimer timer;
	private final Canvas canvas;
	private final ImageView background;
	private final Image decalImage;

	public TwitterFeed tf = new TwitterFeed();

	public Vector<FeedBannerFX> banners = new Vector<FeedBannerFX>();

	public static int vShift = 50;

	public int[] yPos;

	public HeartCanvasFX() {

		// create canvas

		canvas = new Canvas(500, 500);

		// canvas.setBlendMode(BlendMode.ADD);

		// canvas.setEffect(new Reflection(0,0.4,0.15,0));

		decalImage = new Image("/view/heartdecal.png", 500, 500, false, false);

		background = new ImageView(decalImage);

		getChildren().addAll(canvas, background);

		// create animation timer that will be called every frame

		// final AnimationTimer timer = new AnimationTimer() {

		initBanners();

		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {

				GraphicsContext gc = canvas.getGraphicsContext2D();

				// clear area with transparent black

				gc.setFill(Color.WHITE);

				gc.fillRect(0, 0, canvas.getWidth(), canvas.getWidth());

				// draw fireworks

				// drawFireworks(gc);

				drawBanners(gc);

			}

		};

	}

	public int[] randomOrder(int max) {

		ArrayList<Integer> ordered = new ArrayList<Integer>();

		for (int i = 0; i < max; i++) {
			ordered.add(i);
		}

		int[] rand = new int[max];

		int sel;
		for (int i = 0; i < max; i++) {
			sel = (int) (Math.random() * (double) ordered.size());
			rand[i] = ordered.get(sel);
			ordered.remove(sel);
		}

		return rand;

	}

	public void drawBanners(GraphicsContext gc) {

		int[] rand = randomOrder(yPos.length);
		FeedBannerFX banner;

		for (int i = 0; i < yPos.length; i++) {
			banner = banners.get(rand[i]);
			banner.drawOnCanvas(gc, yPos[rand[i]]);
		}
	}

	public void initBanners() {
		double h = 32;

		ArrayList<Integer> rowHeights = new ArrayList<Integer>();

		int sum = 0;
		int rh;
		while (sum < (decalImage.getHeight() - vShift)) {
			rh = (int) Math.max(16, Math.random() * h);
			sum += rh;
			rowHeights.add(rh);
		}

		int numRows = rowHeights.size();
		yPos = new int[numRows];

		int y = vShift;
		for (int i = 0; i < numRows; i++) {
			y += rowHeights.get(i);
			banners.add(new FeedBannerFX(tf, rowHeights.get(i), (int) decalImage.getWidth(), (float) Math.max(Math.random() / 2.0, .2)));
			yPos[i] = y;
		}
	}

	public void start() {
		timer.start();
	}

	public void stop() {
		timer.stop();
	}

}
