package cat.uib.secom.multicoupon2d.common.msg;

import java.math.BigInteger;

public interface Coupon {

	public abstract void setI(Integer i);

	public abstract void setJ(Integer j);

	public abstract void setHash(BigInteger hash);

	public abstract Integer getI();

	public abstract Integer getJ();

	public abstract BigInteger getHash();
	
	public abstract void setCoupon(Coupon coupon);

}