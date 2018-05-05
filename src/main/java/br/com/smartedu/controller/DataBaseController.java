/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.smartedu.controller;

import br.com.smartedu.model.Variable;
import org.springframework.stereotype.Controller;
import weka.core.Instances;
import weka.experiment.DatabaseUtils;
import weka.experiment.InstanceQuery;

import java.io.File;
import java.util.List;

@Controller
public class DataBaseController {
    
    public static final int TEST = 0;
    public static final int TRAINING = 1;

    public InstanceQuery ConectaBanco() throws Exception {
        DatabaseUtils databaseUtils = new DatabaseUtils(new File("/DatabaseUtils.props"));
        InstanceQuery query = new InstanceQuery();
        query.setUsername("smartedu");
        query.setPassword("smartedu");
        return query;
    }

    public String montaSQLTraining(List<Variable> variaveis, long idCurso) throws Exception {
        //System.out.println("------------------------------------------ Data Set Teste ------------------------------------------");

        String sql = "SELECT student.id,";
        for (Variable variable : variaveis) {
            sql += " \n" + variable.getTable() + "." + variable.getName_database() + ",";
        }

        sql += "\nsituation.situation_short\n";

        sql += "FROM student\n"
                + "LEFT JOIN detail\n"
                + "ON detail.student_id = student.id\n"
                + "LEFT JOIN situation\n"
                + "ON situation.id = detail.situation_id\n"
                + "LEFT JOIN course\n"
                + "ON course.id = student.course_id\n"
                + "LEFT JOIN campus\n"
                + "ON campus.id = course.campus_id\n"
                + "WHERE detail.loading_period = (SELECT MAX(detail.loading_period) FROM detail WHERE detail.student_id = student.id)\n"
                + "AND course.id =  " + idCurso + "\n"
                + "AND (situation.situation_short LIKE 'Evadido' OR situation.situation_short LIKE 'Não Evadido')\n"
                + "GROUP BY student.id\n"
                + "ORDER BY student.id";

        //System.out.println(sql);
        return sql;
    }
    
    public String montaSQLTest(List<Variable> variaveis, long idCurso) throws Exception {
        //System.out.println("------------------------------------------ Data Set Teste ------------------------------------------");

        String sql = "SELECT student.id,";
        for (Variable variable : variaveis) {
            sql += " \n" + variable.getTable() + "." + variable.getName_database() + ",";
        }

        sql += "\nsituation.situation_short\n";

        sql += "FROM student\n"
                + "LEFT JOIN detail\n"
                + "ON detail.student_id = student.id\n"
                + "LEFT JOIN situation\n"
                + "ON situation.id = detail.situation_id\n"
                + "LEFT JOIN course\n"
                + "ON course.id = student.course_id\n"
                + "LEFT JOIN campus\n"
                + "ON campus.id = course.campus_id\n"
                + "WHERE detail.loading_period = (SELECT MAX(detail.loading_period) FROM detail WHERE detail.student_id = student.id)\n"
                + "AND course.id =  " + idCurso + "\n"
                + "AND (situation.situation_short LIKE 'Não Evadido')\n"
                + "GROUP BY student.id\n"
                + "ORDER BY student.id";

        //System.out.println(sql);
        return sql;
    }

    /*
    public String montaSQLByPeriod(List<Variable> variaveis, long idCurso, int period) throws Exception {
        //System.out.println("------------------------------------------ Data Set Teste ------------------------------------------");

        String sql = "SELECT student.id,";
        for (Variable variable : variaveis) {
            sql += " \n" + variable.getTable() + "." + variable.getName_database() + ",";
        }

        sql += "\nsituation.situation_short\n";

        sql += "FROM student\n"
                + "LEFT JOIN student_detail\n"
                + "ON student.id = student_detail.student_id\n"
                + "LEFT JOIN detail\n"
                + "ON detail.id = student_detail.detail_id\n"
                + "LEFT JOIN situation\n"
                + "ON situation.id = detail.situation_id\n"
                + "LEFT JOIN course\n"
                + "ON course.id = student.course_id\n"
                + "LEFT JOIN campus\n"
                + "ON campus.id = course.campus_id\n"
                + "WHERE detail.loading_period = (SELECT MAX(detail.loading_period) FROM detail WHERE detail.student_id = student.id)\n"
                + "AND course.id =  " + idCurso + "\n"
                + "AND (situation.situation_short LIKE 'Evadido' OR situation.situation_short LIKE 'Não Evadido')\n"
                + "GROUP BY student.id\n"
                + "ORDER BY student.id";

        //System.out.println(sql);
        return sql;
    }
    */

    public Instances getDataSet(List<Variable> variaveis, long idCurso, int type) throws Exception {
        InstanceQuery query = ConectaBanco();
        
        if(type == TEST){
           query.setQuery(montaSQLTest(variaveis, idCurso)); 
        }else if(type == TRAINING){
            query.setQuery(montaSQLTraining(variaveis, idCurso)); 
        }
        
        Instances dataSet = query.retrieveInstances();

        dataSet.setClassIndex(dataSet.numAttributes() - 1);

        String col_Discretize = "";
        String col_NumericToNominal = "";
        int position = 2;
        for (Variable variable : variaveis) {
            if (variable.getDiscretize() == 1) {
                if ("".equals(col_Discretize)) {
                    col_Discretize += "" + position;
                } else {
                    col_Discretize += "," + position;
                }
            }

            if (variable.getNominal() == 1) {
                if ("".equals(col_NumericToNominal)) {
                    col_NumericToNominal += "" + position;
                } else {
                    col_NumericToNominal += "," + position;
                }
            }
            position++;
        }

        Discretize discretize = new Discretize();
        dataSet = discretize.discretize(dataSet, col_Discretize, col_NumericToNominal, false);
        return dataSet;
    }

    /*
    public Instances getDataSetByPeriod(List<Variable> variaveis, long idCurso, int period) throws Exception {
        InstanceQuery query = ConectaBanco();
        query.setQuery(montaSQLByPeriod(variaveis, idCurso, period));
        Instances dataSet = query.retrieveInstances();

        dataSet.setClassIndex(dataSet.numAttributes() - 1);

        String col_Discretize = "";
        String col_NumericToNominal = "";
        int position = 2;
        for (Variable variable : variaveis) {
            if (variable.getDiscretize() == 1) {
                if ("".equals(col_Discretize)) {
                    col_Discretize += "" + position;
                } else {
                    col_Discretize += "," + position;
                }
            }

            if (variable.getNominal() == 1) {
                if ("".equals(col_NumericToNominal)) {
                    col_NumericToNominal += "" + position;
                } else {
                    col_NumericToNominal += "," + position;
                }
            }
            position++;
        }

        Discretize discretize = new Discretize();
        dataSet = discretize.discretize(dataSet, col_Discretize, col_NumericToNominal, false);
        return dataSet;
    }
    */
}
