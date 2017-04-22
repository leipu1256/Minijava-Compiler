package com.hcindy.syntaxanalyzer;

public class TreeNode
{
	private SynType synType;
	private int id;
	private int fatherNode;
	private String sem;

	public TreeNode(SynType synType, int id, String sem)
	{
		super();
		this.synType = synType;
		this.id = id;
		this.sem = sem;
		fatherNode = -1;
	}
	
	public void addAttribute(int fatherNode)
	{
		this.fatherNode = fatherNode;
	}

	public SynType getSynType()
	{
		return synType;
	}

	public void setSynType(SynType synType)
	{
		this.synType = synType;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getFatherNode()
	{
		return fatherNode;
	}

	public void setFatherNode(int fatherNode)
	{
		this.fatherNode = fatherNode;
	}

	public String getSem()
	{
		return sem;
	}

	public void setSem(String sem)
	{
		this.sem = sem;
	}

}
