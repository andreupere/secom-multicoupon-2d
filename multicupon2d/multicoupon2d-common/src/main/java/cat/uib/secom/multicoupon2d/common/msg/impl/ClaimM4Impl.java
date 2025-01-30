package cat.uib.secom.multicoupon2d.common.msg.impl;

import org.simpleframework.xml.Root;

import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;

@Root(name=MessageTypesConstants.CLAIM_MESSAGE4)
public class ClaimM4Impl extends ClaimM2Impl {

	public static String TYPE = MessageTypesConstants.CLAIM_MESSAGE4;
	
	public ClaimM4Impl() {
		super(ClaimM4Impl.class);
	}
	
}
