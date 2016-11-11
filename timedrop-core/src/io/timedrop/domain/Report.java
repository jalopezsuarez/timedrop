package io.timedrop.domain;

public class Report
{
	private long idProject;
	private String projectDescription;

	private long idTask;
	private String taskDescription;
	private long taskEstimation;
	private long taskRestimate;
	private long taskSummary;

	// ~ Methods
	// =======================================================

	public Report()
	{
		idProject = 0;
		projectDescription = "";

		idTask = 0;
		taskDescription = "";
		taskEstimation = 0;
		taskRestimate = 0;
		taskSummary = 0;
	}

	// ~ Methods
	// =======================================================

	public long getIdProject()
	{
		return idProject;
	}

	public void setIdProject(long idProject)
	{
		this.idProject = idProject;
	}

	public String getProjectDescription()
	{
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription)
	{
		this.projectDescription = projectDescription;
	}

	public long getIdTask()
	{
		return idTask;
	}

	public void setIdTask(long idTask)
	{
		this.idTask = idTask;
	}

	public String getTaskDescription()
	{
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription)
	{
		this.taskDescription = taskDescription;
	}

	public long getTaskEstimation()
	{
		return taskEstimation;
	}

	public void setTaskEstimation(long taskEstimation)
	{
		this.taskEstimation = taskEstimation;
	}

	public long getTaskRestimate()
	{
		return taskRestimate;
	}

	public void setTaskRestimate(long taskRestimate)
	{
		this.taskRestimate = taskRestimate;
	}

	public long getTaskSummary()
	{
		return taskSummary;
	}

	public void setTaskSummary(long taskSummary)
	{
		this.taskSummary = taskSummary;
	}

}
