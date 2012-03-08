package Admin;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.*;
import java.lang.*;
import java.awt.font.*;
import java.text.*;
import javax.swing.*;


import java.util.Vector;

class TwoDCurve
{

String dtype;
int[] continNums;
int zoom;
double zoomSection;
double zoomX,zoomY;
double scale;
int maxSection;
int minSection;
int maxY;
int minY;
int maxX;
int minX;
int step;
String continName;
double slope;
int width,height,stroke;
HashMap syns;    
String[] series;
String[] continNames;
protected int m=0;




  

public TwoDCurve(int[] continNums, String dtype,int zoom,int stroke)throws SQLException,
ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException

{
this.continNums = continNums;
continNames = new String[continNums.length];
series = new String[continNums.length];
this.dtype = dtype;
this.zoom = zoom;
this.stroke = stroke;
step = 10;
setContinName();
setDimension();
if (dtype.equals("Z_Y")||dtype.equals("Z_X")) setSyns();


}



public void setSyns() throws SQLException,
ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException
{
	syns = new HashMap(10000);
    Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	try
	{           
		Class.forName("com.mysql.jdbc.Driver").newInstance();		
		con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		 for (int i=0;i<continNames.length;i++)
        {
	    pst = con.prepareStatement("select type,pre,post,post1,post2,post3,post4,mid,sections,partnerNum,preobj,postobj1,postobj2,postobj3,postobj4 from synapsecombined " );
		rs = pst.executeQuery(); 
		while(rs.next())
		{
			String type = rs.getString("type"),
			       pre = rs.getString("pre"),
			       post = rs.getString("post"),
			       post1 = rs.getString("post1"),
			       post2 = rs.getString("post2"),
			       post3 = rs.getString("post3"),
			       post4 = rs.getString("post4"),
			     
			       preobj = rs.getString("preobj"),
			       postobj1 = rs.getString("postobj1"),
			       postobj2 = rs.getString("postobj2"),
			       postobj3 = rs.getString("postobj3"),
			       postobj4 = rs.getString("postobj4");
			
			int    sections = rs.getInt("sections"),
			       partnerNum = rs.getInt("partnerNum");
			String name="" ;
			      
			
			
            if (type.equals("electrical"))   
            {
            	type="e";
            	if(pre.equals(continNames[i]))
            	{            		
            		name=post;
            		Synapse2 syn = new Synapse2(name,type,sections,preobj,postobj1,postobj2,postobj3,postobj4);
            		
            		syns.put(syn.preobj, syn);
            	}
            	else if(post.equals(continNames[i]))
            	{
            		name=pre;
            		Synapse2 syn = new Synapse2(name,type,sections,preobj,postobj1,postobj2,postobj3,postobj4);
            		syns.put(syn.postobj1, syn);
            	}
            	
            } 
            else 
            {
            	
            	if(pre.equals(continNames[i]))
            	{
            		type = "out";
            		name = post;
            		Synapse2 syn = new Synapse2(name,type,sections,preobj,postobj1,postobj2,postobj3,postobj4);
            		syns.put(syn.preobj, syn);
            	}
            	else if(post1.equals(continNames[i]))
            	{
            		type = "in";
            		name=pre;
            		if (partnerNum>1) name = pre+"->"+post;
            		Synapse2 syn = new Synapse2(name,type,sections,preobj,postobj1,postobj2,postobj3,postobj4);
            		syns.put(syn.postobj1, syn);
            	}
            	else if(post2.equals(continNames[i]))
            	{
            		type = "in";
            		name=pre;
            		if (partnerNum>1) name = pre+"->"+post;
            		Synapse2 syn = new Synapse2(name,type,sections,preobj,postobj1,postobj2,postobj3,postobj4);
            		syns.put(syn.postobj2, syn);
            	}
            	else if(post3.equals(continNames[i]))
            	{
            		type = "in";
            		name=pre;
            		if (partnerNum>1) name = pre+"->"+post;
            		Synapse2 syn = new Synapse2(name,type,sections,preobj,postobj1,postobj2,postobj3,postobj4);
            		syns.put(syn.postobj3, syn);
            	}
            	else if(post4.equals(continNames[i]))
            	{
            		type = "in";
            		name=pre;
            		if (partnerNum>1) name = pre+"->"+post;
            		Synapse2 syn = new Synapse2(name,type,sections,preobj,postobj1,postobj2,postobj3,postobj4);
            		syns.put(syn.postobj4, syn);
            	}
            	
            }//else
			
		

	        }//while
				rs.close();
				pst.close();
        }//for
	}catch (SQLException e) 
	{
    e.printStackTrace (  );
    } 
	finally 
	{
    if (con != null) con = null;
    }

} 

public void setContinName()
{
	
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	
	try
		{	  Class.forName("com.mysql.jdbc.Driver").newInstance();		
		 con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
			
	          
			  for (int i = 0; i < continNums.length; i++) {
				st = con.createStatement();
				rs = st
						.executeQuery("select CON_AlternateName,series from contin where CON_Number="
								+ continNums[i]);
				while (rs.next()) {
					continNames[i] = rs.getString(1);
					//System.out.println(continNames[i]);
					series[i] = rs.getString(2);
					if (i==0) {
						continName = continNames[0];
					}else{
					if (!continNames[i].equals(continNames[i-1]))continName = continName+"_"+continNames[i];
					}
					
					//System.out.println(continNum);
					//System.out.println(continName);

				}
				rs.close();
				st.close();
			}
		}
		catch (Exception e) 
		{
	    e.printStackTrace (  );
	    } 
		
}



public void setDimension(){

    maxSection=0;
	minSection=0;
	

	scale = 1.0000;


	Connection con = null;
	PreparedStatement pstmt=null;
	String jsql=null,jsql2=null;
	ResultSet rs = null;
	
// find out the max and min X and Y
    try{
    	 con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
 		


  jsql = "select max(z1),min(z1) from display2 where ";
		for (int i = 0; i < continNums.length; i++) {
			

		if (i==continNums.length - 1) 	
		{
			jsql = jsql + "continNum="+continNums[i];
		}else{
		jsql = jsql + "continNum="+continNums[i]+" or ";
		}
		}//for
		System.out.println(jsql);
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		rs.next();
		maxSection = rs.getInt(1)+1;
		minSection = rs.getInt(2);

	
    }
	catch (Exception e) 
	{
    e.printStackTrace (  );
    }

	width = (int) zoom*1024;
	height = (int) zoom*768;
	System.out.println("maxSection="+maxSection);
	System.out.println("minSection="+minSection);
	zoomSection = width/((maxSection-minSection)*1.2);

	zoomX=height/3000.0000d; // the width of image , need to be change here if different
	zoomY=height/4000.0000d; // the height of image , need to be change here if different
 
}



  public BufferedImage getBufferedImage()  {
	
	  
	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
	Graphics2D g2 = image.createGraphics();
	g2.setPaint(Color.white);
	g2.fillRect(0, 0, width, height);
	
	
	
    Composite origComposite = g2.getComposite();
  //show title
    Font font0 = new Font("SansSerif",Font.PLAIN,30);
    Font font1 = new Font("SansSerif",Font.PLAIN,11);
    Font font2 = new Font("SansSerif",Font.PLAIN,18);
  
    
    
	g2.setColor(Color.magenta);
	g2.setFont(font1);
	g2.drawString("presynapse",(float)(50*zoom),(float)(60*zoom));
	
	g2.setColor(Color.red);
	g2.setFont(font1);
	g2.drawString("postsynapse",(float)(50*zoom),(float)(80*zoom));
	
	g2.setColor(Color.green);
	g2.setFont(font1);
	g2.drawString("gap junction",(float)(50*zoom),(float)(100*zoom));
	
	//show direction
	Line2D ll = new Line2D.Float();
	ll.setLine((double)(10*zoom),(double)(100*zoom),(double)(10*zoom),(double)(600*zoom));
	g2.setPaint(Color.blue);
    g2.draw(ll);
    
	g2.setColor(Color.BLUE);
    g2.setFont(font1);
	
    if (dtype=="Z_Y")
	{
    g2.drawString("Dorsal",(float)(10*zoom),(float)(75*zoom));
	g2.drawString("Ventral",(float)(10*zoom),(float)(625*zoom));
	}else
	{
		g2.drawString("Right",(float)(10*zoom),(float)(75*zoom));
		g2.drawString("Left",(float)(10*zoom),(float)(625*zoom));	
	}
	
    
	g2.setPaint(Color.blue);
	g2.setFont(font1);
    
    g2.drawString("anterior",(float)(25*zoom),(float)(height*0.97));
	g2.drawString("posterior",(float)(950*zoom),(float)(height*0.97));
	


	Font font = new Font("SansSerif",Font.PLAIN,10);
    
	// Draw the line.
	Line2D l = new Line2D.Float();	
    Integer scaleNum = null;

    int scaleLength = (maxSection -minSection)/100;
	for (int f=0;f<=scaleLength+1;f++ ){
	double start = 50-(minSection-Math.floor(minSection/100)*100)*zoomSection;
    l.setLine((double)(start+f*100*zoomSection),(double)(height*0.95),(double)(start+f*100*zoomSection),(double)(height*0.95-5));
	g2.setPaint(Color.black);
    g2.draw(l);

	l.setLine((double)(start+f*(100*zoomSection)),(double)(height*0.95),(double)(start+(f+1)*(100*zoomSection)),(double)(height*0.95));
	g2.setPaint(Color.black);
    g2.draw(l);
    if(f%2==0){
	g2.setFont(font);
	scaleNum = new Integer( (int)Math.floor(minSection/100)*100+f*100 );
	g2.drawString(scaleNum.toString(),(float)(start+f*(100*zoomSection)),(float)(height*0.95-10));
    }
	}



	Connection con = null;
	PreparedStatement pstmt=null;
	String jsql=null;
	ResultSet rs = null;

	Double x1,y1,z1,x2,y2,z2;
	Point2D.Double point1,point2;

	int cellbody1,cellbody2;
	int branch1,branch2;
	String remark1,remark2,objName1,objName2,series1,series2;
	

	Color[] color = {Color.blue,Color.red,Color.green,Color.magenta,Color.orange,Color.black,Color.yellow,Color.cyan,Color.DARK_GRAY,Color.pink,Color.gray,Color.lightGray,Color.blue,Color.red,Color.green,Color.magenta,Color.orange,Color.black,Color.yellow,Color.cyan,Color.DARK_GRAY,Color.pink,Color.gray,Color.lightGray};

	try {
		 con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
			
	int cint = 0;
	for (int i = 0; i < continNums.length; i++) 
	{
		
		
		g2.setColor(color[cint]);
	    g2.setFont(font0);
		g2.drawString(continNames[i],(float)(50*zoom),(float)(30+30*i*zoom));
		g2.setFont(font2);
		g2.drawString(" in "+series[i],(float)(250*zoom),(float)(30+30*i*zoom));
		
		String[] serie = series[i].split(",");
		Color[] scolor = new Color[serie.length];
		g2.drawString(" in ",(float)(150*zoom),(float)(30+30*i*zoom));
		for (int j=0;j<serie.length;j++)
		{
			scolor[j] = color[cint+j];
			g2.setColor(scolor[j]);
			g2.drawString(serie[j],(float)(150*zoom+60+60*j),(float)(30+30*i*zoom));
			
		}
		cint = cint+serie.length;

		

		
	
		
		jsql = "select x1,y1,z1,remarks1,cellbody1,branch1,objName1,x2,y2,z2,remarks2,cellbody2,branch2,objName2,series1,series2 from display2 where continNum="+ continNums[i];
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) {



			x1 = (double)(3000.0000-rs.getInt(8))*zoomX/1.2;
			y1 = (double)rs.getInt(2)*zoomY/1.2;
			z1 = (rs.getInt(3) - minSection) * zoomSection + 50;
			objName1 = rs.getString(7);
	
			if (rs.getString(4) == null) {
				remark1 = "";
			} else {
				remark1 = rs.getString(4);
			}
			
			cellbody1 = rs.getInt(5);
			branch1 = rs.getInt(6);
			series1 =rs.getString(15);
			series2 =rs.getString(16);
			

		
			

			x2 = (double)(3000.0000-rs.getInt(8))*zoomX/1.2;
			y2 = (double)rs.getInt(9)*zoomY/1.2;
			z2 = (rs.getInt(10) - minSection) * zoomSection + 50;
			objName2 = rs.getString(14);
			
			if (rs.getString(11) == null) {
				remark2 = "";
			} else {
				remark2 = rs.getString(11);
			}
			cellbody2 = rs.getInt(12);
			branch2 = rs.getInt(13);




			if (dtype.equals("Z_X") || dtype.equals("Z_X_NoSynapse")) {
				point1 = new Point2D.Double(z1, x1);
				point2 = new Point2D.Double(z2, x2);
			} else {
				point1 = new Point2D.Double(z1, y1);
				point2 = new Point2D.Double(z2, y2);
			}
			if (syns.containsKey(objName1))
			{
				Synapse2 sy = (Synapse2) syns.get(objName1);
				if(sy.getFlag()==0)
				{
				    //draw synapse
					drawSyn(sy,g2,point1);
					sy.setFlag(1);
				}
			}
			if (syns.containsKey(objName2))
			{
				Synapse2 sy = (Synapse2) syns.get(objName2);
				if(sy.getFlag()==0)
				{
				    //draw synapse
					drawSyn(sy,g2,point2);
					sy.setFlag(1);
				}
			}

			
			//System.out.println(point1.x+"  "+point1.y);
			//System.out.println(point2.x+"  "+point2.y);
			g2.setStroke(new BasicStroke(1));
			
			l.setLine(point1,point2);
			g2.setComposite(origComposite);
			if (!remark1.equals("") && !remark1.equals("null")
					&& remark1 != null) {
				g2.setFont(font);
				g2.setPaint(color[i]);
				g2.drawString(remark1,
						(float) (((Point2D.Double) point1).x),
						(float) (((Point2D.Double) point2).y - 5));
		
			}

			if (!remark2.equals("")
					&& !remark2.equals("null")
					&& remark2 != null) {
				g2.setFont(font);
				g2.setPaint(color[i]);
				g2.drawString(remark2,
						(float) (point2.x),
						(float) (point2.y - 5));
				System.out.println(remark2);
				remark2 = "";
			}

			if (cellbody1 == 1 && cellbody2 == 1) {
				g2.setStroke(new BasicStroke(10 * zoom));
				g2.setPaint(new Color(100, 100, 255, 180));

			}
/**
			if (branch1==1) {
				g2.setPaint(new Color(0, 128, 128, 180));
				g2.fill(getControlPoint(point1));
				g2.setFont(font1);
				
				g2.drawString( (rs.getInt(3))+ "",(float) (point1.getX()),(float) (point2.getY() + 20));
				g2.setPaint(color[i]);
			}

			if (branch2==1) {
				g2.setPaint(new Color(0, 128, 128, 180));
				g2.fill(getControlPoint(point2));
				g2.setFont(font1);
				
				//g2.drawString( (rs.getInt(3)) + "",(float) (point2.getX()),(float) (point2.getY() + 20));
				g2.setPaint(color[i]);
			}
**/
			//System.out.println(((Point2D.Double)points.elementAt(2*j)).getX()+" "+((Point2D.Double)points.elementAt(2*j)).getY()+"      "+((Point2D.Double)points.elementAt(2*j+1)).getX()+" "+((Point2D.Double)points.elementAt(2*j+1)).getY());
            // System.out.println("sereies1= "+series1);
             
			for (int j=0;j<serie.length;j++)
			{
				//System.out.println("sereie= "+serie[j]);
				if (series1.equals(serie[j])) g2.setColor(scolor[j]);
			}
			g2.draw(l);

			
			
		}// end of while
	
		rs.close();
		pstmt.close();
	}//for
	
	
	}catch(Exception e){e.printStackTrace();}

	

	return image;
  } 
  
  protected void drawSyn(Synapse2 sy, Graphics2D g2, Point2D p)
  {
	//draw synapse
	  int compositeType = AlphaComposite.SRC_OVER;
	  AlphaComposite transparentComposite = AlphaComposite.getInstance(compositeType, 0.4F);
	  Composite origComposite = g2.getComposite();
	  Font font = new Font("SansSerif",Font.PLAIN,10);
	  Line2D l = new Line2D.Float();
	 
  	
  	if (sy.type.equals("out"))
		{
  	
  		g2.setComposite(transparentComposite);
  	    g2.setStroke(new BasicStroke(sy.sections));
  	    
  		l.setLine(p.getX(),p.getY(),p.getX(),p.getY()-step*zoom );
          g2.setPaint(Color.magenta);
          g2.draw(l);
          
          

  		l.setLine(p.getX(),p.getY()-step*zoom,p.getX()-5,p.getY()-step*zoom+5);
          g2.setPaint(Color.magenta);
          g2.draw(l);
          
          l.setLine(p.getX(),p.getY()-step*zoom,p.getX()+5,p.getY()-step*zoom+5);
          g2.setPaint(Color.magenta);
          g2.draw(l);

          g2.setComposite(origComposite);
  		g2.setPaint(Color.magenta);
  		g2.setFont(font);
  		String foreignContinName = sy.name;
  		g2.setStroke(new BasicStroke(1));
  		g2.drawString(foreignContinName,(float)p.getX(),(float)p.getY()-step*zoom-5);
  		
  		step = step+10;
  		if (step == 50) step = 10;
  		
  		
		}
  	if (sy.type.equals("in"))
		{
  		g2.setComposite(transparentComposite);
  		g2.setStroke(new BasicStroke(sy.sections));
  		l.setLine(p.getX(),p.getY(),p.getX(),p.getY()-step*zoom );
          g2.setPaint(Color.red);
          g2.draw(l);
          
         
        
  		l.setLine(p.getX(),p.getY(),p.getX()-5,p.getY()-5);
          g2.setPaint(Color.red);
          g2.draw(l);
          
          l.setLine(p.getX(),p.getY(),p.getX()+5,p.getY()-5);
          g2.setPaint(Color.red);
          g2.draw(l);

          g2.setComposite(origComposite);
  		g2.setPaint(Color.red);
  		g2.setFont(font);
  		String foreignContinName = sy.name;
  		g2.setStroke(new BasicStroke(1));
  		g2.drawString(foreignContinName,(float)p.getX(),(float)p.getY()-step*zoom-5);
  		step = step+10;
  		if (step == 50) step = 10;
  		
		}
  	if (sy.type.equals("e"))
		{
  		g2.setComposite(transparentComposite);
  		g2.setStroke(new BasicStroke(sy.sections/10));	

  		l.setLine(p.getX(),p.getY(),p.getX(),p.getY()-step*zoom );
          g2.setPaint(Color.green);
          g2.draw(l);
          
         

          g2.setComposite(origComposite);
  		g2.setPaint(Color.green);
  		g2.setFont(font);
  		String foreignContinName = sy.name;
  		g2.setStroke(new BasicStroke(1));
  		g2.drawString(foreignContinName,(float)p.getX(),(float)p.getY()-step*zoom-5);
  		step = step+10;
  		if (step == 50) step = 10;
		}
  }
  
  protected Shape getControlPoint(Point2D p) {
	    // Create a small square around the given point.
	    int side = 4;
	    return new Rectangle2D.Double(
	        p.getX() - side / 2, p.getY() - side / 2,
	        side, side);
	  }  
}