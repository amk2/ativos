--CARGA  TIPO_CAMPO

INSERT INTO tipos_campo (tipos_campo_id, tipos_campo_descricao)
VALUES (1, 'TEXTO');

INSERT INTO tipos_campo (tipos_campo_id, tipos_campo_descricao)
VALUES (2, 'AREA TEXTO');

INSERT INTO tipos_campo (tipos_campo_id, tipos_campo_descricao)
VALUES (3, 'BOTÕES RADIO');

INSERT INTO tipos_campo (tipos_campo_id, tipos_campo_descricao)
VALUES (4, 'MENU ESCOLHA');

INSERT INTO tipos_campo (tipos_campo_id, tipos_campo_descricao)
VALUES (5, 'NUMERO INTEIRO');

INSERT INTO tipos_campo (tipos_campo_id, tipos_campo_descricao)
VALUES (6, 'NUMERO DECIMAL');

INSERT INTO tipos_campo (tipos_campo_id, tipos_campo_descricao)
VALUES (7, 'DATA');

INSERT INTO tipos_campo (tipos_campo_id, tipos_campo_descricao)
VALUES (8, 'IMAGEM');

INSERT INTO tipos_campo (tipos_campo_id, tipos_campo_descricao)
VALUES (9, 'MENU ESCOLHA MULTIPLO');

--Sequence da tabela 
--SELECT pg_catalog.setval('tipos_campo_tipos_campo_id_seq', 10, true);

ALTER SEQUENCE "public"."tipos_campo_tipos_campo_id_seq"
 RESTART 10 ;

