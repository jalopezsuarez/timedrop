package io.timedrop.ui.components.selector;

import io.timedrop.domain.Project;
import io.timedrop.service.ProjectService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class ProjectSearchBox extends AbstractListModel<Object> implements ComboBoxModel<Object>, KeyListener, ItemListener, FocusListener, ModelSearchBox, ActionListener
{
	private static final long serialVersionUID = 791572948406283213L;

	private ArrayList<Object> data = new ArrayList<Object>();
	private Object selection;
	private JComboBox<Object> cb;
	private ComboBoxEditor cbe;

	private ProjectService projectService = null;
	private Project project = null;

	private SelectorAction selectorAction = null;

	public ProjectSearchBox(JComboBox<Object> jcb, Project project)
	{
		cb = jcb;
		cb.addFocusListener(this);

		cbe = jcb.getEditor();
		cbe.getEditorComponent().addKeyListener(this);
		cbe.getEditorComponent().addFocusListener(this);
		cbe.addActionListener(this);

		projectService = new ProjectService();
		this.project = project;
	}

	public void clearUI()
	{
		try
		{
			JTextField textField = (JTextField) cbe.getEditorComponent();
			textField.setText("");
			cb.setSelectedIndex(-1);
			cbe.setItem(null);
		}
		catch (Exception ex)
		{
		}
	}

	@Override
	public void updateModel()
	{
		try
		{
			project.setDescription(cbe.getItem().toString());
			data = projectService.fetchProjects(project);
			super.fireContentsChanged(this, 0, data.size());
		}
		catch (Exception ex)
		{
		}
		cb.hidePopup();
	}

	@Override
	public void updateModel(String in)
	{
		try
		{
			project.setDescription(in);
			data = projectService.fetchProjects(project);
			super.fireContentsChanged(this, 0, data.size());
		}
		catch (Exception ex)
		{
		}
		cb.hidePopup();
		if (data.size() > 0)
		{
			cb.showPopup();
			cb.setSelectedIndex(0);
		}
	}

	public int getSize()
	{
		return data.size();
	}

	public Object getElementAt(int index)
	{
		return data.get(index);
	}

	public void setSelectedItem(Object anItem)
	{
		selection = anItem;
	}

	public Object getSelectedItem()
	{
		return selection;
	}

	public void keyTyped(KeyEvent e)
	{
	}

	public void keyPressed(KeyEvent e)
	{
		Object str = cbe.getItem();
		JTextField jtf = (JTextField) cbe.getEditorComponent();
		int currPos = jtf.getCaretPosition();

		if (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED)
		{
			if (e.getKeyCode() != KeyEvent.VK_ENTER)
			{
				cbe.setItem(str);
				jtf.setCaretPosition(currPos);
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			if (selectorAction != null)
				selectorAction.onCancel();
		}
		else
		{
			updateModel(cbe.getItem().toString());
			cbe.setItem(str);
			jtf.setCaretPosition(currPos);
		}
	}

	public void keyReleased(KeyEvent e)
	{
	}

	public void itemStateChanged(ItemEvent e)
	{
		Object source = e.getItem();
		if (source instanceof Project && project != null)
		{
			Project item = (Project) source;
			project.setDescription(item.getDescription());
			project.setIdProject(item.getIdProject());
		}
		else if (source instanceof String)
		{
			project.setDescription((String) source);
			project.setIdProject(-1);
		}
		cbe.setItem(e.getItem().toString());
		cb.setSelectedItem(e.getItem());
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		updateModel();
	}

	@Override
	public void focusLost(FocusEvent e)
	{
	}

	public void setSelectorAction(SelectorAction selector)
	{
		this.selectorAction = selector;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (selectorAction != null)
			selectorAction.onSelection();
	}

}