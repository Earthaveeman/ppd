package org.jlk.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ReadProperties {

	/**
	 * 根据key获取value
	 * 
	 * @param pram
	 * @return
	 */
	public static String getProperties(String pram) {
		String param = "";
		Properties prop = new Properties();
		InputStream in = ReadProperties.class.getClassLoader()
				.getResourceAsStream("project.properties");
		try {
			prop.load(in);
			param = prop.getProperty(pram).trim();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return param;
	}

	/**
	 * 获取key列表，用逗号分隔
	 * 
	 * @return
	 */
	public static String getKeys() {
		String param = "";
		Properties prop = new Properties();
		InputStream in = ReadProperties.class.getClassLoader()
				.getResourceAsStream("task_project.properties");
		try {
			prop.load(in);
			Set<Object> keys = prop.keySet();
			for (Object key : keys) {
				param = param + getProperties("canal_schemaName") + "."
						+ key.toString().trim() + ",";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		if (param != null && !"".equals(param)) {
			param = param.substring(0, param.length() - 1);
		}
		return param;
	}

	/**
	 * 根据key获取value
	 * 
	 * @param pram
	 * @return
	 */
	public static String getProperties(String pram, String properties) {
		String param = "";
		Properties prop = new Properties();
		InputStream in = ReadProperties.class.getClassLoader()
				.getResourceAsStream(properties + ".properties");
		try {
			prop.load(in);
			param = prop.getProperty(pram).trim();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return param;
	}

	/**
	 * 获取配置文件的key-value值
	 * 
	 * @return
	 */
	public static Map<String, String> getPropertiesMap() {
		Map<String, String> result = new HashMap<String, String>();
		Properties prop = new Properties();
		InputStream in = ReadProperties.class.getClassLoader()
				.getResourceAsStream("task_project.properties");
		try {
			prop.load(in);
			Set<Object> keys = prop.keySet();
			for (Object key : keys) {
				result.put(key.toString().trim(),
						prop.getProperty(key.toString()).trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return result;
	}
}
