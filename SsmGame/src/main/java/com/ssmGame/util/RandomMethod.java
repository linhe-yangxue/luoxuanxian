package com.ssmGame.util;

import java.util.List;

public class RandomMethod {
			
		//计算在probs中，哪个击中了？
		public static int CalcHitWhichIndex(int[] probs)
		{
			if (probs == null)
			{
				return -1;
			}
			
			int[] index_list = new int[probs.length];
			int[] value_list = new int[probs.length];
			int sum = 0;
			for (int i = 0, j = 0; i < probs.length; ++i)
			{
				int p = probs[i];
				if (p > 0) {
					index_list[j] = i;
					value_list[j] = p;
					j++;
					sum += p;
				}
			}
			int rand = (int)(Math.random() * sum);
			//System.out.println("sum " + sum + " rand " + rand);
			sum = 0;
			
			for (int i = 0; i < value_list.length; ++i)
			{
				sum += value_list[i];
				if (rand < sum)
					return index_list[i];
			}
			return -1;
		}
		
		//计算在probs中，哪个击中了？
		public static int CalcHitWhichIndex(List<Integer> probs)
		{
			if (probs == null)
			{
				return -1;
			}
			
			int[] index_list = new int[probs.size()];
			int[] value_list = new int[probs.size()];
			int sum = 0;
			for (int i = 0, j = 0; i < probs.size(); ++i)
			{
				int p = probs.get(i);
				if (p > 0) {
					index_list[j] = i;
					value_list[j] = p;
					j++;
					sum += p;
				}
			}
			int rand = (int)(Math.random() * sum);
			sum = 0;
			for (int i = 0; i < value_list.length; ++i)
			{
				sum += value_list[i];
				if (rand < sum)
					return index_list[i];
			}
			return -1;
		}
}
