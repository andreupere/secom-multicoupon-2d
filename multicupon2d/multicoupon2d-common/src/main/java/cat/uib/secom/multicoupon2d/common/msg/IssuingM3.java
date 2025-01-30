package cat.uib.secom.multicoupon2d.common.msg;

import java.math.BigInteger;

public interface IssuingM3 {

	public abstract BigInteger getBeta();

	public abstract Integer getSessionID();

	public abstract void setSessionID(Integer sessionID);

	public abstract void setBeta(BigInteger beta);

}