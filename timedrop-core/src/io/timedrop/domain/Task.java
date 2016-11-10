package io.timedrop.domain;

public class Task extends Object
{
	private Project project;

	private long idTask;
	private String description;

	// ~ Methods
	// =======================================================

	public Task()
	{
		project = new Project();

		idTask = 0;
		description = "";
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

	// ~ Methods
	// =======================================================

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

}
