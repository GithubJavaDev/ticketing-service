package com.assignment.ticketing.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.assignment.ticketing.domain.SeatHold;
import com.assignment.ticketing.domain.Reservation;
import com.assignment.ticketing.domain.Seat;

/**
 * In memory implementation of seat repository. Uses <Collection> objects to store seat,
 * seat holds and seat reservation data.
 * 
 * @author ravitha.lingampally
 *
 */
@Service
public class LocalCacheSeatRepository implements SeatRepository {
	
	private static  List<Seat> ALL_SEATS = new ArrayList<>();
	
	private final Map<Integer,SeatHold> holdSeats = new ConcurrentHashMap<>();
	
	private final Map<String,Reservation> reservedSeats = new ConcurrentHashMap<>();
	
	private AtomicInteger holdIdentifier = new AtomicInteger();
	
	
	@Value("${hold-expiry-duration}")
	private int holdExpiryInterval; // in seconds
	
	@Value("${row-count}")
	private int noOfRows;
	
	@Value("${column-count}")
	private int noOfColumns;
	
	/**
	 * Loads static seat data based on the configured number of rows and columns.
	 */
	@PostConstruct
	public void initializeSeats() {
		List<Seat> seats = new ArrayList<>();
		int count = 0;
		for(int i = 0;i<noOfRows;i++) {
			for(int j = 0; j<noOfColumns;j++) {
				count++;
				seats.add(new Seat(count,i,j));
			}
		}
		ALL_SEATS = Collections.unmodifiableList(seats);
	}
	
	@PreDestroy
	public void destroyCache() {
		ALL_SEATS.clear();
		holdSeats.clear();
		reservedSeats.clear();
	}
	
	public int numSeatsAvailable() {
		//evaluate in combination with reserved seats and hold seats 
		return this.findAvailableSeats().size();
	}
	
	/**
	 * Thread safe operation to hold seats. Persists the hold to in memory/cache holdSeats map.
	 */
	public synchronized SeatHold findAndHoldSeats(int totalSeats,String customerEmail) {
		
		List<Seat> availableSeats = this.findAvailableSeats();
		if(availableSeats.isEmpty() || availableSeats.size() <totalSeats) {
			throw new IllegalArgumentException("Not enough seats available for hold");
		}
		//create hold
		int nextHoldNumber = holdIdentifier.incrementAndGet();
		SeatHold hold = new SeatHold(nextHoldNumber,customerEmail,LocalDateTime.now().plusSeconds(holdExpiryInterval),availableSeats.subList(0, totalSeats));
		holdSeats.put(nextHoldNumber, hold);
		return hold;
		
	}
	
	public Reservation reserveSeats(int seatHoldId, String customerEmail) {
		SeatHold hold = holdSeats.get(seatHoldId);
		if(hold == null) {
			throw new IllegalArgumentException("Specified hold id does not exist"); 
		}
		if(hold.getExpirationDate().isBefore(LocalDateTime.now())){
			holdSeats.remove(seatHoldId); //eject hold
			throw new IllegalArgumentException("Specified hold expired, cannot reserve seats.");
		}
		
		Reservation reservation = new Reservation(UUID.randomUUID().toString(),customerEmail,hold.getSeats());
		reservedSeats.put(reservation.getReservationNumber(), reservation);
		holdSeats.remove(seatHoldId); //eject hold
		return reservation;
	}
	
	/**
	 * Filters reserved seats and hold seats that are not expired.
	 * @return
	 */
	private List<Seat> findAvailableSeats() {
		if(ALL_SEATS.isEmpty()) {
			return Collections.emptyList();
		}
		List<Seat> occupiedSeats = new ArrayList<>();
		if(!reservedSeats.isEmpty()) {
			Collection<Reservation> allReservations = reservedSeats.values();
			occupiedSeats.addAll( 
					allReservations.stream().flatMap(reservation->reservation.getSeats().stream()).collect(Collectors.toList())
			);
		}
		if(!holdSeats.isEmpty()) {
			Collection<SeatHold> allHolds = holdSeats.values();
					occupiedSeats.addAll(
							allHolds.stream().filter(hold->hold.getExpirationDate().isAfter(LocalDateTime.now()))
							.flatMap(hold->hold.getSeats().stream()).collect(Collectors.toList())
					);
		}
		List<Seat> copyOfAllSeats = new ArrayList<>(ALL_SEATS);
		copyOfAllSeats.removeAll(occupiedSeats);
		return copyOfAllSeats;
	}
	
	
}


