package io.timedrop.domain;

public class Organization extends Object
{
	private long idOrganization;
	private String description;
	private String annotation;
	private long record;
	private long version;

	// ~ Methods
	// =======================================================

	public Organization()
	{
		idOrganization = 0;
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

	public void copy(Organization copy)
	{
		if (copy != null & copy instanceof Organization)
		{
			idOrganization = copy.getIdOrganization();
			description = copy.getDescription();
			annotation = copy.getAnnotation();
			record = copy.getRecord();
			version = copy.getVersion();
		}
	}

	// ~ Methods
	// =======================================================

	public long getIdOrganization()
	{
		return idOrganization;
	}

	public void setIdOrganization(long idOrganization)
	{
		this.idOrganization = idOrganization;
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
