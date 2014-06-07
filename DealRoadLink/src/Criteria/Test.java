package Criteria;

public class Test {

	public static boolean isLinedoubleersectRectangle(double linePodoubleX1,
			double linePodoubleY1, double linePodoubleX2,
			double linePodoubleY2, double rectangleLeftTopX,
			double rectangleLeftTopY, double rectangleRightBottomX,
			double rectangleRightBottomY) {

		double lineHeight = linePodoubleY1 - linePodoubleY2;
		double lineWidth = linePodoubleX2 - linePodoubleX1;
		// 计算叉乘
		double c = linePodoubleX1 * linePodoubleY2 - linePodoubleX2
				* linePodoubleY1;

		if ((lineHeight * rectangleLeftTopX + lineWidth * rectangleLeftTopY + c >= 0 && lineHeight
				* rectangleRightBottomX + lineWidth * rectangleRightBottomY + c <= 0)
				|| (lineHeight * rectangleLeftTopX + lineWidth
						* rectangleLeftTopY + c <= 0 && lineHeight
						* rectangleRightBottomX + lineWidth
						* rectangleRightBottomY + c >= 0)
				|| (lineHeight * rectangleLeftTopX + lineWidth
						* rectangleRightBottomY + c >= 0 && lineHeight
						* rectangleRightBottomX + lineWidth * rectangleLeftTopY
						+ c <= 0)
				|| (lineHeight * rectangleLeftTopX + lineWidth
						* rectangleRightBottomY + c <= 0 && lineHeight
						* rectangleRightBottomX + lineWidth * rectangleLeftTopY
						+ c >= 0)) {
			if (rectangleLeftTopX > rectangleRightBottomX) {
				double temp = rectangleLeftTopX;
				rectangleLeftTopX = rectangleRightBottomX;
				rectangleRightBottomX = temp;
			}
			if (rectangleLeftTopY < rectangleRightBottomY) {
				double temp = rectangleLeftTopY;
				rectangleLeftTopY = rectangleRightBottomY;
				rectangleRightBottomY = temp;
			}
			if ((linePodoubleX1 < rectangleLeftTopX && linePodoubleX2 < rectangleLeftTopX)
					|| (linePodoubleX1 > rectangleRightBottomX && linePodoubleX2 > rectangleRightBottomX)
					|| (linePodoubleY1 > rectangleLeftTopY && linePodoubleY2 > rectangleLeftTopY)
					|| (linePodoubleY1 < rectangleRightBottomY && linePodoubleY2 < rectangleRightBottomY)) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	public static void main(String[]args) {
		new Test();
		if(Test.isLinedoubleersectRectangle(115.468498, 39.823601, 115.468552, 39.823606, 115.4675, 39.82333968, 115.46875, 39.82417304)){
			System.out.println("True");
		}else{
			System.out.println("False");
		}
		
	}
}
