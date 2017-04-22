package com.hcindy.lexicalanalyzer;

/*
 *词法分析器自动机
 *外部对其设置inString
 *当外部调用doDFA方法时候进行一个单词的切割，返回的int类型为对应break的case
 *而外部需要在判断还有没切的inString后才能doDFA
 *意思就是一次设置，多次切割获取，直至切完
 */
public class DoLexic
{
	private static String inString = "";// 待自动机切割的字符串
	private static String outString = "";// 切割一次的结果

	public static final int ID = 1;// 关键字，标识符，布尔常量
	public static final int NUM = 2;// 数字
	public static final int CON_STR = 3;// 字符串
	public static final int OPE = 4;// 操作符
	public static final int DEL = 5;// 分界符
	public static final int OTH = 6;// 其它
	public static final int NONSTRING = 7;// 字符串不封闭

	public static int doDFA()// 一次切一个单词并且返回case
	{
		int state = 0;// 状态
		StringBuffer aToken = new StringBuffer();// 缓存区
		while (inString.charAt(0) == ' ')
			// 针对之前遇到"没有切空格的情况下去掉空格
			inString = inString.substring(1);
		aToken.append(inString.charAt(0));// 拿第一个字符
		switch (inString.charAt(0))
		{
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'g':
			case 'h':
			case 'i':
			case 'j':
			case 'k':
			case 'l':
			case 'm':
			case 'n':
			case 'o':
			case 'p':
			case 'q':
			case 'r':
			case 's':
			case 't':
			case 'u':
			case 'v':
			case 'w':
			case 'x':
			case 'y':
			case 'z':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'L':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'U':
			case 'V':
			case 'W':
			case 'X':
			case 'Y':
			case 'Z':
			case '_':
			case '$':
				if (inString.length() > 1)
				{
					for (int index = 1; index < inString.length(); index++)
					{
						// 是否满足标识符
						if (Character.isJavaIdentifierPart(inString
								.charAt(index)))
							aToken.append(inString.charAt(index));
						else
						{
							// 截取下面的字符串作为inString
							inString = inString.substring(index);
							break;
						}
						if (index == inString.length() - 1)
							inString = inString.substring(index + 1);
					}
					outString = aToken.toString();
					state = ID;
				}
				else
				{
					// 截取下面的字符串作为inString
					inString = inString.substring(1);
					outString = aToken.toString();
					state = ID;
				}
				break;

			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				if (inString.length() > 1)
				{
					for (int index = 1; index < inString.length(); index++)
					{
						// 是否是数字
						if (Character.isDigit(inString.charAt(index)))
							aToken.append(inString.charAt(index));
						else
						{
							// 截取下面的字符串作为inString
							inString = inString.substring(index);
							break;
						}
						if (index == inString.length() - 1)
							inString = inString.substring(index + 1);
					}
					outString = aToken.toString();
					state = NUM;
				}
				else
				{
					// 截取下面的字符串作为inString
					inString = inString.substring(1);
					outString = aToken.toString();
					state = NUM;
				}
				break;

			case '"':
				String temp = inString.substring(1);// 取子串
				int index = temp.indexOf('\"');// 寻找"的索引
				if (index != -1)// 若找到了
				{
					aToken.append(temp.substring(0, index + 1));// 添加进aToken
					inString = inString.substring(index + 2);
					outString = aToken.toString();
					state = CON_STR;
				}
				else
				{
					// 若没找到
					inString = inString.substring(1);
					state = NONSTRING;
				}
				break;

			case '+':
			case '-':
			case '*':
			case '=':
			case '<':
			case '&':
			case '!':
				if (inString.length() > 1)
				{
					// 再取一个看是不是&
					Character a = inString.charAt(1);
					if (a.equals('&'))
					{
						aToken.append(inString.charAt(1));
						inString = inString.substring(2);
					}
					else
						inString = inString.substring(1);
					outString = aToken.toString();
					state = OPE;
				}
				else
				{
					// 截取下面的字符串作为inString
					inString = inString.substring(1);
					outString = aToken.toString();
					state = OPE;
				}
				break;

			case ',':
			case '.':
			case '(':
			case ')':
			case '[':
			case ']':
			case '{':
			case '}':
			case ';':
				inString = inString.substring(1);
				outString = aToken.toString();
				state = DEL;
				break;

			default:
				inString = inString.substring(1);
				outString = aToken.toString();
				state = OTH;
				break;
		}
		return state;
	}

	public static String getOutString()// 返回本次切割的结果
	{
		return outString;
	}

	public static void setInString(String in)// 设置待切割的字符串
	{
		inString = in;
	}

	public static boolean hasNext()// 判断是否切割完的方法
	{
		if (!inString.equals(""))
			return true;
		else
			return false;
	}

}
