package cat.uib.secom.multicoupon2d.servers.merchant.vo;

import java.util.Hashtable;



/**
 * 
 * @author Andreu Pere
 * 
 * Indexed table of redeem instances. It is indexed by a hash over the multicouponPBSXML definition.
 *
 */
public class MerchantRedeemTableVO {

	private Hashtable<String, MerchantRedeemVO> data = new Hashtable<String,MerchantRedeemVO>();
	
	public MerchantRedeemTableVO() {}
	
	
	/**
	 * Search key in structure
	 * 
	 * @param key to be searched in the structure
	 * 
	 * @return MerchantRedeemVO object containing data VO
	 * 
	 * @throws NullPointerException if key is not found
	 */
	public MerchantRedeemVO lookup(String key) {
		data.containsKey(key);
		return data.get(key);
	}
	
	
	public void add(String key, MerchantRedeemVO value) {
		data.put(key, value);
	}
	
	public void remove(String key) throws NullPointerException {
		data.remove(key);
	}
	
	public void update(String key, MerchantRedeemVO value) {
		data.put(key, value);
	}
	
}
