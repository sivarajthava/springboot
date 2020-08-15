package com.rabo.customer.statement.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.ToString;

@XmlRootElement(name = "records")
@XmlAccessorType(XmlAccessType.PROPERTY)
@ToString
public class Records {

	private Record[] record;

	public Record[] getRecord() {
		return record;
	}

	public void setRecord(Record[] record) {
		this.record = record;
	}

}
