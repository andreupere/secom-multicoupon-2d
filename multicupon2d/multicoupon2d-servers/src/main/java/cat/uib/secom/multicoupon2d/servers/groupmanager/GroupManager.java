package cat.uib.secom.multicoupon2d.servers.groupmanager;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;


import cat.uib.secom.crypto.sig.bbs.core.accessors.enhanced.GroupManagerAccessor;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.helper.BBSGroupKeyPairImpl;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKeyImpl;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKeyImpl;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupKeyPairMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupPublicKeyMSG;
import cat.uib.secom.multicoupon2d.common.entity.Entity;
import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.common.msg.JoinM1;
import cat.uib.secom.multicoupon2d.common.msg.JoinM2;
import cat.uib.secom.multicoupon2d.common.msg.impl.JoinM2Impl;
import cat.uib.secom.security.CertificateUtils;
import cat.uib.secom.utils.networking.NetworkClient;
import cat.uib.secom.utils.networking.ServerDefaultImpl;
import cat.uib.secom.utils.strings.PerformanceUtils;

import org.spongycastle.asn1.x509.Certificate;
import org.spongycastle.crypto.InvalidCipherTextException;

public class GroupManager extends Entity {

	public class GroupManagerServerImpl extends ServerDefaultImpl {

		public GroupManagerServerImpl(Integer port) {
			super(port);
		}
		
		public void listen() throws IOException {
			while (_listening) {
				Socket socket = _ss.accept();
				NetworkClient nc = new NetworkClient(socket);
				nc.createSocket();
				nc.prepareInput();
				nc.prepareOutput();
				new GroupManagerThread(socket, _groupManager).run();
			}
		}
		
	}
	
	private static GroupManager _groupManager;
	
	private GroupManagerAccessor gma;
	
	private Certificate gpkCertificate;
	
	
	
	
	private GroupManager() throws Multicoupon2DException, IOException, InvalidCipherTextException, Exception {
		super();
		

		System.out.println("Init group manager server");
		System.out.println("msg format=" + getMSGFormat());
		gma = new GroupManagerAccessor(10, _bbsParameters.getCurveDescriptionFileName());
		gma.setup();
		// Generate certificate according to the group public key
		BBSGroupPublicKeyMSG gpkMSG = new BBSGroupPublicKeyMSG((BBSGroupPublicKeyImpl) gma.getGroupPublicKey());
		gpkCertificate = CertificateUtils.createGPKx509("", 365, gpkMSG.serialize(getMSGFormat())); 
		
		// for performance purposes
		_performanceFN = getRootFolder() + "performance/" + "groupmanager_" + (new Date()).getTime() + ".txt";
		
		_pu = new PerformanceUtils();
		_pu.addComments( "Customer registers to the group manager" );
		_pu.addComments( (new Date()).toString() );
		_pu.addComments( "-" );
		_pu.addComments( getMSGFormat().toString() );
		_pu.addComments( "Time in miliseconds (ms)" );
		_pu.addComments("t1 \t receiving M1");
		_pu.addComments("t2 \t decoding M1");
		_pu.addComments("t3 \t managing M1 and building M2");
		_pu.addComments("t4 \t encoding M2");
		_pu.addComments("t5 \t sending M2");
		_pu.addComments("proto \t it \t t1 \t t2 \t t3 \t t4 \t t5");
		
		
	}
	
	public static GroupManager getInstance() throws Multicoupon2DException, IOException, InvalidCipherTextException, Exception {
		if (_groupManager == null) 
			_groupManager = new GroupManager();
		return _groupManager;
	}
	
	

	
	public BBSGroupPublicKeyImpl getBBSGroupPublicKey() {
		return (BBSGroupPublicKeyImpl) gma.getGroupPublicKey();
	}
	
	
	public JoinM2 receiveMessage1JoinMSG(JoinM1 m1) throws Exception {
		System.out.println("Retrieve keypair for cert=" + m1.getCertificate() + " ...");
		
		System.out.println("Linking " + m1.getCertificate() + " with a keypair ...");
		
		System.out.println("Setting keypair no more assignable ...");
		
		
		BBSGroupKeyPairImpl gkPair = new BBSGroupKeyPairImpl((BBSGroupPublicKeyImpl) gma.getGroupPublicKey(),
													 		 (BBSUserPrivateKeyImpl) gma.getUserPrivateKey(1));
		
		BBSGroupKeyPairMSG gkPairMSG = new BBSGroupKeyPairMSG(gkPair.getGroupPublicKey(), 
															  gkPair.getUserPrivateKey());
		
		
		
		JoinM2Impl joinM2 = new JoinM2Impl();
		joinM2.setBBSUserPrivateKeyMSG(gkPairMSG.getUserPrivateKeyMSG());
		joinM2.setBBSGroupPublicKeyCertificate(gpkCertificate);
		
		return joinM2;
	}
	
	
	
	
}
