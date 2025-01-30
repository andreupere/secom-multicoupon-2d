package cat.uib.secom.multicoupon2d.common.msg.impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.util.ASN1Dump;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;
import cat.uib.secom.multicoupon2d.common.msg.Coupon;
import cat.uib.secom.multicoupon2d.common.msg.Multicoupon;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;

@Root(name=MessageTypesConstants.MULTICOUPONS)
public class MulticouponImpl extends AbstractMSG implements Multicoupon {

	
	public static String TYPE = MessageTypesConstants.MULTICOUPONS;

	@ElementList(name="coupons-list")
	private ArrayList<Coupon> coupons;
	

	@Attribute(name="j")
	private Integer j;
	

	
	/**
	 * First unused coupon in this multicoupon j. It is transient simple xml (not in xml dom)
	 * */
	@Transient
	private Integer firstUnusedCoupon;
	
	
	public MulticouponImpl() {
		super(cat.uib.secom.multicoupon2d.common.msg.impl.MulticouponImpl.class);
		coupons = new ArrayList<Coupon>();
	}

	
	
	public ArrayList<Coupon> getCoupons() {
		return coupons;
	}


	public void setCoupons(ArrayList<Coupon> coupons) {
		this.coupons = coupons;
	}



	public Integer getJ() {
		return j;
	}

	public void setJ(Integer j) {
		this.j = j;
	}



	public Integer getFirstUnusedCoupon() {
		return firstUnusedCoupon;
	}

	public void setFirstUnusedCoupon(Integer firstUnusedCoupon) {
		this.firstUnusedCoupon = firstUnusedCoupon;
	}
	
	
	
	
	public ASN1Object toASNObject(){

		ASN1Integer jasn = new ASN1Integer(j.intValue());
		
		Iterator<Coupon> it = this.coupons.iterator();
		CouponImpl ctmp;
		ASN1EncodableVector cSeq = new ASN1EncodableVector();
		while(it.hasNext()) {
			ctmp = (CouponImpl) it.next();
			cSeq.add(ctmp.toASNObject());
		}
		
		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(jasn);
		tmp.add(new DERSequence(cSeq));
		
		asn1Object = new DERSequence(tmp);
		return asn1Object.toASN1Primitive();
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


	public String toString() {
		Iterator<Coupon> it = this.coupons.iterator();
		StringBuffer sb = new StringBuffer();
		while (it.hasNext()) {
			sb.append(it.next().toString());
		}
		sb.append(j);
		return sb.toString();
	}



	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);
	}
	
}
