/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mxu
 */


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.ScrollPane;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class New2D  extends ScrollPane {

    String dtype;
    int[] continNums;
    float zoom;
    double zoomSection;
    double zoomX, zoomY;
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
    int width, height, stroke;
    HashMap syns;
    String name;
    String[] continNames;
    protected int m = 0;
    ArrayList<String> series;
    String[] showPartners ;
    int showIn;
    int showOut;
    int showElectrical;
    int showBackbone;
    float spacing;

    public New2D(int[] showContins) {
        this.continNums = showContins;
        continNames = new String[continNums.length];
        series = new ArrayList<String>();
        this.dtype = "Y_Z";
        this.zoom = 1;
        this.showIn = 1;
        this.showOut = 1;

        this.showElectrical = 1;
        this.showBackbone = 1;
        this.spacing = 1;
        this.showPartners = null;
        this.step = 10;

        setContinName();
        this.name = continName;
        setDimension();
        try {
            setSyns();
        } catch (Exception ex) {
          ex.printStackTrace();
        }

    }

    public Color getColor(Connection con, String series) {
        Color col = Color.blue;
        Color[] color = {Color.blue, Color.red, Color.green, Color.magenta, Color.orange, Color.black, Color.pink, Color.cyan, Color.DARK_GRAY, Color.yellow, Color.gray, Color.lightGray, Color.blue, Color.red, Color.green, Color.magenta, Color.orange, Color.black, Color.yellow, Color.cyan, Color.DARK_GRAY, Color.pink, Color.gray, Color.lightGray};


        try {

            PreparedStatement pst = null;
            ResultSet rs = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            pst = con.prepareStatement("select distinct IMG_Series from image");
            rs = pst.executeQuery();
            int i = 0;
            while (rs.next()) {
                String seri = rs.getString(1);
                if (seri.equals("Ventral Cord") || seri.equals("Ventral Cord 2")) {
                    seri = "VC";
                }
                if (series.equals(seri)) {
                    pst.close();
                    rs.close();
                    con = null;

                    return color[i];
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con = null;
            }
        }


        return col;
    }

    public void setSyns() throws SQLException,
            ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException {
        syns = new HashMap(10000);
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(DatabaseProperties.CONNECTION_STRING, DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
            for (int i = 0; i < continNames.length; i++) {
                pst = con.prepareStatement("select type,pre,post,post1,post2,post3,post4,mid,sections,partnerNum,preobj,postobj1,postobj2,postobj3,postobj4 from synapsecombined");
                rs = pst.executeQuery();
                while (rs.next()) {
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

                    int sections = rs.getInt("sections"),
                            partnerNum = rs.getInt("partnerNum");
                    String name = "";



                    if (type.equals("electrical")) {
                        type = "e";
                        if (pre.equals(continNames[i])) {
                            name = post;
                            Synapse2 syn = new Synapse2(name, type, sections, preobj, postobj1, postobj2, postobj3, postobj4);

                            syns.put(syn.preobj, syn);
                        } else if (post.equals(continNames[i])) {
                            name = pre;
                            Synapse2 syn = new Synapse2(name, type, sections, preobj, postobj1, postobj2, postobj3, postobj4);
                            syns.put(syn.postobj1, syn);
                        }

                    } else {

                        if (pre.equals(continNames[i])) {
                            type = "out";
                            name = post;
                            Synapse2 syn = new Synapse2(name, type, sections, preobj, postobj1, postobj2, postobj3, postobj4);
                            syns.put(syn.preobj, syn);
                        } else if (post1.equals(continNames[i])) {
                            type = "in";
                            name = pre;
                            if (partnerNum > 1) {
                                name = pre + "->" + post;
                            }
                            Synapse2 syn = new Synapse2(name, type, sections, preobj, postobj1, postobj2, postobj3, postobj4);
                            syns.put(syn.postobj1, syn);
                        } else if (post2.equals(continNames[i])) {
                            type = "in";
                            name = pre;
                            if (partnerNum > 1) {
                                name = pre + "->" + post;
                            }
                            Synapse2 syn = new Synapse2(name, type, sections, preobj, postobj1, postobj2, postobj3, postobj4);
                            syns.put(syn.postobj2, syn);
                        } else if (post3.equals(continNames[i])) {
                            type = "in";
                            name = pre;

                            if (partnerNum > 1) {
                                name = pre + "->" + post;
                            }
                            Synapse2 syn = new Synapse2(name, type, sections, preobj, postobj1, postobj2, postobj3, postobj4);
                            syns.put(syn.postobj3, syn);
                            //System.out.println("post3 found"+pre+">"+post+"obj3="+syn.postobj3);
                        } else if (post4.equals(continNames[i])) {
                            type = "in";
                            name = pre;
                            if (partnerNum > 1) {
                                name = pre + "->" + post;
                            }
                            Synapse2 syn = new Synapse2(name, type, sections, preobj, postobj1, postobj2, postobj3, postobj4);
                            syns.put(syn.postobj4, syn);
                        }

                    }//else



                }//while
                rs.close();
                pst.close();
            }//for
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con = null;
            }
        }

    }

    public void setContinName() {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(DatabaseProperties.CONNECTION_STRING, DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
            for (int i = 0; i < continNums.length; i++) {
                st = con.createStatement();
                rs = st.executeQuery("select CON_AlternateName from contin where CON_Number="
                        + continNums[i]);
                while (rs.next()) {
                    continNames[i] = rs.getString(1);

                    if (i == 0) {
                        continName = continNames[0];
                    } else {
                        if (!continNames[i].equals(continNames[i - 1])) {
                            continName = continName + "_" + continNames[i];
                        }
                    }

                    System.out.println(continNums[0]);
                    System.out.println(continName);

                }
                rs.close();
                st.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setDimension() {

        maxSection = 0;
        minSection = 0;


        scale = 1.0000;


        Connection con = null;
        PreparedStatement pstmt = null;
        String jsql = null, jsql2 = null;
        ResultSet rs = null;

// find out the max and min X and Y
        try {
            con = DriverManager.getConnection(DatabaseProperties.CONNECTION_STRING, DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);




            jsql = "select max(z1),min(z1) from display2 where ";
            for (int i = 0; i < continNums.length; i++) {


                if (i == continNums.length - 1) {
                    jsql = jsql + "continNum=" + continNums[i];
                } else {
                    jsql = jsql + "continNum=" + continNums[i] + " or ";
                }
            }//for
            //System.out.println(jsql);
            pstmt = con.prepareStatement(jsql);
            rs = pstmt.executeQuery();
            rs.next();
            maxSection = rs.getInt(1) + 1;
            minSection = rs.getInt(2);


            jsql = "select distinct series1 from display2 where ";
            for (int i = 0; i < continNums.length; i++) {


                if (i == continNums.length - 1) {
                    jsql = jsql + "continNum=" + continNums[i];
                } else {
                    jsql = jsql + "continNum=" + continNums[i] + " or ";
                }
            }//for
            //System.out.println(jsql);
            pstmt = con.prepareStatement(jsql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                series.add(rs.getString(1));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        System.out.println("printing");

        width = (int) zoom * (1024 - 100);
        height = (int) zoom * (768 - 200);

        zoomSection = (double) width / (double) (maxSection - minSection + minSection % 50 + (50 - maxSection % 50));

        zoomX = height / 10000.0000d;
        zoomY = height / 10000.0000d;
        System.out.println("zoomSection=" + zoomSection);
        System.out.println("zoomX=" + zoomX);
        System.out.println("zoomY=" + zoomY);

        g2.setPaint(Color.white);
        g2.fillRect(0, 0, (int) (1028 * zoom), (int) (768 * zoom));



        Composite origComposite = g2.getComposite();
        //show title
        Font font0 = new Font("SansSerif", Font.PLAIN, 30);
        Font font1 = new Font("SansSerif", Font.PLAIN, 11);
        Font font2 = new Font("SansSerif", Font.PLAIN, 18);



        g2.setColor(Color.magenta);
        g2.setFont(font1);
        g2.drawString("presynapse", (float) (50 * zoom), (float) (60 * zoom));

        g2.setColor(Color.red);
        g2.setFont(font1);
        g2.drawString("postsynapse", (float) (50 * zoom), (float) (80 * zoom));

        g2.setColor(Color.green);
        g2.setFont(font1);
        g2.drawString("gap junction", (float) (50 * zoom), (float) (100 * zoom));

        //show direction
        Line2D ll = new Line2D.Float();
        ll.setLine((double) (10 * zoom), (double) (100 * zoom), (double) (10 * zoom), (double) (600 * zoom));
        g2.setPaint(Color.blue);
        g2.draw(ll);

        g2.setColor(Color.BLUE);
        g2.setFont(font1);

        if (dtype == "Y_Z") {
            g2.drawString("Dorsal", (float) (10 * zoom), (float) (75 * zoom));
            g2.drawString("Ventral", (float) (10 * zoom), (float) (625 * zoom));
        } else {
            g2.drawString("Right", (float) (10 * zoom), (float) (75 * zoom));
            g2.drawString("Left", (float) (10 * zoom), (float) (625 * zoom));
        }


        g2.setPaint(Color.blue);
        g2.setFont(font1);

        g2.drawString("anterior", (float) (25 * zoom), (float) (height * 0.97));
        g2.drawString("posterior", (float) (950 * zoom), (float) (height * 0.97));



        Font font = new Font("SansSerif", Font.PLAIN, 10);

        // Draw the line.
        Line2D l = new Line2D.Float();
        Integer scaleNum = null;

        int scaleLength = (maxSection - minSection) / 50 + 2;
        for (int f = 0; f <= scaleLength + 1; f++) {
            double start = 50 * zoom;
            l.setLine((double) (start + f * 50 * zoomSection), (double) (height + 105), (double) (start + f * 50 * zoomSection), (double) (height + 100));
            g2.setPaint(Color.black);
            g2.draw(l);

            l.setLine((double) (start + f * (50 * zoomSection)), (double) (height + 105), (double) (start + (f + 1) * (50 * zoomSection)), (double) (height + 105));
            g2.setPaint(Color.black);
            g2.draw(l);
            if (f % 4 == 0) {
                g2.setFont(font);
                scaleNum = new Integer((int) Math.floor(minSection / 50) * 50 + f * 50);
                g2.drawString(scaleNum.toString(), (float) (start + f * (50 * zoomSection)), (float) (height + 95));
            }
        }



        Connection con = null;
        PreparedStatement pstmt = null;
        String jsql = null;
        ResultSet rs = null;

        Double x1, y1, z1, x2, y2, z2;
        Point2D.Double point1, point2;

        int cellbody1, cellbody2;
        int branch1, branch2;
        String remark1, remark2, objName1, objName2, series1, series2;


        try {
            con = DriverManager.getConnection(DatabaseProperties.CONNECTION_STRING, DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
            System.out.println(continNums[0]+"print");



            g2.setColor(Color.blue);
            g2.setFont(font0);
            g2.drawString(name, (float) (50 * zoom), (float) (30));
            g2.setFont(font2);
            g2.drawString(" in ", (float) (250 * zoom), (float) (30));





            for (int i = 0; i < series.size(); i++) {


                g2.setColor(getColor(con, series.get(i)));
                g2.drawString(series.get(i), (float) (300 * zoom + i * 60), (float) (30));

            }

            for (int i = 0; i < continNums.length; i++) {





                jsql = "select x1,y1,z1,remarks1,cellbody1,branch1,objName1,x2,y2,z2,remarks2,cellbody2,branch2,objName2,series1,series2 from display2 where continNum=" + continNums[i];
                pstmt = con.prepareStatement(jsql);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Pattern p = Pattern.compile("\\[.*\\]");
                    Matcher m = p.matcher("");



                    x1 = (double) (9000 - rs.getInt(1)) * zoomX + 100 * zoom;
                    y1 = (double) (rs.getInt(2)) * zoomY + 100 * zoom;
                    z1 = (rs.getInt(3) - minSection) * zoomSection + 50 * zoom;
                    objName1 = rs.getString(7);

                    if (rs.getString(4) == null) {
                        remark1 = "";
                    } else {
                        remark1 = rs.getString(4);
                        m.reset(remark1);
                        remark1 = m.replaceAll("");
                    }

                    cellbody1 = rs.getInt(5);
                    branch1 = rs.getInt(6);
                    series1 = rs.getString(15);
                    series2 = rs.getString(16);

                    g2.setColor(getColor(con, series1));





                    x2 = (double) (9000 - rs.getInt(8)) * zoomX + 100 * zoom;
                    y2 = (double) rs.getInt(9) * zoomY + 100 * zoom;
                    z2 = (rs.getInt(10) - minSection) * zoomSection + 50 * zoom;
                    objName2 = rs.getString(14);

                    if (rs.getString(11) == null) {
                        remark2 = "";
                    } else {
                        remark2 = rs.getString(11);
                        m.reset(remark2);
                        remark2 = m.replaceAll("");
                    }
                    cellbody2 = rs.getInt(12);
                    branch2 = rs.getInt(13);




                    if (dtype.equals("X_Z") ) {
                        point1 = new Point2D.Double(z1, x1);
                        point2 = new Point2D.Double(z2, x2);
                    } else {
                        point1 = new Point2D.Double(z1, y1);
                        point2 = new Point2D.Double(z2, y2);
                    }
                    if (syns.containsKey(objName1)) {
                        Synapse2 sy = (Synapse2) syns.get(objName1);
                        if (sy.getFlag() == 0 || isShow(sy.name)) {
                            //draw synapse
                            drawSyn(sy, g2, point1);
                            sy.setFlag(1);
                        }
                    }
                    if (syns.containsKey(objName2)) {
                        Synapse2 sy = (Synapse2) syns.get(objName2);
                        if (sy.getFlag() == 0 || isShow(sy.name)) {
                            //draw synapse
                            drawSyn(sy, g2, point2);
                            sy.setFlag(1);
                        }
                    }


                    //System.out.println(point1.x+"  "+point1.y);
                    //System.out.println(point2.x+"  "+point2.y);
                    g2.setStroke(new BasicStroke(1));

                    l.setLine(point1, point2);
                    g2.setComposite(origComposite);
                    if (!remark1.equals("") && !remark1.equals("null")
                            && remark1 != null) {
                        g2.setFont(font);
                        g2.drawString(remark1,
                                (float) (((Point2D.Double) point1).x),
                                (float) (((Point2D.Double) point1).y - 5));

                    }

                    if (!remark2.equals("")
                            && !remark2.equals("null")
                            && remark2 != null) {
                        g2.setFont(font);
                        g2.drawString(remark2,
                                (float) (point2.x),
                                (float) (point2.y - 5));
                        //System.out.println(remark2);
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
                    if (showBackbone==1) g2.draw(l);



                }// end of while

                rs.close();
                pstmt.close();
            }//for


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private boolean isShow(String p){
        if (showPartners==null) {
            return true;
        }else{

            for (String pp : showPartners){
                if(p.equalsIgnoreCase(pp)) return true;
            }

        }

        return false;
    }

    protected void drawSyn(Synapse2 sy, Graphics2D g2, Point2D p) {
        //draw synapse
        int compositeType = AlphaComposite.SRC_OVER;
        AlphaComposite transparentComposite = AlphaComposite.getInstance(compositeType, 0.4F);
        Composite origComposite = g2.getComposite();
        Font font = new Font("SansSerif", Font.PLAIN, 10);
        Line2D l = new Line2D.Float();


        if (sy.type.equals("out") && showOut == 1) {

            g2.setComposite(transparentComposite);
            g2.setStroke(new BasicStroke(sy.sections));

            l.setLine(p.getX(), p.getY(), p.getX(), p.getY() - step * zoom);
            g2.setPaint(Color.magenta);
            g2.draw(l);



            l.setLine(p.getX(), p.getY() - step * zoom, p.getX() - 5, p.getY() - step * zoom + 5);
            g2.setPaint(Color.magenta);
            g2.draw(l);

            l.setLine(p.getX(), p.getY() - step * zoom, p.getX() + 5, p.getY() - step * zoom + 5);
            g2.setPaint(Color.magenta);
            g2.draw(l);

            g2.setComposite(origComposite);
            g2.setPaint(Color.magenta);
            g2.setFont(font);
            String foreignContinName = sy.name;
            g2.setStroke(new BasicStroke(1));
            g2.drawString(foreignContinName, (float) p.getX(), (float) p.getY() - step * zoom - 5);

            step = step + 10;
            if (step == 50) {
                step = 10;
            }


        }
        if (sy.type.equals("in") && showIn == 1) {
            g2.setComposite(transparentComposite);
            g2.setStroke(new BasicStroke(sy.sections));
            l.setLine(p.getX(), p.getY(), p.getX(), p.getY() - step * zoom);
            g2.setPaint(Color.red);
            g2.draw(l);



            l.setLine(p.getX(), p.getY(), p.getX() - 5, p.getY() - 5);
            g2.setPaint(Color.red);
            g2.draw(l);

            l.setLine(p.getX(), p.getY(), p.getX() + 5, p.getY() - 5);
            g2.setPaint(Color.red);
            g2.draw(l);

            g2.setComposite(origComposite);
            g2.setPaint(Color.red);
            g2.setFont(font);
            String foreignContinName = sy.name;
            g2.setStroke(new BasicStroke(1));
            g2.drawString(foreignContinName, (float) p.getX(), (float) p.getY() - step * zoom - 5);
            step = step + 10;
            if (step == 50) {
                step = 10;
            }

        }
        if (sy.type.equals("e") && showElectrical == 1) {
            g2.setComposite(transparentComposite);
            g2.setStroke(new BasicStroke(sy.sections));

            l.setLine(p.getX(), p.getY(), p.getX(), p.getY() - step * zoom);
            g2.setPaint(Color.green);
            g2.draw(l);



            g2.setComposite(origComposite);
            g2.setPaint(Color.green);
            g2.setFont(font);
            String foreignContinName = sy.name;
            g2.setStroke(new BasicStroke(1));
            g2.drawString(foreignContinName, (float) p.getX(), (float) p.getY() - step * zoom - 5);
            step = step + 10;
            if (step == 50) {
                step = 10;
            }
        }
    }


}