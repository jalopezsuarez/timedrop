package io.timedrop.domain;

public class Organization extends Object
{
	private long idOrganization;
	private String description;

	// ~ Methods
	// =======================================================

	public Organization()
	{
		idOrganization = 0;
		description = "";
	}

	@Override
	public String toString()
	{
		return description;
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

}
