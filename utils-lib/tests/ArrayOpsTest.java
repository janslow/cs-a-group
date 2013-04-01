import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import static grouppractical.utils.ArrayOps.subtract;

public class ArrayOpsTest {

	@Test
	public void testSubtract() {
		int N = 20;
		Object[] empty = new Object[0], odds = new Object[N], evens = new Object[N], twenty = new Object[N],
				forty = new Object[2*N];
		for (int i = 0; i < N; i++) {
			twenty[i] = i;
			forty[2*i] = evens[i] = i*2;
			forty[2*i+1] = odds[i] = i*2 + 1;
		}
		
		assertArrayEquals("{} - {} = {}",empty, subtract(empty,empty));
		assertArrayEquals("{} - forty = {}",empty, subtract(empty,forty));
		
		Object[] oddsClone = odds.clone();
		assertArrayEquals("odds - {} = odds",odds, subtract(odds,empty));
		assertArrayEquals("odds - evens = odds",odds, subtract(odds,evens));
		assertArrayEquals("odds - twenty = { 11,13,15,17,19 }",new Object[] { 21,23,25,27,29,31,33,35,37,39 }, subtract(odds,twenty));
		assertArrayEquals("odds - odds = {}",empty, subtract(odds,odds));
		assertArrayEquals("odds - forty = {}",empty, subtract(odds,forty));
		assertArrayEquals("odds is not changed",oddsClone, odds);
	}
}
