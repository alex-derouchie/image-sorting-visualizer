package image_sorting_visualizer.sorting;

public class Sorter {

	public static boolean sort(Pixel[] pixels, SortingMethod method, int speed) {
		for(int i=0; i<speed; i++) {
			if(method == SortingMethod.BUBBLE_SORT) BubbleSort.sort(pixels);
		}
		return isSorted(pixels);
	}
	
	public static boolean isSorted(Pixel[] pixels) {
		for(int i=0;i<pixels.length;i++) {
			if(i != pixels[i].id) return false;
		}
		return true;
	}
	
}
