package com.ssmCore.tool.colligate;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;

public class CommUtil {
	/**
	 * 随机数生成器
	 */
	private static Random RANDOM = new Random();

	/**
	 * @param src
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T cloneObject(T src) {
		if (src == null) {
			return src;
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(baos);
			os.writeObject(src);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream is = new ObjectInputStream(bais);
			T dst = (T) is.readObject();

			os.close();
			is.close();

			baos = null;
			os = null;
			bais = null;
			is = null;

			return dst;
		} catch (Throwable err) {
			err.printStackTrace();
		}
		return null;
	}

	/**
	 * @param src
	 * @return
	 */
	public static <T> byte[] serializeObject(T src) {
		if (src != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = null;
			try {
				oos = new ObjectOutputStream(baos);
				oos.writeObject(src);

				byte[] ret = baos.toByteArray();
				return ret;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (oos != null)
						oos.close();
					if (baos != null)
						baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	/**
	 * @param src
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T deserializeObject(byte[] src) {
		if ((src != null) && (src.length > 0)) {
			ByteArrayInputStream bais = new ByteArrayInputStream(src);
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(bais);
				T ret = (T) ois.readObject();

				return ret;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (ois != null)
						ois.close();
					if (bais != null)
						bais.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	/**
	 * @param message
	 */
	public static void printError(String message) {
		try {
			throw new Exception(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param src
	 * @param src_pos
	 * @param dst
	 * @param dst_pos
	 * @param length
	 */

	public static <T> void arrayCopy(T[] src, int src_pos, T[] dst, int dst_pos, int length) {
		System.arraycopy(src, src_pos, dst, dst_pos, length);
	}

	/**
	 * @param array
	 * @param value
	 * @return
	 */
	public static int arrayFirstIndexOf(int[] array, int value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param array1
	 * @param array2
	 * @return
	 */

	public static <T> boolean arrayEquals(T[] array1, T[] array2) {
		if (array1 == array2)
			return true;

		if (((array1 == null) && (array2 != null)) || ((array1 != null) && (array2 == null)))
			return false;

		if (array1.length != array2.length)
			return false;

		for (int i = 0; i < array1.length; i++) {
			if (!array1[i].equals(array2[i]))
				return false;
		}
		return true;
	}

	/**
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static <T> boolean arrayEquals(List<T> array1, List<T> array2) {
		if (array1 == array2) {
			return true;
		}
		if (array1.size() != array2.size()) {
			return false;
		}
		for (int i = 0; i < array1.size(); i++) {
			if (!array1.get(i).equals(array2.get(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 多数组合并
	 * 
	 */
	public static byte[] concatAll(byte[] first, byte[]... rest) {
		if (first == null)
			return null;
		int totalLength = first.length;
		for (byte[] array : rest) {
			totalLength += array.length;
		}
		byte[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (byte[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

	/**
	 * 多数组合并
	 * 
	 */
	public static int[] concatAll(int[] first, int[]... rest) {
		if (first == null)
			return null;
		int totalLength = first.length;
		for (int[] array : rest) {
			totalLength += array.length;
		}
		int[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (int[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

	/**
	 * @param src
	 * @param srcIndex
	 * @param dst
	 * @param dstIndex
	 * @param length
	 * @return
	 */
	public static boolean arrayEquals(byte[] src, int srcIndex, byte[] dst, int dstIndex, int length) {
		for (int i = 0; i < length; i++) {
			if (src[srcIndex + i] != dst[dstIndex + i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static boolean arrayEquals(byte[] array1, byte[] array2) {
		if (array1 == array2) {
			return true;
		}
		if (array1.length != array2.length) {
			return false;
		}
		for (int i = 0; i < array1.length; i++) {
			if (array1[i] != (array2[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static boolean arrayEquals(short[] array1, short[] array2) {
		if (array1 == array2) {
			return true;
		}
		if (array1.length != array2.length) {
			return false;
		}
		for (int i = 0; i < array1.length; i++) {
			if (array1[i] != (array2[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static boolean arrayEquals(int[] array1, int[] array2) {
		if (array1 == array2) {
			return true;
		}
		if (array1.length != array2.length) {
			return false;
		}
		for (int i = 0; i < array1.length; i++) {
			if (array1[i] != (array2[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static boolean arrayEquals(long[] array1, long[] array2) {
		if (array1 == array2) {
			return true;
		}
		if (array1.length != array2.length) {
			return false;
		}
		for (int i = 0; i < array1.length; i++) {
			if (array1[i] != (array2[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static boolean arrayEquals(float[] array1, float[] array2) {
		if (array1 == array2) {
			return true;
		}
		if (array1.length != array2.length) {
			return false;
		}
		for (int i = 0; i < array1.length; i++) {
			if (array1[i] != (array2[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static boolean arrayEquals(double[] array1, double[] array2) {
		if (array1 == array2) {
			return true;
		}
		if (array1.length != array2.length) {
			return false;
		}
		for (int i = 0; i < array1.length; i++) {
			if (array1[i] != (array2[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param array
	 * @param separator
	 * @param end
	 * @return
	 */
	public static <T> String arrayToString(List<T> array, String separator, String end) {
		if (array == null)
			return "null";
		StringBuffer sb = new StringBuffer();
		int count = array.size();
		for (int i = 0; i < count; i++) {
			if (i != count - 1) {
				sb.append(array.get(i) + separator);
			} else {
				sb.append(array.get(i) + end);
			}
		}
		return sb.toString();
	}

	/**
	 * @param array
	 * @param separator
	 * @param end
	 * @return
	 */
	public static <T> String arrayToString(T[] array, String separator, String end) {
		if (array == null)
			return "null";
		StringBuffer sb = new StringBuffer();
		int count = array.length;
		for (int i = 0; i < count; i++) {
			if (i != count - 1) {
				sb.append(array[i] + separator);
			} else {
				sb.append(array[i] + end);
			}
		}
		return sb.toString();
	}

	/**
	 * @param array
	 * @param separator
	 * @return
	 */
	public static <T> String arrayToString(List<T> array, String separator) {
		return arrayToString(array, separator, separator);
	}

	/**
	 * @param array
	 * @param separator
	 * @return
	 */
	public static <T> String arrayToString(T[] array, String separator) {
		return arrayToString(array, separator, separator);
	}

	/**
	 * @param array
	 * @return
	 */
	public static <T> String arrayToString(List<T> array) {
		return arrayToString(array, ",");
	}

	/**
	 * @param array
	 * @return
	 */
	public static <T> String arrayToString(T[] array) {
		return arrayToString(array, ",");
	}

	/**
	 * @param array
	 * @param separator
	 * @param end
	 * @return
	 */
	public static String arrayObjectToString(Object array, String separator, String end) {
		if (array == null) {
			return "null";
		}
		StringBuffer sb = new StringBuffer();
		int count = Array.getLength(array);
		for (int i = 0; i < count; i++) {
			Object s = Array.get(array, i);
			if (i != count - 1) {
				sb.append(s + separator);
			} else {
				sb.append(s + end);
			}
		}
		return sb.toString();
	}

	/**
	 * @param array
	 * @return
	 */
	public static String arrayToString(byte[] array) {
		if (array == null)
			return "null";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i] + ",");
		}
		return sb.toString();
	}

	/**
	 * @param array
	 * @return
	 */
	public static String arrayToString(short[] array) {
		if (array == null)
			return "null";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i] + ",");
		}
		return sb.toString();
	}

	/**
	 * @param array
	 * @return
	 */
	public static String arrayToString(int[] array) {
		if (array == null)
			return "null";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i] + ",");
		}
		return sb.toString();
	}

	/**
	 * @param array
	 * @return
	 */
	public static String arrayToString(long[] array) {
		if (array == null)
			return "null";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i] + ",");
		}
		return sb.toString();
	}

	/**
	 * @param array
	 * @return
	 */
	public static String arrayToString(float[] array) {
		if (array == null)
			return "null";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i] + ",");
		}
		return sb.toString();
	}

	/**
	 * @param array
	 * @return
	 */
	public static String arrayToString(double[] array) {
		if (array == null)
			return "null";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i] + ",");
		}
		return sb.toString();
	}

	/**
	 * @param array
	 * @return
	 */
	public static <T> String[] arrayToStringArray(List<T> array) {
		if (array == null)
			return null;
		synchronized (array) {
			String[] arrays = new String[array.size()];
			for (int i = array.size() - 1; i >= 0; --i) {
				arrays[i] = "" + array.get(i);
			}
			return arrays;
		}
	}

	/**
	 * @param array
	 * @return
	 */
	public static <T> String[] arrayToStringArray(T[] array) {
		if (array == null)
			return null;
		synchronized (array) {
			String[] arrays = new String[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				arrays[i] = "" + array[i];
			}
			return arrays;
		}
	}

	/**
	 * @param array
	 * @return
	 */
	public static String[] arrayToStringArray(byte[] array) {
		if (array == null)
			return null;
		synchronized (array) {
			String[] arrays = new String[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				arrays[i] = "" + array[i];
			}
			return arrays;
		}
	}

	/**
	 * @param array
	 * @return
	 */
	public static String[] arrayToStringArray(short[] array) {
		if (array == null)
			return null;
		synchronized (array) {
			String[] arrays = new String[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				arrays[i] = "" + array[i];
			}
			return arrays;
		}
	}

	/**
	 * @param array
	 * @return
	 */
	public static String[] arrayToStringArray(int[] array) {
		if (array == null)
			return null;
		synchronized (array) {
			String[] arrays = new String[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				arrays[i] = "" + array[i];
			}
			return arrays;
		}
	}

	/**
	 * @param array
	 * @return
	 */
	public static String[] arrayToStringArray(long[] array) {
		if (array == null)
			return null;
		synchronized (array) {
			String[] arrays = new String[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				arrays[i] = "" + array[i];
			}
			return arrays;
		}
	}

	/**
	 * @param array
	 * @return
	 */
	public static String[] arrayToStringArray(float[] array) {
		if (array == null)
			return null;
		synchronized (array) {
			String[] arrays = new String[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				arrays[i] = "" + array[i];
			}
			return arrays;
		}
	}

	/**
	 * @param array
	 * @return
	 */
	public static String[] arrayToStringArray(double[] array) {
		if (array == null)
			return null;
		synchronized (array) {
			String[] arrays = new String[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				arrays[i] = "" + array[i];
			}
			return arrays;
		}
	}

	/**
	 * @param src
	 * @param dst
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T[] arrayCovert(Object[] src, T[] dst) {
		for (int i = src.length - 1; i >= 0; --i) {
			dst[i] = (T) src[i];
		}
		return dst;
	}

	/**
	 * @param array
	 * @return
	 */
	public static final byte[] toByteArray(Collection<Byte> array) {
		if (array == null) {
			return null;
		}
		return toByteArray(array.toArray(new Byte[array.size()]));
	}

	/**
	 * @param array
	 * @return
	 */
	public static final byte[] toByteArray(Byte[] array) {
		if (array == null) {
			return null;
		}
		byte[] ints = new byte[array.length];
		for (int qi = 0; qi < array.length; qi++) {
			ints[qi] = array[qi];
		}
		return ints;
	}

	/**
	 * @param array
	 * @return
	 */
	public static final Byte[] toByteArray(byte[] array) {
		if (array == null) {
			return null;
		}
		Byte[] ints = new Byte[array.length];
		for (int qi = 0; qi < array.length; qi++) {
			ints[qi] = array[qi];
		}
		return ints;
	}

	/**
	 * @param array
	 * @return
	 */
	public static final ArrayList<Byte> toByteCollection(byte... array) {
		ArrayList<Byte> ret = new ArrayList<Byte>(array.length);
		for (byte i : array) {
			ret.add(i);
		}
		return ret;
	}

	/**
	 * @param array
	 * @return
	 */
	public static final int[] toIntArray(Collection<Integer> array) {
		if (array == null) {
			return null;
		}
		return toIntArray(array.toArray(new Integer[array.size()]));
	}

	/**
	 * @param array
	 * @return
	 */
	public static final int[] toIntArray(Integer[] array) {
		if (array == null) {
			return null;
		}
		int[] ints = new int[array.length];
		for (int qi = 0; qi < array.length; qi++) {
			ints[qi] = array[qi];
		}
		return ints;
	}

	/**
	 * @param array
	 * @return
	 */
	public static final Integer[] toIntArray(int[] array) {
		if (array == null) {
			return null;
		}
		Integer[] ints = new Integer[array.length];
		for (int qi = 0; qi < array.length; qi++) {
			ints[qi] = array[qi];
		}
		return ints;
	}

	/**
	 * @param array
	 * @return
	 */
	public static final ArrayList<Integer> toIntCollection(int... array) {
		ArrayList<Integer> ret = new ArrayList<Integer>(array.length);
		for (int i : array) {
			ret.add(i);
		}
		return ret;
	}

	// /**
	// * @param array
	// * @param component_type
	// * @return
	// */
	// public static final <s> T[] toArray(Collection<T> array,
	// Class<T> component_type) {
	// if (array == null) {
	// return null;
	// }
	// T[] ret = (T[]) Array.newInstance(component_type, array.size());
	// array.toArray(ret);
	// return ret;
	// }

	/**
	 * @param component_type
	 * @param len
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T[] newArray(Class<T> component_type, int len) {
		T[] ret = (T[]) Array.newInstance(component_type, len);
		return ret;
	}

	/**
	 * @param component_type
	 * @param compoments
	 * @return
	 */
	@SafeVarargs
	public static <T> T[] newArray(Class<T> component_type, T... compoments) {
		T[] ret = Arrays.copyOf(compoments, compoments.length);
		return ret;
	}

	/**
	 * @param array
	 * @param e
	 * @return
	 */
	public static int[] arrayAddElement(int[] array, int e) {
		if (array == null) {
			int[] na = new int[1];
			na[0] = e;
			return na;
		}
		int[] na = new int[array.length + 1];
		System.arraycopy(array, 0, na, 0, array.length);
		na[array.length] = e;
		return na;
	}

	/**
	 * @param array
	 * @param e
	 * @return
	 */
	public static int[] arrayRemoveElement(int[] array, int e) {
		if (array == null) {
			return new int[0];
		}
		int[] na = new int[array.length];
		int n = 0;
		for (int o : array) {
			if (o != e) {
				na[n++] = o;
			}
		}
		int[] nb = new int[n];
		System.arraycopy(na, 0, nb, 0, n);
		return nb;
	}

	/**
	 * @param array
	 * @param e
	 * @return
	 */
	public static long[] arrayAddElement(long[] array, long e) {
		if (array == null) {
			long[] na = new long[1];
			na[0] = e;
			return na;
		}
		long[] na = new long[array.length + 1];
		System.arraycopy(array, 0, na, 0, array.length);
		na[array.length] = e;
		return na;
	}

	/**
	 * @param array
	 * @param e
	 * @return
	 */
	public static long[] arrayRemoveElement(long[] array, long e) {
		if (array == null) {
			return new long[0];
		}
		long[] na = new long[array.length];
		int n = 0;
		for (long o : array) {
			if (o != e) {
				na[n++] = o;
			}
		}
		long[] nb = new long[n];
		System.arraycopy(na, 0, nb, 0, n);
		return nb;
	}

	/**
	 * @param array
	 * @param e
	 * @return
	 */
	public static boolean arrayIsContain(int[] array, int e) {
		for (int o : array)
			if (o == e)
				return true;
		return false;
	}

	/**
	 * @param array
	 * @param e
	 * @return
	 */
	public static boolean arrayIsContain(long[] array, long e) {
		for (long o : array)
			if (o == e)
				return true;
		return false;
	}

	/**
	 * <pre>
	 * eg: src = "12" other="0000" return "1200"
	 * 
	 * @param src
	 * @param other
	 * @return
	 */
	public static String fillStringHead(String src, String other) {
		char[] ret = other.toCharArray();
		for (int s = 0, d = 0; s < src.length() && d < ret.length; s++, d++) {
			ret[d] = src.charAt(s);
		}
		return new String(ret);
	}

	/**
	 * <pre>
	 * eg: src = "12" other="0000" return "0012"
	 * 
	 * @param src
	 * @param other
	 * @return
	 */
	public static String fillStringTail(String src, String other) {
		char[] ret = other.toCharArray();
		for (int s = src.length() - 1, d = ret.length - 1; s >= 0 && d > 0; s--, d--) {
			ret[d] = src.charAt(s);
		}
		return new String(ret);
	}

	/**
	 * @param ch
	 * @param count
	 * @return
	 */
	public static String fillString(String ch, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(ch);
		}
		return sb.toString();
	}

	private static char FORMAT_KEY = '\b';

	/**
	 * @param key
	 */
	public static void setDefaultFormatStringKey(char key) {
		FORMAT_KEY = key;
	}

	/**
	 * @param src
	 * @param args
	 * @return
	 */
	public static String formatString(String src, String[] args) {
		return formatString(src, FORMAT_KEY, args);
	}

	/**
	 * formatString("abcde abcde",'a',new String[]{"A"}) == "Abcde abcde";
	 * formatString("abcde abcde",'a',new String[]{"A","ABC"}) ==
	 * "Abcde ABCbcde";
	 * 
	 * @param src
	 * @param key
	 * @param args
	 * @return
	 */
	public static String formatString(String src, char key, String[] args) {
		if (args == null || args.length <= 0) {
			return src;
		} else {
			int argIndex = 0;

			StringBuffer sb = new StringBuffer();
			int count = src.length();

			for (int i = 0; i < count; i++) {
				char c = src.charAt(i);

				if (c == key) {
					if (argIndex < args.length) {
						sb.append(args[argIndex]);
						argIndex++;
					} else if (i < count - 1) {
						sb.append(src.substring(i + 1));
					}
				} else {
					sb.append(c);
				}
			}

			return sb.toString();
		}
	}

	/**
	 * get [key : value] form str <\b> str = "FORCE=A" key = "FORCE=" value =
	 * "A"
	 * 
	 * @param str
	 * @param key
	 * @param value
	 */
	static public String getStringValueFromKey(String str, String key) {
		int index = str.indexOf(key);
		if (index >= 0) {
			return str.substring(index + key.length());
		} else {
			return null;
		}
	}

	/**
	 * 将字符串格式化为main入口一样的args<br>
	 * 比如输入<br>
	 * command /switch "d:\abc\def gh" <br>
	 * 输出<br>
	 * [command]<br>
	 * [/switch]<br>
	 * [d:\abc\def gh]<br>
	 * 
	 * @param text
	 * @return
	 */
	static public String[] splitStringToArgs(String text) {
		ArrayList<String> ret = new ArrayList<String>(1);

		StringBuilder sb = new StringBuilder(text.length());
		boolean is_lc = false;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			switch (ch) {
			case '\"': {
				if (!is_lc) {
					is_lc = true;
				} else {
					is_lc = false;
					if (sb.length() > 0) {
						ret.add(sb.toString());
						sb.setLength(0);
					}
				}
				break;
			}
			case ' ':
			case '\t': {
				if (is_lc) {
					sb.append(ch);
				} else if (sb.length() > 0) {
					ret.add(sb.toString());
					sb.setLength(0);
				}
				break;
			}
			default:
				sb.append(ch);
			}
		}
		if (sb.length() > 0) {
			ret.add(sb.toString());
			sb.setLength(0);
		}

		return ret.toArray(new String[ret.size()]);
	}

	/**
	 * @param hex
	 * @return
	 */
	public static String hex2str(String hex) {
		StringBuilder bindata = new StringBuilder();
		if (hex.length() % 2 != 0) {
			hex = "0" + hex;
		}
		for (int i = 0; i < hex.length(); i += 2) {
			bindata.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
		}
		return bindata.toString();
	}

	/**
	 * @param str
	 * @return
	 */
	public static String str2hex(String str) {
		StringBuilder hexdata = new StringBuilder();
		for (int i = 0; i < str.length(); i += 1) {
			String hex = Integer.toString(0x00ff & str.charAt(i), 16);
			if (hex.length() < 2) {
				hexdata.append("0" + hex);
			} else {
				hexdata.append(hex);
			}
		}
		return hexdata.toString();
	}

	/**
	 * @param bin
	 * @return
	 */
	public static String bin2hex(byte[] bin) {
		StringBuilder hexdata = new StringBuilder();
		for (int i = 0; i < bin.length; i++) {
			String hex = Integer.toString(0x00ff & bin[i], 16);
			if (hex.length() < 2) {
				hexdata.append("0" + hex);
			} else if (hex.length() == 2) {
				hexdata.append(hex);
			} else {
			}
		}
		return hexdata.toString();
	}

	/**
	 * 字符转字节数组
	 * 
	 * @param hex
	 *            string
	 * @return byte[]
	 */
	public static byte[] hex2bin(String hex) {
		if (hex == null) {
			return null;
		}
		int l = hex.length();
		if (l % 2 == 1) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
		}
		return b;
	}

	/**
	 * 字节数组转字符串
	 * 
	 * @param by
	 *            byte[]
	 * @return string
	 */
	public static String getHexDump(byte[] data) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < data.length; n++) {
			stmp = (java.lang.Integer.toHexString(data[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	/**
	 * @param bytes
	 * @return
	 */
	public static String toBytesSizeString(long bytes) {
		return getBytesSizeString(bytes);
	}

	/**
	 * @param bytes
	 * @return
	 */
	public static String getBytesSizeString(long bytes) {
		long b = bytes;
		long kb = b >> 10;
		long mb = kb >> 10;
		long gb = mb >> 10;
		long tb = gb >> 10;

		if (tb > 10) {
			return tb + "." + gb % 1024 + "t";
		}
		if (gb > 10) {
			return gb + "." + mb % 1024 + "g";
		}
		if (mb > 10) {
			return mb + "." + kb % 1024 + "m";
		}
		if (kb > 10) {
			return kb + "." + b % 1024 + "k";
		}

		return b + "b";
	}

	public static String timesliceToStringHour(long timesliceInMillis) {

		long time = timesliceInMillis;

		int ss = (int) (time / 1000 % 60);
		int mm = (int) (time / 1000 / 60 % 60);
		int hh = (int) (time / 1000 / 60 / 60);

		String ret = hh + ":" + intToSting(mm, 10, 2, "0") + ":" + intToSting(ss, 10, 2, "0");

		return ret;
	}

	/**
	 * @param value
	 * @param max
	 * @param maxColor
	 * @param minColor
	 * @return
	 */
	static public int toStripColorGR(int value, int max, int maxColor, int minColor) {
		int hp = Math.min(value, max);
		int halfWear = max / 2;

		int G = (hp >= halfWear) ? 0xff : (hp * 2);
		int R = (hp <= halfWear) ? 0xff : ((max - hp) * 2);

		int Color = 0xff000000 | (((maxColor >> 0) & G) << 0) | (((maxColor >> 8) & G) << 8)
				| (((maxColor >> 16) & G) << 16) | (((minColor >> 0) & R) << 0) | (((minColor >> 8) & R) << 8)
				| (((minColor >> 16) & R) << 16);

		return Color;
	}

	/**
	 * @param argb
	 * @return
	 */
	static public String toColorKey(int argb) {
		String text = Long.toString(0x00000000ffffffff & argb, 16);
		if (text.startsWith("0x")) {
			return text.substring(2);
		}
		return text;
	}

	/**
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	static public int toColor(int r, int g, int b) {
		return 0xff000000 | ((r > 0xff ? 0xff : r) << 16) | ((g > 0xff ? 0xff : g) << 8) | ((b > 0xff ? 0xff : b));
	}

	/**
	 * @param a
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	static public int toColor(int a, int r, int g, int b) {
		return ((a > 0xff ? 0xff : a) << 24) | ((r > 0xff ? 0xff : r) << 16) | ((g > 0xff ? 0xff : g) << 8)
				| ((b > 0xff ? 0xff : b));
	}

	/**
	 * @param a
	 * @param rgb
	 * @return
	 */
	static public int toColor(int a, int rgb) {
		return ((a > 0xff ? 0xff : a) << 24) | (0x00ffffff & rgb);
	}

	static StringCompare default_string_compare = new StringCompare();

	/**
	 * @author Administrator
	 * @param <T1>
	 * @param <T2>
	 */
	static public interface ICompare<T1, T2> {
		/**
		 * @param a
		 *            i
		 * @param b
		 *            i+1
		 * @return if ret < 0 q[b] -> q[a]
		 */
		public int compare(T1 a, T2 b);
	}

	/**
	 * @author Administrator
	 */
	static public class StringCompare implements ICompare<String, String>, Comparator<String> {
		public int compare(String a, String b) {
			return stringCompare(a, b);
		}
	}

	/**
	 * @return
	 */
	static public StringCompare getStringCompare() {
		return default_string_compare;
	}

	/**
	 * @param a
	 * @param b
	 * @return
	 */
	static public int stringCompare(String a, String b) {
		int len = Math.min(a.length(), b.length());
		for (int i = 0; i < len; i++) {
			char ca = a.charAt(i);
			char cb = b.charAt(i);
			if (ca != cb) {
				return cb - ca;
			}
		}
		return b.length() - a.length();
	}

	/**
	 * current sort rule is popo sort
	 * 
	 * @param list
	 * @param compare
	 */
	static public <T> T[] sort(T list[], ICompare<T, T> compare) {
		T temp;
		boolean tag = true;
		for (int i = list.length - 1; i >= 0; i--) {
			for (int j = 0; j < i; j++) {
				if (compare.compare(list[j], list[j + 1]) < 0) {
					temp = list[j];
					list[j] = list[j + 1];
					list[j + 1] = temp;
					tag = true;
				}
			}
			if (tag == false)
				break;
		}
		return list;
	}

	/**
	 * current sort rule is popo sort
	 * 
	 * @param list
	 * @param compare
	 */
	static public <T> List<T> sort(List<T> list, ICompare<T, T> compare) {
		T temp2;
		T temp1;
		boolean tag = true;
		for (int i = list.size() - 1; i >= 0; i--) {
			for (int j = 0; j < i; j++) {
				if (compare.compare(list.get(j), list.get(j + 1)) < 0) {
					temp1 = list.get(j);
					temp2 = list.get(j + 1);
					list.set(j, temp2);
					list.set(j + 1, temp1);
					tag = true;
				}
			}
			if (tag == false)
				break;
		}
		return list;
	}

	/**
	 * @param arrays
	 * @return
	 */
	static public int[] link(int[]... arrays) {
		int count = 0;
		for (int[] array : arrays) {
			if (array != null)
				count += array.length;
		}
		int[] ret = new int[count];
		count = 0;
		for (int[] array : arrays) {
			if (array != null) {
				System.arraycopy(array, 0, ret, count, array.length);
				count += array.length;
			}
		}
		return ret;
	}

	/**
	 * @param arrays
	 * @return
	 */
	static public short[] link(short[]... arrays) {
		int count = 0;
		for (short[] array : arrays) {
			if (array != null)
				count += array.length;
		}
		short[] ret = new short[count];
		count = 0;
		for (short[] array : arrays) {
			if (array != null) {
				System.arraycopy(array, 0, ret, count, array.length);
				count += array.length;
			}
		}
		return ret;
	}

	/**
	 * @param arrays
	 * @return
	 */
	static public byte[] link(byte[]... arrays) {
		int count = 0;
		for (byte[] array : arrays) {
			if (array != null)
				count += array.length;
		}
		byte[] ret = new byte[count];
		count = 0;
		for (byte[] array : arrays) {
			if (array != null) {
				System.arraycopy(array, 0, ret, count, array.length);
				count += array.length;
			}
		}
		return ret;
	}

	/**
	 * @param arrays
	 * @return
	 */
	@SafeVarargs
	static public <T> T[] link(T[]... arrays) {
		T[] ret = null;
		int count = 0;
		for (T[] array : arrays) {
			if (array != null) {
				count += array.length;
				if (ret == null) {
					ret = array;
				}
			}
		}
		ret = Arrays.copyOf(ret, count);
		count = 0;
		for (T[] array : arrays) {
			if (array != null) {
				System.arraycopy(array, 0, ret, count, array.length);
				count += array.length;
			}
		}
		return ret;
	}

	/**
	 * @param arrays
	 * @return
	 */
	@SafeVarargs
	static public <T> Vector<T> linkv(T[]... arrays) {
		int count = 0;
		for (Object[] array : arrays) {
			if (array != null)
				count += array.length;
		}
		Vector<T> retv = new Vector<T>(count);
		for (T[] array : arrays) {
			if (array != null) {
				Collections.addAll(retv, array);
			}
		}
		return retv;
	}

	/**
	 * @param data
	 * @return
	 */
	static public String toHexString(byte[] data) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			if (i > 0 && i % 16 == 0)
				sb.append('\n');
			String ch = Integer.toString(0x000000ff & data[i], 16);
			if (ch.length() < 2)
				ch = "0" + ch;
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * @param i
	 * @param radix
	 * @param len
	 *            bits
	 * @return
	 */
	static public String intToSting(int i, int radix, int len, String before) {
		String str = Integer.toString(i, radix);
		if (str.length() > len) {
			str = str.substring(str.length() - len);
		} else if (str.length() < len) {
			int l = len - str.length();
			for (int d = 0; d < l; d++) {
				str = before + str;
			}
		}
		return str;
	}

	/**
	 * @param strs
	 * @param seperator
	 * @return
	 */
	static public String concat(String[] strs, String seperator) {
		if ((strs != null) && (strs.length > 0)) {
			StringBuffer strbuf = new StringBuffer();

			if ((seperator != null) && !seperator.isEmpty()) {
				for (String str : strs)
					strbuf.append(str).append(seperator);
			} else {
				for (String str : strs)
					strbuf.append(str);
			}

			return strbuf.toString();
		}

		return "";
	}

	/**
	 * @param strs
	 * @param seperator
	 * @return
	 */
	static public String concat(Collection<String> strs, String seperator) {
		if ((strs != null) && !strs.isEmpty()) {
			StringBuffer strbuf = new StringBuffer();

			if ((seperator != null) && !seperator.isEmpty()) {
				for (String str : strs)
					strbuf.append(str).append(seperator);
			} else {
				for (String str : strs)
					strbuf.append(str);
			}

			return strbuf.toString();
		}

		return "";
	}

	/**
	 * @param src
	 * @param start
	 * @param ch
	 * @return
	 */
	static public String getSameCharBlock(String src, int start, char ch) {
		String str = "";

		for (int i = src.indexOf(ch, start); i >= 0 && i < src.length(); i++) {
			if (src.charAt(i) == ch) {
				str += ch;
			} else {
				break;
			}
		}

		return str;
	}

	/**
	 * @param text
	 * @param s
	 * @param d
	 * @return
	 */
	static public String replaceString(String text, String s, String d, int limit) {
		if (text.indexOf(s, 0) < 0) {
			return text;
		}

		int count = 0;

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < text.length(); i++) {
			if (count < limit) {
				int dst = text.indexOf(s, i);
				if (dst >= 0) {
					sb.append(text.substring(i, dst) + d);
					i = dst + s.length() - 1;
					count++;
				} else {
					sb.append(text.substring(i));
					break;
				}
			} else {
				sb.append(text.substring(i));
				break;
			}
		}

		return sb.toString();
	}

	/**
	 * @param text
	 * @param s
	 * @param d
	 * @return
	 */
	static public String replaceString(String text, String s, String d) {
		return replaceString(text, s, d, Integer.MAX_VALUE);
	}

	/**
	 * @author split string
	 * @param text
	 * @param separator
	 * @return
	 */
	static public String[] splitString(String text, String separator, int limit, boolean autotrim) {
		int count = 0;
		try {
			Vector<String> lines = new Vector<String>();
			for (int i = 0; i < text.length(); i++) {
				if (count < limit) {
					int dst = text.indexOf(separator, i);
					if (dst >= 0) {
						lines.addElement(text.substring(i, dst));
						i = dst + separator.length() - 1;
						count++;
					} else {
						lines.addElement(text.substring(i, text.length()));
						break;
					}
				} else {
					lines.addElement(text.substring(i, text.length()));
					break;
				}
			}
			String[] texts = new String[lines.size()];
			lines.copyInto(texts);
			if (autotrim) {
				for (int i = 0; i < texts.length; i++) {
					texts[i] = texts[i].trim();
				}
			}
			lines = null;
			return texts;
		} catch (Exception err) {
			err.printStackTrace();
			return new String[] { "(Error !)" };
		}
	}

	/**
	 * @param text
	 * @param separator
	 * @return
	 */
	static public String[] splitString(String text, String separator) {
		return splitString(text, separator, Integer.MAX_VALUE, false);
	}

	/**
	 * @param text
	 * @param separator
	 * @param limit
	 * @return
	 */
	static public String[] splitString(String text, String separator, int limit) {
		return splitString(text, separator, limit, false);
	}

	/**
	 * @param text
	 * @param separator
	 * @param autotrim
	 * @return
	 */
	static public String[] splitString(String text, String separator, boolean autotrim) {
		return splitString(text, separator, Integer.MAX_VALUE, autotrim);
	}

	/**
	 * @param src
	 * @param des
	 * @param start
	 * @return
	 */
	public static int charArrayIndexOf(char[] src, char des, int start) {
		for (int i = start; i < src.length; i++) {
			if (src[i] == des)
				return i;
		}
		return -1;
	}

	/**
	 * @param src
	 * @param des
	 * @param start
	 * @return
	 */
	public static int charArrayIndexOf(char[] src, char[] des, int start) {
		for (int i = start; i < src.length; i++) {
			if (charArrayCMP(src, start, des.length, des, 0, des.length))
				return i;
		}
		return -1;
	}

	/**
	 * @param src
	 * @param des
	 * @param start
	 * @return
	 */
	public static int charArrayIndexOf(char[] src, char des, int start, int len) {
		for (int i = start; i < start + len; i++) {
			if (src[i] == des)
				return i;
		}
		return -1;
	}

	/**
	 * @param src
	 * @param des
	 * @param start
	 * @return
	 */
	public static int charArrayIndexOf(char[] src, char[] des, int start, int len) {
		for (int i = start; i < start + len; i++) {
			if (charArrayCMP(src, start, des.length, des, 0, des.length))
				return i;
		}
		return -1;
	}

	/**
	 * @param src
	 * @param des
	 * @return
	 */
	public static boolean charArrayCMP(char[] src, char[] des) {
		if (src.length != des.length)
			return false;
		for (int i = src.length - 1; i >= 0; i--) {
			if (src[i] != des[i])
				return false;
		}
		return true;
	}

	/**
	 * @param src
	 * @param src_start
	 * @param src_len
	 * @param des
	 * @param des_start
	 * @param des_len
	 * @return
	 */
	public static boolean charArrayCMP(char[] src, int src_start, int src_len, char[] des, int des_start, int des_len) {
		if (src_len != des_len)
			return false;
		if (src_start + src_len > src.length)
			return false;
		if (des_start + des_len > des.length)
			return false;
		for (int i = src_len - 1; i >= 0; i--) {
			if (src[src_start + i] != des[des_start + i])
				return false;
		}
		return true;
	}

	/**
	 * <summary> \r \n \f \t \b
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int charArrayIsBlank(char[] str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			char ch = str[i];
			if (ch <= ' ') {
				return value;
			} else {
				value++;
			}
		}
		return value;
	}

	/**
	 * <summary>
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int charArrayIsNotWordNum(char[] str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			char ch = str[i];
			if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
				return value;
			} else {
				value++;
			}
		}
		return value;
	}

	/**
	 * <summary>
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int charArrayIsWord(char[] str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			char ch = str[i];
			if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
				value++;
			} else {
				return value;
			}
		}
		return value;
	}

	/**
	 * <summary>
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int charArrayIsWordNum(char[] str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			char ch = str[i];
			if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
				value++;
			} else {
				return value;
			}
		}
		return value;
	}

	/**
	 * <summary>
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int charArrayIsDigitHex(char[] str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			char ch = str[i];
			if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'f' || ch >= 'A' && ch <= 'F') {
				value++;
			} else {
				return value;
			}
		}
		return value;
	}

	/**
	 * <summary>
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int charArrayIsDigit(char[] str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			char ch = str[i];
			if (ch >= '0' && ch <= '9') {
				value++;
			} else {
				return value;
			}
		}
		return value;
	}

	/**
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int charArrayHexToInt(char[] str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			value = value * 0x10;
			switch (str[i]) {
			case '0':
				value += 0x00;
				break;
			case '1':
				value += 0x01;
				break;
			case '2':
				value += 0x02;
				break;
			case '3':
				value += 0x03;
				break;
			case '4':
				value += 0x04;
				break;
			case '5':
				value += 0x05;
				break;
			case '6':
				value += 0x06;
				break;
			case '7':
				value += 0x07;
				break;
			case '8':
				value += 0x08;
				break;
			case '9':
				value += 0x09;
				break;
			case 'a':
			case 'A':
				value += 0x0a;
				break;
			case 'b':
			case 'B':
				value += 0x0b;
				break;
			case 'c':
			case 'C':
				value += 0x0c;
				break;
			case 'd':
			case 'D':
				value += 0x0d;
				break;
			case 'e':
			case 'E':
				value += 0x0e;
				break;
			case 'f':
			case 'F':
				value += 0x0f;
				break;
			default:
				return value;
			}
		}
		return value;
	}

	/**
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int charArrayDigitToInt(char[] str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			value = value * 10;
			switch (str[i]) {
			case '0':
				value += 0x00;
				break;
			case '1':
				value += 0x01;
				break;
			case '2':
				value += 0x02;
				break;
			case '3':
				value += 0x03;
				break;
			case '4':
				value += 0x04;
				break;
			case '5':
				value += 0x05;
				break;
			case '6':
				value += 0x06;
				break;
			case '7':
				value += 0x07;
				break;
			case '8':
				value += 0x08;
				break;
			case '9':
				value += 0x09;
				break;
			default:
				return value;
			}
		}
		return value;
	}

	/**
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int charArrayDecimalToInt(char[] str, int start, int len, int mul) {
		int v = 0;
		int f = 0;
		int m = mul;
		boolean p = false;

		for (int i = start; i < start + len; i++) {
			int bit = -1;
			switch (str[i]) {
			case '0':
				bit = 0;
				break;
			case '1':
				bit = 1;
				break;
			case '2':
				bit = 2;
				break;
			case '3':
				bit = 3;
				break;
			case '4':
				bit = 4;
				break;
			case '5':
				bit = 5;
				break;
			case '6':
				bit = 6;
				break;
			case '7':
				bit = 7;
				break;
			case '8':
				bit = 8;
				break;
			case '9':
				bit = 9;
				break;
			case '.':
				if (!p) {
					p = true;
					bit = 0;
				}
				break;
			default:
				break;
			}
			if (bit < 0)
				break;
			if (p) {
				f += bit * m;
				m = m / 10;
			} else {
				v += bit;
				v = v * 10;
			}
		}

		v = v / 10;
		return v * mul + f;
	}

	/**
	 * 将等宽字符向左对齐，并且长度维持在 length
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static String snapStringRightSize(Object src, int length, char blank) {
		return snapStringRightSize(src.toString(), length, blank);
	}

	/**
	 * 将等宽字符向左对齐，并且长度维持在 length
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static String snapStringRightSize(String src, int length, char blank) {
		if (src.length() < length) {
			for (int i = length - src.length() - 1; i >= 0; --i) {
				src += blank;
			}
		}
		return src;
	}

	/**
	 * 将等宽字符向左对齐，并且长度维持在 length
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static String snapStringRL(Object[] left, Object[] right, char blank, String split) {
		int lc = 0;
		for (Object o : left) {
			lc = Math.max(lc, o.toString().length());
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < left.length; i++) {
			int lb = lc - left[i].toString().length();
			for (int b = 0; b < lb; b++) {
				sb.append(blank);
			}
			sb.append(left[i] + split + right[i] + "\n");
		}
		return sb.toString();
	}

	/**
	 * 将等宽字符向左对齐，并且长度维持在 length
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static String snapStringRL(Map<?, ?> map, char blank, String split) {
		int lc = 0;
		for (Object o : map.keySet()) {
			lc = Math.max(lc, o.toString().length());
		}
		StringBuilder sb = new StringBuilder();
		for (Entry<?, ?> e : map.entrySet()) {
			int lb = lc - e.getKey().toString().length();
			for (int b = 0; b < lb; b++) {
				sb.append(blank);
			}
			sb.append(e.getKey() + split + e.getValue() + "\n");
		}
		return sb.toString();
	}

	/**
	 * <b>提供类似的输出</b> <br>
	 * <table>
	 * <tr>
	 * <th>key</th>
	 * <th>value</th>
	 * <tr>
	 * <td>|- ActiveThreads</td>
	 * <td>: 0</td>
	 * <tr>
	 * <td>|- CorePoolSize</td>
	 * <td>: 20</td>
	 * <tr>
	 * <td>|- PoolSize</td>
	 * <td>: 20</td>
	 * <tr>
	 * <td>|- MaximumPoolSize</td>
	 * <td>: 2147483647</td>
	 * <tr>
	 * <td>|- CompletedTasks</td>
	 * <td>: 28104</td>
	 * <tr>
	 * <td>|- ScheduledTasks</td>
	 * <td>: 405</td>
	 * <tr>
	 * <td>|-------------------</td>
	 * <td></td>
	 * </table>
	 * 
	 * @param key
	 * @param value
	 * @param sb
	 * @return
	 */
	public static String toStatusLine(int size, String key, String value, Appendable sb) {
		if (key.length() < size) {
			char[] blank = new char[size - key.length()];
			Arrays.fill(blank, ' ');
			key = new String(blank) + key;
		}
		String ret = " |-" + key + " : " + value + "\n";
		if (sb != null) {
			try {
				sb.append(ret);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * @see #toStatusLine(String, String, Appendable)
	 * @param sb
	 * @return
	 */
	public static String toStatusSeparator(int size, Appendable sb) {
		char[] blank = new char[size];
		Arrays.fill(blank, '-');
		String ret = " |-" + new String(blank) + "\n";
		if (sb != null) {
			try {
				sb.append(ret);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * @see #toStatusLine(String, String, Appendable)
	 * @param key
	 * @param value
	 * @param sb
	 * @return
	 */
	public static String toStatusLine(int size, Object key, Object value, Appendable sb) {
		return toStatusLine(size, key + "", value + "", sb);
	}

	private static int STATUS_LINE_KEY_SIZE = 32;

	/**
	 * 设置左边的宽度
	 * 
	 * @see #toStatusLine(String, String, Appendable)
	 */
	public static void setStatusLineKeySize(int size) {
		STATUS_LINE_KEY_SIZE = size;
	}

	/**
	 * @param key
	 * @param value
	 * @param sb
	 * @return
	 */
	public static String toStatusLine(String key, String value, Appendable sb) {
		return toStatusLine(STATUS_LINE_KEY_SIZE, key, value, sb);
	}

	/**
	 * @see #toStatusLine(String, String, Appendable)
	 * @param sb
	 * @return
	 */
	public static String toStatusSeparator(Appendable sb) {
		return toStatusSeparator(STATUS_LINE_KEY_SIZE, sb);
	}

	/**
	 * @see #toStatusLine(String, String, Appendable)
	 * @param key
	 * @param value
	 * @param sb
	 * @return
	 */
	public static String toStatusLine(Object key, Object value, Appendable sb) {
		return toStatusLine(key + "", value + "", sb);
	}

	/**
	 * @see #toStatusLine(String, String, Appendable)
	 * @param map
	 * @param sb
	 */
	public static void toStatusLines(int size, Map<?, ?> map, Appendable sb) {
		for (Entry<?, ?> e : map.entrySet()) {
			toStatusLine(size, e.getKey(), e.getValue(), sb);
		}
	}

	/**
	 * @see #toStatusLine(String, String, Appendable)
	 * @param map
	 * @param sb
	 */
	public static String toStatusLines(int size, Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		for (Entry<?, ?> e : map.entrySet()) {
			toStatusLine(size, e.getKey(), e.getValue(), sb);
		}
		return sb.toString();
	}

	/**
	 * @see #toStatusLine(String, String, Appendable)
	 * @param map
	 * @param sb
	 */
	public static void toStatusLines(Map<?, ?> map, Appendable sb) {
		for (Entry<?, ?> e : map.entrySet()) {
			toStatusLine(e.getKey(), e.getValue(), sb);
		}
	}

	/**
	 * @see #toStatusLine(String, String, Appendable)
	 * @param map
	 * @param sb
	 */
	public static String toStatusLines(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		for (Entry<?, ?> e : map.entrySet()) {
			toStatusLine(e.getKey(), e.getValue(), sb);
		}
		return sb.toString();
	}

	/**
	 * @param hd_line
	 * @param lines
	 * @param out
	 * @return
	 */
	public static PrintStream printTable(String[] hd_line, String[][] lines, PrintStream out) {
		List<List<String>> dlines = new ArrayList<List<String>>();
		for (int r = 0; r < lines.length; r++) {
			dlines.add(Arrays.asList(lines[r]));
		}
		printTable(Arrays.asList(hd_line), dlines, out);
		return out;
	}

	/**
	 * @param hd_line
	 * @param lines
	 * @param out
	 * @return
	 */
	public static PrintStream printTable(List<String> hd_line, List<List<String>> lines, PrintStream out) {
		String c_split = " | ";
		String c_begin = "| ";
		String c_end = " |";

		try {
			int max_len = 0;
			ArrayList<Integer> columns_len = new ArrayList<Integer>();
			for (int c = 0; c < hd_line.size(); c++) {
				columns_len.add(hd_line.get(c).length());
			}
			for (List<String> line : lines) {
				int line_len = 0;
				for (int c = 0; c < line.size(); c++) {
					String add = line.get(c);
					int len = columns_len.get(c);
					if (add.length() > len) {
						len = add.length();
						columns_len.set(c, len);
					}
					line_len += len;
				}
				max_len = Math.max(max_len, line_len);
			}

			max_len += (hd_line.size() - 1) * c_split.length() + c_begin.length() + c_end.length();
			out.println(fillString("-", max_len));
			printLine(c_begin, hd_line, columns_len, c_split, c_end, out);
			out.println(fillString("-", max_len));
			for (List<String> line : lines) {
				printLine(c_begin, line, columns_len, c_split, c_end, out);
			}
			out.println(fillString("-", max_len));
		} catch (Exception e) {
			e.printStackTrace(out);
		}
		return out;
	}

	/**
	 * @param begin
	 * @param line
	 * @param columns_len
	 * @param split
	 * @param end
	 * @param out
	 * @return
	 */
	public static PrintStream printLine(String begin, List<String> line, List<Integer> columns_len, String split,
			String end, PrintStream out) {
		try {
			out.append(begin);

			for (int c = 0; c < line.size(); c++) {
				int clen = columns_len.get(c);
				String nd = line.get(c);
				while (nd.length() < clen) {
					nd += " ";
				}
				if (c < line.size() - 1) {
					out.append(nd + split);
				} else {
					out.append(nd);
				}
			}
			out.append(end);
			out.println();
		} catch (Exception e) {
			e.printStackTrace(out);
		}

		return out;
	}

	/**
	 * @param src
	 * @param count
	 * @return
	 */
	public static String stringFill(String src, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = count - 1; i >= 0; --i) {
			sb.append(src);
		}
		return sb.toString();
	}

	/**
	 * <summary> \r \n \f \t \b
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int stringIsBlank(String str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			char ch = str.charAt(i);
			if (ch <= ' ') {
				return value;
			} else {
				value++;
			}
		}
		return value;
	}

	/**
	 * <summary>
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int stringIsNotWordNum(String str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			char ch = str.charAt(i);
			if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
				return value;
			} else {
				value++;
			}
		}
		return value;
	}

	/**
	 * <summary>
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int stringIsWord(String str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			char ch = str.charAt(i);
			if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
				value++;
			} else {
				return value;
			}
		}
		return value;
	}

	public static Integer[] stringToArray(String intString) {
		Integer[] intArray;
		if (StringUtils.indexOf(intString, "[") > -1 && StringUtils.indexOf(intString, "]") > -1) {
			int start = StringUtils.indexOf(intString, "[") + 1;
			int end = StringUtils.indexOf(intString, "]");
			intString = StringUtils.substring(intString, start, end);
		}
		String[] str = StringUtils.split(intString, ",");
		intArray = new Integer[str.length];
		for (int i = 0; i < str.length; i++) {
			intArray[i] = Integer.parseInt(str[i].trim());
		}
		return intArray;
	}

	public static int[] stringToIntArray(String intString) {
		int[] intArray;
		if (StringUtils.indexOf(intString, "[") > -1 && StringUtils.indexOf(intString, "]") > -1) {
			int start = StringUtils.indexOf(intString, "[") + 1;
			int end = StringUtils.indexOf(intString, "]");
			intString = StringUtils.substring(intString, start, end);
		}
		String[] str = StringUtils.split(intString, ",");
		intArray = new int[str.length];
		for (int i = 0; i < str.length; i++) {
			intArray[i] = Integer.parseInt(str[i].trim());
		}
		return intArray;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] stringToObjectArray(String intString, Class<T> arg) {
		T[] intArray;
		if (StringUtils.indexOf(intString, "[") > -1 && StringUtils.indexOf(intString, "]") > -1) {
			int start = StringUtils.indexOf(intString, "[") + 1;
			int end = StringUtils.indexOf(intString, "]");
			intString = StringUtils.substring(intString, start, end);
		}
		String[] str = StringUtils.split(intString, ",");
		intArray = (T[]) Array.newInstance(arg, str.length);
		for (int i = 0; i < str.length; i++) {
			intArray[i] = (T) str[i].trim();
		}
		return intArray;
	}

	/**
	 * <summary>
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int stringIsWordNum(String str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			char ch = str.charAt(i);
			if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
				value++;
			} else {
				return value;
			}
		}
		return value;
	}

	/**
	 * <summary>
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int stringIsDigitHex(String str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			char ch = str.charAt(i);
			if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'f' || ch >= 'A' && ch <= 'F') {
				value++;
			} else {
				return value;
			}
		}
		return value;
	}

	/**
	 * <summary>
	 * 
	 * @param str
	 * @param start
	 * @param len
	 * @return
	 */
	public static int stringIsDigit(String str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			char ch = str.charAt(i);
			if (ch >= '0' && ch <= '9') {
				value++;
			} else {
				return value;
			}
		}
		return value;
	}

	/**
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int stringHexToInt(String str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			value = value * 0x10;
			switch (str.charAt(i)) {
			case '0':
				value += 0x00;
				break;
			case '1':
				value += 0x01;
				break;
			case '2':
				value += 0x02;
				break;
			case '3':
				value += 0x03;
				break;
			case '4':
				value += 0x04;
				break;
			case '5':
				value += 0x05;
				break;
			case '6':
				value += 0x06;
				break;
			case '7':
				value += 0x07;
				break;
			case '8':
				value += 0x08;
				break;
			case '9':
				value += 0x09;
				break;
			case 'a':
			case 'A':
				value += 0x0a;
				break;
			case 'b':
			case 'B':
				value += 0x0b;
				break;
			case 'c':
			case 'C':
				value += 0x0c;
				break;
			case 'd':
			case 'D':
				value += 0x0d;
				break;
			case 'e':
			case 'E':
				value += 0x0e;
				break;
			case 'f':
			case 'F':
				value += 0x0f;
				break;
			default:
				return value;
			}
		}
		return value;
	}

	/**
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int stringDigitToInt(String str, int start, int len) {
		int value = 0;
		for (int i = start; i < start + len; i++) {
			value = value * 10;
			switch (str.charAt(i)) {
			case '0':
				value += 0x00;
				break;
			case '1':
				value += 0x01;
				break;
			case '2':
				value += 0x02;
				break;
			case '3':
				value += 0x03;
				break;
			case '4':
				value += 0x04;
				break;
			case '5':
				value += 0x05;
				break;
			case '6':
				value += 0x06;
				break;
			case '7':
				value += 0x07;
				break;
			case '8':
				value += 0x08;
				break;
			case '9':
				value += 0x09;
				break;
			default:
				return value;
			}
		}
		return value;
	}

	/**
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static int stringDecimalToInt(String str, int start, int len, int mul) {
		int v = 0;
		int f = 0;
		int m = mul;
		boolean p = false;

		for (int i = start; i < start + len; i++) {
			int bit = -1;
			switch (str.charAt(i)) {
			case '0':
				bit = 0;
				break;
			case '1':
				bit = 1;
				break;
			case '2':
				bit = 2;
				break;
			case '3':
				bit = 3;
				break;
			case '4':
				bit = 4;
				break;
			case '5':
				bit = 5;
				break;
			case '6':
				bit = 6;
				break;
			case '7':
				bit = 7;
				break;
			case '8':
				bit = 8;
				break;
			case '9':
				bit = 9;
				break;
			case '.':
				if (!p) {
					p = true;
					bit = 0;
				}
				break;
			default:
				break;
			}
			if (bit < 0)
				break;
			if (p) {
				f += bit * m;
				m = m / 10;
			} else {
				v += bit;
				v = v * 10;
			}
		}

		v = v / 10;
		return v * mul + f;
	}

	/**
	 * <summary>
	 * 
	 * @param src
	 * @param key
	 * @return
	 */
	final static public int stringKeyValue(String src, String key) {
		try {
			int pos = src.indexOf(key) + key.length();
			return Integer.parseInt(src.substring(pos, pos + stringIsDigit(src, pos, src.length() - pos)), 10);
		} catch (Exception err) {
			return -1;
		}
	}

	/**
	 * <summary> save byte to data (0xaa,0xbb,0xcc,0xdd = 0xddccbbaa)
	 * 
	 * @param
	 * @param
	 * @param
	 */
	public static void write8(byte[] data, int p, byte val) {
		data[p++] = val;
	}

	/**
	 * <summary> (0xaa,0xbb,0xcc,0xdd = 0xddccbbaa)
	 * 
	 * @param
	 * @param
	 * @param
	 */
	public static void write16(byte[] data, int p, short val) {
		data[p + 0] = (byte) (0x00ff & (val >> 0));
		data[p + 1] = (byte) (0x00ff & (val >> 8));
	}

	/**
	 * <summary> (0xaa,0xbb,0xcc,0xdd = 0xddccbbaa)
	 * 
	 * @param
	 * @param
	 * @param
	 */
	public static void write24(byte[] data, int p, int val) {
		data[p + 0] = (byte) (0x00ff & (val >> 0));
		data[p + 1] = (byte) (0x00ff & (val >> 8));
		data[p + 2] = (byte) (0x00ff & (val >> 16));
	}

	/**
	 * <summary> (0xaa,0xbb,0xcc,0xdd = 0xddccbbaa)
	 * 
	 * @param
	 * @param
	 * @param
	 */
	public static void write32(byte[] data, int p, int val) {
		data[p + 0] = (byte) (0x00ff & (val >> 0));
		data[p + 1] = (byte) (0x00ff & (val >> 8));
		data[p + 2] = (byte) (0x00ff & (val >> 16));
		data[p + 3] = (byte) (0x00ff & (val >> 24));
	}

	/**
	 * <summary> (0xaa,0xbb,0xcc,0xdd = 0xddccbbaa)
	 * 
	 * @param
	 * @param
	 * @param
	 */
	public static void write64(byte[] data, int p, long val) {
		data[p + 0] = (byte) (0x00ff & (val >> 0));
		data[p + 1] = (byte) (0x00ff & (val >> 8));
		data[p + 2] = (byte) (0x00ff & (val >> 16));
		data[p + 3] = (byte) (0x00ff & (val >> 24));
		data[p + 4] = (byte) (0x00ff & (val >> 32));
		data[p + 5] = (byte) (0x00ff & (val >> 40));
		data[p + 6] = (byte) (0x00ff & (val >> 48));
		data[p + 7] = (byte) (0x00ff & (val >> 56));
	}

	/**
	 * <summary> (0xaa,0xbb,0xcc,0xdd = 0xddccbbaa)
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static byte read8(byte[] data, int p) {
		return data[p++];
	}

	/**
	 * <summary> (0xaa,0xbb,0xcc,0xdd = 0xddccbbaa)
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static short read16(byte[] data, int p) {
		short val = (short) ((data[p + 0] << 0) & 0x00ff | (data[p + 1] << 8) & 0xff00);

		return val;
	}

	/**
	 * <summary> (0xaa,0xbb,0xcc,0xdd = 0xddccbbaa)
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static int read24(byte[] data, int p) {
		int value = ((data[p + 0] << 0) & 0x000000ff | (data[p + 1] << 8) & 0x0000ff00
				| (data[p + 2] << 16) & 0x00ff0000);
		return value;
	}

	/**
	 * <summary> (0xaa,0xbb,0xcc,0xdd = 0xddccbbaa)
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static int read32(byte[] data, int p) {
		int value = ((data[p + 0] << 0) & 0x000000ff | (data[p + 1] << 8) & 0x0000ff00
				| (data[p + 2] << 16) & 0x00ff0000 | (data[p + 3] << 24) & 0xff000000);
		return value;
	}

	/**
	 * <summary> (0xaa,0xbb,0xcc,0xdd = 0xddccbbaa)
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static long read64(byte[] data, int p) {
		long value = (0 | (data[p + 0] << 0) | (data[p + 1] << 8) | (data[p + 2] << 16) | (data[p + 3] << 24)
				| (data[p + 4] << 32) | (data[p + 5] << 40) | (data[p + 6] << 48) | (data[p + 7] << 56));
		return value;
	}

	/**
	 * <summary> (0xaa,0xbb,0xcc,0xdd = 0xaabbccdd)
	 * 
	 * @param
	 * @param
	 * @param
	 */
	public static void writeMSB8(byte[] data, int p, byte val) {
		data[p++] = val;
	}

	/**
	 * <summary> (0xaa,0xbb,0xcc,0xdd = 0xaabbccdd)
	 * 
	 * @param
	 * @param
	 * @param
	 */
	public static void writeMSB16(byte[] data, int p, short val) {
		data[p + 0] = (byte) (0x00ff & (val >> 8));
		data[p + 1] = (byte) (0x00ff & (val >> 0));
	}

	/**
	 * <summary> (0xaa,0xbb,0xcc,0xdd = 0xaabbccdd)
	 * 
	 * @param
	 * @param
	 * @param
	 */
	public static void writeMSB24(byte[] data, int p, int val) {
		data[p + 0] = (byte) (0x00ff & (val >> 16));
		data[p + 1] = (byte) (0x00ff & (val >> 8));
		data[p + 2] = (byte) (0x00ff & (val >> 0));
	}

	/**
	 * @param keys
	 * @return
	 */
	public static <T> Map<T, Integer> count(Collection<T> keys) {
		Map<T, Integer> ret = null;

		if ((keys != null) && !keys.isEmpty()) {
			for (T key : keys) {
				if (key != null) {
					if (ret == null)
						ret = new HashMap<T, Integer>();

					Integer val = ret.get(key);

					if (val == null)
						val = 1;
					else
						val++;

					ret.put(key, val);
				}
			}
		}

		return ret;
	}

	/**
	 * @param random
	 * @param min
	 * @param max
	 * @return
	 */
	public static final double getRandom(java.util.Random random, double min, double max) {
		if (max == min) {
			return min;
		}
		if (min > max) {
			double temp = min;
			min = max;
			max = temp;
		}
		double delta = max - min;

		return min + Math.abs(random.nextDouble() * delta);

	}

	/**
	 * @param random
	 * @param min
	 * @param max
	 * @return
	 */
	public static final float getRandom(java.util.Random random, float min, float max) {
		if (max == min) {
			return min;
		}
		if (min > max) {
			max += min;
			min = max - min;
			max = max - min;
		}
		float delta = max - min;

		return min + Math.abs(random.nextFloat() * delta);
	}

	/**
	 * @param random
	 * @param min
	 * @param max
	 * @return
	 */
	public static final int getRandom(java.util.Random random, int min, int max) {
		if (max == min) {
			return min;
		}
		if (min > max) {
			int temp = min;
			min = max;
			max = temp;
		}
		int delta = max - min;

		return min + Math.abs(random.nextInt() % delta);
	}

	/**
	 * @param random
	 * @param min
	 * @param max
	 * @return
	 */
	public static final long getRandom(java.util.Random random, long min, long max) {
		if (max == min) {
			return min;
		}
		if (min > max) {
			long temp = min;
			min = max;
			max = temp;
		}
		long delta = max - min;

		return min + Math.abs(random.nextLong() % delta);
	}

	/**
	 * @param min
	 * @param max
	 * @return
	 */
	public static final int getRandomEqual(int min, int max) {
		return (max - min) == 0 ? max : (Math.abs(RANDOM.nextInt() % (max - min + 1)) + min);
	}

	/**
	 * @param min
	 * @param max
	 * @return
	 */
	public static final int getRandom(int min, int max) {
		return (max - min) == 0 ? max : (Math.abs(RANDOM.nextInt() % (max - min)) + min);
	}

	/**
	 * @param array
	 * @return
	 */
	public static final byte getRandom(byte[] array) {
		return array[Math.abs(RANDOM.nextInt() % array.length)];
	}

	/**
	 * @param array
	 * @return
	 */
	public static final short getRandom(short[] array) {
		return array[Math.abs(RANDOM.nextInt() % array.length)];
	}

	/**
	 * @param array
	 * @return
	 */
	public static final int getRandom(int[] array) {
		return array[Math.abs(RANDOM.nextInt() % array.length)];
	}

	/**
	 * @param array
	 * @return
	 */
	public static final long getRandom(long[] array) {
		return array[Math.abs(RANDOM.nextInt() % array.length)];
	}

	/**
	 * @param array
	 * @return
	 */
	public static final <T> T getRandom(T[] array) {
		if (array == null || array.length == 0)
			return null;

		return array[Math.abs(RANDOM.nextInt() % array.length)];
	}

	/**
	 * @return
	 */
	public static float getRandomFloat() {
		return RANDOM.nextFloat();
	}

	/**
	 * @return
	 */
	public static int getRandomInt(int _maxValue) {
		return RANDOM.nextInt(_maxValue);
	}

	/**
	 * @param array
	 * @return
	 */
	public static final <T> T getRandom(List<T> array) {
		if (array == null || array.size() == 0)
			return null;
		return array.get(Math.abs(RANDOM.nextInt() % array.size()));
	}

	@SuppressWarnings("unchecked")
	public static final <T> T getRandom(Collection<T> array) {
		if (array == null || array.size() == 0)
			return null;
		return (T) getRandom(array.toArray());
	}

	// @SuppressWarnings("unchecked")
	public static final <E extends Enum<E>> E getRandomEnum(Class<E> enums) {
		return (E) getRandom(EnumSet.allOf(enums));
	}

	/**
	 * 将数组内元素位置打乱
	 * 
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> T randomArray(T array) {
		if (array.getClass().isArray()) {
			int count = Array.getLength(array);
			for (int si = count - 1; si >= 0; --si) {
				int di = getRandom(0, count - 1);
				Object s = Array.get(array, si);
				Object d = Array.get(array, di);
				Array.set(array, di, s);
				Array.set(array, si, d);
			}
		}
		return array;
	}

	/**
	 * 将List内元素位置打乱
	 * 
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static List<Object> randomList(List<Object> list) {
		int count = list.size();
		for (int si = count - 1; si >= 0; --si) {
			int di = getRandom(0, count - 1);
			Object s = list.get(si);
			Object d = list.get(di);
			list.set(di, s);
			list.set(si, d);
		}
		return list;
	}

	/**
	 * @param e
	 * @return
	 */
	public static String getStackTrace(Throwable e) {
		StringBuilder message = new StringBuilder();
		for (StackTraceElement stack : e.getStackTrace()) {
			message.append("\t" + stack.toString() + "\n");
		}
		return message.toString();
	}

	/**
	 * 字符串数组，其中每个元素的环境变量的设置格式为 name=value；如果子进程应该继承当前进程的环境，或该参数为 null。
	 * 
	 * @return
	 */
	public static String[] getEnv() {
		Map<String, String> map = System.getenv();
		String[] ev = new String[map.size()];
		int index = 0;
		for (Entry<String, String> e : map.entrySet()) {
			ev[index++] = e.getKey() + "=" + e.getValue();
		}
		return ev;
	}

	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		// 由高位到低位
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	/**
	 * byte[]转int
	 * 
	 * @param bytes
	 * @return
	 */
	public static int byteArrayToInt(byte[] bytes) {
		int value = 0;
		// 由高位到低位
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (bytes[i] & 0x000000FF) << shift;// 往高位游
		}
		return value;
	}

	/**
	 * 注释：short到字节数组的转换！
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] shortToByte(short s) {
		byte[] shortBuf = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = (shortBuf.length - 1 - i) * 8;
			shortBuf[i] = (byte) ((s >>> offset) & 0xff);
		}
		return shortBuf;
	}

	/**
	 * 注释：字节数组到short的转换！
	 * 
	 * @param b
	 * @return
	 */
	public static short byteToShort(byte[] b) {
		return (short) ((b[0] << 8) + (b[1] & 0xFF));
	}

	/**
	 * 判断数组中是否存在value
	 * 
	 * @param array
	 * @param value
	 * @return
	 */
	public static boolean contains(int[] array, int value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value)
				return true;
		}
		return false;
	}

	/**
	 * 随机两个字母
	 * 
	 * @return
	 */
	public static String randomChar() {
		char rc = (char) (new Random().nextInt(26) + (int) 'a');
		char rc1 = (char) (new Random().nextInt(26) + (int) 'A');
		return rc + "" + rc1;
	}

	/**
	 * 判断是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static Map<String, Object> beanToMap(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>(0);
		try {
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
			PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				if (!"class".equals(name)) {
					params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}
}
