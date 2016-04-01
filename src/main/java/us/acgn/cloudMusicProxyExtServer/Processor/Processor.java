package us.acgn.cloudMusicProxyExtServer.Processor;

import fi.iki.elonen.NanoHTTPD.Method;
import us.acgn.cloudMusicProxyExtServer.Server;

public class Processor {
	private String ip = "127.0.0.1";
	private int port = 1112;
	public Processor(){
		
	}
	public void init(Server server){
		GetDFSid getDFSid = new GetDFSid(ip, port);
		server.bind(Method.POST, "/getDFSid", (map) -> getDFSid.process(map));
	}
	
}
