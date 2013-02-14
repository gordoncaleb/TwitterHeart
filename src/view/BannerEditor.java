package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BannerEditor extends javax.swing.JDialog {

	public static double maxRowHeight = 32.0;

	public final HeartCanvas canvas;

	public JPanel panel = new JPanel(new GridLayout(0, 3));

	public BannerEditor(JFrame owner, final HeartCanvas canvas) {
		super(owner, false);

		this.setLayout(new BorderLayout());

		this.canvas = canvas;

		build();

		this.add(panel, BorderLayout.CENTER);

		final JSlider masterSpeed = new JSlider();

		masterSpeed.setMaximum(50);
		masterSpeed.setMinimum(1);
		masterSpeed.setValue(1);

		masterSpeed.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {

				Vector<FeedBanner> banners = canvas.getFeedBanners();

				for (FeedBanner banner : banners) {
					banner.setSpeedMult(masterSpeed.getValue());
				}

			}

		});

		this.add(masterSpeed, BorderLayout.NORTH);

		JPanel spanel = new JPanel(new BorderLayout());

		final JTextField txt = new JTextField();

		txt.setText(canvas.getTf().getKeyWords());

		JButton changeBtn = new JButton("BOOM!");

		changeBtn.addMouseListener(new MouseListener() {

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
				canvas.getTf().setKeywords(txt.getText().trim());
			}

		});

		spanel.add(txt, BorderLayout.CENTER);
		spanel.add(changeBtn, BorderLayout.EAST);

		this.add(spanel, BorderLayout.SOUTH);

		this.setSize(500, 500);
		this.setVisible(true);
	}

	public void build() {
		panel.removeAll();
		// this.setLayout(new GridLayout(20, 2));

		panel.add(new JLabel("Speed", SwingConstants.CENTER));
		panel.add(new JLabel("Size", SwingConstants.CENTER));
		panel.add(new JLabel("Font", SwingConstants.CENTER));

		Vector<FeedBanner> banners = canvas.getFeedBanners();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = ge.getAvailableFontFamilyNames();
		Integer[] styles = new Integer[] { Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC };

		JSlider speedSlider;
		JSlider sizeSlider;
		JPanel fontPanel;
		JComboBox<Integer> cmbStyle;
		JComboBox<String> cmbFont;

		for (FeedBanner banner : banners) {
			speedSlider = new JSlider();
			speedSlider.setSize(100, 100);
			speedSlider.setMaximum(100);
			speedSlider.setMinimum(1);
			speedSlider.setValue((int) (banner.getSpeed() / 5.0f));
			speedSlider.addChangeListener(new SpeedChangeListener(banner));

			sizeSlider = new JSlider();
			sizeSlider.setSize(100, 100);
			sizeSlider.setMaximum(100);
			sizeSlider.setMinimum(1);
			sizeSlider.setValue((int) ((double) banner.getH() * 100.0 / (double) maxRowHeight));
			sizeSlider.addChangeListener(new SizeChangeListener(banner));

			fontPanel = new JPanel();
			cmbStyle = new JComboBox<Integer>(styles);
			cmbStyle.setSelectedItem(banner.getFontStyle());
			cmbStyle.addActionListener(new StyleChangeListener(banner));
			fontPanel.add(cmbStyle);

			cmbFont = new JComboBox<String>(fontNames);
			cmbFont.setSelectedItem(banner.getFontName());
			cmbFont.addActionListener(new FontChangeListener(banner));
			fontPanel.add(cmbFont);

			panel.add(speedSlider);
			panel.add(sizeSlider);
			panel.add(fontPanel);
		}

		// this.doLayout();
		// this.invalidate();
		// this.validate();
		panel.revalidate();
		// this.pack();
		panel.repaint();
	}

	class SpeedChangeListener implements ChangeListener {
		private FeedBanner banner;

		public SpeedChangeListener(FeedBanner banner) {
			this.banner = banner;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider slider = (JSlider) e.getSource();
			banner.setSpeed(5 * slider.getValue());

		}

	}

	class SizeChangeListener implements ChangeListener {
		private FeedBanner banner;

		public SizeChangeListener(FeedBanner banner) {
			this.banner = banner;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider slider = (JSlider) e.getSource();
			banner.setH((int) (maxRowHeight * (((float) slider.getValue()) / 100)));
		}

	}

	class StyleChangeListener implements ActionListener {
		private FeedBanner banner;

		public StyleChangeListener(FeedBanner banner) {
			this.banner = banner;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (arg0.getSource() instanceof JComboBox) {
				JComboBox<Integer> box = (JComboBox<Integer>) arg0.getSource();

				int s = (Integer) box.getSelectedItem();

				banner.setFontStyle(s);
			}
		}

	}

	class FontChangeListener implements ActionListener {
		private FeedBanner banner;

		public FontChangeListener(FeedBanner banner) {
			this.banner = banner;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (arg0.getSource() instanceof JComboBox) {
				JComboBox<Integer> box = (JComboBox<Integer>) arg0.getSource();

				String s = (String) box.getSelectedItem();

				banner.setFontName(s);
			}
		}

	}

}
