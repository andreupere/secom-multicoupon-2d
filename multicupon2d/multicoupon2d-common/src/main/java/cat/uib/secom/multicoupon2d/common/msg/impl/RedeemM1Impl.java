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
import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;
import cat.uib.secom.multicoupon2d.common.msg.MCPBS;
import cat.uib.secom.multicoupon2d.common.msg.RedeemM1;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;
import cat.uib.secom.multicoupon2d.common.msg.converters.CertificateConverter;

@Root(name=MessageTypesConstants.MULTIREDEEM_MESSAGE1) 
public class RedeemM1Impl extends AbstractMSG implements RedeemM1 { 

	public static String TYPE = MessageTypesConstants.MULTIREDEEM_MESSAGE1;
	
	@Element(name="multicoupon-pbs")
	private MCPBS mcpbs;
	
	@Element(name="a-redeem-set-encrypt")
	private String aRedeemSet;
	
	@Element(name="idr")
	private String idR;
	
	@Element(name="a-redeem-set-gsigned")
	private BBSSignatureMSG groupSignature;
	
//	@Element(name="group-public-key")
//	private BBSGroupPublicKeyMSG groupPublicKey;
	
	@Element(name="X509Certificate")
	@Convert(CertificateConverter.class)
	private Certificate certGPK; 
	
	
	public RedeemM1Impl() {
		super(cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM1Impl.class);
	}

	public MCPBS getMCPBS() {
		return mcpbs;
	}

	public void setMCPBS(MCPBS multicouponPBS) {
		this.mcpbs = multicouponPBS;
	}

	public String getaRedeemSet() {
		return aRedeemSet;
	}

	public void setaRedeemSet(String aRedeemSet) {
		this.aRedeemSet = aRedeemSet;
	}
	
	public void setIdR(String idR) {
		this.idR = idR;
	}
	
	public String getIdR() {
		return this.idR;
	}

	public BBSSignatureMSG getGroupSignature() {
		return groupSignature;
	}

	public void setGroupSignature(BBSSignatureMSG groupSignature) {
		this.groupSignature = groupSignature;
	}

//	public String getCertificateGroupManager() {
//		return certificateGroupManager;
//	}
//	
//	public void setCertificateGroupManager(String certificateGroupManager) {
//		this.certificateGroupManager = certificateGroupManager;
//	}

//	public void setGroupPublicKey(BBSGroupPublicKeyMSG groupPublicKey) {
//		this.groupPublicKey = groupPublicKey;
//	}
//
//	public BBSGroupPublicKeyMSG getGroupPublicKey() {
//		return groupPublicKey;
//	}
	
	
	public void setCertGPK(Certificate certGPK) {
		this.certGPK = certGPK;
	}

	public Certificate getCertGPK() {
		return certGPK;
	}

	
	
	// ANS1
	
	
	public ASN1Object toASNObject(){

		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(((MCPBSImpl) mcpbs).toASNObject());
		tmp.add(new DERGeneralString(aRedeemSet));
		tmp.add(groupSignature.toASNObject());
		//tmp.add(groupPublicKey.toASNObject());
		//tmp.add(new DERGeneralString(certificateGroupManager));
		tmp.add( (ASN1Object) certGPK );
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
	
	public boolean decode(ASN1Object asn){
		asn1Object = (ASN1Sequence) asn;
		return getValues(asn1Object);
	}
	
	private boolean getValues(ASN1Sequence m1redeemASN){

		mcpbs =  new MCPBSImpl();
		((MCPBSImpl) mcpbs).decode((ASN1Object) m1redeemASN.getObjectAt(0));
		
		
		aRedeemSet = ((DERGeneralString) m1redeemASN.getObjectAt(1)).getString();
		
		groupSignature =  new BBSSignatureMSG();
		boolean result = groupSignature.decode((ASN1Object) m1redeemASN.getObjectAt(2));
		
		
		if(!result)
			return false;


		//groupPublicKey = new BBSGroupPublicKeyMSG();
		//result = groupPublicKey.decode((ASN1Object) m1redeemASN.getObjectAt(3) );
		
		certGPK = Certificate.getInstance((ASN1Object) m1redeemASN.getObjectAt(3));
        
        //certGPK.getSubjectPublicKeyInfo();


		//groupPublicKey =  new BBSGroupPublicKeyMSG();
		//groupPublicKey.decode((ASN1Object) m1redeemASN.getObjectAt(3));
		
		//certificateGroupManager = ((DERGeneralString) m1redeemASN.getObjectAt(4) ).getString();
		
		idR = ((DERGeneralString) m1redeemASN.getObjectAt(4) ).getString();

		return true;
	}

	@Override
	protected String dump() throws Exception { 
		return ASN1Dump.dumpAsString(asn1Object);

	}
	
}
