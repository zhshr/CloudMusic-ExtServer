package us.acgn.cloudMusicProxyExtServer;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class CloudMusicProxyExtServer 
{
    public static void main( String[] args )
    {
    	System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
    	ProjectStatus.printStatus();
    	try {
            new Server(1111);
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
        System.out.println( "Hello World!" );
    }
}
