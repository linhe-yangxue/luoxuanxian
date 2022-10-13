package com.ssmCore.tool.colligate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class TRandom {

	/**
	 * 传入几率字符串 , "100,200,300,400"
	 * 
	 * @param rate
	 * @return 返回几率的下标 [0,1,2,3]
	 */
	public static Integer getRate(String rate) {
		String[] ss = StringUtils.split(rate, ",");
		RandomMap map = new RandomMap();
		for (int i = 0; i < ss.length; i++) {
			Item item = new Item();
			item.id = i;
			item.possibility = Integer.parseInt(ss[i]);
			map.addItem(item);
		}
		Item item = map.getRandomItem(Math.random() * map.possibility);
		if (item != null) {
			return item.id;
		} else {
			return null;
		}
	}

	public static Integer getRate(Integer[] rate) {
		RandomMap map = new RandomMap();
		for (int i = 0; i < rate.length; i++) {
			Item item = new Item();
			item.id = i;
			item.possibility = rate[i];
			map.addItem(item);
		}

		Item item = map.getRandomItem(Math.random() * map.possibility);
		if (item != null) {
			return item.id;
		} else {
			return null;
		}
	}

	public static Integer[] getRates(Integer[] rate, Integer count) {
		if (rate.length <= count) {
			try {
				throw new Exception("随即数量大于数组长度！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<Item> items = new ArrayList<Item>();
		for (int i = 0; i < rate.length; i++) {
			Item item = new Item();
			item.id = i;
			item.possibility = rate[i];
			items.add(item);
		}
		RandomMap map = new RandomMap();
		resetMap(map, items);
		Integer[] result = new Integer[count];
		for (int i = 0; i < result.length; i++) {
			Item item = map.getRandomItem(Math.random() * map.possibility);
			result[i] = item.id;
			items.remove(item);
			resetMap(map, items);
		}
		return result;
	}

	public static Integer[] getRates(String rate, Integer count) {
		String[] ss = StringUtils.split(rate, ",");
		if (ss.length <= count) {
			try {
				throw new Exception("随即数量大于数组长度！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<Item> items = new ArrayList<Item>();
		for (int i = 0; i < ss.length; i++) {
			Item item = new Item();
			item.id = i;
			item.possibility = Integer.parseInt(ss[i]);
			items.add(item);
		}
		RandomMap map = new RandomMap();
		resetMap(map, items);
		Integer[] result = new Integer[count];
		for (int i = 0; i < result.length; i++) {
			Item item = map.getRandomItem(Math.random() * map.possibility);
			result[i] = item.id;
			items.remove(item);
			resetMap(map, items);
		}
		return result;
	}

	public static void resetMap(RandomMap map, List<Item> items) {
		map.clear();
		for (Item item : items) {
			map.addItem(item);
		}
	}

	public static Integer getRate(String[] rate) {
		Integer[] retu = new Integer[rate.length];
		for (int i = 0; i < rate.length; i++) {
			retu[i] = Integer.parseInt(rate[i]);
		}
		return getRate(retu);
	}

	/**
	 * 随机排序
	 * 
	 * @param list
	 * @return
	 */
	public static <T> List<T> roundList(List<T> list) {
		int size = list.size();
		Random r = new Random();
		while (size > 0) {
			T t = list.remove(r.nextInt(size));
			if (r.nextBoolean()) {
				list.add(0, t);
			} else {
				list.add(t);
			}
			size--;
		}
		return list;
	}

}

class Item {
	public int id; // 奖励类型
	public int possibility; // 奖励几率
}

class RandomMap {

	public int possibility = 0;
	/** <possibility,item> */
	public TreeMap<Integer, Item> map = new TreeMap<Integer, Item>();

	public void addItem(Item item) {
		map.put(possibility, item);
		possibility += item.possibility;
		item.possibility = possibility;
	}

	public void clear() {
		map.clear();
		possibility = 0;
	}

	public Item getRandomItem(double possibility) {
		int psb = (int) possibility;
		Map.Entry<Integer, Item> entry = map.floorEntry(psb);
		if (entry != null) {
			return entry.getValue();
		}
		return null;
	}
}
