package com.assignment.ticketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.ticketing.domain.Reservation;
import com.assignment.ticketing.domain.SeatHold;
import com.assignment.ticketing.repository.SeatRepository;
/**
 * Concrete class for ticket service interface implementation.
 * @author ravitha.lingampally
 *
 */

@Service
public class DefaultTicketServiceImpl implements TicketService {
	
	@Autowired
	private SeatRepository repository;

	@Override
	public int numSeatsAvailable() {
		return repository.numSeatsAvailable();
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		return repository.findAndHoldSeats(numSeats, customerEmail);
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		Reservation reservation = repository.reserveSeats(seatHoldId, customerEmail);
		return reservation.getReservationNumber();
	}

}
