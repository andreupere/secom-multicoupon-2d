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
import cat.uib.secom.multicoupon2d.common.msg.IssuingM4;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;

@Root(name=MessageTypesConstants.ISSUING_MESSAGE4)
public class IssuingM4Impl extends AbstractMSG implements IssuingM4 {

	public static String TYPE = MessageTypesConstants.ISSUING_MESSAGE4;

	
	@Element(name="varphi")
	private BigInteger varphi;
	
	@Element(name="gamma")
	private BigInteger gamma;
	
	@Element(name="session-id")
	private Integer sessionID;
	


	public IssuingM4Impl() {
		super(cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM4Impl.class);
	}

	
	
	
	public BigInteger getVarphi() {
		return varphi;
	}

	public void setVarphi(BigInteger varphi) {
		this.varphi = varphi;
	}




	public BigInteger getGamma() {
		return gamma;
	}

	public void setGamma(BigInteger gamma) {
		this.gamma = gamma;
	}



	public Integer getSessionID() {
		return sessionID;
	}

	public void setSessionID(Integer sessionID) {
		this.sessionID = sessionID;
	}

	
	
	
	public ASN1Object toASNObject(){
		ASN1Integer varasn = new ASN1Integer(varphi);
		ASN1Integer gamasn = new ASN1Integer(gamma);
		ASN1Integer sesionasn = new ASN1Integer(sessionID.intValue());
		
		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(varasn);
		tmp.add(gamasn);
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
		varphi = ((ASN1Integer) asn1Object.getObjectAt(0)).getValue();
		gamma = ((ASN1Integer) asn1Object.getObjectAt(1)).getValue();
		sessionID = (((ASN1Integer) asn1Object.getObjectAt(2)).getValue()).intValue();
		return this;
	}




	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);
	}
	
}
