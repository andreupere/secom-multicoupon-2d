package cat.uib.secom.multicoupon2d.test.integration;

import java.io.File;
import java.util.Hashtable;

import javax.crypto.SecretKey;

import org.junit.Ignore;
import org.junit.Test;

import cat.uib.secom.crypto.sig.bbs.core.accessors.enhanced.GroupManagerAccessor;
import cat.uib.secom.multicoupon2d.client.customer.Customer;
import cat.uib.secom.multicoupon2d.common.msg.RedeemM2;
import cat.uib.secom.multicoupon2d.common.msg.RedeemM4;
import cat.uib.secom.multicoupon2d.common.msg.impl.CommonInfoImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.MCPBSImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM3Impl;
import cat.uib.secom.multicoupon2d.servers.merchant.Merchant;


public class TestClientMerchant {

	
	protected static final String CURVE_FILE = "dtype_q175_r167.param";
	
	
	@Test
	public void testMultiRedeem() {
		
		
		try {
			
			GroupManagerAccessor gma = new GroupManagerAccessor(10, CURVE_FILE);
			gma.setup();
			
			Merchant merchant = Merchant.getInstance();
			merchant.enableLogging(true);
			Customer customer = Customer.getInstance(null);
			customer.enableLogging(true);
			customer.loadFromFile();
			customer.loadGroupKeyPair((cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKeyImpl) gma.getGroupPublicKey(), 
									  (cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKeyImpl) gma.getUserPrivateKey(1));
			SecretKey ks = customer.getCustomerStore().getKS();
			//merchant.setKs(ks);
			
			//Hashtable<Integer, Integer> couponsToBeSpend = new Hashtable<Integer, Integer>();
			//couponsToBeSpend.put(0, 9);
			
			
			long start = System.currentTimeMillis();
			RedeemM1Impl m1 = (RedeemM1Impl) customer.createMessageRedeem1( createCouponsToBeSpend1(11) );
			System.out.print((System.currentTimeMillis() - start) + "\t");
			
			m1.serialize(merchant.getMSGFormat());
			start = System.currentTimeMillis();
			
			
			CommonInfoImpl ci = (CommonInfoImpl) m1.getMCPBS().getCommonInfo();
			MCPBSImpl mcpbs = (MCPBSImpl) m1.getMCPBS();
			
			
			RedeemM2 m2 = merchant.createRedeemM2(m1);	
			System.out.print((System.currentTimeMillis() - start) + "\t");
			
			
			////couponsToBeSpend = new Hashtable<Integer, Integer>();
			////couponsToBeSpend.put(0, 10);
			
			
			RedeemM3Impl m3 = (RedeemM3Impl) customer.createMessageRedeem3(m2, createCouponsToBeSpend1(12));
			
			
			
			RedeemM4 m4 = merchant.createRedeemM4(m3);
			
			
			
			m1.serialize(customer.getMSGFormat());
			System.out.println("RedeemM1\n" + m1.dump(customer.getMSGFormat()));
			
			((RedeemM2Impl)m2).serialize(customer.getMSGFormat());
			System.out.println("RedeemM2\n" + ((RedeemM2Impl)m2).dump(customer.getMSGFormat()));
			
			m3.serialize(customer.getMSGFormat());
			System.out.println("RedeemM3\n" + m3.dump(customer.getMSGFormat()));
			
			// System.out.println("RedeemM4\n" + m1.dump(customer.getMSGFormat()));
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Ignore("I only want a single integration test... no benchmarking")
	@Test
	public void testMultipleTimesTestMultiRedeem() {
		int it = 100;
		while (it>0) {
			//System.out.println("\n-------- iteration " + it + " --------");
			testMultiRedeem();
			it--;
			System.gc();
			
		}
	}
	
	
	
	private Hashtable<Integer, Integer> createCouponsToBeSpend1(Integer i) {
		// according to TestClientIssuer createDescription1/2
		Hashtable<Integer, Integer> couponsToBeSpend = new Hashtable<Integer, Integer>();
		couponsToBeSpend.put(0, i); // i: fins al cupo que vull enviar
		return couponsToBeSpend;
	}
	
	private Hashtable<Integer, Integer> createCouponsToBeSpend2(Integer i) {
		Hashtable<Integer, Integer> couponsToBeSpend = new Hashtable<Integer, Integer>();
		couponsToBeSpend.put(0, i);
		couponsToBeSpend.put(1, i);
		couponsToBeSpend.put(2, i);
		couponsToBeSpend.put(3, i);
		couponsToBeSpend.put(4, i);
		return couponsToBeSpend;
		
	}
	
	
}
