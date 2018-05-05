package br.com.smartedu.controller;

import br.com.smartedu.model.*;
import br.com.smartedu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Controller;
import weka.classifiers.Evaluation;
import weka.core.Instances;

import java.util.*;

@Controller
@Configurable
public class ClassifyController {

    @Autowired
    private ProbabilityRepository probabilityRepository;

    @Autowired
    private TestClassifierRepository testClassifierRepository;

    @Autowired
    private SituationRepository situationRepository;

    public void ClassifyTest(TestClassifier testClassifier, weka.classifiers.Classifier tree, Instances dataSetTraining, Instances dataSetTest, List<Student> students) throws Exception {
        Instances dataSetTeste = new Instances(dataSetTest);
        dataSetTeste.deleteAttributeAt(0);

        Instances dataSetTreino = new Instances(dataSetTraining);
        dataSetTreino.deleteAttributeAt(0);

        int num_instances = dataSetTeste.numInstances();
        List<Probability> probabilitys = new ArrayList<>();

        try {
            //System.out.println(num_instances - 1);
            //Instances training = new Instances(dataSetTreino);
            //tree.buildClassifier(training);
           // Evaluation eval1 = new Evaluation(training);

            for (int i = 0; i <= (num_instances - 1); i++) {
                Instances training = new Instances(dataSetTreino);
                tree.buildClassifier(training);
                Evaluation eval1 = new Evaluation(training);

                double ed = eval1.evaluateModelOnceAndRecordPrediction(tree, dataSetTeste.instance(i));

                //System.out.println("\n\n"+eval1.getHeader().attribute(1).value((int) ed));
                System.out.println("\n\n"+eval1.getHeader().attribute(eval1.getHeader().numAttributes()-1).value((int) ed));
                System.out.println("Predição do classificador: "+ed);
                System.out.println("Resultado: "+eval1.correct());

                String evadir = String.valueOf(eval1.predictions().toArray()[0]);
                String[] arrayValores = evadir.split(" ");

                System.out.println(dataSetTeste.instance(i).toString(dataSetTeste.numAttributes() - 1) + ": " +arrayValores[0] + " - "+ arrayValores[1] + " - " + arrayValores[2] + " - " + arrayValores[3] + " - " + arrayValores[4] + " - " + arrayValores[5]);

                Probability probabilidade = new Probability();

                if(eval1.getHeader().attribute(eval1.getHeader().numAttributes()-1).value(0) == "Evadido" ){
                    probabilidade.setProbability_evasion(Double.parseDouble(arrayValores[4]));
                }else{
                    probabilidade.setProbability_evasion(Double.parseDouble(arrayValores[5]));
                }


                //Situation situation = situationRepository.findBySituationShort("Não evadido");
                probabilidade.setSituation("Não evadido");

                probabilidade.setTestClassifier(testClassifier);
                probabilidade.setStudent(students.get(i));
                probabilitys.add(probabilidade);

                //System.out.println(i);
            }

            //System.out.println(probabilitys);
            List<Probability> probs = probabilityRepository.save(probabilitys);

            //System.out.println(probs);

            //for (Probability prob : probs) {
            //    System.out.println("\nNome: " + prob.getStudent().getName() + "\nProb: " + prob.getProbability_evasion() + "\n\n");
            //}


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void ClassifyTraining(br.com.smartedu.model.Classifier classificador, weka.classifiers.Classifier tree, Instances dataSet, List<Student> students, Course curso, List<Variable> variaveis, int type, int period_calculation, int period) throws Exception {
        Instances dataSetTeste = new Instances(dataSet);
        dataSetTeste.deleteAttributeAt(0);

        Instances dataSetTreino = new Instances(dataSet);
        dataSetTreino.deleteAttributeAt(0);

        int num_instances = dataSetTeste.numInstances();
        double[] probEvasao = new double[num_instances];
        String[] situacao = new String[num_instances];
        int acertoEvadido = 0;
        int erroEvadido = 0;
        int intervaloEvadido = 0;
        int acertoNEvadido = 0;
        int intervaloNaoEvadido = 0;
        int erroNEvadido = 0;

        String st_Classificador = tree.getClass().getName();
        st_Classificador = st_Classificador.replace(".", ";");
        String[] arrayClassificador = st_Classificador.split(";");
        st_Classificador = arrayClassificador[3];

        Date start_test_classifier = new Date();

        if (testClassifierRepository.findByPeriodCalculation(1) != null) {
            TestClassifier test_classifier_error = testClassifierRepository.findByPeriodCalculation(1);
            test_classifier_error.setPeriodCalculation(0);
            testClassifierRepository.save(test_classifier_error);
        }

        TestClassifier test_classifier_before = new TestClassifier();
        test_classifier_before.setPeriodCalculation(1);
        test_classifier_before.setVariable(variaveis);
        test_classifier_before.setClassifier(classificador);
        test_classifier_before.setCourse(curso);
        test_classifier_before.setStart(start_test_classifier);
        test_classifier_before.setType(type);
        //test_classifier_before.setInterval(0);
        test_classifier_before.setResult(TestClassifier.RESULT_ERROR);
        testClassifierRepository.save(test_classifier_before);

        List<Probability> probabilitys = new ArrayList<>();
        try {
            for (int i = 0; i <= (num_instances - 1); i++) {
                Instances training = new Instances(dataSetTreino);
                training.delete(i);

                tree.buildClassifier(training);

                Evaluation eval1 = new Evaluation(training);
                double ed = eval1.evaluateModelOnceAndRecordPrediction(tree, dataSetTeste.instance(i));

                //System.out.println("\n\n"+eval1.getHeader().attribute(eval1.getHeader().numAttributes()-1).value((int) ed));
                //System.out.println("Predição do classificador: "+ed);
                //System.out.println("Resultado: "+eval1.correct());

                String evadir = String.valueOf(eval1.predictions().toArray()[0]);
                String[] arrayValores = evadir.split(" ");

                //System.out.println(dataSetTeste.instance(i).toString(dataSetTeste.numAttributes() - 1) + ": " +arrayValores[0] + " - "+ arrayValores[1] + " - " + arrayValores[2] + " - " + arrayValores[3] + " - " + arrayValores[4] + " - " + arrayValores[5]);

                //System.out.println(eval1.getHeader().attribute(1).value(0));
                if(eval1.getHeader().attribute(eval1.getHeader().numAttributes()-1).value(0) == "Evadido" ){
                    probEvasao[i] = Double.parseDouble(arrayValores[4]);
                }else{
                    probEvasao[i] = Double.parseDouble(arrayValores[5]);
                }


                if ("Evadido".equals(String.valueOf(dataSetTeste.instance(i).toString(dataSetTeste.numAttributes() - 1)))) {
                    situacao[i] = "Evadido";
                    if (probEvasao[i] <= 0.4) {
                        erroEvadido++;
                    } else if (probEvasao[i] > 0.4 && probEvasao[i] <= 0.6) {
                        intervaloEvadido++;
                    } else if (probEvasao[i] > 0.6) {
                        acertoEvadido++;
                    }
                } else if ("Formado".equals(String.valueOf(dataSetTeste.instance(i).toString(dataSetTeste.numAttributes() - 1)))) {
                    situacao[i] = "Formado";
                    if (probEvasao[i] <= 0.4) {
                        acertoNEvadido++;
                    } else if (probEvasao[i] > 0.4 && probEvasao[i] <= 0.6) {
                        intervaloNaoEvadido++;
                    } else if (probEvasao[i] > 0.6) {
                        erroNEvadido++;
                    }
                } else if ("'Não Evadido'".equals(String.valueOf(dataSetTeste.instance(i).toString(dataSetTeste.numAttributes() - 1)))) {
                    situacao[i] = "Não Evadido";
                    if (probEvasao[i] <= 0.4) {
                        acertoNEvadido++;
                    } else if (probEvasao[i] > 0.4 && probEvasao[i] <= 0.6) {
                        intervaloNaoEvadido++;
                    } else if (probEvasao[i] > 0.6) {
                        erroNEvadido++;
                    }
                } else if ("Outro".equals(String.valueOf(dataSetTeste.instance(i).toString(dataSetTeste.numAttributes() - 1)))) {
                    situacao[i] = "Outro";
                }
                Probability probabilidade = new Probability();
                probabilidade.setProbability_evasion(probEvasao[i]);

                //Situation situation = situationRepository.findBySituationShort(situacao[i]);
                probabilidade.setSituation(situacao[i]);

                //TestClassifier testeClassificadorDurante = testClassifierRepository.findByPeriodCalculation(1);
                probabilidade.setTestClassifier(test_classifier_before);
                probabilidade.setStudent(students.get(i));
                //probabilidade = probabilityRepository.save(probabilidade);

                //Student aluno = studentRepository.findOne(Long.parseLong(dataSet.instance(i).toString(0)));
                //Set<Probability> probs = students.get(i).getProbability();
                //probs.add(probabilidade);
                probabilitys.add(probabilidade);

                //System.out.println("\n\nAluno: " + students.get(i).getName());
                //System.out.println("Prob: " + probabilidade.getProbability_evasion() + " - " + probabilidade.getSituation());
                //for(Probability probability : probs){
                //    System.out.println(probability.getId()+" - "+probability.getState());
                //}
                //aluno.setProbability(probs);
                //studentRepository.save(aluno);
                //students.get(i).setProbability(probs);
                //students.get(i).getProbability().add(probabilidade);
            }
            //System.out.println("\n\n"+students);
            //studentRepository.save(students);

            probabilityRepository.save(probabilitys);

            Date end_test_classifier = new Date();
            long horas = (start_test_classifier.getTime() - end_test_classifier.getTime()) / 3600000;
            long minutos = (start_test_classifier.getTime() - end_test_classifier.getTime() - horas * 3600000) / 60000;
            long segundos = (start_test_classifier.getTime() - end_test_classifier.getTime() - minutos * 3600000) / 60000;

            TestClassifier test_classifier_after = testClassifierRepository.findByPeriodCalculation(1);
            test_classifier_after.setSuccess((acertoEvadido + acertoNEvadido));
            test_classifier_after.setFailure((erroEvadido + erroNEvadido));
            test_classifier_after.setSuccess_evaded(acertoEvadido);
            test_classifier_after.setSuccess_not_evaded(acertoNEvadido);
            test_classifier_after.setFailure_evaded(erroEvadido);
            test_classifier_after.setFailure_not_evaded(erroNEvadido);
            test_classifier_after.setNeuter(intervaloEvadido + intervaloNaoEvadido);
            test_classifier_after.setNeuter_evaded(intervaloEvadido);
            test_classifier_after.setNeuter_not_evaded(intervaloNaoEvadido);
            test_classifier_after.setEnd(end_test_classifier);
            test_classifier_after.setTime_seconds((int) segundos);
            test_classifier_after.setResult(TestClassifier.RESULT_SUCCESS);

            System.out.println("\n\n");
            System.out.println("-- Sucesso: " + test_classifier_after.getSuccess());
            System.out.println("-- Failuere: " + test_classifier_after.getFailure());
            System.out.println("-- Intervalo: " + test_classifier_after.getNeuter());
            System.out.println("-- Sucesso Evadido: " + test_classifier_after.getSuccess_evaded());
            System.out.println("-- Failuere Evadido: " + test_classifier_after.getFailure_evaded());
            System.out.println("-- Sucesso Não Evadido: " + test_classifier_after.getSuccess_not_evaded());
            System.out.println("-- Failuere Não Evadido: " + test_classifier_after.getFailure_not_evaded());
            System.out.println("-- Intervalo Evadido: " + test_classifier_after.getNeuter_evaded());
            System.out.println("-- Intervalo  Não Evadido: " + test_classifier_after.getNeuter_not_evaded());
            System.out.println("-- Total: " + (test_classifier_after.getSuccess() + test_classifier_after.getFailure() + test_classifier_after.getNeuter()));

            System.out.println("\n\n");
            //System.out.println("Fim: " + end_test_classifier);
            //System.out.println("Tempo percorrido: " + segundos + " segundos.");

            //Calendar datetime = Calendar.getInstance();

            test_classifier_after.setPeriodCalculation(period_calculation);
            testClassifierRepository.save(test_classifier_after);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            Date end_test_classifier = new Date();
            long horas = (start_test_classifier.getTime() - end_test_classifier.getTime()) / 3600000;
            long minutos = (start_test_classifier.getTime() - end_test_classifier.getTime() - horas * 3600000) / 60000;
            long segundos = (start_test_classifier.getTime() - end_test_classifier.getTime() - minutos * 3600000) / 60000;

            TestClassifier test_classifier_error = testClassifierRepository.findByPeriodCalculation(1);
            test_classifier_error.setPeriodCalculation(0);
            test_classifier_error.setEnd(end_test_classifier);
            test_classifier_error.setTime_seconds((int) segundos);
            testClassifierRepository.save(test_classifier_error);
        }
    }

    public List Combinations(List<Variable> variables) {
        List<SortedSet<Comparable>> allCombList = new ArrayList<SortedSet<Comparable>>(); //aqui vai ficar a resposta

        for (Variable nstatus : variables) {
            allCombList.add(new TreeSet<Comparable>(Arrays.asList(nstatus.getId()))); //insiro a combinação "1 a 1" de cada item
        }

        for (int nivel = 1; nivel < variables.size(); nivel++) {
            List<SortedSet<Comparable>> statusAntes = new ArrayList<SortedSet<Comparable>>(allCombList); //crio uma cópia para poder não iterar sobre o que já foi

            for (Set<Comparable> antes : statusAntes) {
                SortedSet<Comparable> novo = new TreeSet<Comparable>(antes); //para manter ordenado os objetos dentro do set
                novo.add(variables.get(nivel).getId());
                if (!allCombList.contains(novo)) { //testo para ver se não está repetido
                    allCombList.add(novo);
                }
            }
        }

        Collections.sort(allCombList, new Comparator<SortedSet<Comparable>>() { //aqui só para organizar a saída de modo "bonitinho"
            @Override
            public int compare(SortedSet<Comparable> o1, SortedSet<Comparable> o2) {
                int sizeComp = o1.size() - o2.size();
                if (sizeComp == 0) {
                    Iterator<Comparable> o1iIterator = o1.iterator();
                    Iterator<Comparable> o2iIterator = o2.iterator();
                    while (sizeComp == 0 && o1iIterator.hasNext()) {
                        sizeComp = o1iIterator.next().compareTo(o2iIterator.next());
                    }
                }
                return sizeComp;
            }
        });

        return allCombList;
    }

}
