package net.caprazzi.rest;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Really Simple random timer.
 * @author mcaprari
 */
abstract public class RandomTimer {

	private final Random random = new Random();
		
	private int waitCount;
	
	/**
	 * Creates a new random timer that will execute tick()
	 * each random number of seconds between min and mx
	 * @param minWait
	 * @param maxWait
	 */
	public RandomTimer(final int minWait, final int maxWait) {
		waitCount = nextIntInRange(minWait, maxWait);
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {						
			@Override
			public void run() {
				if (--waitCount == 0) {
					tick();
					waitCount = nextIntInRange(minWait, maxWait);
				}				
			}
			
		}, 0, 1000);
	}
	
	private int nextIntInRange(int min, int max) {
		return min + random.nextInt(max);
	}
		
	public abstract void tick(); 
}
