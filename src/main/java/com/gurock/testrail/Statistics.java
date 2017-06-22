package com.gurock.testrail;

import java.util.ArrayList;

/**
 * Created by gsaik on 6/5/2017.
 */
public class Statistics {

        private static ArrayList<Case> casesData;
        private static ArrayList<String[]> envData;

    public static void main(String[] args) throws Exception {

    }

    public Statistics(ArrayList<Case> allCases, ArrayList<String[]> environmentsSet ){
            casesData = allCases;
            envData = environmentsSet;
    }

    public static void printTotalCasesNumberPerEnv(){
        String envNumber = "";
        for (String[] item:envData){
        }
    }
    public static void getCasesNumbersByPriorityPerEnv(){

    }

    public static void getCasesNumbersByStatePerEnv(){

    }

}
