package cat.uib.secom.multicoupon2d.test.integration.redeem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Hashtable;


import cat.uib.secom.multicoupon2d.client.customer.Customer;
import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM4Impl;
import cat.uib.secom.utils.networking.NetUtils;
import cat.uib.secom.utils.strings.LoadCfgUtils;


public class ClientMerchant {
	


	
	public void startProcess() {
		try {
			LoadCfgUtils cfg = new LoadCfgUtils(this.getClass().getResourceAsStream("/common.cfg"));
			String host = cfg.read("host");
			Integer port_merchant = new Integer( cfg.read("port_merchant") );
			
			System.out.println("starting customer app");
			Customer customer = Customer.getInstance(null);
			System.out.println("loading from files");
			customer.loadFromFile();
			
			System.out.println("load BBS parameters");
			// load group signature parameters
			//BBSGroupPublicKeyMSG gpkm2 = new BBSGroupPublicKeyMSG();
			//BBSGroupPublicKey gpk = (BBSGroupPublicKey) gpkm2.toObject(new BufferedReader(new FileReader(FOLDER_MSG + GPK)), customer.getBBSParameters());
			
			//BBSUserPrivateKeyMSG uskm2 = new BBSUserPrivateKeyMSG();
			//BBSUserPrivateKey usk = (BBSUserPrivateKey) uskm2.toObject(new BufferedReader(new FileReader(FOLDER_MSG + USK)), customer.getBBSParameters());
			
			//customer.loadGroupKeyPair(gpk, usk);
			
			System.out.println("connecting to the server");
			Socket socket = NetUtils.getSocket(host, port_merchant);
			
			System.out.println("preparing message redeem 1");
			Hashtable<Integer, Integer> couponsToBeSpend = new Hashtable<Integer, Integer>();
			couponsToBeSpend.put(0, 1); // i: fins al cupo que vull enviar
			RedeemM1Impl m1 = (RedeemM1Impl) customer.createMessageRedeem1(couponsToBeSpend);
			
			
			
			byte[] data = m1.serialize(customer.getMSGFormat());
			System.out.println("RedeemM1 size (" + customer.getMSGFormat() + "): " + data.length + " (bytes)"); 
			
			NetUtils.write(socket.getOutputStream(), data);
			
			data = NetUtils.read(socket.getInputStream());
			System.out.println("RedeemM2 size (" + customer.getMSGFormat() + "): " + data.length + " (bytes)");
			
			
			RedeemM2Impl m2 = new RedeemM2Impl();
			m2 = (RedeemM2Impl) m2.deSerialize(data, customer.getMSGFormat());
			
			couponsToBeSpend = new Hashtable<Integer, Integer>();
			couponsToBeSpend.put(0, 12); // i: fins al cupo que vull enviar
			RedeemM3Impl m3 = (RedeemM3Impl) customer.createMessageRedeem3(m2, couponsToBeSpend);
			
			data = m3.serialize(customer.getMSGFormat());
			NetUtils.write(socket.getOutputStream(), data);
			System.out.println("RedeemM3 size (" + customer.getMSGFormat() + "): " + data.length + " (bytes)");
			
			data = NetUtils.read(socket.getInputStream());
			System.out.println("RedeemM4 size (" + customer.getMSGFormat() + "): " + data.length + " (bytes)");
			
			RedeemM4Impl m4 = new RedeemM4Impl();
			m4 = (RedeemM4Impl) m4.deSerialize(data, customer.getMSGFormat());
			
			
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
	
	public static void main(String args[]) {
		(new ClientMerchant()).startProcess();
	}
}
