package com.ecentum.MonthlyExpenses.helpers;

public class Help {
    public static String toSentenceCase(String input){
        String lowerCase = input.toLowerCase();
        return lowerCase.substring(0, 1)
                .toUpperCase()
                .concat(lowerCase
                        .substring(1)
                        .toLowerCase());
    }
}
