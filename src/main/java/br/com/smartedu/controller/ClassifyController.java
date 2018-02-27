package br.com.smartedu.controller;

import br.com.smartedu.model.Course;
import br.com.smartedu.model.Probability;
import br.com.smartedu.model.Student;
import br.com.smartedu.model.TestClassifier;
import br.com.smartedu.model.Classify;
import br.com.smartedu.model.Situation;
import br.com.smartedu.model.Variable;
import br.com.smartedu.repository.ClassifierRepository;
import br.com.smartedu.repository.CourseRepository;
import br.com.smartedu.repository.ProbabilityRepository;
import br.com.smartedu.repository.StudentRepository;
import br.com.smartedu.repository.VariableRepository;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import br.com.smartedu.repository.TestClassifierRepository;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.JOptionPane;
import lombok.Data;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import br.com.smartedu.repository.ClassifyRepository;
import br.com.smartedu.repository.SituationRepository;

@Data
@ToString
class Retorno {

    @Getter
    @Setter
    private List<Variable> variables;

    @Getter
    @Setter
    private List<br.com.smartedu.model.Classifier> classifiers;

    @Getter
    @Setter
    private List<Course> courses;

}

@RestController
@RequestMapping("/classify")
public class ClassifyController {

    @Autowired
    private ProbabilityRepository probabilityRepository;

    @Autowired
    private ClassifierRepository classifierRepository;

    @Autowired
    private VariableRepository variableRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestClassifierRepository testClassifierRepository;

    @Autowired
    private ClassifyRepository classifyRepository;

    @Autowired
    private SituationRepository situationRepository;

    @Autowired
    private CourseRepository courseRepository;

    private ClassifierController classifierController = new ClassifierController();
    private DataBaseController dataBaseController = new DataBaseController();

    @PostMapping("/test2")
    public void teste(@RequestBody Retorno retorno) throws Exception {
        System.out.println(retorno.toString());

        //JSONObject jsonObj = new JSONObject(retorno);
        //System.out.println(jsonObj);
        //System.out.println(jsonObj.get("courses"));
        System.out.println(retorno.getClassifiers().get(0).getName());
    }

    @PostMapping("/")
    public ResponseEntity classificar(@RequestBody Retorno retorno) throws Exception {
        List<br.com.smartedu.model.Classifier> classifiersList = retorno.getClassifiers();
        List<Variable> variablesList = retorno.getVariables();
        List<Course> coursesList = retorno.getCourses();

        int period_calculation = testClassifierRepository.findMaxPeriodCalculation();

        for (Course course : coursesList) {
            System.out.println("\n\n-------------------------- NOVA CLASSIFICACAO --------------------------");
            System.out.println("Curso: " + course.getName());
            Classifier[] classificadores = classifierController.GeraClassificadores(classifiersList);
            List<Student> students = studentRepository.findByCourse(course.getId());

            for (Classifier classificador : classificadores) {
                if (classificador != null) {
                    System.out.println("Classificador: " + classificador.getClass().getSimpleName());

                    Instances dataSet = dataBaseController.getDataSet(variablesList, course.getId());
                    Classificador(classificador, dataSet, students, course, variablesList, TestClassifier.TEST_SINGLE, period_calculation + 1, 0);
                }
            }

        }
        return new ResponseEntity(1, HttpStatus.OK);
    }

    @PostMapping("/all")
    public ResponseEntity classificarAll() throws Exception {
        List<br.com.smartedu.model.Classifier> classifiersList = classifierRepository.findByUseClassify(1);
        List<Variable> variablesList = variableRepository.findByUseClassify(1);
        List<Course> coursesList = courseRepository.findByUseClassify(1);

        List combinations = Combinations(variablesList);

        int period_calculation = testClassifierRepository.findMaxPeriodCalculation();

        //System.out.println(combinations);
        System.out.println("Quantidade de possibilidades: " + combinations.size());

        for (Course course : coursesList) {
            System.out.println("\n\n-------------------------- NOVA CLASSIFICACAO --------------------------");
            System.out.println("Curso: " + course.getName());
            Classifier[] classificadores = classifierController.GeraClassificadores(classifiersList);
            List<Student> students = studentRepository.findByCourse(course.getId());

            for (Classifier classificador : classificadores) {
                if (classificador != null) {
                    System.out.println("\n\n-------- Classificador: " + classificador.getClass().getSimpleName() + " --------");

                    for (int p = 0; p < combinations.size(); p++) {
                        List<Variable> newVariables = new ArrayList<>();

                        Date start_combination = new Date();
                        System.out.println("\n\n-- Combinação " + (p + 1) + " de " + (combinations.size() + 1) + ".");

                        String[] s_combinations = combinations.get(p).toString().replace("[", "").replace("]", "").replace(" ", "").split(",");
                        for (int t = 0; t < s_combinations.length; t++) {
                            newVariables.add(variableRepository.findOne(Long.parseLong(s_combinations[t])));
                        }
                        System.out.println("Variaveis: " + newVariables.toString());

                        Instances dataSet = dataBaseController.getDataSet(newVariables, course.getId());
                        Classificador(classificador, dataSet, students, course, variablesList, TestClassifier.TEST_ALL, period_calculation + 1, 0);
                    }

                }
            }

        }
        return new ResponseEntity(1, HttpStatus.OK);
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

    @GetMapping("/pattern")
    public ResponseEntity classificarAllPattern() throws Exception {
        //List<br.com.smartedu.model.Classifier> classifiersList = classifierRepository.findByUseClassify(1);
        //List<Variable> variablesList = variableRepository.findByUseClassify(1);
        List<Course> coursesList = courseRepository.findByUseClassify(1);

        int period_calculation = testClassifierRepository.findMaxPeriodCalculation();

        for (Course course : coursesList) {
            System.out.println("\n\n-------------------------- NOVA CLASSIFICACAO --------------------------");
            System.out.println("Curso: " + course.getName());

            List<Classify> classifys = classifyRepository.findByCourseAndMaxPeriodCalcularion(course.getId());
            List<Student> students = studentRepository.findByCourse(course.getId());

            System.out.println("\n\nClassifys: " + classifys);

            for (Classify classify : classifys) {
                Classifier classificador = classifierController.NewClassifier(classify.getClassifier());

                List combinations = Combinations(classify.getVariable());
                System.out.println("Quantidade de possibilidades: " + combinations.size());

                if (classificador != null) {
                    System.out.println("\n\n-------- Classificador: " + classificador.getClass().getSimpleName() + " --------");

                    for (int p = 0; p < combinations.size(); p++) {
                        List<Variable> newVariables = new ArrayList<>();

                        Date start_combination = new Date();
                        System.out.println("\n\n-- Combinação " + (p + 1) + " de " + combinations.size() + ".");

                        String[] s_combinations = combinations.get(p).toString().replace("[", "").replace("]", "").replace(" ", "").split(",");
                        for (int t = 0; t < s_combinations.length; t++) {
                            newVariables.add(variableRepository.findOne(Long.parseLong(s_combinations[t])));
                        }
                        System.out.println("Variaveis: " + newVariables.toString());

                        Instances dataSet = dataBaseController.getDataSet(newVariables, course.getId());
                        Classificador(classificador, dataSet, students, course, newVariables, TestClassifier.TEST_ALL, period_calculation + 1, 0);
                    }
                }
            }
        }
        return new ResponseEntity(1, HttpStatus.OK);
    }

    @PostMapping("/base")
    public ResponseEntity base() throws Exception {
        List<br.com.smartedu.model.Classifier> classifiersList = classifierRepository.findByUseClassify(1);
        List<Variable> variablesList = variableRepository.findByUseClassify(1);
        List<Course> coursesList = courseRepository.findByUseClassify(1);

        int period_calculation = testClassifierRepository.findMaxPeriodCalculation();

        for (Course course : coursesList) {
            System.out.println("\n\n-------------------------- NOVA CLASSIFICACAO --------------------------");
            System.out.println("Curso: " + course.getName());
            Classifier[] classificadores = classifierController.GeraClassificadores(classifiersList);
            List<Student> students = studentRepository.findByCourse(course.getId());

            for (Classifier classificador : classificadores) {
                if (classificador != null) {
                    System.out.println("\n\nClassificador: " + classificador.getClass().getSimpleName());
                    for (Variable variable : variablesList) {
                        System.out.println("\n\nVariavel: " + variable.getName());
                        List<Variable> variables = new ArrayList<>();
                        variables.add(variable);

                        Instances dataSet = dataBaseController.getDataSet(variables, course.getId());
                        Classificador(classificador, dataSet, students, course, variables, TestClassifier.TEST_BASE, period_calculation + 1, 0);
                    }
                }
            }
        }
        return new ResponseEntity(1, HttpStatus.OK);
    }

    @GetMapping("/setpattern")
    public ResponseEntity setPattern() throws Exception {
        List<Course> coursesList = courseRepository.findByUseClassify(1);

        int period_calculation = classifyRepository.findMaxPeriodCalculation();

        for (Course course : coursesList) {
            List<Classify> classifys = course.getClassify();

            List<br.com.smartedu.model.Classifier> best_classifiers = classifierRepository.findTop3ClassifiersByCourse(course.getId());
            System.out.println(best_classifiers);

            for (br.com.smartedu.model.Classifier classifier : best_classifiers) {
                Classify classify = new Classify();

                List<Variable> best_variable_by_classifier = variableRepository.findTop3VariableByCourseAndClassifier(course.getId(), classifier.getId());
                System.out.println(best_variable_by_classifier);

                classify.setClassifier(classifier);
                classify.setVariable(best_variable_by_classifier);
                classify.setPeriodCalculation(period_calculation + 1);
                classify = classifyRepository.save(classify);
                classifys.add(classify);
            }

            course.setClassify(classifys);
            courseRepository.save(course);
        }

        return new ResponseEntity(1, HttpStatus.OK);
    }

    @GetMapping("/base-period")
    public ResponseEntity basePeriod() throws Exception {
        List<br.com.smartedu.model.Classifier> classifiersList = classifierRepository.findByUseClassify(1);
        List<Variable> variablesList = variableRepository.findByUseClassify(1);
        List<Course> coursesList = courseRepository.findByUseClassify(1);

        Situation situation = situationRepository.findBySituationLong("Regular");
        situation.setSituationShort("Não Evadido");
        situationRepository.save(situation);

        try {
            for (Course course : coursesList) {
                System.out.println("\n\n-------------------------- NOVA CLASSIFICACAO --------------------------");
                System.out.println("Curso: " + course.getName());
                Classifier[] classificadores = classifierController.GeraClassificadores(classifiersList);

                for (int period = 1; period <= course.getTotal_periodos(); period++) {
                    List<Student> students = studentRepository.findByCourseAndPeriod(course.getId(), period);
                    int period_calculation = testClassifierRepository.findMaxPeriodCalculationByCourseAndPeriod(course.getId(), period);

                    if (period == course.getTotal_periodos()) {
                        situation.setSituationShort("Outros");
                        situationRepository.save(situation);
                    }

                    for (Classifier classificador : classificadores) {
                        if (classificador != null) {
                            System.out.println("\n\nClassificador: " + classificador.getClass().getSimpleName());
                            for (Variable variable : variablesList) {
                                if (!"periodo".equals(variable.getName_database())) {
                                    System.out.println("\n\nVariavel: " + variable.getName());
                                    List<Variable> variables = new ArrayList<>();
                                    variables.add(variable);

                                    Instances dataSet = dataBaseController.getDataSetByPeriod(variables, course.getId(), period);

                                    System.out.println("\n\nRegistros Normal: " + students.size() + " Weka: " + dataSet.size());
                                    Classificador(classificador, dataSet, students, course, variables, TestClassifier.TEST_PERIOD_BASE, period_calculation + 1, period);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
            situation = situationRepository.findBySituationLong("Regular");
            situation.setSituationShort("Outros");
            situationRepository.save(situation);
        }

        return new ResponseEntity(1, HttpStatus.OK);
    }

    @GetMapping("/set-pattern-period")
    public ResponseEntity setPatternPeriod() throws Exception {
        List<Course> coursesList = courseRepository.findByUseClassify(1);

        for (Course course : coursesList) {
            for (int period = 1; period <= course.getTotal_periodos(); period++) {
                int period_calculation = testClassifierRepository.findMaxPeriodCalculationByCourseAndPeriod(course.getId(), period);

                List<Classify> classifys = course.getClassify();

                List<br.com.smartedu.model.Classifier> best_classifiers = classifierRepository.findTop3ClassifiersByCourseByPeriod(course.getId(), period);
                System.out.println(best_classifiers);

                for (br.com.smartedu.model.Classifier classifier : best_classifiers) {
                    Classify classify = new Classify();

                    List<Variable> best_variable_by_classifier = variableRepository.findTop3VariableByCourseAndClassifierAndPeriod(course.getId(), classifier.getId(), period);
                    System.out.println(best_variable_by_classifier);

                    classify.setClassifier(classifier);
                    classify.setPeriod(period);
                    classify.setVariable(best_variable_by_classifier);
                    classify.setPeriodCalculation(period_calculation + 1);
                    classify = classifyRepository.save(classify);
                    classifys.add(classify);
                }

                course.setClassify(classifys);
                courseRepository.save(course);
            }

        }

        return new ResponseEntity(1, HttpStatus.OK);
    }

    public void Classificador(weka.classifiers.Classifier tree, Instances dataSet, List<Student> students, Course curso, List<Variable> variaveis, int type, int period_calculation, int period) throws Exception {
        Instances dataSetTeste = new Instances(dataSet);
        dataSetTeste.deleteAttributeAt(0);

        Instances dataSetTreino = new Instances(dataSet);
        dataSetTreino.deleteAttributeAt(0);

        int num_instances = dataSetTeste.numInstances();
        double[] probEvasao = new double[num_instances];
        String[] situacao = new String[num_instances];
        String[] resultado = new String[num_instances];
        int acertoEvadido = 0;
        int erroEvadido = 0;
        int acertoNEvadido = 0;
        int erroNEvadido = 0;

        String st_Classificador = tree.getClass().getName();
        st_Classificador = st_Classificador.replace(".", ";");
        String[] arrayClassificador = st_Classificador.split(";");
        st_Classificador = arrayClassificador[3];

        br.com.smartedu.model.Classifier classificador = classifierRepository.findByName(st_Classificador);

        Date start_test_classifier = new Date();
        System.out.println("Incicio: " + start_test_classifier);

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
        test_classifier_before.setPeriod(period);
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

                String evadir = String.valueOf(eval1.predictions().toArray()[0]);
                String[] arrayValores = evadir.split(" ");

                probEvasao[i] = Double.parseDouble(arrayValores[4]);

                if ("V1".equals(String.valueOf(dataSetTeste.instance(i).toString(dataSetTeste.numAttributes() - 1))) || "Evadido".equals(String.valueOf(dataSetTeste.instance(i).toString(dataSetTeste.numAttributes() - 1)))) {
                    if (probEvasao[i] > 0.5) {
                        acertoEvadido++;
                        resultado[i] = "Acerto"; // Sucesso
                        situacao[i] = "Evadido";
                    } else if (probEvasao[i] <= 0.5) {
                        erroEvadido++;
                        resultado[i] = "Erro"; //Insucesso
                        situacao[i] = "Evadido";
                    }

                } else if ("V3".equals(String.valueOf(dataSetTeste.instance(i).toString(dataSetTeste.numAttributes() - 1))) || "'Não Evadido'".equals(String.valueOf(dataSetTeste.instance(i).toString(dataSetTeste.numAttributes() - 1)))) {
                    if (probEvasao[i] <= 0.5) {
                        acertoNEvadido++;
                        resultado[i] = "Acerto"; //Sucesso
                        situacao[i] = "Não Evadido";
                    } else if (probEvasao[i] > 0.5) {
                        erroNEvadido++;
                        resultado[i] = "Erro"; //Insucesso
                        situacao[i] = "Não Evadido";
                    }
                } else if ("V2".equals(String.valueOf(dataSetTeste.instance(i).toString(dataSetTeste.numAttributes() - 1))) || "Outro".equals(String.valueOf(dataSetTeste.instance(i).toString(dataSetTeste.numAttributes() - 1)))) {
                    resultado[i] = "Outro";
                    situacao[i] = "Outro";
                }
                Probability probabilidade = new Probability();
                probabilidade.setProbability_evasion(probEvasao[i]);
                probabilidade.setState(situacao[i]);

                //TestClassifier testeClassificadorDurante = testClassifierRepository.findByPeriodCalculation(1);
                probabilidade.setTestClassifier(test_classifier_before);
                probabilidade.setStudent(students.get(i));
                //probabilidade = probabilityRepository.save(probabilidade);

                //Student aluno = studentRepository.findOne(Long.parseLong(dataSet.instance(i).toString(0)));
                //Set<Probability> probs = students.get(i).getProbability();
                //probs.add(probabilidade);
                probabilitys.add(probabilidade);

                //System.out.println("\n\nAluno: "+students.get(i).getNome());
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
            test_classifier_after.setSeccess_evaded(acertoEvadido);
            test_classifier_after.setSeccess_not_evaded(acertoNEvadido);
            test_classifier_after.setFailure_evaded(erroEvadido);
            test_classifier_after.setFailure_not_evaded(erroNEvadido);
            test_classifier_after.setEnd(end_test_classifier);
            test_classifier_after.setTime_seconds((int) segundos);
            test_classifier_after.setResult(TestClassifier.RESULT_SUCCESS);

            System.out.println("Fim: " + end_test_classifier);
            System.out.println("Tempo percorrido: " + segundos + " segundos.");

            Calendar datetime = Calendar.getInstance();

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
}
