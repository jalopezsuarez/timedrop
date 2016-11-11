package io.timedrop.ui.components.table;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class BasicTableView extends JScrollPane
{
	private static final long serialVersionUID = 4040820615116789368L;

	public BasicTableView(TableCellRenderer render)
	{
		super(new BasicTable(new DefaultTableModel(0, 1)
		{
			private static final long serialVersionUID = 7449215644097277753L;

			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		}));

		BasicTable table = (BasicTable) getViewport().getView();
		table.setDefaultRenderer(Object.class, render);

		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setBorder(BorderFactory.createEmptyBorder());
		setBackground(Color.red);
	}

	public TableModel getModel()
	{
		BasicTable table = (BasicTable) getViewport().getView();
		return table.getModel();
	}
}
