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
        PhoneBookJSONRepository repository = new PhoneBookJSONRepository("test111.json");
        String json = "{\"phoneNumber\":\"010-1234-4321\",\"name\":\"이말년\",\"id\":55,\"email\":\"chim@gg.com\",\"group\":\"Families\"}";
        JSONParser jsonParser = new JSONParser();
        IPhoneBook phoneBook = null;
        Object obj = jsonParser.parse(json);
        phoneBook = repository.getObjectFromJson((JSONObject)obj);
        assertThat(phoneBook.getId()).isEqualTo(55L);
        assertThat(phoneBook.getName()).isEqualTo("이말년");
        assertThat(phoneBook.getGroup()).isEqualTo(EPhoneGroup.Families);
        assertThat(phoneBook.getPhoneNumber()).isEqualTo("010-1234-4321");
        assertThat(phoneBook.getEmail()).isEqualTo("chim@gg.com");

        IPhoneBook phoneBook2 = new PhoneBook();
        phoneBook2.setId(null);
        phoneBook2.setName("북북북");
        phoneBook2.setGroup(EPhoneGroup.Schools);
        phoneBook2.setPhoneNumber("0101010-0101010");
        phoneBook2.setEmail("qwerasdf@grgr.net");
        JSONObject jobject = repository.getJsonFromObject(phoneBook2);
        assertThat((Long)jobject.get("id")).isEqualTo(null);
        assertThat((String)jobject.get("name")).isEqualTo("북북북");
        assertThat((String)jobject.get("group")).isEqualTo("Schools");
        assertThat((String)jobject.get("phoneNumber")).isEqualTo("0101010-0101010");
        assertThat((String)jobject.get("email")).isEqualTo("qwerasdf@grgr.net");
        assertThat(jobject.toJSONString()).isEqualTo(
                "{\"phoneNumber\":\"0101010-0101010\",\"name\":\"북북북\",\"id\":null,\"email\":\"qwerasdf@grgr.net\",\"group\":\"Schools\"}");
    }

    @Test
    public void textRepositoryTest() throws Exception {
        PhoneBookTextRepository repository = new PhoneBookTextRepository("test222.json");
        Throwable ex = assertThrows(Exception.class, () -> repository.getObjectFromText(""));
        System.out.println(ex.toString());

        IPhoneBook phoneBook = repository.getObjectFromText("123,길길동,Friends,010-1111-1111,1234567@com.com");
        assertThat(phoneBook.getId()).isEqualTo(123L);
        assertThat(phoneBook.getName()).isEqualTo("길길동");
        assertThat(phoneBook.getGroup()).isEqualTo(EPhoneGroup.Friends);
        assertThat(phoneBook.getPhoneNumber()).isEqualTo("010-1111-1111");
        assertThat(phoneBook.getEmail()).isEqualTo("1234567@com.com");

        String str = repository.getTextFromObject(phoneBook);
        assertThat(str).isEqualTo("123,길길동,Friends,010-1111-1111,1234567@com.com\n");
    }
}