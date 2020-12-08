package assignment3;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	public static void main(String[] args) throws IOException {
		IntelligentSIDC sidc = new IntelligentSIDC();
		sidc.setSIDCThreshold(0);
		File file = new File("C:\\Users\\jenis\\Desktop\\Fall 2020\\Problem Solving\\input3.txt"); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String st; 
		while ((st = br.readLine()) != null) {
			long currentKey = Long.parseLong(st);
			sidc.add(currentKey, "FL-DOB-" + currentKey);
		}
		System.out.println(sidc.allKeys());
		sidc.remove(99999999);
		sidc.remove(11111111);
		sidc.remove(22222222);
		sidc.remove(44444444);
		sidc.remove(33333333);
		System.out.println(sidc.allKeys());
		System.out.println(sidc.nextKey(55555555));
		sidc.add(99999999, ("FL-DOB-" + 99999999));
		System.out.println(sidc.allKeys());
		sidc.remove(99999999);
		System.out.println(sidc.allKeys());
		sidc.add(55555555, ("FL-DOB-" + 99999999));
		System.out.println(sidc.allKeys());
		sidc.remove(99999999);
		System.out.println(sidc.allKeys());
		br.close();
	}
}

class IntelligentSIDC{
	static int currentSizeOfSystem;
	boolean useTrees;
	DoublyLL dll;
	long currentKey;
	LessThanThousand ltt;
	MoreThanThousand mtt;
	KeepTrack kt;
	boolean justOnce;
	
	public IntelligentSIDC() {
		currentSizeOfSystem = 0;
		useTrees = false;
		dll = new DoublyLL();
		currentKey = -1;
		ltt = new LessThanThousand();
		mtt = new MoreThanThousand();
		kt = new KeepTrack();
		justOnce = true;
	}
	
	public void upConversion() {
		assignment3.IntelligentSIDC.DoublyLL.Node temp = dll.head;
		while(temp != null) {
			mtt.add(temp.key, "FL-DOB" + temp.key);
			temp = temp.next;
		}
		ltt.reset();
		
	}
	
	public void downConversion(long key) {
		assignment3.IntelligentSIDC.DoublyLL.Node temp = dll.head;
		while(temp != null) {
			ltt.addEntry(temp.key, ("FL-DOB" + temp.key));
			temp = temp.next;
		}
		mtt.reset();
	}

	public String allKeys() {
		System.out.println("Size: " + currentSizeOfSystem + " UseTrees: " + useTrees);
		if(useTrees) {
			System.out.println("Sorted using AVL");
			return mtt.sort();
		}
		System.out.println("Sorted using Heap Sort");
		return dll.loopOverKeys(currentSizeOfSystem);
	}
	
	public void setSIDCThreshold(int size) {
		currentSizeOfSystem = size;
		if(size >= 5) {
			useTrees = true;
		}
	}
	
	public void add(long key, String value) {
//		long newKey = generate();
		boolean isInserted = false;
		if(kt.find(key) == null) {
			kt.insert(key);
			dll.add(key);
			isInserted = true;
		}
		if(useTrees) 
			mtt.add(key, value);
		else
			ltt.addEntry(key, value);
		if(isInserted) currentSizeOfSystem += 1;
		if(currentSizeOfSystem >= 5 && justOnce) {
			upConversion();
			justOnce = false;
			useTrees = true;
			
		}
	}
	
	public void remove(long key) {
		boolean contains = kt.find(key) != null ? true : false;
		if(!contains) return;
		dll.remove(key);	
		kt.delete(key);
		if(useTrees)
			mtt.remove(key);
		else
			ltt.removeEntry(key);
		currentSizeOfSystem -= 1;
		if(currentSizeOfSystem < 5 && !justOnce) {
			ltt.removeEntry(key);
			downConversion(key);
			justOnce = true;
			useTrees = false;
		}
	}
	
	public int rangeKey(long key1, long key2) {
		return dll.rangeKey(key1, key2);
	}
	
	public long generate() {
		generateHelper();
		return currentKey;
	}
	
	public String getValues(long key) {
		return mtt.getValue(key);
//		return ltt.getValue(key);
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
	
	class MoreThanThousand{
		
		AVLTree tree;
		
		public MoreThanThousand() {
			tree = new AVLTree();
		}
		
		public void add(long key, String value) {
			tree.insert(key, value);
		}
		
		public void remove(long key) {
			tree.delete(key);
		}
		
		public String sort() {
			return tree.inOrderTraversal();
		}
		
		public String getValue(long key) {
			String fetchedValue = tree.find(key);
			if(fetchedValue.equals("-1")) {
				return "No such key in the system.";
			}
			return fetchedValue;
		}
		
		public void reset() {
			tree.root = null;
		}
		
		 class AVLTree {
			 
			 Node root;
			 String sortedArray = "";
			 
		     class Node {
		        long key;
		        int height;
		        Node left;
		        Node right;
		        String value;
		        
		        Node(long key) {
		            this.key = key;
		        }
		        
		        public Node(long key, String value) {
		        	this.key = key;
		        	this.value = value;
		        }
		    }

		    public String find(long key) {
		        Node current = root;
		        while (current != null) {
		            if (current.key == key) {
		               return current.value;
		            }
		            current = current.key < key ? current.right : current.left;
		        }
		        return "-1";
		    }

		    public void insert(long key, String value) {
		        root = insert(root, key, value);
		    }

		    public void delete(long key) {
		        root = delete(root, key);
		    }

		    public Node getRoot() {
		        return root;
		    }

		    public int height() {
		        return root == null ? -1 : root.height;
		    }

		    private Node insert(Node node, long key, String value) {
		        if (node == null) {
		            return new Node(key, value);
		        } else if (node.key > key) {
		            node.left = insert(node.left, key, value);
		        } else if (node.key < key) {
		            node.right = insert(node.right, key, value);
		        }
		        return rebalance(node);
		    }

		    private Node delete(Node node, long key) {
		        if (node == null) {
		            return node;
		        } else if (node.key > key) {
		            node.left = delete(node.left, key);
		        } else if (node.key < key) {
		            node.right = delete(node.right, key);
		        } else {
		            if (node.left == null || node.right == null) {
		                node = (node.left == null) ? node.right : node.left;
		            } else {
		                Node mostLeftChild = mostLeftChild(node.right);
		                node.key = mostLeftChild.key;
		                node.right = delete(node.right, node.key);
		            }
		        }
		        if (node != null) {
		            node = rebalance(node);
		        }
		        return node;
		    }

		    private Node mostLeftChild(Node node) {
		        Node current = node;
		        while (current.left != null) {
		            current = current.left;
		        }
		        return current;
		    }

		    private Node rebalance(Node z) {
		        updateHeight(z);
		        int balance = getBalance(z);
		        if (balance > 1) {
		            if (height(z.right.right) > height(z.right.left)) {
		                z = rotateLeft(z);
		            } else {
		                z.right = rotateRight(z.right);
		                z = rotateLeft(z);
		            }
		        } else if (balance < -1) {
		            if (height(z.left.left) > height(z.left.right)) {
		                z = rotateRight(z);
		            } else {
		                z.left = rotateLeft(z.left);
		                z = rotateRight(z);
		            }
		        }
		        return z;
		    }

		    private Node rotateRight(Node y) {
		        Node x = y.left;
		        Node z = x.right;
		        x.right = y;
		        y.left = z;
		        updateHeight(y);
		        updateHeight(x);
		        return x;
		    }

		    private Node rotateLeft(Node y) {
		        Node x = y.right;
		        Node z = x.left;
		        x.left = y;
		        y.right = z;
		        updateHeight(y);
		        updateHeight(x);
		        return x;
		    }

		    private void updateHeight(Node n) {
		        n.height = 1 + Math.max(height(n.left), height(n.right));
		    }

		    private int height(Node n) {
		        return n == null ? -1 : n.height;
		    }

		    public int getBalance(Node n) {
		        return (n == null) ? 0 : height(n.right) - height(n.left);
		    }
		    
		    public String inOrderTraversal() {
		    	sortedArray = "[";
		    	inOrderTraversalRecursion(root);
		    	return sortedArray + "]";
		    }
		    
		    public void inOrderTraversalRecursion(Node root) {
		    	if(root == null) return;
		    	inOrderTraversalRecursion(root.left);
		    	System.out.println(root.key + ",");
//		    	sortedArray = (sortedArray.equals("[")) ? sortedArray += root.key : sortedArray + ", " + root.key;
		    	inOrderTraversalRecursion(root.right);
		    }
		}
	}
	
	class KeepTrack {
	    class Node {
	        long key;
	        int height;
	        Node left;
	        Node right;

	        Node(long key) {
	            this.key = key;
	        }
	    }

	    Node root;

	    public Node find(long key) {
	        Node current = root;
	        while (current != null) {
	            if (current.key == key) {
	               break;
	            }
	            current = current.key < key ? current.right : current.left;
	        }
	        return current;
	    }

	    public void insert(long key) {
	        root = insert(root, key);
	    }

	    public void delete(long key) {
	        root = delete(root, key);
	    }

	    public Node getRoot() {
	        return root;
	    }

	    public int height() {
	        return root == null ? -1 : root.height;
	    }

	    private Node insert(Node node, long key) {
	        if (node == null) {
	            return new Node(key);
	        } else if (node.key > key) {
	            node.left = insert(node.left, key);
	        } else if (node.key < key) {
	            node.right = insert(node.right, key);
	        }
	        return node;
	    }

	    private Node delete(Node node, long key) {
	        if (node == null) {
	            return node;
	        } else if (node.key > key) {
	            node.left = delete(node.left, key);
	        } else if (node.key < key) {
	            node.right = delete(node.right, key);
	        } else {
	            if (node.left == null || node.right == null) {
	                node = (node.left == null) ? node.right : node.left;
	            } else {
	                Node mostLeftChild = mostLeftChild(node.right);
	                node.key = mostLeftChild.key;
	                node.right = delete(node.right, node.key);
	            }
	        }
	        return node;
	    }

	    private Node mostLeftChild(Node node) {
	        Node current = node;
	        while (current.left != null) {
	            current = current.left;
	        }
	        return current;
	    }
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
			if(head.key == key) {
				head = head.next;
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
				System.out.println(temp.key);
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
		
		public void reset() {
			arrayOfNodes = new EntryList[10];
			for(int i = 0; i < 10; i++) {
				arrayOfNodes[i] = new EntryList();
			}
		}
		
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
				if(temp == null)
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