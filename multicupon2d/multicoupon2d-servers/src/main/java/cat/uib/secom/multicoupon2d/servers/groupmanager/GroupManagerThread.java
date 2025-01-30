package cat.uib.secom.multicoupon2d.servers.groupmanager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;




import cat.uib.secom.multicoupon2d.common.msg.impl.JoinM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.JoinM2Impl;
import cat.uib.secom.utils.networking.NetUtils;
import cat.uib.secom.utils.strings.PerformanceUtils;

public class GroupManagerThread extends Thread {
	
	private Socket _socket;
	
	private InputStream _input;
	
	private OutputStream _output;
	
	private GroupManager _groupManager;
	
	

	public GroupManagerThread(Socket socket, GroupManager gm) throws IOException {
		super("Group Manager");
		this._socket = socket;
		this._input = socket.getInputStream();
		this._output = socket.getOutputStream();
		this._groupManager = gm;
		
		
		
	}
	
	
	public void run() {
		
		
		
		try {
			
			Long t1 = System.currentTimeMillis();
			byte[] data = NetUtils.read(this._input);
			t1 = System.currentTimeMillis() - t1;
			
			JoinM1Impl m1 = new JoinM1Impl();
			
			
			Long t2 = System.currentTimeMillis();
			m1 = (JoinM1Impl) m1.deSerialize(data, this._groupManager.getMSGFormat());
			t2 = System.currentTimeMillis() - t2;
			
			Long t3 = System.currentTimeMillis();
			JoinM2Impl m2 = (JoinM2Impl) this._groupManager.receiveMessage1JoinMSG(m1);
			t3 = System.currentTimeMillis() - t3;

			Long t4 = System.currentTimeMillis();
			data = m2.serialize(this._groupManager.getMSGFormat());
			t4 = System.currentTimeMillis() - t4;
			
			Long t5 = System.currentTimeMillis();
			NetUtils.write(this._output, data);
			t5 = System.currentTimeMillis() - t5;
			
			NetUtils.closeStreams(this._input, this._output);
			
			NetUtils.closeSocket(this._socket);
			
			this._groupManager.getPU().addResult("join", 0, t1, t2, t3, t4, t5);
			this._groupManager.getPU().storeInFile( this._groupManager.getPerformanceFN(), true);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
