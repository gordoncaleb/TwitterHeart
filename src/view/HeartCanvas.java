package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import model.TwitterFeed;

public class HeartCanvas extends JLabel implements ActionListener, ComponentListener, KeyEventDispatcher {

	public static int vShift = 50;

	public Vector<FeedBanner> banners = new Vector<FeedBanner>();

	public BufferedImage decalImage;
	public TwitterFeed tf;

	public BufferedImage resizedDecalImage;

	public BufferedImage textImage;

	public Color heartColor;

	public int[] yPos;

	private Timer timer;

	BannerEditor editor;

	private JFrame owner;
	private DisplayMode oldMode;
	private boolean fullscreen = false;

	private Thread anamationThread;

	private long time;
	private int frames;

	private Timer fpsCounter;

	public HeartCanvas() {
		owner = new JFrame();
		owner.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		owner.setLayout(new BorderLayout());

		owner.add(this, BorderLayout.CENTER);

		oldMode = owner.getGraphicsConfiguration().getDevice().getDisplayMode();

		tf = new TwitterFeed();

		decalImage = getImage("heartdecal.png", 0, 0);

		this.setSize(500, 500);

		initBanners();

		this.setPreferredSize(new Dimension(resizedDecalImage.getWidth(), resizedDecalImage.getHeight()));

		Color baseColor = Color.RED;
		heartColor = new Color(baseColor.getRed() / 255.0f, baseColor.getGreen() / 255.0f, baseColor.getBlue() / 255.0f, 0.5f);

		this.addComponentListener(this);

		textImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

		// anamationThread = new Thread(this);
		//
		// anamationThread.start();

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);

		timer = new Timer(10, this);
		timer.start();

		fpsCounter = new Timer(1000, this);
		fpsCounter.start();

		owner.setSize(500, 500);

		owner.setVisible(true);

		editor = new BannerEditor(this);

	}

	public void initBanners() {
		resizedDecalImage = resize(decalImage, this.getHeight(), this.getHeight());

		if (resizedDecalImage == null) {
			return;
		}

		double h = this.getHeight() * 0.08;

		BannerEditor.maxRowHeight = h;

		ArrayList<Integer> rowHeights = new ArrayList<Integer>();

		int totH = (resizedDecalImage.getHeight() - vShift * 2);
		int sum = 0;
		int rh;
		while (sum < totH) {

			if (Math.abs(sum - totH) < h) {
				rh = (int) h;

				System.out.println("Row " + rowHeights.size() + " is middle");
			} else {
				rh = (int) Math.max(22, Math.random() * h);
			}

			sum += rh;
			rowHeights.add(rh);
		}

		int numRows = rowHeights.size();

		yPos = new int[numRows];
		int y = vShift;
		for (int i = 0; i < numRows; i++) {
			y += rowHeights.get(i);

			if (i < banners.size()) {
				banners.get(i).setWH(this.getWidth(), rowHeights.get(i));
			} else {
				banners.add(new FeedBanner(tf, rowHeights.get(i), resizedDecalImage.getWidth(), (float) Math.max(Math.random() / 2.0, .2)));
			}

			yPos[i] = y;
		}

		for (FeedBanner banner : banners) {
			banner.setSpeed((float) Math.max(Math.random() * 50, 10));
		}
	}

	public Vector<FeedBanner> getFeedBanners() {
		return banners;
	}

	public static void main(String[] args) {
		new HeartCanvas();
	}

	public void paintComponent(Graphics g) {

		if (resizedDecalImage == null) {
			return;
		}

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		// RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// g2.setColor(Color.WHITE);
		// g2.fillRect(0, 0, this.getWidth(), this.getHeight());

		int pos = vShift;
		for (FeedBanner banner : banners) {
			pos += banner.drawOnCanvas(g2, pos);
		}

		// synchronized (textImage) {
		// g2.drawImage(textImage, xoff, yoff, null);
		// }

		// g2.setColor(heartColor);
		// g2.fillRect(0, 0, this.getWidth(), this.getHeight());

		if (decalImage != null) {

			int xoff = (int) (((double) this.getWidth() / 2.0) - ((double) resizedDecalImage.getWidth() / 2.0));
			int yoff = (int) (((double) this.getHeight() / 2.0) - ((double) resizedDecalImage.getHeight() / 2.0));

			g2.drawImage(resizedDecalImage, xoff, yoff, null);

			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, xoff, this.getHeight());
			g2.fillRect(xoff + resizedDecalImage.getWidth(), 0, this.getWidth() - (xoff + resizedDecalImage.getWidth()), this.getHeight());
		}

		g2.dispose();
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

		if (arg0.getSource() == timer) {
			this.repaint();
			frames++;
		}

		if (arg0.getSource() == fpsCounter) {

			System.out.println(1000 * ((double) frames / ((double) System.currentTimeMillis() - (double) time)) + "fps");

			frames = 0;
			time = System.currentTimeMillis();
		}
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent arg0) {

		initBanners();

		if (editor != null) {
			editor.build();
		}

		// synchronized (textImage) {
		// textImage = new BufferedImage(resizedDecalImage.getWidth(),
		// resizedDecalImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		// }
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {

		} else {
			if (e.getID() == KeyEvent.KEY_RELEASED) {
				// System.out.println("Pressed :" + e.getKeyCode());

				if (e.getKeyCode() == KeyEvent.VK_F11) {

					fullscreen = !fullscreen;

					GraphicsDevice device = owner.getGraphicsConfiguration().getDevice();

					if (!fullscreen) {

						device.setDisplayMode(oldMode);

						owner.setVisible(false);

						owner.dispose();

						owner.setUndecorated(false);

						device.setFullScreenWindow(null);

						owner.setSize(500, 500);

						owner.setLocationRelativeTo(null);

						owner.setVisible(true);

					} else {

						owner.setVisible(false);
						owner.dispose();
						owner.setUndecorated(true);
						device.setFullScreenWindow(owner);
						// device.setDisplayMode(dm)

						owner.setVisible(true);
					}
				}
			} else {
				if (e.getID() == KeyEvent.KEY_TYPED) {

				}
			}
		}
		return false;
	}

	public TwitterFeed getTf() {
		return tf;
	}

	public void setTf(TwitterFeed tf) {
		this.tf = tf;
	}

	// @Override
	// public void run() {
	//
	// while (true) {
	//
	// synchronized (textImage) {
	//
	// Graphics2D g2 = textImage.createGraphics();
	//
	// g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
	// RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	// g2.setRenderingHint(RenderingHints.KEY_RENDERING,
	// RenderingHints.VALUE_RENDER_QUALITY);
	//
	// // clear old image
	// g2.setColor(Color.white);
	// g2.fillRect(0, 0, textImage.getWidth(), textImage.getHeight());
	//
	// synchronized (banners) {
	// int[] rand = randomOrder(yPos.length);
	// FeedBanner banner;
	//
	// for (int i = 0; i < yPos.length; i++) {
	// banner = banners.get(rand[i]);
	// banner.drawOnCanvas(g2, yPos[rand[i]]);
	// }
	// }
	//
	// g2.dispose();
	//
	// }
	//
	// try {
	// Thread.sleep(10);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// }

}
