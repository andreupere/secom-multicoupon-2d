package cat.uib.secom.multicoupon2d.common.msg;

import java.math.BigInteger;

public interface IssuingM2 {

	public abstract BigInteger getLambda();

	public abstract Integer getSessionID();

	public abstract void setSessionID(Integer sessionID);

	public abstract void setLambda(BigInteger lambda);

}