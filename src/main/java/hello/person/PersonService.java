package hello.person;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import static org.fusesource.leveldbjni.JniDBFactory.*;

import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private int instance = 0;

    private HashMap<String, byte[]> persons;

    private MessageDigest digest;

    private DB db;

    private ObjectMapper cborMapper;

    PersonService() throws NoSuchAlgorithmException, IOException {
        this.instance = 1;
        this.persons = new HashMap<String, byte[]>(20);
        // this.digest = MessageDigest.getInstance("SHA3-256");
        this.digest = new Keccak.Digest256();

        CBORFactory f = new CBORFactory();
        this.cborMapper = new ObjectMapper(f);

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

        byte[] cborPerson = this.cborMapper.writeValueAsBytes(person);

        this.persons.put(hexHash, cborPerson);

        return hexHash;
    }

    public Person getPerson(String hash) throws JsonParseException, JsonMappingException, IOException {

        byte[] cborData = this.persons.get(hash);
        Person p = this.cborMapper.readValue(cborData, Person.class);

        System.out.println(p.name);
        return p;
    }
}
