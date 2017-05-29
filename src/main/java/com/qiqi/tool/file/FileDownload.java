package com.qiqi.tool.file;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * 文件下载工具类
 * 
 * @author qc_zhong
 */
public class FileDownload {
	/**
	 * 下载
	 *
	 * @author qc_zhong
	 * @param response
	 * @param path 文件完整路径(包括文件名和扩展名)
	 * @param fileName 下载后看到的文件名
	 * @throws Exception
	 */
	public static void download(final HttpServletResponse response, String path, String fileName) throws Exception {
		byte[] data = FileUtil.getBytesByChannel(path);
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream;charset=UTF-8");
		OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
		outputStream.write(data);
		outputStream.flush();
		outputStream.close();
		response.flushBuffer();
	}
	
	/**
	 * 下载
	 *
	 * @author qc_zhong
	 * @param response
	 * @param is
	 * @param fileName
	 * @throws Exception
	 */
	public static void download(final HttpServletResponse response, InputStream is, String fileName) throws Exception {
		byte[] data = FileUtil.getBytes(is);
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream;charset=UTF-8");
		OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
		outputStream.write(data);
		outputStream.flush();
		outputStream.close();
		response.flushBuffer();
	}
	
	/**
	 * 下载
	 *
	 * @author qc_zhong
	 * @param response
	 * @param data
	 * @param fileName
	 * @throws Exception
	 */
	public static void download(final HttpServletResponse response, Workbook workbook, String fileName) throws Exception {
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		//response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream;charset=UTF-8");
		OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();
		response.flushBuffer();
	}
}