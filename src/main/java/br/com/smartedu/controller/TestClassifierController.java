package br.com.smartedu.controller;

import br.com.smartedu.model.*;
import br.com.smartedu.repository.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weka.classifiers.Classifier;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
@RequestMapping("/test-classifier")
public class TestClassifierController {

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
    private ClassifyController classifyController = new ClassifyController();

    @GetMapping("/")
    public ResponseEntity classify(@RequestBody Retorno retorno) throws Exception {

        return new ResponseEntity(1, HttpStatus.OK);
    }

    // 1ª PASSO
    // GERA OS TESTES COM TODAS OS CLASSIFICADORES E TODAS AS VARIAVEIS
    @GetMapping("/base")
    public ResponseEntity base() throws Exception {
        List<br.com.smartedu.model.Classifier> classifiersList = classifierRepository.findByUseClassify(1);
        List<Variable> variablesList = variableRepository.findByUseClassify(1);
        List<Course> coursesList = courseRepository.findByUseClassify(1);

        int period_calculation = testClassifierRepository.findMaxPeriodCalculationByType(TestClassifier.TEST_BASE);

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

                        Instances dataSet = dataBaseController.getDataSet(variables, course.getId(), dataBaseController.TRAINING);
                        classifyController.ClassifyTraining(classificador, dataSet, students, course, variables, TestClassifier.TEST_BASE, period_calculation + 1, 0);
                    }
                }
            }
        }
        return new ResponseEntity(1, HttpStatus.OK);
    }

    // 2ª PASSO
    // SELECIONA OS 3 CLASSIFICADORES E AS 3 MELHORES VARIAVEIS E SETA COMO PADRÕES DE TEST
    @GetMapping("/set-pattern-test")
    public ResponseEntity setPatternTest() throws Exception {
        List<Course> coursesList = courseRepository.findByUseClassify(1);

        int period_calculation = testClassifierRepository.findMaxPeriodCalculationByType(TestClassifier.TEST_BASE);

        for (Course course : coursesList) {
            List<Classify> classifys = course.getClassify();
            List<br.com.smartedu.model.Classifier> best_classifiers = classifierRepository.findTopXClassifiersByCourse(course.getId(), 3);
            System.out.println(best_classifiers);

            for (br.com.smartedu.model.Classifier classifier : best_classifiers) {
                Classify classify = new Classify();

                List<Variable> best_variable_by_classifier = variableRepository.findTopXVariableByCourseAndClassifier(course.getId(), classifier.getId(), 3);
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

    // 3ª PASSO
    // GERA OS TESTE COM AS VARIAVEIS E CLASSIFICADORES QUE SÃO PADRÃO
    @GetMapping("/pattern-test")
    public ResponseEntity classificarAllPatternTest() throws Exception {
        List<Course> coursesList = courseRepository.findByUseClassify(1);

        int period_calculation = testClassifierRepository.findMaxPeriodCalculationByType(TestClassifier.TEST_PATTERN);

        for (Course course : coursesList) {
            System.out.println("\n\n-------------------------- NOVA CLASSIFICACAO --------------------------");
            System.out.println("Curso: " + course.getName());
            List<Classify> classifys = classifyRepository.findByCourseAndMaxPeriodCalcularion(course.getId());
            List<Student> students = studentRepository.findByCourse(course.getId());

            System.out.println("\n\nClassifys: " + classifys);

            for (Classify classify : classifys) {
                Classifier classificador = classifierController.NewClassifier(classify.getClassifier());

                List combinations = classifyController.Combinations(classify.getVariable());
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

                        Instances dataSet = dataBaseController.getDataSet(newVariables, course.getId(), dataBaseController.TRAINING);
                        classifyController.ClassifyTraining(classificador, dataSet, students, course, newVariables, TestClassifier.TEST_PATTERN, period_calculation + 1, 0);
                    }
                }
            }
        }
        return new ResponseEntity(1, HttpStatus.OK);
    }

    // 4ª PASSO
    // SETAR MELHOR TESTE
    @GetMapping("/set-pattern")
    public ResponseEntity setPattern() throws Exception {
        List<Course> coursesList = courseRepository.findByUseClassify(1);
        int period_calculation = testClassifierRepository.findMaxPeriodCalculationByType(TestClassifier.TEST_PATTERN);

        for (Course course : coursesList) {
            TestClassifier testClassifier = testClassifierRepository.findTop1ByCourseAndPeriodCalculationOrderBySuccessDesc(course, period_calculation);
            //testClassifier.setType(TestClassifier.PATTERN_TEST);
            
            System.out.println("Id: " + testClassifier.getId());
            System.out.println("Curso: " + course.getName());
            System.out.println("Classificador: " + testClassifier.getClassifier().getName());
            System.out.println("Sucesso: " + testClassifier.getSuccess());
            System.out.println("Falha: " + testClassifier.getFailure());

            Classifier classificador = classifierController.NewClassifier(testClassifier.getClassifier());
            Instances dataSetTraining = dataBaseController.getDataSet(testClassifier.getVariable(), course.getId(), dataBaseController.TRAINING);
            Instances dataSetTest = dataBaseController.getDataSet(testClassifier.getVariable(), course.getId(), dataBaseController.TEST);
            
            List<Student> students = studentRepository.findByCourseTest(course.getId());
            
            
            System.out.println("\n\nStudens: "+students.size() + " --- dataSetTest: "+dataSetTest.size() + " --- dataSetTraining: "+dataSetTraining.size());
            classifyController.ClassifyTest(testClassifier, classificador, dataSetTraining, dataSetTest, students);

            testClassifier.setType(TestClassifier.PATTERN_RESULT);
            testClassifierRepository.save(testClassifier);
        }

        return new ResponseEntity(1, HttpStatus.OK);
    }

    @GetMapping("/test-one")
    public ResponseEntity classificarOneTest(@RequestBody Retorno retorno) throws Exception {
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

                        Instances dataSet = dataBaseController.getDataSet(newVariables, course.getId(), dataBaseController.TRAINING);
                        classifyController.ClassifyTraining(classificador, dataSet, students, course, variablesList, TestClassifier.TEST_ALL, period_calculation + 1, 0);
                    }
                }
            }

        }
        return new ResponseEntity(1, HttpStatus.OK);
    }
}
