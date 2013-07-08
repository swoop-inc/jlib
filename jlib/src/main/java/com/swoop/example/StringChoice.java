package com.swoop.example;

public class StringChoice
{
	double weight;
	String value;

	public StringChoice(String value)
	{
		this(value, 1);
	}

	public StringChoice(String value, double weight)
	{
		this.value = value;
		this.weight = weight;
	}

	public StringChoice downgrade()
	{
		weight /= 3;
		return this;
	}
}

