package Admin;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;
import javax.swing.*;
import javax.swing.table.*;
 
public class blink extends JFrame
{
	ColorRenderer colorRenderer;
 
	public blink()
	{
		String[] columnNames = {"Date", "String", "Integer", "Boolean"};
		Object[][] data =
		{
			{new Date(), "A", new Integer(1), new Boolean(true)},
			{new Date(), "B", new Integer(2), new Boolean(false)},
			{new Date(), "C", new Integer(3), new Boolean(true)},
			{new Date(), "D", new Integer(4), new Boolean(false)}
		};
 
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		JTable table = new JTable( model )
		{
			public Class getColumnClass(int column)
			{
				return getValueAt(0, column).getClass();
			}
 
			//  By overriding this method we can use a single renderer for every column
 
			public Component 
				prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);
				colorRenderer.setBackground(c, row, column);
				return c;
			}
		};
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		JScrollPane scrollPane = new JScrollPane( table );
		getContentPane().add( scrollPane );
 
		//  Create blinking color renderer
 
		colorRenderer = new ColorRenderer( table );
		colorRenderer.setCellColor(1, 1, Color.RED);
		colorRenderer.setRowColor(1, Color.GREEN);
		colorRenderer.setColumnColor(1, Color.BLUE);
		colorRenderer.startBlinking(1000);
	}
 
	public static void main(String[] args)
	{
		blink frame = new blink();
		frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
		frame.pack();
		frame.setVisible(true);
	}
 
	/*
	**  Color cell background
	*/
	class ColorRenderer implements ActionListener
	{
		private JTable table;
		private AbstractTableModel model;
		private Map colors;
		private boolean isBlinking = true;
		private Timer timer;
		private Point location;
 
		public ColorRenderer(JTable table)
		{
			this.table = table;
			model = (AbstractTableModel)table.getModel();
			colors = new HashMap();
			location = new Point();
		}
 
		public void setBackground(Component c, int row, int column)
		{
			//  Don't override the background color of a selected cell
 
			if ( table.isCellSelected(row, column) ) return;
 
			//  The default render does not reset the background color
			//  that was set for the previous cell, so reset it here
 
			if (c instanceof DefaultTableCellRenderer)
			{
				c.setBackground( table.getBackground() );
			}
 
			//  Don't highlight this time
 
			if ( !isBlinking ) return;
 
			//  In case columns have been reordered, convert the column number
 
			column = table.convertColumnIndexToModel(column);
 
			//  Get cell color
 
			Object key = getKey(row, column);
			Object o = colors.get( key );
 
			if (o != null)
			{
				c.setBackground( (Color)o );
				return;
			}
 
			//  Get row color
 
			key = getKey(row, -1);
			o = colors.get( key );
 
			if (o != null)
			{
				c.setBackground( (Color)o );
				return;
			}
 
			//  Get column color
 
			key = getKey(-1, column);
			o = colors.get( key );
 
			if (o != null)
			{
				c.setBackground( (Color)o );
				return;
			}
 
		}
 
		public void setCellColor(int row, int column, Color color)
		{
			Point key = new Point(row, column);
			colors.put(key, color);
		}
 
		public void setColumnColor(int column, Color color)
		{
			setCellColor(-1, column, color);
		}
 
		public void setRowColor(int row, Color color)
		{
			setCellColor(row, -1, color);
		}
 
		private Object getKey(int row, int column)
		{
			location.x = row;
			location.y = column;
			return location;
		}
 
		public void startBlinking(int interval)
		{
			timer = new Timer(interval, this);
			timer.start();
		}
 
		public void stopBlinking()
		{
			timer.stop();
		}
 
		public void actionPerformed(ActionEvent e)
		{
			isBlinking = !isBlinking;
 
			Iterator it = colors.keySet().iterator();
 
			while ( it.hasNext() )
			{
				Point key = (Point)it.next();
				int row = key.x;
				int column = key.y;
 
				if (column == -1)
				{
					model.fireTableRowsUpdated(row, row);
				}
				else if (row == -1)
				{
					int rows = table.getRowCount();
 
					for (int i = 0; i < rows; i++)
					{
						model.fireTableCellUpdated(i, column);
					}
				}
				else
				{
					model.fireTableCellUpdated(row, column);
				}
			}
		}
	}
}
