package cat.uib.secom.multicoupon2d.test.integration.join;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;



import cat.uib.secom.crypto.sig.bbs.core.impl.keys.helper.BBSGroupKeyPairImpl;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKeyImpl;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupKeyPairMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupPublicKeyMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSReaderFactory;
import cat.uib.secom.multicoupon2d.client.customer.Customer;
import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.common.msg.impl.JoinM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.JoinM2Impl;
import cat.uib.secom.utils.networking.NetUtils;
import cat.uib.secom.utils.strings.LoadCfgUtils;


public class ClientGroupManager {

	public void startProcess() {
		try {
			LoadCfgUtils cfg = new LoadCfgUtils(this.getClass().getResourceAsStream("/common.cfg"));
			String host = cfg.read("host");
			Integer port_gmanager = new Integer( cfg.read("port_gmanager") );
			
			
			// get customer object instance
			Customer customer = Customer.getInstance(null);
		
			// get socket from NetUtils
			Socket socket = NetUtils.getSocket(host, port_gmanager);
			
			// create Join message with userID=1 and cert="certificate"
			JoinM1Impl m1 = (JoinM1Impl)customer.createMessageJoinM1("1");
			
			// serialize message m1 with the format specified by getMSGFormat() (contained in common.cfg, param msg_format) 
			byte[] send = m1.serialize(customer.getMSGFormat());
			
			System.out.println("JoinM1 size (" + customer.getMSGFormat() + "): " + send.length + " (bytes)"); 
			
			// send to network
			NetUtils.write(socket.getOutputStream(), send);
			
			// receive data from server
			byte[] receive = NetUtils.read(socket.getInputStream());
			
			System.out.println("JoinM2 size (" + customer.getMSGFormat() + "): " + receive.length + " (bytes)");
			
			// create new object to receive the group key pair
			JoinM2Impl m2 = new JoinM2Impl();
			////BBSGroupKeyPairMSG keyPair = new BBSGroupKeyPairMSG();
			
			// rebuild group key pair and certificate
			m2 = (JoinM2Impl) m2.deSerialize(receive, customer.getMSGFormat());
			
			////BBSGroupKeyPair gkp = (BBSGroupKeyPair)keyPair.deSerialize(receive, 
			////														   customer.getMSGFormat().toString(), 
			////														   customer.getBBSParameters());
			
			customer.receiveJoinM2(m2);
			
			// get parameters from gkp and usk
			BBSGroupPublicKeyMSG gpkMSG = new BBSGroupPublicKeyMSG();
			gpkMSG = (BBSGroupPublicKeyMSG) gpkMSG.deSerialize(m2.getBBSGroupPublicKeyCertificate().getSubjectPublicKeyInfo().getPublicKeyData().getBytes(), customer.getMSGFormat());
			BBSGroupPublicKeyImpl gpkBBS = (BBSGroupPublicKeyImpl) BBSReaderFactory.getBBS(gpkMSG, customer.getBBSParameters());
			
			//BBSGroupPublicKey gkp = (BBSGroupPublicKey) m2.getBBSGroupPublicKeyMSG().toObject(customer.getBBSParameters());
			String a = m2.getBBSUserPrivateKeyMSG().getA();
			
			BigInteger certSerialNumber = m2.getBBSGroupPublicKeyCertificate().getSerialNumber().getValue();
			
			// close streams
			NetUtils.closeStreams(socket.getInputStream(), socket.getOutputStream());
			
			// close socket
			NetUtils.closeSocket(socket);
			
			System.out.println("--- JoinM1 ---");
			System.out.println(m1.dump( customer.getMSGFormat() ) );
			
			System.out.println("--- JoinM2 ---");
			System.out.println(m2.dump( customer.getMSGFormat() ));
			
			System.out.println("--- Group public key ---");
			System.out.println(new String(m2.getBBSGroupPublicKeyCertificate().getSubjectPublicKeyInfo().getPublicKeyData().getBytes() ));
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (Multicoupon2DException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]) {
		(new ClientGroupManager()).startProcess();
	}
}
