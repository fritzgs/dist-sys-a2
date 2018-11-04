package a2_fritz_20071968;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class SQLTest {
	
	SQLHandler sql;

	void setUp()
	{
		sql = new SQLHandler();
	}
	
	@Test
	void test() {
		setUp();
		try {
			Boolean exist = sql.studentNoExist(sql.getConnection(), "12345", "pass");
			System.out.println(exist);
			assertTrue(exist);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
