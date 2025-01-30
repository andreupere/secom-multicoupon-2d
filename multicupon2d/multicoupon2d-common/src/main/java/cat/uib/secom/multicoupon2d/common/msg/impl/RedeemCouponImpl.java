package cat.uib.secom.multicoupon2d.common.msg.impl;

import java.io.IOException;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.util.ASN1Dump;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.multicoupon2d.common.msg.RedeemCoupon;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;

@Root(name=MessageTypesConstants.REDEEM_COUPON)
public class RedeemCouponImpl extends CouponImpl implements RedeemCoupon {

	public static String TYPE = MessageTypesConstants.REDEEM_COUPON;
	
	@Element(name="kj")
	protected Integer kj;
	
	

	
	
	public RedeemCouponImpl() {
		super();
	}
	
	
	public RedeemCouponImpl(ASN1Sequence couponASN) {
		this.asn1Object = couponASN;
		i =  ((ASN1Integer) couponASN.getObjectAt(0)).getValue().intValue();
		j = ((ASN1Integer) couponASN.getObjectAt(1)).getValue().intValue();
		hash = ((ASN1Integer) couponASN.getObjectAt(2)).getValue();
		kj =  ((ASN1Integer) couponASN.getObjectAt(3)).getValue().intValue();
	}
	
	
	
	public Integer getKj() {
		return kj;
	}

	public void setKj(Integer kj) {
		this.kj = kj;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.i);
		sb.append("\r\n");
		sb.append(this.j);
		sb.append("\r\n");
		sb.append(this.hash);
		sb.append("\r\n");
		sb.append(this.kj);
		sb.append("\r\n");
		return sb.toString();
	}
	
	
	
	
	
	
	
	
	
	public ASN1Object toASNObject(){
		
		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(new ASN1Integer(i));
		tmp.add(new ASN1Integer(j));
		tmp.add(new ASN1Integer(hash));
		tmp.add(new ASN1Integer(kj));
		
		asn1Object = new DERSequence(tmp);
		return asn1Object.toASN1Primitive();
	}
	
	public ASN1Object getASNObject(){
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
	
	public boolean decode(ASN1Object asn){
		asn1Object = (ASN1Sequence) asn;
		i = (((ASN1Integer) asn1Object.getObjectAt(0)).getValue()).intValue();
		j = (((ASN1Integer) asn1Object.getObjectAt(1)).getValue()).intValue();
		hash = (((ASN1Integer) asn1Object.getObjectAt(2)).getValue());
		kj = (((ASN1Integer) asn1Object.getObjectAt(3)).getValue()).intValue();
		return true;
	}
	
	public String dump() {
		return ASN1Dump.dumpAsString(asn1Object);

	}
}
