package com.nextramile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configs {
		
		private static InputStream propertiesStream;
		private static Properties prop;
		
		public Configs() {
		}
		
	    public static String getConfig(String key) {
	    	ClassLoader classLoader = Configs.class.getClassLoader();
	    	File configFile = new File(classLoader.getResource("config.properties").getFile());
			String value = null;
			try {
				propertiesStream = new FileInputStream(configFile);
				prop = new Properties();
				prop.load(propertiesStream);
				value = prop.getProperty(key);
				propertiesStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try { propertiesStream.close(); } catch (IOException e) { }
			}
			return value;
	    }
	}