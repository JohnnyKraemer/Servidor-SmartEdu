package br.com.smartedu.controller;

import br.com.smartedu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-classifier-period")
public class TestClassifierPeriodController {

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

    /*
    @GetMapping("/")
    public ResponseEntity classify(@RequestBody Retorno retorno) throws Exception {

        return new ResponseEntity(1, HttpStatus.OK);
    }

    // 1ª PASSO
    // GERA OS TESTES COM TODAS OS CLASSIFICADORES E TODAS AS VARIAVEIS
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
                Classifier[] classificadores = classifierController.getWekaClassifiers(classifiersList);

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
                                    ClassifyTraining(classificador, dataSet, students, course, variables, TestClassifier.TEST_PERIOD_BASE, period_calculation + 1, period);
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

    // 2ª PASSO
    // SELECIONA OS 3 CLASSIFICADORES E AS 3 MELHORES VARIAVEIS E SETA COMO PADRÕES DE TEST
    @GetMapping("/set-pattern-period")
    public ResponseEntity setPatternPeriod() throws Exception {
        basePeriod();

        List<Course> coursesList = courseRepository.findByUseClassify(1);

        for (Course course : coursesList) {
            for (int period = 1; period <= course.getTotal_periodos(); period++) {
                int period_calculation = testClassifierRepository.findMaxPeriodCalculationByCourseAndPeriod(course.getId(), period);

                List<Classify> classifys = course.getClassify();

                List<br.com.smartedu.model.Classifier> best_classifiers = classifierRepository.findTop3ClassifiersByCourseByPeriod(course.getId(), period);
                System.out.println(best_classifiers);

                for (br.com.smartedu.model.Classifier classifier : best_classifiers) {
                    Classify classify = new Classify();

                    List<Variable> best_variable_by_classifier = variableRepository.findTopXVariableByCourseAndClassifierAndPeriod(course.getId(), classifier.getId(), period, 3);
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
     */
}
