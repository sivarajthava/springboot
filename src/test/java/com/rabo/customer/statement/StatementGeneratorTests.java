package com.rabo.customer.statement;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.rabo.customer.statement.business.StatementReportGenerator;
import com.rabo.customer.statement.model.Record;
import com.rabo.customer.statement.model.Records;
import com.rabo.customer.statement.model.StatementReport;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class StatementGeneratorTests {

	@InjectMocks
	private StatementReportGenerator reportGenerator;

	@Test
	void testFindErrorRecord() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		List<StatementReport> reports = new ArrayList<>();
		StatementReport report = StatementReport.builder().accountNumber("1").description("Test Data")
				.transactionReference(111L).build();
		reports.add(report);

		Records records = new Records();
		Record record = new Record();
		record.setAccountNumber("1");
		record.setDescription("Test Data");
		record.setReference(111L);
		records.setRecord(new Record[] { record });

		List<StatementReport> responseReports = reportGenerator.generateReport(records);
		assertThat(responseReports.size()).isEqualTo(1);
	}

	@Test
	void testFindSuccessRecord() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		List<StatementReport> reports = new ArrayList<>();
		StatementReport report = StatementReport.builder().accountNumber("1").description("Test Data")
				.transactionReference(111L).build();
		reports.add(report);

		Records records = new Records();
		Record record = new Record();
		record.setAccountNumber("1");
		record.setDescription("Test Data");
		record.setReference(111L);
		record.setStartBalance(new BigDecimal(1));
		record.setMutation(new BigDecimal(1));
		record.setEndBalance(new BigDecimal(2));
		records.setRecord(new Record[] { record });

		List<StatementReport> responseReports = reportGenerator.generateReport(records);
		assertThat(responseReports.size()).isEqualTo(0);
	}

}
