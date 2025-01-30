package cat.uib.secom.multicoupon2d.common.msg.impl;

import java.io.IOException;
import java.math.BigInteger;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.util.ASN1Dump;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;
import cat.uib.secom.multicoupon2d.common.msg.CommonInfo;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM1;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;

@Root(name=MessageTypesConstants.ISSUING_MESSAGE1)
public class IssuingM1Impl extends AbstractMSG implements IssuingM1 {

	@Element(name="common-info")
	private CommonInfo ci;
	
	@Element(name="alpha")
	private BigInteger alpha;

	
	
	public IssuingM1Impl() {
		super(cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM1Impl.class);
	}

	
	public CommonInfo getCommonInfo() {
		return ci;
	}


	public void setCommonInfo(CommonInfo ci) {
		this.ci = ci;
	}



	public BigInteger getAlpha() {
		return alpha;
	}

	public void setAlpha(BigInteger alpha) {
		this.alpha = alpha;
	}
	
	
	
	
	
	
	
	
public ASN1Object toASNObject(){
		
		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(((CommonInfoImpl) ci).toASNObject());
		tmp.add(new ASN1Integer(alpha));
		
		asn1Object = new DERSequence(tmp);
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
			return null;
		}
		return getValues(asn1Object);
	}
	
	public Object decode(ASN1Object asn){
		asn1Object = (ASN1Sequence) asn;
		return getValues(asn1Object);
	}
	
	private Object getValues(ASN1Sequence pbsASN){

		ci =  new CommonInfoImpl();
		ci = (CommonInfo) ((CommonInfoImpl) ci).decode((ASN1Object) asn1Object.getObjectAt(0));
		if(ci==null){return null;}
		
		alpha = ((ASN1Integer) pbsASN.getObjectAt(1)).getValue();

		return this;
	}


	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);
	}
}
