import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateRedirect {

  // Firstly run CreateTSV.sh
	public static void main(String[] args) {
		String folderPath = "/home/valdestilhas/Downloads/SameAsPaperDimitris/DataBaseDimitris/testScriptSebastian/";
		Path path = Paths.get(folderPath, "ld.tsv"); // or any text file
														// eg.: txt,
														// bat, etc
		Charset charset = Charset.forName("UTF-8");
		String line = null;
		String dbPediaURI = null;
		String realURI = null;
		String linkTarget = null;

		Connection con = null;

		long startTime = System.currentTimeMillis();

		try {
			System.out.println("Starting generation of table REDIRECT...");
			final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
			final String DB_URL = "jdbc:mysql://localhost/dbSameAs";
			String user = "root";
			String password = "aux123";

			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, user, password);

			PreparedStatement prep = null;

			BufferedReader reader = Files.newBufferedReader(path, charset);
			while ((line = reader.readLine()) != null) {
				// separate all tsv fields into string array
				String[] lineVariables = line.split("\t");
				if (lineVariables[0].startsWith("http://dbpedia.org")) {
					dbPediaURI = lineVariables[0];
					linkTarget = lineVariables[1];
				} else {
					dbPediaURI = lineVariables[1];
					linkTarget = lineVariables[0];
				}

				realURI = getRealURI(dbPediaURI);

				prep = con
						.prepareStatement("INSERT INTO REDIRECT (from_uri, to_uri) VALUES (?, ?);");
				prep.setString(1, linkTarget);
				prep.setString(2, realURI);
				try {
					prep.executeUpdate();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}

		} catch (Exception e) {
			System.err.println(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("Generation of table REDIRECT finished !");
		System.out.println("Total time [seconds]: "
				+ (System.currentTimeMillis() - startTime) * 0.001);

	}

	private static String getRealURI(String pURI) {
		String ret = pURI;
		try {
			URL url = new URL(pURI);

			InputStream is = url.openConnection().getInputStream();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));

			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.indexOf("rel=\"foaf:primarytopic") > 0) {
					int ind = line.indexOf("href");
					ret = line.substring(ind + 6, line.length() - 3);
					break;
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
