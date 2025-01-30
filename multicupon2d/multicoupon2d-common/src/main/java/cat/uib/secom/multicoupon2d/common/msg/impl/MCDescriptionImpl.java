package cat.uib.secom.multicoupon2d.common.msg.impl;

import java.io.IOException;

import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERGeneralString;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.util.ASN1Dump;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.multicoupon2d.common.msg.AbstractMSG;
import cat.uib.secom.multicoupon2d.common.msg.MCDescription;
import cat.uib.secom.multicoupon2d.common.cfg.MessageTypesConstants;


@Root(name=MessageTypesConstants.MULTICOUPON_DESCRIPTION)
public class MCDescriptionImpl extends AbstractMSG implements MCDescription {

	public static String TYPE = MessageTypesConstants.MULTICOUPON_DESCRIPTION;
	
	@Attribute
	protected Integer j;
	
	@Element(name="number")
	protected Integer number;
	
	@Element(name="value")
	protected String value;
	
	
	
	
	public MCDescriptionImpl() {
		super(cat.uib.secom.multicoupon2d.common.msg.impl.MCDescriptionImpl.class);
	}


	
	
	
	public Integer getNumber() {
		return number;
	}


	public void setNumber(Integer number) {
		this.number = number;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public Integer getJ() {
		return j;
	}


	public void setJ(Integer j) {
		this.j = j;
	}

	
	
	
	
	
	public ASN1Object toASNObject(){
		ASN1Integer jasn = new ASN1Integer(j);
		ASN1Integer numasn = new ASN1Integer(number);
		DERGeneralString valasn = new DERGeneralString(value);
		
		ASN1EncodableVector tmp = new ASN1EncodableVector();
		tmp.add(jasn);
		tmp.add(numasn);
		tmp.add(valasn);
		
		asn1Object = new DERSequence(tmp);
		return asn1Object.toASN1Primitive();
	}
	
	public ASN1Object getASNObject(){
		return asn1Object.toASN1Primitive();
	}
	
	public byte[] encodeASN1(){
		try {
			toASNObject();
			return asn1Object.getEncoded();
		} catch (IOException e) {
			return null;
		}
	}
	
	public boolean decode(ASN1Object asn){
		asn1Object = (ASN1Sequence) asn;
		j = (((ASN1Integer) asn1Object.getObjectAt(0)).getValue().intValue());
		number = (((ASN1Integer) asn1Object.getObjectAt(1)).getValue().intValue());
		value = (((DERGeneralString) asn1Object.getObjectAt(2)).getString());
		return true;
	}





	@Override
	protected Object decodeASN1(byte[] input) {
		// TODO Auto-generated method stub
		return null;
	}





	@Override
	protected String dump() throws Exception {
		return ASN1Dump.dumpAsString(asn1Object);

	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getNumber());
		sb.append("\r\n");
		sb.append(this.getJ());
		sb.append("\r\n");
		sb.append(this.getValue());
		return sb.toString();
	}
	
}
