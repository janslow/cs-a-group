
import grouppractical.utils.map.BSPMap;
import grouppractical.utils.map.Position;
import grouppractical.utils.map.VerticalNode;

import java.util.Iterator;

public class BSPMapTest {

	public static void main(String[] args) {
		BSPMap testMap = new VerticalNode(-100, 100, 200, 300);
		Position testPosition = new Position(50, 250, true, (short)100);
		testMap.setPosition(testPosition);
		
		Position pos = testMap.getPosition(50,  250);
		System.out.println("x = " + pos.getX() + "; y = " + pos.getY() + "; certainty = " + pos.getCertainty());

		testMap.setPosition(new Position(-50, 250, true, (short)27));
		testMap.setPosition(new Position(-100, 200, true, (short)30));
		testMap.setPosition(new Position(0, 300, true, (short)75));
		testMap.setPosition(new Position(100, 250, true, (short)100));
		
		Iterator<Position> it = testMap.iterator();
		while (it.hasNext()) {
			Position p = it.next();
			System.out.println("x = " + p.getX() + "; y = " + p.getY() + "; certainty = " + p.getCertainty());
		}
	}

}
