package grouppractical.utils;

public class StandardOps {
	/**
	 * Converts a number x (where minx <= x < maxx) to an number y (where miny <= y <= maxy)
	 * @param x Number x to convert
	 * @param minx Minimum (inclusive) value of x
	 * @param maxx Maximum (exclusive) value of x
	 * @param miny Minimum (inclusive) value of y
	 * @param maxy Maximum (exclusive) value of y
	 * @return Converted number y
	 */
	public static double convert(double x, double minx, double maxx, double miny, double maxy) {
		//Converts x to intermediate value z (where 0 <= z < 1)
		double z = (x - minx) / (maxx - minx);
		//Converts z to final value y
		return (z * (maxy - miny)) + miny;
	}
}
