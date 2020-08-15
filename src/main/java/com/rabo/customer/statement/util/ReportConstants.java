package com.rabo.customer.statement.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;

/**
 * 
 * @author Sivaraj
 * @category : Constant file
 * @since <b> Feb-29-2020 </b>
 * @version 1.0
 * 
 *          <pre>
 *          Statement application constant class.
 * 
 *          <pre>
 */
public final class ReportConstants {

	public static final String METHOD_BEGIN = "{} - {} - Begin";
	public static final String METHOD_END = "{} - {} - End";
	// List of xls,csv supported files based on OS
	public static List<String> csvFileFormats = Arrays.asList("text/csv", "application/vnd.ms-excel");

	public static List<String> xmlFileFormats = Arrays.asList(MediaType.TEXT_XML_VALUE,
			MediaType.APPLICATION_XML_VALUE);

	public static final String MAX_FILE_SIZE_EXCEEDED = "Maximum request length exceeded. Please upload accepted file size.";

	public static final String INTERNAL_SERVER_ERROR = "Unable to process your request. Please contact system administrator.";

	public static final String REMARKS_DUPLICATE_REFERENCE = "Duplicate Reference";

	public static final String REMARKS_ERROR_DATA = "Error record";
}
