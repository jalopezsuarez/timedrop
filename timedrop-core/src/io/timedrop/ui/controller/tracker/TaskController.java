package io.timedrop.ui.controller.tracker;

import io.timedrop.business.TrackerInterface;
import io.timedrop.business.TrackerManager;
import io.timedrop.business.report.ReportManager;
import io.timedrop.commons.GridHelper;
import io.timedrop.domain.Interruption;
import io.timedrop.domain.Organization;
import io.timedrop.domain.Report;
import io.timedrop.domain.Session;
import io.timedrop.domain.Task;
import io.timedrop.service.TaskService;
import io.timedrop.ui.components.BasicField;
import io.timedrop.ui.components.BasicImage;
import io.timedrop.ui.components.BasicFrame;
import io.timedrop.ui.components.BasicLabel;
import io.timedrop.ui.components.BasicPanel;
import io.timedrop.ui.components.selector.DarkComboBox;
import io.timedrop.ui.components.selector.LightComboBox;
import io.timedrop.ui.components.selector.ProjectSearchBox;
import io.timedrop.ui.components.selector.SelectorAction;
import io.timedrop.ui.components.selector.TaskSearchBox;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;

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

	private BasicField textTrackerTimerTask;
	private BasicField textTrackerTimerProject;

	private BasicImage buttonTrackerTimerIncrease;
	private BasicImage buttonTrackerTimerDecrease;

	private BasicLabel valueTrackerTimer;

	private BasicLabel labelTrackerTimerStarted;
	private BasicLabel valueTrackerTimerStarted;

	private BasicLabel labelTrackerTimerRemaining;
	private BasicLabel valueTrackerTimerRemaining;

	// -------------------------------------------------------

	private BasicPanel panelBreakManager;
	private BasicPanel panelBreakEmpty;

	private BasicPanel panelBreakTracker;
	private BasicPanel panelBreakTrackerEditor;

	private BasicPanel panelBreakTrackerProject;
	private BasicPanel panelBreakTrackerTask;
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

	// -------------------------------------------------------

	private BasicLabel labelBreakTrackerTitle;
	private BasicImage buttonBreakTrackerIncrease;
	private BasicImage buttonBreakTrackerDecrease;
	private BasicImage buttonBreakTrackerCommit;
	private BasicImage buttonBreakTrackerCancel;

	private BasicLabel valueBreakTrackerTimer;

	// -------------------------------------------------------

	private BasicPanel panelBreakList;

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
				panelTrackerManager = new BasicPanel();
				panelTrackerManager.setBackground(Color.decode("#212B33"));
				GridHelper layoutTrackerManager = new GridHelper(panelTrackerManager);
				{
					BasicPanel panelTrackerManagerInformation = new BasicPanel();
					panelTrackerManagerInformation.setBackground(Color.decode("#212B33"));
					panelTrackerManagerInformation.setLayout(new BorderLayout());
					{
						textTrackerTimerTask = new BasicField("No task");
						textTrackerTimerTask.setFont(UIManager.getFont("TextField.font").deriveFont(20f));
						textTrackerTimerTask.setBackground(Color.decode("#212B33"));
						textTrackerTimerTask.setForeground(Color.decode("#ffffff"));
						panelTrackerManagerInformation.add(textTrackerTimerTask, BorderLayout.NORTH);
					}
					{
						textTrackerTimerProject = new BasicField("Start tracking by selecting the task you are working on");
						textTrackerTimerProject.setBackground(Color.decode("#212B33"));
						textTrackerTimerProject.setForeground(Color.decode("#A6AEB4"));
						textTrackerTimerProject.setFont(UIManager.getFont("Label.font").deriveFont(14f));
						panelTrackerManagerInformation.add(textTrackerTimerProject, BorderLayout.CENTER);
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
						layoutTrackerManagerTimer.constrains().insets = new Insets(4, 0, 0, 4);
						layoutTrackerManagerTimer.constrains().weightx = 1.0;
						layoutTrackerManagerTimer.constrains().weighty = 0.0;
						layoutTrackerManagerTimer.constrains().fill = GridBagConstraints.HORIZONTAL;
						layoutTrackerManagerTimer.add(buttonTrackerTimerDecrease, 0, 0);
					}
					{
						buttonTrackerTimerIncrease = new BasicImage("tracking_increase");
						buttonTrackerTimerIncrease.setBackground(Color.decode("#212B33"));
						layoutTrackerManagerTimer.constrains().anchor = GridBagConstraints.CENTER;
						layoutTrackerManagerTimer.constrains().insets = new Insets(4, 4, 0, 4);
						layoutTrackerManagerTimer.constrains().weightx = 1.0;
						layoutTrackerManagerTimer.constrains().weighty = 0.0;
						layoutTrackerManagerTimer.constrains().fill = GridBagConstraints.HORIZONTAL;
						layoutTrackerManagerTimer.add(buttonTrackerTimerIncrease, 1, 0);
					}
					{
						valueTrackerTimer = new BasicLabel("--:--");
						valueTrackerTimer.setFont(UIManager.getFont("TextField.font").deriveFont(32f));
						valueTrackerTimer.setForeground(Color.decode("#ffffff"));
						valueTrackerTimer.setBackground(Color.decode("#212B33"));
						layoutTrackerManagerTimer.constrains().anchor = GridBagConstraints.CENTER;
						layoutTrackerManagerTimer.constrains().insets = new Insets(4, 4, 0, 8);
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
						labelTrackerTimerStarted = new BasicLabel("STARTED");
						labelTrackerTimerStarted.setFont(UIManager.getFont("Label.font").deriveFont(12f));
						labelTrackerTimerStarted.setForeground(Color.decode("#525B61"));
						labelTrackerTimerStarted.setBackground(Color.decode("#212B33"));
						layoutTrackerManagerEstimation.constrains().ipadx = 20;
						layoutTrackerManagerEstimation.constrains().insets = new Insets(2, 0, 0, 0);
						layoutTrackerManagerEstimation.add(labelTrackerTimerStarted, 0, 0);
					}
					{
						valueTrackerTimerStarted = new BasicLabel("--:--h");
						valueTrackerTimerStarted.setFont(UIManager.getFont("Label.font").deriveFont(12f));
						valueTrackerTimerStarted.setForeground(Color.decode("#A6AEB4"));
						valueTrackerTimerStarted.setBackground(Color.decode("#212B33"));
						layoutTrackerManagerEstimation.constrains().insets = new Insets(2, 0, 0, 0);
						layoutTrackerManagerEstimation.constrains().anchor = GridBagConstraints.LINE_END;

						layoutTrackerManagerEstimation.add(valueTrackerTimerStarted, 1, 0);
					}
					{
						labelTrackerTimerRemaining = new BasicLabel("REMAINING");
						labelTrackerTimerRemaining.setFont(UIManager.getFont("Label.font").deriveFont(12f));
						labelTrackerTimerRemaining.setForeground(Color.decode("#525B61"));
						labelTrackerTimerRemaining.setBackground(Color.decode("#212B33"));
						layoutTrackerManagerEstimation.constrains().ipadx = 20;
						layoutTrackerManagerEstimation.constrains().insets = new Insets(0, 0, 8, 0);
						layoutTrackerManagerEstimation.add(labelTrackerTimerRemaining, 0, 1);
					}
					{
						valueTrackerTimerRemaining = new BasicLabel("--h");
						valueTrackerTimerRemaining.setFont(UIManager.getFont("Label.font").deriveFont(12f));
						valueTrackerTimerRemaining.setForeground(Color.decode("#A6AEB4"));
						valueTrackerTimerRemaining.setBackground(Color.decode("#212B33"));
						layoutTrackerManagerEstimation.constrains().anchor = GridBagConstraints.LINE_END;
						layoutTrackerManagerEstimation.constrains().insets = new Insets(0, 0, 8, 0);
						layoutTrackerManagerEstimation.add(valueTrackerTimerRemaining, 1, 1);
					}
					layoutTrackerManager.add(panelTrackerManagerEstimation, 1, 1);
				}
				panelTracker.add(panelTrackerManager, BorderLayout.CENTER);
			}
			layout.constrains().weightx = 1.0;
			layout.add(panelTracker, 0, 0);
		}
		// --------------------------------------------------------
		{
			panelBreakManager = new BasicPanel();
			panelBreakManager.setLayout(new CardLayout());
			panelBreakManager.setBackground(Color.decode("#FFFFFF"));
			panelBreakManager.setPreferredSize(new Dimension(0, 150));
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
					panelEmptyHelper.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode("#DCDCDC")));
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
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 10, 10, 5);
						layoutBreakTrackerTimer.add(labelBreakTrackerTitle, 0, 0);
					}
					{
						buttonBreakTrackerDecrease = new BasicImage("interruption_decrease");
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 10, 0, 5);
						layoutBreakTrackerTimer.add(buttonBreakTrackerDecrease, 0, 1);
					}
					{
						buttonBreakTrackerIncrease = new BasicImage("interruption_increase");
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 5, 0, 5);
						layoutBreakTrackerTimer.add(buttonBreakTrackerIncrease, 1, 1);
					}
					{
						buttonBreakTrackerCommit = new BasicImage("interruption_commit");
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 5, 0, 5);
						layoutBreakTrackerTimer.add(buttonBreakTrackerCommit, 2, 1);
					}
					{
						buttonBreakTrackerCancel = new BasicImage("interruption_cancel");
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 5, 0, 10);
						layoutBreakTrackerTimer.add(buttonBreakTrackerCancel, 3, 1);
					}
					{
						layoutBreakTrackerTimer.constrains().weightx = 1.0;
						layoutBreakTrackerTimer.add(new BasicLabel(), 4, 1);
					}
					{
						valueBreakTrackerTimer = new BasicLabel("--:--");
						valueBreakTrackerTimer.setFont(UIManager.getFont("TextField.font").deriveFont(24f));
						valueBreakTrackerTimer.setForeground(Color.decode("#4A4A4A"));
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
			panelBreakList.setBackground(Color.decode("#ffffff"));
			{

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
		// -------------------------------------------------------
		{
			buttonTrackerTimerIncrease.addActionListener(trackerTimerIncrease);
			buttonTrackerTimerDecrease.addActionListener(trackerTimerDecrease);
		}
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

			}
		});

		selectorBreakTask.setSelectorAction(new SelectorAction()
		{
			@Override
			public void onSelection()
			{
				actionInterruptionCommit();
				actionTrackerDismiss();
			}

			@Override
			public void onCancel()
			{
				actionInterruptionProjectSelector();
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
				Report response = taskService.findTaskEstimationById(trackerTask);

				long duration = Math.round(response.getTaskDuration() / 3600);

				String timeString = String.format("%02d h", response.getEstimationCurrent());
				valueEstimationTask.setText(timeString);
				trackerEstimation = response.getEstimationCurrent();

				valueEstimationSpent.setText(String.format("%d h", duration));
				valueEstimationRemaining.setText(String.format("%d h", response.getEstimationCurrent() - duration));
				valueEstimationInitial.setText(String.format("%d h", response.getEstimationInit()));
				valueEstimationCurrent.setText(String.format("%d h", response.getEstimationCurrent()));
			}
			catch (Exception e)
			{
			}
		}
		else
		{
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
		}
		context.setVisible(false);
		context.dispose();
	}

	private void actionTrackerTaskIncrease()
	{
		try
		{
			trackerManager.increaseTimer();
		}
		catch (Exception ex)
		{
		}
	}

	private void actionTrackerTaskDecrease()
	{
		try
		{
			trackerManager.decreaseTimer();
		}
		catch (Exception ex)
		{
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
		comboBreakEditorProject.requestFocusInWindow();
		labelBreakEditorTask.setText("BREAK PROJECT");

		selectorBreakTask.clearUI();
		breakTask.setDescription("");
		breakTask.setIdTask(0);
	}

	private void actionInterruptionTaskSelector()
	{
		if (breakTask.hasProject())
		{
			panelBreakTrackerProject.setVisible(false);
			panelBreakTrackerTask.setVisible(true);
			comboBreakEditorTask.requestFocusInWindow();

			labelBreakEditorTask.setText("TASK" + " - " + breakTask.getProject().getDescription().trim().toUpperCase());
		}
	}

	private void actionInterruptionCommit()
	{
		try
		{
			trackerManager.commitInterruption(breakTask);
		}
		catch (Exception ex)
		{
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

	private void actionInterruptionCancel()
	{
		try
		{
			trackerManager.cancelInterruption();
		}
		catch (Exception ex)
		{
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
		}
	}

	// ~ Methods
	// =======================================================

	static boolean valueTrackerTimerBlink = false;

	@Override
	public void track(Session session, Interruption interruption)
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

			String timeString = String.format("%02d:%02d", hours, minutes, seconds);
			valueBreakTrackerTimer.setText(timeString);
		}
		{
			valueTrackerTimerRemaining.setText("XXX");
		}

		if (trackerManager.isPaused())
		{
			if (valueTrackerTimerBlink)
				valueTrackerTimer.setForeground(Color.decode("#161F26"));
			else
				valueTrackerTimer.setForeground(Color.decode("#ffffff"));
			valueTrackerTimerBlink = !valueTrackerTimerBlink;
		}
	}

	@Override
	public void update(Session session, Interruption interruption)
	{
		if (trackerManager.isRunning())
		{
			textTrackerTimerProject.setText(session.getTask().getProject().getDescription());
			textTrackerTimerTask.setText(session.getTask().getDescription());

			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date resultdate = new Date(session.getInitTime());
			String startedString = String.format("%sh", sdf.format(resultdate));
			valueTrackerTimerStarted.setText(startedString);
		}
		if (trackerManager.isInterrupted())
		{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date resultdate = new Date(interruption.getInitTime());
			String startedString = String.format("Break started at %sh", sdf.format(resultdate));
			labelBreakTrackerTitle.setText(startedString);
		}
	}
}
