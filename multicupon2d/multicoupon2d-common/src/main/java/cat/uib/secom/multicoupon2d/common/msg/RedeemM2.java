package cat.uib.secom.multicoupon2d.common.msg;



public interface RedeemM2 {

	public abstract String getMerchantSignature();

	public abstract void setMerchantSignature(String mSignature);

	public abstract String getServiceEncrypted();

	public abstract void setServiceEncrypted(String servEnc);
	
	public abstract String getIdR();
	
	public abstract void setIdR(String idR);

}