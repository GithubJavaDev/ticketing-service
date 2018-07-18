package com.assignment.ticketing.repository;

import com.assignment.ticketing.domain.SeatHold;

import com.assignment.ticketing.domain.Reservation;

public interface SeatRepository {
	
	public int numSeatsAvailable();
	
	public SeatHold findAndHoldSeats(int totalSeats,String customerEmail) ;
	
	public Reservation reserveSeats(int seatHoldId, String customerEmail) ;

}
