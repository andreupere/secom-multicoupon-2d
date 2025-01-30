package cat.uib.secom.multicoupon2d.common;

import java.math.BigInteger;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;


import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.util.ASN1Dump;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.hamcrest.CoreMatchers;

import cat.uib.secom.crypto.sig.bbs.marshalling.BBSGroupPublicKeyMSG;
import cat.uib.secom.crypto.sig.bbs.marshalling.BBSSignatureMSG;
import cat.uib.secom.multicoupon2d.common.msg.CommonInfo;
import cat.uib.secom.multicoupon2d.common.msg.Coupon;
import cat.uib.secom.multicoupon2d.common.msg.MCDescription;
import cat.uib.secom.multicoupon2d.common.msg.MCPBS;
import cat.uib.secom.multicoupon2d.common.msg.RedeemM1;
import cat.uib.secom.multicoupon2d.common.msg.impl.CommonInfoImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.CouponImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.MCDescriptionImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.MCPBSImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.RedeemM1Impl;
import cat.uib.secom.utils.strings.MSGFormatConstants;

public class TestMessagesASN {

	@Rule
	public ErrorCollector collector = new ErrorCollector();
	
	@Test
	public  void testASN1() {
		// TODO Auto-generated method stub
		
		try {
	        BouncyCastleProvider bcp = new BouncyCastleProvider();
	        Security.insertProviderAt(bcp,1);
	        
		   /*
		    * TEST M1RedeemASN
		    */
	        
			MCDescription mcdDesc1 = new MCDescriptionImpl();
			mcdDesc1.setJ(0);
			mcdDesc1.setNumber(2);
			mcdDesc1.setValue(new String("10"));
			
			MCDescription mcdDesc2 = new MCDescriptionImpl();
			mcdDesc2.setJ(1);
			mcdDesc2.setNumber(2);
			mcdDesc2.setValue(new String("20"));
			
			ArrayList<MCDescription> al = new ArrayList<MCDescription>();
			al.add(mcdDesc1);
			al.add(mcdDesc2);  
	        
	        CommonInfo ci = new CommonInfoImpl();
	        ci.setIssuerID(new Integer("1"));
	        ci.setServiceID(new Integer("2"));
	        ci.setMCDescription(al);
	        ci.setExpiration(new Date());
	        ci.setClaim(new Date());
	        ci.setRefund(new Date());  
	        
	        //ASN1Object ciASN = ci.toASNObject();
	        
	    	 String t1 = "03049723652e08b1f1be2b4a879acbb7d9c0992d31e8134f7e1333f4d42828f1684628038a4f67e16eafd725";
	    	 String t2 = "0dea72d5c03338aa4725a6ef2674a8149fa3c743180d1474836990449ee48e000d9e00f8864d364e35820c02";
	    	 String t3 = "25466db9d18368bf32a6127ed0bb63c3a6079560d65d317095749c91d151ab3e66c97c5f92bf7832ee6317bd";
	    	 String c = "32f7e6a29cc7445090e71607161426518064b7d780";
	    	 String salpha = "40aab4e02bd0ed44e455a62866089ee4a946afe518";
	    	 String sbeta = "3b5b3a6b4edf22e6c53f0ff36d5126b27cd6244525";
	    	 String sx = "46b1d6946c7debd0d81c293842608efd8c2721fbc7";
	    	 String sdelta1 = "2e653ea6aff5c4c4bf7e7d5ff85a2fb8006e2bc7a4";
	    	 String sdelta2 = "426c4958aa05b1551586a9c3602d88d17d5b7e261e";
	         
	    	 BBSSignatureMSG bbs = new BBSSignatureMSG();
	    	 bbs.setT1(t1);
	    	 bbs.setT2(t2);
	    	 bbs.setT3(t3);
	    	 bbs.setC(c);
	    	 bbs.setSalpha(salpha);
	    	 bbs.setSbeta(sbeta);
	    	 bbs.setSx(sx);
	    	 bbs.setSdelta1(sdelta1);
	    	 bbs.setSdelta2(sdelta2);
	    	 
	    	String g1 = "0ce0c46de3416c93cc78a8f6989622fe6607c05292d22414276b10b907dcac9ca076ca025ca86e0d5b60cf40";
	    	String g2 = "1d2d138ad8aaabfd91e333f647933f86f5e9010aaa2c413f12e4fa48be50bbb4c2c717aefdfd3f02956483ab3184d57d8b391fcbe8d86044da0154a49ca251aa78db2df6c90c608c5ba15ef84159eb65caa02a707c2fa03b02a9ba9275c8d2775c924cb195b140a5ab0b4fab12ed378070d519305c28bf10bede48e62c55a41d98beedf9";
	    	String h = "2141a6657108cf6e7c7a8012a64836302d5b9beacb09290ae7c4d10dad4ec17f9b4b4d8ef8f87d9fb2145dd0";
	    	String u = "12a6efce0c2907c5b619347b72da72ca28903ef4710d0f16bc144b421c7190e7d16bf1387dfe0346b662e743";
	    	String v = "1a7a881f15fa0c1aa49ecbb44c8f8bd3626d5b0031d61a0d99ad5be7d2b9fca5457f2d4eb38f34824a2e8ac5";
	    	String omega = "41ffcb30f12a6cbaa17791f05dedc07ffbc2125f2d603c107083e561535ad957c4356a2d32dcc05c793ae27d25a504f3b2ecb9acdb0e4bd1c5c60331ba2767e9ac32098975a1b07fb070f69c22a5cdbc3fabe89854fbf83703efaea5c5ba8097cf1043c7d97b1bb4c90b326321d72a539d49ada11aa3035f2a565df15ec81086e6b3a79b";
	    	
	    	BBSGroupPublicKeyMSG pkASN = new BBSGroupPublicKeyMSG();
	    	pkASN.setG1(new String("0ce0c46de3416c93cc78a8f6989622fe6607c05292d22414276b10b907dcac9ca076ca025ca86e0d5b60cf40"));
	    	pkASN.setG2(g2);
	    	pkASN.setH(h);
	    	pkASN.setU(u);
	    	pkASN.setV(v);
	    	pkASN.setOmega(omega);
	    	 
	    	String aRedeemSet = "PJ1SCgBsUep+bb4gt4iNd61DfC9+XR/TiJzx/EWwsRjpqS1BBlbzvQ9EWYSegVBkPvpC8LbcEzIlTbkZ6s+GjNoAwOA9fPKgXqj0uLV/NSBYDPu6IOHv0OL4/hU156RbqH2XunAE0PAru11XJdSslwZUF0vPgNCRfhaUhMegB1ksWPTNK98kkNIKtCIof7xBNmQ+e/UJ2ace9FzQD75MRZDUZrTCQr1cIELxAYMmmBXL4KOe6pzrRHSrwK2v0IOS4OduNDQNwkh+V3NEFDAG/IBgj+q5y675s4pZ99rtGzeKdOj3xqeqcXZ7zZvwVAcY4/z2Owx+g9cnpQqP1OrgVQ==";
	    	
	    	Coupon coupon = new CouponImpl();
	    	coupon.setJ(new Integer("0"));
	    	coupon.setI(new Integer("0"));
	    	coupon.setHash(new BigInteger("106063637449099684256462027834573247598544603666"));
			
			ArrayList<Coupon> cl = new ArrayList<Coupon>();
			cl.add(coupon);
	
			BigInteger delta =  new BigInteger("221272052699952315008237164498071692369060957581810727838900761766873376617641299329650908387521825205966350848890832108202851761979779119478444933476784374825782496079442011330210540070250278944246086844239453650415153923488300050860112277303084681974734019101871929570137507942562166246898742783122109330442185825002667906690965962598718584982447689905345163960740165669605657364767307422275366029338026604283509751627456049296739221142078031925131991635863433248288578770292778603277433398782307198122619494141346023049457518738060191914024329847918880540330594306048246360888239868983279890177600536940037510055097669991352431715262707627141729538190923329116046344373435490657237447750140760474848413497368336896833557978433437752577140293026636229247428550603260642955353098194108737395631483039020056381477740538582923794460847400175062744626254173848281096241487729736182831257505896023458238674363984526836275260853319842908609665183545530820973675592271762187642052457133334077968754250143930988463760161795320035739525238546912893235215726371336431159028702945302632044187100005765878369740518280184354608400032383821828268979684839835813551120633893376831576821780526644212771889951549092789119675302713557350498771973504");
			BigInteger omega2 = new BigInteger("141571690090946226040666734452087609969958324291232886272361422890793644326442160046296635764152831606827047501331485357630467613404078414338635831768797655504486739553293103864367221339575939429767242708018121582705219998865837781153174377135680831960247349108115093158534482747304157197607215646745139691716170139709811842637640957195427160264053715892411471912352063064059543843327951937653167665034114253215419041431856330818446510151013354096247980114437686675456112257059089074872134922172109990973583463049789620215496446056484217204418368128024182044522865237640174531996277250497560025489391139661650683578906399749970782806166351558234925881578096425410045754766787094590783797321685598681277281099056725801436041007360440042543334912672595485025550222965697865484473329750588479428517583640005280239549240395406712857109914937888100456590716923266145998436100241669284903815455123276258816639842598859038794078836381792324092074103409706854244159938905098601759093105210904536529693771192367835037985903473186799834878405030092059769788239840639186011986048456993938908783562370139772160643352012784919461773374244679357967937041385946212139954241060339572022930265611374941338332811629430713489786753922499622816403957804");
			MCPBS pbsASN = new MCPBSImpl();
			pbsASN.setRootCoupons(cl);
			pbsASN.setCommonInfo(ci);
			pbsASN.setDelta(delta);
			pbsASN.setOmega(omega2);
			
	    	RedeemM1 m1redeemASN = new RedeemM1Impl();
	    	m1redeemASN.setaRedeemSet(aRedeemSet);
	    	m1redeemASN.setGroupSignature(bbs);
	    	//m1redeemASN.setGroupPublicKey(pkASN);
	    	m1redeemASN.setMCPBS(pbsASN);
	
	        ASN1Object m1RASN = ((RedeemM1Impl) m1redeemASN).toASNObject();
	        //System.out.println("ASN1 to String M1RedeemASN");    
	        //System.out.println(m1RASN.toString());
	       
	        String dump = ASN1Dump.dumpAsString(m1RASN);
	        //System.out.println("ASN1 STRING DUMP M1RedeemASN");    
	        //System.out.println(dump);
	 
	     
	        byte[] m2der = null;
			m2der = (byte[]) ((RedeemM1Impl) m1redeemASN).serialize(MSGFormatConstants.ASN1);
			
			
			// send through network here!
			
			
	        System.out.println("DER M1RedeemASN SIZE");    
	        System.out.println(m2der.length);
	        //String tostring = StringUtils.readHexString(m2der);
	        //System.out.println("DER M1RedeemASN Read HEX String: "+tostring.length());    
	        //System.out.println(tostring);
			
	        
	        // receive from network
	    	RedeemM1Impl m1redeemDecode = new RedeemM1Impl();
			m1redeemDecode = (RedeemM1Impl) m1redeemDecode.deSerialize(m2der, MSGFormatConstants.ASN1);
			
			String DaRedeemSet = m1redeemDecode.getaRedeemSet();
			BBSGroupPublicKeyMSG Dbbs = new BBSGroupPublicKeyMSG();
	   	  //  Dbbs = m1redeemDecode.getGroupPublicKey();
	      //  String Dg1 = Dbbs.getG1();
	        String Dt1 = m1redeemDecode.getGroupSignature().getT1();
			//System.out.println("T1 decoded from BBSSignatureASN: "+StringUtils.readHexString(Dt1));
			System.out.println("T1 decoded from BBSSignatureASN: "+ Dt1 );
			
//			collector.checkThat(g1, CoreMatchers.is(m1redeemDecode.getGroupPublicKey().getG1() ) );
//			collector.checkThat(g2, CoreMatchers.is(m1redeemDecode.getGroupPublicKey().getG2()) );
//			collector.checkThat(h, CoreMatchers.is(m1redeemDecode.getGroupPublicKey().getH()));
//			collector.checkThat(omega, CoreMatchers.is(m1redeemDecode.getGroupPublicKey().getOmega()));
//			collector.checkThat(u, CoreMatchers.is(m1redeemDecode.getGroupPublicKey().getU()));
//			collector.checkThat(v, CoreMatchers.is(m1redeemDecode.getGroupPublicKey().getV()));
			
			collector.checkThat("t1", t1, CoreMatchers.is(m1redeemDecode.getGroupSignature().getT1()));
			collector.checkThat("t2",  t2, CoreMatchers.is(m1redeemDecode.getGroupSignature().getT2())); 
			collector.checkThat("t3", t3, CoreMatchers.is(m1redeemDecode.getGroupSignature().getT3()));
			collector.checkThat("c", c, CoreMatchers.is(m1redeemDecode.getGroupSignature().getC()));
			collector.checkThat("salpha", salpha, CoreMatchers.is(m1redeemDecode.getGroupSignature().getSalpha()));
			collector.checkThat("sbeta", sbeta, CoreMatchers.is(m1redeemDecode.getGroupSignature().getSbeta()));
			collector.checkThat("sx", sx, CoreMatchers.is(m1redeemDecode.getGroupSignature().getSx()));
			collector.checkThat("sdelta1", sdelta1, CoreMatchers.is(m1redeemDecode.getGroupSignature().getSdelta1()));
			collector.checkThat("sdelta2", sdelta2, CoreMatchers.is(m1redeemDecode.getGroupSignature().getSdelta2()));
			
			collector.checkThat("aRedeemSet", aRedeemSet, CoreMatchers.is(m1redeemDecode.getaRedeemSet()));
			
			collector.checkThat("delta", delta, CoreMatchers.is(m1redeemDecode.getMCPBS().getDelta()));
			collector.checkThat("omega2", omega2, CoreMatchers.is(m1redeemDecode.getMCPBS().getOmega()));
	
	
	 /**  
	  * 
	  *     MCDescASN mcdDesc1 = new MCDescASN();
			mcdDesc1.setJ(new BigInteger("0"));
			mcdDesc1.setNumber(new BigInteger("2"));
			mcdDesc1.setValue(new BigInteger("10"));
			
			MCDescASN mcdDesc2 = new MCDescASN();
			mcdDesc2.setJ(new BigInteger("1"));
			mcdDesc2.setNumber(new BigInteger("2"));
			mcdDesc2.setValue(new BigInteger("20"));
			
			ArrayList<MCDescASN> al = new ArrayList<MCDescASN>();
			al.add(mcdDesc1);
			al.add(mcdDesc2);
			
	        CommonInfoASN ci = new CommonInfoASN();
	        ci.setIssuerID(new Integer("1"));
	        ci.setServiceID(new Integer("2"));
	        ci.setMCDescASN(al);
	        ci.setExpiration(new Date());
	        ci.setClaim(new Date());
	        ci.setRefund(new Date());  
	        
	        ASN1Object ciASN = ci.toASNObject();
	        System.out.println("ASN1 CommonInfoASN");    
	        System.out.println(ciASN.toString());
	        
	        //ASN1Dump ciDump = new ASN1Dump();
	        String dump = ASN1Dump.dumpAsString(ciASN);
	        System.out.println("ASN1 STRING DUMP CommonInfoASN");    
	        System.out.println(dump);
	        
	        byte[] cider = null;
			cider = ci.encode();
			
	        System.out.println("DER CommonInfoASN");    
	        System.out.println(new String(cider));
	        
	        String tostring = StringUtils.readHexString(cider);
	        System.out.println("DER CommonInfoASN Read HEX String");    
	        System.out.println(new String(tostring));
	        
	        //System.out.println("BER M2IssuingASN");    
	        //System.out.println(new String(m2der));
			CommonInfoASN ciResp = new CommonInfoASN();
			boolean resultDecode = ciResp.decode(cider);
			if (resultDecode){
				Integer issID = ciResp.getIssuerID();
				Integer servID = ciResp.getServiceID();
				Date expDate = ciResp.getExpiration();
				Date clDate = ciResp.getClaim();
				Date refDate = ciResp.getRefund();
				
				System.out.println("IssuerID:     "+issID.intValue());
				System.out.println("ServiceID:    "+servID.intValue());
				System.out.println("Expiration:   "+expDate);
				System.out.println("Claim:        "+clDate);
				System.out.println("Refund:       "+refDate);
				System.out.println("Array List: MCDescription  ");
				ArrayList<MCDescASN> mcRes = ciResp.getMCDescASN();
				Iterator<MCDescASN> it = mcRes.iterator();
				MCDescASN mctmp;
				while(it.hasNext()) {
					mctmp = it.next();
					System.out.println("   J:      "+mctmp.getJ());
					System.out.println("   Numer:  "+mctmp.getNumber());
					System.out.println("   Value:  "+mctmp.getValue());
				}
			}
			else{
				System.out.println("ERROR: decodificación");
			}
	
			//PRUEBA DATOS CIFRADOS
			String aEnc = "RUnj86ILzlR3qrdW03aA+DaK3+fjbnFPMrxGobTe5IQ=";
			System.out.println("DATOS A CIFRAR:    "+aEnc);
			//DERGeneralString aEncASN = new DERGeneralString(aEnc);
			byte[] derASN = (new DERGeneralString(aEnc)).toASN1Primitive().getEncoded();
			
			//DERGeneralString aEncASN2;
			//aEncASN2 = (DERGeneralString) ASN1Primitive.fromByteArray(derASN);
			String aEncDecode = ((DERGeneralString) ASN1Primitive.fromByteArray(derASN)).getString();
			System.out.println("DATOS OBTENIDOS:   "+aEncDecode);
			**/
	 /**       
	        BigInteger alpha = new BigInteger("483173500437038755037070353872352949011259556918185234941419402727691619285969451292938143059174137939008076932721372892727961959705531663487341659378787918407475092723448012241307088709567732362459171065845411796968085418679185135060453350108005611282462389889916653718576276215399834755787632094029260383836685301099894549038195248302069898745224344425039844481294455488132995186555432490076418117583877280792969695720885303316441671354272735199396796662907971480532548913777376169596582625536860485822472385473656383706772111469538273950811313820906827429094641204070465669722620929558958689310948230785366541940482233340372107596102861593480253707848353134915362443881947285898511893111159366217461437351679022078022816705174160746507385020373602955847701444746413406513081674070519579863933264508425054412157464275269562886058679481027033714235154165066750038130300166281525416305214380050123892969940932268780946038248215523311538637603768373221283046060559648164116075835191888504876808674216273754922982646534191385603191824357746450503414764972274987506540193348980484115121255146366263811834696195337483224676835060613567531405167288452264641384439589860002218964764925525972171600764768049855203644145469902988908400437440");
	        
	        M1IssuingASN m1issuing = new M1IssuingASN();
	        m1issuing.setCiASN(ci);
	        m1issuing.setAlpha(alpha);
	    **/    
	      /*
	       * TEST M2IssuingASN
	       */
	        /**
	        M2IssuingASN m2issuing = new M2IssuingASN();
	        m2issuing.setLambda(alpha);
	        m2issuing.setSessionID(new Integer("1"));
	        
	        ASN1Object m2ASN = m2issuing.toASNObject();
	        System.out.println("ASN1 M1IssuingASN");    
	        System.out.println(m2ASN.toString());
	        
	        byte[] m2der = null;
			try {
				m2der = m2ASN.getEncoded();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        //System.out.println("BER M2IssuingASN");    
	        //System.out.println(new String(m2der));
			M2IssuingASN m2issuingResp = new M2IssuingASN();
			boolean resultDecode = m2issuingResp.decode(m2der);
			if (resultDecode){
				BigInteger alphaR = m2issuingResp.getLambda();
				Integer idR = m2issuingResp.getSessionID();
				System.out.println("Lambda:     "+alphaR.toString());
				System.out.println("Session ID: "+idR.intValue());
			}
			else{
				System.out.println("ERROR: decodificación");
			}
			**/
			
			
	        /**
		        M4IssuingASN m4issuing = new M4IssuingASN();
		        m4issuing.setVarphi(alpha);
		        m4issuing.setGamma(alpha);
		        m4issuing.setSessionID(new Integer("1"));
		        
		        ASN1Object m4ASN = m4issuing.toASNObject();
		        System.out.println("ASN1 M1IssuingASN");    
		        System.out.println(m4ASN.toString());
		        
		        byte[] m4der = null;
				m4der = m4issuing.encode();
		        
		        //System.out.println("BER M2IssuingASN");    
		        //System.out.println(new String(m2der));
				M4IssuingASN m4issuingResp = new M4IssuingASN();
				boolean resultDecode = m4issuingResp.decode(m4der);
				if (resultDecode){
					BigInteger varR = m4issuingResp.getVarphi();
					BigInteger gamR = m4issuingResp.getGamma();
					Integer idR = m4issuingResp.getSessionID();
					System.out.println("Varphi:     "+varR.toString());
					System.out.println("Gamma:      "+gamR.toString());
					System.out.println("Session ID: "+idR.intValue());
				}
				else{
					System.out.println("ERROR: decodificación");
				}
	**/
	        System.out.println("FIN EJECUCIÓN"); 
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}