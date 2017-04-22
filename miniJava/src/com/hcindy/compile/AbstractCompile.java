package com.hcindy.compile;

import java.util.List;

//编译器部件抽象类
public abstract class AbstractCompile implements CompileIO, HandlingErrors
{
	
	@Override
	public void input()
	{
	}

	@Override
	public List<? extends Object> output()
	{
		return null;
	}

	// 只实现了处理错误的方法，那就是退出
	@Override
	public void handling()
	{
		System.exit(0);
	}

}
