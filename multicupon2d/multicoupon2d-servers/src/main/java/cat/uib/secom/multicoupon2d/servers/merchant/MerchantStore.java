package cat.uib.secom.multicoupon2d.servers.merchant;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.SecretKey;

import org.spongycastle.asn1.x509.Certificate;

import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM4Impl;

public class MerchantStore {
	
	
	//protected BigInteger rsaNIssuer;
	
	//protected BigInteger rsaEIssuer;
	
	protected String keyStoreFName;
	
	protected String keyStorePwd;
	
	protected String keyAlias;
	
	protected SecretKey ks;
	
	protected RedeemM1Impl redeemM1;
	
	protected RedeemM2Impl redeemM2;
	
	protected RedeemM3Impl redeemM3;
	
	protected RedeemM4Impl redeemM4;
	
	protected Certificate certMerchantASN1;
	
	protected PrivateKey rsaPrivKey;
	
	protected PublicKey rsaPubKey;
	
	protected Certificate certIssuerASN1;
	
	protected RSAPublicKey rsaIssuerPubKey;
	
	protected boolean onlineVerification;
	
	protected String issuerHost;
	
	protected Integer issuerPort;


	
	
	public MerchantStore() {}



//	public BigInteger getRsaNIssuer() {
//		return rsaNIssuer;
//	}
//
//
//
//	public void setRsaNIssuer(BigInteger rsaNIssuer) {
//		this.rsaNIssuer = rsaNIssuer;
//	}
//
//
//
//	public BigInteger getRsaEIssuer() {
//		return rsaEIssuer;
//	}
//
//
//
//	public void setRsaEIssuer(BigInteger rsaEIssuer) {
//		this.rsaEIssuer = rsaEIssuer;
//	}



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



	public SecretKey getKs() {
		return ks;
	}



	public void setKs(SecretKey ks) {
		this.ks = ks;
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



	public Certificate getCertMerchantASN1() {
		return certMerchantASN1;
	}



	public void setCertMerchantASN1(Certificate certMerchantASN1) {
		this.certMerchantASN1 = certMerchantASN1;
	}



	public PrivateKey getRsaPrivKey() {
		return rsaPrivKey;
	}



	public void setRsaPrivKey(PrivateKey rsaPrivKey) {
		this.rsaPrivKey = rsaPrivKey;
	}



	public PublicKey getRsaPubKey() {
		return rsaPubKey;
	}



	public void setRsaPubKey(PublicKey rsaPubKey) {
		this.rsaPubKey = rsaPubKey;
	}
	
	
	
	
	
}
