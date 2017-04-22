package com.hcindy.semanticanalyzer;

import java.util.HashMap;
import java.util.Iterator;

public class GlobalTable extends HashMap<SymbTable, String>
{
	public void addTable(SymbTable symbTable, String s)
	{
		this.put(symbTable, s);
	}

	public boolean findTable(String s)
	{
		return this.containsValue(s);
	}

	public boolean findDeclar(String s, String st)
	{
		boolean find = false;
		for (Iterator<String> it = this.values().iterator(); it.hasNext();)
		{
			String temp = it.next();
			if (temp.contains(s) && temp.contains(st))
			{
				find = true;
				break;
			}
		}
		return find;
	}
}
