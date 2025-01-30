package cat.uib.secom.multicoupon2d.common.msg;

import java.util.ArrayList;


public interface Multicoupon {

	public abstract Integer getJ();

	public abstract void setJ(Integer j);

	public abstract ArrayList<Coupon> getCoupons();

	public abstract void setCoupons(ArrayList<Coupon> coupons);

	public abstract Integer getFirstUnusedCoupon();

	public abstract void setFirstUnusedCoupon(Integer firstUnusedCoupon);

}