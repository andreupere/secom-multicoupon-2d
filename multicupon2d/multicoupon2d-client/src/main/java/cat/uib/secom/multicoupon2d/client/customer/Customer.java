package cat.uib.secom.multicoupon2d.client.customer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.crypto.spec.SecretKeySpec;

import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.params.RSAKeyParameters;


import cat.uib.secom.crypto.sig.bbs.core.accessors.enhanced.SignerAccessor;
import cat.uib.secom.crypto.sig.bbs.core.engines.BBSEnginePrecomputation;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.*;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupPublicKeyMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSReaderFactory;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSSignatureMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSUserPrivateKeyMSG;
import cat.uib.secom.multicoupon2d.common.cfg.Constants;
import cat.uib.secom.multicoupon2d.common.dao.Multicoupon2D;
import cat.uib.secom.multicoupon2d.common.entity.Entity;
import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.common.msg.CommonInfo;
import cat.uib.secom.multicoupon2d.common.msg.Coupon;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM1;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM2;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM3;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM4;
import cat.uib.secom.multicoupon2d.common.msg.JoinM1;
import cat.uib.secom.multicoupon2d.common.msg.JoinM2;
import cat.uib.secom.multicoupon2d.common.msg.MCPBS;
import cat.uib.secom.multicoupon2d.common.msg.Multicoupon;
import cat.uib.secom.multicoupon2d.common.msg.RedeemCoupon;
import cat.uib.secom.multicoupon2d.common.msg.RedeemM1;
import cat.uib.secom.multicoupon2d.common.msg.RedeemM2;
import cat.uib.secom.multicoupon2d.common.msg.RedeemM3;
import cat.uib.secom.multicoupon2d.common.msg.RedeemSetCoupons;
import cat.uib.secom.multicoupon2d.common.msg.impl.CouponImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM4Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.JoinM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.JoinM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.MCPBSImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.MulticouponImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemCouponImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM4Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemSetCouponsImpl;
import cat.uib.secom.security.CertificateUtils;
import cat.uib.secom.security.CryptoUtils;
import cat.uib.secom.security.HashUtils;
import cat.uib.secom.security.KeyStoreUtils;
import cat.uib.secom.security.RandomGeneratorUtils;
import cat.uib.secom.utils.strings.LoadCfgUtils;
import cat.uib.secom.utils.strings.LoggingUtils;
import cat.uib.secom.utils.strings.MSGFormatConstants;
import cat.uib.secom.utils.strings.StringUtils;


public class Customer extends Entity {
	
	private static Customer 	_customer;
	
	private CustomerStore 		_customerStore;

	
	private BigInteger 			_eta;
	private BigInteger 			_mi;
	private BigInteger 			_lambda;
	private BigInteger 			_rho;
	private BigInteger 			_beta;
		
	private String 				_idR;
	
	private String 				_idM = "1"; // TODO: now it is hardcoded
	
	
	private String				_customerGroupCertX509FileName;
	
	private String				_customerGroupUSKFileName;
	
	private String				_issuerCertX509FileName;
	
	private String				_merchantCertX509FileName;
	
	private String				_hostServers;
	
	private Integer				_issuerPort;
	
	private Integer				_merchantPort;
	
	private Integer				_gmanagerPort;
	
	
	
	
	public String getHostServers() {
		return _hostServers;
	}



	public Integer getIssuerPort() {
		return _issuerPort;
	}



	public Integer getMerchantPort() {
		return _merchantPort;
	}



	public Integer getGmanagerPort() {
		return _gmanagerPort;
	}



	/**
	 * Protected Customer constructor: call constructor through getInstance method
	 * @throws InvalidCipherTextException 
	 * @throws CertificateException 
	 * @throws KeyStoreException  
	 * @throws UnrecoverableKeyException  
	 * @throws InvalidKeySpecException 
	 * */
	protected Customer(InputStream commonConfigInputStream) 
			throws FileNotFoundException, 
				   IOException, 
				   NoSuchAlgorithmException, 
				   NoSuchProviderException, 
				   Multicoupon2DException, 
				   InvalidCipherTextException, 
				   KeyStoreException, 
				   CertificateException, 
				   UnrecoverableKeyException, 
				   InvalidKeySpecException {
		
		// rely on Entity class to load common configuration
		super(commonConfigInputStream);
		
		loadConfiguration();
		
		

	}
	
	
	
	protected Customer() 
			throws FileNotFoundException, 
				   IOException, 
				   NoSuchAlgorithmException, 
				   NoSuchProviderException, 
				   Multicoupon2DException, 
				   InvalidCipherTextException, 
				   KeyStoreException, 
				   CertificateException, 
				   UnrecoverableKeyException, 
				   InvalidKeySpecException {
		
		// rely on Entity class to load common configuration
		super();
		
		loadConfiguration();

	}
	
	
	/**
	 * Generates an instance of the class Customer
	 * @throws InvalidCipherTextException 
	 * @throws CertificateException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 * @throws InvalidKeySpecException 
	 * */
	public static Customer getInstance(InputStream config) 
			throws FileNotFoundException, 
				   NoSuchAlgorithmException, 
				   NoSuchProviderException, 
				   IOException, 
				   Multicoupon2DException, 
				   InvalidCipherTextException, 
				   UnrecoverableKeyException, 
				   KeyStoreException, 
				   CertificateException, 
				   InvalidKeySpecException   {
		
		if (_customer == null) {
			if (config == null)
				_customer = new Customer();
			else
				_customer = new Customer(config);
		}
		return _customer; 
	}
	
	
	/**
	 * Either enables or disables logging functions through Customer code
	 * */
	public void enableLogging(boolean b) {
		_log = LoggingUtils.getInstance(b);
	}
	

	public CustomerStore getCustomerStore() {
		return this._customerStore;
	}
	

	
	public void loadGroupKeyPair(BBSGroupPublicKeyImpl gpk, BBSUserPrivateKeyImpl usk) {
		this._customerStore.gpk = gpk;
		this._customerStore.usk = usk;
	}
	
	
	
	public BBSParameters getBBSParameters() {
		return this._bbsParameters;
	}

	
	public void loadFromFile() throws Exception  {
		// load MCPBS from file
		FileReader fr = new FileReader( new File(this.rootFolder + MCPBSImpl.fNAME +".xml") );
		_customerStore.mcpbs = (MCPBSImpl) ( new MCPBSImpl() ).deSerialize(fr, MSGFormatConstants.XML); // load always as XML
		
		// load Multicoupon2D from file
		fr = new FileReader( new File( this.rootFolder + Multicoupon2D.fNAME +  ".xml" ) );
		_customerStore.m2d = (Multicoupon2D) (new Multicoupon2D()).deSerialize( fr , MSGFormatConstants.XML); // load always as XML
		
		// load group public key		
		//BBSGroupPublicKeyMSG gpkm2 = new BBSGroupPublicKeyMSG();
		//_customerStore.gpk = (BBSGroupPublicKey) gpkm2.toObject(new BufferedReader(new FileReader(this.rootFolder + "gpk.xml")), this.getBBSParameters());
		
		// load user private key
		BBSUserPrivateKeyMSG uskm2 = new BBSUserPrivateKeyMSG();
		uskm2 = (BBSUserPrivateKeyMSG) uskm2.deSerialize(new BufferedReader(new FileReader(this.rootFolder + "private/" + this._customerGroupUSKFileName)), MSGFormatConstants.XML);
		_customerStore.usk = (BBSUserPrivateKeyImpl) BBSReaderFactory.getBBS(uskm2, _bbsParameters);

		// load group manager X.509 certificate from file
		_customerStore.certGPKASN1 = CertificateUtils.getCertX509( this.rootFolder + "public/" + this._customerGroupCertX509FileName ); 
		
		// load group public key from the X509 certificate
		BBSGroupPublicKeyMSG gpkBBS = (BBSGroupPublicKeyMSG) (new BBSGroupPublicKeyMSG()).deSerialize(_customerStore.certGPKASN1.getSubjectPublicKeyInfo().getPublicKeyData().getBytes(), this.getMSGFormat());
		
		_customerStore.gpk = (BBSGroupPublicKeyImpl) BBSReaderFactory.getBBS( gpkBBS, _bbsParameters);
		
		// load user X.509 certificate from file
		
		//_customerStore.certUserASN1 = SecUtils.getCertX509( this.rootFolder + "certUserX509.data" );
		
		
		// here I can change signerAccessor behaviour: precomputation or without precomputation (see BBSEngine())
		signerAccessor = new SignerAccessor( new BBSEnginePrecomputation( _customerStore.gpk, _customerStore.usk, BBSEnginePrecomputation.SIGNER ) );
		
	}
	
	
	/******************************************************************************************
	 * 
	 * Public methods for JOIN protocol between customer and group manager 
	 * 
	 ******************************************************************************************/
	
	/**
	 * Creates first message of Join algorithm
	 * 
	 * @param id
	 * @param userCert
	 * @return first message
	 * */
	public JoinM1 createMessageJoinM1(String id) throws Exception {
		JoinM1 m1 = new JoinM1Impl();
		m1.setCertificate(_customerStore.certUserASN1);		
		m1.setId(id);
		return m1;
	}
	
	
	public JoinM2 receiveJoinM2(JoinM2 joinM2) throws Exception {
		JoinM2Impl joinM2Impl = (JoinM2Impl) joinM2;
		_customerStore.certGPKASN1 = joinM2Impl.getBBSGroupPublicKeyCertificate();
		// store in file
		CertificateUtils.setCertX509(_customerStore.certGPKASN1, this.rootFolder + "public/" + this._customerGroupCertX509FileName);
		
		BBSUserPrivateKeyMSG upk = new BBSUserPrivateKeyMSG();
		upk = joinM2Impl.getBBSUserPrivateKeyMSG();
		
		upk.serialize( new BufferedWriter(new FileWriter(this.rootFolder + "private/" + this._customerGroupUSKFileName)) , MSGFormatConstants.XML);
		
//		BufferedWriter bw = new BufferedWriter(new FileWriter(this.rootFolder + "usk.xml"));
//		bw.write("<usk>"); 
//		bw.newLine();
//		bw.write("<a>" + upk.getA() + "</a>");
//		bw.newLine();
//		bw.write("<x>" + upk.getX() + "</x>");
//		bw.newLine();
//		bw.write("</usk>");
//		bw.close();
		
		return joinM2Impl;
	}
	
	
	/******************************************************************************************
	 * 
	 * Public methods for Issuing protocol between customer and issuer 
	 * 
	 ******************************************************************************************/
	
	
	
	/******************************************************************************************
	 * 
	 * @param ci the common information to build the first message of issuing algorithm
	 * @return IssuingM1 object containing the first message of issuing algorithm
	 * @throws NoSuchAlgorithmException 
	 */
	
	public IssuingM1 issuingMSG1Logic(CommonInfo ci) throws NoSuchAlgorithmException   {
		
		_customerStore.commonInfo = ci;
		
		createMulticoupon2D( ci );		
		
		// concatenate all the root coupons
		String roots = concatenateRootCoupons();
		
		// Compute alfa
		_eta = RandomGeneratorUtils.getRandomModuleN(_customerStore.rsaIssuerPubKey.getModulus());
		_mi = RandomGeneratorUtils.getRandomModuleN(_customerStore.rsaIssuerPubKey.getModulus());
		BigInteger alfa1 = _eta.modPow(_customerStore.rsaIssuerPubKey.getPublicExponent(), _customerStore.rsaIssuerPubKey.getModulus());
		BigInteger alfa2 = HashUtils.getHash(roots).mod(_customerStore.rsaIssuerPubKey.getModulus());
		BigInteger alfa3 = _mi.modPow(new BigInteger("2"), _customerStore.rsaIssuerPubKey.getModulus()).add(BigInteger.ONE).mod(_customerStore.rsaIssuerPubKey.getModulus());
		BigInteger alfa = alfa1.multiply(alfa2).mod(_customerStore.rsaIssuerPubKey.getModulus());
		alfa = alfa.multiply(alfa3).mod(_customerStore.rsaIssuerPubKey.getModulus());
		
		// compose Issuing M1
		IssuingM1 m1 = new IssuingM1Impl();
		m1.setAlpha(alfa);
		m1.setCommonInfo(ci);
		
		_customerStore.setIssuingM1((IssuingM1Impl) m1);
		
		return m1;
	}
	
	
	
	public IssuingM3 issuingMSG2Logic(IssuingM2 m2) {
		
		// store received message
		_customerStore.setIssuingM2((IssuingM2Impl) m2);
		
		_lambda = m2.getLambda();
		
		_rho = RandomGeneratorUtils.getRandomModuleN(_customerStore.rsaIssuerPubKey.getModulus());
		BigInteger be = _rho.multiply(_eta);
		BigInteger beta1 = be.modPow(_customerStore.rsaIssuerPubKey.getPublicExponent(), _customerStore.rsaIssuerPubKey.getModulus());
		BigInteger beta2 = _mi.add(_lambda.negate()).mod(_customerStore.rsaIssuerPubKey.getModulus() );
		
		_beta = beta1.multiply(beta2).mod(_customerStore.rsaIssuerPubKey.getModulus() );
		
		
		// compose Issuing M3
		IssuingM3 m3 = new IssuingM3Impl();
		m3.setBeta(_beta);
		m3.setSessionID( m2.getSessionID() );
		
		_customerStore.setIssuingM3((IssuingM3Impl) m3);
		
		return m3;
	}
	
	
	
	public MCPBS unblindIssuing(IssuingM4 m4) {
		
		_customerStore.setIssuingM4((IssuingM4Impl) m4);
		
		// calculate delta
		BigInteger delta1 = _mi.multiply(_lambda).mod(_customerStore.rsaIssuerPubKey.getModulus()).add(BigInteger.ONE).mod(_customerStore.rsaIssuerPubKey.getModulus());
		BigInteger delta2 = _mi.add(_lambda.negate()).mod(_customerStore.rsaIssuerPubKey.getModulus());
		BigInteger delta = delta1.multiply(delta2.modInverse(_customerStore.rsaIssuerPubKey.getModulus())).mod(_customerStore.rsaIssuerPubKey.getModulus());
		
		
		// calculate omega
		BigInteger omega1 = _eta.modPow(new BigInteger("2"), _customerStore.rsaIssuerPubKey.getModulus());
		BigInteger omega2 = _rho.modPow(new BigInteger("4"), _customerStore.rsaIssuerPubKey.getModulus());
		BigInteger omega = m4.getGamma().multiply(omega1).mod(_customerStore.rsaIssuerPubKey.getModulus());
		omega = omega.multiply(omega2).mod(_customerStore.rsaIssuerPubKey.getModulus());
		
		
		// compose signature PBS
		_customerStore.mcpbs = new MCPBSImpl();
		_customerStore.mcpbs.setCommonInfo( _customerStore.commonInfo );
		_customerStore.mcpbs.setDelta( delta );
		_customerStore.mcpbs.setOmega( omega );
		_customerStore.mcpbs.setRootCoupons( getRootCoupons() );
		
		return _customerStore.mcpbs;
		
	}
	
	
	
	

	/******************************************************************************************
	 * 
	 * Public methods for multi-redeem protocol between customer and merchant 
	 * 
	 ******************************************************************************************/
	
	/**
	 * Create the first message of multi-redeem algorithm
	 * @param couponsToBeSpend as the set of coupons to be used to redeem
	 * @return RedeemM1 message representing the first message of the multi-redeem algorithm
	 * @exception Exception if something wrong occurs
	 * */
	public RedeemM1 createMessageRedeem1(Hashtable<Integer, Integer> couponsToBeSpend) throws Exception {

		// NOTE: we are supposing a common KS already exchanged before to start the protocol
		
		
		// picks a random number to be idR (redeem instantiation ID)
		_idR = RandomGeneratorUtils.getRandom(100).toString();
		
		System.out.println("Customer:get redeem set");
		
		// extract set Ai,j,kj (payment redeem copon set)
		RedeemSetCouponsImpl aset = (RedeemSetCouponsImpl) getRedeemSetCoupons(couponsToBeSpend);
		
		System.out.println("Customer:before serialize");
		
		// encrypt aset
		byte[] asetEncoded = aset.serialize(getMSGFormat());
		System.out.println("Customer:before encrypt");
		byte[] encrypted = CryptoUtils.encrypt(asetEncoded, _customerStore.ks, CryptoUtils.createFastIv());
		
		System.out.println("Customer:before group sign");
		
		// group sign aset
		BBSSignatureMSG signature = getRedeemSetGroupSigned(aset,
															_idM,
															_idR);
		
		System.out.println("Customer:before bbsgrouppkmsg");
		
		// attach group public key for verification purposes
		BBSGroupPublicKeyMSG gpk = new BBSGroupPublicKeyMSG(_customerStore.gpk);

		
		// compose message
		RedeemM1 m1 = new RedeemM1Impl();
		m1.setCertGPK( _customerStore.certGPKASN1 ); 
		m1.setGroupSignature( signature );
		//m1.setGroupPublicKey( gpk );
		m1.setMCPBS( _customerStore.mcpbs );
		m1.setaRedeemSet( StringUtils.encodeBase64(encrypted) );
		m1.setIdR( _idR );
		
		//_log.printConsole(this.getClass(), "Customer::createMessageRedeem1::aset(string):\n" + aset.dump(getMSGFormat()));
		//_log.printConsole(this.getClass(), "Customer::createMessageRedeem1::asetEnc(string):\n" + new String(SecUtils.encodeBase64(encrypted)));

		this._customerStore.setRedeemM1((RedeemM1Impl) m1);
		
		return m1;
		
	}
	
	
	
	
	
	
	public RedeemM3 createMessageRedeem3(RedeemM2 m2, Hashtable<Integer, Integer> couponsToBeSpend) throws Exception {
		
		// store received message
		this._customerStore.setRedeemM2((RedeemM2Impl) m2);
		
		// decode the received merchant RSA signature
		byte[] sig = StringUtils.decodeBase64(m2.getMerchantSignature());
		
		// decode the received encrypted service
		byte[] servEnc = StringUtils.decodeBase64(m2.getServiceEncrypted());
		
		// decrypt the received encrypted service
		byte[] serv = CryptoUtils.decrypt(servEnc, _customerStore.ks, CryptoUtils.createFastIv());
		
		// since service is a String, rebuild it according the the default application CHARSET
		String service = new String(serv, Constants.CHARSET );
		
		//_log.printConsoleSimple(this.getClass(), "Customer::createMessageRedeem3::service=" + service);

		// extract Merchant RSA public key from the keystore
		//KeyStore ks = KeyStoreUtils.getInstance(this._customerStore.keyStoreFName, this._customerStore.keyStorePwd);
		//RSAPublicKey rsaPubKey = KeyStoreUtils.getRSAPublicKey(ks, this._customerStore.keyAlias);
		//KeyPair keyPair = AsymetricCryptoUtils.getKeyPair("BKS", this._customerStore.keyStoreFName, this._customerStore.keyStorePwd, this._customerStore.keyAlias);
		
		// Merchant signed on a byte array containing "service+previus group signature", so rebuilt it
		byte[] gsBytes =  _customerStore.getRedeemM1().getGroupSignature().serialize(this.getMSGFormat());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(gsBytes);
		baos.write(serv);
		
		// verify Merchant's RSA signature over baos contents
		// TODO: agafar clau public del certificat!!
		boolean ver = CryptoUtils.verifySign( baos.toByteArray() , 
										   sig, 
										   this._customerStore.rsaMerchantPubKey, 
										   CryptoUtils.SIGN_ALGORITHM);  
		
		if (false)
			throw new Exception("Customer::createMessageRedeem3::RSA verification not valid...");
		
		//_log.printConsoleSimple(this.getClass(), "RSA verification ok...");
		
				
		// extract set Bi+1,j,kj
		RedeemSetCouponsImpl bset = (RedeemSetCouponsImpl) getRedeemSetCoupons(couponsToBeSpend);
		
		// encrypt Bi+1,j,kj
		// encrypt bset
		byte[] bsetEncoded = bset.serialize(getMSGFormat()); 
		byte[] encrypted = CryptoUtils.encrypt(bsetEncoded, _customerStore.ks, CryptoUtils.createFastIv());
		

		// group sign Bi+1,j,kj (proof redeem coupon set)
		BBSSignatureMSG signature = getRedeemSetGroupSigned(bset,
															_idM,
															_idR);
		
		
		// compose message
		RedeemM3 m3 = new RedeemM3Impl();
		m3.setGroupSignature( signature );
		m3.setBRedeemSet( StringUtils.encodeBase64(encrypted) );
		m3.setIdR(m2.getIdR()); // TODO: no esta a l'especificació, posar o no?
		
		// store it
		this._customerStore.setRedeemM3((RedeemM3Impl) m3);
		
		return m3;
	}
	
	
	
	public void processMessageRedeem4(RedeemM4Impl m4) throws Exception {
		// decode the received merchant RSA signature
		byte[] sig = StringUtils.decodeBase64(m4.getMerchantSignature());
		
		// Merchant signed on a byte array containing the previous group signature, so rebuilt it
		byte[] gsBytes =  _customerStore.getRedeemM3().getGroupSignature().serialize(this.getMSGFormat());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(gsBytes);
		
		// verify signature
		boolean ver = CryptoUtils.verifySign( baos.toByteArray() , 
				   							  sig, 
				   							  this._customerStore.rsaMerchantPubKey, 
				   							  CryptoUtils.SIGN_ALGORITHM);  
	}

	
	
	
	
	/**
	 * Returns the RedeemSetCoupons object filled with the set of coupons to be redeemed. This method is used both aset and bset.
	 * 
	 * @param couponsToBeSpend contains the set of coupons either payment or proof
	 * @return RedeemSetCoupons to be used by the multi-redeem algorithm 
	 */
	private RedeemSetCoupons getRedeemSetCoupons(Hashtable<Integer, Integer> couponsToBeSpend) {
		// Hashtable: el primer Integer es j, i el segon es i. kj s'hauria de calcular saben quin es el darrer cupó gastat
		RedeemSetCouponsImpl rs = new RedeemSetCouponsImpl();
		Enumeration<Integer> keys = couponsToBeSpend.keys();
		while (keys.hasMoreElements()) {
			Integer j = keys.nextElement();
			Integer i = couponsToBeSpend.get(j);
			//_log.printConsoleSimple(this.getClass(), "Customer:returnRedeemSet: " + j + " " + i + " " + _customerStore.m2d + " " + _customerStore.mcpbs);
			Coupon coupon = _customerStore.m2d.getCoupon(j, i);
			RedeemCoupon rcoupon = new RedeemCouponImpl();
			rcoupon.setCoupon(coupon);
			rcoupon.setKj(i); // TODO: extreure fins a quin cupo esta gastat aquest multicupo i calcular kj corresponent
			rs.setRedeemCoupon(rcoupon);
		}
		return rs;
	}
	
	
	
	
	
	
	/******************************************************************************************
	 * 
	 * Protected and private methods for Issuing protocol between customer and issuer 
	 * 
	 ******************************************************************************************/
	
	
	protected void createMulticoupon2D(CommonInfo commonInfo) throws NoSuchAlgorithmException {
		commonInfo.getMCDescription();
		
		_customerStore.m2d = new Multicoupon2D();
		
		
		// for each 1 <= j <= J
		int J = commonInfo.getMCDescription().size();
		//System.out.println("how many multicoupons? " + J);
		for (int j = 0; j <= J-1; j++) {
			int Ij = commonInfo.getMCDescription().get(j).getNumber(); // number of coupons within multicoupon j
			Multicoupon mc = new MulticouponImpl(); // new multicoupon
			
			BigInteger wmu = RandomGeneratorUtils.getRandomModuleN(_customerStore.q); // seed coupon
			Coupon c = new CouponImpl();
			c.setHash( HashUtils.getHash(wmu) );
			c.setI(Ij);
			c.setJ(j);
			// store seed coupon inside multicoupon
			mc.getCoupons().add(c); 
			// set j row
			mc.setJ(j); 
			// initialize the first unused coupon
			mc.setFirstUnusedCoupon(1); 
			// to do hash chain
			Coupon previous = c; 
			// create the other coupons up to root coupon (i=0)
			for (int i = Ij-1; i >= 0; i--) { 
				c = new CouponImpl();
				c.setHash( HashUtils.getHash( previous.getHash() ) );
				c.setI(i);
				c.setJ(j);
				// add to multicoupon
				mc.getCoupons().add(c); 
				
				// to do hash chain
				previous = c;  
			}
			previous = null;
			c = null;
			
			// add row to the multicoupon2D
			_customerStore.m2d.getMulticoupon2D().add(reflect(mc)); 
		}
		
	}
	
	
	/**
	 * Because hash chain creation and store, seed coupon remains in the index "0" within the ArrayList of MulticouponXML.
	 * In order to bypass it, and put root coupon as the index "0", I need to reflect positions of the ArrayList
	 * 
	 * @param input object to be reflected
	 * */
	private Multicoupon reflect(Multicoupon input) {
		int Ij = input.getCoupons().size();
		ArrayList<Coupon> reflectedListOfCoupons = new ArrayList<Coupon>();
		for (int i = Ij-1; i >= 0; i--) {
			ArrayList<Coupon> al = input.getCoupons();
			CouponImpl c = (CouponImpl) al.get(i);
			reflectedListOfCoupons.add(c);
		}
		input.setCoupons(reflectedListOfCoupons);
		return input;
	}
	
	
	protected String concatenateRootCoupons() {
		String roots = "";
		
		int J = _customerStore.m2d.getMulticoupon2D().size();
		int j = 0;
		while (j <= J-1) {
			roots += " " + _customerStore.m2d.getCoupon(j, 0).getHash();
			j++;
		}
		return roots;
	}
	
	protected ArrayList<Coupon> getRootCoupons() {
		if (_customerStore.roots == null) {
			int J = _customerStore.m2d.getMulticoupon2D().size();
			_customerStore.roots = new ArrayList<Coupon>();
			for (int j = 0; j <= J-1; j++) {
				_customerStore.roots.add( _customerStore.m2d.getCoupon(j, 0) );
			}
		}
		return _customerStore.roots;
	}
	
	
	
	
	private void loadConfiguration() 
			throws IOException, 
				   KeyStoreException, 
				   NoSuchProviderException, 
				   NoSuchAlgorithmException, 
				   CertificateException, 
				   UnrecoverableKeyException, 
				   InvalidCipherTextException, 
				   InvalidKeySpecException { 
		
		// create an object to store data
		_customerStore = new CustomerStore();
		
		// load configuration from files
		LoadCfgUtils cfgConfig = new LoadCfgUtils(this.getClass().getResourceAsStream("/config.properties"));
		LoadCfgUtils cfgCustomer = new LoadCfgUtils(this.getClass().getResourceAsStream( "/customer.cfg" ));
		LoadCfgUtils cfgIssuerPublic = new LoadCfgUtils(this.getClass().getResourceAsStream( "/issuer_public.cfg" )); 
		
		this._customerGroupCertX509FileName = cfgConfig.read("customer_group_certx509"); 
		this._customerGroupUSKFileName = cfgConfig.read("customer_group_usk");
		this._issuerCertX509FileName = cfgConfig.read("issuer_rsacertx509");
		this._merchantCertX509FileName = cfgConfig.read("merchant_rsacertx509");
		
		// load q parameter
		this._customerStore.q = new BigInteger( cfgCustomer.read("q") );
		
		// load keystore
		this._customerStore.keyStoreFName = cfgCustomer.read("bks");
		this._customerStore.keyStorePwd = cfgCustomer.read("pwd");
		this._customerStore.keyAlias = cfgCustomer.read("alias");
		
		// load Issuer's RSA public key parameters (e,n) [now, loaded from certificate]
		//this._customerStore.rsaEIssuer = new BigInteger( cfgIssuerPublic.read("rsa_e") );
		//this._customerStore.rsaNIssuer = new BigInteger( cfgIssuerPublic.read("rsa_n") );
		
		// load symmetric key from configuration file
		BigInteger ks = new BigInteger( cfgCustomer.read("ks") );
		this._customerStore.ks = new SecretKeySpec( ks.toByteArray() ,"AES");

		//RSAPublicKey rsaPublicKey = new RSAPublicKey(_customerStore.rsaNIssuer, _customerStore.rsaEIssuer); // TODO: make use of Issuer key pair... it should be customer key instead
		System.out.println("filename: " + this.rootFolder+this._customerStore.keyStoreFName + " " + this._customerStore.keyStorePwd + " " + this._customerStore.keyAlias);
		
		
		String keyStoreType = cfgCommon.read("keystoretype");
		String provider = cfgCommon.read("provider");
		
		KeyStore keyStore = KeyStoreUtils.getInstance(this.rootFolder + this._customerStore.keyStoreFName + "." + keyStoreType.toLowerCase(), this._customerStore.keyStorePwd, keyStoreType, provider); 
		this._customerStore.rsaPrivKey = KeyStoreUtils.getRSAPrivateKey(keyStore, this._customerStore.keyAlias, this._customerStore.keyStorePwd);
		this._customerStore.rsaPubKey = KeyStoreUtils.getRSAPublicKey(keyStore, this._customerStore.keyAlias);
		
		
		_customerStore.certUserASN1 = CertificateUtils.createGPKx509("", 365, this._customerStore.rsaPubKey.getEncoded());
		
		//this._customerStore.rsaIssuerPubKey = (RSAPublicKey) KeyStoreUtils.getRSAPublicKey(keyStore, "certIssuer1024"); //(I do not know why it is null...)
		//System.out.println("*******" + this._customerStore.rsaIssuerPubKey);
		this._customerStore.rsaIssuerPubKey = CertificateUtils.getRSAPublicKey( this.rootFolder + "public/" + this._issuerCertX509FileName  );
		
		this._customerStore.rsaMerchantPubKey = CertificateUtils.getRSAPublicKey( this.rootFolder + "public/" + this._merchantCertX509FileName );
		
		
		this._hostServers = super.cfgCommon.read("host");
		this._issuerPort = Integer.valueOf( super.cfgCommon.read("port_issuer") );
		this._merchantPort = Integer.valueOf( super.cfgCommon.read("port_merchant") );
		this._gmanagerPort = Integer.valueOf( super.cfgCommon.read("port_gmanager") );
	}
	
	
	

}
