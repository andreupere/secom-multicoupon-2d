package cat.uib.secom.multicoupon2d.common.dao;


import java.util.ArrayList;
import java.util.Iterator;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;



import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;
import cat.uib.secom.multicoupon2d.common.msg.Coupon;
import cat.uib.secom.multicoupon2d.common.msg.Multicoupon;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;



@Root(name=MessageTypesConstants.MULTICOUPON_2D)
public class Multicoupon2D extends AbstractMSG  {
	
	public static String TYPE = MessageTypesConstants.MULTICOUPON_2D;
	
	public static String fNAME = "m2d";
	
	
	@ElementList(name="multicoupons-list")
	private ArrayList<Multicoupon> multicoupons;
	
	

	public Multicoupon2D() {
		super(Multicoupon2D.class);
		multicoupons = new ArrayList<Multicoupon>();
	}
	
	
	
	

	public ArrayList<Multicoupon> getMulticoupon2D() {
		return multicoupons;
	}


	public void setMulticoupon2D(ArrayList<Multicoupon> multicoupons) {
		this.multicoupons = multicoupons;
	}


	/**
	 * Extract coupon i within multicoupon row j
	 * 
	 * @param j is the selected multicoupon (row)
	 * @param i is the index of the desired coupon within the especified multicoupon j
	 * 
	 * @return the desired {@see CouponXML} 
	 * */
	public Coupon getCoupon(Integer j, Integer i) {
		return (Coupon) this.multicoupons.get(j).getCoupons().get(i);
	}


	
	public String toString() {
		Iterator<Multicoupon> it = this.multicoupons.iterator();
		StringBuffer sb = new StringBuffer();
		while (it.hasNext()) {
			sb.append(it.next().toString());
		}
		return sb.toString();
	}





	@Override
	protected byte[] encodeASN1() {
		// TODO Auto-generated method stub
		return null;
	}





	@Override
	protected Object decodeASN1(byte[] input) {
		// TODO Auto-generated method stub
		return null;
	}





	@Override
	protected String dump() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



}
