package com.hcindy.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.hcindy.compile.Compile;
import com.hcindy.syntaxanalyzer.TreeNode;

public class CompileGUI extends JFrame implements ActionListener
{
	private Compile compile;

	private static CompileGUI comGUI = new CompileGUI();

	private static final int WIDTH = 800;
	private static final int HIGHT = 600;

	private JMenuBar jmb;// 菜单栏
	private JMenu menu1;// 菜单一：文件
	private JMenu menu2;// 菜单二：编译
	private JMenu menu3;// 菜单三：帮助
	private JMenuItem item1;// 打开
	private JMenuItem item2;// 保存
	private JMenuItem item3;// 退出
	private JMenuItem item4;// 词法分析
	private JMenuItem item5;// 语法分析
	private JMenuItem item6;// 绘制语树
	private JMenuItem item7;// 语义分析
	private JMenuItem item9;// 关于

	private JFileChooser fileChooser;

	private JPanel jp;// 总面板
	private JTextArea jta1;// 编辑面板
	private JScrollPane jsp1;
	private JTabbedPane jtp;// 结果面板
	private JTextArea result1;// 词法分析面板
	private JScrollPane layresult1;
	private JTextArea result2;// 语法分析面板
	private JScrollPane layresult2;
	private TreePanel result3;// 语法树面板
	private JScrollPane layresult3;
	private JTextArea result4;// 语义分析面板
	private JScrollPane layresult4;

	private static final String r1 = "词法分析结果";
	private static final String r2 = "语法分析结果";
	private static final String r3 = "绘制语树结果";
	private static final String r4 = "语义分析结果";

	private ArrayList<TreeNode> syntaxResult;

	private CompileGUI()
	{
		super();

		try
		{
			// 将LookAndFeel设置成Windows样式
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		jmb = new JMenuBar();

		menu1 = new JMenu("文件");
		item1 = new JMenuItem("打开");
		item2 = new JMenuItem("保存");
		item3 = new JMenuItem("退出");

		menu2 = new JMenu("编译");
		item4 = new JMenuItem("词法分析");
		item5 = new JMenuItem("语法分析");
		item6 = new JMenuItem("绘制语树");
		item7 = new JMenuItem("语义分析");

		menu3 = new JMenu("帮助");
		item9 = new JMenuItem("关于");

		fileChooser = new JFileChooser("D:\\");

		jp = new JPanel(new GridLayout(1, 2));

		jta1 = new JTextArea();
		jsp1 = new JScrollPane(jta1);
		jtp = new JTabbedPane();
		result1 = new JTextArea();
		layresult1 = new JScrollPane(result1);
		result2 = new JTextArea();
		layresult2 = new JScrollPane(result2);
		result3 = new TreePanel();
		layresult3 = new JScrollPane(result3);
		result4 = new JTextArea();
		layresult4 = new JScrollPane(result4);

		Initialize();
	}

	private void Initialize()
	{
		this.setSize(WIDTH, HIGHT);// 设置宽高
		setLocationRelativeTo(null);// 位置居于中间
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);// 设置点叉退出
		this.setResizable(false);// 不能调整宽高，不能最大化
		this.setTitle("miniJavaCompile");// 设置标题
		this.setJMenuBar(jmb);// 添加菜单栏

		jmb.add(menu1);// 添加文件菜单
		menu1.add(item1);// 文件菜单添加打开
		menu1.add(item2);// 文件菜单添加保存
		menu1.addSeparator();// 文件菜单添加分隔线
		menu1.add(item3);// 文件菜单添加退出

		jmb.add(menu2);// 添加编译菜单
		menu2.add(item4);// 编译菜单添加词法分析
		menu2.add(item5);// 编译菜单添加语法分析
		menu2.add(item6);// 编译菜单添加绘制语树
		menu2.add(item7);// 编译菜单添加语义分析
		item5.setEnabled(false);// 初始语法分析不可用
		item6.setEnabled(false);// 初始语法分析不可用
		item7.setEnabled(false);// 初始语法分析不可用

		jmb.add(menu3);// 添加编译菜单
		menu3.add(item9);// 编译菜单添加词法分析

		jp.add(jsp1);

		jtp.addTab("result1", layresult1);
		jtp.setEnabledAt(0, true);
		jtp.setTitleAt(0, r1);
		jtp.addTab("result2", layresult2);
		jtp.setEnabledAt(1, true);
		jtp.setTitleAt(1, r2);
		jtp.addTab("result3", layresult3);
		jtp.setEnabledAt(2, true);
		jtp.setTitleAt(2, r3);
		jtp.addTab("result4", layresult4);
		jtp.setEnabledAt(3, true);
		jtp.setTitleAt(3, r4);
		jp.add(jtp);
		this.setContentPane(jp);

		item1.addActionListener(this);
		item2.addActionListener(this);
		item3.addActionListener(this);
		item4.addActionListener(this);
		item5.addActionListener(this);
		item6.addActionListener(this);
		item7.addActionListener(this);

		this.setVisible(true);
	}

	public static CompileGUI getInstance()
	{
		return comGUI;
	}

	public String getCode()
	{
		return jta1.getText();
	}

	public void addResult1(String s)
	{
		result1.append(s + "\n");
	}

	public void addResult2(String s)
	{
		result2.append(s + "\n");
	}

	public void addResult4(String s)
	{
		result4.append(s + "\n");
	}

	public void setSyntaxEnabled(boolean b)
	{
		item5.setEnabled(b);
	}

	public void setPaintTreeEnabled(boolean b)
	{
		item6.setEnabled(b);
	}

	public void getTreeSource(ArrayList<TreeNode> syntaxResult)
	{
		this.syntaxResult = syntaxResult;
	}

	private void paintTree()
	{
		result3.getTreeSource(syntaxResult);
		result3.updateUI();
		result3.changeVisible(true);
	}

	public void setSemanticEnabled(boolean b)
	{
		item7.setEnabled(b);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

		File file = null;
		int result;

		if (e.getActionCommand().equals("打开"))
		{
			fileChooser.setApproveButtonText("确定");
			fileChooser.setDialogTitle("打开文件");
			result = fileChooser.showOpenDialog(this);
			jta1.setText("");
			if (result == JFileChooser.APPROVE_OPTION)
			{
				file = fileChooser.getSelectedFile();
			}
			else if (result == JFileChooser.CANCEL_OPTION)
			{
			}
			FileInputStream fileInStream = null;
			if (file != null)
			{
				try
				{
					fileInStream = new FileInputStream(file);
				}
				catch (FileNotFoundException fe)
				{
				}
				int readbyte;
				try
				{
					while ((readbyte = fileInStream.read()) != -1)
					{
						jta1.append(String.valueOf((char) readbyte));
					}
				}
				catch (IOException ioe)
				{
				}
				finally
				{
					try
					{
						if (fileInStream != null)
							fileInStream.close();
					}
					catch (IOException ioe2)
					{
					}
				}
			}
		}
		if (e.getActionCommand().equals("保存"))
		{
			result = fileChooser.showSaveDialog(this);
			file = null;
			if (result == JFileChooser.APPROVE_OPTION)
			{
				file = fileChooser.getSelectedFile();
			}
			else if (result == JFileChooser.CANCEL_OPTION)
			{
			}
			FileOutputStream fileOutStream = null;
			if (file != null)
			{
				try
				{
					fileOutStream = new FileOutputStream(file);
				}
				catch (FileNotFoundException fe)
				{
					return;
				}
				String content = jta1.getText();
				try
				{
					fileOutStream.write(content.getBytes());
				}
				catch (IOException ioe)
				{
				}
				finally
				{
					try
					{
						if (fileOutStream != null)
							fileOutStream.close();
					}
					catch (IOException ioe2)
					{
					}
				}
			}
		}
		if (e.getActionCommand().equals("退出"))
		{
			System.exit(0);
		}
		if (e.getSource() == item4)
		{
			item5.setEnabled(false);
			item6.setEnabled(false);
			item7.setEnabled(false);
			result1.setText("");
			result2.setText("");
			result4.setText("");
			compile = new Compile();
			compile.compileLexicAnalyze();
		}
		if (e.getSource() == item5)
		{
			compile.compileSyntaxAnalyze();
		}
		if (e.getSource() == item6)
		{
			paintTree();
		}
		if (e.getSource() == item7)
		{
			compile.compileSemanticAnalyze();
		}
	}

}
