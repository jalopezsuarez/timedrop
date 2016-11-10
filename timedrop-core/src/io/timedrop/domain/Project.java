package io.timedrop.domain;

public class Project extends Object
{
	private Organization organization;

	private long idProject;
	private String description;

	// ~ Methods
	// =======================================================

	public Project()
	{
		organization = new Organization();

		idProject = 0;
		description = "";
	}

	@Override
	public String toString()
	{
		return description;
	}

	// ~ Methods
	// =======================================================

	public void copy(Project copy)
	{
		if (copy != null & copy instanceof Project)
		{
			organization.copy(copy.getOrganization());
			
			idProject = copy.getIdProject();
			description = copy.getDescription();
		}
	}

	// ~ Methods
	// =======================================================

	public Organization getOrganization()
	{
		return organization;
	}

	public void setOrganization(Organization organization)
	{
		this.organization = organization;
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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	// ~ Methods
	// =======================================================

}
