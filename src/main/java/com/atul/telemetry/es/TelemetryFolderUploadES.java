package com.atul.telemetry.es;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.json.JSONArray;
import org.json.JSONObject;

public class TelemetryFolderUploadES implements TelemetryFields{
	
	private static File FOLDER = new File("C:\\Users\\satul\\Desktop\\telemetry");

	public static void main(String[] args) throws Exception{			
		Settings settings = Settings.settingsBuilder().put("cluster.name", "atul-es").build();
		Client client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(Configuration.IP), Configuration.PORT));
		System.out.println("Connected to elasticsearch");
				
		List<JSONObject> objects = getTelemetryDataFromFolder();
		
		for(JSONObject objFull : objects){
			List<JSONObject> objs = convert(objFull);	
			for(JSONObject obj : objs){

				IndexResponse response = client.prepareIndex(Configuration.INDEX, Configuration.TYPE)
			        .setSource(obj.toString())
			        .execute()
			        .actionGet();
			}
			        
		}
		
		// on shutdown
		client.close();
	}

	private static List<JSONObject> getTelemetryDataFromFolder() throws Exception{
		List<JSONObject> ret = new ArrayList<JSONObject>();
		String[] files = FOLDER.list(new FilenameFilter() {			
			public boolean accept(File dir, String name) {
				return name.endsWith(".json");
			}
		});
		
		for(String file : files){
			System.out.println("Reading " + file);
			
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new FileReader(new File(FOLDER,file)));
			String line = null;
			while( (line = reader.readLine()) != null){
				builder.append(line);
			}
			
			JSONObject obj = new JSONObject(builder.toString());
			ret.add(obj);
			
			reader.close();
		}
		
		
		return ret;
	}

	private static List<JSONObject> convert(JSONObject fullObj) {
		List<JSONObject> objects = new ArrayList<JSONObject>();
		
		JSONObject baseObj = new JSONObject();
		
		try{
		
			baseObj.put(INSTALL_ID, fullObj.get(INSTALL_ID));
			baseObj.put(SIDN, fullObj.get(SIDN));
			baseObj.put(SES_START, fullObj.get(SES_START));
			baseObj.put(PRODUCT_ID, fullObj.get(PRODUCT_ID));
			baseObj.put(SID, fullObj.get(SID));
			
			try{
				JSONObject props = (JSONObject)fullObj.get(PROPERTIES);
				baseObj.put(PROP_OS, props.get("os"));
				baseObj.put(PROP_OSVER, props.get("osver"));
				baseObj.put(PROP_MACHINE, props.get("machine"));		
				baseObj.put(PROP_BINDING, props.get("binding"));
				baseObj.put(PROP_LANGUAGE, props.get("language"));
				baseObj.put(PROP_COUNTRY, props.get("country"));
			}catch(Exception e){
				System.out.println("Properties missing");
			}
			
			try{
				JSONArray events = (JSONArray)fullObj.get("Events");
				for(Iterator<Object> itr = events.iterator(); itr.hasNext();){
					JSONObject evtObj = (JSONObject)itr.next();
					JSONObject newObj = new JSONObject(baseObj.toString());
					
					newObj.put(EVT_N, evtObj.get("N"));
					newObj.put(EVT_C,evtObj.get("C"));
					newObj.put(EVT_T, evtObj.get("T"));
					
					objects.add(newObj);
				}				
			}catch(Exception e){
				System.out.println("Events missing");
			}
			
			//obj.put("prop_geo", country[1]);
			//obj.put(PROP_GEO, getRandomLatLong());
			
			/*
			obj.put(EVT_N, EVT_NAME[rand.nextInt(EVT_NAME.length)]);
			obj.put(EVT_C, "1");
			obj.put(EVT_T, getDate());
			*/
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(fullObj);
		}
		
		return objects;
	}
	
}
