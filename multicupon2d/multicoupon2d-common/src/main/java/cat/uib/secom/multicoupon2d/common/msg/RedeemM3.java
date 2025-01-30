package cat.uib.secom.multicoupon2d.common.msg;

import cat.uib.secom.crypto.sig.bbs.marshalling.BBSSignatureMSG;

public interface RedeemM3 {

	public abstract String getBRedeemSet();

	public abstract void setBRedeemSet(String bRedeemSet);

	public abstract BBSSignatureMSG getGroupSignature();

	public abstract void setGroupSignature(BBSSignatureMSG groupSignature);

	public abstract String getIdR();
	
	public abstract void setIdR(String idR);
}