package ass1;

import java.time.LocalDate;
import java.util.ArrayList;

public class Booking {
	private String bookedBy;
	private ArrayList<Rooms> room;
	private LocalDate startDate;
	private int days;
	
		public Booking() { 
			room = new ArrayList<Rooms>();
		}
	
		public void addRoom(int number, int capacity) { //add rooms to booking
			Rooms newRoom = new Rooms(number, capacity);
			room.add(newRoom);
		}
		
		public int getRoomNumber(int i) { //get room number of booked room by i
			return room.get(i).getRoomNum();
		}
		
		public int getNumOfRoom() { //returns number of rooms booked 
			return room.size();
		}
		
		public LocalDate getStartDate() { //gets the start booking date
			return startDate;
		}	
		
		public LocalDate endDate() { //returns the end date = start date + days
			return startDate.plusDays(days);
		}
		
		public int getDays() { //return the days
			return days;
		}
		
		public void addName(String name) { //add name for the booking
			bookedBy = name;
		}
		
		public String getBooker() { //returns the booker's name
			return bookedBy;
		}
		
		public ArrayList<Rooms> getRooms() { //return the whole list of rooms booked
			return room;
		}
		
		public void addDates(int month, int date, int stay) { //add starting dates and days for the booking
			startDate = LocalDate.of(2018, month, date);
			days = stay;
		}
		
		public void copyBooking(Booking temp) { //Copy booking from current to temp
			int i = 0;
			Rooms buff = null;
			LocalDate tempDate;
			
			temp.addName(bookedBy);
			
			while(i < room.size()) {
				buff = room.get(i);
				temp.addRoom(buff.getRoomNum(), buff.getCapacity());
				i++;
			}
			tempDate = startDate;
			
			int month = tempDate.getMonthValue();
			int day = tempDate.getDayOfMonth();
			temp.addDates(month, day, days);
		}
		
}
