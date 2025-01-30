package cat.uib.secom.multicoupon2d.common.msg;

import org.spongycastle.asn1.x509.Certificate;

public interface JoinM1 {

	public void setCertificate(Certificate userCert);
	
	public Certificate getCertificate();
	
	public void setId(String id);
	
	public String getId();
}
