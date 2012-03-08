package Admin;
import java.util.ArrayList;


public class testArray {
	public static void main(String[] args) 
	{
		String ss1 ="1,23,234,1,5";
		String ss2="2,4,1,78";
		String[] s1 = ss1.split(",");
		String[] s2 = ss2.split(",");
		System.out.println(joinTwoPostname(s1,s2));
	}
	public static String joinTwoPostname(String[] s1, String[] s2)
	{
		ArrayList names = new ArrayList(10);
		for(int j=0;j<s1.length;j++)
		{
			if (!names.contains(s1[j]))names.add(s1[j]);
		}
		for(int j=0;j<s2.length;j++)
		{
			if (!names.contains(s2[j]))names.add(s2[j]);
		}
		String pname="";
		for(int j=0;j<names.size();j++)
		{
			if(pname.equals("")) {pname=(String)names.get(j);}
			else{pname=pname+","+(String)names.get(j);}
		
		}
		return pname;
	}

}
