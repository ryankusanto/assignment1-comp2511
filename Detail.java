package ass1;

public class Detail {
	private int single = 0;
	private int doub = 0;
	private int triple = 0;
	
		public void addSingle () {
			single++;
		}
		
		public void addDouble() {
			doub++;
		}
		
		public void addTriple() {
			triple++;
		}
		
		public int getQuantity(int size) {
			if(size == 1) return single;
			else if(size == 2) return doub;
			else return triple;
		}
		
		public void addToList(int cap) {
			if (cap == 1) addSingle();
			else if (cap == 2) addDouble();
			else addTriple();
		}
}
