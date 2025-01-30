package cat.uib.secom.multicoupon2d.common.msg;

import java.math.BigInteger;

public interface IssuingM4 {

	public abstract BigInteger getVarphi();

	public abstract BigInteger getGamma();

	public abstract Integer getSessionID();

	public abstract void setSessionID(Integer sessionID);

	public abstract void setGamma(BigInteger gamma);

	public abstract void setVarphi(BigInteger varphi);

}