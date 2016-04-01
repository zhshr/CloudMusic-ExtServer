package us.acgn.cloudMusicProxyExtServer;

import java.io.File;

public class ProjectStatus {
	public static boolean isDevelopment(){
		File f = new File("./dev");
		if (f.exists() && !f.isDirectory()){
			return true;
		}
		return false;
	}
	public static void printStatus(){
		System.out.println("Running Status:");
		System.out.println("\tDevelopment: " + (isDevelopment()?"Yes":"No"));
	}
}
