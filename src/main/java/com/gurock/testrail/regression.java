/**
 * Created by gsaik on 6/2/2017.
 */

package com.gurock.testrail;

import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class regression {


    public static void main(String[] args) throws Exception {
        int project_id = 29;
        int suite_id = 332;

        ArrayList<String[]> environmentsSet = new ArrayList<String[]>();
        environmentsSet.add(new String[]{"1", "Core", "ACe", "Google"});
        environmentsSet.add(new String[]{"2", "Cloud", "AfW", "Exchange"});
        environmentsSet.add(new String[]{"3", "Core", "ACe", "Exchange"});
        environmentsSet.add(new String[]{"4", "Cloud", "AfW", "Exchange"});
        environmentsSet.add(new String[]{"5", "Cloud", "ACe", "Exchange"});
        environmentsSet.add(new String[]{"6", "Core", "ACe", "Exchange"});
        environmentsSet.add(new String[]{"7", "Core", "ACe", "Google"});
        environmentsSet.add(new String[]{"8", "Core", "AfW", "Exchange"});

        //add log about login result

        //get all cases in suite Android Email+ 2.x
        ArrayList<Case> allCases = SendRequests.getAllCases(project_id, suite_id);
        System.out.println("Total number of cases: " + allCases.size());


        //with priority P0-P2 : id for P0=4; P1=3; P2=10; P3=6
        ArrayList<Case> regressionTestCases = new ArrayList<Case>();

        for (Case item:allCases){
            if(item.getPriority() == 4 || item.getPriority() == 3 || item.getPriority() == 10) {
                regressionTestCases.add(item);
            }
        }

        System.out.println("Number of P0-P2 cases is " + regressionTestCases.size());

        //--Clear everything after #in references field for all cases
        for (Case item:regressionTestCases){
            try {
                item.clearFieldByPattern("refs", "#\\d+");
            } catch (Exception e){
                System.out.println("Case with id "+item.getCaseId() + " doesn't have ref");
            }
        }

        //update ref value on server for cases in regressionTestCases suite
        SendRequests.updateValue("refs", regressionTestCases);

        //for each case form number of env to run
        for(Case item:regressionTestCases){
            //--Check each environment if the case could be run with
            ArrayList<String> listEnvForCaseWhichCouldBeRun = item.getListOfEnvToPass(environmentsSet);
            //find number of env for case
            int size = listEnvForCaseWhichCouldBeRun.size();
            //find number of times it should be run due to priority
            int numberOfEnvToRun = (int) Math.ceil(size * item.getPriorityCoeficient());

            //Randomly select :numberOfEnvToRun environments from :listEnvForCaseWhichCouldBeRun
            List<String> finalEnvToRun = item.generateRandomSet(numberOfEnvToRun, listEnvForCaseWhichCouldBeRun);

            //add #sign for each env number
            String ref = item.formatRefList(finalEnvToRun);

            String currentRefValue = item.getRef();
            item.setRef(currentRefValue + ref);

            System.out.println("Environments for case:" + item.getCaseId() + "defined and saved locally");
        }

        SendRequests.updateValue("refs", regressionTestCases);

        allCases = SendRequests.getAllCases(project_id, suite_id);
        Statistics statistics = new Statistics(allCases, environmentsSet);

    }
}
