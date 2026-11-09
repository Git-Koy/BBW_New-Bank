package ch.bbw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Einstiegspunkt der Spring-Boot-Anwendung.
 *
 * <p>
 * Startet den eingebetteten Webserver auf <code>localhost:8080</code>:
 * <pre>./mvnw spring-boot:run</pre>
 * </p>
 *
 * <p>
 * Die Annotation {@code @SpringBootApplication} aktiviert das Component-Scanning
 * ab diesem Package ({@code ch.bbw}) sowie die Autokonfiguration. Die
 * Domänenklassen ({@link Bank}, {@link AccountFactory}, …) sind bewusst
 * <em>keine</em> Spring-Beans — sie bleiben reines OO-Design.
 * </p>
 *
 * @author Luigi Cavuoti
 * @version 1.0
 */
@SpringBootApplication
public class NewBankApplication {

	/**
	 * Starts the Spring Boot application
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(NewBankApplication.class, args);
	}
}
