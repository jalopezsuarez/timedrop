package io.timedrop.ui.components.table;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class Table extends JTable
{
	private static final long serialVersionUID = -1350656893711208356L;

	public Table(TableModel model)
	{
		super(model);

		setBorder(BorderFactory.createEmptyBorder());
		setShowGrid(false);
		setTableHeader(null);

		setRowMargin(0);
		getColumnModel().setColumnMargin(0);
		setIntercellSpacing(new Dimension(0, 0));

		setPreferredScrollableViewportSize(getPreferredSize());
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		setFillsViewportHeight(true);

		setColumnSelectionAllowed(true);
		setRowSelectionAllowed(true);
		setCellSelectionEnabled(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public TableCellRenderer getCellRenderer(int row, int column)
	{
		Object value = getValueAt(row, column);
		if (value != null)
		{
			return getDefaultRenderer(value.getClass());
		}
		return super.getCellRenderer(row, column);
	}
};