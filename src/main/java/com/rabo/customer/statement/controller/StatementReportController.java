package com.rabo.customer.statement.controller;

import static com.rabo.customer.statement.util.ReportConstants.csvFileFormats;
import static com.rabo.customer.statement.util.ReportConstants.xmlFileFormats;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.rabo.customer.statement.model.StatementReport;
import com.rabo.customer.statement.model.SuccessResponse;
import com.rabo.customer.statement.service.StatementReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Sivaraj
 * @category : REST resource class
 * @since <b> Feb-29-2020 </b>
 * @version 1.0
 * 
 *          <pre>
 *          StatementReportController - Delegation for uploaded files to
 *          generate the error statement report
 * 
 *          <pre>
 */
@RestController
@Slf4j
@RequestMapping(value = "/report")
@Api(value = "StatementReportController")
public class StatementReportController {

	@Autowired
	private StatementReportService reportService;

	/**
	 * Support both XML and CSV files and delegates to Report generation service
	 * 
	 * @param uploadfile
	 * @param fsreqid
	 * @return
	 */
	@ApiOperation(value = "Get the customer statement report ", response = SuccessResponse.class, tags = "RABO Customer Report", produces = "application/json", consumes = "multipart/form-data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<SuccessResponse> generateReport(
			@RequestPart("file") @ApiParam(name = "file", value = "file") MultipartFile uploadfile,
			@ApiParam(name = "fsreqid", value = "fsreqid") @RequestHeader(name = "fsreqid", required = false) String fsreqid) {

		SuccessResponse successResponse = null;
		List<StatementReport> reports = null;
		// Validate the uploaded file
		this.validate(uploadfile);

		if (xmlFileFormats.contains(uploadfile.getContentType())) {
			reports = reportService.generateXmlReport(uploadfile);
		} else if (csvFileFormats.contains(uploadfile.getContentType())) {
			reports = reportService.generateCsvReport(uploadfile);
		}
		successResponse = SuccessResponse.builder().reports(reports).success(true)
				.remarks(reports.isEmpty()? "You have uploaded a valid file and no errors are found" : "Having some error records").build();
		return new ResponseEntity<SuccessResponse>(successResponse, HttpStatus.OK);
	}

	/**
	 * Validate the uploaded file constraints such as file type, max size and so on
	 * 
	 * @param uploadfile
	 */
	private void validate(MultipartFile uploadfile) {
		if (uploadfile.isEmpty() || uploadfile.getSize() == 0) {
			log.warn("You have uploaded empty file, please select a file!");
			throw new MultipartException("You have uploaded empty file, please select a file!");
		}
		if (!(xmlFileFormats.contains(uploadfile.getContentType())
				|| csvFileFormats.contains(uploadfile.getContentType()))) {
			log.error("Invalid file format. Please select either XML or CSV file");
			throw new MultipartException("Invalid file format. Please select either XML or CSV file");
		}
		log.debug("Files are uploaded - {}", uploadfile.getContentType());
	}
}
