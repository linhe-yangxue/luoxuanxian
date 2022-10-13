package com.ssmCore.tool.zip7;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.ssmCore.tool.zip7.Compression.LZMA.Decoder;
import com.ssmCore.tool.zip7.Compression.LZMA.Encoder;

public class LzmaRar {

	/**
	 * 文件压缩
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void CompressFileLZMA(String in, String out) throws IOException
	{	
		Encoder encoder = new Encoder();
		File inFile = new File(in);
		File outFile = new File(out);
		BufferedInputStream inStream  = new BufferedInputStream(new FileInputStream(inFile));
		BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(outFile));
		encoder.WriteCoderProperties(outStream);
		long fileSize = inFile.length();		
		for (int i = 0; i < 8; i++)
			outStream.write((int)(fileSize >>> (8 * i)) & 0xFF);
		
		encoder.Code(inStream, outStream, -1, -1, null);
		outStream.flush();
		outStream.close();
		inStream.close();
	}
	
	/**
	 * 文件解压
	 * @param in
	 * @param out
	 * @throws Exception 
	 */
	public static byte[] CompressStrDecLZMA(InputStream in) throws Exception
	{	
		Decoder decoder = new Decoder();
		BufferedInputStream inStream  = new BufferedInputStream(in);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try{
			int propertiesSize = 5;
			byte[] properties = new byte[propertiesSize];
			if (inStream.read(properties, 0, propertiesSize) != propertiesSize)
				throw new Exception("input .lzma file is too short");
			if (!decoder.SetDecoderProperties(properties))
				throw new Exception("Incorrect stream properties");
			long outSize = 0;
			for (int i = 0; i < 8; i++)
			{
				int v = inStream.read();
				if (v < 0)
					throw new Exception("Can't read stream size");
				outSize |= ((long)v) << (8 * i);
			}
			if (!decoder.Code(inStream, outStream, outSize))
				throw new Exception("Error in data stream");
			
			outStream.flush();
			return outStream.toByteArray();
		}catch(Exception e){
			return null;
		}finally{
			outStream.close();
			inStream.close();
		}
	}
	
	/**
	 * 字符压缩
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static byte[] CompressStringLZMA(String in) throws IOException
	{
		BufferedInputStream inStream  = new BufferedInputStream(IOUtils.toInputStream(in));
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try{
			Encoder encoder = new Encoder();
			encoder.WriteCoderProperties(outStream);
			
			long fileSize = inStream.available();
			for (int i = 0; i < 8; i++)
				outStream.write((int)(fileSize >>> (8 * i)) & 0xFF);
			encoder.Code(inStream, outStream, -1, -1, null);
			outStream.flush();
			return outStream.toByteArray();
		}catch(Exception e){
			return null;
		}finally{
			outStream.close();
			inStream.close();
		}
	}
	
}
