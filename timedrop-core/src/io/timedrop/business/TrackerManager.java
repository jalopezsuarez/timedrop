package io.timedrop.business;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.Timer;

import io.timedrop.domain.Interruption;
import io.timedrop.domain.Session;
import io.timedrop.domain.Task;
import io.timedrop.service.BreakService;
import io.timedrop.service.SessionService;

public class TrackerManager
{
	private SessionService sessionService;
	private Session session;

	private BreakService interruptionService;
	private Interruption interruption;

	private TrackerInterface ui;

	private boolean enabled;
	private boolean running;
	private boolean breaks;

	private Timer timer;

	private long internal;

	// ~ Methods
	// =======================================================

	public TrackerManager()
	{
		sessionService = new SessionService();
		session = new Session();

		interruptionService = new BreakService();
		interruption = new Interruption();

		// -------------------------------------------------------

		internal = 0;

		// -------------------------------------------------------

		enabled = true;
		running = false;

		// -------------------------------------------------------

		timer = new Timer(1000, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					run(false);
					internal++;
				}
				catch (Exception ex)
				{
				}
			}
		});
		timer.start();
	}

	public void run(boolean update) throws Exception
	{
		if (enabled && breaks)
		{
			long duration = interruption.getDuration() + 1;
			interruption.setDuration(duration);
			interruptionService.process(interruption);
			if (ui != null && update)
				ui.update(session, interruption);
			if (ui != null)
				ui.track(session, interruption);
		}
		else if (enabled && running)
		{
			long duration = session.getDuration() + 1;
			session.setDuration(duration);
			sessionService.process(session);
			if (ui != null && update)
				ui.update(session, interruption);
			if (ui != null)
				ui.track(session, interruption);
		}

		// -------------------------------------------------------

		notifications();
	}

	// ~ Methods
	// =======================================================

	public void setUI(TrackerInterface ui)
	{
		this.ui = ui;

	}

	public void updateUI()
	{
		try
		{
			run(true);
		}
		catch (Exception ex)
		{
		}
	}

	// ~ Methods
	// =======================================================

	public void startTracker(Task task, long estimation) throws Exception
	{
		enabled = false;
		running = false;
		breaks = false;

		// -------------------------------------------------------

		Session sessionTracker = new Session();

		sessionTracker.getTask().getProject().getOrganization().setDescription(task.getProject().getOrganization().getDescription());
		sessionTracker.getTask().getProject().getOrganization().setIdOrganization(task.getProject().getOrganization().getIdOrganization());
		sessionTracker.getTask().getProject().setDescription(task.getProject().getDescription());
		sessionTracker.getTask().getProject().setIdProject(task.getProject().getIdProject());
		sessionTracker.getTask().setDescription(task.getDescription());
		sessionTracker.getTask().setIdTask(task.getIdTask());

		sessionTracker.setEstimation(estimation);
		sessionTracker.setDuration(0);

		sessionService.generate(sessionTracker);
		session = sessionTracker;

		// -------------------------------------------------------

		enabled = true;
		running = true;
		breaks = false;
		run(true);
	}

	// ~ Methods
	// =======================================================

	public boolean isInterrupted()
	{
		return breaks;
	}

	public void startInterruption() throws Exception
	{
		enabled = false;
		running = false;
		breaks = false;

		// -------------------------------------------------------

		Interruption interruptionTracker = new Interruption();
		interruptionTracker.setSession(session);
		interruptionTracker.setDuration(0);

		interruptionService.process(interruptionTracker);
		interruption = interruptionTracker;

		// -------------------------------------------------------

		enabled = true;
		running = false;
		breaks = true;
		run(true);
	}

	public void commitInterruption(Task task) throws Exception
	{
		enabled = false;
		running = false;
		breaks = false;

		// -------------------------------------------------------

		interruption.getTask().getProject().getOrganization().setDescription(task.getProject().getOrganization().getDescription());
		interruption.getTask().getProject().getOrganization().setIdOrganization(task.getProject().getOrganization().getIdOrganization());
		interruption.getTask().getProject().setDescription(task.getProject().getDescription());
		interruption.getTask().getProject().setIdProject(task.getProject().getIdProject());
		interruption.getTask().setDescription(task.getDescription());
		interruption.getTask().setIdTask(task.getIdTask());

		interruptionService.process(interruption);		

		// -------------------------------------------------------

		enabled = true;
		running = true;
		breaks = false;
		run(true);
	}

	public void cancelInterruption() throws Exception
	{
		enabled = false;
		running = false;
		breaks = false;

		// -------------------------------------------------------

		interruptionService.remove(interruption);

		// -------------------------------------------------------

		enabled = true;
		running = true;
		breaks = false;
		run(true);
	}

	// ~ Methods
	// =======================================================

	public void increaseTimer() throws Exception
	{
		enabled = false;

		// -------------------------------------------------------

		long duration = session.getDuration();
		duration = duration + 60;
		session.setDuration(duration);
		sessionService.process(session);
		ui.track(session, interruption);

		// -------------------------------------------------------

		enabled = true;
	}

	public void decreaseTimer() throws Exception
	{
		enabled = false;

		// -------------------------------------------------------

		long duration = session.getDuration();
		duration = duration - 60;
		duration = duration > 0 ? duration : 0;
		session.setDuration(duration);
		sessionService.process(session);
		ui.track(session, interruption);

		// -------------------------------------------------------

		enabled = true;
	}

	public void increaseInterruption() throws Exception
	{
		enabled = false;

		// -------------------------------------------------------

		long duration = interruption.getDuration();
		duration = duration + 60;
		interruption.setDuration(duration);
		interruptionService.process(interruption);
		ui.track(session, interruption);

		// -------------------------------------------------------

		enabled = true;
	}

	public void decreaseInterruption() throws Exception
	{
		enabled = false;

		// -------------------------------------------------------

		long duration = interruption.getDuration();
		duration = duration - 60;
		duration = duration > 0 ? duration : 0;
		interruption.setDuration(duration);
		interruptionService.process(interruption);
		ui.track(session, interruption);

		// -------------------------------------------------------

		enabled = true;
	}

	// ~ Methods
	// =======================================================

	public void notifications()
	{
		if (enabled && breaks)
		{
			long duration = interruption.getDuration();
			if (duration % 60 == 0)
			{
				long hours = TimeUnit.SECONDS.toHours(duration);
				duration -= TimeUnit.HOURS.toSeconds(hours);
				long minutes = TimeUnit.SECONDS.toMinutes(duration);
				duration -= TimeUnit.MINUTES.toSeconds(minutes);

				String timeString = String.format("%02d:%02d", hours, minutes);
				NotificationCenter.notify("Interrupted " + timeString, session.getTask().getDescription() + " - " + session.getTask().getProject().getDescription(), 15);
			}
		}
		else if (enabled && running)
		{
			long duration = session.getDuration();
			if (duration % 60 == 0)
			{
				long hours = TimeUnit.SECONDS.toHours(duration);
				duration -= TimeUnit.HOURS.toSeconds(hours);
				long minutes = TimeUnit.SECONDS.toMinutes(duration);
				duration -= TimeUnit.MINUTES.toSeconds(minutes);

				String timeString = String.format("%02d:%02d", hours, minutes);
				NotificationCenter.notify("Tracking " + timeString, session.getTask().getDescription() + " - " + session.getTask().getProject().getDescription(), 15);
			}
		}
		else
		{
			if (internal % 60 == 0)
			{
				NotificationCenter.notify("No task", "Start tracking by selecting the task you are working on", 15);
			}
		}
	}

	public boolean isRunning()
	{
		return enabled && running;
	}

}
