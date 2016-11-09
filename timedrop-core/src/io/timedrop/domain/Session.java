package io.timedrop.domain;

public class Session extends Object
{
	private Task task;

	private long idSession;
	private long initTime;
	private long duration;
	private long estimation;

	// ~ Methods
	// =======================================================

	public Session()
	{
		task = new Task();

		idSession = 0;
		initTime = 0;
		duration = 0;
		estimation = 0;
	}

	// ~ Methods
	// =======================================================

	public Task getTask()
	{
		return task;
	}

	public void setTask(Task task)
	{
		this.task = task;
	}

	public long getIdSession()
	{
		return idSession;
	}

	public void setIdSession(long idSession)
	{
		this.idSession = idSession;
	}

	public long getInitTime()
	{
		return initTime;
	}

	public void setInitTime(long initTime)
	{
		this.initTime = initTime;
	}

	public long getDuration()
	{
		return duration;
	}

	public void setDuration(long duration)
	{
		this.duration = duration;
	}

	public long getEstimation()
	{
		return estimation;
	}

	public void setEstimation(long estimation)
	{
		this.estimation = estimation;
	}

}
