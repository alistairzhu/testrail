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

    static String user = "alistairzhu@gmail.com";
    static String password = "d4qcH51/dK91aaDtA8iN-GJu9qyyxy03K/zks9bKP";
    static APIClient client;
    static ArrayList <Case> allCases = new ArrayList<Case>();
    static Case  aCase ;

    public static void main(String[] args) throws Exception {
        //client = new APIClient("https://mobileiron.testrail.net/");
        client = new APIClient("https://laserfiche20.testrail.io/");
        client.setUser(user);
        client.setPassword(password);

        //getAllCases(1,1);
        //getCase(2);
        //updateValue("pass-------------------------------------A", getAllCases(1,1));

        updateResult(2, 5);

    }

    public static ArrayList <Case> getAllCases(int project_id, int suite_id) throws Exception{

        JSONArray jsonSetAllCases = (JSONArray) client.sendGet("get_cases/" + project_id + "&suite_id=" + suite_id);

        for(int i=0; i<jsonSetAllCases.size(); i++){
            allCases.add(new Case((JSONObject) jsonSetAllCases.get(i)));
        }
        return allCases;
    }


    public static Case getCase(int case_id) throws Exception{
        aCase =   new Case((JSONObject) client.sendGet("get_case/" + case_id));
        return aCase;
    }

    //AZ: not work now!
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

    /**
     * Add test result to a test case by case_id.
     * result = 1 Passed , 2 Blocked, 4 Retest, 5 Failed .
     */

    public static void updateResult(int result, int case_id) throws Exception {

        Map valueToUpdate = new HashMap();
        valueToUpdate.put("status_id", result);

            try {
                client.sendPost("add_result/" + case_id , valueToUpdate);
            } catch (APIException e){
                System.out.println("Timeout riched on item: " + case_id +  " Reconnectng after 10seconds");
                Thread.sleep(10000);
                client.sendPost("update_case/" + case_id, valueToUpdate);
            }
        System.out.println("Result: " + result + " _ was successfully updated on server;");
    }

}
