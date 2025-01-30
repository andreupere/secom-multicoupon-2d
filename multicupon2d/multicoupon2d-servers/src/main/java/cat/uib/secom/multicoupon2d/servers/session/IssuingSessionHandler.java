package cat.uib.secom.multicoupon2d.servers.session;

import cat.uib.secom.multicoupon2d.common.msg.IssuingM1;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM2;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM3;
import cat.uib.secom.multicoupon2d.common.msg.IssuingM4;

public class IssuingSessionHandler {

	private IssuingM1 message1;
	
	private IssuingM2 message2;
	
	private IssuingM3 message3;
	
	private IssuingM4 message4;

	
	public IssuingSessionHandler() {}
	
	
	
	public IssuingM1 getMessage1() {
		return message1;
	}

	public void setMessage1(IssuingM1 message1) {
		this.message1 = message1;
	}

	public IssuingM2 getMessage2() {
		return message2;
	}

	public void setMessage2(IssuingM2 message2) {
		this.message2 = message2;
	}

	public IssuingM3 getMessage3() {
		return message3;
	}

	public void setMessage3(IssuingM3 message3) {
		this.message3 = message3;
	}

	public IssuingM4 getMessage4() {
		return message4;
	}

	public void setMessage4(IssuingM4 message4) {
		this.message4 = message4;
	}
	
	
	
}
