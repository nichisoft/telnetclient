package nicola.modugno.telnetclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.apache.commons.net.telnet.TelnetClient;

public class MyTelnetClient {
	private TelnetClient telnet;
	
	public static void main(String[] args) {
		if(args!=null && args.length==2) {
			final String IP=args[0];
			final int PORT=Integer.parseInt(args[1]);
			MyTelnetClient myTelnetClient=new MyTelnetClient();
			myTelnetClient.connect(IP, PORT);
		}
		else {
			throw new IllegalArgumentException("IP PORT");
		}

	}
	
	public void connect(final String ip, final int port) {
		telnet = new TelnetClient();
		try {
			telnet.connect(ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String sendCommand(final String command) {
		String output=null;
		if(telnet!=null && telnet.isConnected()) {
			InputStream in = telnet.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(telnet.getInputStream()));
			
			PrintStream out = new PrintStream(telnet.getOutputStream());
			out.println(command);
			out.flush();
			
			try {
				output=reader.readLine();
				System.out.println("COMMAND="+command+ " OUTPUT="+output);
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				if(out!=null) {
					out.close();
				}
				if(reader!=null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(in!=null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return output;
	}
	
	public void disconnect() {
		if(telnet!=null && telnet.isConnected()) {
			try {
				telnet.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
