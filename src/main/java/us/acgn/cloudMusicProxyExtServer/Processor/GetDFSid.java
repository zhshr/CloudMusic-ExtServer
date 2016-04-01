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

	/**
	 * Only allows one id per request!
	 * 
	 * @param params
	 *            includes:<br>
	 *            id songID<br>
	 *            quality String represents the quality requested
	 * @return
	 */
	public String process(Map<String, String> params) {
		// TODO: first try to get it from back-end server
		String[] songIDs = params.get("ids").split(",");
		Quality quality = Quality.valueOf(params.get("quality"));
		String backendResult = "";
		if (backendResult != "") {
			// DFSid get from backend server
		}

		// try to get from netease server
		Map<String, String> headers = new HashMap<String, String>();
		Map<String, String> httpParams = new HashMap<String, String>();
		httpParams.put("c", encodeSongIDs(songIDs));
		headers.put("Cookie", "os=pc");

		String spy = "";
		spy = HTTP.httpPost(ip, port, "api/v2/song/detail", null, headers, httpParams);

		// TODO: split results
		JSONAccesser data = new JSONAccesser(spy);
		System.out.println(spy);
		return extractDFSid(data.get("songs").get(0), quality);
	}

	private String encodeSongIDs(String[] ids) {
		String result = "[{\"id\":";
		for (String id : ids) {
			result += "\"" + id + "\",";
		}
		if (ids.length != 0) {
			result = result.substring(0, result.length() - 1);
		}
		result += "}]";
		return result;
	}

	private String extractDFSid(JSONAccesser data, Quality quality) {
		String songURL = "";
		long dfsID = 0;
		switch (quality) {
		case AsHigh:
		case High:
			if ((dfsID = data.get("h").get("fid").parseLong()) != 0) {
				break;
			}
		case Medium:
			if ((dfsID = data.get("m").get("fid").parseLong()) != 0) {
				break;
			}
		case Low:
			dfsID = data.get("l").get("fid").parseLong();
			break;
		case AsLow:
			if ((dfsID = data.get("l").get("fid").parseLong()) == 0) {
				if ((dfsID = data.get("m").get("fid").parseLong()) == 0) {
					dfsID = data.get("h").get("fid").parseLong();
				}
			}
		}
		return String.valueOf(dfsID);
	}
}
