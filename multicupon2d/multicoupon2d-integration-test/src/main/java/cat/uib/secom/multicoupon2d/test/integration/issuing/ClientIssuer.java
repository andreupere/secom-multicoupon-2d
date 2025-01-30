package cat.uib.secom.multicoupon2d.test.integration.issuing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

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




public class ClientIssuer {

	public void startProcess() {
		try {
			
			LoadCfgUtils cfg = new LoadCfgUtils(this.getClass().getResourceAsStream("/common.cfg"));
			String host = cfg.read("host");
			Integer port_issuer = new Integer( cfg.read("port_issuer") );
			
			Customer customer = Customer.getInstance(null);
			
			// create mock CommonInfo
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 100);
			
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
			
			
			// network: creating socket to the Issuer server
			Socket socket = NetUtils.getSocket(host, port_issuer);
			
			IssuingM1Impl m1 = (IssuingM1Impl) customer.issuingMSG1Logic(ci);
			// network: sending m1
			byte[] send = m1.serialize(customer.getMSGFormat());
			System.out.println("IssueM1 size (" + customer.getMSGFormat() + "): " + send.length + " (bytes)"); 
			NetUtils.write(socket.getOutputStream(), send );
			
			// network: receiving m2
			byte[] received = NetUtils.read(socket.getInputStream());
			
			System.out.println("IssueM2 size (" + customer.getMSGFormat() + "): " + received.length + " (bytes)"); 
			
			// rebuilding IssuingM2Impl message
			IssuingM2Impl m2 = new IssuingM2Impl();
			m2 = (IssuingM2Impl) m2.deSerialize(received, customer.getMSGFormat());
			
			// creating IssuingM3Impl message
			IssuingM3Impl m3 = (IssuingM3Impl) customer.issuingMSG2Logic(m2);
			
			// network: sending m3
			send = m3.serialize(customer.getMSGFormat());
			
			System.out.println("IssueM3 size (" + customer.getMSGFormat() + "): " + send.length + " (bytes)"); 
			
			NetUtils.write(socket.getOutputStream(), send);
			
			// network: receiving m4
			received = NetUtils.read(socket.getInputStream());
			System.out.println("IssueM4 size (" + customer.getMSGFormat() + "): " + received.length + " (bytes)");
			
			// network: close streams and connection to the Issuer
			NetUtils.closeStreams(socket.getInputStream(), socket.getOutputStream());
			NetUtils.closeSocket(socket);
			
			// rebuilding IssuingM4Impl message
			IssuingM4Impl m4 = new IssuingM4Impl();
			m4 = (IssuingM4Impl) m4.deSerialize(received, customer.getMSGFormat());
			
			// unblinding partially blind signature
			MCPBSImpl mcpbs = (MCPBSImpl) customer.unblindIssuing(m4);
			
			//Assert.assertEquals(mcpbs.getCommonInfo().getIssuerID(), ci.getIssuerID() );
			//Assert.assertEquals(mcpbs.getCommonInfo().getMCDescription().get(0).getValue(), ci.getMCDescription().get(0).getValue());
			
			
			System.out.println("--- Issuing M1 ---");
			System.out.println(m1.dump( customer.getMSGFormat() ));
			
			System.out.println("--- Issuing M2 ---");
			System.out.println(m2.dump( customer.getMSGFormat() ));
			
			System.out.println("--- Issuing M3 ---");
			System.out.println(m3.dump( customer.getMSGFormat() ));
			
			System.out.println("--- Issuing M4 ---");
			System.out.println(m4.dump( customer.getMSGFormat() ));
			
			MCPBSImpl mcpbsImpl = (MCPBSImpl) customer.getCustomerStore().getMcpbs();
			mcpbsImpl.serialize( new FileWriter( customer.getRootFolder() + "mcpbs.xml" ),  MSGFormatConstants.XML );
			
			Multicoupon2D mc2dImpl = (Multicoupon2D) customer.getCustomerStore().getM2d();
			mc2dImpl.serialize( new FileWriter( customer.getRootFolder()+ "m2d.xml") , MSGFormatConstants.XML );
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String args[] ) {
		(new ClientIssuer()).startProcess();
	}
	
}
