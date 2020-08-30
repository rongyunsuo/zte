package com.atguigu.hdfs.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * 1. 上传文件时，只上传这个文件的一部分
 * 
 * 2. 下载文件时，如何只下载这个文件的某一个块？ 
 * 			或只下载文件的某一部分？
 */
public class TestCustomUploadAndDownload {

   private FileSystem fs;
   private FileSystem localFs;
	
	private Configuration conf = new Configuration();
	
	@Before
	public void init() throws IOException, URISyntaxException {
		
		//创建一个客户端对象
		 fs=FileSystem.get(new URI("hdfs://hadoop101:9000"),conf);
		 
		 localFs=FileSystem.get(new Configuration());
		
	}
	
	@After
	public void close() throws IOException {
		
		if (fs !=null) {
			fs.close();
		}
		
	}
	
	// 只上传文件的前10M
	/*
	 * 官方的实现
	 * InputStream in=null;
      OutputStream out = null;
      try {
        in = srcFS.open(src);
        out = dstFS.create(dst, overwrite);
        IOUtils.copyBytes(in, out, conf, true);
      } catch (IOException e) {
        IOUtils.closeStream(out);
        IOUtils.closeStream(in);
        throw e;
      }
	 */
	
	@Test
	public void testCustomUpload() throws Exception {
		
		//提供两个Path，和两个FileSystem
		Path src=new Path("e:/悲惨世界(英文版).txt");
		Path dest=new Path("/悲惨世界(英文版)10M.txt");
		
		// 使用本地文件系统中获取的输入流读取本地文件
		FSDataInputStream is = localFs.open(src);
		
		// 使用HDFS的分布式文件系统中获取的输出流，向dest路径写入数据
		FSDataOutputStream os = fs.create(dest, true);
		
		// 1k
		byte [] buffer=new byte[1024];
		
		// 流中数据的拷贝
		for (int i = 0; i < 1024 * 10; i++) {
			
			is.read(buffer);
			os.write(buffer);
			
		}
		
		//关流
		 IOUtils.closeStream(is);
	     IOUtils.closeStream(os);
	}
	
	
	@Test
	public void testFirstBlock() throws Exception {
		//提供两个Path，和两个FileSystem
		Path src=new Path("/sts.zip");
		Path dest=new Path("e:/firstblock");
		
		// 使用HDFS的分布式文件系统中获取的输入流，读取HDFS上指定路径的数据
		FSDataInputStream is = fs.open(src);
		// 使用本地文件系统中获取的输出流写入本地文件
		FSDataOutputStream os = localFs.create(dest, true);
		
		// 1k
		byte [] buffer=new byte[1024];
				
		// 流中数据的拷贝
		for (int i = 0; i < 1024 * 128; i++) {
					
			is.read(buffer);
			os.write(buffer);
					
		}
				
		//关流
		IOUtils.closeStream(is);
		IOUtils.closeStream(os);
				
		
		
	}
	
	@Test
	public void testSecondBlock() throws Exception {
		//提供两个Path，和两个FileSystem
		Path src=new Path("/sts.zip");
		//Path dest=new Path("e:/secondblock");
		Path dest=new Path("e:/thirdblock");
		
		// 使用HDFS的分布式文件系统中获取的输入流，读取HDFS上指定路径的数据
		FSDataInputStream is = fs.open(src);
		// 使用本地文件系统中获取的输出流写入本地文件
		FSDataOutputStream os = localFs.create(dest, true);
		
		//定位到流的指定位置
		is.seek(1024*1024*128*2);
		
		// 1k
		byte [] buffer=new byte[1024];
				
		// 流中数据的拷贝
		for (int i = 0; i < 1024 * 128; i++) {
					
			is.read(buffer);
			os.write(buffer);
					
		}
				
		//关流
		IOUtils.closeStream(is);
		IOUtils.closeStream(os);
				
		
		
	}
	
	@Test
	public void testFinalBlock() throws Exception {
		//提供两个Path，和两个FileSystem
		Path src=new Path("/sts.zip");
		//Path dest=new Path("e:/secondblock");
		Path dest=new Path("e:/fourthblock");
		
		// 使用HDFS的分布式文件系统中获取的输入流，读取HDFS上指定路径的数据
		FSDataInputStream is = fs.open(src);
		// 使用本地文件系统中获取的输出流写入本地文件
		FSDataOutputStream os = localFs.create(dest, true);
		
		//定位到流的指定位置
		is.seek(1024*1024*128*3);
		
		//buffSize 默认不能超过4096
		IOUtils.copyBytes(is, os, 4096, true);
				
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
