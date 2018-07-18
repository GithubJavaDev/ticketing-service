package com.assignment.ticketing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.assignment.ticketing.domain.SeatHold;
import com.assignment.ticketing.service.TicketService;

/**
 * Spring Boot entry point to ticketing service application
 * @author ravitha.lingampally
 *
 */
@SpringBootApplication
public class TicketingServiceApplication  implements CommandLineRunner {
	
	@Autowired
	private TicketService ticketService;

	public static void main(String[] args) {
		SpringApplication.run(TicketingServiceApplication.class, args);
	}
	
	@Override
    public void run(String... args) throws Exception {
		
		introDisplayMessage();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
	       while ((line = reader.readLine()) != null) {
	    	   if(line.equals("seats")) {
	   			   System.out.println(ticketService.numSeatsAvailable());
	   		   }else if(line.equals("hold")) {
	   			   processHoldCommand(reader);				   				
	   		   }else if(line.equals("reserve")) {
	   			   processReserveCommand(reader);   					   				
	   		   }else{
	   			   System.out.println("Operation not available!");
	   		   }
	      }					
    }
	
	private void introDisplayMessage() {
		System.out.println("Welcome to XYZ ticket service application. Please use the below commands to reserve seats.");
		System.out.println("- Input 'seats' for finding the number of available seats");
		System.out.println("- Input 'hold' for holding the seats.");
		System.out.println("- Input 'reserve' for reserving the seats");
	}
	
	private void processHoldCommand(BufferedReader reader) throws IOException {
	 	System.out.println("Enter number of seats:");	
	   	String line = reader.readLine();
		int noOfSeats = Integer.parseInt(line);
		System.out.println("Enter email address:");
		line = reader.readLine();
		try{
			SeatHold hold = ticketService.findAndHoldSeats(noOfSeats, line);
			System.out.println("Seat hold details.. :"+hold);
		}catch(Exception ex) {
			System.out.println("Hold seats operation failed with the following error :");
			System.out.println(ex.getMessage());
		}   
	}
	
	private void processReserveCommand(BufferedReader reader) throws IOException{
		System.out.println("Enter seat hold id:");	
	   	String line = reader.readLine();
		int seatHoldId = Integer.parseInt(line);
		System.out.println("Enter email address:");
		line = reader.readLine();
		try{
			String confirmationCode = ticketService.reserveSeats(seatHoldId, line);
			System.out.println("Reservation confirmation code is :"+confirmationCode);
		}catch(Exception ex) {
			System.out.println("Reserved seats operation failed with the following error :");
			System.out.println(ex.getMessage());
		}
	}
}
