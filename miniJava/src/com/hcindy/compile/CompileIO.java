package com.hcindy.compile;

import java.util.List;

//编译器部件都有输入输出
public interface CompileIO
{
	// 输入
	public void input();

	// 输出
	public List<? extends Object> output();
}
