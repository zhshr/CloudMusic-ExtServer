package us.acgn.cloudMusicProxyServer;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	try {
            new Server(1111);
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
        System.out.println( "Hello World!" );
    }
}
