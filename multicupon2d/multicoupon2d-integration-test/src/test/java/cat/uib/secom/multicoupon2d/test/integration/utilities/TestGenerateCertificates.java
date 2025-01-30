package cat.uib.secom.multicoupon2d.test.integration.utilities;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.cert.CertificateException;

import org.spongycastle.asn1.pkcs.RSAPublicKey;
import org.spongycastle.asn1.util.ASN1Dump;
import org.spongycastle.asn1.x509.Certificate;
import org.spongycastle.crypto.InvalidCipherTextException;
import org.junit.Ignore;
import org.junit.Test;

import cat.uib.secom.security.CertificateUtils;
import cat.uib.secom.security.KeyStoreUtils;


/**
 * Test utility to compute certificates from RSA public key allocated in a keystore
 * */
public class TestGenerateCertificates {

	// hardcoded uris to various BKS keystore
	String uriBKSIssuer = "/home/apaspai/development/data/multicoupon2d/private/issuerRSA.bks";
	
	String uriBKSMerchant = "/home/apaspai/development/data/multicoupon2d/private/merchantRSA.bks";

	String uriBKSCustomer = "/home/apaspai/development/data/multicoupon2d/private/customerRSA.bks";
	
	// hardcoded alias to keys
	String aliasIssuer = "issuer1024";
	
	String aliasMerchant = "merchant1024";
	
	String aliasCustomer = "customer1024";
	
	String provider = "BC";
	
	String keyStoreType = "BKS";
	
	// hardcoded password to open the BKS keystore
	String password = "1234";
	
	@Ignore
	@Test
	public void generate() {
		try {
			KeyStore keyStore = KeyStoreUtils.getInstance(uriBKSIssuer, password, keyStoreType, provider);
			PublicKey rsaPubKeyIssuer = KeyStoreUtils.getRSAPublicKey(keyStore, aliasIssuer);
			System.out.println(rsaPubKeyIssuer);
			Certificate certIssuer = CertificateUtils.createGPKx509("", 365, rsaPubKeyIssuer.getEncoded());
			System.out.println(ASN1Dump.dumpAsString( certIssuer.getSubjectPublicKeyInfo().getPublicKeyData().getString() ));
			
			
			keyStore = KeyStoreUtils.getInstance(uriBKSMerchant, password, keyStoreType, provider);
			PublicKey rsaPubKeyMerchant = KeyStoreUtils.getRSAPublicKey(keyStore, aliasMerchant);
			Certificate certMerchant = CertificateUtils.createGPKx509("", 365, rsaPubKeyMerchant.getEncoded());
			
			
			keyStore = KeyStoreUtils.getInstance(uriBKSCustomer, password, keyStoreType, provider);
			PublicKey rsaPubKeyCustomer = KeyStoreUtils.getRSAPublicKey(keyStore, aliasCustomer);
			Certificate certCustomer = CertificateUtils.createGPKx509("", 365, rsaPubKeyCustomer.getEncoded());
			
			//System.out.println( ((java.security.interfaces.RSAPublicKey)rsaPubKeyCustomer).getModulus() );
			
			
			// store in file
			CertificateUtils.setCertX509(certIssuer, "/home/apaspai/development/data/multicoupon2d/issuer_cert.data");
			CertificateUtils.setCertX509(certMerchant, "/home/apaspai/development/data/multicoupon2d/merchant_cert.data");
			CertificateUtils.setCertX509(certCustomer, "/home/apaspai/development/data/multicoupon2d/customer_cert.data");
			
			

			
			
			
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidCipherTextException e) {
			e.printStackTrace();
		}
	}
	
}
