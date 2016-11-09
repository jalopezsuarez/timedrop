package io.timedrop.domain;

public class Interruption extends Object
{
	private Session session;
	private Task task;

	private long idBreak;
	private long initTime;
	private long endTime;
	private long duration;

	// ~ Methods
	// =======================================================

	public Interruption()
	{
		session = new Session();
		task = new Task();

		idBreak = 0;
		initTime = System.currentTimeMillis();
		endTime = System.currentTimeMillis();
		duration = 0;
	}

	// ~ Methods
	// =======================================================

	public Session getSession()
	{
		return session;
	}

	public void setSession(Session session)
	{
		this.session = session;
	}

	public Task getTask()
	{
		return task;
	}

	public void setTask(Task task)
	{
		this.task = task;
	}

	public long getIdBreak()
	{
		return idBreak;
	}

	public void setIdBreak(long idBreak)
	{
		this.idBreak = idBreak;
	}

	public long getInitTime()
	{
		return initTime;
	}

	public void setInitTime(long initTime)
	{
		this.initTime = initTime;
	}

	public long getEndTime()
	{
		return endTime;
	}

	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

	public long getDuration()
	{
		return duration;
	}

	public void setDuration(long duration)
	{
		this.duration = duration;
	}

}
