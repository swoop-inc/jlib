package com.swoop.example;

import com.swoop.util.Randomizer;
import com.swoop.util.WeightedChoiceIterator;

import java.util.Arrays;

public class SingleSelectionDistributionAnalysisExample {
  
  // configuration
  private int loops = 10000000;
  private double[] weights = new double[] { 4, 2, 1, 3, 5, 7 };
  private double maxTolerance = 1; // % of tolerance between expected and actual results
  
  
  private void launchSingleSelection() {
    Randomizer randomizer = new Randomizer();
    int[] selections = new int[weights.length];
    for (int i = 0; i < loops; i++) {
      int selected = randomizer.select(new WeightedChoiceAdapter(weights));
      selections[selected]++;
    }
    showResults(selections);
  }

  private void showResults(int[] selections) {
    // calculate total weight
    double totalWeight = 0;
    for (double weight : weights) {
      totalWeight += weight;
    }
    
    // calculate real weight distribution and errors, check tolerance %
    double[] realWeight = new double[selections.length];
    double[] errors = new double[selections.length];
    boolean[] tolerances = new boolean[selections.length];
    boolean tolerated = true;
    for (int i = 0; i < selections.length; i++) {
      realWeight[i] = (double)selections[i] / (double) loops * totalWeight;
      double errorDiff = Math.abs(realWeight[i] - weights[i]);
      errors[i] = errorDiff / weights[i] * (double)100;
      if (errors[i] > maxTolerance) {
        tolerances[i] = false;
        tolerated = false;
      } else {
        tolerances[i] = true;
      }
    }
    
    System.out.println("Weigths:          " + Arrays.toString(weights));
    System.out.println("Selections:       " + Arrays.toString(selections));
    System.out.println("Real weights:     " + Arrays.toString(realWeight));
    System.out.println("Errors (%):       " + Arrays.toString(errors));
    System.out.println("Tolerances met:   " + Arrays.toString(tolerances));
    System.out.println("Random tolerated: " + tolerated);
  }

  public static void main(String[] args) {
    SingleSelectionDistributionAnalysisExample example = new SingleSelectionDistributionAnalysisExample();
    example.launchSingleSelection();
  }

  private static class WeightedChoiceAdapter implements WeightedChoiceIterator<Integer> {
    double[] weights;
    int index;

    WeightedChoiceAdapter(double[] weights) {
      this.weights = weights;
    }

    @Override
    public boolean hasNext() {
      return index < weights.length;
    }

    @Override
    public double nextWeight() {
      return weights[index++];
    }

    @Override
    public Integer getCurrentValue() {
      return index - 1;
    }
  }
}
