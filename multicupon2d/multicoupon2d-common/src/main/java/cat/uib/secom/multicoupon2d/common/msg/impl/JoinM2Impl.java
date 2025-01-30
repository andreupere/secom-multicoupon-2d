package cat.uib.secom.multicoupon2d.common.msg.impl;

import java.io.IOException;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.util.ASN1Dump;
import org.spongycastle.asn1.x509.Certificate;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import cat.uib.secom.crypto.sig.bbs.marshalling.BBSUserPrivateKeyMSG;
import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;
import cat.uib.secom.multicoupon2d.common.msg.JoinM2;
import cat.uib.secom.multicoupon2d.common.msg.converters.CertificateConverter;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;


@Root(name=MessageTypesConstants.JOIN_MESSAGE2)
public class JoinM2Impl extends AbstractMSG implements JoinM2 {
	
	public static String TYPE = MessageTypesConstants.JOIN_MESSAGE2;
	
	public JoinM2Impl() {
		super(JoinM2Impl.class);
	}

	@Element(name="X509Certificate")
	@Convert(CertificateConverter.class)
	private Certificate gpkCertificate;
	
	@Element(name="usk")
	private BBSUserPrivateKeyMSG userPrivateKey;
	
	

	@Override
	public BBSUserPrivateKeyMSG getBBSUserPrivateKeyMSG() {
		return this.userPrivateKey;
	}

	@Override
	public void setBBSUserPrivateKeyMSG(BBSUserPrivateKeyMSG gpk) {
		this.userPrivateKey = gpk;
		
	}

	@Override
	public Certificate getBBSGroupPublicKeyCertificate() {
		return this.gpkCertificate;
	}

	@Override
	public void setBBSGroupPublicKeyCertificate(Certificate gpkCertificate) {
		this.gpkCertificate = gpkCertificate;
	}

	
	
	
	
	/**
	 * ANS1 ENCODING/DECODING LOGIC
	 * */
	
	public ASN1Object toASN1Object() {
		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add( ((BBSUserPrivateKeyMSG) userPrivateKey).toASNObject() );
		tmp.add( (ASN1Object) gpkCertificate );
		
		asn1Object = new DERSequence(tmp);
		return asn1Object.toASN1Primitive();
	}
	
	@Override
	protected byte[] encodeASN1() {
		try {
			toASN1Object();
			return asn1Object.getEncoded();
		} catch(IOException e) {
			return null;
		}
	}
	
	public boolean decode(ASN1Object asn) {
		asn1Object = (ASN1Sequence) asn;
		return getValues(asn1Object);
	}

	@Override
	protected Object decodeASN1(byte[] der) {
		try {
			asn1Object = (ASN1Sequence) ASN1Primitive.fromByteArray(der);
		} catch(IOException e) {
			return null;
		}
		getValues(asn1Object);
		
		return this;
	}
	
	private boolean getValues(ASN1Sequence asn) {
		
		userPrivateKey = new BBSUserPrivateKeyMSG();
		//groupKeyPair.decode( (ASN1Object) join2ASN.getObjectAt(0));
		userPrivateKey.decode((ASN1Object) asn.getObjectAt(0));
		gpkCertificate = Certificate.getInstance( asn.getObjectAt(1) );
		
		return true;
	}
	

	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);	
	}

}
