package image_sorting_visualizer.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Picture {
	
	private int[] pixels;
	private BufferedImage image;
	private String path;
	
	public Picture(String path) {
		this.path = path;
		load();
	}
	
	private void load() {
		try {
			
			image = ImageIO.read(Picture.class.getResource(path));
			int width = image.getWidth();
			int height = image.getHeight();
			this.pixels = new int[width*height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int[] getPixels() {
		return pixels;
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
}
