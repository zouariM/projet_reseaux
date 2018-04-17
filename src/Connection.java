import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {
	protected String ip;
	protected int port;
	protected BufferedInputStream is;
	protected PrintWriter writer;
	protected Socket soc;
	
	
	public Connection(String ip,int port){
		this.ip = ip;
		this.port = port;
	}
	
	protected void makeRequest(String request) throws Exception{
        soc = new Socket(ip,port);
        is = new BufferedInputStream(soc.getInputStream());
		writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())), true);	
		writer.println(request);
		writer.flush();
	}
	
	protected void endRequest() throws Exception{
		is.close();
		writer.close();
		soc.close();
	}

	// read word and accept it
	protected  boolean accept(String word) throws Exception{
		for(int i=0;i<word.length();i++){
			int ret = is.read();
			if(ret == -1 || ((char) ret)!=word.charAt(i)) return false;
		}
		return true;
	}
	
	// accept word foating a a sea of spaces
	protected boolean acceptNext(String word) throws Exception{
		escapeWhite();
		if(!accept(word)) return false;
		escapeWhite();
		return true;
	}
	
	protected  void escapeWhite() throws Exception{
		while(true){
			is.mark(2); 
			char c = (char ) is.read();
			if(c == ' ') continue;
		    is.reset();	
		    break;
		}
	}
	
	// or until the stream fucks up
	protected String readUntil(char c) throws Exception{
		String res = "";
		while(true){
			is.mark(2);
			int ret = is.read();
			if(ret == -1) break;
			char k = (char ) ret;
			if(k == c) {
				is.reset();
				break;
			}
			res+=k;
		}
		return res;
	}
	
	protected String readUntil(char a,char b) throws Exception{
		String res = "";
		while(true){
			is.mark(2);
			char k = (char ) is.read();
			if(k == a || k == b) {
				is.reset();
				break;
			}
			res+=k;
		}
		return res;
	}
	
	protected char peekNext() throws Exception{
		// TODO: if im peeking, read should not return -1, throw exception in case!
		is.mark(2);
		char c = (char) is.read();
		is.reset();
		return c;
	}
	
}