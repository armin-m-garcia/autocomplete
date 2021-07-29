package loop.baby.autocomplete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutocompleteApplication {

	static Logger logger = LoggerFactory.getLogger(AutocompleteApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AutocompleteApplication.class, args);
	}

}
