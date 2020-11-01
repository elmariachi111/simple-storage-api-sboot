package hello.person;

import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private int instance = 0;

    PersonService() {
        this.instance = 1;
    }

    public String getServiceValue() {
        this.instance++;
        return "service value " + this.instance;
    }
}
