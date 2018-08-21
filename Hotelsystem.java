package ass1;

import java.util.ArrayList;

public class Hotelsystem {
	private ArrayList<Hotels> hotel;
	
		public Hotelsystem() {
			hotel = new ArrayList<Hotels>();
		}
		
		public int getSize() { //return number of hotels
			return hotel.size();
		}
		
		public Hotels getHotel(String Name) { //find hotel by its name
			String name = null;
			Hotels fromList = null;
			int i=0;
			
			while(i < hotel.size()) { //Iterate through all hotel
				fromList = hotel.get(i);
				name = fromList.getHotelName();
				if (name.contentEquals(Name)) { //If found matching hotel name
					return fromList;
				}
				i++;
			}
			return null;
		}
				
		public Hotels iterateHotel(int i) { //Returns hotel for iteration (index-wise)
			Hotels hotel1 = hotel.get(i);
			return hotel1;
		}
		
		public void addHotels(Hotels hotel) { //Add hotel to the class
			this.hotel.add(hotel);
		}
				
		public void addBooking(String name, ArrayList<Integer> roomToUse, int month, int date, int stay, String hotelName) {
			//Add booking for a hotel
			Hotels buffer = getHotel(hotelName);
			buffer.addBookings(name, roomToUse, month, date, stay);
		}
		
		public void immediateBooking(String name, int month, int date, int stay, String hotelName, int[] array) {
			//Add an immediateBooking for a hotel
			Hotels buffer = getHotel(hotelName);
			buffer.addImmediate(name, month, date, stay, array);
		}
		
		public boolean cancelBooking(Hotelsystem hotelSystem, String[] separated) {
			//Cancel a booking (Destroy)
			String booker = separated[1];
			Hotels hotel1 = null;
			boolean flag = false;
			int i = 0;
			
			while(!flag) { //Find the hotel that the customer is booked to
				hotel1 = hotelSystem.iterateHotel(i);
				flag = hotel1.destroyBooking(booker); //Destroy booking
				i++;
			}

			return flag;
		}
		
		public Hotels findHotel(String bookedBy) { //Find hotel that a customer (bookedBy) is booked to
			int i = 0;
			Hotels hotel1 = null;
			Booking buff = null;
			
			while(buff == null) {
				hotel1 = iterateHotel(i);
				buff = hotel1.getBooking(bookedBy); //get booking from a hotel if there's one
				i++;
			}
			
			return hotel1;
		}
		
		public void printJob(String name) { //Methods that link hotelsystem to hotel for printings
			Hotels hotel1;
			
			hotel1 = getHotel(name);
			hotel1.allBookings();
		}
}
