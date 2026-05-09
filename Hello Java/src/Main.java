
public class Main {
	public static void main(String[] args) {
		Library lib = new Library();
		
		lib.borrow(3);
		lib.borrow(3);
		lib.returnBook(3);
		lib.returnBook(3);
		lib.borrow(11);
	}
}
