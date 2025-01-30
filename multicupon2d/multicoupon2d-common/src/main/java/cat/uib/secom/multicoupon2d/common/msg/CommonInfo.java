package cat.uib.secom.multicoupon2d.common.msg;

import java.util.ArrayList;
import java.util.Date;

public interface CommonInfo {

	public abstract Integer getIssuerID();

	public abstract void setIssuerID(Integer issuerID);

	public abstract Integer getServiceID();

	public abstract void setServiceID(Integer serviceID);

	public abstract void setExpiration(Date expiration);

	public abstract void setRefund(Date refund);

	public abstract void setClaim(Date claim);

	public abstract Date getExpiration();

	public abstract Date getClaim();

	public abstract Date getRefund();
	
	public ArrayList<MCDescription> getMCDescription();
	
	public void setMCDescription(ArrayList<MCDescription> mcdDesc);

}