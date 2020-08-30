package com.atguigu.hdfs.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * 1. FileSystem: 文件系统的抽象基类
 * 			FileSystem的实现取决于fs.defaultFS的配置！
 * 				
 * 		有两种实现！
 *     LocalFileSystem： 本地文件系统   fs.defaultFS=file:///
 *     DistributedFileSystem： 分布式文件系统  fs.defaultFS=hdfs://xxx:9000
 *     
 *     声明用户身份：
 *     	 FileSystem fs = FileSystem.get(new URI("hdfs://hadoop101:9000"), conf, "atguigu");
 *     
 *  2. Configuration : 功能是读取配置文件中的参数
 *  		
		Configuration在读取配置文件的参数时，根据文件名，从类路径按照顺序读取配置文件！
				先读取 xxx-default.xml，再读取xxx-site.xml
				
		Configuration类一加载，就会默认读取8个配置文件！
		将8个配置文件中所有属性，读取到一个Map集合中！
		
		也提供了set(name,value)，来手动设置用户自定义的参数！
		
	3. FileStatus
			代表一个文件的状态(文件的属性信息)
			
	4. offset和length
			offset是偏移量： 指块在文件中的起始位置
			length是长度，指块大小
			
			sts.zip 390M
								length    offset
			blk1:   0-128M      128M		0
			blk2:    128M-256M  128M        128M
			...
			blk4:    384M-390M  6M          384M
			
	5. LocatedFileStatus
			LocatedFileStatus是FileStatus的子类，除了文件的属性，还有块的位置信息！
 * 			
 */
public class TestHDFS {
	
	private FileSystem fs;
	
	private Configuration conf = new Configuration();
	
	@Before
	public void init() throws IOException, URISyntaxException {
		System.setProperty("hadoop.home.dir", "E:\\software\\java\\hadoop\\hadoop-2.7.2");
		//创建一个客户端对象
		 fs=FileSystem.get(new URI("hdfs://hadoop201:9000"),conf);
		
	}
	
	@After
	public void close() throws IOException {
		
		if (fs !=null) {
			fs.close();
		}
		
	}

	//  hadoop fs(运行一个通用的用户客户端)   -mkdir /xxx
	//  创建一个客户端对象 ，调用创建目录的方法，路径作为方法的参数掺入
	@Test
	public void testMkdir() throws IOException, InterruptedException, URISyntaxException {
		
		fs.mkdirs(new Path("/eclipse2"));
	}
	
	// 上传文件： hadoop fs -put 本地文件  hdfs
	@Test
	public void testUpload() throws Exception {
		
		fs.copyFromLocalFile(false, true, new Path("e:/sts.zip"), new Path("/"));
		
	}
	
	// 下载文件：  hadoop fs -get hdfs  本地路径
	@Test
	public void testDownload() throws Exception {
		
		fs.copyToLocalFile(false, new Path("/wcinput"), new Path("e:/"), true);
		
	}
	
	// 删除文件：  hadoop fs -rm -r -f  路径
	@Test
	public void testDelete() throws Exception {
		
		fs.delete(new Path("/eclipse"), true);
		
		
	}
	
	// 重命名：  hadoop fs -mv  源文件   目标文件
	@Test
	public void testRename() throws Exception {
		
		fs.rename(new Path("/eclipse1"), new Path("/eclipsedir"));
		
	}
	
	// 判断当前路径是否存在
	@Test
	public void testIfPathExsits() throws Exception {
		
		System.out.println(fs.exists(new Path("/eclipsedir1")));
		
	}
	
	// 判断当前路径是目录还是文件
	@Test
	public void testFileIsDir() throws Exception {
		
		//Path path = new Path("/eclipsedir");
		Path path = new Path("/wcoutput1");
		
		// 不建议使用此方法，建议好似用Instead reuse the FileStatus returned 
		//by getFileStatus() or listStatus() methods.

	/*	System.out.println(fs.isDirectory(path));
		System.out.println(fs.isFile(path));*/
		
		//FileStatus fileStatus = fs.getFileStatus(path);
		
		FileStatus[] listStatus = fs.listStatus(path);
		
		for (FileStatus fileStatus : listStatus) {
			
			//获取文件名 Path是完整的路径 协议+文件名
			Path filePath = fileStatus.getPath();
			System.out.println(filePath.getName()+"是否是目录："+fileStatus.isDirectory());
			System.out.println(filePath.getName()+"是否是文件："+fileStatus.isFile());
		}
			
	}
	
	// 获取到文件的块信息
	@Test
	public void testGetBlockInfomation() throws Exception {
		
		Path path = new Path("/sts.zip");
		
		RemoteIterator<LocatedFileStatus> status = fs.listLocatedStatus(path);
		
		while(status.hasNext()) {
			
			LocatedFileStatus locatedFileStatus = status.next();
			
			System.out.println("Ownner:"+locatedFileStatus.getOwner());
			System.out.println("Group:"+locatedFileStatus.getGroup());
			
			//---------------块的位置信息--------------------
			BlockLocation[] blockLocations = locatedFileStatus.getBlockLocations();
			
			for (BlockLocation blockLocation : blockLocations) {
				
				System.out.println(blockLocation);
				
				System.out.println("------------------------");
				
			}
			
		}
		
	}
	

}
