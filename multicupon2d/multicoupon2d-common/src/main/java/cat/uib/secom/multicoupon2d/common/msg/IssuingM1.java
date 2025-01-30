package cat.uib.secom.multicoupon2d.common.msg;

import java.math.BigInteger;


public interface IssuingM1 {

	public abstract CommonInfo getCommonInfo();

	public abstract void setCommonInfo(CommonInfo ci);

	public abstract BigInteger getAlpha();

	public abstract void setAlpha(BigInteger alpha);

}