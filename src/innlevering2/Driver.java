package innlevering2;

import java.awt.List;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.text.*;

public class Driver {
	
	static java.util.Date utilDate = new java.util.Date();
	static java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

	public static void addBruker(String navn, String telefonnr, int vekt, int fødselsår) throws SQLException {
		String addBruker = "INSERT INTO BRUKER (brukerID, navn, telefonnr, vekt, fødselsår) VALUES (default, ?, ?, ?, ?)";

		Connection myConnection = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/anderoha_database", "anderoha", "");
		PreparedStatement Statement = myConnection.prepareStatement(addBruker);

		Statement.setString(1, navn);
		Statement.setString(2, telefonnr);
		Statement.setInt(3, vekt);
		Statement.setInt(4, fødselsår);
		Statement.executeUpdate();
	}

	public static void addØkt(int brukerID, Date dato, int varighet, int form, int prestasjon, String notat, String lokasjon) throws SQLException {
		String addØkt = "INSERT INTO ØKT (øktID, brukerID, dato, varighet, form, prestasjon, notat, lokasjon) VALUES (default,?,?,?,?,?,?,?,?)";

		Connection myConnection = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/anderoha_database", "anderoha", "");
		PreparedStatement Statement = myConnection.prepareStatement(addØkt);

		Statement.setInt(1, brukerID);
		Statement.setDate(2, dato);
		Statement.setInt(3, varighet);
		Statement.setInt(4, form);
		Statement.setInt(5, prestasjon);
		Statement.setString(6, notat);
		Statement.setString(7, lokasjon);
		Statement.executeUpdate();
	}

	public static void addMål(int brukerID, String øvelsesNavn, Date dato) throws SQLException {
		String addMål = "INSERT INTO MÅL (målID, brukerID, øvelsesNavn, dato) VALUES (default, ?, ?, ?)";

		Connection myConnection = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/anderoha_database", "anderoha", "");
		PreparedStatement Statement = myConnection.prepareStatement(addMål);

		Statement.setInt(1, brukerID);
		Statement.setString(2, øvelsesNavn);
		Statement.setDate(3, dato);
		Statement.executeUpdate();
	}

	public static void addMal(String malNavn, int brukerID) throws SQLException {
		String addMal = "INSERT INTO MAL (malID, malNavn, brukerID) VALUES (default, ?, ?)";

		Connection myConnection = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/anderoha_database", "anderoha", "");
		PreparedStatement Statement = myConnection.prepareStatement(addMal);

		Statement.setString(1, malNavn);
		Statement.setInt(2, brukerID);
		Statement.executeUpdate();
	}

	public static void addResultat(int brukerID, String øvelsesNavn, int vekt, int repetisjoner, Date dato) throws SQLException {
		String addResultat = "INSERT INTO RESULTAT (resultatID, brukerID, øvelsesNavn, vekt, repetisjoner, dato) VALUES (default, ?, ?, ?, ?, ?)";

		Connection myConnection = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/anderoha_database", "anderoha", "");
		PreparedStatement Statement = myConnection.prepareStatement(addResultat);

		Statement.setInt(1, brukerID);
		Statement.setString(2, øvelsesNavn);
		Statement.setInt(3, vekt);
		Statement.setInt(4, repetisjoner);
		Statement.setDate(5, sqlDate);
		Statement.executeUpdate();
	}

	public static void main(String[] args) throws Exception {
		String navnValg = null;
		int Valg = 0;
		int brukerID = 0;

		Connection myConnection = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/anderoha_database", "anderoha", "");
		Statement myStatement = myConnection.createStatement();

		Scanner Scanner = new Scanner(System.in);

		ResultSet Name = myStatement.executeQuery("SELECT navn FROM BRUKER");
		ArrayList<String> Navn = new ArrayList<String>();
		while (Name.next()) {
			Navn.add(Name.getString(1));
		}
		System.out.println("Hei og velkommen! Skriv inn ditt navn: ");
		navnValg = Scanner.nextLine();

		while (!(Navn.contains(navnValg))) {
			System.out.println("Denne brukeren eksisterer ikke. Vil du: [1] Prøve igjen? [2] Registrere ny bruker?");
			Valg = Scanner.nextInt();
			if (Valg == 2) {
				Scanner.nextLine();
				System.out.println("Navn: ");
				String navn = Scanner.nextLine();
				System.out.println("Telefonnummer: ");
				String telefonnr = Scanner.nextLine();
				System.out.println("Vekt [i kg]: ");
				int vekt = Scanner.nextInt();
				System.out.println("Fødselsår: ");
				int fødselsår = Scanner.nextInt();
				Driver.addBruker(navn, telefonnr, vekt, fødselsår);
				break;
			} else if (Valg == 1) {
				Scanner.nextLine();
				System.out.println("Skriv inn ditt navn: ");
				navnValg = Scanner.nextLine();
			}else{
				throw new Exception("Ikke gyldig valg [1 eller 2]. Begynn på nytt!");
			}
		}
		ResultSet Login = myStatement.executeQuery("SELECT brukerID FROM BRUKER WHERE navn ='" + navnValg + "'");

		while (Login.next()) {
			String str = Login.getString(1);
			brukerID = Integer.parseInt(str);
		}

		System.out.println("[1] Legg til økt \n[2] Legg til resultat \n[3] Vis beste resultater \n" +
				"[4] Lag mal \n[5] Velg mal");
		Valg = Scanner.nextInt();

		switch (Valg) {

		case 1: 

		case 2: // Legg til resultater
			Scanner.nextLine();	
			System.out.println("Navn på øvelse: ");
			String øvelsesNavn = Scanner.nextLine();
			System.out.println("Vekt brukt på øvelse: ");
			int vekt = Scanner.nextInt();
			System.out.println("Antall repetisjoner på øvelse: ");
			int repetisjoner = Scanner.nextInt();
			addResultat(brukerID, øvelsesNavn, vekt, repetisjoner, sqlDate);
			Scanner.nextLine();
			break;

		case 3: // Vis beste resultater på en øvelse (styrke)
			Scanner.nextLine();
			System.out.println("Velg en øvelse: ");
			String ØvelsesNavn = Scanner.nextLine();

			PreparedStatement PrepStatement = myConnection.prepareStatement("SELECT øvelsesNavn, vekt, repetisjoner, dato FROM RESULTAT WHERE øvelsesNavn = ?"
					+ "AND brukerID = ? ORDER BY vekt DESC");
			PrepStatement.setString(1, ØvelsesNavn);
			PrepStatement.setInt(2, brukerID);
			ResultSet resultatSet = PrepStatement.executeQuery();

			ResultSetMetaData Resultatmetadata = PrepStatement.getMetaData();
			int numberOfColumns = Resultatmetadata.getColumnCount();

			ArrayList<String[]> resultater = new ArrayList<>();
			while( resultatSet.next()) {
				String[] row = new String[numberOfColumns];
				for( int iCol = 1; iCol <= numberOfColumns; iCol++ ){
					row[iCol-1] = resultatSet.getObject( iCol ).toString();
				}
				resultater.add( row );
			}
			for( String[] row: resultater ){
				for( String s: row ){
					System.out.print( " " + s );
				}
				System.out.println();
			}
			break;
		}
	}
}
