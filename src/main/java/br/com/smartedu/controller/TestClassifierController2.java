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
@RequestMapping("/test-classifier2")
public class TestClassifierController2 {

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
    private CourseRepository courseRepository;

    private ClassifierController classifierController = new ClassifierController();
    private DataBaseController dataBaseController = new DataBaseController();

    @Autowired
    private ClassifyController classifyController;

    @GetMapping("/")
    public ResponseEntity classify() throws Exception {
        try {
            base();
            setBestBase();
            classifyBestBase();
            setPattern();
            return new ResponseEntity("Success in making classification!", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/base")
    public ResponseEntity base() throws Exception {
        List<Classifier> classifiersList = classifierRepository.findByUseClassify(1);
        List<Variable> variablesList = variableRepository.findByUseClassify(1);
        List<Course> coursesList = courseRepository.findByUseClassify(1);

        int period_calculation = testClassifierRepository.findMaxPeriodCalculationByType(TestClassifier.TEST_BASE);
        weka.classifiers.Classifier[] wekaClassifiers = classifierController.getWekaClassifiers(classifiersList);

        try {
            for (Course course : coursesList) {
                int position_classifier = 0;
                for (weka.classifiers.Classifier wekaClassifier : wekaClassifiers) {
                    if (wekaClassifier != null) {
                        for (Variable variable : variablesList) {
                            List<Variable> variables = new ArrayList<>();
                            variables.add(variable);
                            Instances dataSet = dataBaseController.getDataSet(variables, course.getId(), dataBaseController.TRAINING);
                            classifyController.ClassifyTraining(classifiersList.get(position_classifier), wekaClassifier, dataSet, course, variables, TestClassifier.TEST_BASE, period_calculation + 1);
                        }
                    }
                    position_classifier++;
                }
            }
            return new ResponseEntity("Success in making the basic classification!", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2ª PASSO
    // SELECIONA OS 3 CLASSIFICADORES E AS 3 MELHORES VARIAVEIS E SETA COMO PADRÕES DE TEST
    @GetMapping("/set-best-base")
    public ResponseEntity setBestBase() throws Exception {
        List<Course> coursesList = courseRepository.findByUseClassify(1);
        int period_calculation = classifyRepository.findMaxPeriodCalculation();
        try {
            for (Course course : coursesList) {
                List<Classify> classifys = course.getClassify();
                List<Classifier> best_classifiers = classifierRepository.findTopXClassifiersByCourse(course.getId(), 3);

                for (Classifier classifier : best_classifiers) {
                    Classify classify = new Classify();
                    List<Variable> best_variable_by_classifier = variableRepository.findTopXVariableByCourseAndClassifier(course.getId(), classifier.getId(), 7);
                    classify.setClassifier(classifier);
                    classify.setVariable(best_variable_by_classifier);
                    classify.setPeriodCalculation(period_calculation + 1);
                    classify = classifyRepository.save(classify);
                    classifys.add(classify);
                }
                course.setClassify(classifys);
                courseRepository.save(course);
            }
            return new ResponseEntity("Success in set best base classification!", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 3ª PASSO
    // GERA OS TESTE COM AS VARIAVEIS E CLASSIFICADORES QUE SÃO PADRÃO
    @GetMapping("/classify-best-base")
    public ResponseEntity classifyBestBase() throws Exception {
        List<Course> coursesList = courseRepository.findByUseClassify(1);
        int period_calculation = testClassifierRepository.findMaxPeriodCalculationByType(TestClassifier.TEST_PATTERN);

        try {
            for (Course course : coursesList) {
                List<Classify> classifys = classifyRepository.findByCourseAndMaxPeriodCalcularion(course.getId());
                for (Classify classify : classifys) {
                    weka.classifiers.Classifier classifier = classifierController.NewClassifier(classify.getClassifier());
                    List combinations = classifyController.Combinations(classify.getVariable(), 3);
                    if (classifier != null) {
                        for (int p = 0; p < combinations.size(); p++) {
                            List<Variable> newVariables = new ArrayList<>();
                            String[] s_combinations = combinations.get(p).toString().replace("[", "").replace("]", "").replace(" ", "").split(",");
                            for (int t = 0; t < s_combinations.length; t++) {
                                newVariables.add(variableRepository.findOne(Long.parseLong(s_combinations[t])));
                            }
                            Instances dataSet = dataBaseController.getDataSet(newVariables, course.getId(), dataBaseController.TRAINING);
                            classifyController.ClassifyTraining(classify.getClassifier(), classifier, dataSet, course, newVariables, TestClassifier.TEST_PATTERN, period_calculation + 1);
                        }
                    }
                }
            }
            return new ResponseEntity("Success in set best base classification!", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 4ª PASSO
    // SETAR MELHOR TESTE
    @GetMapping("/set-pattern")
    public ResponseEntity setPattern() throws Exception {
        List<Course> coursesList = courseRepository.findByUseClassify(1);
        int period_calculation = testClassifierRepository.findMaxPeriodCalculationByType(TestClassifier.TEST_PATTERN);

        try {
            for (Course course : coursesList) {
                TestClassifier testClassifier = testClassifierRepository.findTop1ByCourseAndPeriodCalculationOrderBySuccessDesc(course, period_calculation);
                weka.classifiers.Classifier classifier = classifierController.NewClassifier(testClassifier.getClassifier());

                Instances dataSetTraining = dataBaseController.getDataSet(testClassifier.getVariable(), course.getId(), dataBaseController.TRAINING);
                List<Student> students = studentRepository.findByCourse(course.getId());
                classifyController.ClassifyTestAll(testClassifier, classifier, dataSetTraining, students);

                Instances dataSetTest = dataBaseController.getDataSet(testClassifier.getVariable(), course.getId(), dataBaseController.TEST);
                List<Student> students_not_evaded = studentRepository.findByCourseTest(course.getId());
                classifyController.ClassifyTest(testClassifier, classifier, dataSetTraining, dataSetTest, students_not_evaded);

                testClassifier.setType(TestClassifier.PATTERN_RESULT);
                testClassifierRepository.save(testClassifier);
            }

            return new ResponseEntity("Success in set best base classification!", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
