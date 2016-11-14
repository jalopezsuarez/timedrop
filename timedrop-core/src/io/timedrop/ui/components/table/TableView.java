package io.timedrop.ui.components.table;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class TableView extends JScrollPane
{
	private static final long serialVersionUID = 4040820615116789368L;

	public TableView(TableCellRenderer render)
	{
		super(new Table(new DefaultTableModel(0, 1)
		{
			private static final long serialVersionUID = 7449215644097277753L;

			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		}));

		Table table = (Table) getViewport().getView();
		table.setDefaultRenderer(Object.class, render);

		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setBorder(BorderFactory.createEmptyBorder());
	}

	public TableModel getTableModel()
	{
		Table table = (Table) getViewport().getView();
		return table.getModel();
	}
}
