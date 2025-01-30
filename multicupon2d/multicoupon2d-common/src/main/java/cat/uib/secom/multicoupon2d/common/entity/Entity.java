package cat.uib.secom.multicoupon2d.common.entity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Iterator;

import cat.uib.secom.crypto.sig.bbs.core.accessors.enhanced.SignerAccessor;
import cat.uib.secom.crypto.sig.bbs.core.accessors.enhanced.VerifierAccessor;
import cat.uib.secom.crypto.sig.bbs.core.engines.BBSEngine;
import cat.uib.secom.crypto.sig.bbs.core.engines.BBSEnginePrecomputation;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKeyImpl;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKeyImpl;
import cat.uib.secom.crypto.sig.bbs.core.impl.signature.BBSSignatureImpl;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupPublicKeyMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSReaderFactory;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSSignatureMSG;
import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.common.msg.MCPBS;
import cat.uib.secom.multicoupon2d.common.msg.RedeemCoupon;
import cat.uib.secom.multicoupon2d.common.msg.RedeemSetCoupons;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemSetCouponsImpl;
import cat.uib.secom.security.HashUtils;
import cat.uib.secom.utils.strings.LoadCfgUtils;
import cat.uib.secom.utils.strings.LoggingUtils;
import cat.uib.secom.utils.strings.MSGFormatConstants;
import cat.uib.secom.utils.strings.PerformanceUtils;

public class Entity {
	
	protected MSGFormatConstants format;
	
	protected LoggingUtils _log;
	
	protected BBSParameters _bbsParameters;
	
	protected String rootFolder;
	
	protected SignerAccessor signerAccessor;
	
	// only for performance purposes
	/**
	 * Object managing performance results
	 * */
	protected PerformanceUtils _pu;
	
	/**
	 * Path and filename of file containing performance results
	 * */
	protected String _performanceFN;
	
	protected LoadCfgUtils cfgCommon;
		
	private static final String MSGFORMAT_EXCEPTION_MSG = "Message format not set...";
	private static final String LOGGING_EXCEPTION_MSG = "Logging exception...";
	
	public Entity(InputStream commonConfigInputStream) throws FileNotFoundException, IOException, Multicoupon2DException {
		cfgCommon = new LoadCfgUtils( commonConfigInputStream );		
		loadConfig(cfgCommon);
	}
	
	public Entity() throws FileNotFoundException, IOException, Multicoupon2DException {
		cfgCommon = new LoadCfgUtils(this.getClass().getResourceAsStream( "/common.cfg" ));
		
		loadConfig(cfgCommon);
		
		
				
	}

	public MSGFormatConstants getMSGFormat() {
		return this.format;
	}
	
	public boolean isLoggingActive() {
		return this._log.getActive();
	}
	
	public void enableLogging(boolean b) {
		_log = LoggingUtils.getInstance(b);
	}
	
	
	public String getRootFolder() {
		return this.rootFolder;
	}
	
	/**
	 * Helper method that returns the coupon set (either aset or bset) group signed
	 * */
	protected BBSSignatureMSG getRedeemSetGroupSigned(RedeemSetCouponsImpl rs, 
													  String idM, 
													  String idR) 
															  throws Exception {

		
		
		BBSSignatureImpl signature = signerAccessor.sign( toBeSigned( rs.toString(), idM, idR ) );
		
		return new BBSSignatureMSG(signature);
		
	}
	
	protected boolean verifyGroupSignatureHelper(BBSGroupPublicKeyMSG groupPublicKey, 
				   							     BBSSignatureMSG bbsGroupSignature, 
				   							     RedeemSetCouponsImpl redeemSet,
				   							     String idR,
				   							     String idM) 
				   							    		 throws Exception {
		
		
		BBSGroupPublicKeyImpl bbsGpk = (BBSGroupPublicKeyImpl) BBSReaderFactory.getBBS(groupPublicKey, _bbsParameters); 
		
		BBSSignatureImpl bbsSignature = (BBSSignatureImpl) BBSReaderFactory.getBBS(bbsGroupSignature, _bbsParameters);
		
		VerifierAccessor va = new VerifierAccessor(new BBSEngine(bbsGpk));
		
		va.setGroupPublicKey(bbsGpk);
		
		boolean verification = va.verify(bbsSignature, toBeSigned(redeemSet.toString(), idM, idR) );
		
		if (!verification) 
			throw new Exception("Group Signature verification failed...");
		
		return verification;
	}
	
	
	
	
	protected boolean verifyCouponsHelper(RedeemSetCoupons rs, MCPBS mcpbs) throws Exception {
		Iterator<RedeemCoupon> it = rs.getRedeemCoupons().iterator();
		while (it.hasNext()) {
			RedeemCoupon rcxml = it.next();
			Integer i = rcxml.getI(); // controla quantes vegades repetir hash fins a root (o darrer rebut i guardat previament) TODO: tenir en compte kj
			BigInteger rootCouponHash = mcpbs.getRootCoupons().get(rcxml.getJ()).getHash();
			BigInteger couponHash = rcxml.getHash();
			while (i>0) {
				couponHash = HashUtils.getHash(couponHash);
				i--;
			}
			// check last coupon = root coupon
			if (!couponHash.equals( rootCouponHash ))
				return false;
		}
		return true;
	}
	
	
	
	
	private String toBeSigned(String s1, String s2, String s3) {
		return s1 + s2 + "\r\n" + s3;
	}
	
	
	private void loadConfig(LoadCfgUtils cfgCommon) throws Multicoupon2DException { 
		// load message format
		String f = cfgCommon.read("msg_format");
		if (f.equals("XML"))
			format = MSGFormatConstants.XML;
		else if (f.equals("ASN1"))
			format = MSGFormatConstants.ASN1;
		else
			throw new Multicoupon2DException(MSGFORMAT_EXCEPTION_MSG);
		
		// load whether loggig is active or not
		String l = cfgCommon.read("log_active");
		if (l.equals("1")) {
			_log = LoggingUtils.getInstance(true);
		}
		else if (l.equals("0"))
			_log = LoggingUtils.getInstance(false);
		else
			throw new Multicoupon2DException(LOGGING_EXCEPTION_MSG);
		
		// load elliptic curve parameters file
		String curve = cfgCommon.read("bbs_curve");
		_bbsParameters = new BBSParameters(curve);
		
		// load folder where data is stored
		this.rootFolder = cfgCommon.read("root_folder");
		
		

	}
	
	
	
	
	public PerformanceUtils getPU() {
		return this._pu;
	}
	public String getPerformanceFN() {
		return this._performanceFN;
	}
	
	
}
