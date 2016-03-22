package com.atul.telemetry.es;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Common class to load same elasticsearch index data for all classes
 * @author Atul
 *
 */
public class Configuration {
	public static Properties prop;
	
	//private static Properties loadProp(){
	static{
		if(prop==null){
			try{
				prop = new Properties();
				InputStream in = new FileInputStream("elasticsearch.properties");
				prop.load(in);
				in.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
//		return prop;
//	}	

	public static String IP = prop.getProperty("es.ip");
	public static int PORT = Integer.parseInt(prop.getProperty("es.port"));
	public static String INDEX = prop.getProperty("es.index");
	public static String TYPE = prop.getProperty("es.type");
}
