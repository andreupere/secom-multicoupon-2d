package cat.uib.secom.multicoupon2d.servers.issuer;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class IssuerStore {
	
	protected String keyStoreFName;
	
	protected String keyStorePwd;
	
	protected String keyAlias; 
	
	protected RSAPrivateKey rsaPrivKey;
	
	protected RSAPublicKey rsaPubKey;
	
	protected RSAPublicKey rsaMerchantPubKey;
	
	
	
	public IssuerStore() {}
	

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

	public RSAPrivateKey getRsaPrivKey() {
		return rsaPrivKey;
	}

	public void setRsaPrivKey(RSAPrivateKey rsaPrivKey) {
		this.rsaPrivKey = rsaPrivKey;
	}

	public RSAPublicKey getRsaPubKey() {
		return rsaPubKey;
	}

	public void setRsaPubKey(RSAPublicKey rsaPubKey) {
		this.rsaPubKey = rsaPubKey;
	}
	
	

}
