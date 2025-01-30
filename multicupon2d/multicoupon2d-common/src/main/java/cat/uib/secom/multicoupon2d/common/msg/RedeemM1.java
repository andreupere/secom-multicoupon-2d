package cat.uib.secom.multicoupon2d.common.msg;

import org.spongycastle.asn1.x509.Certificate;

import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupPublicKeyMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSSignatureMSG;


public interface RedeemM1 {

	public abstract MCPBS getMCPBS();

	public abstract void setMCPBS(MCPBS mcpbs);

	public abstract String getaRedeemSet();

	public abstract void setaRedeemSet(String aRedeemSet);

	public abstract BBSSignatureMSG getGroupSignature();

	public abstract void setGroupSignature(BBSSignatureMSG groupSignature);

//	public abstract void setGroupPublicKey(BBSGroupPublicKeyMSG groupPublicKey);
//
//	public abstract BBSGroupPublicKeyMSG getGroupPublicKey();
	
	public abstract String getIdR();
	
	public abstract void setIdR(String idR);
	
	public void setCertGPK(Certificate certGPK);

	public Certificate getCertGPK();

}