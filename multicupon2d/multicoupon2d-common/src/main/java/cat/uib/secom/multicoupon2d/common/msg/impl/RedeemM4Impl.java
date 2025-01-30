package cat.uib.secom.multicoupon2d.common.msg.impl;

import java.io.IOException;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERGeneralString;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.util.ASN1Dump;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;
import cat.uib.secom.multicoupon2d.common.msg.RedeemM4;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;

@Root(name=MessageTypesConstants.MULTIREDEEM_MESSAGE4)
public class RedeemM4Impl extends AbstractMSG implements RedeemM4 {

	@Element(name="idr")
	private String idR;
	
	@Element(name="merchant-signature")
	private String merchantSignature;
	

	
	public RedeemM4Impl() {
		super(RedeemM4Impl.class);
	}
	
	
	@Override
	public String getIdR() {
		return this.idR;
	}

	@Override
	public void setIdR(String idR) {
		this.idR = idR;
	}
	
	public String getMerchantSignature() {
		return this.merchantSignature;
	}
	
	public void setMerchantSignature(String merchantSignature) {
		this.merchantSignature = merchantSignature;
	}
	
	

	public ASN1Object toASNObject(){

		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(new DERGeneralString(merchantSignature));
		tmp.add(new DERGeneralString(idR));
		
		asn1Object = new DERSequence(tmp);
		return asn1Object.toASN1Primitive();
	}
	

	@Override
	protected byte[] encodeASN1() {
		try {
			toASNObject();
			return asn1Object.getEncoded();
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	protected Object decodeASN1(byte[] input) {
		try {
			asn1Object = (ASN1Sequence) ASN1Primitive.fromByteArray(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		getValues(asn1Object);
		return this;
	}
	
	
	public boolean decode(ASN1Object asn){
		asn1Object = (ASN1Sequence) asn;
		return getValues(asn1Object);
	}
	
	private boolean getValues(ASN1Sequence m2redeemASN){
		merchantSignature = ((DERGeneralString) m2redeemASN.getObjectAt(0)).getString();
		idR = ((DERGeneralString) m2redeemASN.getObjectAt(1) ).getString();
		//mSignature =  new MerchantSignature();
		//boolean result = mSignature.decode((ASN1Object) m2redeemASN.getObjectAt(1));
		//if(!result){return false;}

		return true;
	}

	

	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);
	}

}
