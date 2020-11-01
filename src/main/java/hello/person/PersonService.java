package hello.person;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import static org.fusesource.leveldbjni.JniDBFactory.*;

import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private int instance = 0;

    private HashMap<String, Person> persons;

    private MessageDigest digest;

    private DB db;

    PersonService() throws NoSuchAlgorithmException, IOException {
        this.instance = 1;
        this.persons = new HashMap<String, Person>(20);
        // this.digest = MessageDigest.getInstance("SHA3-256");
        this.digest = new Keccak.Digest256();
        Options options = new Options();
        options.createIfMissing(true);
        this.db = factory.open(new File(".leveldb"), options);
    }

    public String getServiceValue() {
        this.instance++;
        return "service value " + this.instance;
    }

    public String addPerson(Person person) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonPerson = mapper.writeValueAsString(person);

        byte[] hash = this.digest.digest(jsonPerson.getBytes(StandardCharsets.UTF_8));
        String hexHash = new String(Hex.encode(hash));
        this.persons.put(hexHash, person);

        return hexHash;
    }

    public Person getPerson(String hash) {

        byte[] key = Hex.decode(hash);
        System.out.println(new String(key));

        Person p = this.persons.get(hash);
        System.out.println(p.name);
        return p;
    }
}
