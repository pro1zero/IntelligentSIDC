package assignment3;
import java.util.*;
public class assign3 {
//55555555
//44444444
//77777777
//88888888
//99999999
//11111111
//22222222
//33333333
//66666666
//!
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		IntelligentSIDC sidc = new IntelligentSIDC();
		sidc.setSIDCThreshold(10);
		while(scan.hasNextLong()) {
			long currentKey = scan.nextLong();
			sidc.add(currentKey, "FL-DOB-" + currentKey);
		}
		System.out.println(sidc.allKeys());
		
		System.out.println(sidc.rangeKey(11111111, 88888888));
		scan.close();
	}
}
class IntelligentSIDC{
	static int currentSizeOfSystem;
	boolean useTrees;
	DoublyLL dll;
	long currentKey;
	LessThanThousand ltt;
	
	public IntelligentSIDC() {
		currentSizeOfSystem = 0;
		useTrees = false;
		dll = new DoublyLL();
		currentKey = -1;
		ltt = new LessThanThousand();
	}

	public String allKeys() {
		dll.print();
		return dll.loopOverKeys(currentSizeOfSystem);
	}
	
	public void setSIDCThreshold(int size) {
		if(size <= 1000) {
			useTrees = false;
//			currentSizeOfSystem = size;
		}
	}
	
	public void add(long key, String value) {
//		long newKey = generate();
		dll.add(key);
		ltt.addEntry(key, "FL-DOB-" + key);
		currentSizeOfSystem += 1;
	}
	
	public void remove(long key) {
		boolean contains = dll.containsNodeWithKey(key);
		if(!contains) return;
		dll.remove(key);
		ltt.removeEntry(key);
		currentSizeOfSystem -= 1;
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
			
			public Node(long key, Node next) {
				this.key = key;
				this.next = next;
			}
			
			public Node(long key) {
				this.key = key;
			}
		}
		
		public long nextKey(long key) {
			Node temp = head;
			if(temp.key == key) return -1;
			while(temp.next != null) {
				if(temp.next.key == key) {
					return temp.key;
				}
				temp = temp.next;
			}
			return -1;
		}
		
		public long prevKey(long key) {
			Node temp = head;
			while(temp != null) {
				if(temp.key == key && temp.next != null) {
					return temp.next.key;
				}
				temp = temp.next;
			}
			return -1;
		}
		
		public int rangeKey(long key1, long  key2) {
			Node temp = head;
			int keysInBetween = 0;
			while(temp != null) {
				if(temp.key > key1 && temp.key < key2) {
					keysInBetween += 1;
				}
				temp = temp.next;
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
			head = new Node(key, head);
		}
		
		public void remove(long key) {
			Node temp = head;
			if(temp.key == key) {
				temp = temp.next;
				return;
			}
			while(temp.next != null && temp.next.key != key) {
				temp = temp.next;
			}
			temp.next = temp.next.next;
		}
		
		public String loopOverKeys(int size) {
			if(size <= 0) {
				return "[]";
			}
			long[] allKeys = new long[size];
			Node temp = head;
			int i = 0;
			while(temp != null) {
				allKeys[i++] = temp.key;
				temp = temp.next;
			}
			return heapSort(allKeys);
		}
		
		public String heapSort(long[] nums) {
			buildMaxHeap(nums);
			for(int i = nums.length - 1; i > 0; i--) {
				swap(0, i, nums);
				siftDown(0, i - 1, nums);
			}
			return Arrays.toString(nums);
		}
		
		public void buildMaxHeap(long[] nums) {
			int firstParentIndex = (nums.length - 2) / 2;
			for(int i = firstParentIndex; i >= 0; i--) {
				siftDown(i, nums.length - 1, nums);
			}
		}
		
		public void siftDown(long currentIndex, long endIndex, long[] nums) {
			long childOneIndex = currentIndex * 2 + 1;
			while(childOneIndex <= endIndex) {
				long childTwoIndex = currentIndex * 2 + 2 <= endIndex ? currentIndex * 2 + 2 : -1;
				long indexToSwap;
				if(childTwoIndex != -1 && nums[(int) childTwoIndex] > nums[(int) childOneIndex]) {
					indexToSwap = childTwoIndex;
				}
				else {
					indexToSwap = childOneIndex;
				}
				if(nums[(int) indexToSwap] > nums[(int) currentIndex]) {
					swap(currentIndex, indexToSwap, nums);
					currentIndex = indexToSwap;
					childOneIndex = currentIndex * 2 + 1;
				}
				else {
					return;
				}
			}
		}
		
		public void swap(long currentIndex, long indexToSwap, long[] nums) {
			long temp = nums[(int) currentIndex];
			nums[(int) currentIndex] = nums[(int) indexToSwap];
			nums[(int) indexToSwap] = temp;
		}
		
		public void print() {
			Node temp = head;
			while(temp != null) {
				System.out.print(temp.key + "-->");
				temp = temp.next;
			}
			System.out.print("X\n");
//			ltt.print();
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