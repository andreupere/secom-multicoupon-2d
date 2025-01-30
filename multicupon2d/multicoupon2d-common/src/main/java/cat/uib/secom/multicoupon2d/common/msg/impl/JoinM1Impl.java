package cat.uib.secom.multicoupon2d.common.msg.impl;

import java.io.IOException;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERGeneralString;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.util.ASN1Dump;
import org.spongycastle.asn1.x509.Certificate;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;
import cat.uib.secom.multicoupon2d.common.msg.JoinM1;
import cat.uib.secom.multicoupon2d.common.msg.converters.CertificateConverter;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;


@Root(name=MessageTypesConstants.JOIN_MESSAGE1)
public class JoinM1Impl extends AbstractMSG implements JoinM1 {
	
	public static String TYPE = MessageTypesConstants.JOIN_MESSAGE1;

	@Element(name="id")
	private String id;
	
	@Element(name="user-X509Certificate")
	@Convert(CertificateConverter.class)
	private Certificate certificate;
	

	public JoinM1Impl() {
		super(cat.uib.secom.multicoupon2d.common.msg.impl.JoinM1Impl.class);
	}

	
	
	public void setCertificate(Certificate userCert) {
		this.certificate = userCert;
	}

	public Certificate getCertificate() {
		return certificate;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	
	public ASN1Object toASNObject() {
		ASN1Object cert = (ASN1Object)(this.certificate);
		DERGeneralString id = new DERGeneralString(this.id);
		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(id);
		tmp.add(cert);
		asn1Object = new DERSequence(tmp);
		return asn1Object.toASN1Primitive();
	}



	@Override
	protected byte[] encodeASN1() {
		try {
			toASNObject();
			return asn1Object.getEncoded();
		} catch(IOException e) {
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
		this.id = ((DERGeneralString) asn1Object.getObjectAt(0)).getString();
		this.certificate = Certificate.getInstance(( ASN1Object ) asn1Object.getObjectAt(1)); 
		return this;
	}



	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);

	}
	
	
	

}
