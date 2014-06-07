package base;

import java.awt.*;
import java.awt.List.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;

public class kmeans extends JFrame implements MouseListener, ActionListener,
		ItemListener {
	JLabel l1 = new JLabel("聚  类  个  数 ：");
	JLabel l2 = new JLabel("点     集");
	JLabel l3 = new JLabel("初始聚类中心");
	JLabel l4 = new JLabel("聚  类  结  果");
	JTextField t1 = new JTextField(6);
	JButton b1 = new JButton("聚    类");
	JButton b2 = new JButton("清   空");
	List list1 = new List();
	List list2 = new List();
	List list3 = new List();

	int t = 0;
	int count = 0;// count用来保存聚类个数;
	vector dataSet = new vector();// dataSet保存添加的数据集（点集）；
	Vector v = new Vector();// v保存容器;
	Vector candidate = new Vector();// candidate是v的副本，用于比较确定聚类中心是否发生改变；
	vector dataItem;// dataItem保存每一项；

	public static void main(String args[]) {
		kmeans cluster = new kmeans();
		cluster.initial();
	}

	public void initial() {
		setLayout(null);
		setTitle("k-means聚类算法");
		setSize(680, 430);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		l1.setBounds(420, 27, 102, 28);
		l1.setHorizontalAlignment(l1.CENTER);
		add(l1);
		l2.setBounds(419, 61, 103, 24);
		l2.setHorizontalAlignment(l2.CENTER);
		add(l2);
		l3.setBounds(534, 62, 105, 24);
		l3.setHorizontalAlignment(l3.CENTER);
		add(l3);
		l4.setBounds(421, 195, 222, 25);
		l4.setHorizontalAlignment(l4.CENTER);
		add(l4);
		t1.setBounds(534, 28, 106, 27);
		add(t1);
		b1.setBounds(421, 159, 103, 29);
		add(b1);
		b1.addActionListener(this);
		b2.setBounds(537, 160, 104, 28);
		add(b2);
		b2.addActionListener(this);
		list1.setBounds(419, 89, 104, 64);
		add(list1);
		list1.addItemListener(this);
		list2.setBounds(535, 89, 106, 66);
		add(list2);
		list2.addItemListener(this);
		list3.setBounds(421, 223, 222, 143);
		add(list3);

		addMouseListener(this);
		setVisible(true);
	}

	public void mouseClicked(MouseEvent e) {
		if (newX(e.getX()) > 0 && newX(e.getX()) < 370 && newY(e.getY()) > 0
				&& newY(e.getY()) < 350) {
			Graphics g = this.getGraphics();
			g.setColor(Color.red);
			g.fillOval(e.getX(), e.getY(), 4, 4);
			list1.add(newX(e.getX()) + "," + newY(e.getY()));
			dataSet.addElement(newX(e.getX()) + "," + newY(e.getY()));// 添加点集；
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			if ((t1.getText()).equals("")) {
				JOptionPane.showMessageDialog(null, "请输入聚类个数！");
				return;
			}
			count = Integer.parseInt(t1.getText());
			if ((list2.getItemCount()) != count) {
				JOptionPane.showMessageDialog(null, "请正确输入初始聚类中心！");
				return;
			}
			list3.removeAll();
			v.removeAllElements();
			candidate.removeAllElements();
			for (int i = 0; i < count; i++) {
				String str = list2.getItem(i);
				String str1[] = str.split(",");
				dataItem = new vector();
				dataItem.centerNodeX = Integer.parseInt(str1[0]);
				dataItem.centerNodeY = Integer.parseInt(str1[1]);
				v.addElement(dataItem);
			}// for循环建立count个容器，并设置每个容器的初始聚类中心；
			for (int i = 0; i < count; i++) {
				dataItem = new vector();
				candidate.addElement(dataItem);
			}

			double d[] = new double[count];// 数组d保存点到每个聚类中心的距离；
			while (equals(v, candidate) == false) {
				for (int i = 0; i < count; i++) {
					((vector) (candidate.elementAt(i))).centerNodeX = ((vector) (v
							.elementAt(i))).centerNodeX;
					((vector) (candidate.elementAt(i))).centerNodeY = ((vector) (v
							.elementAt(i))).centerNodeY;
				}
				for (int i = 0; i < count; i++) {
					((vector) (v.elementAt(i))).removeAllElements();
				}
				for (int i = 0; i < dataSet.size(); i++) {
					for (int j = 0; j < count; j++) {
						d[j] = Math
								.hypot(dataSet.getX(i)
										- ((vector) (candidate.elementAt(j))).centerNodeX,
										dataSet.getY(i)
												- ((vector) (candidate
														.elementAt(j))).centerNodeY);
					}
					int index = min(d);// index表示距离最短的容器的索引；min函数求数组中最小值；
					((vector) (v.elementAt(index))).addElement(dataSet
							.elementAt(i));
				}
				int xx = 0;
				int yy = 0;
				for (int i = 0; i < count; i++) {
					for (int j = 0; j < ((vector) (v.elementAt(i))).size(); j++) {
						xx += ((vector) (v.elementAt(i))).getX(j);
						yy += ((vector) (v.elementAt(i))).getY(j);
					}
					((vector) (v.elementAt(i))).centerNodeX = xx
							/ ((vector) (v.elementAt(i))).size();// 新的聚类中心的x坐标；
					((vector) (v.elementAt(i))).centerNodeY = yy
							/ ((vector) (v.elementAt(i))).size();// 新的聚类中心的y坐标；
					xx = 0;
					yy = 0;
				}// for循环确定新的聚类中心；
			}// k-means算法；
			for (int i = 0; i < count; i++) {
				list3.add("第" + (i + 1) + "个聚类");
				for (int j = 0; j < ((vector) (v.elementAt(i))).size(); j++) {
					list3.add((((vector) (v.elementAt(i))).elementAt(j))
							.toString());
				}
			}
			Graphics g = this.getGraphics();
			for (int i = 0; i < count; i++) {
				for (int j = 0; j < ((vector) (v.elementAt(i))).size(); j++) {
					int nodeX = ((vector) (v.elementAt(i))).getX(j);
					int nodeY = ((vector) (v.elementAt(i))).getY(j);
					int average = 255 / count;
					g.setColor(new Color(average * i, 255 - average * i,
							average * i));
					g.fillOval(nodeX + 30, 400 - nodeY, 4, 4);
				}
			}
		} else if (e.getSource() == b2) {
			list1.removeAll();
			list2.removeAll();
			list3.removeAll();
			v.removeAllElements();
			candidate.removeAllElements();
			dataSet.removeAllElements();
			t1.setText("");
			repaint();
			count = 0;
		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == list1) {
			list2.add(list1.getSelectedItem());
		} else if (e.getSource() == list2) {
			if (list2.getItemCount() == 0) {
				return;
			}
			list2.remove(list2.getSelectedIndex());
		}
	}

	public int min(double a[]) {
		double min = a[0];
		int minIndex = 0;
		for (int i = 0; i < a.length; i++) {
			if (min > a[i]) {
				min = a[i];
			}
		}
		for (int i = 0; i < a.length; i++) {
			if (a[i] == min) {
				minIndex = i;
			}
		}
		return minIndex;
	}

	public boolean equals(Vector v1, Vector v2) {
		for (int i = 0; i < count; i++) {
			if ((((vector) (v1.elementAt(i))).centerNodeX) != (((vector) (v2
					.elementAt(i))).centerNodeX)
					|| (((vector) (v1.elementAt(i))).centerNodeY) != (((vector) (v2
							.elementAt(i))).centerNodeY)) {
				return false;
			}
		}
		return true;
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		g.drawLine(30, 400, 30, 50);
		g.drawString("Y", 25, 45);
		g.drawLine(30, 400, 400, 400);
		g.drawString("X", 405, 400);
		g.drawLine(30, 50, 25, 60);
		g.drawLine(30, 50, 35, 60);
		g.drawLine(400, 400, 390, 395);
		g.drawLine(400, 400, 390, 405);
		g.drawString("0", 20, 410);

		g.drawLine(415, 0, 415, 430);
	}

	public int newX(int x) {
		return x - 30;
	}

	public int newY(int y) {
		return 400 - y;
	}
}

class vector extends Vector {
	double centerNodeX = 0;// 保存中心点的X坐标；
	double centerNodeY = 0;// 保存中心点的Y坐标；

	public int getX(int x) {
		String str = (this.elementAt(x)).toString();
		String str1[] = str.split(",");
		int nodeX = Integer.parseInt(str1[0]);
		return nodeX;
	}

	public int getY(int y) {
		String str = (this.elementAt(y)).toString();
		String str1[] = str.split(",");
		int nodeY = Integer.parseInt(str1[1]);
		return nodeY;
	}
}