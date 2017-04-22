package com.hcindy.semanticanalyzer;

public class SymbTable
{
	private String name;
	private int size;
	private Typekind typekind;
	private Kind kind;
	private int level;
	private int off;
	private boolean isParm;

	public SymbTable(String name, Kind kind, Typekind typekind, int level, int off, boolean isParm)
	{
		super();
		this.name = name;
		this.kind = kind;
		this.typekind = typekind;
		this.level = level;
		this.off = off;
		this.isParm = isParm;
		this.size = 1;
	}

	public String getName()
	{
		return name;
	}

	public int getSize()
	{
		return size;
	}

	public Typekind getTypekind()
	{
		return typekind;
	}

	public Kind getKind()
	{
		return kind;
	}

	public int getLevel()
	{
		return level;
	}

	public int getOff()
	{
		return off;
	}

	public boolean isParm()
	{
		return isParm;
	}

}
