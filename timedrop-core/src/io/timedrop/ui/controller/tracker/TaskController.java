package io.timedrop.ui.controller.tracker;

import io.timedrop.business.TrackerInterface;
import io.timedrop.business.TrackerManager;
import io.timedrop.business.report.ReportManager;
import io.timedrop.commons.GridHelper;
import io.timedrop.domain.Organization;
import io.timedrop.domain.Session;
import io.timedrop.domain.Task;
import io.timedrop.service.SessionService;
import io.timedrop.service.TaskService;
import io.timedrop.ui.components.BasicEditor;
import io.timedrop.ui.components.BasicField;
import io.timedrop.ui.components.BasicImage;
import io.timedrop.ui.components.BasicFrame;
import io.timedrop.ui.components.BasicLabel;
import io.timedrop.ui.components.BasicPanel;
import io.timedrop.ui.components.BasicText;
import io.timedrop.ui.components.selector.DarkComboBox;
import io.timedrop.ui.components.selector.LightComboBox;
import io.timedrop.ui.components.selector.ProjectSearchBox;
import io.timedrop.ui.components.selector.SelectorAction;
import io.timedrop.ui.components.selector.TaskSearchBox;
import io.timedrop.ui.components.table.BasicTableView;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class TaskController extends BasicFrame implements TrackerInterface
{
	private static final long serialVersionUID = 7847828219876847244L;

	// =======================================================

	private BasicFrame context;

	private TrackerManager trackerManager;

	private ProjectSearchBox selectorTrackerProject;
	private TaskSearchBox selectorTrackerTask;

	private ProjectSearchBox selectorBreakProject;
	private TaskSearchBox selectorBreakTask;

	private Task trackerTask;
	private long trackerEstimation;
	private Task breakTask;

	private Timer timerTrackerTimerBlink;

	// =======================================================

	private BasicPanel panelTracker;

	// -------------------------------------------------------

	private BasicPanel panelTrackerEditor;

	private BasicPanel panelTrackerEditorProject;
	private BasicPanel panelTrackerEditorTask;
	private BasicPanel panelTrackerEditorTimer;
	private BasicPanel panelTrackerEditorEstimator;

	// -------------------------------------------------------

	private BasicLabel labelTrackerProjectTitle;
	private DarkComboBox<Object> comboTrackerProject;

	// -------------------------------------------------------

	private BasicLabel labelTrackerTaskTitle;
	private DarkComboBox<Object> comboTrackerTask;

	// -------------------------------------------------------

	private BasicLabel labelEstimationTitle;

	private BasicImage buttonEstimationIncrease;
	private BasicImage buttonEstimationDecrease;
	private BasicLabel valueEstimationTask;

	private BasicLabel labelEstimationSpent;
	private BasicLabel valueEstimationSpent;

	private BasicLabel labelEstimationRemaining;
	private BasicLabel valueEstimationRemaining;

	private BasicLabel labelEstimationInitial;
	private BasicLabel valueEstimationInitial;

	private BasicLabel labelEstimationCurrent;
	private BasicLabel valueEstimationCurrent;

	// -------------------------------------------------------

	private BasicPanel panelTrackerManager;

	private BasicText textTrackerTimerTask;
	private BasicField fieldTackerTimerProject;

	private BasicImage buttonTrackerTimerIncrease;
	private BasicImage buttonTrackerTimerDecrease;

	private BasicLabel valueTrackerTimer;

	private BasicLabel labelTrackerTimerStarted;
	private BasicLabel valueTrackerTimerStarted;

	private BasicLabel labelTrackerTimerRemaining;
	private BasicLabel valueTrackerTimerRemaining;

	// -------------------------------------------------------

	private BasicPanel panelTrackerAnnotation;

	private BasicImage buttonTrackerAnnotation;
	private BasicLabel labelTrackerAnnotation;
	private BasicEditor editorTrackerAnnotation;

	// -------------------------------------------------------

	private BasicPanel panelBreakManager;
	private BasicPanel panelBreakEmpty;

	private BasicPanel panelBreakTracker;
	private BasicPanel panelBreakTrackerEditor;

	private BasicPanel panelBreakTrackerProject;
	private BasicPanel panelBreakTrackerTask;
	private BasicPanel panelBreakTrackerReason;
	private BasicPanel panelBreakTrackerTimer;

	// -------------------------------------------------------

	private BasicLabel labelBreakEmptyTitle;
	private BasicImage buttonBreakEmptyHelper;
	private BasicLabel labelBreakEmptyHelper;

	// -------------------------------------------------------

	private BasicLabel labelBreakEditorProject;
	private LightComboBox<Object> comboBreakEditorProject;

	private BasicLabel labelBreakEditorTask;
	private LightComboBox<Object> comboBreakEditorTask;

	private BasicLabel labelBreakEditorReason;
	private BasicEditor editorBreakEditorReason;

	// -------------------------------------------------------

	private BasicLabel labelBreakTrackerTitle;
	private BasicImage buttonBreakTrackerIncrease;
	private BasicImage buttonBreakTrackerDecrease;
	private BasicImage buttonBreakTrackerCommit;
	private BasicImage buttonBreakTrackerCancel;

	private BasicLabel valueBreakTrackerTimer;

	// -------------------------------------------------------

	private BasicPanel panelBreakList;
	private BasicTableView tableTaskInterruptions;

	// ~ Methods
	// =======================================================

	public TaskController(TrackerManager tracker)
	{
		super();

		// -------------------------------------------------------

		context = this;

		// -------------------------------------------------------

		trackerManager = tracker;
		trackerManager.setUI(this);
		{
			trackerTask = new Task();
			Organization organization = new Organization();
			organization.setIdOrganization(1L);
			trackerTask.getProject().setOrganization(organization);
			trackerEstimation = 0;
		}
		{
			breakTask = new Task();

			Organization organization = new Organization();
			organization.setIdOrganization(1L);
			breakTask.getProject().setOrganization(organization);
		}

		view();
		events();

		// -------------------------------------------------------

		trackerManager.updateUI();
	}

	// ~ Methods
	// =======================================================

	public void view()
	{
		GridHelper layout = new GridHelper(getContentPane());
		{
			panelTracker = new BasicPanel();
			panelTracker.setLayout(new BorderLayout());
			panelTracker.setBackground(Color.decode("#212B33"));
			{
				panelTrackerEditor = new BasicPanel();
				panelTrackerEditor.setLayout(new CardLayout());
				{
					panelTrackerEditorProject = new BasicPanel();
					panelTrackerEditorProject.setBackground(Color.decode("#212B33"));
					panelTrackerEditorProject.setLayout(new BorderLayout());
					{
						labelTrackerProjectTitle = new BasicLabel("PROJECT");
						labelTrackerProjectTitle.setFont(UIManager.getFont("Label.font").deriveFont(12f));
						labelTrackerProjectTitle.setForeground(Color.decode("#525B61"));
						labelTrackerProjectTitle.setBackground(Color.decode("#212B33"));
						labelTrackerProjectTitle.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
						panelTrackerEditorProject.add(labelTrackerProjectTitle, BorderLayout.NORTH);
					}
					{
						comboTrackerProject = new DarkComboBox<Object>();
						selectorTrackerProject = new ProjectSearchBox(comboTrackerProject, trackerTask.getProject());
						comboTrackerProject.setModel(selectorTrackerProject);
						comboTrackerProject.addItemListener(selectorTrackerProject);
						panelTrackerEditorProject.add(comboTrackerProject, BorderLayout.CENTER);
					}
					panelTrackerEditor.add(panelTrackerEditorProject);
				}
				{
					panelTrackerEditorTask = new BasicPanel();
					panelTrackerEditorTask.setBackground(Color.decode("#212B33"));
					panelTrackerEditorTask.setLayout(new BorderLayout());
					{
						labelTrackerTaskTitle = new BasicLabel("TASK");
						labelTrackerTaskTitle.setFont(UIManager.getFont("Label.font").deriveFont(12f));
						labelTrackerTaskTitle.setForeground(Color.decode("#525B61"));
						labelTrackerTaskTitle.setBackground(Color.decode("#212B33"));
						labelTrackerTaskTitle.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
						panelTrackerEditorTask.add(labelTrackerTaskTitle, BorderLayout.NORTH);
					}
					{
						comboTrackerTask = new DarkComboBox<Object>();
						selectorTrackerTask = new TaskSearchBox(comboTrackerTask, trackerTask);
						comboTrackerTask.setModel(selectorTrackerTask);
						comboTrackerTask.addItemListener(selectorTrackerTask);
						panelTrackerEditorTask.add(comboTrackerTask, BorderLayout.CENTER);
					}
					panelTrackerEditor.add(panelTrackerEditorTask);
				}
				{
					panelTrackerEditorTimer = new BasicPanel();
					panelTrackerEditorTimer.setBackground(Color.decode("#212B33"));
					panelTrackerEditorTimer.setLayout(new BorderLayout());
					{
						labelEstimationTitle = new BasicLabel("ESTIMATED HOURS");
						labelEstimationTitle.setFont(UIManager.getFont("Label.font").deriveFont(12f));
						labelEstimationTitle.setForeground(Color.decode("#525B61"));
						labelEstimationTitle.setBackground(Color.decode("#212B33"));
						labelEstimationTitle.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
						panelTrackerEditorTimer.add(labelEstimationTitle, BorderLayout.NORTH);
					}
					{
						panelTrackerEditorEstimator = new BasicPanel();
						panelTrackerEditorEstimator.setBackground(Color.decode("#161F26"));
						panelTrackerEditorEstimator.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode("#525B61")));

						GridHelper layoutTrackerEditorEstimation = new GridHelper(panelTrackerEditorEstimator);
						{
							buttonEstimationDecrease = new BasicImage("estimate_decrease");
							layoutTrackerEditorEstimation.constrains().gridheight = 2;
							layoutTrackerEditorEstimation.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerEditorEstimation.constrains().insets = new Insets(0, 10, 0, 5);
							layoutTrackerEditorEstimation.add(buttonEstimationDecrease, 0, 1);
						}
						{
							buttonEstimationIncrease = new BasicImage("estimate_increase");
							layoutTrackerEditorEstimation.constrains().gridheight = 2;
							layoutTrackerEditorEstimation.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerEditorEstimation.constrains().insets = new Insets(0, 5, 0, 5);
							layoutTrackerEditorEstimation.add(buttonEstimationIncrease, 1, 1);
						}
						{
							valueEstimationTask = new BasicLabel("--h");
							valueEstimationTask.setFont(UIManager.getFont("TextField.font").deriveFont(32f));
							valueEstimationTask.setForeground(Color.decode("#79ADD7"));
							valueEstimationTask.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().gridheight = 2;
							layoutTrackerEditorEstimation.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerEditorEstimation.constrains().insets = new Insets(0, 5, 0, 10);
							layoutTrackerEditorEstimation.add(valueEstimationTask, 2, 1);
						}
						{
							labelEstimationSpent = new BasicLabel("SPENT HOURS");
							labelEstimationSpent.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelEstimationSpent.setForeground(Color.decode("#A6AEB4"));
							labelEstimationSpent.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().ipadx = 28;
							layoutTrackerEditorEstimation.constrains().insets = new Insets(5, 0, 0, 0);
							layoutTrackerEditorEstimation.add(labelEstimationSpent, 3, 1);
						}
						{
							valueEstimationSpent = new BasicLabel("--");
							valueEstimationSpent.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							valueEstimationSpent.setForeground(Color.decode("#ffffff"));
							valueEstimationSpent.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().insets = new Insets(5, 0, 0, 0);
							layoutTrackerEditorEstimation.add(valueEstimationSpent, 4, 1);
						}
						{
							labelEstimationRemaining = new BasicLabel("REMAINING");
							labelEstimationRemaining.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelEstimationRemaining.setForeground(Color.decode("#A6AEB4"));
							labelEstimationRemaining.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().ipadx = 28;
							layoutTrackerEditorEstimation.constrains().insets = new Insets(2, 0, 0, 0);
							layoutTrackerEditorEstimation.add(labelEstimationRemaining, 3, 2);
						}
						{
							valueEstimationRemaining = new BasicLabel("--");
							valueEstimationRemaining.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							valueEstimationRemaining.setForeground(Color.decode("#ffffff"));
							valueEstimationRemaining.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().insets = new Insets(2, 0, 0, 0);
							layoutTrackerEditorEstimation.add(valueEstimationRemaining, 4, 2);
						}
						{
							labelEstimationInitial = new BasicLabel("INITIAL");
							labelEstimationInitial.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelEstimationInitial.setForeground(Color.decode("#A6AEB4"));
							labelEstimationInitial.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().ipadx = 28;
							layoutTrackerEditorEstimation.constrains().insets = new Insets(4, 0, 0, 0);
							layoutTrackerEditorEstimation.add(labelEstimationInitial, 5, 1);
						}
						{
							valueEstimationInitial = new BasicLabel("--");
							valueEstimationInitial.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							valueEstimationInitial.setForeground(Color.decode("#ffffff"));
							valueEstimationInitial.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().insets = new Insets(4, 0, 0, 0);
							layoutTrackerEditorEstimation.add(valueEstimationInitial, 6, 1);
						}
						{
							labelEstimationCurrent = new BasicLabel("CURRENT");
							labelEstimationCurrent.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelEstimationCurrent.setForeground(Color.decode("#A6AEB4"));
							labelEstimationCurrent.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().ipadx = 28;
							layoutTrackerEditorEstimation.constrains().insets = new Insets(2, 0, 0, 0);
							layoutTrackerEditorEstimation.add(labelEstimationCurrent, 5, 2);
						}
						{
							valueEstimationCurrent = new BasicLabel("--");
							valueEstimationCurrent.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							valueEstimationCurrent.setForeground(Color.decode("#ffffff"));
							valueEstimationCurrent.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().insets = new Insets(2, 0, 0, 0);
							layoutTrackerEditorEstimation.add(valueEstimationCurrent, 6, 2);
						}
						{
							layoutTrackerEditorEstimation.constrains().gridheight = 2;
							layoutTrackerEditorEstimation.constrains().gridwidth = 1;
							layoutTrackerEditorEstimation.constrains().weightx = 1.0;
							layoutTrackerEditorEstimation.constrains().weighty = 1.0;
							layoutTrackerEditorEstimation.constrains().fill = GridBagConstraints.BOTH;
							layoutTrackerEditorEstimation.add(new BasicLabel(), 7, 1);
						}
						panelTrackerEditorTimer.add(panelTrackerEditorEstimator, BorderLayout.CENTER);
					}
					panelTrackerEditor.add(panelTrackerEditorTimer);
				}
				panelTracker.add(panelTrackerEditor, BorderLayout.NORTH);
			}
			{
				BasicPanel panelTrackerSection = new BasicPanel();
				panelTrackerSection.setLayout(new CardLayout());
				{
					panelTrackerManager = new BasicPanel();
					panelTrackerManager.setBackground(Color.decode("#212B33"));
					GridHelper layoutTrackerManager = new GridHelper(panelTrackerManager);
					{
						BasicPanel panelTrackerManagerInformation = new BasicPanel();
						panelTrackerManagerInformation.setBackground(Color.decode("#212B33"));
						panelTrackerManagerInformation.setLayout(new BorderLayout());
						{
							textTrackerTimerTask = new BasicText("No task");
							textTrackerTimerTask.setFont(UIManager.getFont("TextField.font").deriveFont(20f));
							textTrackerTimerTask.setBackground(Color.decode("#212B33"));
							textTrackerTimerTask.setForeground(Color.decode("#ffffff"));
							panelTrackerManagerInformation.add(textTrackerTimerTask, BorderLayout.NORTH);
						}
						{
							fieldTackerTimerProject = new BasicField("Start tracking by selecting the task you are working on");
							fieldTackerTimerProject.setBackground(Color.decode("#212B33"));
							fieldTackerTimerProject.setForeground(Color.decode("#A6AEB4"));
							fieldTackerTimerProject.setFont(UIManager.getFont("Label.font").deriveFont(14f));
							panelTrackerManagerInformation.add(fieldTackerTimerProject, BorderLayout.CENTER);
						}
						layoutTrackerManager.constrains().gridheight = 2;
						layoutTrackerManager.constrains().gridwidth = 1;
						layoutTrackerManager.constrains().weightx = 1.0;
						layoutTrackerManager.constrains().insets = new Insets(12, 12, 12, 12);
						layoutTrackerManager.add(panelTrackerManagerInformation, 0, 0);
					}
					{
						BasicPanel panelTrackerManagerTimer = new BasicPanel();
						panelTrackerManagerTimer.setBackground(Color.decode("#212B33"));
						GridHelper layoutTrackerManagerTimer = new GridHelper(panelTrackerManagerTimer);
						{
							buttonTrackerTimerDecrease = new BasicImage("tracking_decrease");
							buttonTrackerTimerDecrease.setBackground(Color.decode("#212B33"));
							layoutTrackerManagerTimer.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerManagerTimer.constrains().insets = new Insets(4, 0, 0, 10);
							layoutTrackerManagerTimer.constrains().fill = GridBagConstraints.HORIZONTAL;
							layoutTrackerManagerTimer.add(buttonTrackerTimerDecrease, 0, 0);
						}
						{
							buttonTrackerTimerIncrease = new BasicImage("tracking_increase");
							buttonTrackerTimerIncrease.setBackground(Color.decode("#212B33"));
							layoutTrackerManagerTimer.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerManagerTimer.constrains().insets = new Insets(4, 0, 0, 10);
							layoutTrackerManagerTimer.constrains().fill = GridBagConstraints.HORIZONTAL;
							layoutTrackerManagerTimer.add(buttonTrackerTimerIncrease, 1, 0);
						}
						{
							valueTrackerTimer = new BasicLabel("--:--");
							valueTrackerTimer.setFont(UIManager.getFont("TextField.font").deriveFont(32f));
							valueTrackerTimer.setForeground(Color.decode("#ffffff"));
							valueTrackerTimer.setBackground(Color.decode("#212B33"));
							layoutTrackerManagerTimer.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerManagerTimer.constrains().insets = new Insets(4, 0, 0, 8);
							layoutTrackerManagerTimer.constrains().fill = GridBagConstraints.BOTH;
							layoutTrackerManagerTimer.add(valueTrackerTimer, 2, 0);
						}
						layoutTrackerManager.add(panelTrackerManagerTimer, 1, 0);
					}
					{
						BasicPanel panelTrackerManagerEstimation = new BasicPanel();
						panelTrackerManagerEstimation.setBackground(Color.decode("#212B33"));
						GridHelper layoutTrackerManagerEstimation = new GridHelper(panelTrackerManagerEstimation);
						{
							buttonTrackerAnnotation = new BasicImage("tracking_annotation");
							buttonTrackerAnnotation.setBackground(Color.decode("#212B33"));
							layoutTrackerManagerEstimation.constrains().insets = new Insets(3, 0, 0, 8);
							layoutTrackerManagerEstimation.constrains().gridheight = 2;
							layoutTrackerManagerEstimation.add(buttonTrackerAnnotation, 0, 0);
						}
						{
							labelTrackerTimerStarted = new BasicLabel("STARTED");
							labelTrackerTimerStarted.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelTrackerTimerStarted.setForeground(Color.decode("#525B61"));
							labelTrackerTimerStarted.setBackground(Color.decode("#212B33"));
							layoutTrackerManagerEstimation.constrains().insets = new Insets(0, 0, 0, 0);
							layoutTrackerManagerEstimation.add(labelTrackerTimerStarted, 1, 0);
						}
						{
							valueTrackerTimerStarted = new BasicLabel("--:--h");
							valueTrackerTimerStarted.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							valueTrackerTimerStarted.setForeground(Color.decode("#A6AEB4"));
							valueTrackerTimerStarted.setBackground(Color.decode("#212B33"));
							valueTrackerTimerStarted.setHorizontalAlignment(JLabel.RIGHT);
							layoutTrackerManagerEstimation.constrains().ipadx = 5;
							layoutTrackerManagerEstimation.constrains().insets = new Insets(0, 0, 0, 0);
							layoutTrackerManagerEstimation.add(valueTrackerTimerStarted, 2, 0);
						}
						{
							labelTrackerTimerRemaining = new BasicLabel("REMAINING");
							labelTrackerTimerRemaining.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelTrackerTimerRemaining.setForeground(Color.decode("#525B61"));
							labelTrackerTimerRemaining.setBackground(Color.decode("#212B33"));
							layoutTrackerManagerEstimation.constrains().insets = new Insets(0, 0, 6, 0);
							layoutTrackerManagerEstimation.add(labelTrackerTimerRemaining, 1, 1);
						}
						{
							valueTrackerTimerRemaining = new BasicLabel("--h");
							valueTrackerTimerRemaining.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							valueTrackerTimerRemaining.setForeground(Color.decode("#A6AEB4"));
							valueTrackerTimerRemaining.setBackground(Color.decode("#212B33"));
							valueTrackerTimerRemaining.setHorizontalAlignment(JLabel.RIGHT);
							layoutTrackerManagerEstimation.constrains().ipadx = 5;
							layoutTrackerManagerEstimation.constrains().insets = new Insets(0, 0, 6, 0);
							layoutTrackerManagerEstimation.add(valueTrackerTimerRemaining, 2, 1);
						}
						layoutTrackerManager.add(panelTrackerManagerEstimation, 1, 1);
					}
					panelTrackerSection.add(panelTrackerManager);
				}
				{
					panelTrackerAnnotation = new BasicPanel();
					panelTrackerAnnotation.setLayout(new BorderLayout());
					panelTrackerAnnotation.setBackground(Color.decode("#212B33"));
					panelTrackerAnnotation.setVisible(false);
					{
						{
							labelTrackerAnnotation = new BasicLabel("ANNOTATION");
							labelTrackerAnnotation.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelTrackerAnnotation.setForeground(Color.decode("#525B61"));
							labelTrackerAnnotation.setBackground(Color.decode("#212B33"));
							labelTrackerAnnotation.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
							panelTrackerAnnotation.add(labelTrackerAnnotation, BorderLayout.NORTH);
						}
						{
							editorTrackerAnnotation = new BasicEditor();
							editorTrackerAnnotation.setFont(UIManager.getFont("TextField.font").deriveFont(16f));
							editorTrackerAnnotation.setCaretColor(Color.decode("#ffffff"));
							editorTrackerAnnotation.setForeground(Color.decode("#ffffff"));
							editorTrackerAnnotation.setBackground(Color.decode("#212B33"));
							editorTrackerAnnotation.setBorder(BorderFactory.createEmptyBorder(0, 12, 6, 6));
							panelTrackerAnnotation.add(editorTrackerAnnotation, BorderLayout.CENTER);
						}
						panelTrackerSection.add(panelTrackerAnnotation);
					}
				}
				panelTracker.add(panelTrackerSection, BorderLayout.CENTER);
			}
			layout.constrains().weightx = 1.0;
			layout.add(panelTracker, 0, 0);
		}
		// --------------------------------------------------------
		{
			panelBreakManager = new BasicPanel();
			panelBreakManager.setLayout(new CardLayout());
			panelBreakManager.setBackground(Color.decode("#FFFFFF"));
			panelBreakManager.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode("#DCDCDC")));
			{
				panelBreakEmpty = new BasicPanel();
				GridHelper layoutBreakEmpty = new GridHelper(panelBreakEmpty);
				panelBreakEmpty.setBackground(Color.decode("#FFFFFF"));
				{
					labelBreakEmptyTitle = new BasicLabel("BREAKS ACTIVITY");
					labelBreakEmptyTitle.setForeground(Color.decode("#9B9B9B"));
					labelBreakEmptyTitle.setBackground(Color.decode("#FFFFFF"));
					labelBreakEmptyTitle.setFont(UIManager.getFont("Label.font").deriveFont(12f));
					layoutBreakEmpty.constrains().weightx = 1.0;
					layoutBreakEmpty.constrains().insets = new Insets(2, 8, 2, 8);
					layoutBreakEmpty.add(labelBreakEmptyTitle, 0, 0);
				}
				{
					BasicPanel panelEmptyHelper = new BasicPanel();
					GridHelper layoutEmptyHelper = new GridHelper(panelEmptyHelper);
					panelEmptyHelper.setBackground(Color.decode("#F3F3F3"));
					panelEmptyHelper.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.decode("#DCDCDC")));
					{
						buttonBreakEmptyHelper = new BasicImage("interruption_create");
						buttonBreakEmptyHelper.setForeground(Color.decode("#9B9B9B"));
						buttonBreakEmptyHelper.setBackground(Color.decode("#F3F3F3"));
						layoutEmptyHelper.constrains().weighty = 1.0;
						layoutEmptyHelper.constrains().fill = GridBagConstraints.HORIZONTAL;
						layoutEmptyHelper.constrains().anchor = GridBagConstraints.CENTER;
						layoutEmptyHelper.constrains().insets = new Insets(0, 20, 0, 20);
						layoutEmptyHelper.add(buttonBreakEmptyHelper, 0, 0);
					}
					{
						labelBreakEmptyHelper = new BasicLabel("Interrupt current task");
						labelBreakEmptyHelper.setForeground(Color.decode("#9B9B9B"));
						labelBreakEmptyHelper.setBackground(Color.decode("#F3F3F3"));
						labelBreakEmptyHelper.setFont(UIManager.getFont("TextField.font").deriveFont(32f));
						layoutEmptyHelper.constrains().weightx = 1.0;
						layoutEmptyHelper.constrains().weighty = 1.0;
						layoutEmptyHelper.constrains().fill = GridBagConstraints.BOTH;
						layoutEmptyHelper.constrains().anchor = GridBagConstraints.CENTER;
						layoutEmptyHelper.constrains().insets = new Insets(0, 0, 0, 0);
						layoutEmptyHelper.add(labelBreakEmptyHelper, 1, 0);
					}
					layoutBreakEmpty.constrains().weightx = 1.0;
					layoutBreakEmpty.constrains().weighty = 1.0;
					layoutBreakEmpty.constrains().insets = new Insets(0, 0, 0, 0);
					layoutBreakEmpty.constrains().fill = GridBagConstraints.BOTH;
					layoutBreakEmpty.add(panelEmptyHelper, 0, 1);
				}
				panelBreakManager.add(panelBreakEmpty);
			}
			{
				panelBreakTracker = new BasicPanel();
				GridHelper layoutBreakTracker = new GridHelper(panelBreakTracker);
				{
					panelBreakTrackerEditor = new BasicPanel();
					panelBreakTrackerEditor.setLayout(new CardLayout());
					{
						panelBreakTrackerProject = new BasicPanel();
						GridHelper layoutBreakTrackerProject = new GridHelper(panelBreakTrackerProject);
						{
							labelBreakEditorProject = new BasicLabel("BREAK PROJECT");
							labelBreakEditorProject.setForeground(Color.decode("#9B9B9B"));
							labelBreakEditorProject.setBackground(Color.decode("#FFFFFF"));
							labelBreakEditorProject.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							layoutBreakTrackerProject.constrains().weightx = 1.0;
							layoutBreakTrackerProject.constrains().insets = new Insets(2, 8, 2, 8);
							layoutBreakTrackerProject.add(labelBreakEditorProject, 0, 0);
						}
						{
							comboBreakEditorProject = new LightComboBox<Object>();
							selectorBreakProject = new ProjectSearchBox(comboBreakEditorProject, breakTask.getProject());
							comboBreakEditorProject.setModel(selectorBreakProject);
							comboBreakEditorProject.addItemListener(selectorBreakProject);
							layoutBreakTrackerProject.add(comboBreakEditorProject, 0, 1);
						}
						panelBreakTrackerEditor.add(panelBreakTrackerProject);
					}
					{
						panelBreakTrackerTask = new BasicPanel();
						GridHelper layoutBreakTrackerTask = new GridHelper(panelBreakTrackerTask);
						{
							labelBreakEditorTask = new BasicLabel("BREAK TASK");
							labelBreakEditorTask.setForeground(Color.decode("#9B9B9B"));
							labelBreakEditorTask.setBackground(Color.decode("#FFFFFF"));
							labelBreakEditorTask.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							layoutBreakTrackerTask.constrains().weightx = 1.0;
							layoutBreakTrackerTask.constrains().insets = new Insets(2, 8, 2, 8);
							layoutBreakTrackerTask.add(labelBreakEditorTask, 0, 0);
						}
						{
							comboBreakEditorTask = new LightComboBox<Object>();
							selectorBreakTask = new TaskSearchBox(comboBreakEditorTask, breakTask);
							comboBreakEditorTask.setModel(selectorBreakTask);
							comboBreakEditorTask.addItemListener(selectorBreakTask);
							layoutBreakTrackerTask.add(comboBreakEditorTask, 0, 1);
						}
						panelBreakTrackerEditor.add(panelBreakTrackerTask);
					}
					{
						panelBreakTrackerReason = new BasicPanel();
						panelBreakTrackerReason.setLayout(new BorderLayout());
						{
							labelBreakEditorReason = new BasicLabel("REASON");
							labelBreakEditorReason.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelBreakEditorReason.setForeground(Color.decode("#9B9B9B"));
							labelBreakEditorReason.setBackground(Color.decode("#FFFFFF"));
							labelBreakEditorReason.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
							panelBreakTrackerReason.add(labelBreakEditorReason, BorderLayout.NORTH);
						}
						{
							editorBreakEditorReason = new BasicEditor();
							editorBreakEditorReason.setFont(UIManager.getFont("TextField.font").deriveFont(16f));
							editorBreakEditorReason.setCaretColor(Color.decode("#4A4A4A"));
							editorBreakEditorReason.setForeground(Color.decode("#4A4A4A"));
							editorBreakEditorReason.setBackground(Color.decode("#F3F3F3"));

							Border lineBorder = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode("#DCDCDC"));
							Border emptyBorder = BorderFactory.createEmptyBorder(3, 8, 3, 8);
							editorBreakEditorReason.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));

							panelBreakTrackerReason.add(editorBreakEditorReason, BorderLayout.CENTER);
						}
						panelBreakTrackerEditor.add(panelBreakTrackerReason);
					}
					layoutBreakTracker.add(panelBreakTrackerEditor, 0, 0);
				}
				{
					panelBreakTrackerTimer = new BasicPanel();
					panelBreakTrackerTimer.setForeground(Color.decode("#4A4A4A"));
					panelBreakTrackerTimer.setBackground(Color.decode("#F3F3F3"));
					GridHelper layoutBreakTrackerTimer = new GridHelper(panelBreakTrackerTimer);
					{
						labelBreakTrackerTitle = new BasicLabel("Break started at --:--");
						labelBreakTrackerTitle.setForeground(Color.decode("#4A4A4A"));
						labelBreakTrackerTitle.setFont(UIManager.getFont("TextField.font").deriveFont(16f));
						layoutBreakTrackerTimer.constrains().gridwidth = 5;
						layoutBreakTrackerTimer.constrains().insets = new Insets(12, 10, 10, 5);
						layoutBreakTrackerTimer.add(labelBreakTrackerTitle, 0, 0);
					}
					{
						buttonBreakTrackerDecrease = new BasicImage("interruption_decrease");
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 10, 12, 5);
						layoutBreakTrackerTimer.add(buttonBreakTrackerDecrease, 0, 1);
					}
					{
						buttonBreakTrackerIncrease = new BasicImage("interruption_increase");
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 5, 12, 5);
						layoutBreakTrackerTimer.add(buttonBreakTrackerIncrease, 1, 1);
					}
					{
						buttonBreakTrackerCommit = new BasicImage("interruption_commit");
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 5, 12, 5);
						layoutBreakTrackerTimer.add(buttonBreakTrackerCommit, 2, 1);
					}
					{
						buttonBreakTrackerCancel = new BasicImage("interruption_cancel");
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 5, 12, 10);
						layoutBreakTrackerTimer.add(buttonBreakTrackerCancel, 3, 1);
					}
					{
						layoutBreakTrackerTimer.constrains().weightx = 1.0;
						layoutBreakTrackerTimer.add(new BasicLabel(), 4, 1);
					}
					{
						valueBreakTrackerTimer = new BasicLabel("--:--");
						valueBreakTrackerTimer.setFont(UIManager.getFont("TextField.font").deriveFont(24f));
						valueBreakTrackerTimer.setForeground(Color.decode("#E97C5F"));
						layoutBreakTrackerTimer.constrains().gridheight = 2;
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 0, 0, 10);
						layoutBreakTrackerTimer.constrains().anchor = GridBagConstraints.CENTER;
						layoutBreakTrackerTimer.add(valueBreakTrackerTimer, 5, 0);
					}
					layoutBreakTracker.constrains().weightx = 1.0;
					layoutBreakTracker.constrains().weighty = 1.0;
					layoutBreakTracker.constrains().insets = new Insets(0, 0, 0, 0);
					layoutBreakTracker.constrains().fill = GridBagConstraints.BOTH;
					layoutBreakTracker.add(panelBreakTrackerTimer, 0, 1);
				}
				panelBreakManager.add(panelBreakTracker);
			}
			layout.constrains().weightx = 1.0;
			layout.add(panelBreakManager, 0, 1);
		}
		// --------------------------------------------------------
		{
			panelBreakList = new BasicPanel();
			panelBreakList.setLayout(new BorderLayout());
			panelBreakList.setBackground(Color.decode("#ffffff"));
			{
				tableTaskInterruptions = new BasicTableView(new FactoryCellRenderer());
				panelBreakList.add(tableTaskInterruptions, BorderLayout.CENTER);
			}
			layout.constrains().weightx = 1.0;
			layout.constrains().weighty = 1.0;
			layout.constrains().fill = GridBagConstraints.BOTH;
			layout.add(panelBreakList, 0, 2);
		}
	}

	public void init()
	{
		if (trackerManager.isInterrupted())
		{
			panelBreakEmpty.setVisible(false);
			panelBreakTracker.setVisible(true);
			comboBreakEditorProject.requestFocusInWindow();
		}
		else
		{
			panelBreakEmpty.setVisible(true);
			panelBreakTracker.setVisible(false);
			comboTrackerProject.requestFocusInWindow();
		}

		// =======================================================

		ArrayList<Object> response = null;
		try
		{
			SessionService sessionService = new SessionService();
			response = sessionService.findInterruptionsBySession(trackerManager.getSession());
		}
		catch (Exception ex)
		{
			response = new ArrayList<Object>();
		}
		DefaultTableModel tableModel = (DefaultTableModel) tableTaskInterruptions.getModel();
		for (int i = tableModel.getRowCount() - 1; i > -1; i--)
		{
			tableModel.removeRow(i);
		}
		for (Object session : response)
		{
			tableModel.addRow(new Object[] { session });
		}
	}

	public void events()
	{
		selectorTrackerProject.setSelectorAction(new SelectorAction()
		{
			@Override
			public void onSelection()
			{
				actionTrackerTaskSelector();
			}

			@Override
			public void onCancel()
			{
				actionTrackerDismiss();
			}
		});

		selectorTrackerTask.setSelectorAction(new SelectorAction()
		{
			@Override
			public void onSelection()
			{
				actionTrackerEstimator();
			}

			@Override
			public void onCancel()
			{
				actionTrackerProjectSelector();
			}
		});
		// =======================================================
		{
			KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
			Action action = new AbstractAction()
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
				{
					actionTrackerBegin();
				}
			};
			panelTrackerEditorTimer.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, "VK_ENTER");
			panelTrackerEditorTimer.getActionMap().put("VK_ENTER", action);
		}
		// -------------------------------------------------------
		{
			KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
			Action action = new AbstractAction()
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
				{
					actionTrackerTaskSelector();
				}
			};
			panelTrackerEditorTimer.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, "VK_ESCAPE");
			panelTrackerEditorTimer.getActionMap().put("VK_ESCAPE", action);
		}
		// -------------------------------------------------------
		Action trackerEstimationIncrease = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				actionTrackerEstimationIncrease();
			}
		};
		Action trackerEstimationDecrease = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				actionTrackerEstimationDecrease();
			}
		};
		// -------------------------------------------------------
		{
			KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false);
			panelTrackerEditorTimer.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, "VK_UP");
			panelTrackerEditorTimer.getActionMap().put("VK_UP", trackerEstimationIncrease);
		}
		// -------------------------------------------------------
		{
			KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false);
			panelTrackerEditorTimer.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, "VK_RIGHT");
			panelTrackerEditorTimer.getActionMap().put("VK_RIGHT", trackerEstimationIncrease);
		}
		// -------------------------------------------------------
		{
			KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false);
			panelTrackerEditorTimer.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, "VK_DOWN");
			panelTrackerEditorTimer.getActionMap().put("VK_DOWN", trackerEstimationDecrease);
		}
		// -------------------------------------------------------
		{
			KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false);
			panelTrackerEditorTimer.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, "VK_LEFT");
			panelTrackerEditorTimer.getActionMap().put("VK_LEFT", trackerEstimationDecrease);
		}
		// -------------------------------------------------------
		{
			buttonEstimationIncrease.addActionListener(trackerEstimationIncrease);
			buttonEstimationDecrease.addActionListener(trackerEstimationDecrease);
		}
		// =======================================================

		Action trackerTimerIncrease = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				actionTrackerTaskIncrease();
			}
		};
		Action trackerTimerDecrease = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				actionTrackerTaskDecrease();
			}
		};
		Action trackerTimerAnnotation = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				actionTrackerAnnotation();
			}
		};
		// -------------------------------------------------------
		{
			buttonTrackerTimerIncrease.addActionListener(trackerTimerIncrease);
			buttonTrackerTimerDecrease.addActionListener(trackerTimerDecrease);
			buttonTrackerAnnotation.addActionListener(trackerTimerAnnotation);
		}
		// =======================================================

		textTrackerTimerTask.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					e.consume();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{

				}
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				actionTrackerTaskDescription();
			}
		});

		// -------------------------------------------------------

		editorTrackerAnnotation.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					e.consume();
					actionTrackerAnnotationCommit();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					e.consume();
					actionTrackerAnnotationDismiss();
				}
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
			}
		});

		// =======================================================
		{
			KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.META_MASK);
			Action action = new AbstractAction()
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
				{
					actionInterruptionBegin();
					actionTrackerDismiss();
				}
			};
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, "VK_I");
			getRootPane().getActionMap().put("VK_I", action);
		}
		// -------------------------------------------------------
		{
			KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.META_MASK);
			Action action = new AbstractAction()
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
				{
					try
					{
						ReportManager reportManager = new ReportManager();
						reportManager.generateReport();
						actionTrackerDismiss();
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			};
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, "VK_R");
			getRootPane().getActionMap().put("VK_R", action);
		}
		// -------------------------------------------------------
		{
			KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.META_MASK);
			Action action = new AbstractAction()
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
				{
					actionTrackerToggle();
				}
			};
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, "VK_P");
			getRootPane().getActionMap().put("VK_P", action);
		}
		// -------------------------------------------------------
		{
			KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.META_MASK);
			Action action = new AbstractAction()
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
				{
					actionTrackerAnnotation();
				}
			};
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, "VK_A");
			getRootPane().getActionMap().put("VK_A", action);
		}

		// =======================================================

		Action actionBreakEmptyHelper = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				actionInterruptionBegin();
			}
		};
		// -------------------------------------------------------
		{
			buttonBreakEmptyHelper.addActionListener(actionBreakEmptyHelper);
		}

		// =======================================================

		Action breakTimerIncrease = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				actionInterruptionIncrease();
			}
		};
		Action breakTimerDecrease = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				actionInterruptionDecrease();
			}
		};
		Action breakTrackerCommit = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				actionInterruptionCommit();
			}
		};
		Action breakTrackerCancel = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				actionInterruptionCancel();
			}
		};
		// -------------------------------------------------------
		{
			buttonBreakTrackerIncrease.addActionListener(breakTimerIncrease);
			buttonBreakTrackerDecrease.addActionListener(breakTimerDecrease);
			buttonBreakTrackerCommit.addActionListener(breakTrackerCommit);
			buttonBreakTrackerCancel.addActionListener(breakTrackerCancel);
		}

		// =======================================================

		selectorBreakProject.setSelectorAction(new SelectorAction()
		{
			@Override
			public void onSelection()
			{
				actionInterruptionTaskSelector();
			}

			@Override
			public void onCancel()
			{
				actionTrackerDismiss();
			}
		});

		selectorBreakTask.setSelectorAction(new SelectorAction()
		{
			@Override
			public void onSelection()
			{
				actionInterruptionReason();
			}

			@Override
			public void onCancel()
			{
				actionInterruptionProjectSelector();
			}
		});

		editorBreakEditorReason.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					e.consume();
					actionInterruptionCommit();
					actionTrackerDismiss();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					e.consume();
					actionInterruptionTaskSelector();
				}
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
			}
		});
	}

	// ~ Methods
	// =======================================================

	private void actionTrackerDismiss()
	{
		context.setVisible(false);
		context.dispose();
	}

	private void actionTrackerProjectSelector()
	{
		panelTrackerEditorProject.setVisible(true);
		panelTrackerEditorTask.setVisible(false);
		panelTrackerEditorTimer.setVisible(false);

		selectorTrackerTask.clearUI();
		trackerTask.setDescription("");
		trackerTask.setIdTask(0);
	}

	private void actionTrackerTaskSelector()
	{
		if (trackerTask.hasProject())
		{
			panelTrackerEditorProject.setVisible(false);
			panelTrackerEditorTask.setVisible(true);
			panelTrackerEditorTimer.setVisible(false);
			comboTrackerTask.requestFocusInWindow();

			labelTrackerTaskTitle.setText("TASK" + " - " + trackerTask.getProject().getDescription().trim().toUpperCase());
		}
	}

	private void actionTrackerEstimator()
	{
		if (trackerTask.hasTask())
		{
			panelTrackerEditorProject.setVisible(false);
			panelTrackerEditorTask.setVisible(false);
			panelTrackerEditorTimer.setVisible(true);
			panelTrackerEditorTimer.requestFocusInWindow();

			labelEstimationTitle.setText(trackerTask.getDescription().trim().toUpperCase() + " - " + trackerTask.getProject().getDescription().trim().toUpperCase());
		}
		if (trackerTask.getProject().getIdProject() > 0 && trackerTask.getIdTask() > 0)
		{
			try
			{
				TaskService taskService = new TaskService();
				taskService.findTaskEstimationById(trackerTask);

				long duration = Math.round(trackerTask.getSummary() / 3600);

				String timeString = String.format("%02d h", trackerTask.getReestimate());
				valueEstimationTask.setText(timeString);
				trackerEstimation = trackerTask.getReestimate();

				valueEstimationSpent.setText(String.format("%d h", duration));
				valueEstimationRemaining.setText(String.format("%d h", trackerTask.getReestimate() - duration));
				valueEstimationInitial.setText(String.format("%d h", trackerTask.getEstimation()));
				valueEstimationCurrent.setText(String.format("%d h", trackerTask.getReestimate()));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			trackerEstimation = 0;
			valueEstimationTask.setText("--h");
			valueEstimationSpent.setText("--");
			valueEstimationRemaining.setText("--");
			valueEstimationInitial.setText("--");
			valueEstimationCurrent.setText("--");
		}
	}

	private void actionTrackerEstimationIncrease()
	{
		trackerEstimation++;
		String timeString = String.format("%02d h", trackerEstimation);
		valueEstimationTask.setText(timeString);
	}

	private void actionTrackerEstimationDecrease()
	{
		trackerEstimation = trackerEstimation > 0 ? --trackerEstimation : 0;
		String timeString = String.format("%02d h", trackerEstimation);
		valueEstimationTask.setText(timeString);
	}

	private void actionTrackerBegin()
	{
		try
		{
			trackerManager.startTracker(trackerTask, trackerEstimation);

			trackerTask.setDescription("");
			trackerTask.setIdTask(0);
			trackerTask.getProject().setDescription("");
			trackerTask.getProject().setIdProject(0);
			trackerEstimation = 0;

			selectorTrackerProject.clearUI();
			selectorTrackerTask.clearUI();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		context.setVisible(false);
		context.dispose();
	}

	private void actionTrackerTaskIncrease()
	{
		try
		{
			if (trackerManager.isRunning() || trackerManager.isPaused() || trackerManager.isInterrupted())
			{
				trackerManager.increaseTimer();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void actionTrackerTaskDecrease()
	{
		try
		{
			if (trackerManager.isRunning() || trackerManager.isPaused() || trackerManager.isInterrupted())
			{
				trackerManager.decreaseTimer();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void actionTrackerTaskDescription()
	{
		try
		{
			trackerManager.saveTaskDescription(textTrackerTimerTask.getText());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void actionTrackerAnnotation()
	{
		try
		{
			if (trackerManager.isRunning() || trackerManager.isPaused() || trackerManager.isInterrupted())
			{
				panelTrackerAnnotation.setVisible(true);
				panelTrackerManager.setVisible(false);
				editorTrackerAnnotation.setText(trackerManager.getTaskAnnotation());
				editorTrackerAnnotation.requestFocusInWindow();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void actionTrackerAnnotationCommit()
	{
		try
		{
			if (trackerManager.isRunning() || trackerManager.isPaused() || trackerManager.isInterrupted())
			{
				panelTrackerAnnotation.setVisible(false);
				panelTrackerManager.setVisible(true);
				trackerManager.saveTaskAnnotation(editorTrackerAnnotation.getText());
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void actionTrackerAnnotationDismiss()
	{
		try
		{
			if (trackerManager.isRunning() || trackerManager.isPaused() || trackerManager.isInterrupted())
			{
				panelTrackerAnnotation.setVisible(false);
				panelTrackerManager.setVisible(true);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void actionTrackerToggle()
	{
		try
		{
			trackerManager.toggleTracker();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void actionInterruptionBegin()
	{
		if (trackerManager.isRunning())
		{
			try
			{
				trackerManager.startInterruption();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			selectorBreakProject.clearUI();
			selectorBreakTask.clearUI();
			panelBreakEmpty.setVisible(false);
			panelBreakTracker.setVisible(true);
		}
	}

	private void actionInterruptionProjectSelector()
	{
		panelBreakTrackerProject.setVisible(true);
		panelBreakTrackerTask.setVisible(false);
		panelBreakTrackerReason.setVisible(false);
		comboBreakEditorProject.requestFocusInWindow();
		labelBreakEditorTask.setText("BREAK PROJECT");

		selectorBreakTask.clearUI();
		editorBreakEditorReason.setText("");

		breakTask.setIdTask(0);
		breakTask.setDescription("");
		breakTask.setAnnotation("");
	}

	private void actionInterruptionTaskSelector()
	{
		if (breakTask.hasProject())
		{
			editorBreakEditorReason.setText("");

			panelBreakTrackerProject.setVisible(false);
			panelBreakTrackerTask.setVisible(true);
			panelBreakTrackerReason.setVisible(false);
			comboBreakEditorTask.requestFocusInWindow();

			labelBreakEditorTask.setText("TASK" + " - " + breakTask.getProject().getDescription().trim().toUpperCase());
		}
	}

	private void actionInterruptionReason()
	{
		if (breakTask.hasTask())
		{
			panelBreakTrackerProject.setVisible(false);
			panelBreakTrackerTask.setVisible(false);
			panelBreakTrackerReason.setVisible(true);
			editorBreakEditorReason.requestFocusInWindow();
		}
	}

	private void actionInterruptionCommit()
	{
		try
		{
			trackerManager.saveInterruptionAnnotation(editorBreakEditorReason.getText());
			trackerManager.commitInterruption(breakTask);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		selectorBreakProject.clearUI();
		selectorBreakTask.clearUI();
		editorBreakEditorReason.setText("");

		panelBreakEmpty.setVisible(true);
		panelBreakTracker.setVisible(false);
		panelBreakTrackerProject.setVisible(true);
		panelBreakTrackerTask.setVisible(false);
		panelBreakTrackerReason.setVisible(false);

		labelBreakEditorProject.setText("BREAK PROJECT");
		labelBreakEditorTask.setText("BREAK TASK");
		labelBreakEditorReason.setText("REASON");
	}

	private void actionInterruptionCancel()
	{
		try
		{
			trackerManager.cancelInterruption();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		selectorBreakProject.clearUI();
		selectorBreakTask.clearUI();

		panelBreakEmpty.setVisible(true);
		panelBreakTracker.setVisible(false);
		panelBreakTrackerProject.setVisible(true);
		panelBreakTrackerTask.setVisible(false);

		labelBreakEditorProject.setText("BREAK PROJECT");
		labelBreakEditorTask.setText("BREAK TASK");
	}

	private void actionInterruptionIncrease()
	{
		try
		{
			trackerManager.increaseInterruption();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void actionInterruptionDecrease()
	{
		try
		{
			trackerManager.decreaseInterruption();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	// ~ Methods
	// =======================================================

	@Override
	public void track(Session session, Session interruption)
	{
		{
			long duration = session.getDuration();
			long hours = TimeUnit.SECONDS.toHours(duration);
			duration -= TimeUnit.HOURS.toSeconds(hours);
			long minutes = TimeUnit.SECONDS.toMinutes(duration);
			duration -= TimeUnit.MINUTES.toSeconds(minutes);
			// long seconds = TimeUnit.SECONDS.toSeconds(duration);

			String timeString = String.format("%02d:%02d", hours, minutes);
			valueTrackerTimer.setText(timeString);
		}
		{
			long duration = interruption.getDuration();
			long hours = TimeUnit.SECONDS.toHours(duration);
			duration -= TimeUnit.HOURS.toSeconds(hours);
			long minutes = TimeUnit.SECONDS.toMinutes(duration);
			duration -= TimeUnit.MINUTES.toSeconds(minutes);
			long seconds = TimeUnit.SECONDS.toSeconds(duration);

			String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
			valueBreakTrackerTimer.setText(timeString);
		}
		{
			valueTrackerTimerRemaining.setText("2354h");
		}

		// -------------------------------------------------------

		if (trackerManager.isPaused() || trackerManager.isInterrupted())
		{
			if (timerTrackerTimerBlink == null)
			{
				timerTrackerTimerBlink = new Timer(500, new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (valueTrackerTimer.getForeground().getRGB() == Color.decode("#ffffff").getRGB())
							valueTrackerTimer.setForeground(Color.decode("#161F26"));
						else
							valueTrackerTimer.setForeground(Color.decode("#ffffff"));
					}
				});
				timerTrackerTimerBlink.start();
			}
		}
		else
		{
			if (timerTrackerTimerBlink != null)
			{
				timerTrackerTimerBlink.stop();
				timerTrackerTimerBlink = null;
			}
			valueTrackerTimer.setForeground(Color.decode("#ffffff"));
		}
	}

	@Override
	public void update(Session session, Session interruption)
	{
		{
			fieldTackerTimerProject.setText(session.getTask().getProject().getDescription());
			textTrackerTimerTask.setText(session.getTask().getDescription());

			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date resultdate = new Date(session.getInitiated());
			String startedString = String.format("%sh", sdf.format(resultdate));
			valueTrackerTimerStarted.setText(startedString);
		}
		if (trackerManager.isInterrupted())
		{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date resultdate = new Date(interruption.getInitiated());
			String startedString = String.format("Break started at %sh", sdf.format(resultdate));
			labelBreakTrackerTitle.setText(startedString);
		}
	}

	// Class Methods
	// ===============================================================

	private class FactoryCellRenderer extends BasicPanel implements TableCellRenderer
	{
		private static final long serialVersionUID = 8233159691825884868L;

		BasicField fieldProjectDescription;
		BasicField fieldTaskDescription;
		BasicLabel labelSessionInitiated;
		BasicLabel labelSessionDuration;

		public FactoryCellRenderer()
		{
			super();

			GridHelper layout = new GridHelper(this);
			setBackground(Color.decode("#ffffff"));

			Border borderSuperior = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#DCDCDC"));
			Border borderDecorator = BorderFactory.createMatteBorder(0, 4, 0, 0, Color.decode("#77ABD4"));
			setBorder(BorderFactory.createCompoundBorder(borderSuperior, borderDecorator));
			{
				fieldTaskDescription = new BasicField("--");
				fieldTaskDescription.setFont(UIManager.getFont("TextField.font").deriveFont(18f));
				fieldTaskDescription.setForeground(Color.decode("#4A4A4A"));
				layout.constrains().weightx = 1.0;
				layout.constrains().weighty = 1.0;
				layout.constrains().fill = GridBagConstraints.HORIZONTAL;
				layout.constrains().anchor = GridBagConstraints.SOUTH;
				layout.constrains().insets = new Insets(10, 10, 0, 10);
				layout.add(fieldTaskDescription, 0, 0);
			}
			{
				fieldProjectDescription = new BasicField("--");
				fieldProjectDescription.setFont(UIManager.getFont("Label.font").deriveFont(14f));
				fieldProjectDescription.setForeground(Color.decode("#79ADD7"));
				layout.constrains().weightx = 1.0;
				layout.constrains().weighty = 1.0;
				layout.constrains().fill = GridBagConstraints.HORIZONTAL;
				layout.constrains().anchor = GridBagConstraints.NORTH;
				layout.constrains().insets = new Insets(4, 10, 10, 10);
				layout.add(fieldProjectDescription, 0, 1);
			}
			{
				BasicPanel panelSessionInformation = new BasicPanel();
				panelSessionInformation.setLayout(new BorderLayout());
				{
					labelSessionDuration = new BasicLabel("--");
					labelSessionDuration.setForeground(Color.decode("#4A4A4A"));
					labelSessionDuration.setFont(UIManager.getFont("TextField.font").deriveFont(24f));
					labelSessionDuration.setHorizontalAlignment(JLabel.RIGHT);
					panelSessionInformation.add(labelSessionDuration, BorderLayout.NORTH);
				}
				{
					labelSessionInitiated = new BasicLabel("--");
					labelSessionInitiated.setForeground(Color.decode("#888888"));
					labelSessionInitiated.setFont(UIManager.getFont("TextField.font").deriveFont(16f));
					labelSessionInitiated.setHorizontalAlignment(JLabel.RIGHT);
					panelSessionInformation.add(labelSessionInitiated, BorderLayout.SOUTH);
				}
				layout.constrains().gridheight = 2;
				layout.constrains().anchor = GridBagConstraints.CENTER;
				layout.constrains().insets = new Insets(0, 0, 0, 0);
				layout.add(panelSessionInformation, 1, 0);
			}
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{

			if (value.getClass().equals(Session.class))
			{
				Session session = (Session) value;

				fieldProjectDescription.setText(session.getTask().getProject().getDescription());
				fieldTaskDescription.setText(session.getTask().getDescription());

				{
					long duration = session.getDuration();
					long hours = TimeUnit.SECONDS.toHours(duration);
					duration -= TimeUnit.HOURS.toSeconds(hours);
					long minutes = TimeUnit.SECONDS.toMinutes(duration);
					duration -= TimeUnit.MINUTES.toSeconds(minutes);
					String timeString = String.format("%02d:%02d", hours, minutes);
					labelSessionDuration.setText(timeString);
				}
				{
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					Date resultdate = new Date(session.getInitiated());
					String startedString = String.format("%sh", sdf.format(resultdate));
					labelSessionInitiated.setText(startedString);
				}
			}

			int rowHeight = (int) this.getPreferredSize().getHeight();
			if (table.getRowHeight(row) != rowHeight)
			{
				table.setRowHeight(row, rowHeight);
			}
			if (isSelected)
			{
				setBackground(Color.decode("#ffffff"));
			}
			else
			{
				setBackground(Color.decode("#ffffff"));
			}

			return this;
		}

	}
}
