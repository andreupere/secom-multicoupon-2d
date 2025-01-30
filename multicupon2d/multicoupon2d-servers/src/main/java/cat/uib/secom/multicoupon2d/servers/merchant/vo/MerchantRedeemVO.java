package cat.uib.secom.multicoupon2d.servers.merchant.vo;

import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupPublicKeyMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSSignatureMSG;
import cat.uib.secom.multicoupon2d.common.msg.MCPBS;
import cat.uib.secom.multicoupon2d.common.msg.impl.ClaimM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.MCPBSImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemSetCouponsImpl;


/**
 * 
 * @author Andreu Pere
 * 
 * Datastore object for merchant. It stores data related to multicoupon for each multiredeem instance
 *
 */
public class MerchantRedeemVO {
	
	/**
	 * hash of multicouponPBSXML field. It will be used for lookups
	 * */
	private String key;

	private MCPBSImpl multicouponPBS;
	
	private RedeemSetCouponsImpl aRedeemSet;
	
	private RedeemSetCouponsImpl bRedeemSet;
	
	private BBSSignatureMSG aSetGroupSigned;
	
	private BBSSignatureMSG bSetGroupSigned;
	
	private BBSGroupPublicKeyMSG groupPublicKey;
	
	private ClaimM2Impl claimM2;
	

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public MCPBSImpl getMCPBS() {
		return multicouponPBS;
	}

	public void setMCPBS(MCPBSImpl multicouponPBS) {
		this.multicouponPBS = multicouponPBS;
	}

	public RedeemSetCouponsImpl getaRedeemSet() {
		return aRedeemSet;
	}

	public void setaRedeemSet(RedeemSetCouponsImpl aRedeemSet) {
		this.aRedeemSet = aRedeemSet;
	}

	public RedeemSetCouponsImpl getbRedeemSet() {
		return bRedeemSet;
	}

	public void setbRedeemSet(RedeemSetCouponsImpl bRedeemSet) {
		this.bRedeemSet = bRedeemSet;
	}

	public BBSSignatureMSG getaSetGroupSigned() {
		return aSetGroupSigned;
	}

	public void setaSetGroupSigned(BBSSignatureMSG aSetGroupSigned) {
		this.aSetGroupSigned = aSetGroupSigned;
	}

	public BBSSignatureMSG getbSetGroupSigned() {
		return bSetGroupSigned;
	}

	public void setbSetGroupSigned(BBSSignatureMSG bSetGroupSigned) {
		this.bSetGroupSigned = bSetGroupSigned;
	}

	public BBSGroupPublicKeyMSG getGroupPublicKey() {
		return groupPublicKey;
	}

	public void setGroupPublicKey(BBSGroupPublicKeyMSG groupPublicKey) {
		this.groupPublicKey = groupPublicKey;
	}

	public ClaimM2Impl getClaimM2() {
		return claimM2;
	}

	public void setClaimM2(ClaimM2Impl claimM2) {
		this.claimM2 = claimM2;
	}
	
	
	
	
}
