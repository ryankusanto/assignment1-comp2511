package ass1;

public class Rooms {
	private int roomNum;
	private int capacity;
	
	
		public Rooms(int number, int capacity) {
			roomNum = number;
			this.capacity = capacity;
		}
		
		public int getCapacity() { //Returns Room type / capacity
			return capacity;
		}
		
		public int getRoomNum() { //returns Room Number
			return roomNum;
		}
		
}
