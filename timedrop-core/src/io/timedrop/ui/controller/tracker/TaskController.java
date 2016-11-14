package io.timedrop.ui.controller.tracker;

import io.timedrop.business.TrackerInterface;
import io.timedrop.business.TrackerManager;
import io.timedrop.business.report.ReportManager;
import io.timedrop.domain.Organization;
import io.timedrop.domain.Session;
import io.timedrop.domain.Task;
import io.timedrop.service.SessionService;
import io.timedrop.service.TaskService;
import io.timedrop.ui.components.TextField;
import io.timedrop.ui.components.ImageButton;
import io.timedrop.ui.components.Frame;
import io.timedrop.ui.components.Label;
import io.timedrop.ui.components.Panel;
import io.timedrop.ui.components.TextArea;
import io.timedrop.ui.components.layout.GridHelper;
import io.timedrop.ui.components.selector.DarkComboBox;
import io.timedrop.ui.components.selector.LightComboBox;
import io.timedrop.ui.components.selector.ProjectSearchBox;
import io.timedrop.ui.components.selector.SelectorAction;
import io.timedrop.ui.components.selector.TaskSearchBox;
import io.timedrop.ui.components.table.TableView;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class TaskController extends Frame implements TrackerInterface
{
	private static final long serialVersionUID = 7847828219876847244L;

	// =======================================================

	private Frame context;

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

	private Panel panelTracker;

	// -------------------------------------------------------

	private Panel panelTrackerEditor;

	private Panel panelTrackerEditorProject;
	private Panel panelTrackerEditorTask;
	private Panel panelTrackerEditorTimer;
	private Panel panelTrackerEditorEstimator;

	// -------------------------------------------------------

	private Label labelTrackerProjectTitle;
	private DarkComboBox<Object> comboTrackerProject;

	// -------------------------------------------------------

	private Label labelTrackerTaskTitle;
	private DarkComboBox<Object> comboTrackerTask;

	// -------------------------------------------------------

	private Label labelEstimationTitle;

	private ImageButton buttonEstimationIncrease;
	private ImageButton buttonEstimationDecrease;
	private Label valueEstimationTask;

	private Label labelEstimationWorking;
	private Label valueEstimationWorking;

	private Label labelEstimationRemaining;
	private Label valueEstimationRemaining;

	private Label labelEstimationInitial;
	private Label valueEstimationInitial;

	private Label labelEstimationCurrent;
	private Label valueEstimationCurrent;

	// -------------------------------------------------------

	private Panel panelTrackerManager;

	private TextArea textTrackerTimerTask;
	private TextArea textTrackerTimerProject;

	private ImageButton buttonTrackerTimerIncrease;
	private ImageButton buttonTrackerTimerDecrease;

	private Label valueTrackerTimer;
	private Label valueTrackerTimerStarted;
	private Label valueTrackerTimerRemaining;

	// -------------------------------------------------------

	private Panel panelTrackerDetails;
	private Panel panelTrackerAnnotation;

	private ImageButton buttonTrackerDetails;
	private Label labelTrackerDetails;
	private TextArea editorTrackerDetails;

	private ImageButton buttonTrackerAnnotation;
	private Label labelTrackerAnnotation;
	private TextArea editorTrackerAnnotation;

	// -------------------------------------------------------

	private Panel panelBreakManager;
	private Panel panelBreakEmpty;

	private Panel panelBreakTracker;
	private Panel panelBreakTrackerEditor;

	private Panel panelBreakTrackerProject;
	private Panel panelBreakTrackerTask;
	private Panel panelBreakTrackerReason;
	private Panel panelBreakTrackerTimer;

	// -------------------------------------------------------

	private Label labelBreakEmptyTitle;
	private ImageButton buttonBreakEmptyHelper;
	private Label labelBreakEmptyHelper;

	// -------------------------------------------------------

	private Label labelBreakEditorProject;
	private LightComboBox<Object> comboBreakEditorProject;

	private Label labelBreakEditorTask;
	private LightComboBox<Object> comboBreakEditorTask;

	private Label labelBreakEditorReason;
	private TextArea editorBreakEditorReason;

	// -------------------------------------------------------

	private Label labelBreakTrackerTitle;
	private ImageButton buttonBreakTrackerIncrease;
	private ImageButton buttonBreakTrackerDecrease;
	private ImageButton buttonBreakTrackerCommit;
	private ImageButton buttonBreakTrackerCancel;

	private Label valueBreakTrackerTimer;

	// -------------------------------------------------------

	private Panel panelBreakList;

	private Label labelInterruptionsTitle;
	private Label labelInterruptionsSummary;

	private TableView tableTaskInterruptions;

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
			panelTracker = new Panel();
			panelTracker.setLayout(new BorderLayout());
			panelTracker.setBackground(Color.decode("#212B33"));
			{
				panelTrackerEditor = new Panel();
				panelTrackerEditor.setLayout(new CardLayout());
				{
					panelTrackerEditorProject = new Panel();
					panelTrackerEditorProject.setBackground(Color.decode("#212B33"));
					panelTrackerEditorProject.setLayout(new BorderLayout());
					{
						labelTrackerProjectTitle = new Label("PROJECT");
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
					panelTrackerEditorTask = new Panel();
					panelTrackerEditorTask.setBackground(Color.decode("#212B33"));
					panelTrackerEditorTask.setLayout(new BorderLayout());
					{
						labelTrackerTaskTitle = new Label("TASK");
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
					panelTrackerEditorTimer = new Panel();
					panelTrackerEditorTimer.setFocusTraversalPolicyProvider(true);
					panelTrackerEditorTimer.setBackground(Color.decode("#212B33"));
					panelTrackerEditorTimer.setLayout(new BorderLayout());
					{
						labelEstimationTitle = new Label("ESTIMATED HOURS");
						labelEstimationTitle.setFont(UIManager.getFont("Label.font").deriveFont(12f));
						labelEstimationTitle.setForeground(Color.decode("#525B61"));
						labelEstimationTitle.setBackground(Color.decode("#212B33"));
						labelEstimationTitle.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
						panelTrackerEditorTimer.add(labelEstimationTitle, BorderLayout.NORTH);
					}
					{
						panelTrackerEditorEstimator = new Panel();
						panelTrackerEditorEstimator.setBackground(Color.decode("#161F26"));
						panelTrackerEditorEstimator.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode("#525B61")));

						GridHelper layoutTrackerEditorEstimation = new GridHelper(panelTrackerEditorEstimator);
						{
							buttonEstimationDecrease = new ImageButton("estimate_decrease");
							buttonEstimationDecrease.setToolTipText("Decrease estimation DOWN");
							layoutTrackerEditorEstimation.constrains().gridheight = 2;
							layoutTrackerEditorEstimation.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerEditorEstimation.constrains().insets = new Insets(0, 10, 0, 5);
							layoutTrackerEditorEstimation.add(buttonEstimationDecrease, 0, 1);
						}
						{
							buttonEstimationIncrease = new ImageButton("estimate_increase");
							buttonEstimationIncrease.setToolTipText("Increase estimation UP");
							layoutTrackerEditorEstimation.constrains().gridheight = 2;
							layoutTrackerEditorEstimation.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerEditorEstimation.constrains().insets = new Insets(0, 5, 0, 5);
							layoutTrackerEditorEstimation.add(buttonEstimationIncrease, 1, 1);
						}
						{
							valueEstimationTask = new Label("--h");
							valueEstimationTask.setFont(UIManager.getFont("TextField.font").deriveFont(32f));
							valueEstimationTask.setForeground(Color.decode("#79ADD7"));
							valueEstimationTask.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().gridheight = 2;
							layoutTrackerEditorEstimation.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerEditorEstimation.constrains().insets = new Insets(0, 5, 0, 10);
							layoutTrackerEditorEstimation.add(valueEstimationTask, 2, 1);
						}
						{
							labelEstimationWorking = new Label("WORKED");
							labelEstimationWorking.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelEstimationWorking.setForeground(Color.decode("#A6AEB4"));
							labelEstimationWorking.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().insets = new Insets(3, 15, 0, 0);
							layoutTrackerEditorEstimation.add(labelEstimationWorking, 3, 1);
						}
						{
							valueEstimationWorking = new Label("--");
							valueEstimationWorking.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							valueEstimationWorking.setForeground(Color.decode("#ffffff"));
							valueEstimationWorking.setBackground(Color.decode("#161F26"));
							valueEstimationWorking.setHorizontalAlignment(JLabel.RIGHT);
							layoutTrackerEditorEstimation.constrains().insets = new Insets(3, 10, 0, 0);
							layoutTrackerEditorEstimation.add(valueEstimationWorking, 4, 1);
						}
						{
							labelEstimationRemaining = new Label("REMAINING");
							labelEstimationRemaining.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelEstimationRemaining.setForeground(Color.decode("#A6AEB4"));
							labelEstimationRemaining.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().insets = new Insets(2, 15, 0, 0);
							layoutTrackerEditorEstimation.add(labelEstimationRemaining, 3, 2);
						}
						{
							valueEstimationRemaining = new Label("--");
							valueEstimationRemaining.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							valueEstimationRemaining.setForeground(Color.decode("#ffffff"));
							valueEstimationRemaining.setBackground(Color.decode("#161F26"));
							valueEstimationRemaining.setHorizontalAlignment(JLabel.RIGHT);
							layoutTrackerEditorEstimation.constrains().insets = new Insets(2, 10, 0, 0);
							layoutTrackerEditorEstimation.add(valueEstimationRemaining, 4, 2);
						}
						{
							labelEstimationInitial = new Label("INITIAL");
							labelEstimationInitial.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelEstimationInitial.setForeground(Color.decode("#A6AEB4"));
							labelEstimationInitial.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().insets = new Insets(3, 25, 0, 0);
							layoutTrackerEditorEstimation.add(labelEstimationInitial, 5, 1);
						}
						{
							valueEstimationInitial = new Label("--");
							valueEstimationInitial.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							valueEstimationInitial.setForeground(Color.decode("#ffffff"));
							valueEstimationInitial.setBackground(Color.decode("#161F26"));
							valueEstimationInitial.setHorizontalAlignment(JLabel.RIGHT);
							layoutTrackerEditorEstimation.constrains().insets = new Insets(3, 10, 0, 0);
							layoutTrackerEditorEstimation.add(valueEstimationInitial, 6, 1);
						}
						{
							labelEstimationCurrent = new Label("CURRENT");
							labelEstimationCurrent.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelEstimationCurrent.setForeground(Color.decode("#A6AEB4"));
							labelEstimationCurrent.setBackground(Color.decode("#161F26"));
							layoutTrackerEditorEstimation.constrains().insets = new Insets(2, 25, 0, 0);
							layoutTrackerEditorEstimation.add(labelEstimationCurrent, 5, 2);
						}
						{
							valueEstimationCurrent = new Label("--");
							valueEstimationCurrent.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							valueEstimationCurrent.setForeground(Color.decode("#edc45a"));
							valueEstimationCurrent.setBackground(Color.decode("#161F26"));
							valueEstimationCurrent.setHorizontalAlignment(JLabel.RIGHT);
							layoutTrackerEditorEstimation.constrains().insets = new Insets(2, 10, 0, 0);
							layoutTrackerEditorEstimation.add(valueEstimationCurrent, 6, 2);
						} 
						{
							layoutTrackerEditorEstimation.constrains().gridheight = 2;
							layoutTrackerEditorEstimation.constrains().gridwidth = 1;
							layoutTrackerEditorEstimation.constrains().weightx = 1.0;
							layoutTrackerEditorEstimation.constrains().weighty = 1.0;
							layoutTrackerEditorEstimation.constrains().fill = GridBagConstraints.BOTH;
							layoutTrackerEditorEstimation.add(new Label(), 7, 1);
						}
						panelTrackerEditorTimer.add(panelTrackerEditorEstimator, BorderLayout.CENTER);
					}
					panelTrackerEditor.add(panelTrackerEditorTimer);
				}
				panelTracker.add(panelTrackerEditor, BorderLayout.NORTH);
			}
			{
				Panel panelTrackerSection = new Panel();
				panelTrackerSection.setLayout(new CardLayout());
				{
					panelTrackerManager = new Panel();
					panelTrackerManager.setBackground(Color.decode("#212B33"));
					GridHelper layoutTrackerManager = new GridHelper(panelTrackerManager);
					{
						Panel panelTrackerManagerInformation = new Panel();
						panelTrackerManagerInformation.setBackground(Color.decode("#212B33"));
						panelTrackerManagerInformation.setLayout(new BorderLayout());
						{
							textTrackerTimerTask = new TextArea("No task");
							textTrackerTimerTask.setFont(UIManager.getFont("TextField.font").deriveFont(20f));
							textTrackerTimerTask.setBackground(Color.decode("#212B33"));
							textTrackerTimerTask.setForeground(Color.decode("#ffffff"));
							panelTrackerManagerInformation.add(textTrackerTimerTask, BorderLayout.NORTH);
						}
						{
							textTrackerTimerProject = new TextArea("Start tracking by selecting the task you are working on");
							textTrackerTimerProject.setBackground(Color.decode("#212B33"));
							textTrackerTimerProject.setForeground(Color.decode("#A6AEB4"));
							textTrackerTimerProject.setFont(UIManager.getFont("Label.font").deriveFont(14f));
							textTrackerTimerProject.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
							panelTrackerManagerInformation.add(textTrackerTimerProject, BorderLayout.CENTER);
						}
						layoutTrackerManager.constrains().gridheight = 2;
						layoutTrackerManager.constrains().gridwidth = 1;
						layoutTrackerManager.constrains().weightx = 1.0;
						layoutTrackerManager.constrains().insets = new Insets(12, 12, 12, 6);
						layoutTrackerManager.add(panelTrackerManagerInformation, 0, 0);
					}
					{
						Panel panelTrackerManagerTimer = new Panel();
						panelTrackerManagerTimer.setBackground(Color.decode("#212B33"));
						GridHelper layoutTrackerManagerTimer = new GridHelper(panelTrackerManagerTimer);
						{
							buttonTrackerTimerDecrease = new ImageButton("tracking_decrease");
							buttonTrackerTimerDecrease.setBackground(Color.decode("#212B33"));
							buttonTrackerTimerDecrease.setToolTipText("Decrease tracking time");
							layoutTrackerManagerTimer.constrains().insets = new Insets(0, 0, 0, 10);
							layoutTrackerManagerTimer.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerManagerTimer.add(buttonTrackerTimerDecrease, 0, 0);
						}
						{
							buttonTrackerTimerIncrease = new ImageButton("tracking_increase");
							buttonTrackerTimerIncrease.setBackground(Color.decode("#212B33"));
							buttonTrackerTimerIncrease.setToolTipText("Increase tracking time");
							layoutTrackerManagerTimer.constrains().insets = new Insets(0, 0, 0, 10);
							layoutTrackerManagerTimer.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerManagerTimer.add(buttonTrackerTimerIncrease, 1, 0);
						}
						{
							valueTrackerTimer = new Label("--:--");
							valueTrackerTimer.setFont(UIManager.getFont("TextField.font").deriveFont(32f));
							valueTrackerTimer.setForeground(Color.decode("#ffffff"));
							valueTrackerTimer.setBackground(Color.decode("#212B33"));
							valueTrackerTimer.setHorizontalAlignment(JLabel.RIGHT);
							valueTrackerTimer.setToolTipText("Pause/Resume current task CTRL+P");
							layoutTrackerManagerTimer.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerManagerTimer.constrains().gridwidth = 3;
							layoutTrackerManagerTimer.constrains().weightx = 1.0;
							layoutTrackerManagerTimer.constrains().insets = new Insets(2, 0, 0, 0);
							layoutTrackerManagerTimer.add(valueTrackerTimer, 2, 0);
						}
						{
							buttonTrackerDetails = new ImageButton("tracking_details");
							buttonTrackerDetails.setBackground(Color.decode("#212B33"));
							buttonTrackerDetails.setToolTipText("Task information CTRL+D");
							layoutTrackerManagerTimer.constrains().insets = new Insets(0, 0, 0, 10);
							layoutTrackerManagerTimer.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerManagerTimer.add(buttonTrackerDetails, 0, 1);
						}
						{
							buttonTrackerAnnotation = new ImageButton("tracking_annotation");
							buttonTrackerAnnotation.setBackground(Color.decode("#212B33"));
							buttonTrackerAnnotation.setToolTipText("Session annotations CTRL+S");
							layoutTrackerManagerTimer.constrains().insets = new Insets(0, 0, 0, 10);
							layoutTrackerManagerTimer.constrains().anchor = GridBagConstraints.CENTER;
							layoutTrackerManagerTimer.add(buttonTrackerAnnotation, 1, 1);
						}
						{
							valueTrackerTimerStarted = new Label("--:--h");
							valueTrackerTimerStarted.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							valueTrackerTimerStarted.setForeground(Color.decode("#A6AEB4"));
							valueTrackerTimerStarted.setBackground(Color.decode("#212B33"));
							valueTrackerTimerStarted.setHorizontalAlignment(JLabel.RIGHT);
							valueTrackerTimerStarted.setToolTipText("Generate tracker report CTRL+R");
							layoutTrackerManagerTimer.constrains().weightx = 1.0;
							layoutTrackerManagerTimer.add(valueTrackerTimerStarted, 2, 1);
						}
						{
							Label separator = new Label(" / ");
							separator.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							separator.setForeground(Color.decode("#A6AEB4"));
							separator.setBackground(Color.decode("#212B33"));
							separator.setHorizontalAlignment(JLabel.RIGHT);
							layoutTrackerManagerTimer.add(separator, 3, 1);
						}
						{
							valueTrackerTimerRemaining = new Label("--h");
							valueTrackerTimerRemaining.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							valueTrackerTimerRemaining.setForeground(Color.decode("#A6AEB4"));
							valueTrackerTimerRemaining.setBackground(Color.decode("#212B33"));
							valueTrackerTimerRemaining.setHorizontalAlignment(JLabel.RIGHT);
							valueTrackerTimerRemaining.setToolTipText("Generate tracker report CTRL+R");
							layoutTrackerManagerTimer.constrains().insets = new Insets(0, 0, 0, 2);
							layoutTrackerManagerTimer.add(valueTrackerTimerRemaining, 4, 1);
						}
						layoutTrackerManager.constrains().insets = new Insets(0, 6, 6, 12);
						layoutTrackerManager.add(panelTrackerManagerTimer, 1, 0);
					}
					panelTrackerSection.add(panelTrackerManager);
				}
				{
					panelTrackerDetails = new Panel();
					panelTrackerDetails.setLayout(new BorderLayout());
					panelTrackerDetails.setBackground(Color.decode("#212B33"));
					panelTrackerDetails.setVisible(false);
					{
						{
							labelTrackerDetails = new Label("TASK DETAILS");
							labelTrackerDetails.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelTrackerDetails.setForeground(Color.decode("#525B61"));
							labelTrackerDetails.setBackground(Color.decode("#212B33"));
							labelTrackerDetails.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
							panelTrackerDetails.add(labelTrackerDetails, BorderLayout.NORTH);
						}
						{
							editorTrackerDetails = new TextArea();
							editorTrackerDetails.setFont(UIManager.getFont("TextField.font").deriveFont(16f));
							editorTrackerDetails.setCaretColor(Color.decode("#ffffff"));
							editorTrackerDetails.setForeground(Color.decode("#ffffff"));
							editorTrackerDetails.setBackground(Color.decode("#212B33"));
							editorTrackerDetails.setBorder(BorderFactory.createEmptyBorder(0, 12, 6, 6));
							panelTrackerDetails.add(editorTrackerDetails, BorderLayout.CENTER);
						}
						panelTrackerSection.add(panelTrackerDetails);
					}
				}
				{
					panelTrackerAnnotation = new Panel();
					panelTrackerAnnotation.setLayout(new BorderLayout());
					panelTrackerAnnotation.setBackground(Color.decode("#212B33"));
					panelTrackerAnnotation.setVisible(false);
					{
						{
							labelTrackerAnnotation = new Label("SESSION ANNOTATIONS");
							labelTrackerAnnotation.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelTrackerAnnotation.setForeground(Color.decode("#525B61"));
							labelTrackerAnnotation.setBackground(Color.decode("#212B33"));
							labelTrackerAnnotation.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
							panelTrackerAnnotation.add(labelTrackerAnnotation, BorderLayout.NORTH);
						}
						{
							editorTrackerAnnotation = new TextArea();
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
			panelBreakManager = new Panel();
			panelBreakManager.setLayout(new CardLayout());
			panelBreakManager.setBackground(Color.decode("#FFFFFF"));
			panelBreakManager.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode("#DCDCDC")));
			{
				panelBreakEmpty = new Panel();
				GridHelper layoutBreakEmpty = new GridHelper(panelBreakEmpty);
				panelBreakEmpty.setBackground(Color.decode("#FFFFFF"));
				{
					labelBreakEmptyTitle = new Label("INTERRUPTIONS");
					labelBreakEmptyTitle.setForeground(Color.decode("#9B9B9B"));
					labelBreakEmptyTitle.setBackground(Color.decode("#FFFFFF"));
					labelBreakEmptyTitle.setFont(UIManager.getFont("Label.font").deriveFont(12f));
					layoutBreakEmpty.constrains().weightx = 1.0;
					layoutBreakEmpty.constrains().insets = new Insets(2, 8, 2, 8);
					layoutBreakEmpty.add(labelBreakEmptyTitle, 0, 0);
				}
				{
					Panel panelEmptyHelper = new Panel();
					GridHelper layoutEmptyHelper = new GridHelper(panelEmptyHelper);
					panelEmptyHelper.setBackground(Color.decode("#F3F3F3"));
					panelEmptyHelper.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.decode("#DCDCDC")));
					{
						buttonBreakEmptyHelper = new ImageButton("interruption_create_b");
						buttonBreakEmptyHelper.setForeground(Color.decode("#9B9B9B"));
						buttonBreakEmptyHelper.setBackground(Color.decode("#F3F3F3"));
						buttonBreakEmptyHelper.setToolTipText("Interrupt current task CTRL+I");
						layoutEmptyHelper.constrains().weighty = 1.0;
						layoutEmptyHelper.constrains().fill = GridBagConstraints.HORIZONTAL;
						layoutEmptyHelper.constrains().anchor = GridBagConstraints.CENTER;
						layoutEmptyHelper.constrains().insets = new Insets(0, 25, 0, 20);
						layoutEmptyHelper.add(buttonBreakEmptyHelper, 0, 0);
					}
					{
						labelBreakEmptyHelper = new Label("Interrupt current task");
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
				panelBreakTracker = new Panel();
				GridHelper layoutBreakTracker = new GridHelper(panelBreakTracker);
				{
					panelBreakTrackerEditor = new Panel();
					panelBreakTrackerEditor.setLayout(new CardLayout());
					{
						panelBreakTrackerProject = new Panel();
						GridHelper layoutBreakTrackerProject = new GridHelper(panelBreakTrackerProject);
						{
							labelBreakEditorProject = new Label("INTERRUPTION PROJECT");
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
						panelBreakTrackerTask = new Panel();
						GridHelper layoutBreakTrackerTask = new GridHelper(panelBreakTrackerTask);
						{
							labelBreakEditorTask = new Label("INTERRUPTION TASK");
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
						panelBreakTrackerReason = new Panel();
						panelBreakTrackerReason.setLayout(new BorderLayout());
						{
							labelBreakEditorReason = new Label("REASON");
							labelBreakEditorReason.setFont(UIManager.getFont("Label.font").deriveFont(12f));
							labelBreakEditorReason.setForeground(Color.decode("#9B9B9B"));
							labelBreakEditorReason.setBackground(Color.decode("#FFFFFF"));
							labelBreakEditorReason.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
							panelBreakTrackerReason.add(labelBreakEditorReason, BorderLayout.NORTH);
						}
						{
							editorBreakEditorReason = new TextArea();
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
					panelBreakTrackerTimer = new Panel();
					panelBreakTrackerTimer.setForeground(Color.decode("#4A4A4A"));
					panelBreakTrackerTimer.setBackground(Color.decode("#F3F3F3"));
					GridHelper layoutBreakTrackerTimer = new GridHelper(panelBreakTrackerTimer);
					{
						labelBreakTrackerTitle = new Label("Break started at --:--");
						labelBreakTrackerTitle.setForeground(Color.decode("#4A4A4A"));
						labelBreakTrackerTitle.setFont(UIManager.getFont("TextField.font").deriveFont(16f));
						layoutBreakTrackerTimer.constrains().gridwidth = 5;
						layoutBreakTrackerTimer.constrains().insets = new Insets(12, 10, 10, 5);
						layoutBreakTrackerTimer.add(labelBreakTrackerTitle, 0, 0);
					}
					{
						buttonBreakTrackerDecrease = new ImageButton("interruption_decrease");
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 10, 12, 5);
						layoutBreakTrackerTimer.add(buttonBreakTrackerDecrease, 0, 1);
					}
					{
						buttonBreakTrackerIncrease = new ImageButton("interruption_increase");
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 5, 12, 5);
						layoutBreakTrackerTimer.add(buttonBreakTrackerIncrease, 1, 1);
					}
					{
						buttonBreakTrackerCommit = new ImageButton("interruption_commit");
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 5, 12, 5);
						layoutBreakTrackerTimer.add(buttonBreakTrackerCommit, 2, 1);
					}
					{
						buttonBreakTrackerCancel = new ImageButton("interruption_cancel");
						layoutBreakTrackerTimer.constrains().insets = new Insets(0, 5, 12, 10);
						layoutBreakTrackerTimer.add(buttonBreakTrackerCancel, 3, 1);
					}
					{
						layoutBreakTrackerTimer.constrains().weightx = 1.0;
						layoutBreakTrackerTimer.add(new Label(), 4, 1);
					}
					{
						valueBreakTrackerTimer = new Label("--:--");
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
			panelBreakList = new Panel();
			panelBreakList.setLayout(new BorderLayout());
			panelBreakList.setBackground(Color.decode("#ffffff"));
			{
				Panel panelInterruptionsTitle = new Panel();
				panelInterruptionsTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#DCDCDC")));
				GridHelper layoutInterruptionsTitle = new GridHelper(panelInterruptionsTitle);
				{
					labelInterruptionsTitle = new Label("TODAY");
					labelInterruptionsTitle.setFont(UIManager.getFont("Label.font").deriveFont(12f));
					labelInterruptionsTitle.setForeground(Color.decode("#9B9B9B"));
					labelInterruptionsTitle.setBackground(Color.decode("#FFFFFF"));
					layoutInterruptionsTitle.constrains().weightx = 1.0;
					layoutInterruptionsTitle.constrains().anchor = GridBagConstraints.CENTER;
					layoutInterruptionsTitle.constrains().insets = new Insets(2, 8, 2, 8);
					layoutInterruptionsTitle.add(labelInterruptionsTitle, 0, 0);
				}
				{
					labelInterruptionsSummary = new Label("");
					labelInterruptionsSummary.setFont(UIManager.getFont("Label.font").deriveFont(12f));
					labelInterruptionsSummary.setForeground(Color.decode("#9B9B9B"));
					labelInterruptionsSummary.setBackground(Color.decode("#FFFFFF"));
					layoutInterruptionsTitle.constrains().anchor = GridBagConstraints.CENTER;
					layoutInterruptionsTitle.constrains().insets = new Insets(2, 8, 2, 8);
					layoutInterruptionsTitle.add(labelInterruptionsSummary, 1, 0);
				}
				panelBreakList.add(panelInterruptionsTitle, BorderLayout.NORTH);
			}
			{
				tableTaskInterruptions = new TableView(new FactoryCellRenderer());
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
		DefaultTableModel tableModel = (DefaultTableModel) tableTaskInterruptions.getTableModel();
		for (int i = tableModel.getRowCount() - 1; i > -1; i--)
		{
			tableModel.removeRow(i);
		}
		for (Object session : response)
		{
			tableModel.addRow(new Object[] { session });
		}

		// -------------------------------------------------------

		updateInterruptionSummary();
	}

	/**
	 * Definicion de la navegacion con teclado (CTRL / WIN / CMD).
	 * <ul>
	 * <li>CTRL+SHIFT / WIN+SHIFT / CMD+SHIFT: MOSTRAR TRACKER</li>
	 * <li>ENTER: Adelante / Siguiente paso</li>
	 * <li>ESC: Atras / Cerrar tracker</li>
	 * <li>CTRL+P / WIN+P / CMD+P: Pausar/Reanudar tarea actual</li>
	 * <li>CTRL+R / WIN+R / CMD+R: Genear informe de trabajo</li>
	 * <li>CTRL+I / WIN+I / CMD+I: Iniciar interrupcion</li>
	 * <li>CTRL+S / WIN+S / CMD+S: Realizar una anotacion sobre la sesion</li>
	 * <li>CTRL+D / WIN+D / CMD+D: Editar el detalle de la tarea</li>
	 * </ul>
	 */
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
				if (e.getModifiers() != ActionEvent.MOUSE_EVENT_MASK)
					actionTrackerEstimationIncrease();
			}
		};
		Action trackerEstimationDecrease = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				if (e.getModifiers() != ActionEvent.MOUSE_EVENT_MASK)
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
		buttonEstimationIncrease.addActionListener(trackerEstimationIncrease);
		buttonEstimationDecrease.addActionListener(trackerEstimationDecrease);
		// -------------------------------------------------------
		buttonEstimationIncrease.addMouseListener(new MouseAdapter()
		{
			private Timer timerMousePressed = null;
			private int accelerationMousePressed = 250;

			public void mousePressed(MouseEvent e)
			{
				actionTrackerEstimationIncrease();
				timerMousePressed = new Timer(accelerationMousePressed, new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (timerMousePressed.getDelay() <= 25)
							actionTrackerEstimationIncrease();
						timerMousePressed.setDelay(25);
					}
				});
				timerMousePressed.setCoalesce(true);
				timerMousePressed.setRepeats(true);
				timerMousePressed.start();
			}

			public void mouseReleased(MouseEvent e)
			{
				if (timerMousePressed != null)
					timerMousePressed.stop();
				timerMousePressed = null;
			}
		});

		buttonEstimationDecrease.addMouseListener(new MouseAdapter()
		{
			private Timer timerMousePressed = null;
			private int accelerationMousePressed = 250;

			public void mousePressed(MouseEvent e)
			{
				actionTrackerEstimationDecrease();
				timerMousePressed = new Timer(accelerationMousePressed, new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (timerMousePressed.getDelay() <= 25)
							actionTrackerEstimationDecrease();
						timerMousePressed.setDelay(25);
					}
				});
				timerMousePressed.setCoalesce(true);
				timerMousePressed.setRepeats(true);
				timerMousePressed.start();
			}

			public void mouseReleased(MouseEvent e)
			{
				if (timerMousePressed != null)
					timerMousePressed.stop();
				timerMousePressed = null;
			}
		});

		// =======================================================
		Action trackerTimerIncrease = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				if (e.getModifiers() != ActionEvent.MOUSE_EVENT_MASK)
					actionTrackerTaskIncrease();
			}
		};
		Action trackerTimerDecrease = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				if (e.getModifiers() != ActionEvent.MOUSE_EVENT_MASK)
					actionTrackerTaskDecrease();
			}
		};
		Action trackerTimerDetails = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				actionTrackerDetails();
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
			buttonTrackerDetails.addActionListener(trackerTimerDetails);
			buttonTrackerAnnotation.addActionListener(trackerTimerAnnotation);
		}
		// -------------------------------------------------------

		buttonTrackerTimerIncrease.addMouseListener(new MouseAdapter()
		{
			private Timer timerMousePressed = null;
			private int accelerationMousePressed = 250;

			public void mousePressed(MouseEvent e)
			{
				actionTrackerTaskIncrease();
				timerMousePressed = new Timer(accelerationMousePressed, new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (timerMousePressed.getDelay() <= 25)
							actionTrackerTaskIncrease();
						timerMousePressed.setDelay(25);
					}
				});
				timerMousePressed.setCoalesce(true);
				timerMousePressed.setRepeats(true);
				timerMousePressed.start();
			}

			public void mouseReleased(MouseEvent e)
			{
				if (timerMousePressed != null)
					timerMousePressed.stop();
				timerMousePressed = null;
			}
		});

		buttonTrackerTimerDecrease.addMouseListener(new MouseAdapter()
		{
			private Timer timerMousePressed = null;
			private int accelerationMousePressed = 250;

			public void mousePressed(MouseEvent e)
			{
				actionTrackerTaskDecrease();
				timerMousePressed = new Timer(accelerationMousePressed, new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (timerMousePressed.getDelay() <= 25)
							actionTrackerTaskDecrease();
						timerMousePressed.setDelay(25);
					}
				});
				timerMousePressed.setCoalesce(true);
				timerMousePressed.setRepeats(true);
				timerMousePressed.start();
			}

			public void mouseReleased(MouseEvent e)
			{
				if (timerMousePressed != null)
					timerMousePressed.stop();
				timerMousePressed = null;
			}
		});

		// =======================================================

		textTrackerTimerProject.addKeyListener(new KeyListener()
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
					actionTrackerDismiss();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					actionTrackerDismiss();
				}
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				actionTrackerProjectDescription();
			}
		});

		// -------------------------------------------------------

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
					actionTrackerDismiss();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					actionTrackerDismiss();
				}
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				actionTrackerTaskDescription();
			}
		});

		// -------------------------------------------------------

		editorTrackerDetails.addKeyListener(new KeyListener()
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
					actionTrackerDetailsCommit();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					e.consume();
					actionTrackerDetailsDismiss();
				}
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
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
			KeyStroke ctrlStroke = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK);
			KeyStroke metaStroke = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.META_MASK);
			Action action = new AbstractAction()
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
				{
					actionInterruptionBegin();
					actionTrackerDismiss();
				}
			};
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ctrlStroke, "VK_I");
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(metaStroke, "VK_I");
			getRootPane().getActionMap().put("VK_I", action);
		}
		// -------------------------------------------------------
		{
			KeyStroke ctrlStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK);
			KeyStroke metaStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.META_MASK);
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
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ctrlStroke, "VK_R");
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(metaStroke, "VK_R");
			getRootPane().getActionMap().put("VK_R", action);
		}
		// -------------------------------------------------------
		{
			KeyStroke ctrlStroke = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK);
			KeyStroke metaStroke = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.META_MASK);
			Action action = new AbstractAction()
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
				{
					actionTrackerToggle();
				}
			};
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ctrlStroke, "VK_P");
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(metaStroke, "VK_P");
			getRootPane().getActionMap().put("VK_P", action);
		}
		// -------------------------------------------------------
		{
			KeyStroke ctrlStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK);
			KeyStroke metaStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.META_MASK);
			Action action = new AbstractAction()
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
				{
					actionTrackerAnnotation();
				}
			};
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ctrlStroke, "VK_S");
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(metaStroke, "VK_S");
			getRootPane().getActionMap().put("VK_S", action);
		}
		// -------------------------------------------------------
		{
			KeyStroke ctrlStroke = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK);
			KeyStroke metaStroke = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.META_MASK);
			Action action = new AbstractAction()
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
				{
					actionTrackerDetails();
				}
			};
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ctrlStroke, "VK_D");
			getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(metaStroke, "VK_D");
			getRootPane().getActionMap().put("VK_D", action);
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

		Action breakTrackerIncrease = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				if (e.getModifiers() != ActionEvent.MOUSE_EVENT_MASK)
					actionInterruptionIncrease();
			}
		};
		Action breakTrackerDecrease = new AbstractAction()
		{
			private static final long serialVersionUID = 4805192530293724961L;

			public void actionPerformed(ActionEvent e)
			{
				if (e.getModifiers() != ActionEvent.MOUSE_EVENT_MASK)
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
			buttonBreakTrackerIncrease.addActionListener(breakTrackerIncrease);
			buttonBreakTrackerDecrease.addActionListener(breakTrackerDecrease);
			buttonBreakTrackerCommit.addActionListener(breakTrackerCommit);
			buttonBreakTrackerCancel.addActionListener(breakTrackerCancel);
		}
		// -------------------------------------------------------

		buttonBreakTrackerIncrease.addMouseListener(new MouseAdapter()
		{
			private Timer timerMousePressed = null;
			private int accelerationMousePressed = 250;

			public void mousePressed(MouseEvent e)
			{
				actionInterruptionIncrease();
				timerMousePressed = new Timer(accelerationMousePressed, new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (timerMousePressed.getDelay() <= 25)
							actionInterruptionIncrease();
						timerMousePressed.setDelay(25);
					}
				});
				timerMousePressed.setCoalesce(true);
				timerMousePressed.setRepeats(true);
				timerMousePressed.start();
			}

			public void mouseReleased(MouseEvent e)
			{
				if (timerMousePressed != null)
					timerMousePressed.stop();
				timerMousePressed = null;
			}
		});

		buttonBreakTrackerDecrease.addMouseListener(new MouseAdapter()
		{
			private Timer timerMousePressed = null;
			private int accelerationMousePressed = 250;

			public void mousePressed(MouseEvent e)
			{
				actionInterruptionDecrease();
				timerMousePressed = new Timer(accelerationMousePressed, new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (timerMousePressed.getDelay() <= 25)
							actionInterruptionDecrease();
						timerMousePressed.setDelay(25);
					}
				});
				timerMousePressed.setCoalesce(true);
				timerMousePressed.setRepeats(true);
				timerMousePressed.start();
			}

			public void mouseReleased(MouseEvent e)
			{
				if (timerMousePressed != null)
					timerMousePressed.stop();
				timerMousePressed = null;
			}
		});

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

				// -------------------------------------------------------
				{
					trackerEstimation = trackerTask.getReestimate();
					valueEstimationTask.setText(String.format("%dh", trackerEstimation));

					long duration = Math.round(trackerTask.getSummary());
					long hours = TimeUnit.SECONDS.toHours(duration);
					duration -= TimeUnit.HOURS.toSeconds(hours);
					valueEstimationWorking.setText(String.format("%dh", hours));

					long remaining = trackerEstimation - hours;
					valueEstimationRemaining.setText(String.format("%dh", remaining));
					if (remaining > 0)
					{
						valueEstimationRemaining.setForeground(Color.decode("#77d2c2"));
					}
					else
					{
						valueEstimationRemaining.setForeground(Color.decode("#E97C5F"));
					}
				}
				// -------------------------------------------------------

				if (trackerTask.getEstimation() > 0)
				{
					SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM");
					Date resultdate = new Date(trackerTask.getDateEstimation());
					valueEstimationInitial.setText(String.format("%dh. %s", trackerTask.getEstimation(), sdf.format(resultdate)));
				}
				else
				{
					valueEstimationInitial.setText("--");
				}
				if (trackerTask.getReestimate() > 0)
				{
					SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM");
					Date resultdate = new Date(trackerTask.getDateReestimate());
					valueEstimationCurrent.setText(String.format("%dh. %s", trackerTask.getReestimate(), sdf.format(resultdate)));
				}
				else
				{
					valueEstimationCurrent.setText("--");
				}
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
			valueEstimationWorking.setText("--");
			valueEstimationRemaining.setText("--");
			valueEstimationInitial.setText("--");
			valueEstimationCurrent.setText("--");
		}
	}

	private void actionTrackerEstimationIncrease()
	{
		trackerEstimation++;

		// -------------------------------------------------------

		valueEstimationTask.setText(String.format("%dh", trackerEstimation));

		long duration = Math.round(trackerTask.getSummary());
		long hours = TimeUnit.SECONDS.toHours(duration);
		duration -= TimeUnit.HOURS.toSeconds(hours);
		valueEstimationWorking.setText(String.format("%dh", hours));

		long remaining = trackerEstimation - hours;
		valueEstimationRemaining.setText(String.format("%dh", remaining));
		if (remaining > 0)
		{
			valueEstimationRemaining.setForeground(Color.decode("#77d2c2"));
		}
		else
		{
			valueEstimationRemaining.setForeground(Color.decode("#E97C5F"));
		}
	}

	private void actionTrackerEstimationDecrease()
	{
		trackerEstimation = trackerEstimation > 0 ? --trackerEstimation : 0;

		// -------------------------------------------------------

		valueEstimationTask.setText(String.format("%dh", trackerEstimation));

		long duration = Math.round(trackerTask.getSummary());
		long hours = TimeUnit.SECONDS.toHours(duration);
		duration -= TimeUnit.HOURS.toSeconds(hours);
		valueEstimationWorking.setText(String.format("%dh", hours));

		long remaining = trackerEstimation - hours;
		valueEstimationRemaining.setText(String.format("%dh", remaining));
		if (remaining > 0)
		{
			valueEstimationRemaining.setForeground(Color.decode("#77d2c2"));
		}
		else
		{
			valueEstimationRemaining.setForeground(Color.decode("#E97C5F"));
		}
	}

	private void actionTrackerBegin()
	{
		try
		{
			trackerManager.startTracker(trackerTask, trackerEstimation);

			trackerTask.setIdTask(0);
			trackerTask.setDescription("");
			trackerTask.setAnnotation("");
			trackerTask.getProject().setIdProject(0);
			trackerTask.getProject().setDescription("");
			trackerTask.getProject().setAnnotation("");
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

	private void actionTrackerProjectDescription()
	{
		try
		{
			trackerManager.saveProjectDescription(textTrackerTimerProject.getText());
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

	private void actionTrackerDetails()
	{
		try
		{
			if (trackerManager.isRunning() || trackerManager.isPaused() || trackerManager.isInterrupted())
			{
				panelTrackerDetails.setVisible(true);
				panelTrackerManager.setVisible(false);
				editorTrackerDetails.setText(trackerManager.getTaskAnnotations());
				editorTrackerDetails.requestFocusInWindow();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void actionTrackerDetailsCommit()
	{
		try
		{
			if (trackerManager.isRunning() || trackerManager.isPaused() || trackerManager.isInterrupted())
			{
				panelTrackerDetails.setVisible(false);
				panelTrackerManager.setVisible(true);
				trackerManager.saveTaskAnnotations(editorTrackerDetails.getText());
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void actionTrackerDetailsDismiss()
	{
		try
		{
			if (trackerManager.isRunning() || trackerManager.isPaused() || trackerManager.isInterrupted())
			{
				panelTrackerDetails.setVisible(false);
				panelTrackerManager.setVisible(true);
			}
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
				editorTrackerAnnotation.setText(trackerManager.getSessionAnnotations());
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
				trackerManager.saveSessionAnnotations(editorTrackerAnnotation.getText());
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
			comboBreakEditorProject.requestFocusInWindow();
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
		if (breakTask.hasTask())
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
			DefaultTableModel tableModel = (DefaultTableModel) tableTaskInterruptions.getTableModel();
			for (int i = tableModel.getRowCount() - 1; i > -1; i--)
			{
				tableModel.removeRow(i);
			}
			for (Object session : response)
			{
				tableModel.addRow(new Object[] { session });
			}

			// -------------------------------------------------------

			updateInterruptionSummary();
		}
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

			String timeString = String.format("%02d:%02d", hours, minutes);
			valueTrackerTimer.setText(timeString);
		}
		{
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
			long duration = TimeUnit.SECONDS.toHours(session.getDuration());
			long estimation = session.getEstimation();
			long summary = TimeUnit.SECONDS.toHours(session.getTask().getSummary());
			long remaining = estimation - duration - summary;

			valueTrackerTimerRemaining.setText(String.format("%dh", remaining));
			if (remaining > 0)
			{
				valueTrackerTimerRemaining.setForeground(Color.decode("#77d2c2"));
			}
			else
			{
				valueTrackerTimerRemaining.setForeground(Color.decode("#E97C5F"));
			}
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
			textTrackerTimerProject.setText(session.getTask().getProject().getDescription());
			textTrackerTimerTask.setText(session.getTask().getDescription());
		}
		{
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

	private void updateInterruptionSummary()
	{
		long valueInterruptionsSummary = 0;
		DefaultTableModel tableModel = (DefaultTableModel) tableTaskInterruptions.getTableModel();
		for (int i = 0; i < tableModel.getRowCount(); i++)
		{
			Object response = tableModel.getValueAt(i, 0);
			if (response != null && response instanceof Session)
			{
				Session session = (Session) tableModel.getValueAt(i, 0);
				valueInterruptionsSummary += session.getDuration();
			}
		}

		String stringInterruptionsSummary = "";
		if (valueInterruptionsSummary > 0)
		{
			long duration = valueInterruptionsSummary;
			long hours = TimeUnit.SECONDS.toHours(duration);
			duration -= TimeUnit.HOURS.toSeconds(hours);
			long minutes = TimeUnit.SECONDS.toMinutes(duration);
			duration -= TimeUnit.MINUTES.toSeconds(minutes);
			if (hours > 0)
				stringInterruptionsSummary = String.format("%02d h %02d min", hours, minutes);
			else
				stringInterruptionsSummary = String.format("%02d min", minutes);
		}
		labelInterruptionsSummary.setText(stringInterruptionsSummary);
	}

	// Class Methods
	// ===============================================================

	private class FactoryCellRenderer extends Panel implements TableCellRenderer
	{
		private static final long serialVersionUID = 8233159691825884868L;

		private TextField fieldProjectDescription;
		private TextField fieldTaskDescription;
		private Label labelSessionInitiated;
		private Label labelSessionDuration;

		public FactoryCellRenderer()
		{
			super();

			GridHelper layout = new GridHelper(this);
			setBackground(Color.decode("#ffffff"));

			Border borderSuperior = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#DCDCDC"));
			Border borderDecorator = BorderFactory.createMatteBorder(0, 4, 0, 0, Color.decode("#77ABD4"));
			setBorder(BorderFactory.createCompoundBorder(borderSuperior, borderDecorator));
			{
				fieldTaskDescription = new TextField("--");
				fieldTaskDescription.setFont(UIManager.getFont("TextField.font").deriveFont(18f));
				fieldTaskDescription.setForeground(Color.decode("#4A4A4A"));
				layout.constrains().weightx = 1.0;
				layout.constrains().insets = new Insets(8, 8, 0, 0);
				layout.add(fieldTaskDescription, 0, 0);
			}
			{
				fieldProjectDescription = new TextField("--");
				fieldProjectDescription.setFont(UIManager.getFont("Label.font").deriveFont(14f));
				fieldProjectDescription.setForeground(Color.decode("#79ADD7"));
				layout.constrains().weightx = 1.0;
				layout.constrains().insets = new Insets(2, 8, 8, 0);
				layout.add(fieldProjectDescription, 0, 1);
			}
			{
				Panel panelSessionInformation = new Panel();
				panelSessionInformation.setLayout(new BorderLayout());
				{
					labelSessionDuration = new Label("--");
					labelSessionDuration.setForeground(Color.decode("#4A4A4A"));
					labelSessionDuration.setFont(UIManager.getFont("TextField.font").deriveFont(24f));
					labelSessionDuration.setHorizontalAlignment(JLabel.RIGHT);
					panelSessionInformation.add(labelSessionDuration, BorderLayout.NORTH);
				}
				{
					labelSessionInitiated = new Label("--");
					labelSessionInitiated.setForeground(Color.decode("#888888"));
					labelSessionInitiated.setFont(UIManager.getFont("TextField.font").deriveFont(16f));
					labelSessionInitiated.setHorizontalAlignment(JLabel.RIGHT);
					panelSessionInformation.add(labelSessionInitiated, BorderLayout.SOUTH);
				}
				layout.constrains().gridheight = 2;
				layout.constrains().anchor = GridBagConstraints.CENTER;
				layout.constrains().insets = new Insets(0, 0, 0, 10);
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

			// -------------------------------------------------------

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

			// -------------------------------------------------------

			return this;
		}

	}
}
