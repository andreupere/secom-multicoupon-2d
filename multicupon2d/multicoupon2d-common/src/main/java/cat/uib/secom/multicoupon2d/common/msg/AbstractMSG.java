package cat.uib.secom.multicoupon2d.common.msg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;

import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.util.ASN1Dump;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.transform.Matcher;



import cat.uib.secom.multicoupon2d.common.cfg.Constants;
import cat.uib.secom.multicoupon2d.common.exceptions.NotImplementedException;
import cat.uib.secom.multicoupon2d.common.msg.converters.DateMatcher;
import cat.uib.secom.multicoupon2d.common.msg.impl.MCPBSImpl;
import cat.uib.secom.utils.strings.MSGFormatConstants;

public abstract class AbstractMSG {

	
	@SuppressWarnings("unused")
	private Class<?> _cl;
	
	protected Serializer serializer;
	
	protected StringWriter buffer;
	
	protected MSGFormatConstants msgFormat;
	
	protected ByteArrayOutputStream baos;
	
	protected ASN1Sequence asn1Object;
	
	
	public AbstractMSG(Class<?> cl) {
		this._cl = cl;
		Strategy strategy = new AnnotationStrategy();
		Matcher dateMatcher = new DateMatcher();
		serializer = new Persister(strategy, dateMatcher);
	}
	

	
	
	/////////////////////////////////////////////////////////////
	/////// ABSTRACT METHODS
	/////////////////////////////////////////////////////////////
		
	
	
	/**
	 * Abstract method to be implemented by child classes to encode object contents to ASN1 encoding
	 * 
	 * @return byte array containing the object encoded into ASN1
	 * 
	 * */
	protected abstract byte[] encodeASN1();
	
	/**
	 * Abstract method to be implemented by child classes to decode the byte[] containing the ASN1 encoded
	 * into the representation of the object
	 * 
	 * @param input as the byte array representation of the object to be decoded from ASN1
	 * @return the corresponding object. It should be cast by caller class to obtain the right object (one of the 
	 * class childs)
	 * */
	protected abstract Object decodeASN1(byte[] input);
	
	
	/**
	 * Retorna la representació ASN1 de l'objecte. Ha de ser implementat per totes les classes que deriven de AbstractMSG
	 * per obtenir la representació en forma d'String de l'objecte ASN1 contingut
	 * */
	protected abstract String dump() throws Exception;
	
	
	
	/////////////////////////////////////////////////////////////
	/////// GETTERS AND SETTERS	
	/////////////////////////////////////////////////////////////
	
	
	public MSGFormatConstants getMSGFormat() {
		return this.msgFormat;
	}
	public void setMSGFormat(MSGFormatConstants msgFormat) {
		this.msgFormat = msgFormat;
	}
	

	
	/////////////////////////////////////////////////////////////
	/////// PUBLIC METHODS
	/////////////////////////////////////////////////////////////
	
	/**
	 * Serialize the object contents to the format specified by msgFormat (MSGFormat.XML or MSGFormat.ASN1)
	 * 
	 * @param msgFormat can be MSGFormat.XML or MSGFormat.ASN1 and specifies the serialization mode output (XML or ASN1 encoding)
	 * 
	 * @return byte array representation of the object in the format specified by msgFormat
	 * */
	public byte[] serialize(MSGFormatConstants msgFormat) throws Exception {
		this.msgFormat = msgFormat;
		return this.serialize();
	}
	
	/**
	 * Serialize the object contents to the format specified internally
	 * 
	 * @return byte array representation of the object in the format specified internally
	 * 
	 * */
	protected byte[] serialize() throws Exception {
		if (msgFormat.equals(MSGFormatConstants.XML))
			return encodeXML().toByteArray();
		else if (msgFormat.equals(MSGFormatConstants.ASN1))
			return encodeASN1();
		else
			throw new Exception("MSG format (ASN1 or XML) not set in common.cfg");
	}
	
	
	
	/**
	 * Serialize the object to a file with the specified format (actually only works for XML serialization)
	 * 
	 * @param file where the serialization process have to save the output
	 * @param msgFormat specifying the format of the serialization process
	 * 
	 * */
	public void serialize(Writer out, MSGFormatConstants msgFormat) throws Exception {
		if (msgFormat.equals(MSGFormatConstants.XML))
			serializer.write(this, out); //toXML(file);
		else if (msgFormat.equals(MSGFormatConstants.ASN1))
			throw new NotImplementedException();
		else
			throw new Exception();
	}
	
	
	
	/**
	 * Retrieves the serialized object contained in a byte array
	 * 
	 * @param b as the input byte array where the serialized object is
	 * @param msgFormat specifies the format of the serialized object (can only be MSGFormat.XML or MSGFormat.ASN1)
	 * 
	 * @return the corresponding object (always a child of AbstractMSG abstract class)
	 * 
	 * */
	public Object deSerialize(byte[] b, MSGFormatConstants msgFormat) throws Exception {
		if (msgFormat.equals(MSGFormatConstants.XML))
			return decodeXML(b);
		else if (msgFormat.equals(MSGFormatConstants.ASN1))
			return decodeASN1(b);
		else
			throw new Exception();
	}
	
	


	
	
	/**
	 * Retrieves the serialized object contained in a Reader
	 * 
	 * @param input as the Reader where the object is
	 * @param msgFormat specifies the format of the serialized object (actually only works XML)
	 * 
	 * @return the corresponding object (always a child of AbstractMSG abstract class)
	 * */
	public Object deSerialize(Reader input, MSGFormatConstants msgFormat) throws Exception {
		if (msgFormat.equals(MSGFormatConstants.XML))
			return toObject(input);
		else if (msgFormat.equals(MSGFormatConstants.ASN1))
			throw new NotImplementedException();
		else
			throw new Exception();
	}
	
	
	
	/**
	 * Generates a String representation of the object in both XML or ASN1
	 * 
	 * @param msgFormat specifies the format of the serialized object (both MSGFormat.XML and MSGFormat.ASN1)
	 * 
	 * @return human-readable string representation of the object (XML representation as is or ASN1 as a string)
	 * 
	 * */
	public String dump(MSGFormatConstants msgFormat) throws Exception {
		if (msgFormat.equals(MSGFormatConstants.ASN1))
			return dump();
		else if (msgFormat.equals(MSGFormatConstants.XML))
			return new String(this.serialize(MSGFormatConstants.XML), Constants.CHARSET );
		else
			throw new Exception();
	}
	
	



	
	
	
	

	

	
	
	/**
	 * Unmarshalls the XML representation to the current object
	 * 
	 * @param a {@Reader} object where XML representation should be read and transformed
	 * 
	 * @return Marshalled XML representation to his corresponding object
	 * */
	private Object toObject(Reader input) throws Exception {
		return serializer.read(this.getClass(), input);
		
	}
	
	
	
	
	
	/**
	 * Helper method to encode a XML representation to a byte array output stream
	 * 
	 * @return a ByteArrayOutputStream containing the XML as a byte array
	 * */
	private ByteArrayOutputStream encodeXML() throws Exception {
		baos = new ByteArrayOutputStream();
		serializer.write(this, baos);
		return baos;
	}

	/**
	 * Helper method to decode a XML representation from a byte array
	 * 
	 * @param b as the XML to be decoded
	 * @return object corresponding to the input byte array
	 * */
	private Object decodeXML(byte[] b) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		return serializer.read(this.getClass(), bais);
	}
	
}
