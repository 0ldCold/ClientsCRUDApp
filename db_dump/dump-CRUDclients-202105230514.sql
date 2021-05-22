--
-- PostgreSQL database dump
--

-- Dumped from database version 13.0
-- Dumped by pg_dump version 13.0

-- Started on 2021-05-23 05:14:52

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 2995 (class 0 OID 0)
-- Dependencies: 3
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 201 (class 1259 OID 90838)
-- Name: AccessLevel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."AccessLevel" (
    id bigint NOT NULL,
    title character varying(255) NOT NULL
);


ALTER TABLE public."AccessLevel" OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 90832)
-- Name: User; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."User" (
    id bigint NOT NULL,
    login character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    accesslvl bigint NOT NULL,
    dateofcreation timestamp without time zone NOT NULL,
    dateofmodification timestamp without time zone NOT NULL
);


ALTER TABLE public."User" OWNER TO postgres;

--
-- TOC entry 2989 (class 0 OID 90838)
-- Dependencies: 201
-- Data for Name: AccessLevel; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."AccessLevel" (id, title) FROM stdin;
1	Admin
2	Tester
3	User
\.


--
-- TOC entry 2988 (class 0 OID 90832)
-- Dependencies: 200
-- Data for Name: User; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."User" (id, login, password, accesslvl, dateofcreation, dateofmodification) FROM stdin;
5	Тест5	тест5	3	2021-09-19 10:00:01	2021-09-20 10:00:01
7	Тест7	тест7	3	2021-09-19 10:00:01	2021-09-20 10:00:01
8	Тест8	тест8	3	2021-09-19 10:00:01	2021-09-20 10:00:01
9	Тест9	тест9	3	2021-09-19 10:00:01	2021-09-20 10:00:01
1	Test1	test	1	2021-05-23 04:30:12.07448	2021-05-23 04:30:12.075981
10	Тест10	тест10	2	2021-09-19 10:00:01	2021-09-20 10:00:01
\.


--
-- TOC entry 2856 (class 2606 OID 90842)
-- Name: AccessLevel accesslevel_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."AccessLevel"
    ADD CONSTRAINT accesslevel_pk PRIMARY KEY (id);


--
-- TOC entry 2854 (class 2606 OID 90844)
-- Name: User user_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."User"
    ADD CONSTRAINT user_pk PRIMARY KEY (id);


--
-- TOC entry 2857 (class 2606 OID 90845)
-- Name: User user_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."User"
    ADD CONSTRAINT user_fk FOREIGN KEY (accesslvl) REFERENCES public."AccessLevel"(id);


-- Completed on 2021-05-23 05:14:52

--
-- PostgreSQL database dump complete
--

