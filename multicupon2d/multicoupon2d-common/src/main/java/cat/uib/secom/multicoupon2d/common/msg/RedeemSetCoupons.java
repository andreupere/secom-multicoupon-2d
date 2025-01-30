package cat.uib.secom.multicoupon2d.common.msg;

import java.util.ArrayList;


public interface RedeemSetCoupons {

	public abstract ArrayList<RedeemCoupon> getRedeemCoupons();

	public abstract void setRedeemCoupons(ArrayList<RedeemCoupon> coupons);

}