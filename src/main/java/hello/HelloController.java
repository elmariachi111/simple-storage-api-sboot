package hello;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.joda.time.LocalTime;

/*public class HelloWorld {
	public static void main(String[] args) {
		LocalTime currentTime = new LocalTime();
		System.out.println("Hello world" + currentTime);	
	}
}*/

@RestController
@RequestMapping("/person")
public class HelloController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@PostMapping(value = "/")
	public void addPerson() {

	}
}
