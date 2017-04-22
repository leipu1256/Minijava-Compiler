package com.hcindy.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

import com.hcindy.syntaxanalyzer.TreeNode;

class TreePanel extends JPanel
{
	private ArrayList<TreeNode> syntaxResult;

	private int STnum1;
	private int VDnum1;
	private int FPnum;
	private int VDnum2;
	private int STnum2;
	private int REnum;
	private int[] xy;

	public TreePanel()
	{
		super();
		STnum1 = 0;
		VDnum1 = 0;
		FPnum = 0;
		VDnum2 = 0;
		STnum2 = 0;
		REnum = 0;
		xy = new int[12];
		this.setVisible(false);
	}

	public void getTreeSource(ArrayList<TreeNode> syntaxResult)
	{
		this.syntaxResult = syntaxResult;
	}

	public void changeVisible(boolean b)
	{
		this.setVisible(b);
	}

	private void reset()
	{
		STnum1 = 0;
		VDnum1 = 0;
		FPnum = 0;
		VDnum2 = 0;
		STnum2 = 0;
		REnum = 0;
	}

	public void paint(Graphics graphics)
	{
		Graphics g2d = (Graphics2D) graphics;

		g2d.setColor(Color.black);

		int width = this.getWidth() / 26;// 宽390,每个单位15
		int height = this.getHeight() / 40;// 高520,每个单位13

		TreeNode tn;
		if (syntaxResult != null)
		{
			Iterator<TreeNode> it = syntaxResult.iterator();

			while (it.hasNext())
			{
				tn = it.next();

				int father = tn.getFatherNode();
				String sem = tn.getSem();
				switch (tn.getSynType())
				{
					case GL:
						g2d.drawOval(width * 9 - 5, height * 2, 10, 10);
						g2d.drawString(sem, width * 9 + 5, height * 2 + 4);
						break;
					case MC:
						g2d.drawOval(width * 4 - 5, height * 4, 10, 10);
						g2d.drawLine(width * 9 - 5, height * 2, width * 4 - 5, height * 4);
						g2d.drawString(sem, width * 4 + 5, height * 4 + 4);
						break;
					case GC:
						g2d.drawOval(width * 13 - 5, height * 4, 10, 10);
						g2d.drawLine(width * 9 - 5, height * 2, width * 13 - 5, height * 4);
						g2d.drawString(sem, width * 13 + 5, height * 4 + 4);
						break;
					case ST:
						if (father == 2)// 左ST
						{
							g2d.drawOval(width * 4 - 5, height * 6, 10, 10);
							g2d.drawLine(width * 4 - 5, height * 4, width * 4 - 5, height * 6);
							g2d.drawString(sem, width * 4 + 5, height * 6 + 4);
							xy[0] = width * 4 - 5;
							xy[1] = height * 6;
						}
						else if (father == 6)// 右ST
						{
							g2d.drawOval(width * 21 - 5, height * 8, 10, 10);
							g2d.drawLine(width * 19 - 5, height * 6, width * 21 - 5, height * 8);
							g2d.drawString(sem, width * 21 + 5, height * 8 + 4);
							xy[8] = width * 21 - 5;
							xy[9] = height * 8;
						}

						break;
					case VD:
						if (father == 3)// 左VD
						{
							g2d.drawOval(width * 9 - 5, height * 6, 10, 10);
							g2d.drawLine(width * 13 - 5, height * 4, width * 9 - 5, height * 6);
							g2d.drawString(sem, width * 9 + 5, height * 6 + 4);
							xy[2] = width * 9 - 5;
							xy[3] = height * 6;
						}
						else if (father == 6)// 右VD
						{
							g2d.drawOval(width * 17 - 5, height * 8, 10, 10);
							g2d.drawLine(width * 19 - 5, height * 6, width * 17 - 5, height * 8);
							g2d.drawString(sem, width * 17 + 5, height * 8 + 4);
							xy[6] = width * 17 - 5;
							xy[7] = height * 8;
						}
						else if (father == 5)// 左VD孩子
						{
							VDnum1++;
							g2d.drawOval(xy[2], xy[3] + VDnum1 * height * 2, 10, 10);
							g2d.drawLine(xy[2], xy[3] + (VDnum1 - 1) * height * 2, xy[2], xy[3]
									+ VDnum1 * height * 2);
							g2d.drawString(sem, xy[2] + 10, xy[3] + VDnum1 * height * 2 + 4);
						}
						else if (father == 8)// 右VD孩子
						{
							VDnum2++;
							g2d.drawOval(xy[6], xy[7] + VDnum2 * height * 2, 10, 10);
							g2d.drawLine(xy[6], xy[7] + (VDnum2 - 1) * height * 2, xy[6], xy[7]
									+ VDnum2 * height * 2);
							g2d.drawString(sem, xy[6] + 10, xy[7] + VDnum2 * height * 2 + 4);
						}
						break;
					case MD:
						g2d.drawOval(width * 19 - 5, height * 6, 10, 10);
						g2d.drawLine(width * 13 - 5, height * 4, width * 19 - 5, height * 6);
						g2d.drawString(sem, width * 19 + 5, height * 6 + 4);
						break;
					case FP:
						if (father == 6)
						{
							g2d.drawOval(width * 13 - 5, height * 8, 10, 10);
							g2d.drawLine(width * 19 - 5, height * 6, width * 13 - 5, height * 8);
							g2d.drawString(sem, width * 13 + 5, height * 8 + 4);
							xy[4] = width * 13 - 5;
							xy[5] = height * 8;
						}
						else if (father == 7)
						{
							FPnum++;
							g2d.drawOval(xy[4], xy[5] + FPnum * height * 2, 10, 10);
							g2d.drawLine(xy[4], xy[5] + (FPnum - 1) * height * 2, xy[4], xy[5]
									+ FPnum * height * 2);
							g2d.drawString(sem, xy[4] + 10, xy[5] + FPnum * height * 2 + 4);
						}
						break;
					case RE:
						if (father == 6)
						{
							g2d.drawOval(width * 24 - 5, height * 8, 10, 10);
							g2d.drawLine(width * 19 - 5, height * 6, width * 24 - 5, height * 8);
							g2d.drawString(sem, width * 24 + 5, height * 8 + 4);
							xy[10] = width * 24 - 5;
							xy[11] = height * 8;
						}
						else if (father == 10)
						{
							REnum++;
							g2d.drawOval(xy[10], xy[11] + REnum * height * 2, 10, 10);
							g2d.drawLine(xy[10], xy[11] + (REnum - 1) * height * 2, xy[10], xy[11]
									+ REnum * height * 2);
							g2d.drawString(sem, xy[10] + 10, xy[11] + REnum * height * 2 + 4);
						}
						break;
					case SOP:
					case IF:
						if (father == 4)// 左ST孩子
						{
							STnum1++;
							g2d.drawOval(xy[0], xy[1] + STnum1 * height * 2, 10, 10);
							g2d.drawLine(xy[0], xy[1] + (STnum1 - 1) * height * 2, xy[0], xy[1]
									+ STnum1 * height * 2);
							g2d.drawString(sem, xy[0] + 10, xy[1] + STnum1 * height * 2 + 4);
						}
						if (father == 9)// 右ST孩子
						{
							STnum2++;
							g2d.drawOval(xy[8], xy[9] + STnum2 * height * 2, 10, 10);
							g2d.drawLine(xy[8], xy[9] + (STnum2 - 1) * height * 2, xy[8], xy[9]
									+ STnum2 * height * 2);
							g2d.drawString(sem, xy[8] + 10, xy[9] + STnum2 * height * 2 + 4);
						}
						break;
				}
			}
		}
		reset();
	}
}