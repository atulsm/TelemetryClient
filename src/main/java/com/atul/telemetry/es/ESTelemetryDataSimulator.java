package com.atul.telemetry.es;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.json.JSONObject;

/**
 * 
 * @author satul
 *
 */
public class ESTelemetryDataSimulator implements TelemetryFields{

	private static String[] INSTALER_IDS = new String[]{"bWljcm9mb2N1cy5jb20=", "aWljcm9mb2N1cy5jb30="};
	private static String[] SIDNS = new String[]{"0", "1","2","3"};
	private static String[] SIDS = new String[]{"2beca085-a621-47da-9689-4bbb347f3e00", "2beca085-a621-47da-9689-4bbb347f3e01","2beca085-a621-47da-9689-4bbb347f3e02","2beca085-a621-47da-9689-4bbb347f3e03"};
	private static String[] PRODUCTS = new String[]{"Zenworks", "Sentinel","Filr","ChangeGuardian"};
	private static String[] OS = new String[]{"Microsoft_Windows_10_Enterprise", "Microsoft_Windows_2014","SLES","RHEL"};
	private static String[] MACHINE = new String[]{"NWB-MARKCX1-W10", "atul-laptop","test1.blr.novell.com","buildmaster.vva.com"};
	private static String[] LANG = new String[]{".Net", "JAVA","c++"};
	private static String[][] COUNTRY = new String[][]{{"IN","28.6456024, 77.21110499999998"}, {"GB","51.507068, -0.12761069999999108"},{"US","38.9077775, -77.03609589999996"}};
	
	private static String[] EVT_NAME = new String[]{"StorageUsed", "LoginScreen","Correlation"};
	
	
	public static void main(String[] args) throws Exception{			
		Settings settings = Settings.settingsBuilder().put("cluster.name", "atul-es").build();
		Client client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(Configuration.IP), Configuration.PORT));
		System.out.println("Connected to elasticsearch");
				
		for(int i=0;i<10;i++){
			JSONObject obj = getTelemetrySampleData();				
			IndexResponse response = client.prepareIndex(Configuration.INDEX, Configuration.TYPE)
			        .setSource(obj.toString())
			        .execute()
			        .actionGet();
		}
		
		// on shutdown
		client.close();
	}
	
	public static void main1(String[] args) throws Exception{
		getDate();
		
	}

	private static String getDate() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return format.format(date);
	}
	

	private static JSONObject getTelemetrySampleData() {
		Random rand = new Random();		
		
		JSONObject obj = new JSONObject();
		obj.put(INSTALL_ID, INSTALER_IDS[rand.nextInt(INSTALER_IDS.length)]);
		obj.put(SIDN, SIDNS[rand.nextInt(SIDNS.length)]);
		obj.put(SES_START, getDate());
		obj.put(PRODUCT_ID, PRODUCTS[rand.nextInt(PRODUCTS.length)]);
		obj.put(SID, SIDS[rand.nextInt(SIDS.length)]);
		
		obj.put(PROP_OS, OS[rand.nextInt(OS.length)]);
		obj.put(PROP_OSVER, SIDNS[rand.nextInt(SIDNS.length)]);
		obj.put(PROP_MACHINE, MACHINE[rand.nextInt(MACHINE.length)]);		
		obj.put(PROP_BINDING, LANG[rand.nextInt(LANG.length)]);
		obj.put(PROP_LANGUAGE, "en");
		String[] country = COUNTRY[rand.nextInt(COUNTRY.length)];
		obj.put(PROP_COUNTRY, country[0]);
		//obj.put("prop_geo", country[1]);
		obj.put(PROP_GEO, getRandomLatLong());
		
		obj.put(EVT_N, EVT_NAME[rand.nextInt(EVT_NAME.length)]);
		obj.put(EVT_C, "1");
		obj.put(EVT_T, getDate());
		return obj;
	}
	
	private static String getRandomLatLong(){
		Random rand = new Random();	
		StringBuilder build = new StringBuilder();
		
		if(rand.nextBoolean()){
			build.append("-");
		}
		build.append(rand.nextInt(90)).append(".00, ");
		
		if(rand.nextBoolean()){
			build.append("-");
		}
		build.append(rand.nextInt(180)).append(".00");
		
		return build.toString();
	}
	
	
}
