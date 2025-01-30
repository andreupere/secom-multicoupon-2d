package cat.uib.secom.multicoupon2d.common;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupPublicKeyMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSSignatureMSG;
import cat.uib.secom.multicoupon2d.common.msg.impl.MCPBSImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM3Impl;
import cat.uib.secom.security.CertificateUtils;
import cat.uib.secom.utils.strings.MSGFormatConstants;

public class TestRedeemMessaging {

	private static String aRedeemSetEncrypted = "zTZLLjFcepFRokf6+3pNNTimkTPpB+lv09e/QLt/ciBDdHTH8MwNFOI3DIigJeb8gZ6qEmS51PwUPT5t3gmzpQKfKMW0wgA+Z5QS7NT+gzIrhEXR2oRft2jqGaEe/AE1DYSJA4Q7AU4jXlany4jWuOr3zlD1THHVIIP6z9G19eHXOeYtrP+J1XOiLnMT804NFZ0S1AWbiGXWtxOODhrackiYONmRHq50pSx+8mMbNPE9vwq0bFiGuwUgcBUcxmE+d8NpPSWubKwMIMh8JbYBZQqxCB15MbLkfafIC0+zSPSCJRFrh5eScY2mI3prd5zT/ZUt4DxQrcGADZKoJbjcPQ==";
	private static String certGPKFile = "/home/apaspai/development/data/cert.data";
	
	private static String g1 = "01e6b073edfeec573c23de742466411a3bf34e4501ce1eb23fff62b7e6bfc167afa8292fdcebe376b50c05ee";
	private static String g2 = "00db1c9c2aca01bb1f48004f9056e6f6479effce5adf3ea16cdb271d5932a01d6877294537960a4b348f88bc26dcb923f1a0ecc32ca6c7e3f24001951ba72ccb57d418f6ef559eda594c94d091591cfb65f6ac39cbc24b7503fdf0d162d01a6570bbff1525bf16c7453decc76b771a83e10c423a78a887544763af031c69f65a4db2d65d";
	private static String h = "2a51f435571cc9d459bb8be7043253a23a9f2c1d58851274025073f942f6e0b94db3d9dd9c6cd2f20d8edc8f";
	private static String omega = "0cdc762462fb037eba3414efbd63bae6481c1bcc775105c4d4a5223a5946dd9cd054ccb3b1928db1028d26733c8c2cf2f9b21c58971d267e248eef9921919f3c04ba171f76456bfbf6f6e4e53beef75e3da72449b348ab5031373cde7dfb963549503fb33ce7a76c8cf25303217b11d91763402b2dc4d797c2d410da4e4165dd8cd7f018";
	private static String u = "03d175aa947db2bf60b601dbd819cd64b58ec780d6ad2b64124afa9e3c8ed888dbc5e8ecfc67cc937847bf15";
	private static String v = "006efe6d71476fcd9521e60dddc880ec2b5f0b71e6ad35d0b0e27e00e81c74b59ac9f4194d874de10b3f6bc6";
	
	private static String t1 = "01da6eba024cbdd59a2526ff6739aef80e8a5f9739060c44f5a164cf04955e7f86afee6a6cf64e223e972b1a";
	private static String t2 = "3463650ec4a38e5a687fd5562d3e70706e9605ce3db0046e8ab03cab2d72c014dd09d2ebc0c6456e58a110b2";
	private static String t3 = "177b6d7eac04e1dead6bc901f883b68933dafb1f1e081b3f31d16d4ab484933badc6bc7bda3e874907fee066";
	private static String c = "2a60f1dd2c2bc55648ab9532f0ff0fee37a73ebc40";
	private static String salpha = "0dfd5aaa6cfd00084a8e2316b96c9c4f368faa30aa";
	private static String sbeta = "46930e063edb23529b25adf17fa78c599c73ca977f";
	private static String sdelta1 = "482c3a5632af7401ef917fd83ee8ff3326d199151c";
	private static String sdelta2 = "39838f68d67e43f57975ad883c57bf1639b265c449";
	private static String sx = "2f4749232377bcc619d9a45823d37e7e36de5097bc";
	
	protected static RedeemM1Impl rm1;
	
	
	
	
	private static String serviceEncrypted = "DEoEV8AFI9B/zNVWGpSqO0y3p9i+ygJjkfoTlhbwFXQ=";
	private static String merchantSignature = "merchant_signature";
	
	protected static RedeemM2Impl rm2;
	
	
	
	
	private static String bRedeemSetEncrypted = "PtC61WVM9ryeAIvAqqSuqNrJyDAFf14SYMFsbWOmyqj4PTokv38KrUD4tbKDM4dfAoj4groDBBEGZPIgPqasBh5nve0uoi+OHByATvE/+X/ufYYBjcM5WZRCaMcasXo36jJdb/CN049KwsEMozyvxBwQRCMaAV0CaxrZHx8s7QB/ve/jij7dCaDY+BRRaR6W6C7e3qKm7rsF66yEmiU9oyZ0Yrn2sjlRv5Y9JOeubFQGXICC9PZV2OJQUXmUACnoJtbeySavRyo8k5HBSuRIGo8vETQ8lvSqr7TQJi9Heu+qxo5TPm1wBuU/II9g9A+2fZ6iTETxWNPk5uwVFzooxA==";
	private static String t1m3 = "1cb19f851d34bcd1c0a688aabfb289bf74d7a54f5c4b3c5c0141fc807cf7dcd7d4290909a97091929eec97c7";
	private static String t2m3 = "1c75d63e80edf5ab1632d143e45968afdf896161a29b2c29f5c4510fed0d57bc12978a3ae785e083e2793423";
	private static String t3m3 = "205a10b239e846ff5049cb185d2e0114a7ff3b3e1f8138b384c0aaa924fe47a67591371be40ed3f91f2e5d2b";
	private static String cm3 = "0b34fd387579633c9517e9e7fcc69f0d017d3dfe00";
	private static String salpham3 = "32de502940c3d072eb7b7d05ccbf75d414d5c60bf6";
	private static String sbetam3 = "490825d6f9ec3b247c0bed16fc9604a91c1fe0b32c";
	private static String sxm3 = "495a6dd26dc9c399377b714c0265b26996c58016bf";
	private static String sdelta1m3 = "01d01a7f0690c974a28c1824318a315f2875078090";
	private static String sdelta2m3 = "3a05ed3bf9720b5784d6b3a0118381e076608d764b";
	
	protected static RedeemM3Impl rm3;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	
	@BeforeClass
	public static void init() throws Exception {
		MCPBSImpl mcpbs = (MCPBSImpl) MockObjects.createMCPBS();
		
		BBSGroupPublicKeyMSG gpk = new BBSGroupPublicKeyMSG();
		gpk.setG1(g1);
		gpk.setG2(g2);
		gpk.setH(h);
		gpk.setOmega(omega);
		gpk.setU(u);
		gpk.setV(v);
		
		BBSSignatureMSG signature = new BBSSignatureMSG();
		signature.setT1(t1);
		signature.setT2(t2);
		signature.setT3(t3);
		signature.setC(c);
		signature.setSalpha(salpha);
		signature.setSbeta(sbeta);
		signature.setSdelta1(sdelta1);
		signature.setSdelta2(sdelta2);
		signature.setSx(sx);
		
		rm1 = new RedeemM1Impl();
		rm1.setaRedeemSet(aRedeemSetEncrypted);
		rm1.setCertGPK( CertificateUtils.getCertX509( certGPKFile ) );
		//rm1.setGroupPublicKey(gpk);
		rm1.setGroupSignature(signature);
		rm1.setMCPBS(mcpbs);
		
		
		rm2 = new RedeemM2Impl();
		rm2.setMerchantSignature(merchantSignature);
		rm2.setServiceEncrypted(serviceEncrypted);
		
		
		rm3 = new RedeemM3Impl();
		rm3.setBRedeemSet(bRedeemSetEncrypted);
		BBSSignatureMSG signature2 = new BBSSignatureMSG();
		signature2.setT1(t1m3);
		signature2.setT2(t2m3);
		signature2.setT3(t3m3);
		signature2.setC(cm3);
		signature2.setSalpha(salpham3);
		signature2.setSbeta(sbetam3);
		signature2.setSdelta1(sdelta1m3);
		signature2.setSdelta2(sdelta2m3);
		signature2.setSx(sxm3);
		rm3.setGroupSignature(signature2);
		
	}
	
	@Test
	public void serialize() throws Exception {
		rm1.setMSGFormat(MSGFormatConstants.XML);
		rm2.setMSGFormat(MSGFormatConstants.XML);
		rm3.setMSGFormat(MSGFormatConstants.XML);
		s(MSGFormatConstants.XML);
		
		rm1.setMSGFormat(MSGFormatConstants.ASN1);
		rm2.setMSGFormat(MSGFormatConstants.ASN1);
		rm3.setMSGFormat(MSGFormatConstants.ASN1);
		s(MSGFormatConstants.ASN1);
	}
	
	@Ignore
	private void s(MSGFormatConstants msgFormat) throws Exception {
		System.out.println("##### Test format=" + msgFormat);
		
		// Message 1 from Redeem
		byte[] b = rm1.serialize(msgFormat);
		RedeemM1Impl rm1r = new RedeemM1Impl();
		rm1r = (RedeemM1Impl) rm1r.deSerialize(b, msgFormat);
		collector.checkThat("aRedeemSet", rm1r.getaRedeemSet(), CoreMatchers.is(rm1.getaRedeemSet()));
		collector.checkThat("certificate", rm1r.getCertGPK(), CoreMatchers.is(rm1.getCertGPK()));
		//collector.checkThat("g1", rm1r.getGroupPublicKey().getG1(), CoreMatchers.is(rm1.getGroupPublicKey().getG1()));
		collector.checkThat("sdelta1", rm1r.getGroupSignature().getSdelta1(), CoreMatchers.is(rm1.getGroupSignature().getSdelta1()));
		collector.checkThat("delta", rm1r.getMCPBS().getDelta(), CoreMatchers.is(rm1.getMCPBS().getDelta()));
		collector.checkThat("hash", rm1r.getMCPBS().getRootCoupons().get(0).getHash().toString(), CoreMatchers.is(rm1.getMCPBS().getRootCoupons().get(0).getHash().toString()));
		collector.checkThat("value", rm1r.getMCPBS().getCommonInfo().getMCDescription().get(0).getValue(), CoreMatchers.is(rm1.getMCPBS().getCommonInfo().getMCDescription().get(0).getValue()));

		
		// Message 2 from Redeem
		b = rm2.serialize(msgFormat);
		RedeemM2Impl rm2r = new RedeemM2Impl();
		rm2r = (RedeemM2Impl) rm2r.deSerialize(b, msgFormat);
		collector.checkThat("merchantsignature", rm2r.getMerchantSignature(), CoreMatchers.is(rm2.getMerchantSignature()));
		collector.checkThat("serviceEncrypted", rm2r.getServiceEncrypted(), CoreMatchers.is(rm2.getServiceEncrypted()));
		
		
		
		// Message 3 from Redeem
		b = rm3.serialize(msgFormat);
		RedeemM3Impl rm3r = new RedeemM3Impl();
		rm3r = (RedeemM3Impl) rm3r.deSerialize(b, msgFormat);
		collector.checkThat("bredeemset", rm3r.getBRedeemSet(), CoreMatchers.is(rm3.getBRedeemSet()));
		collector.checkThat("t1", rm3r.getGroupSignature().getT1(), CoreMatchers.is(rm3.getGroupSignature().getT1()));
		collector.checkThat("t2", rm3r.getGroupSignature().getT2(), CoreMatchers.is(rm3.getGroupSignature().getT2()));
		collector.checkThat("sdelta1", rm3r.getGroupSignature().getSdelta1(), CoreMatchers.is(rm3.getGroupSignature().getSdelta1()));

		
		System.out.println("Test finished...");
		
	}
	
	
}
