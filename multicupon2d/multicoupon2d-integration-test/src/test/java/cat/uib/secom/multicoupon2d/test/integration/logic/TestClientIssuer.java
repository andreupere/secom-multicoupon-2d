package cat.uib.secom.multicoupon2d.test.integration.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Calendar;

import org.spongycastle.asn1.util.ASN1Dump;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.hamcrest.CoreMatchers;




import cat.uib.secom.multicoupon2d.client.customer.Customer;
import cat.uib.secom.multicoupon2d.common.dao.Multicoupon2D;
import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.common.msg.CommonInfo;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM1;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM2;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM3;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM4;
import cat.uib.secom.multicoupon2d.common.msg.MCDescription;
import cat.uib.secom.multicoupon2d.common.msg.MCPBS;
import cat.uib.secom.multicoupon2d.common.msg.impl.CommonInfoImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM1Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM2Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM3Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.IssuingM4Impl;
import cat.uib.secom.multicoupon2d.common.msg.impl.MCDescriptionImpl;
import cat.uib.secom.multicoupon2d.common.msg.impl.MCPBSImpl;
import cat.uib.secom.multicoupon2d.servers.issuer.Issuer;
import cat.uib.secom.security.HashUtils;
import cat.uib.secom.utils.strings.MSGFormatConstants;

@Deprecated
public class TestClientIssuer {
	
	@Rule 
	public ErrorCollector collector = new ErrorCollector();
	
	//private BigInteger rsaNIssuer = new BigInteger("776035917212356030873160194582684100938972968892024123740618218841705877216914683418598628207579439668261012542014369569505612989670973835717779592165428744199507338973808694246205381272270031617304394997911884418666417444218249214371301605434899247733423550179141944183534764009406725812567410476630448037113198345082142060259901476217023820849612473002342320256741007280400138574802019308999987811562427593924734339237279370435337810725824884985440557136357434240466805819584822943491818408665231971984636095926408440294810694745545878041609784474984330563438530969133606748579156811332053798450764424274401737549871083358794852859918716699983725255834768126784338265843989965974733400252431489015534376373668409969060188014704088471480197160842890147113900125252843174185973657339803047706648110307847876741880591824977569610378787232411248602588855029774567554334848402632208305367718276556590263238635144028405972624865736048191959015100778399243100853870622087254693321721164898552074042373189098109731282209434543393494714161810310586544767397002577072110044020238825828916503155015766348082693366264757628425854362889422003584921142508960646849282111530744365814942057726446037300184091777128442051126374507193793984954222033");
	//private BigInteger rsaEIssuer = new BigInteger("65537");
	
	
	protected static final String FOLDER = "/home/apaspai/development/data/multicoupon2d/";
	
	protected static final String fNAME_MC2D = "mc2d.xml";
	
	protected static final String fNAME_MCPBS = "mcpbs.xml";
	
	protected static final String fNAME_M1 = "issuing1.xml";
	
	protected static final String fNAME_M2 = "issuing2.xml";
	
	protected static final String fNAME_M3 = "issuing3.xml";
	
	protected static final String fNAME_M4 = "issuing4.xml";
	


	@Ignore
	@Test
	public void requestCoin() {

		
		try {

			
			// create mock CommonInfo
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 100);
			
			CommonInfo ci = new CommonInfoImpl();
			ci.setClaim(calendar.getTime());
			ci.setExpiration(calendar.getTime());
			ci.setIssuerID( new Integer(1) );
			ci.setRefund(calendar.getTime());
			ci.setServiceID( new Integer(2) );
			
			ci = createDescription1(ci);

			
			// create customer instance
			Customer customer = Customer.getInstance(null); 
			
			// create issuer instance
			Issuer issuer = Issuer.getInstance();
			
			
			System.out.println("MSGFormat: customer=" + customer.getMSGFormat() + "; issuer=" + issuer.getMSGFormat() );
			System.out.println("Logging active=" + customer.isLoggingActive());
			
			// test whether customer and merchant shares the same message format
			Assert.assertEquals(customer.getMSGFormat(), issuer.getMSGFormat());
			
			long totalCustomer = 0;
			long totalIssuer = 0; 
			long cstmr = 0;
			long ssr = 0;
			
			long start = System.currentTimeMillis();
			
			
			// Customer creates the message1 (m1)
			IssuingM1 m1 = customer.issuingMSG1Logic(ci);
			System.out.print((System.currentTimeMillis() - start) + "\t");
			
			// Customer sends message1 (m1)
			
			
			
			
			
			// Issuer handles message1 (m1) and creates message2 (m2)
			start = System.currentTimeMillis();
			IssuingM2 m2 = issuer.receiveMessage1Issuing(m1);
			ssr = System.currentTimeMillis() - ssr;
			System.out.print((System.currentTimeMillis() - start) + "\t");
			
			
			// Issuer sends message2 (m2)
			
			// network
			
			// Customer handles message2 (m2) and creates message3 (m3)
			start = System.currentTimeMillis();
			IssuingM3 m3 = customer.issuingMSG2Logic(m2);
			System.out.print((System.currentTimeMillis() - start) + "\t");
			totalCustomer =+ cstmr;
			

			// customer sends message3 (m3)
			
			// network
			
			// Issuer handles message3 (m3) and creates message4 (m4)
			start = System.currentTimeMillis();
			IssuingM4 m4 = issuer.receiveMessage3Issuing(m3);
			System.out.print((System.currentTimeMillis() - start) + "\t");
			totalIssuer =+ ssr;
			

			// Issuer sends message4 (m4)
			
			// network
			
			// Customer handles message4 (m4)
			start = System.currentTimeMillis();
			MCPBS mcpbs = customer.unblindIssuing(m4);
			System.out.print((System.currentTimeMillis() - start) + "\r\n");
					
			
			
			// verify multicoupon
			boolean result = ((MCPBSImpl) mcpbs).verify( customer.getCustomerStore().getRsaIssuerPubKey().getPublicExponent(), 
														 customer.getCustomerStore().getRsaIssuerPubKey().getModulus(), 
														 customer.getMSGFormat() );

			
			// test blind signature verification
			Assert.assertTrue(result);
			
			
			// dump mcpbs
			MCPBSImpl mc = ((MCPBSImpl)customer.getCustomerStore().getMcpbs());
			mc.serialize(customer.getMSGFormat());
			System.out.println( "MCPBS\n" + mc.dump(customer.getMSGFormat()) );
			
			// dump multicoupon2D
			Multicoupon2D m2d = (Multicoupon2D) customer.getCustomerStore().getM2d();
			m2d.serialize(customer.getMSGFormat());
			System.out.println( "MC2D (as XML)\n" + m2d.dump(MSGFormatConstants.XML) ); // only as XML representation
			
			// dump m1
			IssuingM1Impl m1i = ((IssuingM1Impl) m1 );
			m1i.serialize(customer.getMSGFormat());
			System.out.println( "IssuingM1\n" + m1i.dump(customer.getMSGFormat()) );
			
			// dump m2
			IssuingM2Impl m2i = ((IssuingM2Impl) m2 );
			m2i.serialize(customer.getMSGFormat());
			System.out.println( "IssuingM2\n" + m2i.dump(customer.getMSGFormat()) );
			
			// dump m3
			IssuingM3Impl m3i = ((IssuingM3Impl) m3 );
			m3i.serialize(customer.getMSGFormat());
			System.out.println( "IssuingM3\n" + m3i.dump(customer.getMSGFormat()) );
			
			// dump m4
			IssuingM4Impl m4i = ((IssuingM4Impl) m4 );
			m4i.serialize(customer.getMSGFormat());
			System.out.println( "IssuingM4\n" +  m4i.dump(customer.getMSGFormat()) );
			
			
			// store MCPBS to file
			( (MCPBSImpl) customer.getCustomerStore().getMcpbs() ).serialize(new FileWriter(FOLDER + MCPBSImpl.fNAME+".xml"), MSGFormatConstants.XML);
			// store MC2D to file
			( (Multicoupon2D) customer.getCustomerStore().getM2d() ).serialize(new FileWriter(FOLDER + Multicoupon2D.fNAME+".xml"), MSGFormatConstants.XML);
			
			// retrieve MCPBS and Multicupon2D structure from file
			MCPBSImpl mcpbs2 = new MCPBSImpl();
			mcpbs2 = (MCPBSImpl) mcpbs2.deSerialize( new FileReader(new File(FOLDER, MCPBSImpl.fNAME+".xml")) , MSGFormatConstants.XML);
			
			Multicoupon2D m2d2 = new Multicoupon2D();
			m2d2 = (Multicoupon2D) m2d2.deSerialize( new FileReader(new File(FOLDER, Multicoupon2D.fNAME+".xml")), MSGFormatConstants.XML);
			
			
			
			
			collector.checkThat(mcpbs2.getDelta(), CoreMatchers.is( mcpbs.getDelta() ));
			collector.checkThat(mcpbs2.getOmega(), CoreMatchers.is( mcpbs.getOmega()));
			
			collector.checkThat(m2d2.getCoupon(0, 1).getHash(), CoreMatchers.is( customer.getCustomerStore().getM2d().getCoupon(0, 1).getHash() ) );
			
			
			

			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (Multicoupon2DException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Ignore("I only want a single integration test... no benchmarking")
	@Test
	public void testIssuing() {
		int it = 100;
		while (it>0) {
			//System.out.println("\n-------- iteration " + it + " --------");
			this.requestCoin();
			it--;
		}
	}
	
	@Ignore
	@Test
	public void testHash() throws NoSuchAlgorithmException {
		int it = 100;
		while (it>0) {
			BigInteger b = new BigInteger("4587512045200364079245605481202274514");
			long start = System.nanoTime();
			System.out.print((System.nanoTime() - start) + "\r\n");
			b = HashUtils.getHash(b);
			HashUtils.getHash(b);
			it--;
		}
	}
	
	
	private CommonInfo createDescription1(CommonInfo ci) {
		// 10 cupons (J=12, N=10) en un sol multicupo
		MCDescription mcd = new MCDescriptionImpl();
		mcd.setJ(1);
		mcd.setNumber(12);
		mcd.setValue(new String("23"));
		
		ArrayList<MCDescription> al = new ArrayList<MCDescription>();
		al.add(mcd);
		
		ci.setMCDescription(al);
		return ci;
	}
	
	private CommonInfo createDescription2(CommonInfo ci) {
		// 2 cupons en cada un dels 5 multicupons (J=5, N=2)
		MCDescription mcd1 = new MCDescriptionImpl();
		mcd1.setJ(0);
		mcd1.setNumber(2);
		mcd1.setValue(new String("23"));
		
		MCDescription mcd2 = new MCDescriptionImpl();
		mcd2.setJ(1);
		mcd2.setNumber(2);
		mcd2.setValue(new String("3"));
		
		MCDescription mcd3 = new MCDescriptionImpl();
		mcd3.setJ(2);
		mcd3.setNumber(2);
		mcd3.setValue(new String("25"));
		
		MCDescription mcd4 = new MCDescriptionImpl();
		mcd4.setJ(3);
		mcd4.setNumber(2);
		mcd4.setValue(new String("21"));
		
		MCDescription mcd5 = new MCDescriptionImpl();
		mcd5.setJ(4);
		mcd5.setNumber(2);
		mcd5.setValue(new String("14"));
		
		
		ArrayList<MCDescription> al = new ArrayList<MCDescription>();
		al.add(mcd1);
		al.add(mcd2);
		al.add(mcd3);
		al.add(mcd4);
		al.add(mcd5);
		
		ci.setMCDescription(al);
		return ci;
	}
	

}
