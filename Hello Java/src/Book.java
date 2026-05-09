
public class Book {
	boolean isBorrowed;
	
	public Book() {
		this.isBorrowed = false; 
		//처음에 return 상태라는뜻
	}
	
	public boolean isBorrowed() {
		return isBorrowed;
	}
	
	public void setBorrowed(boolean borrowed) {
		isBorrowed = borrowed;
	}
}
