import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JOptionPane;

/*
 * Various String-related utility methods
 */
public final class EString {
	
	//performs classic "join"
	public static String join(Collection<?> s, String delimiter) {
		if (s==null) return "";
		StringBuilder builder = new StringBuilder();
		Iterator iter = s.iterator();
		while (iter.hasNext()) {
			builder.append(iter.next());
			if (!iter.hasNext()) {
				break;
			}
			builder.append(delimiter);
		}
		return builder.toString();
	}

	//performs classic "join"
	public static String join(int[] s, String delimiter) {
		Integer a[] = new Integer[s.length];
		for (int i = 0; i < s.length; i++)
			a[i] = s[i];
	
		return join(Arrays.asList(a), delimiter);
	}

	//performs classic "join"
	public static String join(Integer[] s, String delimiter) {
		Integer a[] = new Integer[s.length];
		for (int i = 0; i < s.length; i++)
			a[i] = s[i];
	
		return join(Arrays.asList(a), delimiter);
	}

	//converts comma-delimited text to set of elements
	public static Set<String> extract(String in){
		if (in==null || in.trim().length()==0) return new HashSet<String>();
		
		Set<String> result=new HashSet<String>();
		
		result.addAll(Arrays.asList((in.split("\\s*,\\s*"))));
		
		return result;
	}

	//reads positive integer from user
	public static Integer getObjectNameFromUser(String message) {
		
		int objectName=-1;
		
		while(objectName < 0) {
			String contin = JOptionPane.showInputDialog(null, message);
			if (contin == null) return null;
			
			try {
				objectName = Integer.parseInt(contin);				
			} catch (NumberFormatException e1) {							
			}
			
			if (objectName<0) {
				JOptionPane.showMessageDialog(null, "Please enter a positive integer as object name", "Enter Object name", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		return objectName;
		
	}

	//reads positive integer from user
	public static Integer getContinNumberFromUser(String message) {
		int continNum = -1;
	
		while (continNum < 0) {
			String contin = JOptionPane.showInputDialog(null, message);
			if (contin == null)
				return null;
	
			try {
				continNum = Integer.parseInt(contin);
				
			} catch (NumberFormatException e1) {
			}
	
			if (continNum < 0) {
				JOptionPane.showMessageDialog(null, "Please enter a positive integer as contin number", "Enter Contin Number", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	
		return continNum;
	
	}

	
}