package com.gurock.testrail;

/**
 * Created by gsaik on 6/4/2017.
 */


import java.lang.reflect.Array;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import com.gurock.testrail.Case;

public class SendRequests {

    static String user = "gsaik@mobileiron.com";
    static String password = "Cro55Fit!";
    static APIClient client;
    static ArrayList <Case> allCases = new ArrayList<Case>();

    public static void main(String[] args) throws Exception {
        client = new APIClient("https://mobileiron.testrail.net/");
        client.setUser(user);
        client.setPassword(password);
    }

    public static ArrayList <Case> getAllCases(int project_id, int suite_id) throws Exception{
        client = new APIClient("https://mobileiron.testrail.net/");
        client.setUser(user);
        client.setPassword(password);
        JSONArray jsonSetAllCases = (JSONArray) client.sendGet("get_cases/" + project_id + "&suite_id=" + suite_id);

        for(int i=0; i<jsonSetAllCases.size(); i++){
            allCases.add(new Case((JSONObject) jsonSetAllCases.get(i)));
        }

        return allCases;
    }

    public static void updateValue(String value, ArrayList<Case> casesSuite) throws Exception {
        Map valueToUpdate = new HashMap();
        for (Case item : casesSuite) {
            valueToUpdate.clear();
            valueToUpdate.put(value, item.getValue(value));
            try {
                client.sendPost("update_case/" + item.getCaseId(), valueToUpdate);
            } catch (APIException e){
                System.out.println("Timeout riched on item: " + item+ " Reconnecting after 10seconds");
                Thread.sleep(10000);
                client.sendPost("update_case/" + item.getCaseId(), valueToUpdate);
            }
        }
        System.out.println("Value: " + value + "was successfully updated on server;");
    }

}
