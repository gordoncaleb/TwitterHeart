package view;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//import javax.media.opengl.GL;
//import javax.media.opengl.GL2;
//import javax.media.opengl.GLAutoDrawable;
//import javax.media.opengl.GLEventListener;
//import javax.media.opengl.awt.GLCanvas;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HeartJOGLCanvas extends JPanel {// implements GLEventListener {

	public HeartJOGLCanvas() {
		super(new BorderLayout());

		// GLCanvas canvas = new GLCanvas();

		// canvas.addGLEventListener(this);

		// this.add(canvas);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		final HeartJOGLCanvas canvas = new HeartJOGLCanvas();

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

		frame.pack();

		frame.setVisible(true);

	}

	// @Override
	// public void display(GLAutoDrawable drawable) {
	// System.out.println("DISPLAY CALLED");
	// GL2 gl = drawable.getGL().getGL2();
	// // Projection mode is for setting camera
	// gl.glMatrixMode(GL2.GL_PROJECTION);
	// // This will set the camera for orthographic projection and allow 2D
	// // view
	// // Our projection will be on 400 X 400 screen
	// gl.glLoadIdentity();
	// gl.glOrtho(0, 400, 400, 0, 0, 1);
	// // Modelview is for drawing
	// gl.glMatrixMode(GL2.GL_MODELVIEW);
	// // Depth is disabled because we are drawing in 2D
	// gl.glDisable(GL.GL_DEPTH_TEST);
	// // Setting the clear color (in this case black)
	// // and clearing the buffer with this set clear color
	// gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	// gl.glClear(GL.GL_COLOR_BUFFER_BIT);
	// // This defines how to blend when a transparent graphics
	// // is placed over another (here we have blended colors of
	// // two consecutively overlapping graphic objects)
	// gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	// gl.glEnable(GL.GL_BLEND);
	// // After this we start the drawing of object
	// // We want to draw a triangle which is a type of polygon
	// gl.glBegin(GL2.GL_POLYGON);
	// // We want to draw triangle in red color
	// // So setting the gl color to red
	// gl.glColor4f(1, 0, 0, 1);
	//
	//
	// // Making vertices of the triangle
	// gl.glVertex2d(100, 100);
	// gl.glVertex2d(100, 200);
	// gl.glVertex2d(200, 200);
	// // Our polygon ends here
	// gl.glEnd();
	// gl.glFlush();
	//
	// }
	//
	// @Override
	// public void dispose(GLAutoDrawable arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void init(GLAutoDrawable arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
	// int arg4) {
	// // TODO Auto-generated method stub
	//
	// }

}
