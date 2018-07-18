package com.assignment.ticketing.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.assignment.ticketing.domain.Reservation;
import com.assignment.ticketing.domain.SeatHold;

public class LocalCacheSeatRepositoryTest {
	
	
	private LocalCacheSeatRepository repository;
	
	
	@Before
	public void setup() {
		this.repository = new LocalCacheSeatRepository();
		ReflectionTestUtils.setField(repository, "noOfRows", 3);
		ReflectionTestUtils.setField(repository, "noOfColumns", 2);
		ReflectionTestUtils.setField(repository, "holdExpiryInterval", 1);
		repository.initializeSeats();
	}
	
	@After
	public void tearDown() {
		repository.destroyCache();
	}
	
	@Test
	public void testNumSeatsAvailable() {
		int noOfSeats = repository.numSeatsAvailable();
		assertEquals(6,noOfSeats);
	}
	
	@Test
	public void testFindAndHoldSeats() {
		assertEquals(6,repository.numSeatsAvailable());
		SeatHold hold = repository.findAndHoldSeats(2, "xyz@xyz.com");
		assertNotNull(hold);
		assertThat(hold.getSeats(),hasSize(2));
		assertEquals(4,repository.numSeatsAvailable());
	}
	
	@Test
	public void testReserveSeats() {
		SeatHold hold = repository.findAndHoldSeats(2, "xyz@xyz.com");
		assertNotNull(hold);
		Reservation reservation = repository.reserveSeats(hold.getHoldNumber(), "xyz@xyz.com");
		assertNotNull(reservation);
		assertThat(reservation.getSeats(),hasSize(2));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReserveSeatsWithAnInvalidHoldId() {
		repository.reserveSeats(1, "fake@fake.com");
	}
	
	@Test
	public void testAvaialbeSeatsWithHoldExpiration() {
		ReflectionTestUtils.setField(repository, "holdExpiryInterval", 0);
		SeatHold hold = repository.findAndHoldSeats(2, "xyz@xyz.com");
		assertNotNull(hold);
		int noOfSeats = repository.numSeatsAvailable();
		assertEquals(6,noOfSeats);
	}

}
