package cat.uib.secom.multicoupon2d.test.integration.join.network;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.junit.Ignore;
import org.junit.Test;

import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.servers.groupmanager.GroupManager;
import cat.uib.secom.multicoupon2d.servers.groupmanager.GroupManager.GroupManagerServerImpl;
import cat.uib.secom.multicoupon2d.test.integration.join.StartGroupManager;
import cat.uib.secom.utils.strings.LoadCfgUtils;


public class TestGroupManagerNetwork {
	
	
	@Test
	public void test() {
		try {
			LoadCfgUtils cfg = new LoadCfgUtils(this.getClass().getResourceAsStream("/common.cfg"));
			Integer port_gmanager = Integer.valueOf(cfg.read("port_gmanager") );
			
			// get a group manager instance
			GroupManager gm = GroupManager.getInstance();
			
			// create group manager server
			GroupManager.GroupManagerServerImpl server = gm.new GroupManagerServerImpl(port_gmanager);
			
			// bind server port
			server.bind();
			
			// server remains listening incoming requests
			server.listen();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
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
