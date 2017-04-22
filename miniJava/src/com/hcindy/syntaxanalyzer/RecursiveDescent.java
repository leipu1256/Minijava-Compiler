package com.hcindy.syntaxanalyzer;

import java.util.ArrayList;
import java.util.Iterator;

import com.hcindy.lexicalanalyzer.LexType;
import com.hcindy.lexicalanalyzer.Token;

public class RecursiveDescent
{
	private Token aToken;
	private int line;
	private Iterator<Token> it;// token链的迭代器
	private ArrayList<TreeNode> syntaxResult;// 过程记录
	private ArrayList<String> processResult;// 过程记录
	private ArrayList<String> errorResult;// 错误记录
	private boolean hasError;// 为了只添加一次错误而设置

	private boolean whichGrammar;// false为mainclass中true为generalclass中
	private boolean isInMethod;// true为方法里定义变量，false为方法外定义变量
	private int num;// 结点ID
	private String buffer;
	private TreeNode node;

	public RecursiveDescent(ArrayList<Token> tokens,
			ArrayList<TreeNode> syntaxResult, ArrayList<String> processResult,
			ArrayList<String> errorResult)
	{
		super();
		line = 1;
		num = 1;
		it = tokens.iterator();
		this.syntaxResult = syntaxResult;
		this.processResult = processResult;
		this.errorResult = errorResult;
	}

	// match操作包括匹配和下一个token
	private boolean match(String s)
	{
		if (aToken.getLexType() != LexType.EOF)
		{
			boolean b = aToken.getSem().equals(s);
			processResult.add(s + "与" + aToken.getSem() + "的匹配结果为" + b);
			if (b)
				nextToken();
			else
				error();
			return b;
		}
		else
			return false;
	}

	// 预测操作
	private boolean forecast(String s)
	{
		if (aToken.getLexType() != LexType.EOF)
		{
			return aToken.getSem().equals(s);
		}
		else
			return false;
	}

	// 下一个token
	private void nextToken()
	{
		buffer = aToken.getSem();
		aToken = it.next();
	}

	// 在第一次检测到错误的时候才添加错误，并遍历到最后个token加快结束递归
	private void error()
	{
		line = aToken.getLine();
		if (!hasError)
		{
			errorResult.add(new String("The " + line + " line has error"));
			while (aToken.getLexType() != LexType.EOF)
			{
				System.out.println("还有" + aToken.getSem());
				nextToken();
			}
			// -----------------测试语句-------------------
			System.out.println("有错误被迭代完了");
			hasError = true;
		}
	}

	private void preRecursiveDescent()
	{
		// 建立goal 1
		node = new TreeNode(SynType.GL, num++, "GL");
		syntaxResult.add(node);
		// 建立mainclass 2
		node = new TreeNode(SynType.MC, num++, "MC");
		node.addAttribute(1);
		syntaxResult.add(node);
		// 建立generalclass 3
		node = new TreeNode(SynType.GC, num++, "GC");
		node.addAttribute(1);
		syntaxResult.add(node);
		// 建立statemtent 4
		node = new TreeNode(SynType.ST, num++, "ST");
		node.addAttribute(2);
		syntaxResult.add(node);
		// 建立vardeclaration 5
		node = new TreeNode(SynType.VD, num++, "VD");
		node.addAttribute(3);
		syntaxResult.add(node);
		// 建立methoddeclaration 6
		node = new TreeNode(SynType.MD, num++, "MD");
		node.addAttribute(3);
		syntaxResult.add(node);
		// 建立formalparameter 7
		node = new TreeNode(SynType.FP, num++, "FP");
		node.addAttribute(6);
		syntaxResult.add(node);
		// 建立vardeclaration 8
		node = new TreeNode(SynType.VD, num++, "VD");
		node.addAttribute(6);
		syntaxResult.add(node);
		// 建立statement 9
		node = new TreeNode(SynType.ST, num++, "ST");
		node.addAttribute(6);
		syntaxResult.add(node);
		// 建立relexpression 10
		node = new TreeNode(SynType.RE, num++, "RE");
		node.addAttribute(6);
		syntaxResult.add(node);
	}

	// 开始递归下降
	public void begin()
	{
		preRecursiveDescent();
		aToken = it.next();
		Goal();
		// -----------------测试语句-------------------
		System.out.println("~~~~~~~~~~~~\n递归下降完成\n~~~~~~~~~~~~");
	}

	private void Goal()
	{
		MainClass();
		whichGrammar = true;
		ClassDeclaration();
		if (aToken.getLexType() == LexType.EOF)
			return;
	}

	private void MainClass()
	{
		match("class");
		Identifier();
		match("{");
		match("public");
		match("static");
		match("void");
		match("main");
		match("(");
		match("String");
		match("[");
		match("]");
		Identifier();
		match(")");
		match("{");
		Statement();
		match("}");
		match("}");
	}

	private void ClassDeclaration()
	{
		if (forecast("class"))
		{
			match("class");
			Identifier();
			match("{");
			VarDeclaration();
			isInMethod = true;
			MethodDeclaration();
			match("}");
			ClassDeclaration();
		}
		else if (aToken.getLexType() == LexType.EOF)
			return;
		else
		{
			System.out.println("ClassDeclaration 调用 error");
			error();
		}
	}

	private void VarDeclaration()
	{
		if (forecast("int")
				|| forecast("boolean")
				|| (aToken.getLexType() == LexType.IDENTIFIER && !forecast("System")))
		{
			Type();
			Identifier();

			node = new TreeNode(SynType.VD, num++, buffer);
			if (!isInMethod)
				node.addAttribute(5);
			else
				node.addAttribute(8);

			VarDefine();
			match(";");

			if (!hasError)
				syntaxResult.add(node);

			VarDeclaration();
		}
		else if (forecast("public") || forecast("}") || forecast("{")
				|| forecast("System") || forecast("if"))
			return;
		else
		{
			System.out.println("VarDeclaration 调用 error");
			error();
		}
	}

	private void VarDefine()
	{
		if (forecast("="))
		{
			match("=");
			Expression();
		}
		else if (forecast(";"))
			return;
		else
		{
			System.out.println("VarDefine 调用 error");
			error();
		}
	}

	private void Type()
	{
		if (forecast("int"))
		{
			match("int");
			T();
		}
		else if (forecast("boolean"))
			match("boolean");
		else if (aToken.getLexType() == LexType.IDENTIFIER)
			Identifier();
		else
		{
			System.out.println("Type 调用 error");
			error();
		}
	}

	private void T()
	{
		if (forecast("["))
		{
			match("[");
			match("]");
		}
		else if (aToken.getLexType() == LexType.IDENTIFIER)
			return;
		else
		{
			System.out.println("T 调用 error");
			error();
		}
	}

	private void MethodDeclaration()
	{
		if (forecast("public"))
		{
			match("public");
			Type();
			Identifier();
			match("(");
			FormalParameterList();
			match(")");
			match("{");
			VarDeclaration();
			Statement();
			match("return");
			Expression();

			node = new TreeNode(SynType.RE, num++, "reblock");
			node.addAttribute(10);
			if (!hasError)
				syntaxResult.add(node);

			match(";");
			match("}");
		}
		else if (forecast("}"))
			return;
		else
		{
			System.out.println("MethodDeclaration 调用 error");
			error();
		}
	}

	private void FormalParameterList()
	{
		if (forecast("int") || forecast("boolean")
				|| aToken.getLexType() == LexType.IDENTIFIER)
		{
			FormalParameter();
			FormalParameterRest();
		}
		else if (forecast(")"))
			return;
		else
		{
			System.out.println("FormalParameterList 调用 error");
			error();
		}
	}

	private void FormalParameter()
	{
		Type();
		Identifier();

		node = new TreeNode(SynType.FP, num++, buffer);
		node.addAttribute(7);
		if (!hasError)
			syntaxResult.add(node);
	}

	private void FormalParameterRest()
	{
		if (forecast(","))
		{
			match(",");
			FormalParameter();
			FormalParameterRest();
		}
		else if (forecast(")"))
			return;
		else
		{
			System.out.println("FormalParameterRest 调用 error");
			error();
		}
	}

	private void Statement()
	{
		if (forecast("{"))
		{
			match("{");
			StatementList();
			match("}");
		}
		else if (forecast("System"))
		{
			match("System");
			match(".");
			match("out");
			match(".");
			match("println");
			match("(");
			Const();

			node = new TreeNode(SynType.SOP, num++, buffer);
			if (!whichGrammar)
				node.addAttribute(4);
			else
				node.addAttribute(9);

			match(")");
			match(";");

			if (!hasError)
				syntaxResult.add(node);
		}
		else if (forecast("if"))
		{
			match("if");
			match("(");
			match("true");
			match(")");
			Statement();
			match("else");
			Statement();

			if (!hasError)
			{
				node = new TreeNode(SynType.IF, num++, "ifblock");
				if (!whichGrammar)
					node.addAttribute(4);
				else
					node.addAttribute(9);
				syntaxResult.add(node);
			}
		}
		else
		{
			System.out.println("Statement 调用 error");
			error();
		}
	}

	private void StatementList()
	{
		if (forecast("{") || forecast("System"))
		{
			Statement();
			StatementList();
		}
		else if (forecast("}"))
			return;
		else
		{
			System.out.println("StatementList 调用 error");
			error();
		}
	}

	private void Expression()
	{
		if (forecast("new"))
		{
			match("new");
			Identifier();
			match("(");
			match(")");
			return;
		}
		boolean bt = false;
		try
		{
			int it = Integer.parseInt(aToken.getSem());
			processResult.add(it + "匹配为数字");
			bt = true;
		}
		catch (Exception e)
		{
			bt = false;
		}
		finally
		{
			if (bt)
				nextToken();
			else
				RelExpression();
		}
	}

	private void RelExpression()
	{
		if (forecast("!"))
		{
			match("!");
			match("(");
			RelExpression();
			match(")");
			R();
		}
		else if (forecast("true"))
		{
			match("true");
			R();
		}
		else if (forecast("false"))
		{
			match("false");
			R();
		}
		else if (forecast("("))
		{
			match("(");
			RelExpression();
			match(")");
			R();
		}
		else
		{
			System.out.println("RelExpression 调用 error");
			error();
		}
	}

	private void R()
	{
		if (forecast("&&"))
		{
			LogicOperator();
			RelExpression();
			R();
		}
		else if (forecast(";"))
			return;
		else
		{
			System.out.println("R 调用 error");
			error();
		}
	}

	private void LogicOperator()
	{
		match("&&");
	}

	private void Identifier()
	{
		if (aToken.getLexType() == LexType.IDENTIFIER)
		{
			processResult.add(aToken.getSem() + "的匹配满足Identifier");
			nextToken();
		}
	}

	private void Const()
	{
		if (aToken.getLexType() == LexType.CONSTANT)
			nextToken();
	}

}
