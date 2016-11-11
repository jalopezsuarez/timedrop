package io.timedrop.domain;

public class Session extends Object
{
	private Task task;

	private long idSession;
	private long initiated;
	private long duration;
	private long estimation;
	private String annotation;
	private long record;
	private long version;

	// ~ Methods
	// =======================================================

	public Session()
	{
		task = new Task();

		idSession = 0;
		initiated = 0;
		duration = 0;
		estimation = 0;
		annotation = "";
		record = System.currentTimeMillis();
		version = System.currentTimeMillis();
	}

	// ~ Methods
	// =======================================================

	public void copy(Session copy)
	{
		if (copy != null & copy instanceof Session)
		{
			task.copy(copy.getTask());

			idSession = copy.getIdSession();
			initiated = copy.getInitiated();
			duration = copy.getDuration();
			estimation = copy.getEstimation();
			annotation = copy.getAnnotation();
			record = copy.getRecord();
			version = copy.getVersion();
		}
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

	public long getEstimation()
	{
		return estimation;
	}

	public void setEstimation(long estimation)
	{
		this.estimation = estimation;
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
