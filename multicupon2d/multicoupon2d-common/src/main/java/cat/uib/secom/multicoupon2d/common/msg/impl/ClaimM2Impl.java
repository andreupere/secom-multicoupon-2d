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

import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;
import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;

@Root(name=MessageTypesConstants.CLAIM_MESSAGE2)
public class ClaimM2Impl extends AbstractMSG {
	
	public static String TYPE = MessageTypesConstants.CLAIM_MESSAGE2;

	@Element(name="response")
	private String response;
	
	@Element(name="signature")
	private String signature;
	
	
	
	public ClaimM2Impl() {
		super(ClaimM2Impl.class);
	}
	public ClaimM2Impl(Class<?> cl) {
		super( cl );
	}
	
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getSignature() {
		return signature;
	}


	public void setSignature(String signature) {
		this.signature = signature;
	}






	public ASN1Object toASNObject() {
		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(new DERGeneralString(response));
		tmp.add(new DERGeneralString(signature));
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
			e.printStackTrace();
			return false;
		}
		getValues(asn1Object);
		return this;
	}

	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);
	}
	
	
	private boolean getValues(ASN1Sequence msg) {
		response = ((DERGeneralString) msg.getObjectAt(0) ).getString();
		signature = ((DERGeneralString) msg.getObjectAt(1) ).getString();
		return true;
	}
	
}
