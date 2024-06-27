package com.kys.sbtest.make;

import com.kys.sbtest.EPhoneGroup;
import com.kys.sbtest.IPhoneBook;
import com.kys.sbtest.IPhoneBookRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.List;

public class PhoneBookJSONRepository implements IPhoneBookRepository<IPhoneBook> {
    public String fileName;
    public PhoneBookJSONRepository(String fileName){
        this.fileName = fileName;
    }

    public IPhoneBook getObjectFromJson(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) {
            throw new Exception("Error : Input jsonObject is null");
        }
        IPhoneBook object = new PhoneBook();
        object.setId((Long) jsonObject.get("id"));
        object.setName((String) jsonObject.get("name"));
        object.setGroup(EPhoneGroup.valueOf((String) jsonObject.get("group")));
        object.setPhoneNumber((String) jsonObject.get("phoneNumber"));
        object.setEmail((String) jsonObject.get("email"));
        return object;
    }

    public JSONObject getJsonFromObject(IPhoneBook object) throws Exception {
        if ( object == null ) {
            throw new Exception("Error : Input object is null");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", object.getId());
        jsonObject.put("name", object.getName());
        jsonObject.put("group", object.getGroup().name());
        jsonObject.put("phoneNumber", object.getPhoneNumber());
        jsonObject.put("email", object.getEmail());
        return jsonObject;
    }

    @Override
    public boolean saveData(List<IPhoneBook> listData) throws Exception {
        if(listData == null || listData.size() <= 0) {
            return false;
        }
        JSONArray jsonArray = new JSONArray();
        for ( IPhoneBook object : listData ) {
            JSONObject jObject = this.getJsonFromObject(object);
            jsonArray.add(jObject);
        }
        JSONObject jroot = new JSONObject();
        jroot.put("iPhoneBooks", jsonArray);

        FileWriter fileWriter = new FileWriter(fileName, Charset.defaultCharset());
        fileWriter.write(jroot.toString());
        fileWriter.flush();
        fileWriter.close();

        return true;
    }

    @Override
    public boolean loadData(List<IPhoneBook> listData) throws Exception {
        if(listData == null){
            return false;
        }
        JSONParser parser = new JSONParser();
        File file = new File(fileName);
        if ( !file.exists() ) {
            return true;
        }
        FileReader reader = new FileReader(file, Charset.defaultCharset());
        JSONObject jsonObject = (JSONObject)parser.parse(reader);

        reader.close();

        JSONArray jsonArray = (JSONArray) jsonObject.get("iPhoneBooks");
        listData.clear();
        for ( Object obj : jsonArray ) {
            JSONObject element = (JSONObject)obj;
            listData.add(getObjectFromJson(element));
        }
        return true;
    }
}
