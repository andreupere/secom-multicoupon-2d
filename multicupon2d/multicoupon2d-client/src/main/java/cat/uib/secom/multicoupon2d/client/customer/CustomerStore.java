package cat.uib.secom.multicoupon2d.client.customer;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;

import javax.crypto.SecretKey;

import org.spongycastle.asn1.x509.Certificate;

import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKeyImpl;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKeyImpl;
import cat.uib.secom.multicoupon2d.common.dao.Multicoupon2D;
import cat.uib.secom.multicoupon2d.common.msg.CommonInfo;
import cat.uib.secom.multicoupon2d.common.msg.Coupon;
import cat.uib.secom.multicoupon2d.common.msg.MCPBS;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM4Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.JoinM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.JoinM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM4Impl;

public class CustomerStore {
	
	
	/********************************************************************
	 * 
	 * Messages exchanged during the protocol
	 * 
	 ********************************************************************/
	
	protected IssuingM1Impl issuingM1;
	
	protected IssuingM2Impl issuingM2;
	
	protected IssuingM3Impl issuingM3;
	
	protected IssuingM4Impl issuingM4;
	
	protected JoinM1Impl joinM1;
	
	protected JoinM2Impl joinM2;
	
	protected RedeemM1Impl redeemM1;
	
	protected RedeemM2Impl redeemM2;
	
	protected RedeemM3Impl redeemM3;
	
	protected RedeemM4Impl redeemM4;
	
	
	/**********************************************************************
	 * 
	 * Security and initialization parameters
	 * 
	 **********************************************************************/
	
	//protected BigInteger			rsaNIssuer;
	
	//protected BigInteger 			rsaEIssuer;
	
	protected BigInteger 			q;
	
	protected RSAPublicKey			rsaIssuerPubKey;
	
	
	
	/**********************************************************************
	 * 
	 * Permanent parameters involved in the protocol
	 * 
	 **********************************************************************/
	
	protected Multicoupon2D 		m2d; 
	
	protected CommonInfo 			commonInfo;
	
	protected MCPBS 				mcpbs;
	
	protected ArrayList<Coupon> 	roots;
	
	protected SecretKey 			ks;
	
	protected String 				keyStoreFName;
	
	protected String 				keyStorePwd;
	
	protected String 				keyAlias;

	protected BBSGroupPublicKeyImpl	gpk;
	
	protected BBSUserPrivateKeyImpl	usk;
	
	protected Certificate 			certGPKASN1;
	
	protected Certificate			certUserASN1;
		
	protected PublicKey				rsaPubKey;
	
	protected PrivateKey			rsaPrivKey;
	
	protected RSAPublicKey			rsaMerchantPubKey;


	public CustomerStore() {}
	

	public IssuingM1Impl getIssuingM1() {
		return issuingM1;
	}

	public void setIssuingM1(IssuingM1Impl issuingM1) {
		this.issuingM1 = issuingM1;
	}

	public IssuingM2Impl getIssuingM2() {
		return issuingM2;
	}

	public void setIssuingM2(IssuingM2Impl issuingM2) {
		this.issuingM2 = issuingM2;
	}

	public IssuingM3Impl getIssuingM3() {
		return issuingM3;
	}

	public void setIssuingM3(IssuingM3Impl issuingM3) {
		this.issuingM3 = issuingM3;
	}

	public IssuingM4Impl getIssuingM4() {
		return issuingM4;
	}

	public void setIssuingM4(IssuingM4Impl issuingM4) {
		this.issuingM4 = issuingM4;
	}

	public JoinM1Impl getJoinM1() {
		return joinM1;
	}

	public void setJoinM1(JoinM1Impl joinM1) {
		this.joinM1 = joinM1;
	}

	public JoinM2Impl getJoinM2() {
		return joinM2;
	}

	public void setJoinM2(JoinM2Impl joinM2) {
		this.joinM2 = joinM2;
	}

	public RedeemM1Impl getRedeemM1() {
		return redeemM1;
	}

	public void setRedeemM1(RedeemM1Impl redeemM1) {
		this.redeemM1 = redeemM1;
	}

	public RedeemM2Impl getRedeemM2() {
		return redeemM2;
	}

	public void setRedeemM2(RedeemM2Impl redeemM2) {
		this.redeemM2 = redeemM2;
	}

	public RedeemM3Impl getRedeemM3() {
		return redeemM3;
	}

	public void setRedeemM3(RedeemM3Impl redeemM3) {
		this.redeemM3 = redeemM3;
	}

	public RedeemM4Impl getRedeemM4() {
		return redeemM4;
	}

	public void setRedeemM4(RedeemM4Impl redeemM4) {
		this.redeemM4 = redeemM4;
	}


//	public BigInteger getRsaNIssuer() {
//		return rsaNIssuer;
//	}
//
//
//	public void setRsaNIssuer(BigInteger rsaNIssuer) {
//		this.rsaNIssuer = rsaNIssuer;
//	}
//
//
//	public BigInteger getRsaEIssuer() {
//		return rsaEIssuer;
//	}
//
//
//	public void setRsaEIssuer(BigInteger rsaEIssuer) {
//		this.rsaEIssuer = rsaEIssuer;
//	}


	public BigInteger getQ() {
		return q;
	}


	public void setQ(BigInteger q) {
		this.q = q;
	}


	public RSAPublicKey getRsaIssuerPubKey() {
		return rsaIssuerPubKey;
	}


	public void setRsaIssuerPubKey(RSAPublicKey rsaIssuerPubKey) {
		this.rsaIssuerPubKey = rsaIssuerPubKey;
	}


	public Multicoupon2D getM2d() {
		return m2d;
	}


	public void setM2d(Multicoupon2D m2d) {
		this.m2d = m2d;
	}


	public CommonInfo getCommonInfo() {
		return commonInfo;
	}


	public void setCommonInfo(CommonInfo commonInfo) {
		this.commonInfo = commonInfo;
	}


	public MCPBS getMcpbs() {
		return mcpbs;
	}


	public void setMcpbs(MCPBS mcpbs) {
		this.mcpbs = mcpbs;
	}


	public SecretKey getKS() {
		return ks;
	}


	public void setKS(SecretKey ks) {
		this.ks = ks;
	}


	public String getKeyStoreFName() {
		return keyStoreFName;
	}


	public void setKeyStoreFName(String keyStoreFName) {
		this.keyStoreFName = keyStoreFName;
	}


	public String getKeyStorePwd() {
		return keyStorePwd;
	}


	public void setKeyStorePwd(String keyStorePwd) {
		this.keyStorePwd = keyStorePwd;
	}


	public String getKeyAlias() {
		return keyAlias;
	}


	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}


	public BBSGroupPublicKeyImpl getGpk() {
		return gpk;
	}


	public void setGpk(BBSGroupPublicKeyImpl gpk) {
		this.gpk = gpk;
	}


	public BBSUserPrivateKeyImpl getUsk() {
		return usk;
	}


	public void setUsk(BBSUserPrivateKeyImpl usk) {
		this.usk = usk;
	}


	public Certificate getCertGPKASN1() {
		return certGPKASN1;
	}


	public void setCertGPKASN1(Certificate certGPKASN1) {
		this.certGPKASN1 = certGPKASN1;
	}


	public ArrayList<Coupon> getRoots() {
		return roots;
	}


	public void setRoots(ArrayList<Coupon> roots) {
		this.roots = roots;
	}
	
	
	public Certificate getCertUserASN1() {
		return certUserASN1;
	}


	public void setCertUserASN1(Certificate certUserASN1) {
		this.certUserASN1 = certUserASN1;
	}


	public PublicKey getRsaPubKey() {
		return rsaPubKey;
	}


	public void setRsaPubKey(PublicKey rsaPubKey) {
		this.rsaPubKey = rsaPubKey;
	}


	public PrivateKey getRsaPrivKey() {
		return rsaPrivKey;
	}


	public void setRsaPrivKey(PrivateKey rsaPrivKey) {
		this.rsaPrivKey = rsaPrivKey;
	}
	
	

	
	
	

}
