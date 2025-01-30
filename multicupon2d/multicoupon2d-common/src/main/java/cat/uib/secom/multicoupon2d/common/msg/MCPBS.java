package cat.uib.secom.multicoupon2d.common.msg;

import java.math.BigInteger;
import java.util.ArrayList;


public interface MCPBS {

	public abstract CommonInfo getCommonInfo();

	public abstract void setCommonInfo(CommonInfo commonInfo);

	public abstract BigInteger getDelta();

	public abstract void setDelta(BigInteger delta);

	public abstract BigInteger getOmega();

	public abstract void setOmega(BigInteger omega);

	public abstract ArrayList<Coupon> getRootCoupons();

	public abstract void setRootCoupons(ArrayList<Coupon> rootCoupons);

}