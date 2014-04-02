package dst.ass1.jpa.listener;

import java.util.HashMap;
import java.util.Map;

public class DefaultListener {
	
	private static int loadOperations;
	private static int updateOperations;
	private static int removeOperations;
	private static int persistOperations;
	private static long overallTimeToPersist;
	
	private Map<Integer, Long> startTimes = new HashMap<Integer, Long>();

	public static int getLoadOperations() {
		return loadOperations;
	}

	public static int getUpdateOperations() {
		return updateOperations;
	}

	public static int getRemoveOperations() {
		return removeOperations;
	}

	public static int getPersistOperations() {
		return persistOperations;
	}

	public static long getOverallTimeToPersist() {
		return overallTimeToPersist;
	}

	public static double getAverageTimeToPersist() {
		return (double) overallTimeToPersist / persistOperations;
	}

	public static void clear() {
		loadOperations = 0;
		updateOperations = 0;
		removeOperations = 0;
		persistOperations = 0;
		overallTimeToPersist = 0;
	}
	
	// TODO add persist time information
	public synchronized void onPrePersist(Object object) {
//		startTimes.put(object.hashCode(), System.currentTimeMillis());
	}
	
	// TODO add persist time information
	public synchronized void onPostPersist(Object object) {
//		overallTimeToPersist += System.currentTimeMillis() - startTimes.get(object.hashCode());
		startTimes.remove(object.hashCode());
		persistOperations++;
	}
	
	public synchronized void onPostRemove(Object object) {
		removeOperations++;
	}
	
	public synchronized void onPostUpdate(Object object) {
		updateOperations++;
	}
	
	public synchronized void onPostLoad(Object object) {
		loadOperations++;
	}
	
}
