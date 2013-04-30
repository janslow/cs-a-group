package grouppractical.demotest;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import grouppractical.client.Map;
import grouppractical.client.Position;

/**
 * A simple implementation of a Map, using two two-dimensional arrays
 * @author Joe Zammit
 *
 */
public class SimpleMap implements Map {
	private final int W;
	private final int H;
	private Boolean[][] obstacle;
	private short[][] certainty;
	private long currentVersion;
	
	/** 
	 * Constructs a new SimpleMap
	 * 
	 * @param width the width of the SimpleMap
	 * @param height the height of the SimpleMap
	 */
	public SimpleMap(int width, int height) {
		W = width;
		H = height;
		obstacle = new Boolean[W][H];
		certainty = new short[W][H];
		for (int i = 0; i < W; i ++) {
			for (int j = 0; j < H; j ++) {
				obstacle[i][j] = false;
				certainty[i][j] = Position.MIN_CERTAINTY;
			}
		}
	}
	
	public Iterator<Position> iterator() {
		Iterator<Position> it = new MapIterator();
		return it;
	}
	
	public int getHeight() {
		return H;
	}

	public int getWidth() {
		return W;
	}

	public Position getPosition(int x, int y) {
		Position p = new Position(x,y,obstacle[x][y], certainty[x][y]);
		return p;
	}
	
	private class MapIterator implements Iterator<Position>{
		private int i = 0;
		private int j = 0;
		private final long version;
		
		public MapIterator() {
			version = currentVersion;
		}
		
		public void checkVersion() {
			if (version!=currentVersion) {
				throw new ConcurrentModificationException();
			}
		}
		
		public boolean hasNext() {
			checkVersion();
			return j!=H;
		}

		public Position next() {
			checkVersion();
			Position ret = new Position(i, j, obstacle[i][j], certainty[i][j]);
			if (i==W-1) {
				i = 0;
				j ++;
			}
			else {
				i ++;
			}
			return ret;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

	@Override
	public void setPosition(Position p) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
}


