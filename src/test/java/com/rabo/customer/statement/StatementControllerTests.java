package com.rabo.customer.statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.rabo.customer.statement.controller.StatementReportController;
import com.rabo.customer.statement.exception.ReportExceptionHandler;
import com.rabo.customer.statement.model.StatementReport;
import com.rabo.customer.statement.model.SuccessResponse;
import com.rabo.customer.statement.service.StatementReportService;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@Slf4j
class StatementControllerTests {

	@InjectMocks
	StatementReportController reportController;

	@Mock
	StatementReportService reportService;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(reportController)
				.setControllerAdvice(new ReportExceptionHandler()).build();
	}

	@Test
	void testWrongFileExtension() throws Exception {
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "ImgFile.JPG", MediaType.IMAGE_JPEG_VALUE,
				getInputStream("ImgFile.JPG"));
		this.mockMvc
				.perform(MockMvcRequestBuilders.multipart("/report").file("file", mockMultipartFile.getBytes())
						.contentType(MediaType.MULTIPART_FORM_DATA_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testEmptyFileExtension() throws Exception {
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "empty-records.xml",
				MediaType.APPLICATION_XML_VALUE, "".getBytes());
		this.mockMvc
				.perform(MockMvcRequestBuilders.multipart("/report").file(mockMultipartFile)
						.contentType(MediaType.MULTIPART_FORM_DATA_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testEmptyFileExtensionCSV() throws Exception {
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "empty-records.csv", "text/csv",
				getInputStream("empty-records.csv"));
		this.mockMvc
				.perform(MockMvcRequestBuilders.multipart("/report").file(mockMultipartFile)
						.contentType(MediaType.MULTIPART_FORM_DATA_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testHappyPath() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		List<StatementReport> reports = new ArrayList<>();
		StatementReport report = StatementReport.builder().accountNumber("1").description("Test Data")
				.transactionReference(111L).build();
		reports.add(report);

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "valid-records.xml",
				MediaType.APPLICATION_XML_VALUE, getInputStream("valid-records.xml"));
		when(reportService.generateXmlReport(mockMultipartFile)).thenReturn(reports);
		ResponseEntity<SuccessResponse> responseEntity = reportController.generateReport(mockMultipartFile,
				UUID.randomUUID().toString());
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	void testHappyPathCSV() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		List<StatementReport> reports = new ArrayList<>();
		StatementReport report = StatementReport.builder().accountNumber("1").description("Test Data")
				.transactionReference(111L).build();
		reports.add(report);

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "valid-records.csv",
				"text/csv", getInputStream("valid-records.csv"));
		when(reportService.generateCsvReport(mockMultipartFile)).thenReturn(reports);
		ResponseEntity<SuccessResponse> responseEntity = reportController.generateReport(mockMultipartFile,
				UUID.randomUUID().toString());
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	void testFoundErrorStatement() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		List<StatementReport> reports = new ArrayList<>();
		StatementReport report = StatementReport.builder().accountNumber("1").description("Test Data")
				.transactionReference(111L).build();
		reports.add(report);

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "valid-records.xml",
				MediaType.APPLICATION_XML_VALUE, getInputStream("valid-records.xml"));
		when(reportService.generateXmlReport(mockMultipartFile)).thenReturn(reports);
		ResponseEntity<SuccessResponse> responseEntity = reportController.generateReport(mockMultipartFile,
				UUID.randomUUID().toString());
		assertThat(responseEntity.getBody().getReports().size()).isGreaterThan(0);
	}

	@Test
	void testFoundErrorRecordStatement() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		List<StatementReport> reports = new ArrayList<>();
		StatementReport report = StatementReport.builder().accountNumber("1").description("Test Data")
				.transactionReference(111L).build();
		reports.add(report);

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "valid-records.xml",
				MediaType.APPLICATION_XML_VALUE, getInputStream("valid-records.xml"));
		when(reportService.generateXmlReport(mockMultipartFile)).thenReturn(reports);
		ResponseEntity<SuccessResponse> responseEntity = reportController.generateReport(mockMultipartFile,
				UUID.randomUUID().toString());
		assertThat(responseEntity.getBody().getReports().get(0).getAccountNumber()).isEqualTo("1");
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
