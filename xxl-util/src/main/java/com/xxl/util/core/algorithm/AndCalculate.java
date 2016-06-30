package com.xxl.util.core.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 1、位与运算（两个数组，求所有组合）
 * 2、用与计算方法，给定数字数组，求相加得到指定结果的一种组合方案
 * at 波克城市
 * @author xuxueli 2016-6-30 19:37:55
 */
public class AndCalculate {
	public static void main(String[] args) {
		int[] array = { 3, 3, 3, 1, 7 };
		List<Integer> indexs = strategy(array, 9);
		if (indexs == null || indexs.size() == 0) {
			System.out.println("not suitable found...");
		} else {
			for (Integer index : indexs) {
				System.out.print(array[index] + " ");
			}
			System.out.println("");
		}
	}

	/**
	 * 给定数字数组，求相加得到指定结果的一种组合方案
	 * 
	 * @param array		组合元素
	 * @param expected	期望相加后等于的值
	 * @return
	 */
	public static List<Integer> strategy(int[] array, int expected) {
		List<Integer> indexs = new ArrayList<Integer>();

		// 从1循环到2^N
		for (int i = 1; i < 1 << array.length; i++) {
			int sum = 0;
			indexs = new ArrayList<Integer>();

			for (int j = 0; j < array.length; j++) {

				// 用i与2^j进行位与运算，若结果不为0,则表示第j位不为0,从数组中取出第j个数
				if ((i & 1 << j) != 0) {
					sum += array[j];
					indexs.add(j);
				}
			}
			if (sum == expected) {
				break;
			} else {
				indexs = null;
			}
		}

		return indexs;
	}

}