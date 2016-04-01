package us.acgn.cloudMusicProxyExtServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import us.acgn.cloudMusicProxyExtServer.Processor.Processor;

public class Server extends NanoHTTPD {
	public interface ProcessingFunction {
		public String process(Map<String, String> params);
	}
	private Processor processor;
	private Map<String, ProcessingFunction> pathList;
	private Map<String, Method> methodList;

	public Server(int port) throws IOException {
		super(port);
		pathList = new HashMap<String, ProcessingFunction>();
		methodList = new HashMap<String, NanoHTTPD.Method>();
		processor = new Processor();
		processor.init(this);
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		System.out.println("\nRunning! Point your browers to http://localhost:" + port + "/ \n");
	}
	public void bind(Method method, String uri, ProcessingFunction fun){
		pathList.put(uri, fun);
		methodList.put(uri, method);
	}
	/**
	 * Filters requests and dispatch to processors
	 * @param method
	 * @param uri
	 * @param params
	 * @return
	 */
	public String process(Method method, String uri, Map<String, String> params) {
		if (method == null || uri == null || params == null) {
			return "";
		}
		if (!methodList.containsKey(uri) || !pathList.containsKey(uri)) {
			return "";
		}
		if (methodList.get(uri) != method) {
			return "";
		}
		ProcessingFunction fun = pathList.get(uri);
		return fun.process(params);
	}

	/**
	 * Main serving part, do not need to check;
	 */
	@Override
	public Response serve(IHTTPSession session) {
		Method method = session.getMethod();
		String uri = session.getUri();
		String msg = "";
		try {
			session.parseBody(new HashMap<String, String>());
			Map<String, String> params = session.getParms();
			msg = process(method, uri, params);
			if (msg=="" && ! ProjectStatus.isDevelopment){
				msg = method.toString() + "<br />" + uri + "<br />";
				for (Map.Entry<String, String> entry : params.entrySet()) {
					msg += entry.getKey() + " : " + entry.getValue() + "<br />";
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg = "IOException";

		} catch (ResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg = "ResponseException";
		}
		return newFixedLengthResponse(msg);
	}

}
