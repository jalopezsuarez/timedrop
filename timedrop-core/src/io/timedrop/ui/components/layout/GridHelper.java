package io.timedrop.ui.components.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class GridHelper
{
	private Container container;
	private GridBagConstraints constraints;

	public GridHelper(Container container)
	{
		this.container = container;
		container.setLayout(new GridBagLayout());

		constraints = new GridBagConstraints();
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.insets = new Insets(0, 0, 0, 0);
	}

	public GridBagConstraints constrains()
	{
		return this.constraints;
	}

	public void add(Component component, int x, int y)
	{
		constraints.gridx = x;
		constraints.gridy = y;
		container.add(component, constraints);

		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.insets = new Insets(0, 0, 0, 0);
	}

}
