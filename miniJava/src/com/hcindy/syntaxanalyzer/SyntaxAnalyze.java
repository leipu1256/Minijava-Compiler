package com.hcindy.syntaxanalyzer;

import java.util.ArrayList;
import java.util.Iterator;

import com.hcindy.compile.AbstractCompile;
import com.hcindy.lexicalanalyzer.Token;

public class SyntaxAnalyze extends AbstractCompile
{
	private ArrayList<Token> lexicAnalyzeResult;// 词法分析结果

	private ArrayList<TreeNode> syntaxResult;// 树
	private ArrayList<String> processResult;// 过程记录
	private ArrayList<String> errorResult;// 错误记录
	private RecursiveDescent rd;// 递归下降对象
	private boolean error;// 语法分析是否有错

	public SyntaxAnalyze()
	{
		super();
		syntaxResult = new ArrayList<TreeNode>();
		processResult = new ArrayList<String>();
		errorResult = new ArrayList<String>();
	}

	// 读入token
	public void input(ArrayList<Token> lexicAnalyzeResult)
	{
		this.lexicAnalyzeResult = lexicAnalyzeResult;
		// -----------------测试语句-------------------
		System.out.println("\n~~~~~~~~~~~~\n语法分析完成读入\n~~~~~~~~~~~~");
	}

	// 语法分析
	public void doSyntaxAnalyze()
	{
		rd = new RecursiveDescent(lexicAnalyzeResult, syntaxResult,
				processResult, errorResult);
		rd.begin();
		if (!errorResult.isEmpty())
			error = true;

		// -----------------测试语句-------------------
		System.out.println("\n--------------下面是对树的遍历--------------");
		System.out.println("id\ttype\tsem\tfather");
		TreeNode node;
		Iterator<TreeNode> it = syntaxResult.iterator();
		while (it.hasNext())
		{
			node = it.next();
			System.out.println(node.getId() + "\t" + node.getSynType() + "\t"
					+ node.getSem() + "\t" + node.getFatherNode());
		}

	}

	public ArrayList<TreeNode> output()
	{
		return syntaxResult;
	}

	public boolean hasError()
	{
		return error;
	}

	public ArrayList<String> getProcessResult()
	{
		return processResult;
	}

	public ArrayList<String> getErrorResult()
	{
		return errorResult;
	}

}
