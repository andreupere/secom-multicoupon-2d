package cat.uib.secom.multicoupon2d.client.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import cat.uib.secom.multicoupon2d.client.customer.Customer;
import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM4Impl;
import cat.uib.secom.utils.networking.NetUtils;
import cat.uib.secom.utils.strings.LoadCfgUtils;
import cat.uib.secom.utils.strings.PerformanceUtils;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MultiredeemActivity extends Activity {

	private static final String TAG = "Redeem";
	
	private static final Integer ITERATIONS = 20;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multiredeem);
		
		Log.d(TAG, Locale.getDefault().toString());

		
		try {
			
			// java code related to performance
			PerformanceUtils pu = new PerformanceUtils();
			pu.addComments("Customer redeems coupons");
			pu.addComments( (new Date()).toLocaleString() );
			pu.addComments( Build.MANUFACTURER + " " + Build.MODEL );
			pu.addComments( "NETWORK_TYPE: " );
			pu.addComments( "ENCODING_TYPE: " );
			pu.addComments( "Time in miliseconds (ms)" );
			pu.addComments("t1 \t builing M1");
			pu.addComments("t2 \t encoding M1");
			pu.addComments("t3 \t sending M1 to network");
			pu.addComments("t4 \t receiving M2 from network");
			pu.addComments("t5 \t decoding M2");
			pu.addComments("t6 \t processing M2 and building M3");
			pu.addComments("t7 \t encoding M3");
			pu.addComments("t8 \t sending M3 to network");
			pu.addComments("t9 \t receiving M4 from network");
			pu.addComments("t10 \t decoding M4");
			pu.addComments("t11 \t processing M4");
			pu.addComments("proto \t it \t t1 \t t2 \t t3 \t t4 \t t5 \t t6 \t t7 \t t8 \t t9 \t t10 \t t11");   

			
			System.out.println("starting customer app");
			Customer customer = Customer.getInstance( getResources().openRawResource( R.raw.common ) );
			System.out.println("loading from files");
			customer.loadFromFile();
			
			System.out.println("load BBS parameters");
			// load group signature parameters
			//BBSGroupPublicKeyMSG gpkm2 = new BBSGroupPublicKeyMSG();
			//BBSGroupPublicKey gpk = (BBSGroupPublicKey) gpkm2.toObject(new BufferedReader(new FileReader(FOLDER_MSG + GPK)), customer.getBBSParameters());
			
			//BBSUserPrivateKeyMSG uskm2 = new BBSUserPrivateKeyMSG();
			//BBSUserPrivateKey usk = (BBSUserPrivateKey) uskm2.toObject(new BufferedReader(new FileReader(FOLDER_MSG + USK)), customer.getBBSParameters());
			
			//customer.loadGroupKeyPair(gpk, usk);
			
			int iteration = 1;
			while (iteration <= ITERATIONS) {
			try {
				System.out.println("connecting to the server");
				Socket socket = NetUtils.getSocket(customer.getHostServers(), customer.getMerchantPort());
				
				System.out.println("preparing message redeem 1");
				
				Long t1 = System.currentTimeMillis();
				Hashtable<Integer, Integer> couponsToBeSpend = new Hashtable<Integer, Integer>();
				couponsToBeSpend.put(0, 1); // i: fins al cupo que vull enviar
				RedeemM1Impl m1 = (RedeemM1Impl) customer.createMessageRedeem1(couponsToBeSpend);
				t1 = System.currentTimeMillis() - t1;
				
				System.out.println(m1.getaRedeemSet());
				System.out.println(m1.getIdR());
				System.out.println(m1.getCertGPK());
				System.out.println(m1.getGroupSignature());
				System.out.println(m1.getMCPBS());
				
				//System.out.println(m1.dump(customer.getMSGFormat()));
				
				Long t2 = System.currentTimeMillis();
				byte[] data = m1.serialize(customer.getMSGFormat());
				t2 = System.currentTimeMillis() - t2;
				System.out.println("RedeemM1 size (" + customer.getMSGFormat() + "): " + data.length + " (bytes)"); 
				
				Long t3 = System.currentTimeMillis();
				NetUtils.write(socket.getOutputStream(), data);
				t3 = System.currentTimeMillis() - t3;
				
				Long t4 = System.currentTimeMillis();
				data = NetUtils.read(socket.getInputStream());
				t4 = System.currentTimeMillis() - t4;
				System.out.println("RedeemM2 size (" + customer.getMSGFormat() + "): " + data.length + " (bytes)");
				
				Long t5 = System.currentTimeMillis();
				RedeemM2Impl m2 = new RedeemM2Impl();
				m2 = (RedeemM2Impl) m2.deSerialize(data, customer.getMSGFormat());
				t5 = System.currentTimeMillis() - t5;
				
				
				Long t6 = System.currentTimeMillis();
				couponsToBeSpend = new Hashtable<Integer, Integer>();
				couponsToBeSpend.put(0, 12); // i: fins al cupo que vull enviar
				RedeemM3Impl m3 = (RedeemM3Impl) customer.createMessageRedeem3(m2, couponsToBeSpend);
				t6 = System.currentTimeMillis() - t6;
				
				Long t7 = System.currentTimeMillis();
				data = m3.serialize(customer.getMSGFormat());
				t7 = System.currentTimeMillis() - t7;
				
				Long t8 = System.currentTimeMillis();
				NetUtils.write(socket.getOutputStream(), data);
				t8 = System.currentTimeMillis() - t8;
				
				System.out.println("RedeemM3 size (" + customer.getMSGFormat() + "): " + data.length + " (bytes)");
				
				
				Long t9 = System.currentTimeMillis();
				data = NetUtils.read(socket.getInputStream());
				t9 = System.currentTimeMillis() - t9;
				
				System.out.println("RedeemM4 size (" + customer.getMSGFormat() + "): " + data.length + " (bytes)");
				
				
				Long t10 = System.currentTimeMillis();
				RedeemM4Impl m4 = new RedeemM4Impl();
				m4 = (RedeemM4Impl) m4.deSerialize(data, customer.getMSGFormat());
				t10 = System.currentTimeMillis() - t10;
				
				Long t11 = System.currentTimeMillis();
				customer.processMessageRedeem4(m4);
				t11 = System.currentTimeMillis() - t11;
				
				
				//m1.serialize(customer.getMSGFormat());
				System.out.println("--- RedeemM1 ---");
				System.out.println(m1.dump(customer.getMSGFormat()));
				
				//m2.serialize(customer.getMSGFormat());
				System.out.println("--- RedeemM2 ---");
				System.out.println(m2.dump(customer.getMSGFormat()));
				
				//m3.serialize(customer.getMSGFormat());
				System.out.println("--- RedeemM3 ---");
				System.out.println(m3.dump(customer.getMSGFormat()));
				
				//m4.serialize(customer.getMSGFormat());
				System.out.println("--- RedeemM4 ---");
				System.out.println(m4.dump(customer.getMSGFormat()));
				
				
				
				pu.addResult("redeem", iteration, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
				
				iteration++;
				
				Log.d(TAG, pu.getResults());
				
			} catch(Exception e) {
				System.out.println("ERROR IN ITERATION...");
				e.printStackTrace();
			}
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Multicoupon2DException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

