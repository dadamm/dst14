package dst.ass1.jpa.interceptor;

import java.util.regex.Pattern;

import org.hibernate.EmptyInterceptor;

public class SQLInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 3894614218727237142L;
	
	private static int selectCount;

	public synchronized String onPrepareStatement(String sql) {
		if(Pattern.matches("select [A-Za-z0-9,_. ]+ from .*(Expert|TaskForce).*", sql)) {
			selectCount++;
		}
		return sql;
	}

	public static void resetCounter() {
		selectCount = 0;
	}

	
	public static int getSelectCount() {
		return selectCount;
	}
}
