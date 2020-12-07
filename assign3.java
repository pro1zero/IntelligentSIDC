package assignment3;
import java.util.*;
public class assign3 {
	public static void main(String[] args) {
//		Scanner scan = new Scanner(System.in);
//		LessThanThousand ltt = new LessThanThousand();
//		while(scan.hasNextLong()) {
//			long currentKey = scan.nextLong();
//			ltt.addEntry(currentKey, ("FL-DOB-" + currentKey));
//		}
//		ltt.print();
//		ltt.removeEntry(45454545);
//		ltt.removeEntry(98989898);
//		System.out.println();
//		ltt.print();
//		scan.close();
//		98989898
//		40132415
//		45454545
//		12121212
//		!
	}
}
class IntelligentSIDC{
	int currentSizeOfSystem;
	boolean useTrees;
	DoublyLL dll;
	long currentKey;
	LessThanThousand ltt;
	
	public IntelligentSIDC() {
		currentSizeOfSystem = -1;
		useTrees = false;
		dll = new DoublyLL();
		currentKey = -1;
		ltt = new LessThanThousand();
	}

	public void setSIDCThreshold(int size) {
		if(size <= 1000) {
			useTrees = false;
		}
	}
	
	public void add(long key, String value) {
		long newKey = generate();
		dll.add(newKey);
		ltt.addEntry(newKey, "FL-DOB-" + newKey);
	}
	
	public void remove(long key) {
		boolean contains = dll.containsNodeWithKey(key);
		if(!contains) return;
		dll.remove(key);
		ltt.removeEntry(key);
	}
	
	public int rangeKey(long key1, long key2) {
		return dll.rangeKey(key1, key2);
	}
	
	public long generate() {
		generateHelper();
		return currentKey;
	}
	
	public String getValues(long key) {
		return ltt.getValue(key);
	}
	
	public long nextKey(long key) {
		return dll.nextKey(key);
	}
	
	public long prevKey(long key) {
		return dll.prevKey(key);
	}
	
	public void generateHelper() {
		Random rand = new Random();
		long keyToGenerate = 10000000 + rand.nextInt(89999999);
		boolean alreadyContains = dll.containsNodeWithKey(keyToGenerate);
		if(alreadyContains) {
			generateHelper();
			return;
		}
		currentKey = keyToGenerate;
	}
	
	class DoublyLL{
		Node head;
		Node tail;
		
		public DoublyLL(){
			head = null;
			tail = null;
		}
		
		class Node{
			long key;
			Node next;
			Node prev;
			
			public Node(long key, Node next, Node prev) {
				this.key = key;
				this.next = next;
				this.prev = prev;
			}
		}
		
		public long nextKey(long key) {
			Node temp = head;
			while(temp != null && temp.key != key) {
				temp = temp.next;
			}
			if(temp == null || temp.next == null) return -1;
			return temp.next.key;
		}
		
		public long prevKey(long key) {
			Node temp = head;
			while(temp != null && temp.key != key) {
				temp = temp.next;
			}
			if(temp == null || temp == head) return -1;
			return temp.prev.key;
		}
		
		public int rangeKey(long key1, long  key2) {
			Node temp = head;
			int keysInBetween = 0;
			while(temp != null) {
				if(temp.key > key1 && temp.key < key2) {
					keysInBetween += 1;
				}
			}
			return keysInBetween;
		}
		
		public boolean containsNodeWithKey(long key) {
			Node temp = head;
			while(temp != null && temp.key != key) {
				temp = temp.next;
			}
			return (temp != null);
		}
		
		public void add(long key) {
			if(head == null) {
				head = new Node(key, head, null);
				return;
			}
			tail = new Node(key, null, tail);
		}
		
		public void remove(long key) {
			Node temp = head;
			while(temp != null && temp.key != key) {
				temp = temp.next;
			}
			if(temp == null) {
				return;
			}
			if(temp.prev != null)
				temp.prev.next = temp.next;
			if(temp.next != null)
				temp.next.prev = temp.prev;
			
			temp.prev = null;
			temp.next = null;
		}
	}
	class LessThanThousand{
		
		public LessThanThousand() {
			arrayOfNodes = new EntryList[10];
			for(int i = 0; i < 10; i++) {
				arrayOfNodes[i] = new EntryList();
			}
		}
		
		EntryList[] arrayOfNodes;
		
		public int computeIndex(long key) {
			long firstDigit = -1;
			try {
				firstDigit = Long.parseLong(Long.toString(key).charAt(0) + "");
			}catch(Exception e) {
				System.out.println("Invalid key");
				return -1;
			}
			return ((int) (firstDigit % 10));
		}
		
		public String getValue(long key) {
			int indexToFind = computeIndex(key);
			return arrayOfNodes[indexToFind].getValue(key); 
		}
		
		public void addEntry(long key, String value) {
			int indexToEntry = computeIndex(key);
			arrayOfNodes[indexToEntry].add(key, value);
		}
		
		public void removeEntry(long key) {
			int indexToEntry = computeIndex(key);
			arrayOfNodes[indexToEntry].remove(key);
		}
		
		public void print() {
			for(int i = 0; i < 10; i++) {
				arrayOfNodes[i].print();
			}
		}
		
		class EntryList{
			private Entry head = null;
			
			class Entry{
				private Entry next;
				private long key;
				private String value;
				
				public Entry(long key, String value, Entry next) {
					this.key = key;
					this.value = value;
					this.next = next;
				}
			}
			
			public void add(long key, String value) {
				head = new Entry(key, value, head);
			}
			
			public void print() {
				Entry temp = head;
				while(temp != null) {
					System.out.print(temp.key + "-->");
					temp = temp.next;
				}
				System.out.print("X\n");
			}
			
			public String getValue(long key) {
				Entry temp = head;
				while(temp != null && temp.key != key) {
					temp = temp.next;
				}
				if(temp == null) return "No such key found.";
				return temp.value;
			}
			
			public boolean remove(long key) {
				Entry temp = head;
				if(temp == null) return false;
				if(head.key == key) {
					head = head.next;
					return true;
				}
				while(temp.next != null && temp.next.key != key) {
					temp = temp.next;
				}
				if(temp.next == null) return false;
				temp.next = temp.next.next;
				return true;
			}
		}
	}
}