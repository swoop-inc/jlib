package com.swoop.example;

import java.util.*;

//
// Weighted Choice Demo.
//
public class WeightedChoiceExample
{
	private StringChoice[] sentenceStructures = {
		new StringChoice("tjnvd"),
		new StringChoice("tnvCtjnv"),
		new StringChoice("tjnvCtnvdptn"),
		new StringChoice("tjnvdCtnv"),
		new StringChoice("ptntjnv"),
		new StringChoice("tjnctnvptn"),
		new StringChoice("Ctnv")
	};

	private Map<String,StringChoice[]> wordChoiceSets = new HashMap<String,StringChoice[]>();

	WeightedChoiceExample()
	{
		wordChoiceSets.put("t", new StringChoice[] {
			new StringChoice("the", 3),
			new StringChoice("any"),
			new StringChoice("every"),
			new StringChoice("this")
		});

		wordChoiceSets.put("n", new StringChoice[] {
			new StringChoice("fool"),
			new StringChoice("hero"),
			new StringChoice("beast"),
			new StringChoice("goddess"),
			new StringChoice("tree"),
			new StringChoice("bird"),
			new StringChoice("star"),
			new StringChoice("wave"),
			new StringChoice("river"),
			new StringChoice("night")
		});

		wordChoiceSets.put("p", new StringChoice[] {
			new StringChoice("of", 2),
			new StringChoice("through"),
			new StringChoice("on"),
			new StringChoice("in"),
			new StringChoice("above"),
			new StringChoice("over")
		});

		wordChoiceSets.put("v", new StringChoice[] {
			new StringChoice("came"),
			new StringChoice("despaired"),
			new StringChoice("issued"),
			new StringChoice("sounded"),
			new StringChoice("approached"),
			new StringChoice("beheld"),
			new StringChoice("listened"),
			new StringChoice("ate"),
			new StringChoice("drank")
		});

		wordChoiceSets.put("j", new StringChoice[] {
			new StringChoice("black"),
			new StringChoice("heavy"),
			new StringChoice("bright"),
			new StringChoice("red"),
			new StringChoice("dull"),
			new StringChoice("light"),
			new StringChoice("keen"),
			new StringChoice("wise"),
			new StringChoice("silly")
		});

		wordChoiceSets.put("d", new StringChoice[] {
			new StringChoice("greedily"),
			new StringChoice("gustily"),
			new StringChoice("darkly"),
			new StringChoice("hotly"),
			new StringChoice("anew"),
			new StringChoice("too")
		});

		wordChoiceSets.put("c", new StringChoice[] {
			new StringChoice("and", 5),
			new StringChoice("with"),
			new StringChoice("against")
		});

		wordChoiceSets.put("C", new StringChoice[] {
			new StringChoice("and", 3),
			new StringChoice("yet"),
			new StringChoice("although"),
			new StringChoice("moreover")
		});
	}

	public void run()
	{
		new SentenceRandomizer(sentenceStructures, wordChoiceSets).run();
	}

	public static void main(String[] args)
	{
		new WeightedChoiceExample().run();
	}
}
