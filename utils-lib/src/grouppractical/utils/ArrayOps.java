package grouppractical.utils;

public class ArrayOps {
	/**
	 * Calculates the difference between two arrays of objects
	 * @param The minuend to subtract from
	 * @param The subtrahend to subtract  
	 * @return The difference of A - B
	 */
	public static Object[] subtract(Object[] A, Object[] B) {
		// Short-circuit for empty sets (A-{} = A and {}-B = {} = A)
		if (A == null || A.length == 0 || B == null || B.length == 0)
			return A;
		
		// Clones A, so as to leave it unaffected
		Object[] Ac = A.clone();
		
		/* Iterates through all the current names, moving new ones to the front of the array,
		 * such that, Ac[0..i) are the new names
		 */
		int i = 0;
		for (int j = 0; j < Ac.length; j++) {
			boolean isNew = true;
			for (Object b : B)
				if (b.equals(Ac[j])) {
					isNew = false;
					break;
				}
			if (isNew)
				Ac[i++] = Ac[j];
		}
		
		// diff = Ac[0..i) = A - B
		Object[] diff = new Object[i];
		System.arraycopy(Ac, 0, diff, 0, i);
		
		return diff;
	}
}
