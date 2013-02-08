package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import model.TwitterFeed;

public class HeartCanvas extends JLabel implements ActionListener {

	public ArrayList<FeedBanner> banners = new ArrayList<FeedBanner>();

	public BufferedImage decalImage;
	public TwitterFeed tf;

	public Color heartColor;

	public int width;
	public int height;

	private Timer timer;

	public HeartCanvas() {

		tf = new TwitterFeed();

		decalImage = getImage("heartdecal.png", 500, 500);

		width = decalImage.getWidth();
		height = decalImage.getHeight();

		System.out.println("Image Size =" + width + "," + height);

		this.setPreferredSize(new Dimension(width, height));

		int h = 18;

		int numRows = (int) ((double) height / (double) h);

		for (int i = 0; i < numRows; i++) {
			banners.add(new FeedBanner(tf, h, 1));
		}

		Color baseColor = Color.RED;
		heartColor = new Color(baseColor.getRed() / 255.0f, baseColor.getGreen() / 255.0f, baseColor.getBlue() / 255.0f, 0.5f);

		timer = new Timer(10, this);
		timer.start();

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		final HeartCanvas canvas = new HeartCanvas();

		frame.add(canvas, BorderLayout.CENTER);

		JButton paint = new JButton("paint");

		paint.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				canvas.repaint();

			}

		});

		// frame.add(paint, BorderLayout.SOUTH);

		// frame.pack();

		frame.setSize(500, 500);

		frame.setVisible(true);

		frame.setResizable(false);

	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		int yPos = 0;
		for (FeedBanner banner : banners) {
			yPos += banner.getH();
			banner.drawBanner(g2, yPos, width);
		}

		g2.setColor(heartColor);
		g2.fillRect(0, 0, width, height);

		if (decalImage != null) {
			g2.drawImage(decalImage, 0, 0, null);
		}
	}

	private void paintTweet(Graphics2D g2, int[] rowPix, int rowHeight, String tweet, int tweetLength) {

		int lengthDrawn = rowPix[1];

		while ((tweetLength - lengthDrawn) >= 0) {
			g2.drawString(tweet, -lengthDrawn, rowHeight * (rowPix[0] + 1));

			lengthDrawn += width;

			if (tweetLength >= lengthDrawn) {
				rowPix[0]++;
			}
		}

		rowPix[1] = -(width - (lengthDrawn - tweetLength));

	}

	public static BufferedImage getImage(String fileName, int w, int h) {

		java.net.URL imgURL = HeartCanvas.class.getResource(fileName);

		if (imgURL == null) {
			System.out.println("Could not load Image for " + fileName);
			return null;
		}

		BufferedImage newImg = null;

		try {
			newImg = ImageIO.read(imgURL);
		} catch (IOException e) {
			System.out.println("Could not load Image for " + fileName);
			return null;
		}

		if (w > 0 && h > 0 && (w != newImg.getWidth() || h != newImg.getHeight())) {
			newImg = resize(newImg, w, h);
		}

		return newImg;

	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {

		if (img == null || newW <= 0 || newH <= 0) {
			return null;
		}

		int w = img.getWidth();
		int h = img.getHeight();

		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();

		return dimg;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		this.repaint();
	}

}
