package io.timedrop.domain;

public class Interruption extends Object
{
	private Session session;
	private Task task;

	private long idBreak;
	private long initiated;
	private long duration;
	private String annotation;
	private long record;
	private long version;

	// ~ Methods
	// =======================================================

	public Interruption()
	{
		session = new Session();
		task = new Task();

		idBreak = 0;
		initiated = System.currentTimeMillis();
		duration = 0;
		annotation = "";
		record = System.currentTimeMillis();
		version = System.currentTimeMillis();
	}

	// ~ Methods
	// =======================================================

	public void copy(Interruption copy)
	{
		if (copy != null & copy instanceof Interruption)
		{
			session.copy(copy.getSession());
			task.copy(copy.getTask());

			idBreak = copy.getIdBreak();
			initiated = copy.getInitiated();
			duration = copy.getDuration();
			annotation = copy.getAnnotation();
			record = copy.getRecord();
			version = copy.getVersion();
		}
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

	public long getInitiated()
	{
		return initiated;
	}

	public void setInitiated(long initiated)
	{
		this.initiated = initiated;
	}

	public long getDuration()
	{
		return duration;
	}

	public void setDuration(long duration)
	{
		this.duration = duration;
	}

	public String getAnnotation()
	{
		return annotation;
	}

	public void setAnnotation(String annotation)
	{
		this.annotation = annotation;
	}

	public long getRecord()
	{
		return record;
	}

	public void setRecord(long record)
	{
		this.record = record;
	}

	public long getVersion()
	{
		return version;
	}

	public void setVersion(long version)
	{
		this.version = version;
	}

}
