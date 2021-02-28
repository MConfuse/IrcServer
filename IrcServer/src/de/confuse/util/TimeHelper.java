package de.confuse.util;

public class TimeHelper {

	public long lastMS = System.currentTimeMillis();
	public long currentMS = System.currentTimeMillis();

	/**
	 * Used to reset the Time for the hasTimeElapsed method.
	 * 
	 */
	public void reset()
	{
		lastMS = System.currentTimeMillis();
	}

	public boolean hasTimeElapsed(long time, boolean reset)
	{
		if (System.currentTimeMillis() - lastMS > time)
		{
			if (reset)
				reset();

			return true;
		}

		return false;
	}

	public boolean hasReached(float paramFloat)
	{
		return ((float) (System.currentTimeMillis() - currentMS) >= paramFloat);
	}

	/**
	 * Used to reset the hasReached method.
	 * 
	 */
	public void resetTimer()
	{
		currentMS = System.currentTimeMillis();
	}

}
