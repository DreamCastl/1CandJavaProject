package WorkWithDocument;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.*;

@Getter
public class ParamDocument {
    private String fileName ;
    private String fileExtension;
    private Map<String,LinkedList<Map<String,String>>> table  = new HashMap<>();
    private String ArrayAddingParam ; //Todo
    private HashMap<String,String> headers = new HashMap<>();
    private boolean addQR = false;
    private boolean substrate = false;
    private String substrateWay = "";
    private String substrateWayFirstPage = "";
    private boolean addRunningTitle = false;
    private boolean  converToPDF = false;
    private boolean variablePrinting = false;
    private LinkedList<Map<String,String>> ParamQR = new LinkedList<>();
    //private boolean oneFields = false;

    public ParamDocument (Map result) {

        try {
            headers.clear();
            fileName = (String) result.get("НазваниеФайла");
            fileExtension = (String) result.get("Расширение");
            addQR = (boolean) result.get("ВставитьQRКод");
            substrate = (boolean) result.get("ДобавитьПодложку");
            substrateWay = (String) result.get("ФайлПодложки");
            substrateWayFirstPage = (String) result.get("ФайлПодложкиПервойСтраницы");
            addRunningTitle = (boolean) result.get("ДобавитьКолонтитулы");
            converToPDF = (boolean) result.get("КонвертироватьВПДФ");
            variablePrinting = (boolean) result.get("ПечатьПоПеременным");

           // oneFields = (Object) result.get("ПоместитьПоляНаОднуСтраницу");// поля на одну таблицу

            LinkedHashMap<String,String> tmp = (LinkedHashMap<String, String>) result.get("Шапка");
            Map<String,String> hea = new HashMap<>();
            tmp.forEach((s, s2) -> headers.put(s,s2));

            LinkedHashMap<String,LinkedHashMap<String,Object>>[] asd;

            ((LinkedHashMap<?, ?>) result.get("Таблицы"))
                    .forEach((o, o2) ->
                            table.put((String) o,ReturnTableValue1(o2)));

            for (LinkedHashMap<?, ?> tmp1 : (List<LinkedHashMap<?, ?>>) result.get("ПараметрыQR")) {

            Map<String,String>  tmpMap = new HashMap<>();
            tmpMap.put("way", (String) tmp1.get("ПутьQR"));
               // tmpMap.put("param",tmp1.get("ПутьQR"))
                ParamQR.add(tmpMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LinkedList<Map<String,String>> ReturnTableValue1(Object o2) {
        LinkedList<Map<String,String>> rez = new LinkedList<>();
        Arrays.asList(o2).forEach(o -> ConverToMap(o,rez));

        return rez;
    }

    private void ConverToMap(Object o1, LinkedList<Map<String, String>> rez) {
       // Map<String, String> rez = new HashMap<>();

        List<LinkedHashMap<String,String>> temp1 = (List<LinkedHashMap<String, String>>) o1;
        for (LinkedHashMap<String,String> temp2: temp1) {
         HashMap<String,String> newHashMap = new HashMap<>();
         temp2.forEach((s, s2) -> newHashMap.put(s,s2));
         rez.add(newHashMap);
        }

    }

}
