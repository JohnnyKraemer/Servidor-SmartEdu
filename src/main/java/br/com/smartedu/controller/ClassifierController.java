/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.smartedu.controller;

import br.com.smartedu.model.Classifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.lazy.LWL;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.LMT;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;

import java.util.List;

@RestController
@RequestMapping("/classificador")
public class ClassifierController {

   // private int num_Classificadores = 0;

    public weka.classifiers.Classifier[] GeraClassificadores(List<Classifier> selecionados) throws Exception {
        int num_Classificadores = 0;

        weka.classifiers.Classifier[] classificadores = new weka.classifiers.Classifier[selecionados.size()];

        for (Classifier classificador : selecionados) {
            if ("BayesNet".equals(classificador.getName())) {
                BayesNet bayesNet = new BayesNet();
                classificadores[num_Classificadores++] = bayesNet;
            }

            if ("NaiveBayes".equals(classificador.getName())) {
                NaiveBayes naiveBayes = new NaiveBayes();
                classificadores[num_Classificadores++] = naiveBayes;
            }

            //NaiveBayesSimple naiveBayesSimple = new NaiveBayesSimple();
            //classificadores[num_Classificadores++] = naiveBayesSimple;
            if ("NaiveBayesUpdateable".equals(classificador.getName())) {
                NaiveBayesUpdateable naiveBayesUpdateable = new NaiveBayesUpdateable();
                classificadores[num_Classificadores++] = naiveBayesUpdateable;
            }

            if ("Logistic".equals(classificador.getName())) {
                Logistic logistic = new Logistic();
                classificadores[num_Classificadores++] = logistic;
            }

            if ("MultilayerPerceptron".equals(classificador.getName())) {
                MultilayerPerceptron multilayerPerceptron = new MultilayerPerceptron();
                classificadores[num_Classificadores++] = multilayerPerceptron;
            }

            //RBFNetwork rBFNetwork = new RBFNetwork();
            //classificadores[num_Classificadores++] = rBFNetwork;
            if ("SimpleLogistic".equals(classificador.getName())) {
                SimpleLogistic simpleLogistic = new SimpleLogistic();
                classificadores[num_Classificadores++] = simpleLogistic;
            }

            if ("SMO".equals(classificador.getName())) {
                SMO smo = new SMO();
                classificadores[num_Classificadores++] = smo;
            }

            //SPegasos sPegasos = new SPegasos();
            //classificadores[num_Classificadores++] = sPegasos;
            //IB1 ib1 = new IB1();
            //classificadores[num_Classificadores++] = ib1;
            if ("IBk".equals(classificador.getName())) {
                IBk ibk = new IBk();
                classificadores[num_Classificadores++] = ibk;
            }

            if ("KStar".equals(classificador.getName())) {
                KStar kStar = new KStar();
                classificadores[num_Classificadores++] = kStar;
            }

            if ("LWL".equals(classificador.getName())) {
                LWL lwl = new LWL();
                classificadores[num_Classificadores++] = lwl;
            }

            if ("AdaBoostM1".equals(classificador.getName())) {
                AdaBoostM1 adaBoostM1 = new AdaBoostM1();
                classificadores[num_Classificadores++] = adaBoostM1;
            }

            //END end = new END();
            //classificadores[num_Classificadores++] = end;
            if ("JRip".equals(classificador.getName())) {
                JRip jRip = new JRip();
                classificadores[num_Classificadores++] = jRip;
            }

            //ADTree aDTree = new ADTree();
            //classificadores[num_Classificadores++] = aDTree;
            //FT ft = new FT();
            //classificadores[num_Classificadores++] = ft;
            if ("J48".equals(classificador.getName())) {
                J48 j48 = new J48();
                classificadores[num_Classificadores++] = j48;
            }

            //J48graft j48graft = new J48graft();
            //classificadores[num_Classificadores++] = j48graft;
            //LADTree lADTree = new LADTree();
            //classificadores[num_Classificadores++] = lADTree;
            if ("LMT".equals(classificador.getName())) {
                LMT lmt = new LMT();
                classificadores[num_Classificadores++] = lmt;
            }

            //NBTree nBTree = new NBTree();
            //classificadores[num_Classificadores++] = nBTree;
            if ("RandomForest".equals(classificador.getName())) {
                RandomForest randomForest = new RandomForest();
                classificadores[num_Classificadores++] = randomForest;
            }

            if ("REPTree".equals(classificador.getName())) {
                REPTree rEPTree = new REPTree();
                classificadores[num_Classificadores++] = rEPTree;
            }
        }
        //SimpleCart simpleCart = new SimpleCart();
        //classificadores[num_Classificadores++] = simpleCart;
        return classificadores;
    }

    public weka.classifiers.Classifier NewClassifier(Classifier classifier) throws Exception {

        if ("BayesNet".equals(classifier.getName())) {
            BayesNet bayesNet = new BayesNet();
            return bayesNet;
        }

        if ("NaiveBayes".equals(classifier.getName())) {
            NaiveBayes naiveBayes = new NaiveBayes();
            return naiveBayes;
        }

        //NaiveBayesSimple naiveBayesSimple = new NaiveBayesSimple();
        //classificadores[num_Classificadores++] = naiveBayesSimple;
        if ("NaiveBayesUpdateable".equals(classifier.getName())) {
            NaiveBayesUpdateable naiveBayesUpdateable = new NaiveBayesUpdateable();
            return naiveBayesUpdateable;
        }

        if ("Logistic".equals(classifier.getName())) {
            Logistic logistic = new Logistic();
            return logistic;
        }

        if ("MultilayerPerceptron".equals(classifier.getName())) {
            MultilayerPerceptron multilayerPerceptron = new MultilayerPerceptron();
            return multilayerPerceptron;
        }

        //RBFNetwork rBFNetwork = new RBFNetwork();
        //classificadores[num_Classificadores++] = rBFNetwork;
        if ("SimpleLogistic".equals(classifier.getName())) {
            SimpleLogistic simpleLogistic = new SimpleLogistic();
            return simpleLogistic;
        }

        if ("SMO".equals(classifier.getName())) {
            SMO smo = new SMO();
            return smo;
        }

        //SPegasos sPegasos = new SPegasos();
        //classificadores[num_Classificadores++] = sPegasos;
        //IB1 ib1 = new IB1();
        //classificadores[num_Classificadores++] = ib1;
        if ("IBk".equals(classifier.getName())) {
            IBk ibk = new IBk();
            return ibk;
        }

        if ("KStar".equals(classifier.getName())) {
            KStar kStar = new KStar();
            return kStar;
        }

        if ("LWL".equals(classifier.getName())) {
            LWL lwl = new LWL();
            return lwl;
        }

        if ("AdaBoostM1".equals(classifier.getName())) {
            AdaBoostM1 adaBoostM1 = new AdaBoostM1();
            return adaBoostM1;
        }

        //END end = new END();
        //classificadores[num_Classificadores++] = end;
        if ("JRip".equals(classifier.getName())) {
            JRip jRip = new JRip();
            return jRip;
        }

        //ADTree aDTree = new ADTree();
        //classificadores[num_Classificadores++] = aDTree;
        //FT ft = new FT();
        //classificadores[num_Classificadores++] = ft;
        if ("J48".equals(classifier.getName())) {
            J48 j48 = new J48();
            return j48;
        }

        //J48graft j48graft = new J48graft();
        //classificadores[num_Classificadores++] = j48graft;
        //LADTree lADTree = new LADTree();
        //classificadores[num_Classificadores++] = lADTree;
        if ("LMT".equals(classifier.getName())) {
            LMT lmt = new LMT();
            return lmt;
        }

        //NBTree nBTree = new NBTree();
        //classificadores[num_Classificadores++] = nBTree;
        if ("RandomForest".equals(classifier.getName())) {
            RandomForest randomForest = new RandomForest();
            return randomForest;
        }

        if ("REPTree".equals(classifier.getName())) {
            REPTree rEPTree = new REPTree();
            return rEPTree;
        }

        //SimpleCart simpleCart = new SimpleCart();
        //classificadores[num_Classificadores++] = simpleCart;
        return null;
    }

}
