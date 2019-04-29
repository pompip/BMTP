/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
	/**
	 * java原生解压zip
	 * @param path 压缩文件路径
	 * @param target 解压后文件路径
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void unzip(String path, String target) throws FileNotFoundException, IOException {
		File targetfolder = new File(target);
		ZipInputStream zi = new ZipInputStream(new FileInputStream(path));
		ZipEntry ze = null;
		FileOutputStream fo = null;
		byte[] buff = new byte[1024];
		int len;
		while ((ze = zi.getNextEntry()) != null) {
			File _file = new File(targetfolder, ze.getName());
			if (!_file.getParentFile().exists())
				_file.getParentFile().mkdirs();
			if (ze.isDirectory()) {
				_file.mkdir();
			} else {
				fo = new FileOutputStream(_file);
				while ((len = zi.read(buff)) > 0) {
					fo.write(buff, 0, len);
				}
				fo.close();
			}
			zi.closeEntry();
		}
		zi.close();
	}
	/**
	 * 删除非空文件夹,空文件夹和文件也可以删除
	 * @param file 文件夹File对象
	 */
	public static void deleteDic(File file) {
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File f:files) {
				deleteDic(f);
			}
			file.delete();
		}else {
			file.delete();
		}
	}
}
