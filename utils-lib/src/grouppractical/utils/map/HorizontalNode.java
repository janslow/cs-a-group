package grouppractical.utils.map;


public class HorizontalNode extends BSPMap {

	public HorizontalNode(int x1, int x2, int y1, int y2) {
		super(x1, x2, y1, y2);
	}

	@Override
	public Position getPosition(int x, int y) {
		// Bottom interval is [y1..middle), top interval is [middle..y2)
		int middle = (y1 + y2) / 2;
		if (y < middle) {
			if (node1 == null)
				return new Position(x, y, false, (short)0);
			else
				return node1.getPosition(x, y);
		}
		else {
			if (node2 == null)
				return new Position(x, y, false, (short)0);
			else
				return node2.getPosition(x, y);
		}
	}

	@Override
	public void setPosition(Position p) {
		// Bottom interval is [y1..middle), top interval is [middle..y2)
		int middle = (y1 + y2) / 2;
		if (p.getY() < middle) {
			if (node1 == null) {
				// special case: leaf node
				if (x2 - x1 == 1 && middle - y1 == 1)
					node1 = new LeafNode(x1, x2, y1, middle);
				// otherwise we need a horizontal node
				else
					node1 = new VerticalNode(x1, x2, y1, middle);
			}
			node1.setPosition(p);
		}
		else {
			if (node2 == null) {
				// special case: leaf node
				if (x2 - x1 == 1 && y2 - middle == 1)
					node2 = new LeafNode(x1, x2, middle, y2);
				// otherwise we need a horizontal node
				else
					node2 = new VerticalNode(x1, x2, middle, y2);
			}
			node2.setPosition(p);
		}
	}

}
