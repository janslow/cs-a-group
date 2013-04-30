package grouppractical.utils.map;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.common.collect.Iterators;

public abstract class BSPMap implements Map {

	// half-open intervals [x1, x2) x [y1, y2)
	protected int x1, x2, y1, y2;
	protected BSPMap node1, node2;
	
	public BSPMap(int x1, int x2, int y1, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	@Override
	public Iterator<Position> iterator() {
		Iterator<Position> empty = (new LinkedList<Position>()).iterator();
		Iterator<Position> leftIterator;
		if (node1 == null)
			leftIterator = empty;
		else
			leftIterator = node1.iterator();
		Iterator<Position> rightIterator;
		if (node2 == null)
			rightIterator = empty;
		else
			rightIterator = node2.iterator();
		return Iterators.concat(leftIterator, rightIterator);
	}

	@Override
	public int getHeight() {
		return y2 - y1;
	}

	@Override
	public int getWidth() {
		return x2 - x1;
	}
}

