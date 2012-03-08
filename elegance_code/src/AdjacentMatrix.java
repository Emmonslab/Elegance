import java.io.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import java.sql.*;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

class AdjacentMatrix extends SwingWorker {
	String synType, left, top, size;

	String[] rows;
	String[] cols;
	int length;

	public static void main(String[] args) {
		String names = "";
		try {

			Connection con = EDatabase.borrowConnection(

			);
			String jsql;
			PreparedStatement pstmt;
			ResultSet rs;

			jsql = "select distinct CON_AlternateName from contin where CON_Remarks like 'OK%'  order by type,count desc";

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

		new AdjacentMatrix("chemical", names, names, "0");
		new AdjacentMatrix("electrical", names, names, "1");

	}

	public AdjacentMatrix(String synType, String left, String top, String size) {
		this.synType = synType;
		this.left = left;
		this.top = top;
		this.size = size;
		ELog.info("top:" + top);
		ELog.info("left:" + left);

	}

	public String[] getNameArrays(String name) {
		String[] nArray;
		try {

			Connection con = EDatabase.borrowConnection(

			);
			String jsql;
			PreparedStatement pstmt;
			ResultSet rs;
			if (name.equals("all")) {

				// jsql =
				// "select count(distinct CON_AlternateName) from contin where CON_AlternateName IS NOT NULL and CON_AlternateName<>''  and type='neuron' and !CON_AlternateName like 'unk%'  order by type,CON_AlternateName";
				jsql = "select distinct CON_AlternateName from contin where CON_Remarks like 'OK%'  order by type,count desc";
				pstmt = con.prepareStatement(jsql);
				rs = pstmt.executeQuery();

				rs.next();
				name = rs.getString(1);

				while (rs.next()) {
					name = name + "," + rs.getString(1);
				}

			}
			nArray = name.split(",");

			return nArray;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private void writeCSV() {
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

			for (int i = 0; i < rows.length; i++) {
				fileWriter.print(rows[i] + ",");
				for (int j = 0; j < cols.length; j++) {
					int num = getNumber(rows[i], cols[j], synType);
					if (num > 0) {
						fileWriter.print(num + ",");
					} else {
						fileWriter.print(",");
					}
				}
				fileWriter.print("\n");

			}

			fileWriter.close();
			// JOptionPane.showMessageDialog(null,
			// synType+" adjacent Matrix: generated and saved to Elegance home directory");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failure");
		}

	}

	public int getNumber(String pre, String post, String type) {

		try {

			Connection con = EDatabase.borrowConnection(

			);
			String jsql;
			PreparedStatement pstmt;
			ResultSet rs;

			if (synType.equals("chemical")) {
				jsql = "select sum(sections) from synapsenomultiple where ( pre=? or pre=? ) and ( post=? or post=? ) and type='chemical'and sections>"
						+ length;
				pstmt = con.prepareStatement(jsql);
				pstmt.setString(1, pre);
				pstmt.setString(2, "[" + pre + "]");
				pstmt.setString(3, post);
				pstmt.setString(4, "[" + post + "]");
			} else {
				jsql = "select sum(sections) from synapsenomultiple where ((( pre=? or pre=? ) and ( post=? or post=? )) or (( pre=? or pre=? ) and ( post=? or post=? ))) and type='electrical' and sections>"
						+ length;
				pstmt = con.prepareStatement(jsql);
				pstmt.setString(1, pre);
				pstmt.setString(2, "[" + pre + "]");
				pstmt.setString(3, post);
				pstmt.setString(4, "[" + post + "]");

				pstmt.setString(5, post);
				pstmt.setString(6, "[" + post + "]");
				pstmt.setString(7, pre);
				pstmt.setString(8, "[" + pre + "]");

			}
			rs = pstmt.executeQuery();
			if (rs.next()) {

				int rr = rs.getInt(1);
				rs.close();
				pstmt.close();
				return rr;
			} else {
				rs.close();
				pstmt.close();
				return 0;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
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
				setProgress(1);
				rows = getNameArrays(left);
				setProgress(40);
				cols = getNameArrays(top);
				setProgress(80);
				length = Integer.parseInt(size);
				ELog.info("rows: " + left);
				ELog.info("columns: " + top);
				writeCSV();
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