package com.h2;

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
}
