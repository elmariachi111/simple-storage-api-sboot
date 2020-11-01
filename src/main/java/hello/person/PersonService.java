package hello.person;

import static org.fusesource.leveldbjni.JniDBFactory.factory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private MessageDigest digest;

    private DB db;

    private ObjectMapper cborMapper;

    PersonService() throws NoSuchAlgorithmException, IOException {
        this.digest = new Keccak.Digest256();

        CBORFactory f = new CBORFactory();
        this.cborMapper = new ObjectMapper(f);

        Options options = new Options();
        options.createIfMissing(true);
        this.db = factory.open(new File(".leveldb"), options);
    }

    public String[] all() {
        DBIterator iterator = this.db.iterator();
        ArrayList<String> res = new ArrayList<String>();

        for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
            byte[] key = iterator.peekNext().getKey();

            res.add(new String(Hex.encode(key)));
        }

        return res.toArray(new String[0]);
    }

    public String addPerson(Person person) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonPerson = mapper.writeValueAsString(person);
        byte[] hash = this.digest.digest(jsonPerson.getBytes(StandardCharsets.UTF_8));
        String hexHash = new String(Hex.encode(hash));

        byte[] cborPerson = this.cborMapper.writeValueAsBytes(person);
        this.db.put(hash, cborPerson);

        return hexHash;
    }

    public Person getPerson(String hash) throws JsonParseException, JsonMappingException, IOException {
        byte[] key = Hex.decode(hash);
        byte[] cborData = this.db.get(key);
        Person p = this.cborMapper.readValue(cborData, Person.class);
        return p;
    }
}
