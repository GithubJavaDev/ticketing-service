package com.assignment.ticketing.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Reservation domain class
 * @author ravitha.lingampally
 *
 */
public class Reservation {
	
	@Getter
	private String reservationNumber;
	
	@Getter
	private String customerEmail;
	
	@Getter
	private List<Seat> seats;
	
	public Reservation(String reservationNumber, String customerEmail,List<Seat> seats) {
		this.reservationNumber = reservationNumber;
		this.customerEmail = customerEmail;
		this.seats = new ArrayList<>(seats);
	}

}
