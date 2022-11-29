package com.h2;

public class MortgageCalculator
{
    private long loanAmount;
    private int termInYears;
    private float annualRate;
    private double monthlyPayment;

    public MortgageCalculator(long loanAmount, int termInYears, float annualRate)
    {
        this.loanAmount = loanAmount;
        this.termInYears = termInYears;
        this.annualRate = annualRate;
    }

    private int getNumberOfPayments()
    {
        return termInYears*12;
    }

    private float getMonthlyInterestRate()
    {
        return 0.0f;
    }
}
