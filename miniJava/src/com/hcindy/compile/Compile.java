package com.hcindy.compile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.hcindy.gui.CompileGUI;
import com.hcindy.lexicalanalyzer.LexType;
import com.hcindy.lexicalanalyzer.LexicAnalyze;
import com.hcindy.lexicalanalyzer.Token;
import com.hcindy.semanticanalyzer.GlobalTable;
import com.hcindy.semanticanalyzer.SemanticAnalyze;
import com.hcindy.semanticanalyzer.SymbTable;
import com.hcindy.syntaxanalyzer.SyntaxAnalyze;
import com.hcindy.syntaxanalyzer.TreeNode;

/*
 * 该类是编译器类，封装词法、语法、语义分析三个类
 * lexicAnalyze是词法分析器类，结果暂时返回到lar中
 */
public class Compile
{
	private CompileGUI comGUI;

	private LexicAnalyze lexicAnalyze;// 词法分析器
	private SyntaxAnalyze syntaxAnalyze;// 语法分析器
	private SemanticAnalyze semanticanalyze;// 语义分析器

	private ArrayList<Token> lar;// 词法分析正确结果
	private ArrayList<String> error;// 词法分析错误结果

	private ArrayList<TreeNode> syntaxResult;// 语法分析结果
	private ArrayList<String> synproc;// 语法分析过程
	private ArrayList<String> synerror;// 语法分析错误结果

	private ArrayList<String> semanticerror;// 语义分析错误结果
	private GlobalTable globalTable;// 语义分析表

	public Compile()
	{
		super();
		comGUI = CompileGUI.getInstance();
		lexicAnalyze = new LexicAnalyze();
		syntaxAnalyze = new SyntaxAnalyze();
		semanticanalyze = new SemanticAnalyze();
	}

	// 词法分析
	public void compileLexicAnalyze()
	{
		while (lexicAnalyze.isNeedInput())
		{
			System.out.println("进到了循环");
			lexicAnalyze.input();
			System.out.println("过了input");
			lexicAnalyze.doLexic();
			System.out.println("过了doLexic");
		}
		System.out.println("--------\n|" + "出了循环|\n--------");

		if (lexicAnalyze.hasError())
		{
			error = lexicAnalyze.getError();
			Iterator<String> it = error.iterator();
			while (it.hasNext())
			{
				String a = it.next();
				comGUI.addResult1(a);
			}
		}
		else
		{
			lar = lexicAnalyze.output();
			Iterator<Token> it = lar.iterator();
			while (it.hasNext())
			{
				Token a = it.next();
				comGUI.addResult1(a.getLine() + "\t" + a.getSem() + "\t" + a.getLexType());
			}
			comGUI.setSyntaxEnabled(true);
		}
	}

	// 预语法分析，为token链加一个EOF
	private void preSyntaxAnalyze()
	{
		lar.add(new Token(LexType.EOF, null, -1));
	}

	// 语法分析
	public void compileSyntaxAnalyze()
	{
		preSyntaxAnalyze();
		syntaxAnalyze.input(lar);
		syntaxAnalyze.doSyntaxAnalyze();

		if (syntaxAnalyze.hasError())
		{
			synerror = syntaxAnalyze.getErrorResult();
			Iterator<String> it = synerror.iterator();
			while (it.hasNext())
			{
				comGUI.addResult2(it.next());
			}
		}
		else
		{
			synproc = syntaxAnalyze.getProcessResult();
			Iterator<String> it = synproc.iterator();
			while (it.hasNext())
			{
				comGUI.addResult2(it.next());
			}

			comGUI.setPaintTreeEnabled(true);
			comGUI.setSemanticEnabled(true);

			syntaxResult = syntaxAnalyze.output();
			comGUI.getTreeSource(syntaxResult);
		}
	}

	// 语义分析
	public void compileSemanticAnalyze()
	{
		semanticanalyze.input(synproc);
		semanticanalyze.doSemanticAnalyze();
		
		if (semanticanalyze.hasError())
		{
			semanticerror = semanticanalyze.getErrorResult();
			Iterator<String> it = semanticerror.iterator();
			while (it.hasNext())
			{
				comGUI.addResult4(it.next());
			}
		}
		else
		{
			globalTable = semanticanalyze.output();
			Set<Entry<SymbTable, String>> set = globalTable.entrySet();
			for (Iterator<Entry<SymbTable, String>> it1 = set.iterator(); it1.hasNext();)
			{
				SymbTable symbTable = it1.next().getKey();
				StringBuffer sbuffer = new StringBuffer();
				sbuffer.append("Name:" + symbTable.getName() + "\t");
				sbuffer.append("Kind:" + symbTable.getKind() + "\t");
				sbuffer.append("Typekind:" + symbTable.getTypekind() + "\n");
				sbuffer.append("Size:" + symbTable.getSize() + "\t");
				sbuffer.append("Level:" + symbTable.getLevel() + "\t");
				sbuffer.append("Off:" + symbTable.getOff() + "\t");
				sbuffer.append("Parm:" + symbTable.isParm() + "\n");
				comGUI.addResult4(sbuffer.toString());
			}
		}
	}

}
