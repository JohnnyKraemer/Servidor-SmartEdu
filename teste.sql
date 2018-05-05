


Id: 2
Curso: Administração
Classificador: BayesNet
Sucesso: 404
Falha: 689
Tamanho -- Students: 130 -- DataSetTeste: 689 -- DataSetTreino: 1093
Index: 130, Size: 130



SELECT student.*
FROM student
LEFT JOIN student_detail
ON student.id = student_detail.student_id
LEFT JOIN detail
ON detail.id = student_detail.detail_id
LEFT JOIN situation
ON situation.id = detail.situation_id
WHERE detail.id =
      (SELECT  MAX(detail.id)
      FROM student s
      LEFT JOIN student_detail
      ON s.id = student_detail.student_id
      LEFT JOIN detail
      ON detail.id = student_detail.detail_id
      WHERE s.id = student.id)
AND student.course_id = 2
AND (situation.situation_short LIKE 'Evadido' OR situation.situation_short LIKE 'Formado')
ORDER BY student.id;


SELECT student.*
FROM student
LEFT JOIN detail ON student.id = detail.student_id
LEFT JOIN situation ON detail.situation_id = situation.id
WHERE detail.loading_period = (SELECT MAX(detail.loading_period) FROM detail WHERE detail.student_id = student.id)
AND (situation.situation_short LIKE 'Evadido' OR situation.situation_short LIKE 'Formado')
AND student.course_id = :course_id
ORDER BY student.id;

SELECT student.id,student.nome, detail.id, situation.situation_long, situation.situation_short
FROM student
LEFT JOIN detail ON student.id = detail.student_id
LEFT JOIN situation ON detail.situation_id = situation.id
WHERE detail.loading_period = (SELECT MAX(detail.loading_period) FROM detail WHERE detail.student_id = student.id)
AND situation.situation_short = "Não Evadido"
AND student.course_id = :course_id
ORDER BY student.id;