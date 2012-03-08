import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/*
 * Logging utilities
 * 
 * @author Sasha Levchuk
 */
public final class ELog {

	//prints message and exception stack.
	public static void error(String msg, Throwable e) {
		System.err.println(msg+":"+ELog.e2s(e));
	}

	//prints message
	public static void info(Object msg) {
		if (ELog.LOG_STACK) {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			
			String s="";
			for(int i=0;i<stackTraceElements.length;i++) {
				s=s+stackTraceElements[i]+":"+stackTraceElements[i].getMethodName()+"\n";
			}
			msg=msg+s;
		}
		System.err.println(new Date()+" "+msg);
	}

	public static final boolean LOG_STACK=false;

	//formats Throwable as string
	public static String e2s(Throwable e) {
		String retValue = null;
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			retValue = sw.toString();
		} finally {
			try {
				if (pw != null)
					pw.close();
				if (sw != null)
					sw.close();
			} catch (IOException ignore) {
			}
		}
		return retValue;
	}

	//returns RAM stats
	public static String getMemoryStats() {
		int mb = 1024*1024;
		 
	        //Getting the runtime reference from system
	        Runtime runtime = Runtime.getRuntime();
	
	        String buf="";
	       
	        buf=buf+("Free Memory:"
		            + runtime.freeMemory() / mb);
	        
	        buf=buf+("\nUsed Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);
	
	        
	        buf=buf+("\nTotal Memory:" + runtime.totalMemory() / mb);
	
	        buf=buf+("\nMax Memory:" + runtime.maxMemory() / mb);
	        
	        return buf;
	}
	
}