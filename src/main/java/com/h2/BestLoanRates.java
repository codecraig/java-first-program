package com.h2;

import java.util.Map;
import java.util.Scanner;

public class BestLoanRates
{
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println( "Enter your name" );
        String name = scanner.nextLine();
        System.out.println(String.format("Hello %s", name));
    }
    public static final Map<Integer, Float> bestRates = Map.of(1, 5.50f, 2, 3.45f, 3, 2.67f);

    public static float getRates(int loanTermInYears)
    {
        if(bestRates.containsKey(loanTermInYears))
        {
            return bestRates.get(loanTermInYears);
        }
        else
        {
            return 0.0f;
        }
    }
}
