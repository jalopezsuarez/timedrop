package io.timedrop.domain;

public class Task extends Object
{
	private Project project;

	private long idTask;
	private String description;
	private String annotation;
	private long record;
	private long version;

	private long estimation;
	private long reestimate;
	private long dateEstimation;
	private long dateReestimate;
	private long summary;

	// ~ Methods
	// =======================================================

	public Task()
	{
		project = new Project();

		idTask = 0;
		description = "";
		annotation = "";
		record = System.currentTimeMillis();
		version = System.currentTimeMillis();

		estimation = 0;
		reestimate = 0;
		dateEstimation = System.currentTimeMillis();
		dateReestimate = System.currentTimeMillis();
		summary = 0;
	}

	@Override
	public String toString()
	{
		return description;
	}

	// ~ Methods
	// =======================================================

	public void copy(Task copy)
	{
		if (copy != null & copy instanceof Task)
		{
			project.copy(copy.getProject());

			idTask = copy.getIdTask();
			description = copy.getDescription();
			annotation = copy.getAnnotation();
			record = copy.getRecord();
			version = copy.getVersion();

			estimation = copy.getEstimation();
			reestimate = copy.getReestimate();
			dateEstimation = copy.getDateEstimation();
			dateReestimate = copy.getDateReestimate();
			summary = copy.getSummary();
		}
	}

	// ~ Methods
	// =======================================================

	public boolean hasProject()
	{
		boolean response = false;

		if (project != null)
		{
			if (project.getIdProject() > 0 || (project.getDescription() != null && project.getDescription().length() > 0))
			{
				response = true;
			}
		}

		return response;
	}

	public boolean hasTask()
	{
		boolean response = false;

		if (idTask > 0 || (description != null && description.length() > 0))
		{
			response = true;
		}

		return response;
	}

	// ~ Methods
	// =======================================================

	public Project getProject()
	{
		return project;
	}

	public void setProject(Project project)
	{
		this.project = project;
	}

	public long getIdTask()
	{
		return idTask;
	}

	public void setIdTask(long idTask)
	{
		this.idTask = idTask;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
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

	public long getEstimation()
	{
		return estimation;
	}

	public void setEstimation(long estimation)
	{
		this.estimation = estimation;
	}

	public long getReestimate()
	{
		return reestimate;
	}

	public void setReestimate(long reestimate)
	{
		this.reestimate = reestimate;
	}

	public long getDateEstimation()
	{
		return dateEstimation;
	}

	public void setDateEstimation(long dateEstimation)
	{
		this.dateEstimation = dateEstimation;
	}

	public long getDateReestimate()
	{
		return dateReestimate;
	}

	public void setDateReestimate(long dateReestimate)
	{
		this.dateReestimate = dateReestimate;
	}

	public long getSummary()
	{
		return summary;
	}

	public void setSummary(long summary)
	{
		this.summary = summary;
	}

}
