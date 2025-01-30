package cat.uib.secom.multicoupon2d.common.exceptions;

public class Multicoupon2DException extends Exception {

	private static final long serialVersionUID = 1L;

	public Multicoupon2DException() {
		super("Multicoupon2D Exception");
	}
	
	public Multicoupon2DException(String msg) {
		super(msg);
	}
}
