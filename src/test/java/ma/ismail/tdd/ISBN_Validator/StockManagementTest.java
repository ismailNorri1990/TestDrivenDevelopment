package ma.ismail.tdd.ISBN_Validator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class StockManagementTest {

	@Test
	public void testCanGetACorrectLocatorCode() {

		ExternaISBNDataService testWebService = new ExternaISBNDataService() {
public Book lookup(String isbn) {
				return new Book(isbn, "Of Mice And Men", "J. Stenbeck");
			}
		};

		ExternaISBNDataService testDatabaseService = new ExternaISBNDataService() {
			public Book lookup(String isbn) {
				return null;
			}
		};

		StockManager stockManager = new StockManager();
		stockManager.setWebService(testWebService);
		stockManager.setDataBaseService(testDatabaseService);

		String isbn = "0140177396";
		String locatorCode = stockManager.getLocatorCode(isbn);
		assertEquals("7396J4", locatorCode);
	}

	
	@Test
	public void dataBaseIsUsedIfDataIsPresent() {
		ExternaISBNDataService databaseService = mock(ExternaISBNDataService.class);
		ExternaISBNDataService webService = mock(ExternaISBNDataService.class);
		
		when(databaseService.lookup("0140177396")).thenReturn(new Book("0140177396","abc","abc"));
		
		StockManager stockManager = new StockManager();
		stockManager.setWebService(webService);
		stockManager.setDataBaseService(databaseService);

		String isbn = "0140177396";
		String locatorCode = stockManager.getLocatorCode(isbn);
		verify(databaseService).lookup("0140177396");
		verify(databaseService, never()).lookup("anyString");
	}
	
	@Test
	public void webServiceIsUsedIfDataIsNotPresentInDatabase() {
		ExternaISBNDataService databaseService = mock(ExternaISBNDataService.class);
		ExternaISBNDataService webService = mock(ExternaISBNDataService.class);
		
		when(databaseService.lookup("0140177396")).thenReturn(null);
		when(webService.lookup("0140177396")).thenReturn(new Book("0140177396","abc","abc"));
		
		StockManager stockManager = new StockManager();
		stockManager.setWebService(webService);
		stockManager.setDataBaseService(databaseService);

		String isbn = "0140177396";
		String locatorCode = stockManager.getLocatorCode(isbn);
		verify(databaseService).lookup("0140177396");
		verify(webService).lookup("0140177396");
	}
}
