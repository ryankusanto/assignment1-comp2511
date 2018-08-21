package ass1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class Hotels {
	private String Name;
	private ArrayList<Booking> Bookings;
	private Detail RoomList;
	private ArrayList<Rooms> RoomDetails;

	
		public Hotels() {
			Bookings = new ArrayList<Booking>();
			RoomDetails = new ArrayList<Rooms>();
			RoomList = new Detail();
		}
		
		public String getHotelName() { //Returns name of hotel
			return Name;
		}
		
		public void addHotelName(String name) { //add hotel name to object
			Name = name;
		}
		
		public void addRoom(int number, int capacity) { //add room to the hotel
			Rooms newRoom = new Rooms(number, capacity);
			RoomDetails.add(newRoom);
		}
		
		public void addRoomList(Detail list) { //add the hotel list to the object
			RoomList = list;
		}
		

		
		public Rooms getRooms(int i) { //get the details of a room (Room Number and type)
			Rooms buff = RoomDetails.get(i);
			return buff;
		}
		
		public int getNumOfRoom() { //get the number of total rooms in a hotel
			return RoomDetails.size();
		}
		
		public void addToList(int cap) { //updates the room list
			RoomList.addToList(cap);
		}
		
		public Booking getBooking(String name) { //Returns a booking made by customer's name
			int i = 0;
			Booking buffer;
			
			while(i < Bookings.size()) {
				buffer = Bookings.get(i);
				String Name = buffer.getBooker();
				if (name.contentEquals(Name)) {
					return buffer;
				}
				i++;
			}
			return null;
		}
		
		public int checkRooms(LocalDate bookdate, int stay, int type, int req, ArrayList<Integer> array) {
			//Check if rooms are available and no clashes occur
			
			int i = 0;
			int k = 0;
			int j = 0;
			int count = 0;
			Booking bookChecker = null; //Saves the bookings of a hotel to be checked
			Rooms roomChecker = null; //Saves the rooms of the hotel
			LocalDate endBook = bookdate.plusDays(stay); //booking start date + number of nights
			boolean booked = false;

			if(req == 0) return 0; //if theres no need for single/double/triple room
			
			while(i < RoomDetails.size()) { //checking room-wise order
				roomChecker = RoomDetails.get(i);
				if(type == roomChecker.getCapacity()) { //only check if its the same room type as needed
					k = 0;
					while(k < Bookings.size()) { //check for bookings of a room
						bookChecker = Bookings.get(k);
						j=0;
						booked = false;
						while(j < bookChecker.getNumOfRoom()) { //To check if a booked room have clashing schedule with new booking
							if(roomChecker.getRoomNum() == bookChecker.getRoomNumber(j)) { //if there is a booking for the room
								if(bookdate.isEqual(bookChecker.endDate()) || endBook.isEqual(bookChecker.getStartDate())) {
									//New booking starts at the same day where another booking ends
									booked = false; //No clash
								}
								else if(bookdate.isAfter(bookChecker.endDate()) || endBook.isBefore(bookChecker.getStartDate())) {
									//New booking starts days after other booking ends, or new booking ends before other booking starts
									booked = false; //no clash
								}
								else { //Any other way means that it clashes and hence room cant be booked
									booked = true;
								}
							}
						
							if(booked == true) break; //if booked and clash , break
							j++;
						}
						if(booked == true) break; //if booked and clash , break
						k++;
					}
				
					if(booked == false) {
						array.add(roomChecker.getRoomNum()); //if no clashing dates, book the room for new cust
						count++;
					}
					if (count == req) break; //if there is enough room already, finish
				}
				i++;
			}
			return count; //returns the number of available rooms acquired
		}
		
		public void addBookings(String name, ArrayList<Integer> roomToUse, int month, int date, int stay) {
			//Add bookings to a hotel for customers
			
			Booking newCust = new Booking();
			int i = 0;
			int k = 0;
			int roomCap = 0; //Roomtype / capacity
			int roomNum; //Room Number
			Rooms buffer; //used to store the rooms to add to the bookings
			
			while(i < roomToUse.size()) { //add all rooms from the available list
				roomNum = roomToUse.get(i);  //get Room number of the avalable, non-clashing room to be booked
				while(k < RoomDetails.size()) { //Looping through all rooms in a hotel
					buffer = RoomDetails.get(k); 
					if (buffer.getRoomNum() == roomNum) { //To get the capacity of the matching room
						roomCap = buffer.getCapacity();
					}
					k++;
				}
				
				newCust.addRoom(roomNum, roomCap); //Add those rooms to the bookings with room number and type filled in
				i++;
				k=0;
			}
			
			newCust.addName(name); //add customer name to booking
			newCust.addDates(month, date, stay); //add dates to the booking
			
			Bookings.add(newCust); //adds the new booking into the hotel bookings list
		}
		
		public void addImmediate(String name, int month, int date, int stay, int[] array) {
			//Works same as addBooking method, but this method is used especially for no bookings found in a hotel
			
			Booking newCust = new Booking();
			int i = 0;
			int roomCap = 0;
			int roomNum;
			
			
				while(i < RoomDetails.size()) { //loop by hotel number
					roomNum = RoomDetails.get(i).getRoomNum(); //get room number
					roomCap = RoomDetails.get(i).getCapacity(); //get room type
					
					if(roomCap == 1 && array[0] != 0) { //if room is single type and is required, add room to booking
						newCust.addRoom(roomNum, roomCap);
						array[0]--; //Required number of room for a type is decremented since we got one
					}
					else if(roomCap == 2 && array[1] != 0) { //if room is double type and is required, add room to booking
						newCust.addRoom(roomNum, roomCap);
						array[1]--;
					}
					else if(roomCap == 3 && array[2] != 0) { //if room is triple type and is required, add room to booking
						newCust.addRoom(roomNum, roomCap);
						array[2]--;
					}
					i++;
				}

			newCust.addName(name); //add customer name
			newCust.addDates(month, date, stay); //add dates to booking
			Bookings.add(newCust); //add new booking to bookings list
		}
		
		public void allBookings() { //Find all the bookings/occupancy for a room
			int i = 0;
			int k = 0;
			int j = 0;
			int roomNum; //Room number
			int buffNum; //buff room number
			int index = 0; //saves the index of the date stored in the arraylist dateArray
			Booking buff; //Booking buffer to use
			Rooms roomCheck;
			ArrayList<LocalDate> dateArray = new ArrayList<LocalDate>(); //Saves the booking dates of a room
			ArrayList<Integer> nights = new ArrayList<Integer>(); //Saves the number of nights a room is booked for
			
			while(i < RoomDetails.size()) { //Looping Room Number-wise
				roomCheck = RoomDetails.get(i);
				roomNum = roomCheck.getRoomNum();
				System.out.print(this.Name + " " + roomNum);
				while(k < Bookings.size()) { //across all bookings
					buff = Bookings.get(k);
					while(j < buff.getNumOfRoom()) { //check each booked room
						buffNum = buff.getRoomNumber(j);
						if(buffNum == roomNum) { //If a booking is found for a specific room (roomNum)
							dateArray.add(buff.getStartDate()); //add the date to the dateArray
							Collections.sort(dateArray); //sort the dateArray
							index = dateArray.indexOf(buff.getStartDate()); //get the index of the newly add date
							nights.add(index, buff.getDays()); //add the number of nights to the same index as the date
						}
						j++;
					}
					k++;
					j=0;
				}
				i++;
				printOccupancy(dateArray, nights); //print the occupancy of the room
				k=0;
				dateArray.clear(); //cleared to be used again
				nights.clear(); //cleared to be used again
			}
		}
		public boolean checkList(int[] array) { 
			//Checks the hotel's room list if there are actually enough rooms for customer's needs
			
			int i = 0;
			boolean flag = true;
			
			while(i < array.length) { //Looping by room types
				if (array[i] <= RoomList.getQuantity(i+1)) flag = true; //If there are enough rooms, flag = true
				else { //else flag = false and return
					flag = false;
					break;
				}
				i++;
			}
			return flag;
		}
		
		public boolean emptyBooking() { //Check if there are no bookings
			if(Bookings.isEmpty()) return true;
			return false;
		}
			
		public boolean destroyBooking(String name) { //Used in cancel to cancel bookings by customer's name
			int i = 0;
			Booking buff = null;
			
			while(i < Bookings.size()) {
				buff = Bookings.get(i);
				if(name.contentEquals(buff.getBooker())) {
					Bookings.remove(buff);
					return true;
				}
				i++;
			}
			
			return false;
		}
				
		public int countBookings() { //Returns number of bookings in a hotel
			return Bookings.size();
		}
		
		public static int roomParse(String type) { //Returns the type for single (type 1), double (type 2), triple (type 3)
			if(type.contentEquals("single")) return 1;
			else if(type.contentEquals("double")) return 2;
			else return 3;
		}
		
		public void rebook(Booking temp) { //rebook the previously canceled booking (used in change)
			Bookings.add(temp);
		}
		
		public void printOccupancy(ArrayList<LocalDate> dates, ArrayList<Integer> days) { //print the occupancy of a room
			int i = 0;
			int nights;
			LocalDate buffer;
			String month;
			
			if(!dates.isEmpty()) { //If there are bookings for the room
				while(i < dates.size()) {
					buffer = dates.get(i);
					nights = days.get(i);
					month = monthConvert(buffer.getMonthValue());
					System.out.print(" " + month + " " + buffer.getDayOfMonth() + " " + nights);
					i++;
				}
			}
			
			System.out.print("\n");
		}
		
		public String monthConvert(int month) { //Convert month value to its appropriate month
			if (month == 1) return "Jan";
			else if (month == 2) return "Feb";
			else if (month == 3) return "Mar";
			else if (month == 4) return "Apr";
			else if (month == 5) return "May";
			else if (month == 6) return "Jun";
			else if (month == 7) return "Jul";
			else if (month == 8) return "Aug";
			else if (month == 9) return "Sep";
			else if (month == 10) return "Oct";
			else if (month == 11) return "Nov";
			else return "Dec";
		}
}
