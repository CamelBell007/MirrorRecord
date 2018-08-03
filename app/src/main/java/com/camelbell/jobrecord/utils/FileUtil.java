package com.camelbell.jobrecord.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Environment;
import android.os.StatFs;
import android.widget.TextView;
import android.widget.Toast;

public class FileUtil {

	/**
	 * 将源文件的数据写入到目标文件中， 不会检查源文件是否存在， 若目标文件存在则直接写入， 否则创建目标文件后再进行写入。
	 * 
	 * @param srcPath
	 * @param desPath
	 */
	public static void copyFile(String srcPath, String desPath) {
		try {
			FileInputStream in = new FileInputStream(srcPath);
			FileOutputStream out = new FileOutputStream(desPath);
			byte[] bt = new byte[1024];
			int count;
			while ((count = in.read(bt)) > 0) {
				out.write(bt, 0, count);
			}
			in.close();
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 复制一个文件
	 * 
	 * @param src
	 * @param tar
	 * @throws Exception
	 */
	public static void copyFile(File src, File tar) throws Exception {

		if (src.isFile()) {
			InputStream is = new FileInputStream(src);
			OutputStream op = new FileOutputStream(tar);
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(op);
			byte[] bt = new byte[4 * 1024];
			int len = bis.read(bt);
			while (len != -1) {
				bos.write(bt, 0, len);
				len = bis.read(bt);
			}
			bis.close();
			bos.close();
			src.delete();
		}
		/*
		 * if (src.isDirectory()) { File[] f = src.listFiles(); tar.mkdir(); for
		 * (int i = 0; i < f.length; i++) { copyFile(f[i].getAbsoluteFile(), new
		 * File(tar.getAbsoluteFile() + File.separator + f[i].getName())); } }
		 */

	}

	final static Pattern pattern = Pattern.compile("\\S*[?]\\S*");

	/**
	 * 获取链接的后缀名
	 * 
	 * @return
	 */
	public static String parseSuffix(String url) {

		Matcher matcher = pattern.matcher(url);

		String[] spUrl = url.toString().split("/");
		int len = spUrl.length;
		String endUrl = spUrl[len - 1];

		if (matcher.find()) {
			String[] spEndUrl = endUrl.split("\\?");
			return spEndUrl[0].split("\\.")[1];
		}
		return endUrl.split("\\.")[1];
	}

	/**
	 * 检查SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	/**
	 * 复制文件，若文件已存在则不进行替换。
	 * 
	 * @param srcPath
	 * @param desPath
	 * @throws Exception
	 */
	public static void copyAndNotReplaceFile(String srcPath, String desPath)
			throws Exception {
		srcPath = separatorReplace(srcPath);
		desPath = separatorReplace(desPath);
		File srcFile = new File(srcPath);
		File desFile = new File(desPath);
		if (!srcFile.isFile()) {
			throw new Exception("source file not found!");
		}
		if (desFile.isFile()) {
			return;
		}
		copyFile(srcPath, desPath);
	}

	/**
	 * 获取SD卡可用空间
	 * 
	 * @return
	 */
	public static long getAvailaleSize() {
		File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize / 1024 / 1024;
		// availableBlocks * blockSize 单位为字节byte
		// (availableBlocks * blockSize)/1024 KIB 单位
		// (availableBlocks * blockSize)/1024 /1024 MIB单位
	}

	/**
	 * 复制文件，若文件存在则替换该文件。
	 * 
	 * @param srcPath
	 * @param desPath
	 * @throws Exception
	 */
	public static void copyAndReplaceFile(String srcPath, String desPath)
			throws Exception {
		srcPath = separatorReplace(srcPath);
		desPath = separatorReplace(desPath);
		File srcFile = new File(srcPath);
		if (!srcFile.isFile()) {
			throw new Exception("source file not found!");
		}
		copyFile(srcPath, desPath);
	}

	/**
	 * 移动文件，若文件存在则替换该文件。
	 * 
	 * @param srcPath
	 * @param desPath
	 * @throws Exception
	 */
	public static void moveAndReplaceFile(String srcPath, String desPath)
			throws Exception {
		srcPath = separatorReplace(srcPath);
		desPath = separatorReplace(desPath);
		copyAndReplaceFile(srcPath, desPath);
		deleteFile(srcPath);
	}

	/**
	 * 移动文件，若文件存在则不进行替换。
	 * 
	 * @param srcPath
	 * @param desPath
	 * @throws Exception
	 */
	public static void moveAndNotReplaceFile(String srcPath, String desPath)
			throws Exception {
		srcPath = separatorReplace(srcPath);
		desPath = separatorReplace(desPath);
		copyAndNotReplaceFile(srcPath, desPath);
		deleteFile(srcPath);
	}

	/**
	 * 复制并合并文件夹， 不会替换目标文件夹中已经存在的文件或文件夹。
	 * 
	 * @param srcPath
	 * @param desPath
	 * @throws Exception
	 */
	public static void copyAndMergerFolder(String srcPath, String desPath)
			throws Exception {
		srcPath = separatorReplace(srcPath);
		desPath = separatorReplace(desPath);
		File folder = getFolder(srcPath);
		createFolder(desPath);
		File[] files = folder.listFiles();
		for (File file : files) {
			String src = file.getAbsolutePath();
			String des = desPath + File.separator + file.getName();
			if (file.isFile()) {
				copyAndNotReplaceFile(src, des);
			} else if (file.isDirectory()) {
				copyAndMergerFolder(src, des);
			}
		}
	}

	/**
	 * 复制并替换文件夹， 将目标文件夹完全替换成源文件夹， 目标文件夹原有的资料会丢失。
	 * 
	 * @param srcPath
	 * @param desPath
	 * @throws Exception
	 */
	public static void copyAndReplaceFolder(String srcPath, String desPath)
			throws Exception {
		srcPath = separatorReplace(srcPath);
		desPath = separatorReplace(desPath);
		File folder = getFolder(srcPath);
		createNewFolder(desPath);
		File[] files = folder.listFiles();
		for (File file : files) {
			String src = file.getAbsolutePath();
			String des = desPath + File.separator + file.getName();
			if (file.isFile()) {
				copyAndReplaceFile(src, des);
			} else if (file.isDirectory()) {
				copyAndReplaceFolder(src, des);
			}
		}
	}

	/**
	 * 合并文件夹后，将源文件夹删除。
	 * 
	 * @param srcPath
	 * @param desPath
	 * @throws Exception
	 */
	public static void moveAndMergerFolder(String srcPath, String desPath)
			throws Exception {
		srcPath = separatorReplace(srcPath);
		desPath = separatorReplace(desPath);
		copyAndMergerFolder(srcPath, desPath);
		deleteFolder(srcPath);
	}

	/**
	 * 替换文件夹后，将源文件夹删除。
	 * 
	 * @param srcPath
	 * @param desPath
	 * @throws Exception
	 */
	public static void moveAndReplaceFolder(String srcPath, String desPath)
			throws Exception {
		srcPath = separatorReplace(srcPath);
		desPath = separatorReplace(desPath);
		copyAndReplaceFolder(srcPath, desPath);
		deleteFolder(srcPath);
	}

	/**
	 * 创建文件夹，如果文件夹存在则不进行创建。
	 * 
	 * @param path
	 * @throws Exception
	 */
	public static void createFolder(String path) throws Exception {
		path = separatorReplace(path);
		File folder = new File(path);
		if (folder.isDirectory()) {
			return;
		} else if (folder.isFile()) {
			deleteFile(path);
		}
		folder.mkdirs();
	}

	/**
	 * 创建一个新的文件夹，如果文件夹存在，则删除后再创建。
	 * 
	 * @param path
	 * @throws Exception
	 */
	public static void createNewFolder(String path) throws Exception {
		path = separatorReplace(path);
		File folder = new File(path);
		if (folder.isDirectory()) {
			deleteFolder(path);
		} else if (folder.isFile()) {
			deleteFile(path);
		}
		folder.mkdirs();
	}

	/**
	 * 创建一个文件，如果文件存在则不进行创建。
	 * 
	 * @param path
	 * @throws Exception
	 */
	public static File createFile(String path) throws Exception {
		path = separatorReplace(path);
		File file = new File(path);
		if (file.isFile()) {
			return file;
		} else if (file.isDirectory()) {
			deleteFolder(path);
		}
		return createFile(file);
	}

	/**
	 * 创建一个新文件，如果存在同名的文件或文件夹将会删除该文件或文件夹， 如果父目录不存在则创建父目录。
	 * 
	 * @param path
	 * @throws Exception
	 */
	public static File createNewFile(String path) throws Exception {
		path = separatorReplace(path);
		File file = new File(path);
		if (file.isFile()) {
			deleteFile(path);
		} else if (file.isDirectory()) {
			deleteFolder(path);
		}
		return createFile(file);
	}

	/**
	 * 分隔符替换 window下测试通过
	 * 
	 * @param path
	 * @return
	 */
	public static String separatorReplace(String path) {
		return path.replace("\\", "/");
	}

	/**
	 * 创建文件及其父目录。
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static File createFile(File file) throws Exception {
		createParentFolder(file);
		if (!file.createNewFile()) {
			throw new Exception("create file failure!");
		}
		return file;
	}

	/**
	 * 创建父目录
	 * 
	 * @param file
	 * @throws Exception
	 */
	private static void createParentFolder(File file) throws Exception {
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				throw new Exception("create parent directory failure!");
			}
		}
	}

	/**
	 * 根据文件路径删除文件，如果路径指向的文件不存在或删除失败则抛出异常。
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static void deleteFile(String path) throws Exception {
		path = separatorReplace(path);
		File file = getFile(path);
		if (!file.delete()) {
			throw new Exception("delete file failure");
		}
	}

	/**
	 * 删除指定目录中指定前缀和后缀的文件。
	 * 
	 * @param dir
	 * @param prefix
	 * @param suffix
	 * @throws Exception
	 */
	public static void deleteFile(String dir, String prefix, String suffix)
			throws Exception {
		dir = separatorReplace(dir);
		File directory = getFolder(dir);
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String fileName = file.getName();
				if (fileName.startsWith(prefix) && fileName.endsWith(suffix)) {
					deleteFile(file.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * 根据路径删除文件夹，如果路径指向的目录不存在则抛出异常， 若存在则先遍历删除子项目后再删除文件夹本身。
	 * 
	 * @param path
	 * @throws Exception
	 */
	public static void deleteFolder(String path) throws Exception {
		path = separatorReplace(path);
		File folder = getFolder(path);
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				deleteFolder(file.getAbsolutePath());
			} else if (file.isFile()) {
				deleteFile(file.getAbsolutePath());
			}
		}
		folder.delete();
	}

	/**
	 * 查找目标文件夹下的目标文件
	 * 
	 * @param dir
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File searchFile(String dir, String fileName)
			throws FileNotFoundException {
		dir = separatorReplace(dir);
		File f = null;
		File folder = getFolder(dir);
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				f = searchFile(file.getAbsolutePath(), fileName);
				if (f != null) {
					break;
				}
			} else if (file.isFile()) {
				if (file.getName().equals(fileName)) {
					f = file;
					break;
				}
			}
		}
		return f;
	}

	/**
	 * 获得文件类型。
	 * 
	 * @param path
	 *            ：文件路径
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String getFileType(String path) throws FileNotFoundException {
		path = separatorReplace(path);
		File file = getFile(path);
		String fileName = file.getName();
		String[] strs = fileName.split("\\.");
		if (strs.length < 2) {
			return "unknownType";
		}
		return strs[strs.length - 1];
	}

	/**
	 * 根据文件路径，获得该路径指向的文件的大小。
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static long getFileSize(String path) throws FileNotFoundException {
		path = separatorReplace(path);
		File file = getFile(path);
		return file.length();
	}

	/**
	 * 根据文件夹路径，获得该路径指向的文件夹的大小。 遍历该文件夹及其子目录的文件，将这些文件的大小进行累加，得出的就是文件夹的大小。
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static long getFolderSize(String path) throws FileNotFoundException {
		path = separatorReplace(path);
		long size = 0;
		File folder = getFolder(path);
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				size += getFolderSize(file.getAbsolutePath());
			} else if (file.isFile()) {
				size += file.length();
			}
		}
		return size;
	}

	/**
	 * 通过路径获得文件， 若不存在则抛异常， 若存在则返回该文件。
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File getFile(String path) throws FileNotFoundException {
		path = separatorReplace(path);
		File file = new File(path);
		if (!file.isFile()) {
			throw new FileNotFoundException("file not found!");
		}
		return file;
	}

	/**
	 * 通过路径获得文件夹， 若不存在则抛异常， 若存在则返回该文件夹。
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File getFolder(String path) throws FileNotFoundException {
		path = separatorReplace(path);
		File folder = new File(path);
		if (!folder.isDirectory()) {
			throw new FileNotFoundException("folder not found!");
		}
		return folder;
	}

	/**
	 * 获得文件最后更改时间。
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Date getFileLastModified(String path)
			throws FileNotFoundException {
		path = separatorReplace(path);
		File file = getFile(path);
		return new Date(file.lastModified());
	}

	/**
	 * 获得文件夹最后更改时间。
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Date getFolderLastModified(String path)
			throws FileNotFoundException {
		path = separatorReplace(path);
		File folder = getFolder(path);
		return new Date(folder.lastModified());
	}

	public static Toast result;
	public static TextView tv;

//	public static void t(Context context, int ids) {
//		if (null != result) {
//			result.cancel();
//		}
//		MetricsUtil.getCurrentWindowMetrics(context);
//		result = new Toast(context);
//		LayoutInflater inflate = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View v = inflate.inflate(R.layout.toast_yoho, null);
//		tv = (TextView) v.findViewById(R.id.tv_toast);
////
////		ViewGroup.LayoutParams layoutParams = tv.getLayoutParams();
////
////		layoutParams.height = (int) (MetricsUtil.getCurrentHeightSize(context
////				.getResources().getDimensionPixelSize(
////						R.dimen.normal_taost_height))
////				* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);
////
////		tv.setLayoutParams(layoutParams);
//		tv.setTextSize(MetricsUtil.getCurrentTextSize(context.getResources()
//				.getDimensionPixelSize(R.dimen.video_subtitle_textsize)));
//		tv.setText(context.getApplicationContext().getResources()
//				.getString(ids));
//		result.setView(v);
//		result.setDuration(Toast.LENGTH_SHORT);
//		result.show();
//	}

//	public static void t(Context context, String ids) {
//		if (null != result) {
//			result.cancel();
//		}
//		MetricsUtil.getCurrentWindowMetrics(context);
//		result = new Toast(context);
//		LayoutInflater inflate = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View v = inflate.inflate(R.layout.toast_yoho, null);
//		tv = (TextView) v.findViewById(R.id.tv_toast);
//
////		ViewGroup.LayoutParams layoutParams = tv.getLayoutParams();
////
////		layoutParams.height = (int) (MetricsUtil.getCurrentHeightSize(context
////				.getResources().getDimensionPixelSize(
////						R.dimen.normal_taost_height))
////				* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);
//
////		tv.setLayoutParams(layoutParams);
//		tv.setTextSize(MetricsUtil.getCurrentTextSize(context.getResources()
//				.getDimensionPixelSize(R.dimen.video_subtitle_textsize)));
//		result.setView(v);
//
//		result.setDuration(Toast.LENGTH_SHORT);
//		tv.setText(ids);
//		result.show();
//	}

	public static String getText(String htmlStr) {
		if (htmlStr == null || "".equals(htmlStr))
			return "";
		String textStr = "";
		Pattern pattern;
		Matcher matcher;

		try {
			String regEx_remark = "<!--.+?-->";
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
																										// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
																									// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			htmlStr = htmlStr.replaceAll("\n", "");
			htmlStr = htmlStr.replaceAll("\t", "");
			pattern = Pattern.compile(regEx_remark);// 过滤注释标签
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll("");

			pattern = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤script标签

			pattern = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤style标签

			pattern = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤html标签

			pattern = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤html标签

			textStr = htmlStr.trim();

		} catch (Exception e) {
//			cn.yohoutils.Logger.d("yin_jiawei", "获取HTML中的text出错:");
			e.printStackTrace();
		}

		return textStr;// 返回文本字符串
	}

	/**
	 * 从文件中获取对象
	 * 
	 * @param fileName
	 * @return
	 */
	public static Object getObjectFromFile(String fileName) {
		Object object = null;
		ObjectInputStream input = null;
		FileInputStream out = null;
		File file = new File(fileName);
		if (file.exists()) {
			object = new Object();

			try {
				out = new FileInputStream(file);
				input = new ObjectInputStream(out);
				object = input.readObject();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return object;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param length
	 * @return
	 */
	public static String formatFileSize(long length) {
		String result = null;
		int sub_string = 0;
		if (length >= 1073741824) {
			sub_string = String.valueOf((float) length / 1073741824).indexOf(
					".");
			result = ((float) length / 1073741824 + "000").substring(0,
					sub_string + 3) + "GB";
		} else if (length >= 1048576) {
			sub_string = String.valueOf((float) length / 1048576).indexOf(".");
			result = ((float) length / 1048576 + "000").substring(0,
					sub_string + 3) + "MB";
		} else if (length >= 1024) {
			sub_string = String.valueOf((float) length / 1024).indexOf(".");
			result = ((float) length / 1024 + "000").substring(0,
					sub_string + 3) + "KB";
		} else if (length < 1024)
			result = Long.toString(length) + "B";
		return result;
	}

	/**
	 * 将对象序列化到本地
	 */
	public static void setObjectToFile(Object object, String fileName) {
		File file = new File(fileName);
		FileOutputStream out = null;
		ObjectOutputStream oos = null;
		if (file.exists()) {
			file.delete();
		}
		try {
			file = createFile(fileName);
			out = new FileOutputStream(file);
			oos = new ObjectOutputStream(out);
			oos.writeObject(object);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 解压zip
	 * @param zipFileString
	 * @param outPathString
	 * @throws Exception
	 */
	public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
		android.util.Log.v("XZip", "UnZipFolder(String, String)");
		java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(new FileInputStream(zipFileString));
		java.util.zip.ZipEntry zipEntry;
		String szName = "";

		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();

			if (zipEntry.isDirectory()) {

				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(outPathString + File.separator + szName);
				folder.mkdirs();

			} else {

				File file = new File(outPathString + File.separator + szName);
				file.createNewFile();
				// get the output stream of the file
				FileOutputStream out = new FileOutputStream(file);
				int len;
				byte[] buffer = new byte[1024];
				// read (len) bytes into buffer
				while ((len = inZip.read(buffer)) != -1) {
					// write (len) byte from buffer at the position 0
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}// end of while

		inZip.close();

	}
	
	/**
	 * 创建并且获取解压章节根路径
	 * @param magId
	 * @param SectonId
	 */
//	public static String createAndGetSectionFilePath(String magId, String SectonId) {
//		try {
//			 createFolder(ConfigManager.SOURCE + magId + File.separator + SectonId + File.separator);
//			 return ConfigManager.SOURCE + magId + File.separator + SectonId + File.separator;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		return null;
//	}
	
	/**
	 * 获取章节根路径
	 * @param magId
	 * @param SectonId
	 * @return
	 */
//	public static String getSectionFilePath(String magId, String SectonId) {
//		 return ConfigManager.SOURCE + magId + File.separator + SectonId + File.separator;
//	}
}