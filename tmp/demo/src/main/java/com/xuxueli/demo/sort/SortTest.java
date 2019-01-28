package com.xuxueli.demo.sort;

import java.util.Arrays;

/**
 * Sort Test
 * <p>
 * https://www.cnblogs.com/guoyaohua/p/8600214.html
 */
public class SortTest {

    public static void main(String[] args) {
        int[] data = new int[]{1, 0, 3, 8, 5, 6, 7, 4, 9, 2};

        if (data.length > 1) {
            new HeapSort().Sort(data);
        }

        System.out.println(Arrays.toString(data));
    }

    public static class HeapSort{

        private int len;

        /**
         * 堆排序算法
         *
         * @param array
         * @return
         */
        public int[] Sort(int[] array) {
            len = array.length;

            //1.构建一个最大堆：从最后一个非叶子节点开始向上构造最大堆
            for (int i = (len/2 - 1); i >= 0; i--) {
                adjustHeap(array, i);
            }

            //2.循环将堆首位（最大值）与末位交换，然后在重新调整最大堆
            while (len > 0) {
                swap(array, 0, len - 1);
                len--;
                adjustHeap(array, 0);
            }
            return array;
        }

        /**
         * 调整使之成为最大堆
         *
         * @param array
         * @param i
         */
        public void adjustHeap(int[] array, int i) {
            int maxIndex = i;
            //如果有左子树，且左子树大于父节点，则将最大指针指向左子树
            if (i * 2 < len && array[i * 2] > array[maxIndex]) {
                maxIndex = i * 2;
            }
            //如果有右子树，且右子树大于父节点，则将最大指针指向右子树
            if (i * 2 + 1 < len && array[i * 2 + 1] > array[maxIndex]) {
                maxIndex = i * 2 + 1;
            }
            //如果父节点不是最大值，则将父节点与最大值交换，并且递归调整与父节点交换的位置。
            if (maxIndex != i) {
                swap(array, maxIndex, i);
                adjustHeap(array, maxIndex);
            }
        }

        // 交换数组元素
        public void swap(int[] array, int i, int j) {
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

    }


    // 插入 (时间 O-n2；空间O-n1; 内部排序；稳定；)
    public static int[] insertionSort(int[] array){

        for (int i = 0; i < array.length - 1; i++) {
            int preIndex = i;
            int current = array[i+1];
            while (preIndex>=0 && current<array[preIndex]) {
                array[preIndex+1] = array[preIndex];
                preIndex--;
            }
            array[preIndex+1] = current;
        }

        return array;
    }

    // 冒泡 (时间 O-n2；空间O-n1; 内部排序；稳定；)
    public static int[] bubbleSort(int[] array){
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length-i-1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                }
            }
        }
        return array;
    }



}
