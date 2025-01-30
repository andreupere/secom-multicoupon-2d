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

import cat.uib.secom.crypto.sig.bbs.marshalling.BBSSignatureMSG;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;
import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;
import cat.uib.secom.multicoupon2d.common.msg.MCPBS;
import cat.uib.secom.multicoupon2d.common.msg.converters.CertificateConverter;


@Root(name=MessageTypesConstants.CLAIM_MESSAGE3)
public class ClaimM3Impl extends AbstractMSG {
	
	public static String TYPE = MessageTypesConstants.CLAIM_MESSAGE3;
	
	@Element(name="multicoupon-pbs")
	protected MCPBS mcpbs;
	
	@Element(name="idr")
	protected String idR;
	
	@Element(name="b-redeem-set")
	private String bRedeemSet;
	
	@Element(name="b-redeem-set-gsigned")
	private BBSSignatureMSG groupSignature;
	
	@Element(name="X509Certificate")
	@Convert(CertificateConverter.class)
	protected Certificate certMerchant; 
	
	@Element(name="signature")
	protected String signature;
	
	public ClaimM3Impl() {
		super( ClaimM3Impl.class );
	}

	
	public MCPBS getMcpbs() {
		return mcpbs;
	}


	public void setMcpbs(MCPBS mcpbs) {
		this.mcpbs = mcpbs;
	}



	public String getIdR() {
		return idR;
	}


	public void setIdR(String idR) {
		this.idR = idR;
	}
	
	public String getbRedeemSet() {
		return bRedeemSet;
	}

	public void setbRedeemSet(String bRedeemSet) {
		this.bRedeemSet = bRedeemSet;
	}

	public BBSSignatureMSG getGroupSignature() {
		return groupSignature;
	}

	public void setGroupSignature(BBSSignatureMSG groupSignature) {
		this.groupSignature = groupSignature;
	}
	
	
	public Certificate getCertMerchant() {
		return certMerchant;
	}


	public void setCertMerchant(Certificate certMerchant) {
		this.certMerchant = certMerchant;
	}


	public String getSignature() {
		return signature;
	}


	public void setSignature(String signature) {
		this.signature = signature;
	}

	
	
	// ASN1
	
	public ASN1Object toASNObject(){
		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(((MCPBSImpl) this.getMcpbs()).toASNObject());
		tmp.add(new DERGeneralString(this.getIdR()));
		tmp.add(new DERGeneralString(bRedeemSet));
		tmp.add(groupSignature.toASNObject());
		tmp.add( (ASN1Object) this.getCertMerchant() );
		tmp.add( new DERGeneralString(this.getSignature()) );
		
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
		mcpbs = new MCPBSImpl();
		((MCPBSImpl) mcpbs).decode((ASN1Object) msg.getObjectAt(0));
		idR = ((DERGeneralString) msg.getObjectAt(1) ).getString();
		bRedeemSet = ((DERGeneralString) msg.getObjectAt(2)).getString();
		groupSignature =  new BBSSignatureMSG();
		boolean result = groupSignature.decode((ASN1Object) msg.getObjectAt(3));
		certMerchant = Certificate.getInstance((ASN1Object) msg.getObjectAt(4));
		signature = ((DERGeneralString) msg.getObjectAt(5) ).getString();
		
		return true;
	}


	
		
	
}
