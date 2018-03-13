/*
 *  How to use WEKA API in Java 
 *  Copyright (C) 2014 
 *  @author Dr Noureddin M. Sadawi (noureddin.sadawi@gmail.com)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it as you wish ... 
 *  I ask you only, as a professional courtesy, to cite my name, web page 
 *  and my YouTube Channel!
 *  
 */
package br.com.smartedu.controller;
//import required classes

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.*;

public class Discretize {

    /**
     * Método para discretizar um DataSet
     *
     * @author Johnny
     * @param dataSet Instances - DataSet.
     * @param col_Discretize String - Culunas numéricas que deseja discretizas.
     * @param col_NumericToNominal String - Culunas que não deseja transformar
     * de número para nominal.
     * @return Instances - DataSet discretizado.
     */
    public Instances discretize(Instances dataSet,
            String col_Discretize,
            String col_NumericToNominal,
            boolean obfuscate) throws Exception {

        //Discretize
        if (!col_Discretize.isEmpty()) {
            String[] opDiscretize = new String[2];
            opDiscretize[0] = "-R";
            opDiscretize[1] = col_Discretize;

            weka.filters.unsupervised.attribute.Discretize mDiscretize = new weka.filters.unsupervised.attribute.Discretize();
            mDiscretize.setOptions(opDiscretize);
            mDiscretize.setInputFormat(dataSet);
            dataSet = Filter.useFilter(dataSet, mDiscretize);
        }
        //NominalToString
        //NominalToString m_NominalToString = new NominalToString();
        //m_NominalToString.setInputFormat(dataSet);
        //dataSet = Filter.useFilter(dataSet, m_NominalToString);

        //NumericToNominal
        if (!col_NumericToNominal.isEmpty()) {
            String[] op_NumericToNominal = new String[2];

            op_NumericToNominal[0] = "-R";
            op_NumericToNominal[1] = col_NumericToNominal;
            //op_NumericToNominal[2] = "-V";

            NumericToNominal m_NumericToNominal = new NumericToNominal();
            m_NumericToNominal.setOptions(op_NumericToNominal);
            m_NumericToNominal.setInputFormat(dataSet);
            dataSet = Filter.useFilter(dataSet, m_NumericToNominal);
        }
        //RemoveUseless
        //RemoveUseless m_AttFilter = new RemoveUseless();
        //m_AttFilter.setInputFormat(dataSet);
        //dataSet = Filter.useFilter(dataSet, m_AttFilter);
        //Discretize
        //Discretize m_Discretize = new Discretize();
        //m_Discretize.setInputFormat(dataSet);
        //dataSet = Filter.useFilter(dataSet, m_Discretize);
        //Obfuscate
        if (obfuscate) {
            Obfuscate m_Obfuscate = new Obfuscate();
            m_Obfuscate.setInputFormat(dataSet);
            dataSet = Filter.useFilter(dataSet, m_Obfuscate);
        }

        //NominalToBinary
        //NominalToBinary m_NominalToBinary = new NominalToBinary();
        //m_NominalToBinary.setInputFormat(dataSet);
        //dataSet = Filter.useFilter(dataSet, m_NominalToBinary);
        //System.out.println(dataSet);
        //ArffSaver saver = new ArffSaver();
        //saver.setInstances(newData);
        //saver.setFile(new File("database/irisDiscretize.arff"));
        //saver.writeBatch();
        return dataSet;
    }

    public Instances obfuscate(Instances dataSet) throws Exception {
        //Obfuscate
        Obfuscate m_Obfuscate = new Obfuscate();
        m_Obfuscate.setInputFormat(dataSet);
        dataSet = Filter.useFilter(dataSet, m_Obfuscate);

        return dataSet;
    }
}
