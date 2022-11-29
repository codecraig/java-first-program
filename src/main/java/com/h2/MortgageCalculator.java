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
        float interestRate = annualRate/100;
        return interestRate/12;
    }

    public void calculateMonthlyPayment()
    {
        long P = loanAmount;
        float r = getMonthlyInterestRate();
        int n = getNumberOfPayments();
        var M = P * (((r * Math.pow(1 + r, n))) / ((Math.pow((1 + r), n)) - 1));
        this.monthlyPayment = M;
    }
}
