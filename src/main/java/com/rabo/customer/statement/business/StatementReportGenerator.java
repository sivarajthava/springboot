package com.rabo.customer.statement.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.rabo.customer.statement.model.Record;
import com.rabo.customer.statement.model.Records;
import com.rabo.customer.statement.model.StatementReport;
import com.rabo.customer.statement.util.ReportConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Sivaraj
 * @category : Business report generator
 * @since <b> Feb-29-2020 </b>
 * @version 1.0
 * 
 *          <pre>
 *          StatementReportGenerator - Generate statement report by processing
 *          each record of statement and find out error data and duplicates
 * 
 *          <pre>
 */
@Component
@Slf4j
public class StatementReportGenerator {

	/**
	 * Generate error report data
	 * 
	 * @param records
	 * @return
	 */
	public List<StatementReport> generateReport(Records records) {

		// Initialization part
		final List<StatementReport> reports = new ArrayList<>();
		final List<Record> recordList = Arrays.asList(records.getRecord());

		// Find out any error data from the list
		final List<Record> errorRecrods = recordList.parallelStream()
				.filter(record -> isErrorRecord(recordList, record)).collect(Collectors.toList());

		log.info("Statement report processed and found {} records with integirity issue.", errorRecrods.size());

		// Generate error model
		errorRecrods.forEach(record -> {
			StatementReport statementReport = StatementReport.builder().transactionReference(record.getReference())
					.accountNumber(record.getAccountNumber()).description(record.getDescription())
					.remarks(record.getRemarks()).build();
			reports.add(statementReport);
		});
		return reports;
	}

	/**
	 * Assuring error data based on Start Balance + Mutation == End Balance and any
	 * duplicate reference number(s) are found.
	 * 
	 * @param record
	 */
	private boolean isErrorRecord(final List<Record> recordList, final Record record) {
		try {
			// Calculation start Balance + Mutation == End Balance
			if (!(record.getStartBalance().add(record.getMutation()).compareTo(record.getEndBalance()) == 0)) {
				record.setRemarks(ReportConstants.REMARKS_ERROR_DATA);
			}
			// Check the duplicate reference number found or not
			if (isDuplicateReference(recordList, record)) {
				record.setRemarks(Objects.nonNull(record.getRemarks())
						? record.getRemarks() + " and " + ReportConstants.REMARKS_DUPLICATE_REFERENCE
						: ReportConstants.REMARKS_DUPLICATE_REFERENCE);
			}
		} catch (Exception e) {
			log.warn("Exception occured while doing manipulation of statement report for reference - {}",
					record.getReference());
			record.setRemarks(ReportConstants.REMARKS_ERROR_DATA);
		}
		return Objects.nonNull(record.getRemarks());
	}

	/**
	 * Check the duplicate reference number found or not
	 * 
	 * @param recordList
	 * @param record
	 * @return
	 */
	private boolean isDuplicateReference(final List<Record> recordList, final Record record) {
		final AtomicInteger count = new AtomicInteger();
		// For each record we are checking against list of records
		recordList.stream().forEach(p -> {
			if (p.getReference().equals(record.getReference())) {
				count.getAndIncrement();
			}
		});
		return count.get() >= 2;
	}
}
