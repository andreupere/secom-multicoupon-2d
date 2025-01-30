package cat.uib.secom.multicoupon2d.servers.issuer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


import cat.uib.secom.multicoupon2d.common.msg.impl.ClaimM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.ClaimM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.ClaimM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.ClaimM4Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM4Impl;
import cat.uib.secom.utils.networking.NetUtils;
import cat.uib.secom.utils.strings.MSGFormatConstants;


public class IssuerThread extends Thread {
	
	private Socket			_socket;
	
	private InputStream 	_input;
	
	private OutputStream	_output;
	
	private Issuer 			_issuer;
	
	// only for performance testing purposes
	private	Long			t1 = null;
	private Long			t2 = null;
	private Long			t3 = null;
	private Long			t4 = null;
	private Long			t5 = null;
	private Long			t6 = null;
	private Long			t7 = null;
	private Long			t8 = null;
	private Long			t9 = null;
	private Long			t10 = null;
	private Long			t11 = null;
	private Long			t12 = null;
	private Long			t13 = null;
	private Long			t14 = null;
	private Long			t15 = null;
	private Long			t16 = null;
	private Long			t17 = null;
	private Long			t18 = null; 
	private String			protocol;
	private static final String	ISSUE = "issue";
	private static final String CLAIM1 = "claim1";
	private static final String CLAIM2 = "claim2";

	public IssuerThread(Socket socket, Issuer issuer) throws IOException {
		//this._nc 		= 	nc;
		this._socket	= 	socket;
		this._input		= 	socket.getInputStream();
		this._output	=	socket.getOutputStream();
		this._issuer 	= 	issuer;
	}

	
	public void run(){
		
		try {
			// receive first message
			t1 = System.currentTimeMillis();
			byte[] data = NetUtils.read(this._input);
			t1 = System.currentTimeMillis() - t1;
			
			boolean isXML = Issuer.getInstance().getMSGFormat().equals(MSGFormatConstants.XML);
			if ( (new String(data, "UTF-8")).startsWith("<step1-issuing>") && isXML ) { // ISSUING PROTOCOL
				
				this.dealWithIssuing(data);
				
			} 
			else if ( (new String(data, "UTF-8")).startsWith("<step1-claim>") && isXML ) { // CLAIM PROTOCOL (first pair of messages)
				
				this.dealWithClaimM1(data);	
				
			} 
			else if ( (new String(data, "UTF-8")).startsWith("<step3-claim>") && isXML ) { // CLAIM PROTOCOL (second pair of messages)
				
				this.dealWithClaimM3(data);
				
			} // THE MESSAGE IS ASN1
			else if (!isXML) {
				try {
					this.dealWithIssuing(data); // TRY TO DECODE AS FIRST MESSAGE OF ISSUING PROTOCOL
					//System.out.println("dealing with issuing protocol");
				} catch(Exception e1) {
					try {
						this.dealWithClaimM1(data); // TRY TO DECODE AS FIRST MESSAGE OF CLAIM PROTOCOL
						//System.out.println("dealing with first message of claim protocol");
					} catch(Exception e2) {
						try {
							this.dealWithClaimM3(data); // TRY TO DECODE AS THRID MESSAGE OF CLAIM PROTOCOL
							//System.out.println("dealing with third message of claim protocol");
						} catch (Exception e3) {
							throw new Exception("invalid message"); // THROW EXCEPTION AS MESSAGE IS UNRECOGNIZED 
						}
					}
				}
			}
			
			NetUtils.closeStreams(this._input, this._output);
			NetUtils.closeSocket(_socket);
			
			this._issuer.getPU().addResult(this.protocol, 0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
			this._issuer.getPU().storeInFile(this._issuer.getPerformanceFN(), true);

			
		} catch(IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	private void dealWithIssuing(byte[] data) throws Exception { 
		this.protocol = ISSUE;
		
		IssuingM1Impl m1 = new IssuingM1Impl();
		t2 = System.currentTimeMillis();
		m1 = (IssuingM1Impl) m1.deSerialize(data, this._issuer.getMSGFormat());
		t2 = System.currentTimeMillis() - t2;
		
		t3 = System.currentTimeMillis();
		IssuingM2Impl m2 = (IssuingM2Impl) this._issuer.receiveMessage1Issuing(m1);
		t3 = System.currentTimeMillis() - t3;
		
		t4 = System.currentTimeMillis();
		// send second message
		data = m2.serialize(this._issuer.getMSGFormat());
		t4 = System.currentTimeMillis() - t4;
		
		t5 = System.currentTimeMillis();
		NetUtils.write(this._output, data);
		t5 = System.currentTimeMillis() - t5;
		
		t6 = System.currentTimeMillis();
		// receive third message
		data = NetUtils.read(this._input);
		t6 = System.currentTimeMillis() - t6;
		
		IssuingM3Impl m3 = new IssuingM3Impl();
		t7 = System.currentTimeMillis();
		m3 = (IssuingM3Impl) m3.deSerialize(data, this._issuer.getMSGFormat());
		t7 = System.currentTimeMillis() - t7;
		
		t8 = System.currentTimeMillis();
		IssuingM4Impl m4 = (IssuingM4Impl) this._issuer.receiveMessage3Issuing(m3);
		t8 = System.currentTimeMillis() - t8;
		
		t9 = System.currentTimeMillis();
		// send fourth message
		data = m4.serialize(this._issuer.getMSGFormat());
		t9 = System.currentTimeMillis() - t9;
		
		t10 = System.currentTimeMillis();
		NetUtils.write(this._output, data);
		t10 = System.currentTimeMillis() - t10;
		
		
		
		
	}
	
	private void dealWithClaimM1(byte[] data) throws Exception { 
		this.protocol = CLAIM1;
		ClaimM1Impl claimM1 = new ClaimM1Impl();
		
		t2 = null; // reset value of t2 because when ASN1 is used, the code tries to decode issue, then claimM1 and finally claimM2. If exception throws, t2 is set to currentTimeMilis() from the previous method
		t11 = null; // the same as t2
		
		t11 = System.currentTimeMillis();
		claimM1 = (ClaimM1Impl) claimM1.deSerialize(data, this._issuer.getMSGFormat());
		t11 = System.currentTimeMillis() - t11;
		
		t12 = System.currentTimeMillis();
		ClaimM2Impl claimM2 = this._issuer.receiveClaimM1(claimM1);
		t12 = System.currentTimeMillis() - t12;
		
		t13 = System.currentTimeMillis();
		// send second message
		data = claimM2.serialize(this._issuer.getMSGFormat());
		t13 = System.currentTimeMillis() - t13;
		
		t14 = System.currentTimeMillis();
		NetUtils.write(this._output, data);
		t14 = System.currentTimeMillis() - t14;
	}
	
	private void dealWithClaimM3(byte[] data) throws Exception { 
		this.protocol = CLAIM2;
		
		//System.out.println("trying to deal with claimM3");

		ClaimM3Impl claimM3 = new ClaimM3Impl();
		
		t2 = null; // reset value of t2 because when ASN1 is used, the code tries to decode issue, then claimM1 and finally claimM2. If exception throws, t2 is set to currentTimeMilis() from the previous method
		t11 = null; // the same as t2
		
		t15 = System.currentTimeMillis();
		claimM3 = (ClaimM3Impl) claimM3.deSerialize(data, this._issuer.getMSGFormat());
		t15 = System.currentTimeMillis() - t15;
		
		t16 = System.currentTimeMillis();
		ClaimM4Impl claimM4 = this._issuer.receiveClaimM3(claimM3);
		t16 = System.currentTimeMillis() - t16;
		
		t17 = System.currentTimeMillis();
		// send fourth message
		data = claimM4.serialize(this._issuer.getMSGFormat());
		t17 = System.currentTimeMillis() - t17;
		
		t18 = System.currentTimeMillis();
		NetUtils.write(this._output, data);
		t18 = System.currentTimeMillis() - t18;
	}
	
	
	
}
