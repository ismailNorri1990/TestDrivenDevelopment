package ma.ismail.tdd.ISBN_Validator;

public class StockManager {

	ExternaISBNDataService webService;
	ExternaISBNDataService dataBaseService;

	public void setWebService(ExternaISBNDataService service) {
		this.webService = service;
	}

	public void setDataBaseService(ExternaISBNDataService dataBaseService) {
		this.dataBaseService = dataBaseService;
	}

	public String getLocatorCode(String isbn) {
		Book book = dataBaseService.lookup(isbn);
		if(book == null) book =  webService.lookup(isbn);
		StringBuilder locator = new StringBuilder();
		locator.append(isbn.substring(isbn.length() - 4));
		locator.append(book.getAuthor().substring(0, 1));
		locator.append(book.getTitle().split(" ").length);
		return locator.toString();
	}

}
