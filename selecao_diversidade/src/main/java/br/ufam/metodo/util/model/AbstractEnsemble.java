package br.ufam.metodo.util.model;

import com.github.javacliparser.FlagOption;
import com.github.javacliparser.FloatOption;
import com.github.javacliparser.IntOption;
import com.github.javacliparser.MultiChoiceOption;

import moa.classifiers.AbstractClassifier;
import moa.classifiers.Classifier;
import moa.options.ClassOption;

public abstract class AbstractEnsemble extends AbstractClassifier{

    public FloatOption lambdaOption = new FloatOption("lambdaAttribute", 'y', "Lambda",1);
    
    public IntOption ensembleSizeOption = new IntOption("ensembleSize", 's',
            "The number of models in the bag.", 10, 1, Integer.MAX_VALUE);
    
    public IntOption numBaseLeanersOption = new IntOption("numBaseLeaners", 'x',
            "The number of baseLeaners.", 1, 1, 5);
    
    public FloatOption deltaAdwinOption = new FloatOption("deltaAdwin", 'a',
            "Delta of Adwin change detection", 0.002, 0.0, 1.0);
    
    public ClassOption baseLearner1Option = new ClassOption("baseLearner1", '1',
            "Classifier to train.", Classifier.class, "trees.HoeffdingTree");
    
    public ClassOption baseLearner2Option = new ClassOption("baseLearner2", '2',
            "Classifier to train.", Classifier.class, "lazy.kNN");
    
    public ClassOption baseLearner3Option = new ClassOption("baseLearner3", '3',
            "Classifier to train.", Classifier.class, "trees.RandomHoeffdingTree");
    
    public ClassOption baseLearner4Option = new ClassOption("baseLearner4", '4',
            "Classifier to train.", Classifier.class, "functions.Perceptron");
    
    public ClassOption baseLearner5Option = new ClassOption("baseLearner5", '5',
            "Classifier to train.", Classifier.class, "bayes.NaiveBayes");
    
    
    public ClassOption baseLearnerOption = new ClassOption("baseLearner", 'l',
            "Classifier to train.", Classifier.class, "trees.HoeffdingTree");

    //public IntOption ensembleSizeOption = new IntOption("ensembleSize", 's',
    //        "The number of models in the bag.", 10, 1, Integer.MAX_VALUE);

    public FloatOption weightShrinkOption = new FloatOption("weightShrink", 'w',
            "The number to use to compute the weight of new instances.", 6, 0.0, Float.MAX_VALUE);

    //public FloatOption deltaAdwinOption = new FloatOption("deltaAdwin", 'a',
    //        "Delta of Adwin change detection", 0.002, 0.0, 1.0);

    // Leveraging Bagging MC: uses this option to use Output Codes
    public FlagOption outputCodesOption = new FlagOption("outputCodes", 'o',
            "Use Output Codes to use binary classifiers.");

    public MultiChoiceOption leveraginBagAlgorithmOption = new MultiChoiceOption(
            "leveraginBagAlgorithm", 'm', "Leveraging Bagging to use.", new String[]{
                "LeveragingBag", "LeveragingBagME", "LeveragingBagHalf", "LeveragingBagWT", "LeveragingSubag"},
            new String[]{"Leveraging Bagging for evolving data streams using ADWIN",
                "Leveraging Bagging ME using weight 1 if misclassified, otherwise error/(1-error)",
                "Leveraging Bagging Half using resampling without replacement half of the instances",
                "Leveraging Bagging WT without taking out all instances.",
                "Leveraging Subagging using resampling without replacement."
            }, 0);
    
    
    public abstract boolean detectouDrift();
    
    public abstract void estrategiaSimpleReset();
    

}
