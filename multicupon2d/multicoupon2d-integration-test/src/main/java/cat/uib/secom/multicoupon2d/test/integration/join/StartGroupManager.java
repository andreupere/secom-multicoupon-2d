package cat.uib.secom.multicoupon2d.test.integration.join;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.spongycastle.crypto.InvalidCipherTextException;

import cat.uib.secom.multicoupon2d.common.exceptions.Multicoupon2DException;
import cat.uib.secom.multicoupon2d.servers.groupmanager.GroupManager;
import cat.uib.secom.utils.strings.LoadCfgUtils;


public class StartGroupManager {
	
	
	public void startServer() {
		
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
	
	public static void main(String args[]) {
		(new StartGroupManager()).startServer();
	}

}
