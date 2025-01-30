package cat.uib.secom.multicoupon2d.servers;

import java.io.IOException;

import org.spongycastle.asn1.x509.Certificate;
import org.spongycastle.crypto.InvalidCipherTextException;
import org.junit.Ignore;
import org.junit.Test;

import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.servers.groupmanager.GroupManager;
import junit.framework.TestCase;

public class TestGroupManagerServer {

	@Ignore("Testing withou networking...")
	@Test
	public void testServer() {
		
		try {
			GroupManager gm = GroupManager.getInstance();
			GroupManager.GroupManagerServerImpl server = gm.new GroupManagerServerImpl(4567);
			System.out.println("Server on port 4567 ... ");
			server.bind();
			server.listen();
			System.out.println("Server accepts connections ...");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Multicoupon2DException e) {
			e.printStackTrace();
		} catch (InvalidCipherTextException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
