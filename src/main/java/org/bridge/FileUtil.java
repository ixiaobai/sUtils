package org.bridge;
import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.io.IOUtils;


public class FileUtil{
	/**处理下载指定文件
	 * @param request
	 * @param response
	 * @param filePath 文件全路径
	 * @param fileName 文件保存名
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void DownLoad(HttpServletRequest request, HttpServletResponse response,String filePath,String fileName) throws ServletException, IOException{
		File file = new File(new String( filePath.getBytes()));
		String agent = request.getHeader("USER-AGENT");
		if(StringUtil.isIE(agent)){
			String encodeName= URLEncoder.encode(fileName, "UTF-8");
			encodeName=encodeName.replace("+", "%20");//处理空格，空格会被编码成+，转化为%20
			if(-1 != agent.indexOf("MSIE 6.0") && encodeName.length() > 150){//IE6下最长支持150个字符左右，一个汉字占9位编码，最多17个汉字
				//这个方式在IE8下 字符集在gb2312内的是可以的，IE6又不支持，那IE6下面超过15个汉字就只能被自动截断了...
				//fileName=new String(fileName.getBytes("gb2312"),"ISO8859-1");//如果文件名里面包含了非gb2312编码的字，比如繁体字，就不行了呀
				fileName=encodeName;//如有解决办法，更改此行
			}else{
				fileName=encodeName;
			}				
		}else{
			fileName=new String(fileName.getBytes("UTF-8"),"ISO8859-1");
		}		
		/* 如果文件存在 */ 
		if (file.exists()) {
			// 清空response   
			response.reset();
			// 设置response的Header   
			response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName+"\"");//火狐读取文件名时，遇到空格就只取空格前面的，把文件名用双引引起来就好了
			response.addHeader("Content-Length", "" + file.length());   
			response.setContentType("application/octet-stream");   
			  
			//打开文件输入流 和 servlet输出流   
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());   
			InputStream fis = new BufferedInputStream(new FileInputStream(file));   
			  
			//通过ioutil 对接输入输出流，实现文件下载   
			IOUtils.copy(fis, toClient);   
			toClient.flush();
			//关闭流   
			fis.close();   
			toClient.close();  
		}else{
			throw new IOException("文件未找到！");
		}
	}
	/**
	* <p>方法名称: copy|描述: 复制（所有文件及子目录）</p>
	* @param sourcePath 源目录路径
	* @param destPath 目的目录路径
	* @throws IOException
	*/
	public static void copy(String sourcePath,String destPath) throws IOException{
		File file=new File(sourcePath);
		File[] files = file.listFiles();
		File destPathDir=new File(destPath);
		if(!destPathDir.exists())destPathDir.mkdirs();
		for (int i = 0; i < files.length; i++) {
			if(files[i].isFile())
				copyFile(files[i],destPath+File.separator+files[i].getName());			
			else if(files[i].isDirectory()){				
				File destDir=new File(destPath+File.separator+files[i].getName());
				if(!destDir.exists())destDir.mkdirs();
				copy(files[i].getPath(),destPath+File.separator+files[i].getName());
			}
		}
	}
	/**
	* <p>方法名称: copyFile|描述: 复制文件</p>
	* @param sSourceFile 源文件路径
	* @param sDestFile 目的文件路径
	* @throws IOException
	*/
	public static void copyFile(String sSourceFile,String sDestFile) throws IOException{
		copyFile(new File(sSourceFile),new File(sDestFile));
	}
	/**
	* <p>方法名称: copyFile|描述: 复制文件</p>
	* @param sourceFile 源文件对象
	* @param sDestFile 目的文件路径
	* @throws IOException
	*/
	public static void copyFile(File sourceFile,String sDestFile) throws IOException{
		copyFile(sourceFile,new File(sDestFile));
	}
	/**
	* <p>方法名称: copyFile|描述: 复制文件</p>
	* @param sourceFile 源文件对象
	* @param destFile 目的文件对象
	* @throws IOException
	*/
	public static void copyFile(File sourceFile,File destFile) throws IOException{
		FileInputStream fi=new FileInputStream(sourceFile);
		FileChannel fci = fi.getChannel();
		FileOutputStream fo=new FileOutputStream(destFile);
		FileChannel fco=fo.getChannel();
		fci.transferTo(0, fci.size(), fco);
		System.out.println("\""+sourceFile.getPath()+"\"-->-copy to-->\""+destFile.getPath()+"\"");
	}
	public static void main(String[] args) throws IOException{
//		File file=new File("D:\\test");
//		System.out.println(file.getPath());
//		System.out.println(file.getName());
		copy("F:\\testS","F:\\testD");
	}
}