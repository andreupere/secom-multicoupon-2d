package cat.uib.secom.multicoupon2d.common.msg.impl;

import java.io.IOException;
import java.math.BigInteger;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.util.ASN1Dump;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;
import cat.uib.secom.multicoupon2d.common.msg.Coupon;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;

@Root(name=MessageTypesConstants.COUPON)
public class CouponImpl extends AbstractMSG implements Coupon {

	
	public static String TYPE = MessageTypesConstants.COUPON;

	@Element(name="j")
	protected Integer j;
	
	@Element(name="hash")
	protected BigInteger hash;
	
	@Element(name="i")
	protected Integer i;
	
	
	
	public CouponImpl() {
		super(cat.uib.secom.multicoupon2d.common.msg.impl.CouponImpl.class);
	}
	
	
	public CouponImpl(ASN1Sequence couponASN) {
		super(cat.uib.secom.multicoupon2d.common.msg.impl.CouponImpl.class);
		this.asn1Object = couponASN;
		i =  ((ASN1Integer) couponASN.getObjectAt(0)).getValue().intValue();
		j = ((ASN1Integer) couponASN.getObjectAt(1)).getValue().intValue();
		hash = ((ASN1Integer) couponASN.getObjectAt(2)).getValue();
	}
	
	
	public BigInteger getHash() {
		return hash;
	}


	public void setHash(BigInteger hash) {
		this.hash = hash;
	}


	public Integer getI() {
		return i;
	}


	public void setI(Integer i) {
		this.i = i;
	}


	public Integer getJ() {
		return j;
	}


	public void setJ(Integer j) {
		this.j = j;
	}

	
	
	
	
	public ASN1Object toASNObject(){
		ASN1Integer iasn = new ASN1Integer(i);
		ASN1Integer jasn = new ASN1Integer(j);
		ASN1Integer hashasn = new ASN1Integer(hash);
		
		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(iasn);
		tmp.add(jasn);
		tmp.add(hashasn);
		
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
		return true;
	}


	@Override
	protected Object decodeASN1(byte[] input) {
		return false;
	}


	public void setCoupon(Coupon coupon) {
		this.hash = coupon.getHash();
		this.i = coupon.getI();
		this.j = coupon.getJ();
	}
	
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(i);
		sb.append(j);
		sb.append(hash);
		return sb.toString();
	}


	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);
	}
	
	
}
