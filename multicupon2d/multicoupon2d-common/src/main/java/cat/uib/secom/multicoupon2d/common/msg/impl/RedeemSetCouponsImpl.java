package cat.uib.secom.multicoupon2d.common.msg.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.util.ASN1Dump;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;
import cat.uib.secom.multicoupon2d.common.msg.RedeemCoupon;
import cat.uib.secom.multicoupon2d.common.msg.RedeemSetCoupons;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;

@Root(name=MessageTypesConstants.REDEEM_SET)
public class RedeemSetCouponsImpl extends AbstractMSG implements RedeemSetCoupons {

	
	public static String TYPE = MessageTypesConstants.REDEEM_SET;
	
	@ElementList(name="redeem-coupons")
	protected ArrayList<RedeemCoupon> redeemCoupons;
	
	
	
	
	public RedeemSetCouponsImpl() {
		super(cat.uib.secom.multicoupon2d.common.msg.impl.RedeemSetCouponsImpl.class);
		this.redeemCoupons = new ArrayList<RedeemCoupon>();
	}


	
	
	
	public ArrayList<RedeemCoupon> getRedeemCoupons() {
		return redeemCoupons;
	}

	public void setRedeemCoupons(ArrayList<RedeemCoupon> redeemCoupons) {
		this.redeemCoupons = redeemCoupons;
	}

	
	public void setRedeemCoupon(RedeemCoupon coupon) {
		this.redeemCoupons.add(coupon);
	}
	
	public RedeemCoupon getRedeemCoupon(Integer j) throws IndexOutOfBoundsException {
		Iterator<RedeemCoupon> it = this.redeemCoupons.iterator();
		while(it.hasNext()) {
			RedeemCoupon rc = (RedeemCouponImpl) it.next();
			if ( rc.getJ().equals(j) )
				return rc;
		}
		throw new IndexOutOfBoundsException("Coupon within " + j + " multicoupon not found...");
	}
	
	
	
	
	
	
	public ASN1Object toASNObject(){
		
		Iterator<RedeemCoupon> it = this.redeemCoupons.iterator();
		RedeemCouponImpl ctmp;
		ASN1EncodableVector cSeq = new ASN1EncodableVector();
		while(it.hasNext()) {
			ctmp = (RedeemCouponImpl) it.next();
			cSeq.add(ctmp.toASNObject());
		}
		
		asn1Object = new DERSequence(cSeq);  // abans DERSet
		return asn1Object.toASN1Primitive();
	}
	
	public byte[] encodeASN1(){
		try {
			toASNObject();
			return asn1Object.getEncoded();
		} catch (IOException e) {
			return null;
		}
	}
	
	public Object decodeASN1(byte[] der){
		try {
			asn1Object = (ASN1Sequence) ASN1Primitive.fromByteArray(der);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return getValues(asn1Object);
	}
	
	public Object decode(ASN1Object asn){
		asn1Object = (ASN1Sequence) asn;
		return getValues(asn1Object);
	}
	
	private Object getValues(ASN1Sequence couponsASN){

		//DLSequence tmp = (DLSequence) couponsASN;//.getObjectAt(0);
		RedeemCouponImpl cTmp;
		redeemCoupons = new ArrayList<RedeemCoupon>();
		int nummc = couponsASN.size(); //tmp
		System.out.println(ASN1Dump.dumpAsString(couponsASN) + "\n\n" + couponsASN.size());// tmp tmp
		for(int i=0;i<nummc;i++){
			cTmp = new RedeemCouponImpl();
			cTmp.decode((ASN1Object)couponsASN.getObjectAt(i)); // tmp
			redeemCoupons.add(cTmp);
		}
		return this;
	}





	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);

	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Iterator<RedeemCoupon> it = this.redeemCoupons.iterator();
		while (it.hasNext()) {
			RedeemCouponImpl rCouponImpl = (RedeemCouponImpl) it.next();
			sb.append(rCouponImpl.toString());
			sb.append("\r\n");
		}
		return sb.toString();
	}
	
}
