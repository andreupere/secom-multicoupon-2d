package cat.uib.secom.multicoupon2d.common;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


import cat.uib.secom.multicoupon2d.common.msg.CommonInfo;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM4Impl;
import cat.uib.secom.utils.strings.MSGFormatConstants;

public class TestIssuingMessaging {
	
	private static CommonInfo commonInfo;
	private static BigInteger alpha = new BigInteger("24");
	private static BigInteger lambda = new BigInteger("71");
	private static BigInteger beta = new BigInteger("11");
	private static BigInteger varphi = new BigInteger("67");
	private static BigInteger gamma = new BigInteger("97");
	
	private static Integer sessionID = new Integer(1);
	
	
	protected static IssuingM1Impl m1;
	protected static IssuingM2Impl m2;
	protected static IssuingM3Impl m3;
	protected static IssuingM4Impl m4;
	
	
	@BeforeClass
	public static void init() {
		commonInfo = MockObjects.createCommonInfo(); 
		m1 = new IssuingM1Impl();
		m1.setAlpha(alpha);
		m1.setCommonInfo(commonInfo);
		
		m2 = new IssuingM2Impl();
		m2.setLambda(lambda);
		m2.setSessionID(sessionID);
		
		m3 = new IssuingM3Impl();
		m3.setBeta(beta);
		m3.setSessionID(sessionID);
		
		m4 = new IssuingM4Impl();
		m4.setGamma(gamma);
		m4.setSessionID(sessionID);
		m4.setVarphi(varphi);
		
	}
	
	@Test
	public void serialize() throws Exception {
		s(MSGFormatConstants.XML);
		s(MSGFormatConstants.ASN1);
	}

	@Ignore
	private void s(MSGFormatConstants msgFormat) throws Exception {
		
		m1.setMSGFormat(msgFormat);
		m2.setMSGFormat(msgFormat);
		m3.setMSGFormat(msgFormat);
		m4.setMSGFormat(msgFormat);
		
		System.out.println("message format: " + m1.getMSGFormat());
		
		
		byte[] input = m1.serialize(msgFormat);
		IssuingM1Impl m1r = new IssuingM1Impl();
		m1r.setMSGFormat(msgFormat);
		m1r = (IssuingM1Impl) m1r.deSerialize(input, msgFormat);
		Assert.assertEquals(m1.getAlpha(), m1r.getAlpha());
		
		
		input = m2.serialize(msgFormat);
		IssuingM2Impl m2r = new IssuingM2Impl();
		m2r.setMSGFormat(msgFormat);
		m2r = (IssuingM2Impl) m2r.deSerialize(input, msgFormat);
		Assert.assertEquals(m2.getLambda(), m2r.getLambda());
		Assert.assertEquals(m2.getSessionID(), m2r.getSessionID());
		
		input = m3.serialize(msgFormat);
		IssuingM3Impl m3r = new IssuingM3Impl();
		m3r.setMSGFormat(msgFormat);
		m3r = (IssuingM3Impl) m3r.deSerialize(input, msgFormat);
		Assert.assertEquals(m3.getBeta(), m3r.getBeta());
		Assert.assertEquals(m3.getSessionID(), m3r.getSessionID());
		
		input = m4.serialize(msgFormat);
		IssuingM4Impl m4r = new IssuingM4Impl();
		m4r.setMSGFormat(msgFormat);
		m4r = (IssuingM4Impl) m4r.deSerialize(input, msgFormat);
		Assert.assertEquals(m4.getGamma(), m4r.getGamma());
		Assert.assertEquals(m4.getVarphi(), m4r.getVarphi());
		Assert.assertEquals(m4.getSessionID(), m4r.getSessionID());
	}
	
	
//	@Test
//	public void asn1() {
//		byte[] b = m1.encodeASN1();
//		IssuingM1Impl m1r = new IssuingM1Impl();
//		m1r.decodeASN1(b);
//		Assert.assertEquals(m1.getAlpha(), m1r.getAlpha());
//		
//		
//		b = m2.encodeASN1();
//		IssuingM2Impl m2r = new IssuingM2Impl();
//		m2r.decodeASN1(b);
//		Assert.assertEquals(m2.getLambda(), m2r.getLambda());
//		Assert.assertEquals(m2.getSessionID(), m2r.getSessionID());
//		
//		
//		b = m3.encodeASN1();
//		IssuingM3Impl m3r = new IssuingM3Impl();
//		m3r.decodeASN1(b);
//		Assert.assertEquals(m3.getBeta(), m3r.getBeta());
//		Assert.assertEquals(m3.getSessionID(), m3r.getSessionID());
//		
//		
//		b = m4.encodeASN1();
//		IssuingM4Impl m4r = new IssuingM4Impl();
//		m4r.decodeASN1(b);
//		Assert.assertEquals(m4.getGamma(), m4r.getGamma());
//		Assert.assertEquals(m4.getVarphi(), m4r.getVarphi());
//		Assert.assertEquals(m4.getSessionID(), m4r.getSessionID());
//		
//	}
	
}
