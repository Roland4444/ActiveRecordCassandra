package roland.activerecord;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class ActiveRecordApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActiveRecordApplication.class, args);
	}

}
