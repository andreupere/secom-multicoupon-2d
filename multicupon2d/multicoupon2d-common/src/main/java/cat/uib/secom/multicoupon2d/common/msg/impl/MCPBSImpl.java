package cat.uib.secom.multicoupon2d.common.msg.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import org.spongycastle.asn1.ASN1EncodableVector;
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
import cat.uib.secom.multicoupon2d.common.msg.Coupon;
import cat.uib.secom.multicoupon2d.common.msg.MCPBS;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;
import cat.uib.secom.security.HashUtils;
import cat.uib.secom.utils.strings.MSGFormatConstants;

@Root(name=MessageTypesConstants.MULTICOUPON_PBS)
public class MCPBSImpl extends AbstractMSG implements MCPBS {

	
	public static String TYPE = MessageTypesConstants.MULTICOUPON_PBS;
	
	public static String fNAME = "mcpbs";

	
	@Element(name="common-info")
	protected CommonInfo commonInfo;
	
	@Element(name="delta")
	protected BigInteger delta;
	
	@Element(name="omega")
	protected BigInteger omega;
	
	@ElementList(name="root-coupons")
	protected ArrayList<Coupon> rootCoupons; 
	
	
	
	
	public MCPBSImpl() {
		super(cat.uib.secom.multicoupon2d.common.msg.impl.MCPBSImpl.class);
	}





	public CommonInfo getCommonInfo() {
		return commonInfo;
	}


	public void setCommonInfo(CommonInfo commonInfoXML) {
		this.commonInfo = commonInfoXML;
	}


	public BigInteger getDelta() {
		return delta;
	}


	public void setDelta(BigInteger delta) {
		this.delta = delta;
	}


	public BigInteger getOmega() {
		return omega;
	}


	public void setOmega(BigInteger omega) {
		this.omega = omega;
	}


	public ArrayList<Coupon> getRootCoupons() {
		return rootCoupons;
	}


	public void setRootCoupons(ArrayList<Coupon> rootCouponsXML) {
		this.rootCoupons = rootCouponsXML;
	}



	
	
	
	public boolean verify(BigInteger rsaEIssuer, BigInteger rsaNIssuer, MSGFormatConstants msgFormat) throws Exception {
		// Verificam la signatura de C*
				
		String roots = concatenateRootCoupons();
		BigInteger checkCoin1 = this.getOmega().modPow(rsaEIssuer, rsaNIssuer);
		CommonInfoImpl ci = (CommonInfoImpl) this.getCommonInfo();
		//System.out.println("verify PBS ci: " + Hash.getHash(ci.dump(msgFormat))); 
		//System.out.println("PBS:" + Hash.getHash(this.dump(msgFormat)));
		//System.out.println("roots: " + roots);
		
		// Per verificar PBS tant amb ASN1 com amb XML, necessit que CommonInfo sigui igual a l'entrada de getHash()
		// El format que entrega dump() no és el mateix per XML o ASN1, per tant, necessit adequar-lo
		// Per tant, el que se m'acud és fer una concatenació de tots els paràmetres que duu CommonInfo, i despres passar-lo a getHash()
		
		
		
		
		BigInteger checkCoin2 = HashUtils.getHash(ci.toString()).mod(rsaNIssuer);
		//BigInteger checkCoin2 = Hash.getHash((ci).dump(msgFormat)).mod(rsaNIssuer);
		//BigInteger checkCoin2 = Hash.getHash(((CommonInfoImpl)(this.getCommonInfo())).toString(msgFormat)).mod(rsaNIssuer);
		checkCoin2 = checkCoin2.multiply(HashUtils.getHash(roots).modPow(new BigInteger("2"), rsaNIssuer)).mod(rsaNIssuer);
		checkCoin2 = checkCoin2.multiply(
				this.getDelta().modPow(new BigInteger("2"), rsaNIssuer).add(BigInteger.ONE).mod(rsaNIssuer)
				.modPow(new BigInteger("2"), rsaNIssuer)
				).mod(rsaNIssuer);
		
		
		
		
		if (!checkCoin1.equals(checkCoin2)) {
			throw new Exception("Signature verification fails...");
			//return false;
		}
		return true;
	}
	
	
	protected String concatenateRootCoupons() {
		String roots = "";
		
		int J = getRootCoupons().size();
		int j = 0;
		while (j <= J-1) {
			//roots += _m2dXML.getCoupon(j, 0).getHash().toString(16);
			roots += " " + getRootCoupons().get(j).getHash();
			j++;
		}
		return roots;
	}

	
	
	
	
	
	
	
	public ASN1Object toASNObject(){
		//DERGeneralString uno = new DERGeneralString(issuerID);
		ASN1Object uno = ((CommonInfoImpl) commonInfo).toASNObject();
		ASN1Integer dos = new ASN1Integer(delta);
		ASN1Integer tres = new ASN1Integer(omega);
		
		Iterator<Coupon> it = this.rootCoupons.iterator();
		CouponImpl ctmp;
		ASN1EncodableVector rcSeq = new ASN1EncodableVector();
		while(it.hasNext()) {
			ctmp = (CouponImpl) it.next();
			rcSeq.add(ctmp.toASNObject());
		}
		
		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(uno);
		tmp.add(dos);
		tmp.add(tres);
		tmp.add(new DERSequence(rcSeq));
		
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
		return getValues(asn1Object);
	}
	
	public Object decode(ASN1Object asn){
		asn1Object = (ASN1Sequence) asn;
		return getValues(asn1Object);
	}
	
	private Object getValues(ASN1Sequence pbsASN){
		ASN1Object ASNci = (ASN1Object) pbsASN.getObjectAt(0);
		commonInfo =  new CommonInfoImpl();
		commonInfo = (CommonInfo) ((CommonInfoImpl) commonInfo).decode(ASNci);
		if(commonInfo == null){return false;}
		
		delta = ((ASN1Integer) pbsASN.getObjectAt(1)).getValue();
		omega = ((ASN1Integer) pbsASN.getObjectAt(2)).getValue();
		DLSequence tmp = (DLSequence) pbsASN.getObjectAt(3);
		CouponImpl cTmp;
		rootCoupons = new ArrayList<Coupon>();
		int nummc = tmp.size();
		for(int i=0;i<nummc;i++){
			ASN1Object sss = (ASN1Object)tmp.getObjectAt(i);
			cTmp = new CouponImpl();
			cTmp.decode(sss);
			rootCoupons.add(cTmp);
		}
		return this;
	}





	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);
		
	}
	
}
