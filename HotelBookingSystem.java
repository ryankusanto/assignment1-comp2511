package ass1;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class HotelBookingSystem {
	
	public static void main(String[] args) {
		String sentence = new String(); //Whole line of argument from file
		String separated[] = null; //sentence split is put in separated
		int i = 0;
		
		Hotelsystem hotelSystem = new Hotelsystem();
		
	    Scanner sc = null;
	    try
	      {
	          sc = new Scanner(new File(args[0]));    // args[0] is the first command line argument
	          // Read input from the scanner here
	          while(sc.hasNextLine()) {
	        	  sentence = sc.nextLine();
	        	  separated = sentence.split(" ");
	        	  
	        	  if(separated[0].contentEquals("Hotel")) {
	        		  //create hotel, rooms
	        		  /*
	        		   * 1. check if hotel is made
	        		   * 2. if made, add room. if not make hotel and add
	        		   */
	        		  
	        		  Hotels hotel1 = hotelSystem.getHotel(separated[1]); //Find a hotel that has the name mentioned
	        		  if (hotel1 == null) { //If no hotel was found, make a new hotel with name from separated[1]
	        			  hotel1 = new Hotels();
	        			  hotelSystem.addHotels(hotel1);
	        			  String buff = String.valueOf(separated[1]);
	        			  hotel1.addHotelName(buff);
	        		  }        		  
	        		  
	        		  int num = Integer.parseInt(separated[2]); //Room Number 
	        		  int cap = Integer.parseInt(separated[3]); //Type / Capacity of the room
	        		  hotel1.addRoom(num, cap); //Add Room to hotel
	        		  hotel1.addToList(cap); //Add Room types list
	        		  
	        	  }
	        	  else if(separated[0].contentEquals("Booking")) {
	        		  boolean success = false;
	        		  i = 0;
	        		  
	        		  success = bookingCustomer(hotelSystem, separated); //Book a customer 
	        		  
	        		  if(success) {
	        			  
	        			  Hotels reservedHotel = hotelSystem.findHotel(separated[1]); //Find hotel booked by the customer
	        			  Booking reservation = reservedHotel.getBooking(separated[1]); //Find booking made for customer
	        			  System.out.print("Booking " + reservation.getBooker() + " " + reservedHotel.getHotelName());
	        			  while(i < reservation.getNumOfRoom()) { //printing out all the rooms for the customer
	        				  System.out.print(" " + reservation.getRoomNumber(i));
	        				  i++;
	        			  }
	        			  System.out.print("\n");
	        		  }

	        		  else System.out.println("Booking rejected"); //Fails to get a reservation for customer
	        	  }
	        	  else if(separated[0].contentEquals("Change")) {
	        		  //change a booking data
	        		  /*
	        		   * assume hotel is there
	        		   * 1. Cancel the previous bookings made by a customer
	        		   * 2. Try to book the new bookings
	        		   * 3. If fail to book, rebook the old booking and reject change
	        		   */
	        		  
	        		  i=0;
	        		  boolean success = false;
	        		  Booking buffer = new Booking(); //Used to save old booking information
	        		  Hotels hotel1 = hotelSystem.findHotel(separated[1]); //Find hotel by customer name
	        		  Hotels hotelTemp; //Saving Which hotel the customer was booked in
	        		  Booking old = hotel1.getBooking(separated[1]); //Get the old bookings by customer
	        		  
	        		  old.copyBooking(buffer); //Copy data from the old booking to the buffer (temporary) booking
	        		  
	        		  hotelTemp = hotelSystem.getHotel(hotel1.getHotelName()); //Saving the previous hotel
	        		  success = hotelSystem.cancelBooking(hotelSystem, separated);
	        		  success = bookingCustomer(hotelSystem, separated);
	        		  
	        		  if(success) { //If booking for the new change succeeds, same as normal booking
	        			  
	        			  Hotels reservedHotel = hotelSystem.findHotel(separated[1]);
	        			  Booking reservation = reservedHotel.getBooking(separated[1]);
	        			  System.out.print("Change " + reservation.getBooker() + " " + reservedHotel.getHotelName());
	        			  while(i < reservation.getNumOfRoom()) {
	        				  System.out.print(" " + reservation.getRoomNumber(i));
	        				  i++;
	        			  }
	        			  System.out.print("\n");
	        		  }

	        		  else { //Failed to book the new changes, hence rebook the previous bookings
	        			  
	        			  hotelTemp.rebook(old);
	        			  System.out.println("Change rejected");
	        		  }
	        		  
	        	  }
	        	  else if(separated[0].contentEquals("Cancel")) {
	        		  //Cancel a booking by name
	        		  /*
	        		   * check any reservations made
	        		   * 1. find by name, cancel all bookings
	        		   */
	        		  
	        		  boolean flag = false;
	        		  flag = hotelSystem.cancelBooking(hotelSystem, separated);
	        		  
	        		  if(flag) System.out.println("Cancel " + separated[1]); //If cancel succeeds
	        		  else System.out.println("Cancel rejected"); //If cancel fails
	        	  }
	        	  else if(separated[0].contentEquals("Print")) {
	        		  //Print occupancy on all hotel
	        		  hotelSystem.printJob(separated[1]);
	        		  
	        	  }
	          }
	      }
	    catch (FileNotFoundException e)
	      {
	          System.out.println(e.getMessage());
	      }
	    finally
	      {
	          if (sc != null) sc.close();
	      }
		
	}

	public static int monthConvert(LocalDate date) { //Converts month to its integer value
			return date.getMonthValue();
	}
	
	public static LocalDate conversion(String month, String date) { //Making the date used and stored to be in a specific format
		String newDate = "2018 " + month + " " + date;
		int dateNum = Integer.parseInt(date);
		DateTimeFormatter format;
		
		if(dateNum > 9) {
			format = DateTimeFormatter.ofPattern("yyyy MMM dd");
		}
		else {
			format = DateTimeFormatter.ofPattern("yyyy MMM d");
		}
		return LocalDate.parse(newDate, format);
	}
	
	public static int roomParse(String type) { //Returns the type for single (type 1), double (type 2), triple (type 3)
		if(type.contentEquals("single")) return 1;
		else if(type.contentEquals("double")) return 2;
		else return 3;
	}
	
	public static boolean bookingCustomer (Hotelsystem hotelSystem, String[] separated) {
		
		  //make a booking if available
		  /*
		   * assume hotel is ready
		   * 1.check if room is there in every hotel
		   * 2.reject if room is not there 
		   * 3.if room OK, check booked or not on the booking day
		   * 
		   */
		
		  int i = 0;
		  int k = 5;
		  int roomCap = 0; //RoomCapacity / RoomType
		  int req = 0; //Required  number of room
		  LocalDate bookdate = conversion(separated[2], separated[3]); //Create a full date in a specific format
		  int month = monthConvert(bookdate); //store month in int value
		  int date = Integer.parseInt(separated[3]); //gets the date
		  int stay = Integer.parseInt(separated[4]); //gets night staying
		  boolean available = false; //Flag is up-ed if there are no bookings for a hotel
		  boolean roomFlag = false; //Flag that tells us that the hotel satisfies the needs of customer
		  boolean bookFlag = true; //Flag that tells us if booking is possible
		  ArrayList<Integer> roomOK = new ArrayList<Integer>(); //holds available room
		  Hotels hotel1 = null;
		  int[] arrayReq = {0, 0, 0}; //The index of this array is the room type, while content is the required number
		  
		  while(k < separated.length) { //making a table of required number of rooms per type
			  req = Integer.parseInt(separated[k+1]);
			  if(separated[k].contentEquals("single")) arrayReq[0] += req; //add 1 to array[0] (single)
			  else if(separated[k].contentEquals("double")) arrayReq[1] += req; //add 1 to array[1] (double)
			  else if(separated[k].contentEquals("triple")) arrayReq[2] += req; //add 1 to array[2] (triple)
			  k += 2;
		  }
		  
		  k = 5;
		  
		  while(i < hotelSystem.getSize()) { //to check if hotels got the room
			hotel1 = hotelSystem.iterateHotel(i); //get hotel
			
			roomFlag = hotel1.checkList(arrayReq); //Check the hotel's room list if there are enough rooms to satisfy cust
			if (roomFlag == true) { //If hotel rooms satisfy customer's needs
				//if no bookings at all, immediate booking since there's enough room and no one is booked yet
				if(hotel1.emptyBooking()) {
					available = true;
					bookFlag = true;
					break;
				}
				
				//bookings are there, we need to check if there are any clashing schedule
				int j = 0;
				int count = 0;
				
				while(j < arrayReq.length) { //looping by roomTypes
					count = 0;
					roomCap = j+1; //Room type
					count = hotel1.checkRooms(bookdate, stay, roomCap, arrayReq[j], roomOK); //Check if the rooms are available to be booked
					if(count != arrayReq[j]) { //If number of available rooms is not equal to the need of customer, check other hotel
						bookFlag = false;
						roomOK.clear();
						break; //if there is no enough rooms, exit
					}
					j++;
				}
			}
			
			if(bookFlag == true && roomFlag == true) break; //If booking is possible already, exit the loop
			i++;
		  }
		  i=0;
		  if (hotel1 == null) { //If there are no hotels in the first place
			  //print reject
			  return false;
			  
		  }
		  
		  //immediate add booking
		  if(bookFlag == true && available == true && roomFlag == true) { //If no bookings were there
			  String hotelName = hotel1.getHotelName();
			  hotelSystem.immediateBooking(separated[1], month, date, stay, hotelName, arrayReq);

			  return true;
		  }
		  
		  //After checking all rooms
		  else if (bookFlag == true && roomFlag == true) { //If booking is possible after checking all the rooms
			   //add booking and print
			  
			  String hotelName = hotel1.getHotelName();
			  
			  hotelSystem.addBooking(separated[1], roomOK, month, date, stay, hotelName); //Add booking to the hotel with checked and available room
			  
			  return true;
			  
		  }
		  else return false; //Else fails to book.
	}
	

}
