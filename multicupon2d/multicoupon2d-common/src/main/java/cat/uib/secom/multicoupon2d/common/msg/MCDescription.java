package cat.uib.secom.multicoupon2d.common.msg;

import java.math.BigInteger;

public interface MCDescription {

	public abstract void setJ(Integer j);

	public abstract void setNumber(Integer number);

	public abstract void setValue(String value);

	public abstract Integer getNumber();

	public abstract String getValue();

	public abstract Integer getJ();

}