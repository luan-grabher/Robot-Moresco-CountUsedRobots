SELECT * FROM tarefas t
where
t.membro_id = 26 AND
t.criadaEm >= ':date_start' AND
t.criadaEm <= ':date_end'