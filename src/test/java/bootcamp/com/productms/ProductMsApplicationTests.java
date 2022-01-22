package bootcamp.com.productms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductMsApplicationTests {

	@Test
	void contextLoads() {
		Assertions.assertNotNull(ProductMsApplication.class);
	}

}
