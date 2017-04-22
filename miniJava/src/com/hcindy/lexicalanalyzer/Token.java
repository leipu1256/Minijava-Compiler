package com.hcindy.lexicalanalyzer;

/*
 * Token类由自动机结束case和切取的词创建对象
 * 在构造方法内自行完成对类型、语义的设置
 * 由外部调用的setLine()设置行号
 * 若是标识符，由KeySet的方法判断是否是关键字
 */
public class Token
{
	private int line;
	private LexType lexType;
	private String sem;

	public Token(int doLexic, String sem, int line)
	{
		super();
		this.line = line;
		this.sem = sem;

		switch (doLexic)
		{
			case DoLexic.ID:
				if (KeySet.isKeyWord(this.sem))
					lexType = LexType.KEYWORD;
				else if (sem.equals("true") || sem.equals("false"))
					lexType = LexType.CONSTANT;
				else
					lexType = LexType.IDENTIFIER;
				break;
			case DoLexic.NUM:
				lexType = LexType.CONSTANT;
				break;
			case DoLexic.CON_STR:
				lexType = LexType.CONSTANT;
				break;
			case DoLexic.OPE:
				lexType = LexType.OPERATER;
				break;
			case DoLexic.DEL:
				lexType = LexType.DELIMITER;
				break;
		}
	}

	public Token(LexType lexType, String sem, int line)
	{
		super();
		this.lexType = lexType;
		this.sem = sem;
		this.line = line;
	}

	public int getLine()
	{
		return line;
	}

	public LexType getLexType()
	{
		return lexType;
	}

	public String getSem()
	{
		return sem;
	}

}
