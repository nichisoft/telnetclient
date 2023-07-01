package nicola.modugno.telnetclient;

import java.io.IOException;
import java.io.InputStream;
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
//			myTelnetClient.sendCommand("?RGC");
//			myTelnetClient.sendCommand("?P");
//			myTelnetClient.sendCommand("?F");
//			myTelnetClient.sendCommand("?GAH");
//			myTelnetClient.sendCommand("?GAP"); //restituisce la cartella corrente
//			myTelnetClient.sendCommand("00001GHP");
//			myTelnetClient.sendCommand("00005GHP");
//			myTelnetClient.sendCommand("?ICA");
//			myTelnetClient.sendCommand("?GIA0000100008");
//			myTelnetClient.sendCommand("?GIA0000900011");
			myTelnetClient.sendCommand("00001GHP");
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
		StringBuffer output=null;
		if(telnet!=null && telnet.isConnected()) {
			InputStream in = telnet.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			PrintStream out = new PrintStream(telnet.getOutputStream());
			out.println(command);
			out.flush();
			
			try {
				
				output = new StringBuffer();
				byte[] buf = new byte[4096];
				int len = 0;
				Thread.sleep(750L);
				while ((len = in.read(buf)) != 0) {
					output.append(new String(buf, 0, len));
					Thread.sleep(750L);
			        if (in.available() == 0)
			        	break;
				}
//				output=reader.readLine();
//				output = reader.lines().collect(Collectors.joining("\r\n"));

				System.out.println("COMMAND="+command);
				System.out.println("OUTPUT=");
				System.out.println(output.toString());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				if(out!=null) {
					out.close();
				}
//				if(reader!=null) {
//					try {
//						reader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
				if(in!=null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return output.toString();
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
