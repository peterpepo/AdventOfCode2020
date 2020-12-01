package day01;

public class NestedForExclusive {

	public static interface IAction {
		public void act(int[] indices);
	}

	private final int idx_low;
	private final int idx_high;
	private final int max_level;
	private final IAction action;

	public NestedForExclusive(int idx_low, int idx_high, int max_level, IAction action) {
		this.idx_low = idx_low;
		this.idx_high = idx_high;
		this.max_level = max_level;
		this.action = action;
	}

	public void nFor() {
		n_for(new int[0]);
	}

	private void n_for(int[] indices) {

		if (indices.length == max_level) {
			action.act(indices);

		} else {
			// Get last indice. In case of empty indices, set previous to the left of
			// initial (0th) => -1
			int lastIndice = idx_low - 1;
			if (indices.length > 0) {
				lastIndice = indices[indices.length - 1];
			}

			// Apend indices "to the right" of last one
			for (int i = lastIndice + 1; i <= idx_high - (max_level - 1) + indices.length; i++) {

				int[] indices_new = new int[indices.length + 1];
				System.arraycopy(indices, 0, indices_new, 0, indices.length);
				indices_new[indices.length] = i;

				n_for(indices_new);

			}
		}
	}

}
