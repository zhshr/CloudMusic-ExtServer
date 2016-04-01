package us.acgn.cloudMusicProxyExtServer.Processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;

import us.acgn.cloudMusicProxyExtServer.Utils.HTTP;
import us.acgn.cloudMusicProxyExtServer.Utils.JSONAccesser;
import us.acgn.cloudMusicProxyExtServer.Utils.NeteaseAPI.Quality;

public class GetDFSid {
	private static String ip;
	private static int port;

	public GetDFSid(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public String process(Map<String, String> params) {
		// first try to get it from back-end server
		String result = "";
		String temp = ""; //HTTP.httpPost(ip, port, "getDFSid", null, null, params);
		//temp = "123,,555";
		String[] songIDs = params.get("ids").split(",");
		String[] dfsIDs = temp.split(",");
		List<String> missingIDs = new ArrayList<String>();
		Map<String, String> musicuuHeaders = new HashMap<String, String>();
		musicuuHeaders.put("Host", "api.musicuu.com");
		for (int i = 0; i < dfsIDs.length; i++) {
			if (dfsIDs[i].equals("")) {
				missingIDs.add(songIDs[i]);
				String spy = "";
				spy = HTTP.httpGet("api.musicuu.com", 80, ("music/search/wy/" + songIDs[i] + "/1?format=json"),null, musicuuHeaders, params);
				JSONAccesser data = new JSONAccesser(spy);
				JSONArray array = data.parseJSONArray();
				for (int j = 0; j < array.size(); j++){
					try{
						if (data.get(j).get("SongId").parseString().equals(songIDs[i])){
							result += extractDFSid(data.get(j), Quality.valueOf(params.get("quality"))) + ",";
							break;
						}
					}catch(Exception e){
						
					}
				}
			}else{
				result += dfsIDs[i] + ",";
			}
		}
		if (result != null && result.length()>0){
			result = result.substring(0, result.length()-1);
		}
		return result;
	}

	private String extractDFSid(JSONAccesser data, Quality quality) {
		String songURL = "";
		switch (quality) {
		case AsHigh:
		case High:
			if ((songURL = data.get("SqUrl").parseString())!=""){
				break;
			}
		case Medium:
			if ((songURL = data.get("HqUrl").parseString())!=""){
				break;
			}
		case Low:
			data.get("LqUrl").parseString();
			break;
		case AsLow:
			if ((data.get("LqUrl").parseString())==""){
				if ((data.get("HqUrl").parseString())==""){
					data.get("SqUrl").parseString();
				}
			}
		}
		String[] temp = songURL.split("/");
		return temp[temp.length-1].split(Pattern.quote("."))[0];
	}
}
