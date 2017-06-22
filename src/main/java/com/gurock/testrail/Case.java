package com.gurock.testrail;

import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by gsaik on 6/4/2017.
 */
public class Case {

    JSONObject data;

    double P0Coeficient = 1;
    double P1Coeficient = 0.5;
    double P2Coeficient = 0.2;


    public static void main(String[] args) throws Exception {

    }

    public Case(JSONObject data) throws Exception {
        this.data = data;
    }

    public int getPriority() {
        return ((Number) data.get("priority_id")).intValue();
    }

    public int getCaseId() {
        return ((Number) data.get("id")).intValue();
    }

    public String getRef() {
        return (data.get("refs")).toString();
    }

    public void setRef(String value) {
        this.data.remove("refs");
        this.data.put("refs", value);
    }

    public void setNewValue(String value, String newValue) {
        this.data.remove(value);
        this.data.put(value, newValue);
    }

    public String getValue(String field) {
        String value="";
        try{
            value = (data.get(field)).toString();
        } catch (Exception e){
            value="null";
        }
        return value;
    }

    public double getPriorityCoeficient() {
        double coeficient = 0;
        if (this.getPriority() == 4){
            coeficient = P0Coeficient;
        } else if (this.getPriority() == 3){
            coeficient = P1Coeficient;
        }else if (this.getPriority() == 10) {
            coeficient = P2Coeficient;
        }
        return coeficient;
    }

    public void clearFieldByPattern(String field, String pattern) {
        String currentValue = getValue("refs");
        String newValue = currentValue.replaceAll(pattern, "");
        this.setNewValue(field,newValue);
    }

    //Generates list of env which could be run for current test case
    public  ArrayList<String> getListOfEnvToPass(ArrayList<String[]> evironmentsSet) {
        ArrayList<String> listOfEnvToPass = new ArrayList<String>();
        //all labels from case
        String[] caseLabels = this.getRef().replaceAll(" ","").split(",");

        //for each env check if labels are listed
        Boolean fit = true;
        for (String[] list : evironmentsSet) {
            for (int i = 1; i < list.length; i++) {
                //if (!Arrays.asList(caseLabels).contains(list[i])) {
                if (!Arrays.asList(caseLabels).contains(list[i])) {
                    fit = false;
                    break;
                }
            }

            //add env number to listOfEnvToPass
            if (fit) listOfEnvToPass.add(list[0]);
            fit = true;
        }
        return listOfEnvToPass;
    }


    public List<String> generateRandomSet(int numberOfEnvInFinalSet, ArrayList<String> listEnvForCaseWhichCouldBeRun){
        Collections.shuffle(listEnvForCaseWhichCouldBeRun);
        List<String> subSet = new ArrayList<String>();
        try {
            subSet = listEnvForCaseWhichCouldBeRun.subList(0, (numberOfEnvInFinalSet));
        } catch (IllegalArgumentException e){}
        return subSet;
    }

    public String formatRefList(List<String> finalEnvToRun){
        String ref="";

        for (String item:finalEnvToRun){
            ref += "#" + item + " ";
        }

        return ref;
    }

}
