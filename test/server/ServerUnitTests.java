package server;

import java.io.File;
import org.junit.* ;
import static org.junit.Assert.* ;
import server.database.Database;

public class ServerUnitTests {
    
    public static String TEST_DATABASE_PATH = "database" + File.separator + "test" + File.separator
                + "test-record-indexer.sqlite";
    
	@Before
	public void setup() {
	}
	
	@After
	public void teardown() {
	}
	
	@Test
	public void test_1() {
		assertEquals("OK", "OK");
		assertTrue(true);
		assertFalse(false);
	}

	public static void main(String[] args) throws Database.DatabaseException {

            Database.initialize();
		String[] testClasses = new String[] {
				"server.database.FieldsTest",
                                "server.database.ImagesTest",
                                "server.database.ProjectsTest",
                                "server.database.RecordsTest",
                                "server.database.UsersTest"
		};

		org.junit.runner.JUnitCore.main(testClasses);
	}
	
}

