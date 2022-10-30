import Util.ConverterFormat;
import WorkWithDocument.ParamDocument;
import WorkWithDocument.SubstituteInTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Runner {

    public static void main(String[] args) {

        Map<String,String> rez = new HashMap<>();
      //  System.out.println(ComputeJson("D:\\v8_9uaLVd_106.txt"));

        for (int i = 0; i < args.length; i++) {
            try {
                rez.put("Rez"+ String.valueOf(i) ,ComputeJson(args[i]));

            } catch (Exception e) {
                Class<? extends Exception> cls = e.getClass();
                System.out.println(cls.getCanonicalName());
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(rez);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static String ComputeJson(String jsonWay){
        String json = "";
        try (FileInputStream fis = new FileInputStream(jsonWay);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)
        ) {
            String str = null;
            while ((str = reader.readLine()) != null) {
                json += str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        json = json.replace("\uFEFF", "");

        Map map = JsonToMap(json);
        String action = (String) map.get("Действие");
        if (action == null || action.equals("word")) {
            SubstituteInTemplate substituteInTemplate = new SubstituteInTemplate();
            ParamDocument param = new ParamDocument(map);
            return substituteInTemplate.SubstituteInTemplateWord(param);
        }
        else if (action.toLowerCase(Locale.ROOT).equals("convert".toLowerCase(Locale.ROOT))){

            return ConverterFormat.ConvertToNeedFormat(
                    (String) map.get("НазваниеФайла"),
                    (String) map.get("Расширение"),
                    (String) map.get("ФайлРезультат")
            );
        }
        return "";
    }

    public static Map JsonToMap(String json)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        Map result = new HashMap();
        try {
            result = objectMapper.readValue(json, HashMap.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
