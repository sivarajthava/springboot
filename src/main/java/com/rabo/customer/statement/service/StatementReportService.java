package com.rabo.customer.statement.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rabo.customer.statement.model.StatementReport;

public interface StatementReportService {

	List<StatementReport> generateXmlReport(MultipartFile uploadfile);

	List<StatementReport> generateCsvReport(MultipartFile uploadfile);

}
