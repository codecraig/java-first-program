package com.h2;

public class SavingsCalculator
{
    private float[] credits;
    private float[] debits;

    public SavingsCalculator(float[] credits, float[] debits)
    {
        this.credits = credits;
        this.debits = debits;
    }

    private float sumOfCredits()
    {
        float sum = 0.0f;
        for(int i = 0; i < credits.length; i++)
        {
            sum += credits[i];
        }
        return sum;
    }
}
