package com.hcindy.semanticanalyzer;

import java.util.ArrayList;
import java.util.Iterator;

public class SemanticAnalyze
{
	private ArrayList<String> synproc;// 语法分析过程
	private ArrayList<String> errorResult;// 语义错误
	private SymbTable symbTable;
	private int level;
	private int off;
	private boolean parm;
	private static String[] buffer = { "-", "-", "-", "-", "-" };
	private boolean error;// 语义分析是否有错

	private GlobalTable globalTable;

	public SemanticAnalyze()
	{
		super();
		this.level = 0;
		this.off = 0;
		this.parm = false;
		this.globalTable = new GlobalTable();
		this.errorResult = new ArrayList<String>();
	}

	public void input(ArrayList<String> synproc)
	{
		this.synproc = synproc;
		// -----------------测试语句-------------------
		System.out.println("\n-------------\n语义分析完成读入\n-------------");
	}

	public void doSemanticAnalyze()
	{
		for (Iterator<String> it = synproc.iterator(); it.hasNext();)
		{
			poll(it.next());
			if (buffer[0].contains("class"))// 类表
			{
				int at = buffer[1].indexOf("的");
				String name = buffer[1].substring(0, at);
				symbTable = new SymbTable(name, Kind.classkind, Typekind.otherTy, level, -1, parm);
				creatTable(name, "class", level);
			}
			else if (buffer[0].contains("{"))// 层数加1并且是否形参改为否
			{
				level++;
				parm = false;
			}
			else if (buffer[0].contains("}"))// 层数减1
			{
				level--;
				off = 0;
			}
			else if (buffer[0].contains("main"))// 主方法表
			{
				off = 0;
				int at = buffer[0].indexOf("与");
				String name = buffer[0].substring(0, at);
				symbTable = new SymbTable(name, Kind.methodkind, Typekind.voidTy, level, off++, parm);
				creatTable(name, "method", level);
				parm = true;
			}
			else if (buffer[0].contains("public") && buffer[3].contains("("))// 一般方法表
			{
				off = 0;
				int at = buffer[1].indexOf("与");
				String type = buffer[1].substring(0, at);
				at = buffer[2].indexOf("的");
				String name = buffer[2].substring(0, at);
				if (type.equals("int"))
				{
					symbTable = new SymbTable(name, Kind.methodkind, Typekind.intTy, level, off++,
							parm);
					creatTable(name, "method", level);
					parm = true;
				}
				else if (type.equals("boolean"))
				{
					symbTable = new SymbTable(name, Kind.methodkind, Typekind.boolTy, level, off++,
							parm);
					creatTable(name, "method", level);
					parm = true;
				}
				else
				{
					symbTable = new SymbTable(name, Kind.methodkind, Typekind.identifierTy, level,
							off++, parm);
					creatTable(name, "method", level);
					parm = true;
				}
			}
			else if (buffer[3].contains("Identifier") && buffer[2].contains("]"))// 数组表
			{
				int at = buffer[3].indexOf("的");
				String name = buffer[3].substring(0, at);
				at = buffer[0].indexOf("与");
				String type = buffer[0].substring(0, at);
				if (type.equals("int"))
				{
					symbTable = new SymbTable(name, Kind.arraykind, Typekind.intTy, level, off++,
							parm);
					creatTable(name, "array", level);
				}
				else if (type.equals("boolean"))
				{
					symbTable = new SymbTable(name, Kind.arraykind, Typekind.boolTy, level, off++,
							parm);
					creatTable(name, "array", level);
				}
				else
				{
					symbTable = new SymbTable(name, Kind.arraykind, Typekind.identifierTy, level,
							off++, parm);
					creatTable(name, "array", level);
				}
			}
			else if (buffer[1].contains("Identifier"))
			{
				if (buffer[2].contains("=") && buffer[4].contains(";"))// 变量赋值表
				{
					int at = buffer[1].indexOf("的");
					String name = buffer[1].substring(0, at);
					if (buffer[0].contains("int") && buffer[3].contains("数字"))
					{
						symbTable = new SymbTable(name, Kind.varkind, Typekind.intTy, level, off++,
								parm);
						creatTable(name, "var", level);
					}
					else if (buffer[0].contains("boolean")
							&& (buffer[3].contains("true") || buffer[3].contains("false")))
					{
						symbTable = new SymbTable(name, Kind.varkind, Typekind.boolTy, level, off++,
								parm);
						creatTable(name, "var", level);
					}
					else
						typenotmatch(name);
				}
				else if (buffer[2].contains("=") && buffer[3].contains("new"))// 创建对象表
				{
					int at = buffer[0].indexOf("的");
					String name1 = buffer[0].substring(0, at);
					at = buffer[4].indexOf("的");
					String name2 = buffer[4].substring(0, at);
					at = buffer[1].indexOf("的");
					String name = buffer[1].substring(0, at);
					if (find(name1, "class"))
					{
						if (find(name2, "class"))
						{
							if (name1.equals(name2))
							{
								symbTable = new SymbTable(name, Kind.varkind,
										Typekind.identifierTy, level, off++, parm);
								creatTable(name, "var", level);
							}
							else
								typenotmatch(name);
						}
						else
							nodeclartouse(name2);
					}
					else
						nodeclartouse(name1);

				}
				else if (buffer[2].contains(",") || buffer[2].contains(")"))// 参数表
				{
					int at = buffer[1].indexOf("的");
					String name = buffer[1].substring(0, at);
					if (buffer[0].contains("int"))
					{
						symbTable = new SymbTable(name, Kind.varkind, Typekind.intTy, level, off++,
								parm);
						creatTable(name, "var", level);
					}
					else if (buffer[0].contains("boolean"))
					{
						symbTable = new SymbTable(name, Kind.varkind, Typekind.boolTy, level, off++,
								parm);
						creatTable(name, "var", level);
					}
					else if (buffer[0].contains("Identifier"))
					{
						symbTable = new SymbTable(name, Kind.varkind, Typekind.identifierTy, level,
								off++, parm);
						creatTable(name, "var", level);
					}
				}
			}
		}
		// -----------------测试语句-------------------
		System.out.println("\n-------------\n语义分析完成建表\n-------------");
		if (!errorResult.isEmpty())
			error = true;
	}

	private void poll(String s)
	{
		buffer[0] = buffer[1];
		buffer[1] = buffer[2];
		buffer[2] = buffer[3];
		buffer[3] = buffer[4];
		buffer[4] = s;
	}

	private boolean creatTable(String s, String st, int i)
	{
		String temp = new String(s + " " + st + " " + level);
		if (!globalTable.findTable(temp))
		{
			globalTable.addTable(symbTable, temp);
			// -----------------测试语句-------------------
			System.out.println("成功添加" + temp + "表");
			return true;
		}
		else
		{
			overdeclar(temp);
			return false;
		}
	}

	private boolean find(String s, String st)
	{
		if (globalTable.findDeclar(s, st))
			return true;
		else
			return false;
	}

	// 重复声明处理
	private void overdeclar(String s)
	{
		String[] ss = s.split(" ");
		errorResult.add(ss[0] + "重复声明");
	}

	// 类型不匹配处理
	private void typenotmatch(String s)
	{
		errorResult.add(s + "赋值类型不匹配");
	}

	// 未声明并使用
	private void nodeclartouse(String s)
	{
		errorResult.add(s + "未声明并使用");
	}

	public boolean hasError()
	{
		return error;
	}

	public GlobalTable output()
	{
		return globalTable;
	}

	public ArrayList<String> getErrorResult()
	{
		return errorResult;
	}

}
