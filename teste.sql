DELETE FROM probability WHERE test_classifier_id > 560;
DELETE FROM test_classifier_variable WHERE test_classifier_id > 560;
DELETE FROM test_classifier WHERE id > 560;

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