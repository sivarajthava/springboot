package com.rabo.customer.statement.util;

import java.io.InputStream;
import java.io.StringWriter;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Sivaraj
 * @category : XML parser class
 * @since <b> Feb-29-2020 </b>
 * @see Marshaller
 * @see Unmarshaller
 * @version 1.0
 * 
 *          <pre>
 *          Statement application XML parse class - Parse the uploaded XML file
 * 
 *          <pre>
 */
@Component
@Slf4j
public class MyMarshallerWrapper {

	private Jaxb2Marshaller jaxb2Marshaller;

	@PostConstruct
	public void initMarshaller() {
		log.debug("MyMarshallerWrapper class initialization - Begin");
		jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setPackagesToScan("com.rabo.customer.statement.model");
		log.debug("MyMarshallerWrapper class initialization - End");
	}

	public <T> String marshallXml(final T obj) throws JAXBException {
		log.debug("MyMarshallerWrapper class marshall - Begin");
		StringWriter sw = new StringWriter();
		Result result = new StreamResult(sw);
		jaxb2Marshaller.marshal(obj, result);
		log.debug("MyMarshallerWrapper class marshall - End");
		return sw.toString();
	}

	/**
	 * Parse the XML to Java class
	 * 
	 * @param xml
	 * @param xmlClass
	 *            - Targeted class object for conversion
	 * @return
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public <T> T unmarshallXml(final InputStream xml, Class<?> xmlClass) throws JAXBException {
		log.debug("MyMarshallerWrapper class unmarshall - Begin");
		jaxb2Marshaller.setMappedClass(xmlClass);
		T unmarshal = (T) jaxb2Marshaller.unmarshal(new StreamSource(xml));
		log.debug("MyMarshallerWrapper class unmarshall - End");
		return unmarshal;
	}

}
