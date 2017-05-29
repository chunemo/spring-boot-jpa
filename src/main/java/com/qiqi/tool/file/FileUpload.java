package com.qiqi.tool.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传工具类
 * 
 * @author qc_zhong
 */
public class FileUpload {
	/**
	 * 文件上传
	 * 
	 * @param file 文件对象
	 * @param path 上传路径
	 * @param fileName 文件名
	 * @return 文件名
	 */
	public static String upload(MultipartFile file, String path, String fileName){
		InputStream in = null;
		try {
			in = file.getInputStream();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return upload(in, file.getOriginalFilename(), path, fileName);
	}
	
	/**
	 * 文件上传
	 *
	 * @author qc_zhong
	 * @param in
	 * @param originalFilename
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static String upload(InputStream in, String originalFilename, String path, String fileName) {
		String extName = getExtName(originalFilename);
		if (extName == null) {
			return null;
		}
		
		String realName = (fileName + extName).replaceAll("-", "");
		try {
			copyFile(in, path, realName);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return realName;
	}
	
	/**
	 * 文件上传
	 *
	 * @author qc_zhong
	 * @param in
	 * @param realName
	 * @param path
	 */
	public static void upload(InputStream in, String realName, String path) {
		try {
			copyFile(in, path, realName);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * 获取扩展名
	 *
	 * @author qc_zhong
	 * @return
	 */
	public static String getExtName(String name) {
		String extName = null; // 扩展名
		if (name != null) {
			int index = name.lastIndexOf(".");
			if (index >= 0) {
				extName = name.substring(index);
			}
		}
		return extName;
	}
	
	/**
	 * 写文件到path目录
	 * 
	 * @param in
	 * @param fileName
	 * @throws IOException
	 */
	private static void copyFile(InputStream in, String path, String realName) throws IOException {
		File file = new File(path, realName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		FileUtils.copyInputStreamToFile(in, file);
	}
}