package com.hcindy.lexicalanalyzer;

import java.util.ArrayList;

import com.hcindy.compile.AbstractCompile;
import com.hcindy.gui.CompileGUI;

/*
 * 词法分析器类
 * readCodeFile读文件的对象
 * isNeedInput是否还需要读入，对应readCodeFile的isReadOver
 * token封装token的对象
 * lexicAnalyzeResult链起token的对象
 * beforeString读出的一行代码
 * afterStrings接收对beforeString的处理结果
 */
public class LexicAnalyze extends AbstractCompile
{
	private CompileGUI comGUI;

	private ReadCodeFile readCodeFile;// 读文件对象
	private boolean isNeedInput;// 是否需要继续读取
	private Token token;// token对象
	private ArrayList<Token> lexicAnalyzeResult;// token链
	private ArrayList<String> errorResult;// 错误链
	private String beforeString;// 读进来的一行代码
	private String[] afterStrings;// 处理一行代码后的数组

	public LexicAnalyze()
	{
		super();
		comGUI = CompileGUI.getInstance();
		lexicAnalyzeResult = new ArrayList<Token>();
		errorResult = new ArrayList<String>();
		isNeedInput = true;
		String code = comGUI.getCode();
		if (code.equals(""))// 判断是否是空代码
		{
			nonFile();
			readCodeFile = new ReadCodeFile(code);// 创建读取对象
		}
		else
			readCodeFile = new ReadCodeFile(code);// 创建读取对象
	}

	@Override
	public void input()// 读入操作
	{
		beforeString = readCodeFile.getCode();// 继续读取
		processBeforeString(beforeString);// 读取后处理
	}

	private void processBeforeString(String needProcessCode)// 对读入的一行代码，去掉前导空格和拿空格切分
	{
		// 去掉注释
		int index = needProcessCode.indexOf('/');
		if (index != -1 && needProcessCode.charAt(index + 1) == '/')
		{
			needProcessCode = needProcessCode.substring(0, index);
			// -----------------测试语句-------------------
			System.out.println("去掉了注释，变成" + needProcessCode);
			// -----------------测试语句-------------------
		}
		// 若该行有字符串则不做切分空格操作
		if (needProcessCode.contains("\""))
			afterStrings[0] = needProcessCode.trim().replaceAll("\t", " ");
		else
			afterStrings = needProcessCode.trim().replaceAll("\t", " ")
					.split(" ");
	}

	public void doLexic()// 做自动机
	{
		for (int i = 0; i < afterStrings.length; i++)
		{
			// -----------------测试语句-------------------
			System.out.println(i + "进入自动机");
			// -----------------测试语句-------------------

			// 对于空行情况下，直接break
			if (afterStrings[i].equals(""))
				break;
			DoLexic.setInString(afterStrings[i]);// 设置待切的字符串
			while (DoLexic.hasNext())// 若没切完
			{
				int state = DoLexic.doDFA();
				// 是否是非法字符的状态结束
				if (state == DoLexic.OTH)
					illegalCharacter(readCodeFile.getLine(),
							DoLexic.getOutString());
				else if (state == DoLexic.NONSTRING)
					nonString(readCodeFile.getLine());
				// 把结束状态、字符串、行号创建token对象
				token = new Token(state, DoLexic.getOutString(),
						readCodeFile.getLine());

				// -----------------测试语句-------------------
				System.out.println(token.getLine() + " " + token.getLexType()
						+ " " + token.getSem());
				// -----------------测试语句-------------------

				// 链上
				lexicAnalyzeResult.add(token);
			}
		}
	}

	@Override
	public ArrayList<Token> output()
	{
		return lexicAnalyzeResult;
	}

	public boolean isNeedInput()
	{
		if (!readCodeFile.isReadOver())// 若没读完
		{
			return isNeedInput;
		}
		else
			isNeedInput = false;// 若读完改变isNeedInput为false
		return isNeedInput;
	}

	public boolean hasError()
	{
		if (errorResult.isEmpty())
			return false;
		else
			return true;
	}

	public ArrayList<String> getError()
	{
		return errorResult;
	}

	private void nonFile()
	{
		errorResult.add("There is no code");
	}

	private void illegalCharacter(int line, String outString)
	{
		errorResult.add("The " + line + " line has illegal character: "
				+ outString);
	}

	private void nonString(int line)
	{
		errorResult.add("The " + line + " line has nonString");
	}

}
