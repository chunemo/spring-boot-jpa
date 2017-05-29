package com.qiqi.tool.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * 
 * @author qc_zhong
 */
public class FileUtil {
	/**
	 * 创建目录
	 * 
	 * @param dir 目录
	 * @return 目录创建成功返回true，否则返回false
	 */
	public static boolean createDir(String dir) {
		File file = new File(dir);
		if (file.exists()) {
			return false;
		}
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		// 创建单个目录
		if (file.mkdirs()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param path 文件路径及名称 如c:/fqf.txt
	 */
	public static void delFile(String path) {
		try {
			File file = new File(path);
			file.delete();
		} catch (Exception e) {
			System.out.println("删除文件操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 读取到字节数组
	 * 
	 * @param path 路径
	 * @throws IOException
	 */
	public static byte[] getBytes(String path) throws IOException {
		File file = new File(path);
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			System.out.println("file too big...");
			return null;
		}
		
		FileInputStream fi = null;
		BufferedInputStream bi = null;
		try {
			fi = new FileInputStream(file);
			bi = new BufferedInputStream(fi);
			byte[] buffer = new byte[(int) length];
			int offset = 0;
			int numRead = 0;
			while (offset < buffer.length && (numRead = bi.read(buffer, offset, buffer.length - offset)) >= 0) {
				offset += numRead;
			}
			// 确保所有数据均被读取
			if (offset != buffer.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}
			
			return buffer;
		} catch (Exception e) {
			throw e;
		} finally {
			if (bi != null) {
				try {
					bi.close();
				} catch (Exception e) {
				}
			}
			if (fi != null) {
				try {
					fi.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	/**
	 * 读取到字节数组
	 *
	 * @author qc_zhong
	 * @param is
	 * @return
	 */
	public static byte[] getBytes(InputStream is) {
		List<byte[]> list = new ArrayList<byte[]>();
		byte[] bs = new byte[] {};
		BufferedInputStream bi = null;

		try {
			bi = new BufferedInputStream(is);
			int len = 0;
			int size = 1024;
			byte[] b = new byte[size];
			// read
			while ((len = bi.read(b)) != -1) {
				byte[] tmp = new byte[len];
				System.arraycopy(b, 0, tmp, 0, len);
				list.add(tmp);
			}
			// concat
			for (int i = 0; i < list.size(); i++) {
				// TODO coty
				// bs = Bytes.concat(bs, list.get(i));
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			if (bi != null) {
				try {
					bi.close();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}

		return bs;
	}

	/**
	 * 读取到字节数组
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesByBos(String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException(path);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 读取到字节数组
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesByChannel(String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException(path);
		}

		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(file);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
				// do nothing
				// System.out.println("reading");
			}
			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesByMbb(String path) throws IOException {
		FileChannel fc = null;
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(path, "r");
			fc = rf.getChannel();
			MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size()).load();
			// System.out.println(byteBuffer.isLoaded());
			byte[] result = new byte[(int) fc.size()];
			if (byteBuffer.remaining() > 0) {
				// System.out.println("remain");
				byteBuffer.get(result, 0, byteBuffer.remaining());
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rf != null) {
				try {
					rf.close();
				} catch (IOException e) {
				}
			}
			if (fc != null) {
				try {
					fc.close();
				} catch (Exception e) {
				}
			}
		}
	}
}