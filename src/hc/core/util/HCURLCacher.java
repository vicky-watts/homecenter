package hc.core.util;

public class HCURLCacher {
	private static final HCURLCacher instance = new HCURLCacher();

	public final static HCURLCacher getInstance() {
		return instance;
	}

	final private Stack free = new Stack();

	private int freeSize = 0;

	public HCURLCacher() {
	}

	public final HCURL getFree() {
		synchronized (free) {
			if (freeSize == 0) {
				// LogManager.log("------MEM ALLOCATE [HCURL]------");
				return new HCURL();
			} else {
				freeSize--;
				return (HCURL) free.pop();
			}
		}

	}

	public final void cycle(HCURL dp) {
		dp.reset();
		synchronized (free) {
			free.push(dp);
			freeSize++;
		}
	}
}