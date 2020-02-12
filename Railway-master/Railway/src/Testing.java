
public class Testing {
	public static void main(String[] args) throws Exception {
	
	
		
		MySQLAccess dao = new MySQLAccess();
		dao.readDataBase();

		dao.displayTicket(12);
		
}
}