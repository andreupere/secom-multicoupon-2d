package cat.uib.secom.multicoupon2d.client.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKeyImpl;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupPublicKeyMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSReaderFactory;
import cat.uib.secom.multicoupon2d.client.customer.Customer;
import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.common.msg.impl.JoinM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.JoinM2Impl;
import cat.uib.secom.utils.networking.NetUtils;
import cat.uib.secom.utils.strings.PerformanceUtils;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class RegisterGroupManagerActivity extends Activity {
	
	public static final String TAG = "registerGM";
	
	public static final String NETWORK_TYPE = ""; // 3G, WIFI
	
	public static final String ENCODING_TYPE = ""; // XML, ASN1
	
	public static final Integer ITERATIONS = 20; // number of times to repeat the process
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_group_manager);
		
		// java code to register group manager...
		
		try {
			
			// object containing performance results
			PerformanceUtils pu = new PerformanceUtils();
			pu.addComments( "Customer registers to the group manager" );
			pu.addComments( (new Date()).toLocaleString() );
			pu.addComments( Build.MANUFACTURER + " " + Build.MODEL );
			pu.addComments( NETWORK_TYPE );
			pu.addComments( ENCODING_TYPE );
			pu.addComments( "Time in miliseconds (ms)" );
			pu.addComments("t1 \t builing M1");
			pu.addComments("t2 \t encoding M1");
			pu.addComments("t3 \t sending M1 to network (group manager)");
			pu.addComments("t4 \t receiving M2 from network (group manager)");
			pu.addComments("t5 \t decoding M2");
			pu.addComments("t6 \t processing M2");
			pu.addComments("proto \t it \t t1 \t t2 \t t3 \t t4 \t t5 \t t6");   

        	
        	// get customer object instance
			Customer customer = Customer.getInstance( getResources().openRawResource( R.raw.common ) );
			
			Log.d(TAG, "after Customer.getInstance");
			
			int iteration = 1;
			while (iteration <= ITERATIONS) {
		
				// get socket from NetUtils
				Socket socket = NetUtils.getSocket(customer.getHostServers(), customer.getGmanagerPort());
				
				Log.d(TAG, "after getSocket");
				
				Long t1 = System.currentTimeMillis();
				// create Join message with userID=1 and cert="certificate"
				JoinM1Impl m1 = (JoinM1Impl)customer.createMessageJoinM1("1");
				t1 = System.currentTimeMillis() - t1;
				
				Long t2 = System.currentTimeMillis();
				// serialize message m1 with the format specified by getMSGFormat() (contained in common.cfg, param msg_format) 
				byte[] send = m1.serialize(customer.getMSGFormat());
				t2 = System.currentTimeMillis() - t2;
				
				System.out.println("JoinM1 size (" + customer.getMSGFormat() + "): " + send.length + " (bytes)"); 
				
				Long t3 = System.currentTimeMillis();
				// send to network
				NetUtils.write(socket.getOutputStream(), send);
				t3 = System.currentTimeMillis() - t3;
				
				Long t4 = System.currentTimeMillis();
				// receive data from server
				byte[] receive = NetUtils.read(socket.getInputStream());
				t4 = System.currentTimeMillis() - t4;
				
				
				System.out.println("JoinM2 size (" + customer.getMSGFormat() + "): " + receive.length + " (bytes)");
				
				// create new object to receive the group key pair
				JoinM2Impl m2 = new JoinM2Impl();
				////BBSGroupKeyPairMSG keyPair = new BBSGroupKeyPairMSG();
				
				Long t5 = System.currentTimeMillis();
				// rebuild group key pair and certificate
				m2 = (JoinM2Impl) m2.deSerialize(receive, customer.getMSGFormat());
				t5 = System.currentTimeMillis() - t5;
				
				////BBSGroupKeyPair gkp = (BBSGroupKeyPair)keyPair.deSerialize(receive, 
				////														   customer.getMSGFormat().toString(), 
				////														   customer.getBBSParameters());
				
				Long t6 = System.currentTimeMillis();
				customer.receiveJoinM2(m2);
				t6 = System.currentTimeMillis() - t6;
				
				// get parameters from gkp and usk
				BBSGroupPublicKeyMSG gpkMSG = new BBSGroupPublicKeyMSG();
				gpkMSG = (BBSGroupPublicKeyMSG) gpkMSG.deSerialize(m2.getBBSGroupPublicKeyCertificate().
																   getSubjectPublicKeyInfo().
																   getPublicKeyData().
																   getBytes(), 
																   customer.getMSGFormat());
				BBSGroupPublicKeyImpl gpkBBS = (BBSGroupPublicKeyImpl) BBSReaderFactory.getBBS(gpkMSG, customer.getBBSParameters());
				
				//BBSGroupPublicKey gkp = (BBSGroupPublicKey) m2.getBBSGroupPublicKeyMSG().toObject(customer.getBBSParameters());
				String a = m2.getBBSUserPrivateKeyMSG().getA();
				
				BigInteger certSerialNumber = m2.getBBSGroupPublicKeyCertificate().getSerialNumber().getValue();
				
				// close streams
				NetUtils.closeStreams(socket.getInputStream(), socket.getOutputStream());
				
				// close socket
				NetUtils.closeSocket(socket);
				
				
				pu.addResult("join", iteration, t1, t2, t3, t4, t5, t6);
				
				// iteration+1
				iteration++;
			
		}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Multicoupon2DException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
