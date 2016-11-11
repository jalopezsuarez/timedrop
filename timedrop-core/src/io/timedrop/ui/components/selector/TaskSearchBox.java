package io.timedrop.ui.components.selector;

import io.timedrop.domain.Task;
import io.timedrop.service.TaskService;

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

public class TaskSearchBox extends AbstractListModel<Object> implements ComboBoxModel<Object>, ModelSearchBox, KeyListener, ItemListener, FocusListener, ActionListener
{
	private static final long serialVersionUID = 791572948406283213L;

	private ArrayList<Object> data = new ArrayList<Object>();
	private Object selection;
	private JComboBox<Object> cb;
	private ComboBoxEditor cbe;

	private TaskService taskService = null;
	private Task object = null;

	private SelectorAction selectorAction = null;

	// ~ Methods
	// =======================================================

	public TaskSearchBox(JComboBox<Object> jcb, Task task)
	{
		cb = jcb;
		cb.addFocusListener(this);

		cbe = jcb.getEditor();
		cbe.getEditorComponent().addKeyListener(this);
		cbe.getEditorComponent().addFocusListener(this);
		cbe.addActionListener(this);

		taskService = new TaskService();
		object = task;
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

	// ~ Methods
	// =======================================================

	public int getSize()
	{
		return data.size();
	}

	public Object getElementAt(int index)
	{
		return data.get(index);
	}

	public Object getSelectedItem()
	{
		return selection;
	}

	public void setSelectedItem(Object anItem)
	{
		selection = anItem;
	}

	// ~ Methods
	// =======================================================

	@Override
	public void updateModel()
	{
		try
		{
			object.setDescription(cbe.getItem().toString());
			data = taskService.fetchTasks(object);
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
			object.setDescription(in);
			data = taskService.fetchTasks(object);
			super.fireContentsChanged(this, 0, data.size());
		}
		catch (Exception ex)
		{
		}
		cb.hidePopup();
		if (data.size() > 0)
			cb.showPopup();
	}

	// ~ Methods
	// =======================================================

	public void keyPressed(KeyEvent e)
	{
		if (e.getModifiers() <= 0)
		{
			Object source = cbe.getItem();
			JTextField textField = (JTextField) cbe.getEditorComponent();
			int currPos = textField.getCaretPosition();

			if (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED)
			{
				if (e.getKeyCode() != KeyEvent.VK_ENTER)
				{
					if (data != null && data.size() <= 0)
					{
						updateModel();
						cbe.setItem(source);
						textField.setCaretPosition(currPos);
					}
				}
			}
			else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			{
				if (selectorAction != null)
					selectorAction.onCancel();
			}
			else
			{
				String string = textField.getText() + e.getKeyChar();
				string = string.trim();
				updateModel(string);
				cbe.setItem(source);
				textField.setCaretPosition(currPos);
			}
		}
	}

	public void keyReleased(KeyEvent e)
	{
		if (e.getModifiers() <= 0)
		{
			Object source = cbe.getItem();
			JTextField textField = (JTextField) cbe.getEditorComponent();
			int currPos = textField.getCaretPosition();

			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
			{
				String string = textField.getText();
				string = string.trim();
				updateModel(string);
				cbe.setItem(source);
				textField.setCaretPosition(currPos);
			}
		}
	}

	public void keyTyped(KeyEvent e)
	{
	}

	// ~ Methods
	// =======================================================

	public void itemStateChanged(ItemEvent e)
	{
	}

	// ~ Methods
	// =======================================================

	@Override
	public void focusGained(FocusEvent e)
	{
	}

	@Override
	public void focusLost(FocusEvent e)
	{
	}

	// ~ Methods
	// =======================================================

	public void setSelectorAction(SelectorAction selector)
	{
		this.selectorAction = selector;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (data != null && data.size() > 0 && cb.getSelectedItem() instanceof Task && ((Task) cb.getSelectedItem()).getIdTask() > 0)
		{
			Task item = (Task) cb.getSelectedItem();
			object.setIdTask(item.getIdTask());
			object.setDescription(item.getDescription());
			object.setAnnotation(item.getAnnotation());
			object.setRecord(item.getRecord());
			object.setVersion(item.getVersion());
			object.setEstimation(item.getEstimation());
			object.setReestimate(item.getReestimate());
			object.setSummary(item.getSummary());

			updateModel(object.toString());
			cbe.setItem(object.toString());
			cb.setSelectedItem(object);
		}
		else if (data != null && data.size() <= 0 && cbe.getItem() instanceof String && ((String) cbe.getItem()).length() > 0)
		{
			object.setIdTask(0);
			object.setDescription(((String) cbe.getItem()).trim());
			object.setAnnotation("");
			object.setRecord(System.currentTimeMillis());
			object.setVersion(System.currentTimeMillis());
			object.setEstimation(0);
			object.setReestimate(0);
			object.setSummary(0);
		}

		if (selectorAction != null)
			selectorAction.onSelection();
	}

}