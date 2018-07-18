package com.assignment.ticketing.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


/**
 * Seat domain class
 * @author ravitha.lingampally
 *
 */
@EqualsAndHashCode
@ToString
public class Seat {
	/**
	 * Seat unique identifier
	 */
	@Getter
	private Integer number;
	
	/**
	 * Seat row number
	 */
	@Getter
	private Integer row;
	
	/**
	 * Seat column number
	 */
	@Getter
	private Integer column;
	
	public Seat(Integer number, Integer row, Integer column) {
		this.number = number;
		this.row=row;
		this.column=column;
	}
	
}
