package com.kys.sbtest;

import com.kys.sbtest.make.PhoneBook;
import com.kys.sbtest.make.PhoneBookJSONRepository;
import com.kys.sbtest.make.PhoneBookTextRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PhoneBookRepositoryTest {
    @Test
    public void jsonRepositoryTest() throws Exception {
        PhoneBookJSONRepository repository = new PhoneBookJSONRepository("test.json");
        String json = "{\"phoneNumber\":\"010-0000-0000\",\"name\":\"이말자\",\"id\":7,\"email\":\"asdfs@gm.com\",\"group\":\"Jobs\"}";
        JSONParser jsonParser = new JSONParser();
        IPhoneBook phoneBook = null;
        Object obj = jsonParser.parse(json);
        phoneBook = repository.getObjectFromJson((JSONObject)obj);
        assertThat(phoneBook.getId()).isEqualTo(7L);
        assertThat(phoneBook.getName()).isEqualTo("이말자");
        assertThat(phoneBook.getGroup()).isEqualTo(EPhoneGroup.Jobs);
        assertThat(phoneBook.getPhoneNumber()).isEqualTo("010-0000-0000");
        assertThat(phoneBook.getEmail()).isEqualTo("asdfs@gm.com");

        IPhoneBook phoneBook2 = new PhoneBook();
        phoneBook2.setId(88L);
        phoneBook2.setName("폰북");
        phoneBook2.setGroup(EPhoneGroup.Hobbies);
        phoneBook2.setPhoneNumber("1111-2222");
        phoneBook2.setEmail("abcdefg@daum.net");
        JSONObject jobject = repository.getJsonFromObject(phoneBook2);
        assertThat((Long)jobject.get("id")).isEqualTo(88L);
        assertThat((String)jobject.get("name")).isEqualTo("폰북");
        assertThat((String)jobject.get("group")).isEqualTo("Hobbies");
        assertThat((String)jobject.get("phoneNumber")).isEqualTo("1111-2222");
        assertThat((String)jobject.get("email")).isEqualTo("abcdefg@daum.net");
        assertThat(jobject.toJSONString()).isEqualTo("{\"phoneNumber\":\"1111-2222\",\"name\":\"폰북\",\"id\":88,\"email\":\"abcdefg@daum.net\",\"group\":\"Hobbies\"}");
    }

    @Test
    public void textRepositoryTest() throws Exception {
        PhoneBookTextRepository repository = new PhoneBookTextRepository("test.json");
        Throwable ex = assertThrows(Exception.class, () -> repository.getObjectFromText(""));
        System.out.println(ex.toString());

        IPhoneBook phoneBook = repository.getObjectFromText("3,홍길동,Families,010-1111-1111,abcd@gmail.com");
        assertThat(phoneBook.getId()).isEqualTo(3L);
        assertThat(phoneBook.getName()).isEqualTo("홍길동");
        assertThat(phoneBook.getGroup()).isEqualTo(EPhoneGroup.Families);
        assertThat(phoneBook.getPhoneNumber()).isEqualTo("010-1111-1111");
        assertThat(phoneBook.getEmail()).isEqualTo("abcd@gmail.com");

        String str = repository.getTextFromObject(phoneBook);
        assertThat(str).isEqualTo("3,홍길동,Families,010-1111-1111,abcd@gmail.com\n");
    }
}