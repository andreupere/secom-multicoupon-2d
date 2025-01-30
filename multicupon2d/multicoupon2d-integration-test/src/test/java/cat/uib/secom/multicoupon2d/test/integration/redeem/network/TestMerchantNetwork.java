package cat.uib.secom.multicoupon2d.test.integration.redeem.network;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.junit.Ignore;
import org.junit.Test;

import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.servers.merchant.Merchant;
import cat.uib.secom.multicoupon2d.servers.merchant.Merchant.MerchantServerImpl;
import cat.uib.secom.multicoupon2d.test.integration.redeem.StartMerchant;
import cat.uib.secom.utils.strings.LoadCfgUtils;

public class TestMerchantNetwork {

	
	@Test
	public void test() {
		
		try {
			LoadCfgUtils cfg = new LoadCfgUtils(this.getClass().getResourceAsStream("/common.cfg"));
			Integer port_merchant = new Integer( cfg.read("port_merchant") );
			
			
			Merchant merchant = Merchant.getInstance();
			Merchant.MerchantServerImpl server = merchant.new MerchantServerImpl(port_merchant);
			server.bind();
			server.listen();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Multicoupon2DException e) {
			e.printStackTrace();
		} catch (InvalidCipherTextException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
