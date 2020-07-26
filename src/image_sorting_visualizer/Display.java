package image_sorting_visualizer;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

import image_sorting_visualizer.graphics.Picture;
import image_sorting_visualizer.sorting.Pixel;
import image_sorting_visualizer.sorting.Sorter;
import image_sorting_visualizer.sorting.SortingMethod;

public class Display extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;


	private JFrame frame;
	private Thread thread;
	private static String title = "Sorting Visualizer";
	private int width = 1000;
	private int height = 600;
	private boolean running = false;
	private boolean sorted = false;
	private BufferedImage image;
	private int[] pixels;
	private Pixel[] morePixels;

	private Picture pic;
	
	public Display() {
		frame = new JFrame();
	
		pic = new Picture("/images/smallsunset.jpg"); //Not yet working
		width = pic.getWidth();
		height = pic.getHeight();
		
		Dimension size = new Dimension(400, (int)(400.0/width * height));
		this.setPreferredSize(size);
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		morePixels = new Pixel[width*height];
		
		initPixels();
	}
	
	private void initPixels() {
		for(int i=0;i<pixels.length;i++) {
			morePixels[i] = new Pixel(pic.getPixels()[i], i);
		}
		randomizePixels();
	}
	
	private void randomizePixels() {
		ArrayList<Pixel> pixelList = new ArrayList<Pixel>();
		
		for(int i=0;i<morePixels.length;i++) {
			pixelList.add(morePixels[i]);
		}
		
		Collections.shuffle(pixelList);
		
		for(int i=0;i<morePixels.length;i++) {
			morePixels[i] = pixelList.get(i);
		}
	}
	
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 1000; //1000 FPS
		double delta = 0;
		int updates = 0;
		int frames = 0;
		
		while(running) {
			long now = System.nanoTime();
			delta += (now-lastTime)/ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer >= 1000) {
				timer += 1000;
				frame.setTitle(title + "| fps: " + frames + " fps");
				frames = 0;
				updates = 0;
			}
			
		}
	}
	
	private void render() {
		BufferStrategy buffStrat = this.getBufferStrategy();
		if (buffStrat == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		for(int i=0;i<pixels.length;i++) {
			pixels[i]= morePixels[i].color;
		}
		
		Graphics graphics = buffStrat.getDrawGraphics();
		graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		graphics.dispose();
		buffStrat.show();
		
	}
	
	private void update() {
		if(!sorted) {
			sorted = Sorter.sort(morePixels, SortingMethod.BUBBLE_SORT, 50000);
		}
	}
	
	
	public static void main(String[] args) {
		Display display = new Display();
		display.frame.setTitle(title);
		display.frame.add(display);
		display.frame.pack();
		display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.frame.setLocationRelativeTo(null);
		display.frame.setVisible(true);
		display.start();
		
	}
	
	
	
	
	
	
}
