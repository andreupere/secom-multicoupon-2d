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
import cat.uib.secom.multicoupon2d.common.msg.IssuingM2;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;

@Root(name=MessageTypesConstants.ISSUING_MESSAGE2)
public class IssuingM2Impl extends AbstractMSG implements IssuingM2 {

	
	public static String TYPE = MessageTypesConstants.ISSUING_MESSAGE2;

	
	@Element(name="lambda")
	private BigInteger lambda;
	
	@Element(name="session-id")
	private Integer sessionID;

	
	
	public IssuingM2Impl() {
		super(cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM2Impl.class);
	}

	
	public Integer getSessionID() {
		return this.sessionID;
	}

	public void setSessionID(Integer sessionID) {
		this.sessionID = sessionID;
	}
	
	
	public BigInteger getLambda() {
		return lambda;
	}


	public void setLambda(BigInteger lambda) {
		this.lambda = lambda;
	}
	
	
	
	
	
	
	
	public ASN1Object toASNObject(){
		ASN1Integer lambasn = new ASN1Integer(lambda);
		ASN1Integer sesionasn = new ASN1Integer(sessionID.intValue());
		
		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(lambasn);
		tmp.add(sesionasn);
		
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
			return false;
		}
		lambda = ((ASN1Integer) asn1Object.getObjectAt(0)).getValue();
		sessionID = (((ASN1Integer) asn1Object.getObjectAt(1)).getValue()).intValue();
		return this;
	}


	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);
	}
}
