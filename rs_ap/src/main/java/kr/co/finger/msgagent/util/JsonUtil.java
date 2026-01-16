package kr.co.finger.msgagent.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonUtil {
	
	public static JSONObject stringToJson(String strText) throws Exception {
		JSONParser jsonParser = new JSONParser();
        
        try {
        	return (JSONObject) jsonParser.parse(strText);
        }
        catch (Exception e){
        	throw e;
        }
	}
	
	/**s
	 * 
	 * @param json :
	 * @param getKeyName : json 데애터 중 추출할 key이름 
	 * @return JSONArray
	 * @throws Exception
	 */
	public static JSONArray getJsonArrayList(JSONObject json, String getKeyName) throws Exception {
		JSONParser jsonParser = new JSONParser();
        
        try {
        	return  (JSONArray)jsonParser.parse(json.get(getKeyName).toString());
        }
        catch (Exception e){
        	throw e;
        }
	}


}
