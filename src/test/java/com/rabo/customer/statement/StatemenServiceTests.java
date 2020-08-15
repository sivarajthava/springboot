package com.rabo.customer.statement;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.rabo.customer.statement.business.StatementReportGenerator;
import com.rabo.customer.statement.model.Record;
import com.rabo.customer.statement.model.Records;
import com.rabo.customer.statement.model.StatementReport;
import com.rabo.customer.statement.service.StatementReportServiceImpl;
import com.rabo.customer.statement.util.MyMarshallerWrapper;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@Slf4j
class StatemenServiceTests {

	@InjectMocks
	StatementReportServiceImpl reportServiceImpl;

	@Mock
	private StatementReportGenerator reportGenerator;

	@Mock
	private MyMarshallerWrapper myMarshallerWrapper;

	@Test
	void testFoundErrorRecordStatementXml() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		List<StatementReport> reports = new ArrayList<>();
		StatementReport report = StatementReport.builder().accountNumber("1").description("Test Data")
				.transactionReference(111L).build();
		reports.add(report);

		Records records = new Records();
		records.setRecord(new Record[] {});
		myMarshallerWrapper.initMarshaller();

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "valid-records.xml",
				MediaType.APPLICATION_XML_VALUE, getInputStream("valid-records.xml"));
		List<StatementReport> responseReports = reportServiceImpl.generateXmlReport(mockMultipartFile);
		assertThat(responseReports.size()).isEqualTo(0);
	}

	@Test
	void testFoundErrorRecordStatementCSV() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		List<StatementReport> reports = new ArrayList<>();
		StatementReport report = StatementReport.builder().accountNumber("1").description("Test Data")
				.transactionReference(111L).build();
		reports.add(report);

		Records records = new Records();
		records.setRecord(new Record[] {});

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "valid-records.csv", "text/csv",
				getInputStream("valid-records.csv"));
		List<StatementReport> responseReports = reportServiceImpl.generateCsvReport(mockMultipartFile);
		assertThat(responseReports.size()).isEqualTo(0);
	}

	private byte[] getInputStream(String path) throws FileNotFoundException {
		byte[] bdata = null;
		ClassPathResource cpr = new ClassPathResource(path);
		try {
			bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return bdata;
	}

}
