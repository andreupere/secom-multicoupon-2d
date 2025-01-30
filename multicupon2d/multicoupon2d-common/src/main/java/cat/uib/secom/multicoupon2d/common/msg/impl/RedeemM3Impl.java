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

import cat.uib.secom.crypto.sig.bbs.marshalling.BBSSignatureMSG;
import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;
import cat.uib.secom.multicoupon2d.common.msg.RedeemM3;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;

@Root(name=MessageTypesConstants.MULTIREDEEM_MESSAGE3)
public class RedeemM3Impl extends AbstractMSG implements RedeemM3 {

	public static String TYPE = MessageTypesConstants.MULTIREDEEM_MESSAGE3;

	
	@Element(name="b-redeem-set-encrypt")
	private String bRedeemSet;
	
	@Element(name="b-reedem-set-gsigned")
	private BBSSignatureMSG groupSignature;
	
	@Element(name="idr")
	private String idR;
	
	

	
	/* previously sent in M1
	 * 
	@Element(name="gmanager-cert")
	private String certificateGroupManager;
	
	@Element(name="group-public-key")
	private BBSGroupPublicKeyMSG groupPublicKey;
	
	@Element(name="multicoupon-pbs")
	private MCPBS multicouponPBS;
	*/
	
	
	public RedeemM3Impl() {
		super(RedeemM3Impl.class);
	}


	@Override
	public String getBRedeemSet() {
		return bRedeemSet;
	}


	@Override
	public void setBRedeemSet(String bRedeemSet) {
		this.bRedeemSet = bRedeemSet;		
	}


	@Override
	public BBSSignatureMSG getGroupSignature() {
		return groupSignature;
	}


	@Override
	public void setGroupSignature(BBSSignatureMSG groupSignature) {
		this.groupSignature = groupSignature;
	}


	@Override
	public String getIdR() {
		return this.idR;
	}

	@Override
	public void setIdR(String idR) {
		this.idR = idR;
		
	}
	
	
	public ASN1Object toASNObject(){

		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(new DERGeneralString(bRedeemSet));
		tmp.add(groupSignature.toASNObject());
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
	
	private boolean getValues(ASN1Sequence m3redeemASN){
		
		bRedeemSet = ((DERGeneralString) m3redeemASN.getObjectAt(0)).getString();
		
		groupSignature =  new BBSSignatureMSG();
		boolean result = groupSignature.decode((ASN1Object) m3redeemASN.getObjectAt(1));
		idR  = ((DERGeneralString) m3redeemASN.getObjectAt(2) ).getString();
		if(!result){return false;}

		return true;
	}


	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);

	}

}
