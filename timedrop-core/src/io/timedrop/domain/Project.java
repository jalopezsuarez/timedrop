package io.timedrop.domain;

public class Project extends Object
{
	private Organization organization;

	private long idProject;
	private String description;
	private String annotation;
	private long record;
	private long version;

	// ~ Methods
	// =======================================================

	public Project()
	{
		organization = new Organization();

		idProject = 0;
		description = "";
		annotation = "";
		record = System.currentTimeMillis();
		version = System.currentTimeMillis();
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
			annotation = copy.getAnnotation();
			record = copy.getRecord();
			version = copy.getVersion();
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
