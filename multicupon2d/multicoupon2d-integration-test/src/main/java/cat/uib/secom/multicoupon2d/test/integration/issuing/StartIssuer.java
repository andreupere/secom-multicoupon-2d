package cat.uib.secom.multicoupon2d.test.integration.issuing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;


import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.servers.issuer.Issuer;
import cat.uib.secom.utils.strings.LoadCfgUtils;

public class StartIssuer {

	
	public void startServer() {
		
		
		try {
			LoadCfgUtils cfg = new LoadCfgUtils(this.getClass().getResourceAsStream("/common.cfg"));
			Integer port_issuer = new Integer( cfg.read("port_issuer") );
			
			Issuer issuer = Issuer.getInstance();
			Issuer.IssuerServerImpl server = issuer.new IssuerServerImpl(port_issuer);
			server.bind();
			server.listen();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Multicoupon2DException e) {
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
	
	public static void main(String args[]) {
		(new StartIssuer()).startServer();
	}
	
}
