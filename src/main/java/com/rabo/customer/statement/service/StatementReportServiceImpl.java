package com.rabo.customer.statement.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rabo.customer.statement.business.StatementReportGenerator;
import com.rabo.customer.statement.exception.ApplicationException;
import com.rabo.customer.statement.model.Record;
import com.rabo.customer.statement.model.Records;
import com.rabo.customer.statement.model.StatementReport;
import com.rabo.customer.statement.util.MyMarshallerWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Sivaraj
 * @category : Service implementation
 * @since <b> Feb-29-2020 </b>
 * @version 1.0
 * 
 *          <pre>
 *          StatementReportServiceImpl - Generate statement report
 * 
 *          <pre>
 */
@Service
@Slf4j
public class StatementReportServiceImpl implements StatementReportService {

	@Autowired
	private StatementReportGenerator reportGenerator;

	@Autowired
	private MyMarshallerWrapper myMarshallerWrapper;

	/**
	 * Process XML file and generate statement report
	 */
	@Override
	public List<StatementReport> generateXmlReport(MultipartFile uploadfile) {
		List<StatementReport> reports = null;
		Records records = null;
		try {
			records = (Records) myMarshallerWrapper.unmarshallXml(uploadfile.getInputStream(), Records.class);
		} catch (IOException | JAXBException e) {
			log.error("StatementReportServiceImpl.generateXmlReport() - Unable to process input file", e);
			throw new ApplicationException("Unable to process input file");
		}
		// Call generate report method and get the failed data
		reports = reportGenerator.generateReport(records);
		return reports;
	}

	/**
	 * Process CSV file and generate report
	 */
	@Override
	public List<StatementReport> generateCsvReport(MultipartFile uploadfile) {
		List<StatementReport> reports = null;
		List<Record> recordList = new ArrayList<>();
		Records records = new Records();
		// try with resource handling - read CSV cells for each row
		try (CSVParser csvParser = CSVFormat.DEFAULT.withHeader()
				.parse(new InputStreamReader(uploadfile.getInputStream()));) {
			csvParser.forEach(csvRecord -> {
				Record record = new Record();
				record.setReference(Long.valueOf(csvRecord.get(0)));
				record.setAccountNumber(csvRecord.get(1));
				record.setDescription(csvRecord.get(2));
				record.setStartBalance(new BigDecimal(csvRecord.get(3)));
				record.setMutation(new BigDecimal(csvRecord.get(4)));
				record.setEndBalance(new BigDecimal(csvRecord.get(5)));
				recordList.add(record);
			});
			records.setRecord(recordList.toArray(new Record[] {}));
		} catch (IOException e) {
			log.error("StatementReportServiceImpl.generateCsvReport() - Unable to process your input file", e);
			throw new ApplicationException("Unable to process your input file");
		}
		// Call generate report method and get the failed data
		reports = reportGenerator.generateReport(records);
		return reports;
	}

}
