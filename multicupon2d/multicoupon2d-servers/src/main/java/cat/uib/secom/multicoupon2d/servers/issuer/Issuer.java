package cat.uib.secom.multicoupon2d.servers.issuer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Hashtable;



import cat.uib.secom.multicoupon2d.common.cfg.Constants;
import cat.uib.secom.multicoupon2d.common.entity.Entity;
import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM1;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM2;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM3;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM4;
import cat.uib.secom.multicoupon2d.common.msg.impl.ClaimM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.ClaimM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.ClaimM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.ClaimM4Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.CommonInfoImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM4Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.MCPBSImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemSetCouponsImpl;
import cat.uib.secom.multicoupon2d.servers.session.IssuingSessionHandler;
import cat.uib.secom.security.CertificateUtils;
import cat.uib.secom.security.CryptoUtils;
import cat.uib.secom.security.HashUtils;
import cat.uib.secom.security.KeyStoreUtils;
import cat.uib.secom.security.RandomGeneratorUtils;
import cat.uib.secom.utils.networking.ServerDefaultImpl;
import cat.uib.secom.utils.strings.LoadCfgUtils;
import cat.uib.secom.utils.strings.PerformanceUtils;
import cat.uib.secom.utils.strings.StringUtils;

public class Issuer extends Entity {

	
	
	public class IssuerServerImpl extends ServerDefaultImpl {
		
		
	
		public IssuerServerImpl(Integer port) {
			super(port);
		}

		public void listen() throws IOException {
			while (_listening) {
				Socket socket = _ss.accept();
				new IssuerThread(socket, _issuer).run();
			}
		}
	}
	
	
	
	private static Issuer _issuer;
	
	private static Hashtable<Integer, IssuingSessionHandler> sessions = new Hashtable<Integer, IssuingSessionHandler>();
	

	private BigInteger _rsaN; 
	private BigInteger _rsaD; 
	
	private Integer _sessionID = 0;
	
	private IssuerStore _issuerStore;
	
	
	private Issuer() throws FileNotFoundException, 
						    IOException, 
						    Multicoupon2DException, 
						    KeyStoreException, 
						    NoSuchProviderException, 
						    NoSuchAlgorithmException, 
						    CertificateException, 
						    UnrecoverableKeyException, 
						    InvalidKeySpecException  {
		super();
		System.out.println("Init issuer server...");
		
		_issuerStore = new IssuerStore();
		
		LoadCfgUtils cfgConfig = new LoadCfgUtils(this.getClass().getResourceAsStream("/config.properties"));
		LoadCfgUtils cfgIssuerPublic = new LoadCfgUtils(this.getClass().getResourceAsStream( "/issuer.cfg" ));
		//LoadCfgUtils cfgIssuerPrivate = new LoadCfgUtils(this.getClass().getResourceAsStream( "/issuer_private.cfg" ));
		
		// load RSA public and private keys
		//this._rsaN = new BigInteger( cfgIssuerPublic.read("rsa_n") );
		//this._rsaD = new BigInteger( cfgIssuerPrivate.read("rsa_d") );
		
		this._issuerStore.keyAlias = new String( cfgIssuerPublic.read("alias") );
		this._issuerStore.keyStoreFName = new String( cfgIssuerPublic.read("bks") ); 
		this._issuerStore.keyStorePwd = new String( cfgIssuerPublic.read("pwd") );
		
		KeyStore keyStore = KeyStoreUtils.getInstance(this.rootFolder + this._issuerStore.keyStoreFName + ".jks", this._issuerStore.keyStorePwd, "jks", "SUN");
		this._issuerStore.rsaPrivKey = KeyStoreUtils.getRSAPrivateKey(keyStore, this._issuerStore.keyAlias, this._issuerStore.keyStorePwd);
		this._issuerStore.rsaPubKey = KeyStoreUtils.getRSAPublicKey(keyStore, this._issuerStore.keyAlias);
		
		this._rsaN = this._issuerStore.rsaPrivKey.getModulus();
		this._rsaD = this._issuerStore.rsaPrivKey.getPrivateExponent();
		//this._issuerStore.rsaPubKey.getPublicExponent();
		
		this._issuerStore.rsaMerchantPubKey = CertificateUtils.getRSAPublicKey(this.rootFolder + "public/" + cfgConfig.read("merchant_rsacertx509"));
		
		
		// for performance purposes
		_performanceFN = getRootFolder() + "performance/" + "issuer_" + (new Date()).getTime() + ".txt";
		
		_pu = new PerformanceUtils();
		_pu.addComments( "Customer issues a new multicoupon + merchant claims redeemed coupons" );
		_pu.addComments( (new Date()).toString() );
		_pu.addComments( "-" );
		_pu.addComments( getMSGFormat().toString() );
		_pu.addComments( "Time in miliseconds (ms)" );
		_pu.addComments("t1 \t receiving M1 (issue) or M1 (claim) or M3 (claim)");
		_pu.addComments("t2 \t decoding M1 (issue)");
		_pu.addComments("t3 \t managing M1 and building M2 (issue)");
		_pu.addComments("t4 \t encoding M2 (issue)");
		_pu.addComments("t5 \t sending M2 (issue)");
		_pu.addComments("t6 \t receiving M3 (issue)");
		_pu.addComments("t7 \t decoding M3 (issue)");
		_pu.addComments("t8 \t managing M3 and building M4 (issue)");
		_pu.addComments("t9 \t encoding M4 (issue)");
		_pu.addComments("t10 \t sending M4 (issue)");
		
		_pu.addComments("t11 \t decoding M1 (claim)");
		_pu.addComments("t12 \t managing M1 and building M2 (claim)");
		_pu.addComments("t13 \t encoding M2 (claim)");
		_pu.addComments("t14 \t sending M2 (claim)");
		_pu.addComments("t15 \t decoding M3 (claim)");
		_pu.addComments("t16 \t managing M3 and building M4 (claim)");
		_pu.addComments("t17 \t encoding M4 (claim)");
		_pu.addComments("t18 \t sending M4 (claim)");
		_pu.addComments("proto \t it \t t1 \t t2 \t t3 \t t4 \t t5 \t t6 \t t7 \t t8 \t t9 \t t10 \t t11 \t t12 \t t13 \t t14 \t t15 \t t16 \t t17 \t t18");
				
		
	}
	
	
	public static Issuer getInstance() throws FileNotFoundException, 
											  IOException, 
											  Multicoupon2DException, 
											  UnrecoverableKeyException, 
											  KeyStoreException, 
											  NoSuchProviderException, 
											  NoSuchAlgorithmException, 
											  CertificateException, 
											  InvalidKeySpecException {
		if (_issuer == null)
			_issuer = new Issuer();
		return _issuer;
	}
	
	
	public IssuingM2 receiveMessage1Issuing(IssuingM1 m1) throws Exception {
		// (commoninfo, alpha) first message is received... go to process it
		
		// TODO: read and verify common info
		
		// read and verify expiration time
		// is not expired?
		if ( m1.getCommonInfo().getExpiration().before(new Date()) )
			throw new Exception("Common info expired..."); 
		
		
		increaseSessionID();
		
		BigInteger lambda = RandomGeneratorUtils.getRandomModuleN(_issuer._rsaN);
		
		
		// compose Issuing M2
		IssuingM2 m2 = new IssuingM2Impl();
		m2.setLambda( lambda );
		m2.setSessionID( getSessionID() ); 
		
		// TODO: control de sessio!
		IssuingSessionHandler s = new IssuingSessionHandler();
		s.setMessage1(m1);
		s.setMessage2(m2);
		sessions.put( getSessionID() , s);
		
		return m2;
	}
	
	public IssuingM4 receiveMessage3Issuing(IssuingM3 m3) throws Exception {
		
		// extract session information
		Integer session = m3.getSessionID();
		IssuingSessionHandler s = sessions.get(session);
		
		if (s == null)
			throw new Exception("Session ID not found");
		
		s.setMessage3(m3);
		
		// calculate varphi (inverse beta)
		BigInteger varphi = s.getMessage3().getBeta().modInverse(_issuer._rsaN);
		
		// calculate gamma
		CommonInfoImpl ciImpl = (CommonInfoImpl) s.getMessage1().getCommonInfo();
		BigInteger gamma1 = HashUtils.getHash( ciImpl.toString() ).modPow(_issuer._rsaD, _issuer._rsaN);
		BigInteger gamma21 = s.getMessage2().getLambda().modPow(new BigInteger("2"), _issuer._rsaN).add(BigInteger.ONE).mod(_issuer._rsaN);
		BigInteger gamma22 = s.getMessage3().getBeta().modInverse(_issuer._rsaN).modPow(new BigInteger("2"), _issuer._rsaN);
		
		BigInteger gamma2 = s.getMessage1().getAlpha().multiply(gamma21).mod(_issuer._rsaN);
		gamma2 = gamma2.multiply(gamma22).mod(_issuer._rsaN);
		gamma2 = gamma2.modPow(_issuer._rsaD.multiply(new BigInteger("2")), _issuer._rsaN);
		
		BigInteger gamma = gamma1.multiply(gamma2).mod(_issuer._rsaN);  
		
		
		
		IssuingM4 m4 = new IssuingM4Impl();
		m4.setSessionID( session );
		m4.setGamma(gamma);
		m4.setVarphi(varphi);
		
		s.setMessage4(m4);
		
		return m4;
	}
	
	
	public Integer increaseSessionID() {
		return _sessionID++;
	}
	public Integer getSessionID() {
		return _sessionID;
	}
	
	
	
	
	public ClaimM2Impl receiveClaimM1(ClaimM1Impl claimM1) 
			throws Exception { 
		
		// check if M belongs affiliation
		// TODO
		
		// check signature on r1
		byte[] arset = StringUtils.decodeBase64( claimM1.getaRedeemSet() );
		byte[] arsetGSigned = claimM1.getGroupSignature().serialize(getMSGFormat());
		byte[] mcpbs = ((MCPBSImpl)claimM1.getMcpbs()).serialize(getMSGFormat());
		
		byte[] toVerify = StringUtils.concatenate(arset, arsetGSigned, mcpbs);
		
		// verify Merchant's RSA signature over baos contents
		// TODO: agafar clau public del certificat!!
		boolean ver = CryptoUtils.verifySign( toVerify , 
										   StringUtils.decodeBase64( claimM1.getSignature() ), 
										   this._issuerStore.rsaMerchantPubKey, 
										   CryptoUtils.SIGN_ALGORITHM);  	
		
		if (false)
			throw new Exception("Issuer::CLAIM.M1::RSA verification not valid...");
		
		
		
		
		
		// verify timestamp
		if ((new Date()).after(claimM1.getMcpbs().getCommonInfo().getClaim()) )
			throw new Exception("Multicoupon expired... (" + claimM1.getMcpbs().getCommonInfo().getExpiration() + ")");
		
		// verify number of coupons
		// TODO
		

		// verifies mcpbs
		MCPBSImpl mcpbsImpl = (MCPBSImpl) claimM1.getMcpbs();
		boolean verification = mcpbsImpl.verify(_issuerStore.rsaPubKey.getPublicExponent(), 
											_issuerStore.rsaPubKey.getModulus(), 
											getMSGFormat());
		
		// throw and exception if PBS verification fails
		if (!verification)
			throw new Exception("PBS verification failed...");

		// check if any coupon are already used
		RedeemSetCouponsImpl rsci = new RedeemSetCouponsImpl();
		
		// deserialize object according to the preconfigured MSGFormat
		rsci = (RedeemSetCouponsImpl) rsci.deSerialize(arset, getMSGFormat());
		
		ver = verifyCouponsHelper(rsci, claimM1.getMcpbs());
		if (false)
			throw new Exception("ISSUER::CLAIM.M1::Coupons verification failed...");

		// sign response
		byte[] response = Constants.ACCEPTED.getBytes();
		byte[] merchantPrevSign = StringUtils.decodeBase64( claimM1.getSignature() );
		byte[] toSign = StringUtils.concatenate(response, merchantPrevSign);
		
		byte[] signed = CryptoUtils.signData(toSign, this._issuerStore.rsaPrivKey, null);
		
		
		
		// compose message claimM2
		ClaimM2Impl claimM2 = new ClaimM2Impl();
		claimM2.setResponse(Constants.ACCEPTED);
		claimM2.setSignature( StringUtils.encodeBase64(signed) );
		
		return claimM2;
	}
	
	public ClaimM4Impl receiveClaimM3(ClaimM3Impl claimM3) 
			throws Exception { 
		
		// check signature on r2
		byte[] brset = StringUtils.decodeBase64( claimM3.getbRedeemSet() );
		byte[] brsetGSigned = claimM3.getGroupSignature().serialize(getMSGFormat());
		byte[] mcpbs = ((MCPBSImpl)claimM3.getMcpbs()).serialize(getMSGFormat());
		
		byte[] toVerify = StringUtils.concatenate(brset, brsetGSigned, mcpbs);
		
		// verify Merchant's RSA signature over baos contents
		// TODO: agafar clau publica del certificat!!
		boolean ver = CryptoUtils.verifySign( toVerify , 
										   StringUtils.decodeBase64( claimM3.getSignature() ), 
										   this._issuerStore.rsaMerchantPubKey, 
										   CryptoUtils.SIGN_ALGORITHM);  	
		
		if (false)
			throw new Exception("Issuer::CLAIM.M3::RSA verification not valid...");
		
		// verify coupons
		// verify coupon reuse
		RedeemSetCouponsImpl rsci = new RedeemSetCouponsImpl();
		
		// deserialize object according to the preconfigured MSGFormat
		rsci = (RedeemSetCouponsImpl) rsci.deSerialize(brset, getMSGFormat());
		
		ver = verifyCouponsHelper(rsci, claimM3.getMcpbs());
		if (!ver)
			throw new Exception("ISSUER::CLAIM.M3::Coupons verification failed...");

		// inits deposit (out of scope)
		
		
		// sign response
		byte[] response = Constants.ACCEPTED.getBytes();
		byte[] merchantPrevSign = StringUtils.decodeBase64( claimM3.getSignature() );
		byte[] toSign = StringUtils.concatenate(response, merchantPrevSign);
		byte[] signed = CryptoUtils.signData(toSign, this._issuerStore.rsaPrivKey, null);
		
		ClaimM4Impl claimM4 = new ClaimM4Impl();
		claimM4.setResponse(Constants.ACCEPTED);
		claimM4.setSignature( StringUtils.encodeBase64(signed) ); 
		
		return claimM4;
	}
	
	

}
