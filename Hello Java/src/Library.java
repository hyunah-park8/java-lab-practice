
public class Library {
	Book[] books;
	
	Library() {
		books = new Book[10];
		for (int i = 0; i < 10; i++) {
			books[i] = new Book();
		}
	}
	
	public void borrow(int n) {
		if (n < 1 || n > 10) {
			System.out.println("Error: Invalid book number.");
			return;
		}
		
		Book targetBook = books[n - 1];
		if (targetBook.isBorrowed()) {
			System.out.println("Error: Book " + n + " is already borrowed.");
		}
		else {
			// 타겟북이 return 상태일 떼. borrowed로 바꾸고 대출했다 표시
			targetBook.setBorrowed(true);
			System.out.println("Book " + n + " borrowed.");
		}
	}
	
	public void returnBook(int n) {
		if (n < 1 || n > 10) {
			System.out.println("Error: Invalid book number.");
			return;
		}
		
		Book targetBook = books[n - 1];
		
		if (!targetBook.isBorrowed()) {
			System.out.println("Error: Book " + n + " is already returned.");
		}
		else {
			targetBook.setBorrowed(false);
			System.out.println("Book " + n + " returned.");
		}
		
	}
}
