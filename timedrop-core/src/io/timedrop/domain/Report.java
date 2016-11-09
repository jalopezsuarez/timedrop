package io.timedrop.domain;

public class Report
{
	private long idProject;
	private String projectDescription;
	private long idTask;
	private String taskDescription;
	private long taskDuration;

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

	public long getTaskDuration()
	{
		return taskDuration;
	}

	public void setTaskDuration(long taskDuration)
	{
		this.taskDuration = taskDuration;
	}

}
