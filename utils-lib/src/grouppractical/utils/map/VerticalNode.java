package grouppractical.utils.map;

public class VerticalNode extends BSPMap {

	public VerticalNode(int x1, int x2, int y1, int y2) {
		super(x1, x2, y1, y2);
	}

	@Override
	public Position getPosition(int x, int y) {
		// Left interval is [x1..middle), right interval is [middle..x2)
		int middle = (x1 + x2) / 2;
		if (x < middle) {
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
		// Left interval is [x1..middle), right interval is [middle..x2)
		int middle = (x1 + x2) / 2;
		if (p.getX() < middle) {
			if (node1 == null) {
				// special case: leaf node
				if (middle - x1 == 1 && y2 - y1 == 1)
					node1 = new LeafNode(x1, middle, y1, y2);
				// otherwise we need a horizontal node
				else
					node1 = new HorizontalNode(x1, middle, y1, y2);
			}
			node1.setPosition(p);
		}
		else {
			if (node2 == null) {
				// special case: leaf node
				if (x2 - middle == 1 && y2 - y1 == 1)
					node2 = new LeafNode(middle, x2, y1, y2);
				// otherwise we need a horizontal node
				else
					node2 = new HorizontalNode(middle, x2, y1, y2);
			}
			node2.setPosition(p);
		}
	}

}
