package cat.uib.secom.multicoupon2d.client.android;

import java.io.FileWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cat.uib.secom.multicoupon2d.client.customer.Customer;
import cat.uib.secom.multicoupon2d.common.dao.Multicoupon2D;
import cat.uib.secom.multicoupon2d.common.msg.CommonInfo;
import cat.uib.secom.multicoupon2d.common.msg.MCDescription;
import cat.uib.secom.multicoupon2d.common.msg.impl.CommonInfoImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM4Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.MCDescriptionImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.MCPBSImpl;
import cat.uib.secom.utils.networking.NetUtils;
import cat.uib.secom.utils.strings.LoadCfgUtils;
import cat.uib.secom.utils.strings.MSGFormatConstants;
import cat.uib.secom.utils.strings.PerformanceUtils;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class IssuingActivity extends Activity {

	public static final String TAG = "issuing";
	
	public static final Integer ITERATIONS = 20;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_issuing);
		
		
		try {
			
			// java code related to performance
			PerformanceUtils pu = new PerformanceUtils();
			pu.addComments("Customer issues a new multicoupon2D");
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

			
			Customer customer = Customer.getInstance( getResources().openRawResource( R.raw.common ) );
			
			// create mock CommonInfo
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 100);
			
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			
			
			CommonInfo ci = new CommonInfoImpl();
			ci.setClaim(calendar.getTime());
			ci.setExpiration(calendar.getTime());
			ci.setIssuerID( new Integer(1) );
			ci.setRefund(calendar.getTime());
			ci.setServiceID( new Integer(2) );
			
			MCDescription mcd = new MCDescriptionImpl();
			mcd.setJ(1);
			mcd.setNumber(12);
			mcd.setValue(new String("23"));
			
			ArrayList<MCDescription> al = new ArrayList<MCDescription>();
			al.add(mcd);
			
			ci.setMCDescription(al);
			
			
			int iteration = 1;
			while (iteration <= ITERATIONS) {
				try {
				// network: creating socket to the Issuer server
				Log.d(TAG, customer.getHostServers() + customer.getIssuerPort());
				
				Socket socket = NetUtils.getSocket(customer.getHostServers(), customer.getIssuerPort());
				
				Long t1 = System.currentTimeMillis();
				IssuingM1Impl m1 = (IssuingM1Impl) customer.issuingMSG1Logic(ci);
				t1 = System.currentTimeMillis() - t1;
				
				Long t2 = System.currentTimeMillis();
				// network: sending m1
				byte[] send = m1.serialize(customer.getMSGFormat());
				t2 = System.currentTimeMillis() - t2;
				
				System.out.println("IssueM1 size (" + customer.getMSGFormat() + "): " + send.length + " (bytes)"); 
				Long t3 = System.currentTimeMillis();
				NetUtils.write(socket.getOutputStream(), send );
				t3 = System.currentTimeMillis() - t3;
				
				// network: receiving m2
				Long t4 = System.currentTimeMillis();
				byte[] received = NetUtils.read(socket.getInputStream());
				t4 = System.currentTimeMillis() - t4;
				
				System.out.println("IssueM2 size (" + customer.getMSGFormat() + "): " + received.length + " (bytes)"); 
				
				// rebuilding IssuingM2Impl message
				IssuingM2Impl m2 = new IssuingM2Impl();
				Long t5 = System.currentTimeMillis();
				m2 = (IssuingM2Impl) m2.deSerialize(received, customer.getMSGFormat());
				t5 = System.currentTimeMillis() - t5;
				
				Long t6 = System.currentTimeMillis();
				// creating IssuingM3Impl message
				IssuingM3Impl m3 = (IssuingM3Impl) customer.issuingMSG2Logic(m2);
				t6 = System.currentTimeMillis() - t6;
				
				Long t7 = System.currentTimeMillis();
				// network: sending m3
				send = m3.serialize(customer.getMSGFormat());
				t7 = System.currentTimeMillis() - t7;
				
				System.out.println("IssueM3 size (" + customer.getMSGFormat() + "): " + send.length + " (bytes)"); 
				
				Long t8 = System.currentTimeMillis();
				NetUtils.write(socket.getOutputStream(), send);
				t8 = System.currentTimeMillis() - t8;
				
				Long t9 = System.currentTimeMillis();
				// network: receiving m4
				received = NetUtils.read(socket.getInputStream());
				t9 = System.currentTimeMillis() - t9;
				
				System.out.println("IssueM4 size (" + customer.getMSGFormat() + "): " + received.length + " (bytes)");
				
				// network: close streams and connection to the Issuer
				NetUtils.closeStreams(socket.getInputStream(), socket.getOutputStream());
				NetUtils.closeSocket(socket);
				
				// rebuilding IssuingM4Impl message
				IssuingM4Impl m4 = new IssuingM4Impl();
				Long t10 = System.currentTimeMillis();
				m4 = (IssuingM4Impl) m4.deSerialize(received, customer.getMSGFormat());
				t10 = System.currentTimeMillis() - t10;
				
				Long t11 = System.currentTimeMillis();
				// unblinding partially blind signature
				MCPBSImpl mcpbs = (MCPBSImpl) customer.unblindIssuing(m4);
				t11 = System.currentTimeMillis() - t11;
				
				//Assert.assertEquals(mcpbs.getCommonInfo().getIssuerID(), ci.getIssuerID() );
				//Assert.assertEquals(mcpbs.getCommonInfo().getMCDescription().get(0).getValue(), ci.getMCDescription().get(0).getValue());
				
				
				
				//System.out.println("--- Issuing M1 ---");
				//System.out.println(m1.dump( customer.getMSGFormat() ));
				
				//System.out.println("--- Issuing M2 ---");
				//System.out.println(m2.dump( customer.getMSGFormat() ));
				
				//System.out.println("--- Issuing M3 ---");
				//System.out.println(m3.dump( customer.getMSGFormat() ));
				
				//System.out.println("--- Issuing M4 ---");
				//System.out.println(m4.dump( customer.getMSGFormat() ));
				
				
				//MCPBSImpl mcpbsImpl = (MCPBSImpl) customer.getCustomerStore().getMcpbs();
				//mcpbsImpl.serialize( new FileWriter( customer.getRootFolder() + "mcpbs.xml" ),  MSGFormatConstants.XML );
				
				//Multicoupon2D mc2dImpl = (Multicoupon2D) customer.getCustomerStore().getM2d();
				//mc2dImpl.serialize( new FileWriter( customer.getRootFolder()+ "m2d.xml") , MSGFormatConstants.XML );
			
				
				pu.addResult("issue", iteration, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
				
				iteration++;
				
				Log.d(TAG, pu.getResults());
				
				} catch (Exception e) {
					Log.d(TAG, "exception during TEST");
					Thread.sleep(2000);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

