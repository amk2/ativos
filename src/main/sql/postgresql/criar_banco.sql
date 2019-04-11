--
-- Criaçao do database 

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 1910 (class 1262 OID 321559)
-- Name: sling_control_julho; Type: DATABASE; Schema: -; Owner: postgres
--

SET search_path = public;

CREATE TABLE ativos (
    ativos_id integer NOT NULL,
    ativos_id_referencia integer,
    layout_id integer,
    estoque_id integer
);


ALTER TABLE public.ativos OWNER TO postgres;

--layout_configuracoes_ordem
-- TOC entry 1521 (class 1259 OID 321725)
-- Dependencies: 3
-- Name: configuracoes; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE layout_configuracoes (
    layout_configuracoes_id integer NOT NULL,
    layout_configuracoes_nome_campo character varying(255) NOT NULL,
    layout_configuracoes_obrigatoriedade character(1) NOT NULL,
    layout_configuracoes_tamanho_campo integer NOT NULL,
    layout_id integer,
    tipos_campo_id integer,
    layout_configuracoes_localizacao character(1),
    layout_configuracoes_ordem integer,
    layout_configuracoes_id_referencia bigint
);


ALTER TABLE public.layout_configuracoes  OWNER TO postgres;

--
-- TOC entry 1523 (class 1259 OID 321734)
-- Dependencies: 3
-- Name: demandas; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE demandas (
    demanda_id integer NOT NULL,
    layout_id integer,
    tipo_demanda_id integer
);


ALTER TABLE public.demandas OWNER TO postgres;

--
-- TOC entry 1525 (class 1259 OID 321743)
-- Dependencies: 3
-- Name: estoque; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE estoque (
    estoque_nome character varying(150),
    estoque_id integer NOT NULL,
    estoque_descricao character varying(255),
    locais_id integer
);


ALTER TABLE public.estoque OWNER TO postgres;

--
-- TOC entry 1527 (class 1259 OID 321752)
-- Dependencies: 3
-- Name: informacoes_ativo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE informacoes_ativo (
    informacoes_id integer NOT NULL,
    informacoes_descricao character varying(500),
    layout_configuracoes_id integer,
    informacoes_id_referencia integer,
    ativos_id integer
);


ALTER TABLE public.informacoes_ativo OWNER TO postgres;

--
-- TOC entry 1529 (class 1259 OID 321764)
-- Dependencies: 3
-- Name: informacoes_demanda; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE informacoes_demanda (
    informacoes_id integer NOT NULL,
    informacoes_descricao character varying(500),
    layout_configuracoes_id integer,
    informacoes_id_referencia integer,
    demanda_id integer NOT NULL
);


ALTER TABLE public.informacoes_demanda OWNER TO postgres;

--
-- TOC entry 1531 (class 1259 OID 321776)
-- Dependencies: 3
-- Name: informacoes_locais; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE informacoes_locais (
    informacoes_id integer NOT NULL,
    informacoes_descricao character varying(500),
    layout_configuracoes_id integer,
    informacoes_id_referencia integer,
    ativos_id integer,
    locais_id integer
);


ALTER TABLE public.informacoes_locais OWNER TO postgres;

--
-- TOC entry 1533 (class 1259 OID 321788)
-- Dependencies: 3
-- Name: informacoes_movimento_ativo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE informacoes_movimento_ativo (
    informacoes_id integer NOT NULL,
    informacoes_descricao character varying(500),
    layout_configuracoes_id integer,
    informacoes_id_referencia integer,
    movimento_ativo_id integer
);


ALTER TABLE public.informacoes_movimento_ativo OWNER TO postgres;

--
-- TOC entry 1535 (class 1259 OID 321800)
-- Dependencies: 3
-- Name: layouts; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE layouts (
    layout_id integer NOT NULL,
    layout_descricao character varying(255) NOT NULL,
    tipo_layout_id integer,
    layout_nome character varying(100)
);


ALTER TABLE public.layouts OWNER TO postgres;

--
-- TOC entry 1537 (class 1259 OID 321809)
-- Dependencies: 3
-- Name: locais; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE locais (
    locais_id integer NOT NULL,
    locais_id_referencia integer,
    layout_id integer,
    locais_nome character varying(100),
    locais_descricao character varying(255)
);


ALTER TABLE public.locais OWNER TO postgres;

--
-- TOC entry 1539 (class 1259 OID 321818)
-- Dependencies: 3
-- Name: movimento_ativo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE movimento_ativo (
    movimento_ativo_id integer NOT NULL,
    layout_id integer,
    demanda_id integer,
    tipo_movimento_ativo_id integer
);


ALTER TABLE public.movimento_ativo OWNER TO postgres;

--
-- TOC entry 1541 (class 1259 OID 321827)
-- Dependencies: 3
-- Name: tipo_demanda; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tipo_demanda (
    tipo_demanda_id integer NOT NULL,
    tipo_demanda_descricao character varying(255)
);


ALTER TABLE public.tipo_demanda OWNER TO postgres;

--
-- TOC entry 1543 (class 1259 OID 321836)
-- Dependencies: 3
-- Name: tipo_layout; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tipo_layout (
    tipo_layout_id integer NOT NULL,
    tipo_layout_descricao character varying(255)
);


ALTER TABLE public.tipo_layout OWNER TO postgres;

--
-- TOC entry 1545 (class 1259 OID 321845)
-- Dependencies: 3
-- Name: tipo_movimento_ativo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tipo_movimento_ativo (
    tipo_movimento_ativo_id integer NOT NULL,
    tipo_movimento_ativo_descricao character varying(255)
);


ALTER TABLE public.tipo_movimento_ativo OWNER TO postgres;

--
-- TOC entry 1547 (class 1259 OID 321854)
-- Dependencies: 3
-- Name: tipos_campo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tipos_campo (
    tipos_campo_id integer NOT NULL,
    tipos_campo_descricao character varying(255) NOT NULL
);


ALTER TABLE public.tipos_campo OWNER TO postgres;

CREATE SEQUENCE ativos_ativos_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.ativos_ativos_id_seq OWNER TO postgres;

--
-- TOC entry 1913 (class 0 OID 0)
-- Dependencies: 1518
-- Name: ativos_ativos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ativos_ativos_id_seq OWNED BY ativos.ativos_id;


--
-- TOC entry 1520 (class 1259 OID 321723)
-- Dependencies: 1521 3
-- Name: configuracoes_configuracoes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE layout_configuracoes_layout_configuracoes_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.layout_configuracoes_layout_configuracoes_id_seq OWNER TO postgres;

--
-- TOC entry 1914 (class 0 OID 0)
-- Dependencies: 1520
-- Name: configuracoes_configuracoes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE layout_configuracoes_layout_configuracoes_id_seq OWNED BY layout_configuracoes.layout_configuracoes_id;


--
-- TOC entry 1522 (class 1259 OID 321732)
-- Dependencies: 1523 3
-- Name: demandas_demanda_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE demandas_demanda_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.demandas_demanda_id_seq OWNER TO postgres;

--
-- TOC entry 1915 (class 0 OID 0)
-- Dependencies: 1522
-- Name: demandas_demanda_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE demandas_demanda_id_seq OWNED BY demandas.demanda_id;


--
-- TOC entry 1524 (class 1259 OID 321741)
-- Dependencies: 3 1525
-- Name: estoque_estoque_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE estoque_estoque_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.estoque_estoque_id_seq OWNER TO postgres;

--
-- TOC entry 1916 (class 0 OID 0)
-- Dependencies: 1524
-- Name: estoque_estoque_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE estoque_estoque_id_seq OWNED BY estoque.estoque_id;


--
-- TOC entry 1526 (class 1259 OID 321750)
-- Dependencies: 3 1527
-- Name: informacoes_ativo_informacoes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE informacoes_ativo_informacoes_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.informacoes_ativo_informacoes_id_seq OWNER TO postgres;

--
-- TOC entry 1917 (class 0 OID 0)
-- Dependencies: 1526
-- Name: informacoes_ativo_informacoes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE informacoes_ativo_informacoes_id_seq OWNED BY informacoes_ativo.informacoes_id;


--
-- TOC entry 1528 (class 1259 OID 321762)
-- Dependencies: 3 1529
-- Name: informacoes_demanda_informacoes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE informacoes_demanda_informacoes_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.informacoes_demanda_informacoes_id_seq OWNER TO postgres;

--
-- TOC entry 1918 (class 0 OID 0)
-- Dependencies: 1528
-- Name: informacoes_demanda_informacoes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE informacoes_demanda_informacoes_id_seq OWNED BY informacoes_demanda.informacoes_id;


--
-- TOC entry 1530 (class 1259 OID 321774)
-- Dependencies: 3 1531
-- Name: informacoes_locais_informacoes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE informacoes_locais_informacoes_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.informacoes_locais_informacoes_id_seq OWNER TO postgres;

--
-- TOC entry 1919 (class 0 OID 0)
-- Dependencies: 1530
-- Name: informacoes_locais_informacoes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE informacoes_locais_informacoes_id_seq OWNED BY informacoes_locais.informacoes_id;


--
-- TOC entry 1532 (class 1259 OID 321786)
-- Dependencies: 1533 3
-- Name: informacoes_movimento_ativo_informacoes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE informacoes_movimento_ativo_informacoes_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.informacoes_movimento_ativo_informacoes_id_seq OWNER TO postgres;

--
-- TOC entry 1920 (class 0 OID 0)
-- Dependencies: 1532
-- Name: informacoes_movimento_ativo_informacoes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE informacoes_movimento_ativo_informacoes_id_seq OWNED BY informacoes_movimento_ativo.informacoes_id;


--
-- TOC entry 1534 (class 1259 OID 321798)
-- Dependencies: 3 1535
-- Name: layouts_layout_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE layouts_layout_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.layouts_layout_id_seq OWNER TO postgres;

--
-- TOC entry 1921 (class 0 OID 0)
-- Dependencies: 1534
-- Name: layouts_layout_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE layouts_layout_id_seq OWNED BY layouts.layout_id;


--
-- TOC entry 1536 (class 1259 OID 321807)
-- Dependencies: 1537 3
-- Name: locais_locais_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE locais_locais_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.locais_locais_id_seq OWNER TO postgres;

--
-- TOC entry 1922 (class 0 OID 0)
-- Dependencies: 1536
-- Name: locais_locais_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE locais_locais_id_seq OWNED BY locais.locais_id;


--
-- TOC entry 1538 (class 1259 OID 321816)
-- Dependencies: 3 1539
-- Name: movimento_ativo_movimento_ativo_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE movimento_ativo_movimento_ativo_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.movimento_ativo_movimento_ativo_id_seq OWNER TO postgres;

--
-- TOC entry 1923 (class 0 OID 0)
-- Dependencies: 1538
-- Name: movimento_ativo_movimento_ativo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE movimento_ativo_movimento_ativo_id_seq OWNED BY movimento_ativo.movimento_ativo_id;


--
-- TOC entry 1540 (class 1259 OID 321825)
-- Dependencies: 3 1541
-- Name: tipo_demanda_tipo_demanda_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tipo_demanda_tipo_demanda_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tipo_demanda_tipo_demanda_id_seq OWNER TO postgres;

--
-- TOC entry 1924 (class 0 OID 0)
-- Dependencies: 1540
-- Name: tipo_demanda_tipo_demanda_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tipo_demanda_tipo_demanda_id_seq OWNED BY tipo_demanda.tipo_demanda_id;


--
-- TOC entry 1542 (class 1259 OID 321834)
-- Dependencies: 3 1543
-- Name: tipo_layout_tipo_layout_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tipo_layout_tipo_layout_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tipo_layout_tipo_layout_id_seq OWNER TO postgres;

--
-- TOC entry 1925 (class 0 OID 0)
-- Dependencies: 1542
-- Name: tipo_layout_tipo_layout_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tipo_layout_tipo_layout_id_seq OWNED BY tipo_layout.tipo_layout_id;


--
-- TOC entry 1544 (class 1259 OID 321843)
-- Dependencies: 3 1545
-- Name: tipo_movimento_ativo_tipo_movimento_ativo_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tipo_movimento_ativo_tipo_movimento_ativo_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tipo_movimento_ativo_tipo_movimento_ativo_id_seq OWNER TO postgres;

--
-- TOC entry 1926 (class 0 OID 0)
-- Dependencies: 1544
-- Name: tipo_movimento_ativo_tipo_movimento_ativo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tipo_movimento_ativo_tipo_movimento_ativo_id_seq OWNED BY tipo_movimento_ativo.tipo_movimento_ativo_id;


--
-- TOC entry 1546 (class 1259 OID 321852)
-- Dependencies: 1547 3
-- Name: tipos_campo_tipos_campo_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tipos_campo_tipos_campo_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tipos_campo_tipos_campo_id_seq OWNER TO postgres;

--
-- TOC entry 1927 (class 0 OID 0)
-- Dependencies: 1546
-- Name: tipos_campo_tipos_campo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tipos_campo_tipos_campo_id_seq OWNED BY tipos_campo.tipos_campo_id;


ALTER TABLE ativos ALTER COLUMN ativos_id SET DEFAULT nextval('ativos_ativos_id_seq'::regclass);


--
-- TOC entry 1820 (class 2604 OID 321728)
-- Dependencies: 1521 1520 1521
-- Name: configuracoes_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE layout_configuracoes ALTER COLUMN layout_configuracoes_id SET DEFAULT nextval('layout_configuracoes_layout_configuracoes_id_seq'::regclass);



ALTER TABLE demandas ALTER COLUMN demanda_id SET DEFAULT nextval('demandas_demanda_id_seq'::regclass);


--
-- TOC entry 1822 (class 2604 OID 321746)
-- Dependencies: 1525 1524 1525
-- Name: estoque_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE estoque ALTER COLUMN estoque_id SET DEFAULT nextval('estoque_estoque_id_seq'::regclass);


--
-- TOC entry 1823 (class 2604 OID 321755)
-- Dependencies: 1526 1527 1527
-- Name: informacoes_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE informacoes_ativo ALTER COLUMN informacoes_id SET DEFAULT nextval('informacoes_ativo_informacoes_id_seq'::regclass);


--
-- TOC entry 1824 (class 2604 OID 321767)
-- Dependencies: 1528 1529 1529
-- Name: informacoes_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE informacoes_demanda ALTER COLUMN informacoes_id SET DEFAULT nextval('informacoes_demanda_informacoes_id_seq'::regclass);


--
-- TOC entry 1825 (class 2604 OID 321779)
-- Dependencies: 1530 1531 1531
-- Name: informacoes_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE informacoes_locais ALTER COLUMN informacoes_id SET DEFAULT nextval('informacoes_locais_informacoes_id_seq'::regclass);


--
-- TOC entry 1826 (class 2604 OID 321791)
-- Dependencies: 1533 1532 1533
-- Name: informacoes_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE informacoes_movimento_ativo ALTER COLUMN informacoes_id SET DEFAULT nextval('informacoes_movimento_ativo_informacoes_id_seq'::regclass);


--
-- TOC entry 1827 (class 2604 OID 321803)
-- Dependencies: 1535 1534 1535
-- Name: layout_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE layouts ALTER COLUMN layout_id SET DEFAULT nextval('layouts_layout_id_seq'::regclass);


--
-- TOC entry 1828 (class 2604 OID 321812)
-- Dependencies: 1537 1536 1537
-- Name: locais_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE locais ALTER COLUMN locais_id SET DEFAULT nextval('locais_locais_id_seq'::regclass);


--
-- TOC entry 1829 (class 2604 OID 321821)
-- Dependencies: 1538 1539 1539
-- Name: movimento_ativo_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE movimento_ativo ALTER COLUMN movimento_ativo_id SET DEFAULT nextval('movimento_ativo_movimento_ativo_id_seq'::regclass);


--
-- TOC entry 1830 (class 2604 OID 321830)
-- Dependencies: 1541 1540 1541
-- Name: tipo_demanda_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE tipo_demanda ALTER COLUMN tipo_demanda_id SET DEFAULT nextval('tipo_demanda_tipo_demanda_id_seq'::regclass);


--
-- TOC entry 1831 (class 2604 OID 321839)
-- Dependencies: 1542 1543 1543
-- Name: tipo_layout_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE tipo_layout ALTER COLUMN tipo_layout_id SET DEFAULT nextval('tipo_layout_tipo_layout_id_seq'::regclass);


--
-- TOC entry 1832 (class 2604 OID 321848)
-- Dependencies: 1545 1544 1545
-- Name: tipo_movimento_ativo_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE tipo_movimento_ativo ALTER COLUMN tipo_movimento_ativo_id SET DEFAULT nextval('tipo_movimento_ativo_tipo_movimento_ativo_id_seq'::regclass);


--
-- TOC entry 1833 (class 2604 OID 321857)
-- Dependencies: 1546 1547 1547
-- Name: tipos_campo_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE tipos_campo ALTER COLUMN tipos_campo_id SET DEFAULT nextval('tipos_campo_tipos_campo_id_seq'::regclass);


--
-- TOC entry 1837 (class 2606 OID 321722)
-- Dependencies: 1519 1519
-- Name: ativos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ativos
    ADD CONSTRAINT ativos_pkey PRIMARY KEY (ativos_id);


--
-- TOC entry 1840 (class 2606 OID 321731)
-- Dependencies: 1521 1521
-- Name: configuracoes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY layout_configuracoes
    ADD CONSTRAINT layout_configuracoes_pkey PRIMARY KEY (layout_configuracoes_id);


--
-- TOC entry 1843 (class 2606 OID 321740)
-- Dependencies: 1523 1523
-- Name: demandas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY demandas
    ADD CONSTRAINT demandas_pkey PRIMARY KEY (demanda_id);


--
-- TOC entry 1846 (class 2606 OID 321749)
-- Dependencies: 1525 1525
-- Name: estoque_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY estoque
    ADD CONSTRAINT estoque_pkey PRIMARY KEY (estoque_id);


--
-- TOC entry 1849 (class 2606 OID 321761)
-- Dependencies: 1527 1527
-- Name: informacoes_ativo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY informacoes_ativo
    ADD CONSTRAINT informacoes_ativo_pkey PRIMARY KEY (informacoes_id);


--
-- TOC entry 1852 (class 2606 OID 321773)
-- Dependencies: 1529 1529
-- Name: informacoes_demanda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY informacoes_demanda
    ADD CONSTRAINT informacoes_demanda_pkey PRIMARY KEY (informacoes_id);


--
-- TOC entry 1855 (class 2606 OID 321785)
-- Dependencies: 1531 1531
-- Name: informacoes_locais_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY informacoes_locais
    ADD CONSTRAINT informacoes_locais_pkey PRIMARY KEY (informacoes_id);


--
-- TOC entry 1858 (class 2606 OID 321797)
-- Dependencies: 1533 1533
-- Name: informacoes_movimento_ativo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY informacoes_movimento_ativo
    ADD CONSTRAINT informacoes_movimento_ativo_pkey PRIMARY KEY (informacoes_id);


--
-- TOC entry 1861 (class 2606 OID 321806)
-- Dependencies: 1535 1535
-- Name: layouts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY layouts
    ADD CONSTRAINT layouts_pkey PRIMARY KEY (layout_id);


--
-- TOC entry 1864 (class 2606 OID 321815)
-- Dependencies: 1537 1537
-- Name: locais_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY locais
    ADD CONSTRAINT locais_pkey PRIMARY KEY (locais_id);


--
-- TOC entry 1867 (class 2606 OID 321824)
-- Dependencies: 1539 1539
-- Name: movimento_ativo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY movimento_ativo
    ADD CONSTRAINT movimento_ativo_pkey PRIMARY KEY (movimento_ativo_id);


--
-- TOC entry 1870 (class 2606 OID 321833)
-- Dependencies: 1541 1541
-- Name: tipo_demanda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipo_demanda
    ADD CONSTRAINT tipo_demanda_pkey PRIMARY KEY (tipo_demanda_id);


--
-- TOC entry 1873 (class 2606 OID 321842)
-- Dependencies: 1543 1543
-- Name: tipo_layout_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipo_layout
    ADD CONSTRAINT tipo_layout_pkey PRIMARY KEY (tipo_layout_id);


--
-- TOC entry 1876 (class 2606 OID 321851)
-- Dependencies: 1545 1545
-- Name: tipo_movimento_ativo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipo_movimento_ativo
    ADD CONSTRAINT tipo_movimento_ativo_pkey PRIMARY KEY (tipo_movimento_ativo_id);


--
-- TOC entry 1879 (class 2606 OID 321860)
-- Dependencies: 1547 1547
-- Name: tipos_campo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipos_campo
    ADD CONSTRAINT tipos_campo_pkey PRIMARY KEY (tipos_campo_id);



CREATE UNIQUE INDEX ativosprimarykey ON ativos USING btree (ativos_id);


--
-- TOC entry 1847 (class 1259 OID 321747)
-- Dependencies: 1525
-- Name: estoqueprimarykey; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX estoqueprimarykey ON estoque USING btree (estoque_id);


--
-- TOC entry 1865 (class 1259 OID 321813)
-- Dependencies: 1537
-- Name: locaisprimarykey; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX locaisprimarykey ON locais USING btree (locais_id);


--
-- TOC entry 1841 (class 1259 OID 321729)
-- Dependencies: 1521
-- Name: xpkconfiguracoes; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX xpkconfiguracoes ON layout_configuracoes USING btree (layout_configuracoes_id);


--
-- TOC entry 1844 (class 1259 OID 321738)
-- Dependencies: 1523
-- Name: xpkdemandas; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX xpkdemandas ON demandas USING btree (demanda_id);


--
-- TOC entry 1850 (class 1259 OID 321759)
-- Dependencies: 1527
-- Name: xpkinformacoes_ativo; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX xpkinformacoes_ativo ON informacoes_ativo USING btree (informacoes_id);


--
-- TOC entry 1853 (class 1259 OID 321771)
-- Dependencies: 1529
-- Name: xpkinformacoes_demanda; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX xpkinformacoes_demanda ON informacoes_demanda USING btree (informacoes_id);


--
-- TOC entry 1856 (class 1259 OID 321783)
-- Dependencies: 1531
-- Name: xpkinformacoes_locais; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX xpkinformacoes_locais ON informacoes_locais USING btree (informacoes_id);


--
-- TOC entry 1859 (class 1259 OID 321795)
-- Dependencies: 1533
-- Name: xpkinformacoes_movimento_ativo; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX xpkinformacoes_movimento_ativo ON informacoes_movimento_ativo USING btree (informacoes_id);


--
-- TOC entry 1862 (class 1259 OID 321804)
-- Dependencies: 1535
-- Name: xpklayouts; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX xpklayouts ON layouts USING btree (layout_id);


--
-- TOC entry 1868 (class 1259 OID 321822)
-- Dependencies: 1539
-- Name: xpkmovimento_ativo; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX xpkmovimento_ativo ON movimento_ativo USING btree (movimento_ativo_id);


--
-- TOC entry 1871 (class 1259 OID 321831)
-- Dependencies: 1541
-- Name: xpktipo_demanda; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX xpktipo_demanda ON tipo_demanda USING btree (tipo_demanda_id);


--
-- TOC entry 1874 (class 1259 OID 321840)
-- Dependencies: 1543
-- Name: xpktipo_layout; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX xpktipo_layout ON tipo_layout USING btree (tipo_layout_id);


--
-- TOC entry 1877 (class 1259 OID 321849)
-- Dependencies: 1545
-- Name: xpktipo_movimento_ativo; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX xpktipo_movimento_ativo ON tipo_movimento_ativo USING btree (tipo_movimento_ativo_id);


--
-- TOC entry 1880 (class 1259 OID 321858)
-- Dependencies: 1547
-- Name: xpktipos_campo; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX xpktipos_campo ON tipos_campo USING btree (tipos_campo_id);


--
-- TOC entry 1886 (class 2606 OID 321866)
-- Dependencies: 1525 1845 1519
-- Name: ativos_estoque_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ativos
    ADD CONSTRAINT ativos_estoque_id_fkey FOREIGN KEY (estoque_id) REFERENCES estoque(estoque_id) ON DELETE SET NULL;




--
-- TOC entry 1887 (class 2606 OID 321871)
-- Dependencies: 1535 1860 1519
-- Name: ativos_layout_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ativos
    ADD CONSTRAINT ativos_layout_id_fkey FOREIGN KEY (layout_id) REFERENCES layouts(layout_id) ON DELETE SET NULL;


--
-- TOC entry 1889 (class 2606 OID 321881)
-- Dependencies: 1521 1535 1860
-- Name: configuracoes_layout_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY layout_configuracoes
    ADD CONSTRAINT configuracoes_layout_id_fkey FOREIGN KEY (layout_id) REFERENCES layouts(layout_id) ON DELETE SET NULL;


--
-- TOC entry 1888 (class 2606 OID 321876)
-- Dependencies: 1547 1521 1878
-- Name: configuracoes_tipos_campo_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY layout_configuracoes
    ADD CONSTRAINT configuracoes_tipos_campo_id_fkey FOREIGN KEY (tipos_campo_id) REFERENCES tipos_campo(tipos_campo_id) ON DELETE SET NULL;



--
-- TOC entry 1892 (class 2606 OID 321896)
-- Dependencies: 1860 1523 1535
-- Name: demandas_layout_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY demandas
    ADD CONSTRAINT demandas_layout_id_fkey FOREIGN KEY (layout_id) REFERENCES layouts(layout_id) ON DELETE SET NULL;


--
-- TOC entry 1890 (class 2606 OID 321886)
-- Dependencies: 1523 1869 1541
-- Name: demandas_tipo_demanda_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY demandas
    ADD CONSTRAINT demandas_tipo_demanda_id_fkey FOREIGN KEY (tipo_demanda_id) REFERENCES tipo_demanda(tipo_demanda_id) ON DELETE SET NULL;


--
-- TOC entry 1893 (class 2606 OID 321901)
-- Dependencies: 1537 1863 1525
-- Name: estoque_locais_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY estoque
    ADD CONSTRAINT estoque_locais_id_fkey FOREIGN KEY (locais_id) REFERENCES locais(locais_id) ON DELETE SET NULL;


--
-- TOC entry 1894 (class 2606 OID 321906)
-- Dependencies: 1839 1527 1521
-- Name: informacoes_ativo_configuracoes_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY informacoes_ativo
    ADD CONSTRAINT informacoes_ativo_configuracoes_id_fkey FOREIGN KEY (layout_configuracoes_id) REFERENCES layout_configuracoes(layout_configuracoes_id) ON DELETE SET NULL;


--
-- TOC entry 1895 (class 2606 OID 321911)
-- Dependencies: 1848 1527 1527
-- Name: informacoes_ativo_informacoes_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY informacoes_ativo
    ADD CONSTRAINT informacoes_ativo_informacoes_id_fkey FOREIGN KEY (informacoes_id) REFERENCES informacoes_ativo(informacoes_id) ON DELETE SET NULL;


--
-- TOC entry 1896 (class 2606 OID 321921)
-- Dependencies: 1529 1521 1839
-- Name: informacoes_demanda_configuracoes_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY informacoes_demanda
    ADD CONSTRAINT informacoes_demanda_configuracoes_id_fkey FOREIGN KEY (layout_configuracoes_id) REFERENCES layout_configuracoes(layout_configuracoes_id) ON DELETE SET NULL;


--
-- TOC entry 1897 (class 2606 OID 321926)
-- Dependencies: 1531 1521 1839
-- Name: informacoes_locais_configuracoes_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY informacoes_locais
    ADD CONSTRAINT informacoes_locais_configuracoes_id_fkey FOREIGN KEY (layout_configuracoes_id) REFERENCES layout_configuracoes(layout_configuracoes_id) ON DELETE SET NULL;


--
-- TOC entry 1899 (class 2606 OID 321936)
-- Dependencies: 1531 1531 1854
-- Name: informacoes_locais_informacoes_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY informacoes_locais
    ADD CONSTRAINT informacoes_locais_informacoes_id_fkey FOREIGN KEY (informacoes_id) REFERENCES informacoes_locais(informacoes_id) ON DELETE SET NULL;


--
-- TOC entry 1898 (class 2606 OID 321931)
-- Dependencies: 1531 1863 1537
-- Name: informacoes_locais_locais_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY informacoes_locais
    ADD CONSTRAINT informacoes_locais_locais_id_fkey FOREIGN KEY (locais_id) REFERENCES locais(locais_id) ON DELETE SET NULL;


--
-- TOC entry 1900 (class 2606 OID 321941)
-- Dependencies: 1533 1839 1521
-- Name: informacoes_movimento_ativo_configuracoes_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY informacoes_movimento_ativo
    ADD CONSTRAINT informacoes_movimento_ativo_configuracoes_id_fkey FOREIGN KEY (layout_configuracoes_id) REFERENCES layout_configuracoes(layout_configuracoes_id) ON DELETE SET NULL;


--
-- TOC entry 1902 (class 2606 OID 321951)
-- Dependencies: 1857 1533 1533
-- Name: informacoes_movimento_ativo_informacoes_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY informacoes_movimento_ativo
    ADD CONSTRAINT informacoes_movimento_ativo_informacoes_id_fkey FOREIGN KEY (informacoes_id) REFERENCES informacoes_movimento_ativo(informacoes_id) ON DELETE SET NULL;


--
-- TOC entry 1901 (class 2606 OID 321946)
-- Dependencies: 1866 1539 1533
-- Name: informacoes_movimento_ativo_movimento_ativo_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY informacoes_movimento_ativo
    ADD CONSTRAINT informacoes_movimento_ativo_movimento_ativo_id_fkey FOREIGN KEY (movimento_ativo_id) REFERENCES movimento_ativo(movimento_ativo_id) ON DELETE SET NULL;


--
-- TOC entry 1903 (class 2606 OID 321956)
-- Dependencies: 1535 1872 1543
-- Name: layouts_tipo_layout_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY layouts
    ADD CONSTRAINT layouts_tipo_layout_id_fkey FOREIGN KEY (tipo_layout_id) REFERENCES tipo_layout(tipo_layout_id) ON DELETE SET NULL;


--
-- TOC entry 1904 (class 2606 OID 321961)
-- Dependencies: 1535 1537 1860
-- Name: locais_layout_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY locais
    ADD CONSTRAINT locais_layout_id_fkey FOREIGN KEY (layout_id) REFERENCES layouts(layout_id) ON DELETE SET NULL;


--
-- TOC entry 1906 (class 2606 OID 321971)
-- Dependencies: 1539 1842 1523
-- Name: movimento_ativo_demanda_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY movimento_ativo
    ADD CONSTRAINT movimento_ativo_demanda_id_fkey FOREIGN KEY (demanda_id) REFERENCES demandas(demanda_id) ON DELETE SET NULL;


--
-- TOC entry 1907 (class 2606 OID 321976)
-- Dependencies: 1535 1539 1860
-- Name: movimento_ativo_layout_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY movimento_ativo
    ADD CONSTRAINT movimento_ativo_layout_id_fkey FOREIGN KEY (layout_id) REFERENCES layouts(layout_id) ON DELETE SET NULL;


--
-- TOC entry 1905 (class 2606 OID 321966)
-- Dependencies: 1875 1545 1539
-- Name: movimento_ativo_tipo_movimento_ativo_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY movimento_ativo
    ADD CONSTRAINT movimento_ativo_tipo_movimento_ativo_id_fkey FOREIGN KEY (tipo_movimento_ativo_id) REFERENCES tipo_movimento_ativo(tipo_movimento_ativo_id) ON DELETE SET NULL;


--
-- TOC entry 1912 (class 0 OID 0)
-- Dependencies: 3
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;

