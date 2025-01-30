package cat.uib.secom.multicoupon2d.common.msg;

import org.spongycastle.asn1.x509.Certificate;

import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupKeyPairMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupPublicKeyMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSUserPrivateKeyMSG;

public interface JoinM2 {
	
	public BBSUserPrivateKeyMSG getBBSUserPrivateKeyMSG();
	
	public void setBBSUserPrivateKeyMSG(BBSUserPrivateKeyMSG usk);
	
	public Certificate getBBSGroupPublicKeyCertificate();
	
	public void setBBSGroupPublicKeyCertificate(Certificate gpkCertificate);

}
