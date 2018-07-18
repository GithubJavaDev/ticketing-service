package com.assignment.ticketing.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

/**
 * SeatHold domain class
 * @author ravitha.lingampally
 *
 */
@ToString
public class SeatHold {
	
	@Getter
	private Integer holdNumber;
	
	@Getter
	private String customerEmail;
	
	@Getter
	private LocalDateTime expirationDate;
	
	@Getter
	private List<Seat> seats;
	
	public SeatHold(Integer holdNumber, String customerEmail, LocalDateTime expirationDate,List<Seat> seats) {
		this.holdNumber = holdNumber;
		this.customerEmail = customerEmail;
		this.expirationDate = expirationDate;
		this.seats = new ArrayList<>(seats);
	}
	
	
}
