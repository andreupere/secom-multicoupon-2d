package cat.uib.secom.multicoupon2d.servers.merchant;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM4Impl;
import cat.uib.secom.utils.networking.NetUtils;

public class MerchantThread extends Thread {

	private Socket 			_socket;
	
	private InputStream 	_input;
	
	private OutputStream 	_output;
	
	private Merchant 		_merchant;
	
	
	public MerchantThread(Socket socket, Merchant merchant) throws IOException {
		_input 		= 	socket.getInputStream();
		_output 	= 	socket.getOutputStream();
		_socket 	= 	socket;
		_merchant 	= 	merchant;
		_merchant.enableLogging(true);
	}
	
	public void run() {
		// receive first message
		try {
			
			Long t1 = System.currentTimeMillis();
			byte[] data = NetUtils.read(_input);
			t1 = System.currentTimeMillis() - t1;
			
			RedeemM1Impl m1 = new RedeemM1Impl();
			
			System.out.println( new String(data) );
			
			Long t2 = System.currentTimeMillis();
			m1 = (RedeemM1Impl) m1.deSerialize(data, _merchant.getMSGFormat());
			t2 = System.currentTimeMillis() - t2;
			
			Long t3 = System.currentTimeMillis();
			RedeemM2Impl m2 = (RedeemM2Impl) _merchant.createRedeemM2(m1);
			t3 = System.currentTimeMillis() - t3;
			
			// send second message
			Long t4 = System.currentTimeMillis();
			data = m2.serialize(_merchant.getMSGFormat());
			t4 = System.currentTimeMillis() - t4;
			
			Long t5 = System.currentTimeMillis();
			NetUtils.write(_output, data);
			t5 = System.currentTimeMillis() - t5;
			
			
			Long t6 = System.currentTimeMillis();
			// receive third message
			data = NetUtils.read( _input );
			t6 = System.currentTimeMillis() - t6;
			
			RedeemM3Impl m3 = new RedeemM3Impl();
			Long t7 = System.currentTimeMillis();
			m3 = (RedeemM3Impl) m3.deSerialize(data, _merchant.getMSGFormat());
			t7 = System.currentTimeMillis() - t7;
			
			
			Long t8 = System.currentTimeMillis();
			RedeemM4Impl m4 = (RedeemM4Impl) _merchant.createRedeemM4(m3);
			t8 = System.currentTimeMillis() - t8;
			
			Long t9 = System.currentTimeMillis();
			// send fourth message
			data = m4.serialize( _merchant.getMSGFormat() );
			t9 = System.currentTimeMillis() - t9;
			
			Long t10 = System.currentTimeMillis();
			NetUtils.write(_output, data);
			t10 = System.currentTimeMillis() - t10;
			
			// close streams and socket
			NetUtils.closeStreams(_input, _output);
			NetUtils.closeSocket(_socket);
			
			
			this._merchant.getPU().addResult("redeem", 0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
			this._merchant.getPU().storeInFile(this._merchant.getPerformanceFN(), true);
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
