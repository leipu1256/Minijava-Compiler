package com.hcindy.lexicalanalyzer;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.hcindy.gui.CompileGUI;

/*
 * 用于文件读写的类
 * 外部由待词法分析文件的名称传进来构造本对象，并初始化行号line为0，是否读完isReadOver为false
 * read()方法为一次读取一行，并行号+1，若已经读完，则isReadOver改为true
 * isExist()方法用于确保拿到文件，若文件没拿到返回false给LexicAnalyze
 * getName()方法用于获取文件的名字
 * getLine()方法用于获取行号
 * getCode()方法用于获取本行代码
 * isReadOver()方法用于返回是否读完文件
 */
public class ReadCodeFile
{
	private String codes;// 总代码
	private static int line;// 行号
	private boolean isReadOver;// 文件是否读完
	private String[] code;// 本行代码
	private int maxLine;

	public ReadCodeFile(String codes)
	{
		super();
		this.codes = codes;
		cutCodes();
		maxLine = code.length;
		line = 0;// 初始化行号
		isReadOver = false;// 初始化是否读完文件
		System.out.println(line + " "+ maxLine+ " " + isReadOver);
	}

	private void cutCodes()
	{
		code = codes.split("\n");
	}

	public int getLine()
	{
		return line;
	}

	public String getCode()
	{
		return code[line++];
	}

	public boolean isReadOver()
	{
		if (line < maxLine)// 若还有一行，则读入一行，并切行号+1
		{
			System.out.println("---------这是第" + (line + 1) + "行---------");
			return isReadOver;
		}
		else
		{
			// 若没有字符，改isReadOver是否读完为true
			isReadOver = true;
			line = 0;
			return isReadOver;
		}
	}

}
