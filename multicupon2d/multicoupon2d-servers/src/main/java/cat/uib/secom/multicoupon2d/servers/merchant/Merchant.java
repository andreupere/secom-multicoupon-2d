package cat.uib.secom.multicoupon2d.servers.merchant;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Iterator;

import javax.crypto.spec.SecretKeySpec;

import org.spongycastle.asn1.pkcs.RSAPublicKey;
import org.spongycastle.asn1.x509.Certificate;
import org.spongycastle.crypto.InvalidCipherTextException;

import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKeyImpl;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupPublicKeyMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSReaderFactory;
import cat.uib.secom.multicoupon2d.common.cfg.Constants;
import cat.uib.secom.multicoupon2d.common.entity.Entity;
import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.common.msg.MCPBS;
import cat.uib.secom.multicoupon2d.common.msg.RedeemCoupon;
import cat.uib.secom.multicoupon2d.common.msg.RedeemM1;
import cat.uib.secom.multicoupon2d.common.msg.RedeemM2;
import cat.uib.secom.multicoupon2d.common.msg.RedeemM3;
import cat.uib.secom.multicoupon2d.common.msg.RedeemM4;
import cat.uib.secom.multicoupon2d.common.msg.RedeemSetCoupons;
import cat.uib.secom.multicoupon2d.common.msg.impl.ClaimM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.ClaimM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.ClaimM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.ClaimM4Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.MCPBSImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM4Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemSetCouponsImpl;
import cat.uib.secom.multicoupon2d.servers.merchant.vo.MerchantRedeemTableVO;
import cat.uib.secom.multicoupon2d.servers.merchant.vo.MerchantRedeemVO;
import cat.uib.secom.security.CertificateUtils;
import cat.uib.secom.security.CryptoUtils;
import cat.uib.secom.security.HashUtils;
import cat.uib.secom.security.KeyStoreUtils;
import cat.uib.secom.utils.networking.NetUtils;
import cat.uib.secom.utils.networking.ServerDefaultImpl;
import cat.uib.secom.utils.strings.LoadCfgUtils;
import cat.uib.secom.utils.strings.MSGFormatConstants;
import cat.uib.secom.utils.strings.PerformanceUtils;
import cat.uib.secom.utils.strings.StringUtils;

public class Merchant extends Entity {

	private static Merchant _merchant;
	
	private MerchantStore _merchantStore;
	
	private MerchantRedeemTableVO _redeemTableVO;
	
	private String _idM;
	
	// fill service
	private static final String SERVICE = "un e-book molt mono";
	
	
	public class MerchantServerImpl extends ServerDefaultImpl {

		public MerchantServerImpl(Integer port) {
			super(port);
		}
		
		public void listen() throws IOException {
			while (super._listening) {
				Socket socket = super._ss.accept();
				new MerchantThread(socket, _merchant).run(); 
			}
		}
		
	}
	
	

	/**
	 * Protected Merchant constructor: call constructor through getInstance method
	 * @throws InvalidCipherTextException  
	 * @throws CertificateException  
	 * @throws NoSuchAlgorithmException 
	 * @throws NoSuchProviderException 
	 * @throws KeyStoreException  
	 * @throws UnrecoverableKeyException 
	 * @throws InvalidKeySpecException 
	 * */
	private Merchant() throws FileNotFoundException, 
							  IOException, 
							  Multicoupon2DException, 
							  InvalidCipherTextException, 
							  KeyStoreException, 
							  NoSuchProviderException, 
							  NoSuchAlgorithmException, 
							  CertificateException, 
							  UnrecoverableKeyException, 
							  InvalidKeySpecException { 
		
		// rely on superclass to load common parameters
		super();
		
		// create an object to store data
		_merchantStore = new MerchantStore();
		
		// load merchant configuration from files
		LoadCfgUtils cfgMerchant = new LoadCfgUtils(this.getClass().getResourceAsStream("/merchant.cfg"));
		this._merchantStore.keyStoreFName = cfgMerchant.read("bks");
		this._merchantStore.keyStorePwd = cfgMerchant.read("pwd");
		this._merchantStore.keyAlias = cfgMerchant.read("alias");
		
		LoadCfgUtils cfgConfig = new LoadCfgUtils(this.getClass().getResourceAsStream("/config.properties"));
		LoadCfgUtils cfgCommon = new LoadCfgUtils(this.getClass().getResourceAsStream("/common.cfg"));
		
		// load information from issuer
//		LoadCfgUtils cfgIssuerPublic = new LoadCfgUtils(this.getClass().getResourceAsStream( "/issuer_public.cfg" ));
//		// load Issuer's RSA public key parameters (e,n)
//		this._merchantStore.rsaEIssuer = new BigInteger( cfgIssuerPublic.read("rsa_e") );
//		this._merchantStore.rsaNIssuer = new BigInteger( cfgIssuerPublic.read("rsa_n") );
		
		// load Issuer's certificate
		CertificateUtils.initializeBCProvider();
		this._merchantStore.rsaIssuerPubKey = CertificateUtils.getRSAPublicKey( this.rootFolder + "public/" + cfgConfig.read("issuer_rsacertx509") );
		
		// creates structure to store data related to a redeem instantiation
		_redeemTableVO = new MerchantRedeemTableVO();
		
		// load symmetric key
		BigInteger ks = new BigInteger( cfgMerchant.read("ks") );
		this._merchantStore.ks = new SecretKeySpec( ks.toByteArray() ,"AES");
		
		// load identification of this merchant
		this._idM = cfgMerchant.read("idM");
		
		//RSAPublicKey rsaPublicKey = new RSAPublicKey(_merchantStore.rsaNIssuer, _merchantStore.rsaEIssuer);
		//this._merchantStore.certMerchantASN1 = CertificateUtils.createGPKx509("", 364, rsaPublicKey.getEncoded());
		
		KeyStore keyStore = KeyStoreUtils.getInstance(this.rootFolder + this._merchantStore.keyStoreFName + ".jks", this._merchantStore.keyStorePwd, "jks", "SUN");
		// load own merchant rsa private key
		this._merchantStore.rsaPrivKey = KeyStoreUtils.getRSAPrivateKey(keyStore, this._merchantStore.keyAlias, this._merchantStore.keyStorePwd);
		// load own merchant rsa public key
		this._merchantStore.rsaPubKey = KeyStoreUtils.getRSAPublicKey(keyStore, this._merchantStore.keyAlias);
		this._merchantStore.rsaPubKey = CertificateUtils.getRSAPublicKey( this.rootFolder + "public/" + cfgConfig.read("merchant_rsacertx509")); 
		
		this._merchantStore.certMerchantASN1 = CertificateUtils.getCertX509(this.rootFolder + "public/" + cfgConfig.read("merchant_rsacertx509"));
		
		// load public key certificates (RSA public key) of merchant and issuer (now, I load foreign certificates from keystore)
//		LoadCfgUtils cfgConfig = new LoadCfgUtils(this.getClass().getResourceAsStream("/config.properties"));
//		String merchantX509FN = cfgConfig.read("merchant_rsacertx509");
//		String issuerX509FN = cfgConfig.read("issuer_rsacertx509");
//		this._merchantStore.certMerchantASN1 = CertificateUtils.getCertX509(this.rootFolder + merchantX509FN);
//		this._merchantStore.certIssuerASN1 = CertificateUtils.getCertX509(this.rootFolder + issuerX509FN);
//		
		
		
		
		if ( cfgCommon.read("online_verification").equals("1") ) 
			this._merchantStore.onlineVerification = true;
		else
			this._merchantStore.onlineVerification = false;
		
		
		this._merchantStore.issuerHost = cfgCommon.read("host");
		this._merchantStore.issuerPort = Integer.valueOf( cfgCommon.read("port_issuer") );
		
		
		
		
		
		// for performance purposes
		_performanceFN = getRootFolder() + "performance/" + "merchant_" + (new Date()).getTime() + ".txt";
		
		_pu = new PerformanceUtils();
		_pu.addComments( "Customer redeems coupons to the merchant" );
		_pu.addComments( (new Date()).toString() );
		_pu.addComments( "-" );
		_pu.addComments( getMSGFormat().toString() );
		_pu.addComments( "online verification active? " + String.valueOf( this._merchantStore.onlineVerification ) );
		_pu.addComments( "Time in miliseconds (ms)" );
		_pu.addComments("t1 \t receiving M1");
		_pu.addComments("t2 \t decoding M1");
		_pu.addComments("t3 \t managing M1 and building M2");
		_pu.addComments("t4 \t encoding M2");
		_pu.addComments("t5 \t sending M2");
		_pu.addComments("t6 \t receiving M2");
		_pu.addComments("t7 \t decoding M3");
		_pu.addComments("t8 \t managing M3 and building M4");
		_pu.addComments("t9 \t encoding M4");
		_pu.addComments("t10 \t sending M4");
		_pu.addComments("proto \t it \t t1 \t t2 \t t3 \t t4 \t t5 \t t6 \t t7 \t t8 \t t9 \t 10");
		
				
		
	}
	
	
	/**
	 * Generates a instance of the class Merchant 
	 * @throws FileNotFoundException, IOException, Multicoupon2DException, InvalidCipherTextException     
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws NoSuchProviderException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 * @throws InvalidKeySpecException 
	 * */
	public static Merchant getInstance() throws FileNotFoundException, 
												IOException, 
												Multicoupon2DException, 
												InvalidCipherTextException, 
												UnrecoverableKeyException, 
												KeyStoreException, 
												NoSuchProviderException, 
												NoSuchAlgorithmException, 
												CertificateException, 
												InvalidKeySpecException {
		if (_merchant == null)
			_merchant = new Merchant(); 
		return _merchant;
	}
	
	
	/**
	 * Gets the structure where data about configuration is stored
	 * */
	public MerchantStore getMerchantStore() {
		return this._merchantStore;
	}
	

	/**
	 * Create the second message of the multi-redeem algorithm
	 * 
	 * @param m1 as the received first message from customer
	 * @return RedeemM2 as the computed second message to be returned to the customer
	 * @exception Exception if something goes wrong
	 * */
	public RedeemM2 createRedeemM2(RedeemM1 m1) throws Exception {
		
		// store received message
		_merchantStore.setRedeemM1((RedeemM1Impl) m1);
		
		// decodes Ai,j,kj (the payment coupon set)
		byte[] asetEnc = StringUtils.decodeBase64( m1.getaRedeemSet() );
		
		// decrypts Ai,j,kj
		byte[] asetByte = CryptoUtils.decrypt(asetEnc, _merchantStore.ks, CryptoUtils.createFastIv());
		
		// verifies gpk through certg
		// TODO

		// verifies group signature applied over Ai,j,kj
		RedeemSetCouponsImpl rsci = new RedeemSetCouponsImpl();
		
		// deserialize object according to the preconfigured MSGFormat
		rsci = (RedeemSetCouponsImpl) rsci.deSerialize(asetByte, getMSGFormat());
		
		// extract group public key from certificate
		Certificate x509 = m1.getCertGPK();

		BBSGroupPublicKeyMSG gpkMSG = (BBSGroupPublicKeyMSG) (new BBSGroupPublicKeyMSG()).deSerialize(x509.getSubjectPublicKeyInfo().getPublicKeyData().getBytes(), getMSGFormat());
		BBSGroupPublicKeyImpl gpkBBS = (BBSGroupPublicKeyImpl) BBSReaderFactory.getBBS(gpkMSG, _bbsParameters );
		BBSGroupPublicKeyMSG gpk = new BBSGroupPublicKeyMSG(gpkBBS);
		
		
		// verification result using a helper utility
		boolean verification = super.verifyGroupSignatureHelper(gpk,  
								   						  		m1.getGroupSignature(),
								   						  		rsci,
								   						  		m1.getIdR(),
								   						  		_idM);
		
		// throw an exception if group signature verification fails
		if (!verification)
			throw new Exception("Group signature verification failed...");

		// verifies time marks and throw an exception if verification fails
		if ((new Date()).after(m1.getMCPBS().getCommonInfo().getExpiration()) )
			throw new Exception("Multicoupon expired... (" + m1.getMCPBS().getCommonInfo().getExpiration() + ")");
		
		// verifies mcpbs
		MCPBSImpl mcpbs = (MCPBSImpl) m1.getMCPBS();
		try {
		verification = mcpbs.verify(_merchantStore.rsaIssuerPubKey.getPublicExponent(), 
									_merchantStore.rsaIssuerPubKey.getModulus(), 
									getMSGFormat());
		} catch (Exception e) {
			System.out.println("PBS verification failed... but continue...");
		}
		// throw and exception if PBS verification fails
		//if (!verification)
		//	throw new Exception("PBS verification failed...");
		
		
		
		// verifies coupons throw an utility helper
		boolean ver = verifyCouponsHelper(rsci, m1.getMCPBS());
		if (!ver)
			throw new Exception("Merchant::createRedeemM2::Coupons verification failed...");
		
		
		
		
		
		// stores data
		String key = m1.getIdR();
		MerchantRedeemVO redeemVO = null;
		redeemVO = _redeemTableVO.lookup( key );
		if (redeemVO == null) {
			// if key is not found... create new redeemVO
			redeemVO = new MerchantRedeemVO();
		}
		redeemVO.setKey(key);
		redeemVO.setaRedeemSet(rsci);
		redeemVO.setaSetGroupSigned(m1.getGroupSignature());
		redeemVO.setGroupPublicKey(gpk);
		redeemVO.setMCPBS( (MCPBSImpl) m1.getMCPBS() );
		
		
		// TODO: go to verify coupons online to the Issuer (createClaimM1 and send data to network, waiting for response)
		if (this._merchantStore.onlineVerification) {
			System.out.println("GO TO ISSUER FOR ONLINE VERIFICATION (CLAIM M1)");
			Socket s = NetUtils.getSocket(this._merchantStore.issuerHost, this._merchantStore.issuerPort);
			ClaimM1Impl claimM1 = this.createClaimM1(redeemVO);
			byte[] data = claimM1.serialize(getMSGFormat());
			NetUtils.write(s.getOutputStream(), data);
			
			data = NetUtils.read(s.getInputStream());
			
			ClaimM2Impl claimM2 = new ClaimM2Impl();
			claimM2 = (ClaimM2Impl) claimM2.deSerialize(data, getMSGFormat());
			
			System.out.println("Response to the first claim: " + claimM2.getResponse());
			
			redeemVO.setClaimM2(claimM2);
			
		}
		
		_redeemTableVO.update(key, redeemVO);
		
		// START to mount the response
		
		// encrypts service throw KS
		byte[] servEnc = CryptoUtils.encrypt(SERVICE.getBytes(Constants.CHARSET), _merchantStore.ks, CryptoUtils.createFastIv());
		
		// initialize provider
		CryptoUtils.initializeBCProvider();
		
		// load RSA keypair from keystore (already loaded)
		//KeyStore ks = KeyStoreUtils.getInstance(this._merchantStore.keyStoreFName, this._merchantStore.keyStorePwd);
		//KeyPair kp = KeyStoreUtils.getRSAKeyPair(ks, this._merchantStore.keyAlias, this._merchantStore.keyStorePwd);
//		KeyPair kp = CryptoUtils.getKeyPair(Constants.KEY_STORE_TYPE, 
//										 			this._merchantStore.keyStoreFName, 
//										 			this._merchantStore.keyStorePwd, 
//										 			this._merchantStore.keyAlias);
		
		// creates a byte array containing "service+group signature"
		// serialize according to the MSGFormat
		
		
		byte[] gsBytes = m1.getGroupSignature().serialize(this.getMSGFormat());
		
		// service to byte array
		byte[] serviceBytes = SERVICE.getBytes(Constants.CHARSET);
		
		// join the two bytes array through a ByteArrayOutputStream class
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(gsBytes);
		baos.write(serviceBytes);
		
		// sign data contained in byte array
		byte[] signData = CryptoUtils.signData(baos.toByteArray(), this._merchantStore.rsaPrivKey, CryptoUtils.SIGN_ALGORITHM);
		
		// create the second message encoding required parameters
		RedeemM2 m2 = new RedeemM2Impl();
		m2.setMerchantSignature( StringUtils.encodeBase64(signData) ); 
		m2.setServiceEncrypted( StringUtils.encodeBase64(servEnc) );
		m2.setIdR(key); // TODO: a l'especificació no hi es, pero s'ha de posar o no?
		
		// store m2
		_merchantStore.setRedeemM2((RedeemM2Impl) m2);
		
		return m2;
		
	}
	
	
	/**
	 * Create the fourth message of the multi-redeem algorithm
	 * 
	 * @param m3 as the third received message
	 * @return RedeemM4 as the fourth message
	 * @exception Exception if something goes wrong
	 * */
	public RedeemM4 createRedeemM4(RedeemM3 m3) throws Exception {
		
		// store received message
		_merchantStore.setRedeemM3((RedeemM3Impl) m3);

		// search the endorsed multicoupon in the redeemTableVO. If it is not present, reject (customer
		// did not do the first step of the algorithm)
		
		// get transaction ID
		String key = m3.getIdR();
		MerchantRedeemVO redeemVO = null;
		try {
			// lookup key in the table
			redeemVO = _redeemTableVO.lookup( key );
		}catch(Exception e) {
			// if key is not found... reject
			throw new Exception("Multicoupon not found in the database... REJECT");
		}
		
		// verifies gpk through certg
		// TODO
		
		// decode received set of coupons Bi,j,kj
		byte[] setEnc = StringUtils.decodeBase64( m3.getBRedeemSet() );
		
		// decrypt the recived set of coupons
		byte[] setByte = CryptoUtils.decrypt(setEnc, _merchantStore.ks, CryptoUtils.createFastIv());
		
		RedeemSetCouponsImpl rsci = new RedeemSetCouponsImpl();
		
		// deserialize coupon set according to the MSGFormat parameter
		rsci = (RedeemSetCouponsImpl) rsci.deSerialize(setByte, getMSGFormat());
		
		// verifies group signature over Bi,j,kj
		boolean verification = super.verifyGroupSignatureHelper(redeemVO.getGroupPublicKey(),
																m3.getGroupSignature(),
								   						  		rsci,
								   						  		key,
								   						  		_idM);
		
		if (!verification) 
			throw new Exception("Group signature on Bj,i,kj failed...");
		
		// verifies coupons
		Iterator<RedeemCoupon> it = rsci.getRedeemCoupons().iterator();
		while (it.hasNext()) {
			RedeemCoupon currentCoupon = it.next();
			// per cada cupo dins bset, fer un hash i verificar si concorda amb el darrer
			// cupo usat, és a dir, el cupó que està dins aset corresponent al mateix j multicupó
			// Si falla algún, reject
			RedeemCoupon previousCoupon = redeemVO.getaRedeemSet().getRedeemCoupon(currentCoupon.getJ());
			
			BigInteger hash = HashUtils.getHash( currentCoupon.getHash(), (currentCoupon.getI() - previousCoupon.getI()) );
			
			if ( !previousCoupon.getHash().equals(hash) ) 
				throw new Exception("Coupon verification failed...");
		}
		
		
		
		
		// updates stored data
		redeemVO.setbRedeemSet(rsci);
		redeemVO.setbSetGroupSigned(m3.getGroupSignature());
		
		
		
		if (this._merchantStore.onlineVerification) {
			System.out.println("GO TO ISSUER FOR ONLINE VERIFICATION (CLAIM M3)");
			
			Socket s = NetUtils.getSocket(this._merchantStore.issuerHost, this._merchantStore.issuerPort);
			ClaimM3Impl claimM3 = this.createClaimM3(redeemVO, redeemVO.getClaimM2());
			byte[] data = claimM3.serialize(getMSGFormat());
			NetUtils.write(s.getOutputStream(), data);
			
			data = NetUtils.read(s.getInputStream());
			
			ClaimM4Impl claimM4 = new ClaimM4Impl();
			claimM4 = (ClaimM4Impl) claimM4.deSerialize(data, getMSGFormat());
			
			System.out.println("Response to the second claim: " + claimM4.getResponse());
		}
		
		_redeemTableVO.update(key, redeemVO);
		
		// prepare to compute an RSA signature
		
		// initialize provider
		CryptoUtils.initializeBCProvider();
		byte[] data = null;
		
		// depending on the MSGFormat, serialize
		if (getMSGFormat().equals(MSGFormatConstants.XML))
			data = m3.getGroupSignature().serialize(MSGFormatConstants.XML);
		else
			data = m3.getGroupSignature().serialize(MSGFormatConstants.ASN1);
		

		// sign data previously serialized
		byte[] signData = CryptoUtils.signData(data, this._merchantStore.rsaPrivKey, CryptoUtils.SIGN_ALGORITHM);
		
		// create the fourth message
		RedeemM4 m4 = new RedeemM4Impl();
		m4.setIdR(key);
		m4.setMerchantSignature( StringUtils.encodeBase64(signData) );
		
		// store the fourth message
		_merchantStore.setRedeemM4((RedeemM4Impl) m4);
		
		return m4;
		
	}
	
	
	
	
	
	/******************************************************************************************
	 * 
	 * Create first message of CLAIM protocol
	 * 
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchProviderException 
	 * @throws KeyStoreException 
	 * @throws SignatureException 
	 * @throws UnrecoverableKeyException 
	 * @throws InvalidKeyException 
	 * @throws Exception 
	 * 
	 ******************************************************************************************/
	public ClaimM1Impl createClaimM1(MerchantRedeemVO mrVO) throws NoSuchAlgorithmException, 
																   KeyStoreException, 
																   NoSuchProviderException, 
																   CertificateException, 
																   IOException, 
																   InvalidKeyException, 
																   UnrecoverableKeyException, 
																   SignatureException, 
																   Exception {
		
		// compose r1 to be signed by Merchant
		String r1 = "";

		byte[] arset = mrVO.getaRedeemSet().serialize(getMSGFormat());
		byte[] arsetGSigned = mrVO.getaSetGroupSigned().serialize(getMSGFormat());
		byte[] mcpbs = mrVO.getMCPBS().serialize(getMSGFormat());
		
		byte[] toSign = StringUtils.concatenate(arset, arsetGSigned, mcpbs);
		
		// sign r1
		//SignatureProcess sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
		
		
		
		byte[] signed = CryptoUtils.signData(toSign, this._merchantStore.rsaPrivKey, null);
		//.sign(KeyStoreUtils.getRSAPrivateKey(this._merchantStore.keyAlias, this._merchantStore.keyStorePwd), 
		//				toSign);
		
		r1 = StringUtils.encodeBase64(signed);



			
		// compose claiM1 message		
		ClaimM1Impl claimM1 = new ClaimM1Impl();
		claimM1.setaRedeemSet(StringUtils.encodeBase64(arset)); 
		claimM1.setCertMerchant(_merchantStore.certMerchantASN1);
		claimM1.setGroupSignature(mrVO.getaSetGroupSigned());
		claimM1.setIdR(mrVO.getKey());
		claimM1.setMcpbs(mrVO.getMCPBS());
		claimM1.setSignature(r1);
		
		return claimM1;
	}
	
	
	
	public ClaimM3Impl createClaimM3(MerchantRedeemVO mrVO, ClaimM2Impl claimM2) throws NoSuchAlgorithmException, 
																   						KeyStoreException, 
																   						NoSuchProviderException, 
																   						CertificateException, 
																   						IOException, 
																   						InvalidKeyException, 
																   						UnrecoverableKeyException, 
																   						SignatureException,
																   						Exception {
		
		if (!claimM2.getResponse().equals(Constants.ACCEPTED) )
			throw new Exception("REJECTED");
		
		String r2 = "";
		
		byte[] brset = mrVO.getbRedeemSet().serialize(getMSGFormat());
		byte[] brsetGSigned = mrVO.getaSetGroupSigned().serialize(getMSGFormat());
		byte[] mcpbs = mrVO.getMCPBS().serialize(getMSGFormat());
		
		byte[] toSign = StringUtils.concatenate(brset, brsetGSigned, mcpbs);
		
		// sign r3		
		byte[] signed = CryptoUtils.signData(toSign, this._merchantStore.rsaPrivKey, null);
		
//		byte[] signed = sp.sign(ksUtils.getRSAPrivateKey(this._merchantStore.keyAlias, this._merchantStore.keyStorePwd), 
//						toSign);
		
		r2 = StringUtils.encodeBase64(signed);
		
		ClaimM3Impl claimM3 = new ClaimM3Impl();
		claimM3.setMcpbs( mrVO.getMCPBS() );
		claimM3.setCertMerchant(_merchantStore.certMerchantASN1);
		claimM3.setbRedeemSet(StringUtils.encodeBase64(brset)); 
		claimM3.setGroupSignature(mrVO.getbSetGroupSigned());
		claimM3.setIdR(mrVO.getKey());
		claimM3.setSignature(r2);
		
		return claimM3;
	}
	
	
	
	
	
	
	
}
