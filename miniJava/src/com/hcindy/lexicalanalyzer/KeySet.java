package com.hcindy.lexicalanalyzer;

/*
 * 关键字集合类
 * isKeyWord(String word)方法返回是否是关键字
 */
public class KeySet
{
	private static String[] keys = { "abstract", "assert", "boolean", "break",
			"byte", "case", "catch", "char", "class", "continue", "default",
			"do", "double", "else", "enum", "extends", "final", "finally",
			"float", "for", "if", "implements", "import", "instanceof", "int",
			"interface", "long", "native", "new", "package", "private",
			"protected", "public", "return", "strictfp", "short", "static",
			"super", "switch", "synchronized", "this", "throw", "throws",
			"transient", "try", "void", "volatile", "while" };// 关键字集合

	// 判断是否是关键字
	public static boolean isKeyWord(String word)
	{
		for (int i = 0; i < keys.length; i++)// 循环匹配
		{
			if (word.equals(keys[i]))
				return true;
		}
		return false;
	}
}
