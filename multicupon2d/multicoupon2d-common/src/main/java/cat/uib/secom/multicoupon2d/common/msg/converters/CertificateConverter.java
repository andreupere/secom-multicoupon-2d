package cat.uib.secom.multicoupon2d.common.msg.converters;

import org.spongycastle.asn1.x509.Certificate;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import cat.uib.secom.utils.strings.StringUtils;


public class CertificateConverter implements Converter<Certificate> {

	@Override
	public Certificate read(InputNode node) throws Exception {
		String certString = node.getValue();
		byte[] cert = StringUtils.decodeBase64(certString);
		return Certificate.getInstance(cert);
	}

	@Override
	public void write(OutputNode node, Certificate x509) throws Exception {
		node.setValue( StringUtils.encodeBase64( x509.getEncoded() ) );
	}

}
