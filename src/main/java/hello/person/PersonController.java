package hello.person;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
	public Person index() {
		Person p = new Person();
		p.age = 42;
		p.name = "foo";
		p.role = Role.GREENHORN;
		return p;
	}

	@GetMapping("/{personHash}")
	public Person getPerson(@PathVariable("personHash") String personHash) {

		Person p = this.personService.getPerson(personHash);
		return p;
	}

	@PostMapping(value = "/")
	public ResponseTransfer addPerson(@RequestBody Person person) {
		String hash;
		try {
			hash = this.personService.addPerson(person);
			ResponseTransfer resp = new ResponseTransfer(hash);
			return resp;
		} catch (JsonProcessingException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "when storing person", e);
		}
	}
}
