package br.com.smartedu.controller;

import br.com.smartedu.model.*;
import br.com.smartedu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weka.core.Instances;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test-classifier")
public class TestClassifierController {

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

    @Autowired
    private ClassifyController classifyController;

    @GetMapping("/")
    public ResponseEntity classify() throws Exception {
        Situation situation = situationRepository.findBySituationLong("Formado");
        situation.setSituationShort("Formado");
        //situation.setSituationShort("Não Evadido");
        situationRepository.save(situation);

        base();
        setBestBase();
        classifyBestBase();
        setPattern();

        Situation situation1 = situationRepository.findBySituationLong("Formado");
        situation1.setSituationShort("Formado");
        //situation.setSituationShort("Não Evadido");
        situationRepository.save(situation1);

        return new ResponseEntity(1, HttpStatus.OK);
    }

    @GetMapping("/after-base")
    public ResponseEntity classifyAfterBase() throws Exception {
        setBestBase();
        classifyBestBase();
        setPattern();
        return new ResponseEntity(1, HttpStatus.OK);
    }

    // 1ª PASSO
    // GERA OS TESTES COM TODAS OS CLASSIFICADORES E TODAS AS VARIAVEIS
    @GetMapping("/base")
    public ResponseEntity base() throws Exception {
        System.out.println("\n\n-----------------------------------------------------------");
        System.out.println("------------------------------ TEST BASE --------------------------");
        System.out.println("---------------------------------------------------------------");

        List<Classifier> classifiersList = classifierRepository.findByUseClassify(1);
        List<Variable> variablesList = variableRepository.findByUseClassify(1);
        List<Course> coursesList = courseRepository.findByUseClassify(1);

        int period_calculation = testClassifierRepository.findMaxPeriodCalculationByType(TestClassifier.TEST_BASE);
        int position_course = 1;
        weka.classifiers.Classifier[] classificadores = classifierController.getWekaClassifiers(classifiersList);

        for (Course course : coursesList) {
            System.out.println("\n\nCourse " + position_course + " of " + coursesList.size() + " : " + course.getName());

            int position_classifier = 1;
            for (weka.classifiers.Classifier classificador : classificadores) {
                System.out.println("Classifier " + position_classifier + " of " + classificadores.length + " : " + classificador.getClass().getSimpleName());
                if (classificador != null) {
                    int position_variable = 1;
                    for (Variable variable : variablesList) {
                        List<Variable> variables = new ArrayList<>();
                        variables.add(variable);

                        Instances dataSet = dataBaseController.getDataSet(variables, course.getId(), dataBaseController.TRAINING);
                        TestClassifier test_classifier_after = classifyController.ClassifyTraining(classifiersList.get(position_classifier - 1), classificador, dataSet, course, variables, TestClassifier.TEST_BASE, period_calculation + 1);

                        NumberFormat formatarFloat = new DecimalFormat("#.##");
                        float sucesso = (float) ((test_classifier_after.getSuccess() * 100.00) / (test_classifier_after.getSuccess() + test_classifier_after.getFailure() + test_classifier_after.getNeuter()));
                        sucesso = Float.parseFloat(formatarFloat.format(sucesso).replace(",", "."));

                        System.out.println(position_variable + " of " + variablesList.size() + "  --- Sucesso: "+ sucesso + " --- Time: "+ test_classifier_after.getTime_seconds() + " ---  " + variable.getName());

                        position_variable++;
                    }
                }
                position_classifier++;
            }
            position_course++;
        }
        return new ResponseEntity(1, HttpStatus.OK);
    }

    // 2ª PASSO
    // SELECIONA OS 3 CLASSIFICADORES E AS 3 MELHORES VARIAVEIS E SETA COMO PADRÕES DE TEST
    @GetMapping("/set-best-base")
    public ResponseEntity setBestBase() throws Exception {
        System.out.println("\n\n-----------------------------------------------------------");
        System.out.println("------------------------------ SET BESTS BASE --------------------------");
        System.out.println("---------------------------------------------------------------");

        List<Course> coursesList = courseRepository.findByUseClassify(1);

        int period_calculation = classifyRepository.findMaxPeriodCalculation();
        int position_course = 1;

        for (Course course : coursesList) {
            System.out.println("\n\nCourse " + position_course + " of " + coursesList.size() + " : " + course.getName());
            List<Classify> classifys = course.getClassify();
            List<br.com.smartedu.model.Classifier> best_classifiers = classifierRepository.findTopXClassifiersByCourse(course.getId(), 3);
            int position_classifier = 1;

            for (br.com.smartedu.model.Classifier classifier : best_classifiers) {
                Classify classify = new Classify();
                System.out.println("Classifier " + position_classifier + " of " + best_classifiers.size() + " : " + classifier.getName());

                List<Variable> best_variable_by_classifier = variableRepository.findTopXVariableByCourseAndClassifier(course.getId(), classifier.getId(), 7);
                System.out.println("Variables: " + best_variable_by_classifier);

                classify.setClassifier(classifier);
                classify.setVariable(best_variable_by_classifier);
                classify.setPeriodCalculation(period_calculation + 1);
                classify = classifyRepository.save(classify);
                classifys.add(classify);
                position_classifier++;
            }

            course.setClassify(classifys);
            courseRepository.save(course);
            position_course++;
        }

        return new ResponseEntity(1, HttpStatus.OK);
    }

    // 3ª PASSO
    // GERA OS TESTE COM AS VARIAVEIS E CLASSIFICADORES QUE SÃO PADRÃO
    @GetMapping("/classify-best-base")
    public ResponseEntity classifyBestBase() throws Exception {
        System.out.println("\n\n-----------------------------------------------------------");
        System.out.println("------------------------------ CLASSIFY BEST BASE --------------------------");
        System.out.println("---------------------------------------------------------------");

        List<Course> coursesList = courseRepository.findByUseClassify(1);
        int period_calculation = testClassifierRepository.findMaxPeriodCalculationByType(TestClassifier.TEST_PATTERN);
        int period_calculation_base = testClassifierRepository.findMaxPeriodCalculationByType(TestClassifier.TEST_BASE);
        int position_course = 1;

        for (Course course : coursesList) {
            System.out.println("\n\nCourse " + position_course + " of " + coursesList.size() + " : " + course.getName());
            List<Classify> classifys = classifyRepository.findByCourseAndMaxPeriodCalcularion(course.getId());
            //List<Student> students = studentRepository.findByCourse(course.getId());
            int position_classifier = 1;
            for (Classify classify : classifys) {
                weka.classifiers.Classifier classificador = classifierController.NewClassifier(classify.getClassifier());
                System.out.println("Classifier " + position_classifier + " of " + classifys.size() + " : " + classificador.getClass().getSimpleName());
                List combinations = classifyController.Combinations(classify.getVariable(), 7);
                if (classificador != null) {
                    int position_combination = 1;
                    for (int p = 0; p < combinations.size(); p++) {
                        List<Variable> newVariables = new ArrayList<>();

                        String[] s_combinations = combinations.get(p).toString().replace("[", "").replace("]", "").replace(" ", "").split(",");
                        for (int t = 0; t < s_combinations.length; t++) {
                            newVariables.add(variableRepository.findOne(Long.parseLong(s_combinations[t])));
                        }

                        Instances dataSet = dataBaseController.getDataSet(newVariables, course.getId(), dataBaseController.TRAINING);
                        TestClassifier test_classifier_after = classifyController.ClassifyTraining(classify.getClassifier(), classificador, dataSet, course, newVariables, TestClassifier.TEST_PATTERN, period_calculation + 1);

                        NumberFormat formatarFloat = new DecimalFormat("#.##");
                        float sucesso = (float) ((test_classifier_after.getSuccess() * 100.00) / (test_classifier_after.getSuccess() + test_classifier_after.getFailure() + test_classifier_after.getNeuter()));
                        sucesso = Float.parseFloat(formatarFloat.format(sucesso).replace(",", "."));

                        System.out.println(position_combination + " of " + combinations.size() + " --- Sucesso: "+ sucesso + " --- Time: "+ test_classifier_after.getTime_seconds() + " ---  " + newVariables.toString());
                        position_combination++;
                    }
                }
                position_classifier++;
            }
            position_course++;
        }
        return new ResponseEntity(1, HttpStatus.OK);
    }

    // 4ª PASSO
    // SETAR MELHOR TESTE
    @GetMapping("/set-pattern")
    public ResponseEntity setPattern() throws Exception {
        System.out.println("\n\n-----------------------------------------------------------");
        System.out.println("------------------------------ SET PATTERN --------------------------");
        System.out.println("---------------------------------------------------------------");

        List<Course> coursesList = courseRepository.findByUseClassify(1);
        int period_calculation = testClassifierRepository.findMaxPeriodCalculationByType(TestClassifier.TEST_PATTERN);

        for (Course course : coursesList) {
            TestClassifier testClassifier = testClassifierRepository.findTop1ByCourseAndPeriodCalculationOrderBySuccessDesc(course, period_calculation);
            //testClassifier.setType(TestClassifier.PATTERN_TEST);

            System.out.println("\n\nId: " + testClassifier.getId());
            System.out.println("Curso: " + course.getName());
            System.out.println("Classificador: " + testClassifier.getClassifier().getName());
            System.out.println("Sucesso: " + testClassifier.getSuccess());
            System.out.println("Falha: " + testClassifier.getFailure());

            weka.classifiers.Classifier classificador = classifierController.NewClassifier(testClassifier.getClassifier());

            Instances dataSet = dataBaseController.getDataSet(testClassifier.getVariable(), course.getId(), dataBaseController.TRAINING);
            List<Student> students = studentRepository.findByCourse(course.getId());
            classifyController.ClassifyTestAll(testClassifier, classificador, dataSet, students);

            //System.out.println("\nId: " + testClassifier.getId());
            //System.out.println("Curso: " + course.getName());
            //System.out.println("Classificador: " + testClassifier.getClassifier().getName());
            //System.out.println("Sucesso: " + testClassifier.getSuccess());
            //System.out.println("Falha: " + testClassifier.getFailure());

            Instances dataSetTraining = dataBaseController.getDataSet(testClassifier.getVariable(), course.getId(), dataBaseController.TRAINING);
            Instances dataSetTest = dataBaseController.getDataSet(testClassifier.getVariable(), course.getId(), dataBaseController.TEST);
            List<Student> students_not_evaded = studentRepository.findByCourseTest(course.getId());
            weka.classifiers.Classifier tree = classifierController.NewClassifier(testClassifier.getClassifier());

            System.out.println("Tamanho -- students_not_evaded: " + students_not_evaded.size() + " -- DataSetTeste: " + dataSetTest.size() + " -- DataSetTreino: " + dataSetTraining.size());
            classifyController.ClassifyTest(testClassifier, tree, dataSetTraining, dataSetTest, students_not_evaded);

            testClassifier.setType(TestClassifier.PATTERN_RESULT);
            testClassifierRepository.save(testClassifier);
        }

        return new ResponseEntity(1, HttpStatus.OK);
    }

    @GetMapping("/delete-tests")
    public ResponseEntity deleteTests() throws Exception {
        List<TestClassifier> testClassifiers = testClassifierRepository.findByIdGreaterThan( Long.parseLong("560"));
        for(TestClassifier test : testClassifiers){
            System.out.println(test.getId());
            //List<Probability> probabilities = probabilityRepository.findAllByTestClassifier(test);
            //if(probabilities != null){
            //    probabilityRepository.delete(probabilities);
            //}
            testClassifierRepository.delete(test);
        }
        return new ResponseEntity(1, HttpStatus.OK);
    }

    /*
    @GetMapping("/test-one")
    public ResponseEntity classificarOneTest(@RequestBody Retorno retorno) throws Exception {
        List<br.com.smartedu.model.Classifier> classifiersList = retorno.getWekaClassifiers();
        List<Variable> variablesList = retorno.getVariables();
        List<Course> coursesList = retorno.getCourses();

        int period_calculation = testClassifierRepository.findMaxPeriodCalculation();

        for (Course course : coursesList) {
            System.out.println("\n\n-------------------------- NOVA CLASSIFICACAO --------------------------");
            System.out.println("Curso: " + course.getName());
            Classifier[] classificadores = classifierController.getWekaClassifiers(classifiersList);
            List<Student> students = studentRepository.findByCourse(course.getId());

            for (Classifier classificador : classificadores) {
                if (classificador != null) {
                    System.out.println("Classificador: " + classificador.getClass().getSimpleName());

                    Instances dataSet = dataBaseController.getDataSet(variablesList, course.getId(), dataBaseController.TRAINING);
                    classifyController.ClassifyTraining(classificador, dataSet, students, course, variablesList, TestClassifier.TEST_SINGLE, period_calculation + 1, 0);
                }
            }

        }
        return new ResponseEntity(1, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity classificarAll() throws Exception {
        List<br.com.smartedu.model.Classifier> classifiersList = classifierRepository.findByUseClassify(1);
        List<Variable> variablesList = variableRepository.findByUseClassify(1);
        List<Course> coursesList = courseRepository.findByUseClassify(1);

        List combinations = classifyController.Combinations(variablesList);

        int period_calculation = testClassifierRepository.findMaxPeriodCalculation();

        //System.out.println(combinations);
        System.out.println("Quantidade de possibilidades: " + combinations.size());

        for (Course course : coursesList) {
            System.out.println("\n\n-------------------------- NOVA CLASSIFICACAO --------------------------");
            System.out.println("Curso: " + course.getName());
            Classifier[] classificadores = classifierController.getWekaClassifiers(classifiersList);
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

                        Instances dataSet = dataBaseController.getDataSet(newVariables, course.getId(), dataBaseController.TRAINING);
                        classifyController.ClassifyTraining(classificador, dataSet, students, course, variablesList, TestClassifier.TEST_ALL, period_calculation + 1, 0);
                    }
                }
            }

        }
        return new ResponseEntity(1, HttpStatus.OK);
    }
    */
}
