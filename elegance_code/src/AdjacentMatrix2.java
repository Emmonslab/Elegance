import java.io.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import java.sql.*;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

class AdjacentMatrix2 extends SwingWorker {
	String synType, left, top, size;
	

	String[] rows;
	String[] cols;
	int length;

	public static void main(String[] args) {
		String names = "PHAL,PHAR,PHBL,PHBR,PHCL,PHCR,R1AL,R1AR,R1BL,R1BR,R2AL,R2AR,R2BL,R2BR,R3AL,R3AR,R3BL,R3BR,R4AL,R4AR,"
				+ "R4BL,R4BR,R5AL,R5AR,R5BL,R5BR,R6AL,R6AR,R6BL,R6BR,R7AL,R7AR,R7BL,R7BR,R8AL,R8AR,R8BL,R8BR,R9AL,R9AR,R9BL,"
				+ "R9BR,HOA,HOB,PCAL,PCAR,PCBL,PCBR,PCCL,PCCR,SPCL,SPCR,SPDL,SPDR,SPVL,SPVR,ALNL,ALNR,PLML,PLMR,PLNL,PLNR,"
				+ "PQR,PVM,PVDL,PVDR,PDEL,PDER,PVR,AVG,PVV,PVY,AVAL,AVAR,AVBL,AVBR,AVDL,AVDR,PVCL,PVCR,AN1a,AN1b,AN2a,AN2b,"
				+ "AN3a,AN3b,AVKL,AVKR,AVL,CP07,CP08,CP09,PDA,PDB,PDC,PVX,DX1,DX2,DX3,EF1,EF2,EF3,DVA,DVB,DVC,DVE,DVF,LUAL,"
				+ "LUAR,CA02,CA03,CA04,CA05,CA06,CA07,CA08,CA09,CP01,CP02,CP03,CP04,CP05,CP06,PGA,PVNL,PVNR,PVQL,PVQR,PVS,"
				+ "PVT,PVU,PVWL,PVWR,PVZ,DA04,DB03,VB04,VB05,DB04,VB06,DD03,DB05,VB07,DA05,VB08,DD04,VA08,VB09,DB06,AS08,"
				+ "VD09,AS09,DA06,VA09,VB10,DD05,VD10,VA10,VB11,DB07,AS10,DA07,VD11,VA11,AS11,DA08,DD06,DA09,VA12,VD12,VD13,"
				+ "dBWML24,dBWML23,dBWML22,dBWMR24,dBWMR23,vBWML23,vBWML22,vBWML21,vBWML20,vBWML19,vBWML17,vBWMR24,vBWMR23,"
				+ "vBWMR22,vBWMR21,vBWMR20,vBWMR19,cdlL,cdlR,dglL7,dglL6,dglL5,dglL4,dglL3,dglL2,dglL1,dglR8,dglR7,dglR6,"
				+ "dglR5,dglR4,dglR3,dglR2,dglR1,polL,polR,gecL,gecR,grtL,grtR,aobL,aobR,pobL,pobR,adp,dspL,dspR,dsrL,dsrR,"
				+ "vspL,vspR,vsrL,vsrR,ailL,ailR,pilL,pilR,intL,intR,sph,gonad,hyp";
		try {

			Connection con = EDatabase.borrowConnection(

			);
			String jsql;
			PreparedStatement pstmt;
			ResultSet rs;

			jsql = "select distinct CON_AlternateName from contin where  order by type,count desc";

			pstmt = con.prepareStatement(jsql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				names = rs.getString(1);
			}

			while (rs.next()) {

				names = names + "," + rs.getString(1);

			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		new ProgressBarDemo(new AdjacentMatrix2("chemical", names, names, "0")).launch();
		// new ProgressBarDemo(new
		// AdjacentMatrix2("electrical",names,names,"1")).launch();

	}

	public AdjacentMatrix2(String synType, String left, String top, String size) {
		this.synType = synType;
		this.left = left;
		this.top = top;
		this.size = size;
		Connection con=null;
		try {

			con = EDatabase.borrowConnection(

			);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			EDatabase.returnConnection(con);
		}

	}

	public String[] getNameArrays(String name) {
		String[] nArray;
		Connection con=null;
		try {

			con = EDatabase.borrowConnection();

			String jsql;
			PreparedStatement pstmt;
			ResultSet rs;
			if (name.equals("all")) {

				// jsql =
				// "select count(distinct CON_AlternateName) from contin where CON_AlternateName IS NOT NULL and CON_AlternateName<>''  and type='neuron' and !CON_AlternateName like 'unk%'  order by type,CON_AlternateName";
				jsql = "select distinct CON_AlternateName from contin where CON_Remarks like 'OK%'  order by type,count desc";
				//jsql = "select distinct CON_AlternateName from contin order by type,count desc";
				pstmt = con.prepareStatement(jsql);
				rs = pstmt.executeQuery();

				if (rs.next()) {
					name = rs.getString(1);

					while (rs.next()) {
						name = name + "," + rs.getString(1);
					}
				}

			}
			nArray = name.split(",");

			return nArray;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			EDatabase.returnConnection(con);
		}

	}

	public int getChemicalNumber(String pre, String post, PreparedStatement pstmt) {

		try {

			pstmt.setString(1, pre);
			pstmt.setString(2, "[" + pre + "]");
			pstmt.setString(3, post);
			pstmt.setString(4, "[" + post + "]");

			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {

				int rr = rs.getInt(1);
				
				rs.close();

				return rr;
			} else {
				
				rs.close();

				return 0;
			}

		} catch (Exception e) {

			e.printStackTrace();
			return 0;
		}

	}
	
	public int getElectricalNumber(String pre, String post,PreparedStatement pstmt) {

		try {

			
			pstmt.setString(1, pre);
			pstmt.setString(2, "[" + pre + "]");
			pstmt.setString(3, post);
			pstmt.setString(4, "[" + post + "]");

			pstmt.setString(5, post);
			pstmt.setString(6, "[" + post + "]");
			pstmt.setString(7, pre);
			pstmt.setString(8, "[" + pre + "]");

			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {

				int rr = rs.getInt(1);
				
				rs.close();

				return rr;
			} else {
				
				rs.close();

				return 0;
			}

		} catch (Exception e) {

			e.printStackTrace();
			return 0;
		}

	}
	


	@Override
	protected Object doInBackground() {

		if ((left != null) && (top != null)) {
			if ((!("".equals(top.trim()))) && (!("".equals(left.trim())))) {
				long time1 = System.currentTimeMillis();
				this.synType = synType;

				rows = getNameArrays(left);

				cols = getNameArrays(top);

				length = Integer.parseInt(size);
				ELog.info("rows: " + left);
				ELog.info("columns: " + top);
				
				Connection con=null;
				try {

					DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
					String datetime = dateFormat.format(new Date());
					String file = "matrix_" + synType + "_" + datetime + ".csv";

					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
					PrintWriter fileWriter = new PrintWriter(bufferedWriter);

					// print first row
					fileWriter.print(" ,");
					for (int i = 0; i < cols.length; i++) {
						fileWriter.print(cols[i] + ",");
					}

					fileWriter.print("\n");

					// print following rows

					con=EDatabase.borrowConnection();
					
					PreparedStatement chemicalPst=con.prepareStatement("select sum(sections) from synapsenomultiple where ( pre=? or pre=? ) and ( post=? or post=? ) and type='chemical' and sections>"+ length);
					
					PreparedStatement electricalPst=con.prepareStatement("select sum(sections) from synapsenomultiple where ((( pre=? or pre=? ) and ( post=? or post=? )) or (( pre=? or pre=? ) and ( post=? or post=? ))) and type='electrical' and sections>"+ length);
					
					for (int i = 0; i < rows.length; i++) {
						fileWriter.print(rows[i] + ",");
						for (int j = 0; j < cols.length; j++) {
							int num = synType.equals("chemical")?getChemicalNumber(rows[i], cols[j],chemicalPst):getElectricalNumber(rows[i], cols[j],electricalPst);
							if (num > 0) {
								fileWriter.print(num + ",");
							} else {
								fileWriter.print(",");
							}
							if (j % 500 == 0) ELog.info(j+" out of "+cols.length+" cols "+i+" out of "+rows.length+" rows");
						}
						fileWriter.print("\n");
						fileWriter.flush();
						setProgress(i * 100 / rows.length);
						
					}

					fileWriter.close();
					// JOptionPane.showMessageDialog(null,
					// synType+" adjacent Matrix: generated and saved to Elegance home directory");
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Failure");
				} finally {
					EDatabase.returnConnection(con);
				}

				setProgress(100);
				long time2 = System.currentTimeMillis();
				long time = (time2 - time1) / 1000;

				JOptionPane.showMessageDialog(null, time + " seconds, adjacency matrix saved in Elegance Home Directory. Done!", "Calculation",
						JOptionPane.INFORMATION_MESSAGE);

			}
		}
		return null;
	}

}