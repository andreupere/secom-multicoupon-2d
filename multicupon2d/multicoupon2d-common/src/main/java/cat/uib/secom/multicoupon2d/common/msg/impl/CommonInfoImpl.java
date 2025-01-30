package cat.uib.secom.multicoupon2d.common.msg.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1GeneralizedTime;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.DLSequence;
import org.spongycastle.asn1.util.ASN1Dump;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;
import cat.uib.secom.multicoupon2d.common.msg.CommonInfo;
import cat.uib.secom.multicoupon2d.common.msg.MCDescription;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;



@Root(name=MessageTypesConstants.PBS_COMMON_INFO)
public class CommonInfoImpl extends AbstractMSG implements CommonInfo {
	
	public static String TYPE = MessageTypesConstants.PBS_COMMON_INFO;
	
	@Element(name="issuer-id")
	protected Integer issuerID;
	
	@Element(name="service-id")
	protected Integer serviceID;
	
	@ElementList(name="multicoupons-description")
	protected ArrayList<MCDescription> multicuponsDescription;
	
	@Element(name="expiration")
	protected Date expiration;
	
	@Element(name="claim")
	protected Date claim;
	
	@Element(name="refund")
	protected Date refund;
	
	

	
	public CommonInfoImpl() {
		super(cat.uib.secom.multicoupon2d.common.msg.impl.CommonInfoImpl.class);
	}
	
	public CommonInfoImpl(String issuerID,
						 String serviceID,
						 ArrayList<MCDescription> multicuponsDescription,
						 Date expiration,
						 Date claim,
						 Date refund) {
		super(cat.uib.secom.multicoupon2d.common.msg.impl.CommonInfoImpl.class);
	}


	public Integer getIssuerID() {
		return issuerID;
	}


	public void setIssuerID(Integer issuerID) {
		this.issuerID = issuerID;
	}


	public Integer getServiceID() {
		return serviceID;
	}


	public void setServiceID(Integer serviceID) {
		this.serviceID = serviceID;
	}


	public ArrayList<MCDescription> getMCDescription() {
		return multicuponsDescription;
	}


	public void setMCDescription(ArrayList<MCDescription> multicuponsDescriptionXML) {
		this.multicuponsDescription = multicuponsDescriptionXML;
	}


	public Date getExpiration() {
		return expiration;
	}


	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}


	public Date getClaim() {
		return claim;
	}


	public void setClaim(Date claim) {
		this.claim = claim;
	}


	public Date getRefund() {
		return refund;
	}


	public void setRefund(Date refund) {
		this.refund = refund;
	}


	
	
	
	
	/*******************************************************
	 * 
	 * ASN1
	 * 
	 */
	
	public ASN1Object toASNObject(){
		//DERGeneralString uno = new DERGeneralString(issuerID);
		ASN1Integer uno = new ASN1Integer(issuerID.intValue());
		ASN1Integer dos = new ASN1Integer(serviceID.intValue());
		//ASN1Integer tres = new ASN1Integer(serviceID.intValue());
		
		Iterator<MCDescription> it = this.multicuponsDescription.iterator();
		MCDescriptionImpl mctmp;
		ASN1EncodableVector mcSeq = new ASN1EncodableVector();
		while(it.hasNext()) {
			mctmp = (MCDescriptionImpl) it.next();
			mcSeq.add(mctmp.toASNObject());
		}
		
		ASN1GeneralizedTime cuatro = new ASN1GeneralizedTime(expiration);
		ASN1GeneralizedTime cinco = new ASN1GeneralizedTime(claim);
		ASN1GeneralizedTime seis = new ASN1GeneralizedTime(refund);
		
		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(uno);
		tmp.add(dos);
		tmp.add(new DERSequence(mcSeq));
		tmp.add(cuatro);
		tmp.add(cinco);
		tmp.add(seis);
		
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
		issuerID = (((ASN1Integer) asn1Object.getObjectAt(0)).getValue()).intValue();
		serviceID = (((ASN1Integer) asn1Object.getObjectAt(1)).getValue()).intValue();
		DLSequence tmp = (DLSequence) asn1Object.getObjectAt(2);
		MCDescriptionImpl mcdDescTmp;
		multicuponsDescription = new ArrayList<MCDescription>();
		int nummc = tmp.size();
		for(int i=0;i<nummc;i++){
			ASN1Object sss = (ASN1Object)tmp.getObjectAt(i);
			mcdDescTmp = new MCDescriptionImpl();
			mcdDescTmp.decode(sss);
			multicuponsDescription.add(mcdDescTmp);
		}
		
		try {
			expiration = ((ASN1GeneralizedTime) asn1Object.getObjectAt(3)).getDate();
			claim = ((ASN1GeneralizedTime) asn1Object.getObjectAt(4)).getDate();
			refund = ((ASN1GeneralizedTime) asn1Object.getObjectAt(5)).getDate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return this;
	}
	
	public Object decode(ASN1Object asn){
		asn1Object = (ASN1Sequence) asn;
		issuerID = (((ASN1Integer) asn1Object.getObjectAt(0)).getValue()).intValue();
		serviceID = (((ASN1Integer) asn1Object.getObjectAt(1)).getValue()).intValue();

		DLSequence tmp = (DLSequence) asn1Object.getObjectAt(2);
		MCDescriptionImpl mcdDescTmp;
		multicuponsDescription = new ArrayList<MCDescription>();
		int nummc = tmp.size();
		for(int i=0;i<nummc;i++){
			ASN1Object sss = (ASN1Object)tmp.getObjectAt(i);
			mcdDescTmp = new MCDescriptionImpl();
			mcdDescTmp.decode(sss);
			multicuponsDescription.add(mcdDescTmp);
		}
		try {
			expiration = ((ASN1GeneralizedTime) asn1Object.getObjectAt(3)).getDate();
			claim = ((ASN1GeneralizedTime) asn1Object.getObjectAt(4)).getDate();
			refund = ((ASN1GeneralizedTime) asn1Object.getObjectAt(5)).getDate();
		} catch(ParseException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getServiceID());
		sb.append("\r\n");
		sb.append(this.getIssuerID());
		sb.append("\r\n");
		Iterator<MCDescription> it = this.getMCDescription().iterator();
		while (it.hasNext()) {
			MCDescriptionImpl mcd = (MCDescriptionImpl) it.next();
			sb.append(mcd.toString());
			sb.append("\r\n");
		}
		//sb.append(this.getMCDescription());
		//sb.append("\r\n");
		sb.append(this.getClaim());
		sb.append("\r\n");
		sb.append(this.getRefund());
		sb.append("\r\n");
		sb.append(this.getExpiration());
		return sb.toString();
	}


}
