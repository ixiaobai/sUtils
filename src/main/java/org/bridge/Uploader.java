package org.bridge;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sun.misc.BASE64Decoder;

/**
 * UEditor文件上传辅助类 * 
 * Edit by zlw 2014.4.8
 */
public class Uploader {
	// 文件大小常量, 单位kb
	private static final int MAX_SIZE = 1;
	// 输出文件地址
	private String url = "";
	// 上传文件名
	private String fileName = "";
	// 状态
	private String state = "";
	// 文件类型
	private String type = "";
	// 原始文件名
	private String originalName = "";
	// 文件大小
	private String size = "";

	private HttpServletRequest request = null;
	private String title = "";

	// 保存路径
	private String savePath = "upload";
	// 文件允许格式
	private String[] allowFiles = { ".rar", ".doc", ".docx", ".zip", ".pdf",
			".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp" };
	// 文件大小限制，单位Byte
	private long maxSize = 0;

	private HashMap<String, String> errorInfo = new HashMap<String, String>();
	private Map<String, String> params = null;
	// 上传的文件数据
	private InputStream inputStream = null;

	public Uploader(HttpServletRequest request) {
		this.request = request;
		this.params = new HashMap<String, String>();
		this.setMaxSize(Uploader.MAX_SIZE);
		HashMap<String, String> tmp = this.errorInfo;
		tmp.put("SUCCESS", "SUCCESS"); // 默认成功
		// 未包含文件上传域
		tmp.put("NOFILE", "\\u672a\\u5305\\u542b\\u6587\\u4ef6\\u4e0a\\u4f20\\u57df");
		// 不允许的文件格式
		tmp.put("TYPE", "\\u4e0d\\u5141\\u8bb8\\u7684\\u6587\\u4ef6\\u683c\\u5f0f");
		// 文件大小超出限制
		tmp.put("SIZE", "\\u6587\\u4ef6\\u5927\\u5c0f\\u8d85\\u51fa\\u9650\\u5236");
		// 请求类型错误
		tmp.put("ENTYPE", "\\u8bf7\\u6c42\\u7c7b\\u578b\\u9519\\u8bef");
		// 上传请求异常
		tmp.put("REQUEST", "\\u4e0a\\u4f20\\u8bf7\\u6c42\\u5f02\\u5e38");
		// 未找到上传文件
		tmp.put("FILE", "\\u672a\\u627e\\u5230\\u4e0a\\u4f20\\u6587\\u4ef6");
		// IO异常
		tmp.put("IO", "IO\\u5f02\\u5e38");
		// 目录创建失败
		tmp.put("DIR", "\\u76ee\\u5f55\\u521b\\u5efa\\u5931\\u8d25");
		// 未知错误
		tmp.put("UNKNOWN", "\\u672a\\u77e5\\u9519\\u8bef");		
	}

	public void upload() throws Exception {
		try {
			/**经过mvc地址上传时调用***********************/
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) this.request;			
			if(!parseParams_SpringMVC(multipartRequest)){
				return;
			}			
		} catch (Exception e1) {
			/**不经过mvc地址上传时调用***********************/
			if(!this.parseParams()){
				return;
			}
		}
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(this.request);
		if (!isMultipart) {
			this.state = this.errorInfo.get("NOFILE");
			return;
		}
		if (this.inputStream == null) {
			this.state = this.errorInfo.get("FILE");
			return;
		}
		// 存储title
		this.title = this.getParameter("pictitle");

		try {
			String savePath = this.getFolder(this.savePath);

			if (!this.checkFileType(this.originalName)) {
				this.state = this.errorInfo.get("TYPE");
				return;
			}

			this.fileName = this.getName(this.originalName);
			this.type = this.getFileExt(this.fileName);
			this.url = savePath + "/" + this.fileName;

			FileOutputStream fos = new FileOutputStream(this.getPhysicalPath(this.url));
			BufferedInputStream bis = new BufferedInputStream(this.inputStream);
			byte[] buff = new byte[128];
			int count = -1;
			while ((count = bis.read(buff)) != -1) {
				fos.write(buff, 0, count);
			}
			bis.close();
			fos.close();
			this.state = this.errorInfo.get("SUCCESS");
			this.url ="/upload/"+this.url;//返回域名以后目录即可
		} catch (Exception e) {
			e.printStackTrace();
			this.state = this.errorInfo.get("IO");
		}
	}

	/**
	 * 接受并保存以base64格式上传的文件
	 * 
	 * @param fieldName
	 */
	public void uploadBase64(String fieldName) {
		String savePath = this.getFolder(this.savePath);
		String base64Data = this.request.getParameter(fieldName);
		this.fileName = this.getName("test.png");
		this.url = savePath + "/" + this.fileName;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			File outFile = new File(this.getPhysicalPath(this.url));
			OutputStream ro = new FileOutputStream(outFile);
			byte[] b = decoder.decodeBuffer(base64Data);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			ro.write(b);
			ro.flush();
			ro.close();
			this.state = this.errorInfo.get("SUCCESS");
		} catch (Exception e) {
			this.state = this.errorInfo.get("IO");
		}
	}

	public String getParameter(String name) {
		return this.params.get(name);
	}
	/**
	 * 文件类型判断
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean checkFileType(String fileName) {
		Iterator<String> type = Arrays.asList(this.allowFiles).iterator();
		while (type.hasNext()) {
			String ext = type.next();
			if (fileName.toLowerCase().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @return string
	 */
	private String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	* <p>方法名称: parseParams|描述: 普通请求上传用这个</p>
	* @return
	*/
	private boolean parseParams(){
		try {
			DiskFileItemFactory dff = new DiskFileItemFactory();		
			ServletFileUpload sfu = new ServletFileUpload(dff);
			sfu.setSizeMax(this.maxSize);
			sfu.setHeaderEncoding("UTF-8");
			FileItemIterator fii = sfu.getItemIterator(this.request);
			while (fii.hasNext()) {
				FileItemStream item = fii.next();
				// 普通参数存储
				if (item.isFormField()) {
					this.params.put(item.getFieldName(), this.getParameterValue(item.openStream()));
				} else {
					// 只保留一个
					if (this.inputStream == null) {
						this.inputStream = item.openStream();
						this.originalName = item.getName();
						return true;
					}
				}
			}
			return true;
		}catch(SizeLimitExceededException e){
			this.state = this.errorInfo.get("SIZE");
			return false;
		}
		catch (Exception e) {
			this.state = this.errorInfo.get("UNKNOWN");
			return false;
		}
	}
	/**
	* <p>方法名称: parseParams_SpringMVC|描述: springMVC下上传文件用这个（请求提交到mapping地址时）</p>
	* @param multipartRequest
	* @return
	*/
	private boolean parseParams_SpringMVC(MultipartHttpServletRequest multipartRequest) {
		Iterator<String> names = multipartRequest.getFileNames();
		while(names.hasNext()){
			MultipartFile file = multipartRequest.getFile(names.next());
			if(file.getSize() > this.maxSize){
				this.state = this.errorInfo.get("SIZE");
				return false;
			}
			if (this.inputStream == null) {
				try {
					this.inputStream = file.getInputStream();
				} catch (IOException e) {
					this.state = this.errorInfo.get("UNKNOWN");
					return false;
				}
				this.originalName = file.getOriginalFilename();
			}
		}
		Enumeration<String> pNames = multipartRequest.getParameterNames();
		while(pNames.hasMoreElements()){
			String filedName= pNames.nextElement();			
			this.params.put(filedName, multipartRequest.getParameter(filedName));
		}
		return true;
	}

	/**
	 * 依据原始文件名生成新文件名
	 * 
	 * @return
	 */
	private String getName(String fileName) {
		Random random = new Random();
		return this.fileName = "" + random.nextInt(10000) + System.currentTimeMillis() + this.getFileExt(fileName);
	}

	/**
	 * 根据字符串创建本地目录 并按照日期建立子目录返回
	 * 
	 * @param path
	 * @return
	 */
	private String getFolder(String path) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		path += "/" + formater.format(new Date());
		File dir = new File(this.getPhysicalPath(path));
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				this.state = this.errorInfo.get("DIR");
				return "";
			}
		}
		return path;
	}

	/**
	 * 根据传入的虚拟路径获取物理路径
	 * @param path
	 * @return
	 */
	private String getPhysicalPath(String path) {
//		return PropertyUtil.getProperty("web_address")+File.separator+"upload"+File.separator+path;
		return "";
	}

	/**
	 * 从输入流中获取字符串数据
	 * 
	 * @param in
	 * 给定的输入流， 结果字符串将从该流中读取
	 * @return 从流中读取到的字符串
	 */
	private String getParameterValue(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String result = "";
		String tmpString = null;
		try {
			while ((tmpString = reader.readLine()) != null) {
				result += tmpString;
			}

		} catch (Exception e) {
			// do nothing
		}
		return result;
	}
	private byte[] getFileOutputStream(InputStream in) {

		try {
			return IOUtils.toByteArray(in);
		} catch (IOException e) {
			return null;
		}
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public void setAllowFiles(String[] allowFiles) {
		this.allowFiles = allowFiles;
	}

	public void setMaxSize(long size) {
		this.maxSize = size * 1024;
	}

	public String getSize() {
		return this.size;
	}

	public String getUrl() {
		return this.url;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getState() {
		return this.state;
	}

	public String getTitle() {
		return this.title;
	}

	public String getType() {
		return this.type;
	}

	public String getOriginalName() {
		return this.originalName;
	}
}