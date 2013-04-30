package grouppractical.utils.map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LeafNode extends BSPMap {

	private Position position;
	
	public LeafNode(int x1, int x2, int y1, int y2) {
		super(x1, x2, y1, y2);
		assert(x1 + 1 == x2 && y1 + 1 == y2);
	}
	
	@Override
	public Iterator<Position> iterator() {
		List<Position> list = new ArrayList<Position>();
		list.add(position);
		return list.iterator();
	}

	@Override
	public Position getPosition(int x, int y) {
		assert(x == x1 && y == y1);
		return position;
	}

	@Override
	public void setPosition(Position p) {
		assert(p.getX() == x1);
		assert(p.getY() == y1);
		position = p;
	}

}
