package hello.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*public class HelloWorld {
	public static void main(String[] args) {
		LocalTime currentTime = new LocalTime();
		System.out.println("Hello world" + currentTime);	
	}
}*/

@RestController()
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonService personService;

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot! Person service instance: " + this.personService.getServiceValue();
	}

	@PostMapping(value = "/")
	public void addPerson() {

	}
}
