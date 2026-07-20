--
-- PostgreSQL database dump
--

\restrict MQz4VLcCygdZ4c4C2TcRbGPZGx6SDlpjIETxNWee0sRs5b1avcpRGoOqduxAP2b

-- Dumped from database version 16.14
-- Dumped by pg_dump version 16.14

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

ALTER TABLE IF EXISTS ONLY public.user_roles DROP CONSTRAINT IF EXISTS fk_user_roles_user;
ALTER TABLE IF EXISTS ONLY public.user_roles DROP CONSTRAINT IF EXISTS fk_user_roles_role;
ALTER TABLE IF EXISTS ONLY public.role_permissions DROP CONSTRAINT IF EXISTS fk_role_permissions_role;
ALTER TABLE IF EXISTS ONLY public.role_permissions DROP CONSTRAINT IF EXISTS fk_role_permissions_permission;
ALTER TABLE IF EXISTS ONLY public.refresh_tokens DROP CONSTRAINT IF EXISTS fk_refresh_tokens_user;
ALTER TABLE IF EXISTS ONLY public.product_names DROP CONSTRAINT IF EXISTS fk_product_names_type;
ALTER TABLE IF EXISTS ONLY public.payments DROP CONSTRAINT IF EXISTS fk_payments_product;
ALTER TABLE IF EXISTS ONLY public.payments DROP CONSTRAINT IF EXISTS fk_payments_client;
ALTER TABLE IF EXISTS ONLY public.leads DROP CONSTRAINT IF EXISTS fk_leads_product;
ALTER TABLE IF EXISTS ONLY public.follow_ups DROP CONSTRAINT IF EXISTS fk_follow_ups_lead;
ALTER TABLE IF EXISTS ONLY public.follow_ups DROP CONSTRAINT IF EXISTS fk_follow_ups_client;
ALTER TABLE IF EXISTS ONLY public.follow_ups DROP CONSTRAINT IF EXISTS fk_follow_ups_assigned_user;
ALTER TABLE IF EXISTS ONLY public.expenses DROP CONSTRAINT IF EXISTS fk_expenses_member_user;
ALTER TABLE IF EXISTS ONLY public.expenses DROP CONSTRAINT IF EXISTS fk_expenses_client;
ALTER TABLE IF EXISTS ONLY public.departments DROP CONSTRAINT IF EXISTS fk_departments_parent;
ALTER TABLE IF EXISTS ONLY public.department_members DROP CONSTRAINT IF EXISTS fk_department_members_user;
ALTER TABLE IF EXISTS ONLY public.department_members DROP CONSTRAINT IF EXISTS fk_department_members_department;
ALTER TABLE IF EXISTS ONLY public.clients DROP CONSTRAINT IF EXISTS fk_clients_product;
ALTER TABLE IF EXISTS ONLY public.client_queries DROP CONSTRAINT IF EXISTS fk_client_queries_department;
ALTER TABLE IF EXISTS ONLY public.client_queries DROP CONSTRAINT IF EXISTS fk_client_queries_client;
ALTER TABLE IF EXISTS ONLY public.client_queries DROP CONSTRAINT IF EXISTS fk_client_queries_assigned_user;
ALTER TABLE IF EXISTS ONLY public.client_manager_assignments DROP CONSTRAINT IF EXISTS fk_client_manager_assignments_user;
ALTER TABLE IF EXISTS ONLY public.client_manager_assignments DROP CONSTRAINT IF EXISTS fk_client_manager_assignments_department;
ALTER TABLE IF EXISTS ONLY public.client_manager_assignments DROP CONSTRAINT IF EXISTS fk_client_manager_assignments_client;
ALTER TABLE IF EXISTS ONLY public.client_deadlines DROP CONSTRAINT IF EXISTS fk_client_deadlines_department;
ALTER TABLE IF EXISTS ONLY public.client_deadlines DROP CONSTRAINT IF EXISTS fk_client_deadlines_client;
DROP INDEX IF EXISTS public.uq_user_roles_user_role_active;
DROP INDEX IF EXISTS public.uq_role_permissions_role_perm_active;
DROP INDEX IF EXISTS public.uq_products_name_type_active;
DROP INDEX IF EXISTS public.uq_product_types_name_active;
DROP INDEX IF EXISTS public.uq_product_names_type_name_active;
DROP INDEX IF EXISTS public.uq_payments_invoice_number_active;
DROP INDEX IF EXISTS public.uq_leads_email_active;
DROP INDEX IF EXISTS public.uq_departments_name_active;
DROP INDEX IF EXISTS public.uq_department_members_dept_user_active;
DROP INDEX IF EXISTS public.uq_clients_email_active;
DROP INDEX IF EXISTS public.uq_client_manager_assignments_client_user_active;
DROP INDEX IF EXISTS public.idx_users_status;
DROP INDEX IF EXISTS public.idx_users_phone;
DROP INDEX IF EXISTS public.idx_users_email;
DROP INDEX IF EXISTS public.idx_users_deleted;
DROP INDEX IF EXISTS public.idx_users_created_at;
DROP INDEX IF EXISTS public.idx_user_roles_user_id;
DROP INDEX IF EXISTS public.idx_user_roles_role_id;
DROP INDEX IF EXISTS public.idx_roles_status;
DROP INDEX IF EXISTS public.idx_roles_deleted;
DROP INDEX IF EXISTS public.idx_role_permissions_role_id;
DROP INDEX IF EXISTS public.idx_role_permissions_permission_id;
DROP INDEX IF EXISTS public.idx_refresh_tokens_user_id;
DROP INDEX IF EXISTS public.idx_refresh_tokens_token;
DROP INDEX IF EXISTS public.idx_refresh_tokens_expires_at;
DROP INDEX IF EXISTS public.idx_products_type;
DROP INDEX IF EXISTS public.idx_products_status;
DROP INDEX IF EXISTS public.idx_products_name;
DROP INDEX IF EXISTS public.idx_products_deleted;
DROP INDEX IF EXISTS public.idx_product_types_status;
DROP INDEX IF EXISTS public.idx_product_types_deleted;
DROP INDEX IF EXISTS public.idx_product_names_type_id;
DROP INDEX IF EXISTS public.idx_product_names_status;
DROP INDEX IF EXISTS public.idx_product_names_deleted;
DROP INDEX IF EXISTS public.idx_permissions_status;
DROP INDEX IF EXISTS public.idx_permissions_module;
DROP INDEX IF EXISTS public.idx_permissions_deleted;
DROP INDEX IF EXISTS public.idx_payments_status;
DROP INDEX IF EXISTS public.idx_payments_product_id;
DROP INDEX IF EXISTS public.idx_payments_payment_mode;
DROP INDEX IF EXISTS public.idx_payments_payment_date;
DROP INDEX IF EXISTS public.idx_payments_invoice_number;
DROP INDEX IF EXISTS public.idx_payments_deleted;
DROP INDEX IF EXISTS public.idx_payments_created_at;
DROP INDEX IF EXISTS public.idx_payments_client_id;
DROP INDEX IF EXISTS public.idx_leads_status;
DROP INDEX IF EXISTS public.idx_leads_state;
DROP INDEX IF EXISTS public.idx_leads_stage;
DROP INDEX IF EXISTS public.idx_leads_product_id;
DROP INDEX IF EXISTS public.idx_leads_phone;
DROP INDEX IF EXISTS public.idx_leads_deleted;
DROP INDEX IF EXISTS public.idx_leads_created_at;
DROP INDEX IF EXISTS public.idx_leads_company_name;
DROP INDEX IF EXISTS public.idx_leads_city;
DROP INDEX IF EXISTS public.idx_follow_ups_type;
DROP INDEX IF EXISTS public.idx_follow_ups_status;
DROP INDEX IF EXISTS public.idx_follow_ups_lead_id;
DROP INDEX IF EXISTS public.idx_follow_ups_follow_up_status;
DROP INDEX IF EXISTS public.idx_follow_ups_follow_up_date;
DROP INDEX IF EXISTS public.idx_follow_ups_deleted;
DROP INDEX IF EXISTS public.idx_follow_ups_created_at;
DROP INDEX IF EXISTS public.idx_follow_ups_client_id;
DROP INDEX IF EXISTS public.idx_follow_ups_assigned_user_id;
DROP INDEX IF EXISTS public.idx_expenses_type;
DROP INDEX IF EXISTS public.idx_expenses_status;
DROP INDEX IF EXISTS public.idx_expenses_paid_date;
DROP INDEX IF EXISTS public.idx_expenses_member_user_id;
DROP INDEX IF EXISTS public.idx_expenses_expense_date;
DROP INDEX IF EXISTS public.idx_expenses_due_date;
DROP INDEX IF EXISTS public.idx_expenses_deleted;
DROP INDEX IF EXISTS public.idx_expenses_created_at;
DROP INDEX IF EXISTS public.idx_expenses_client_id;
DROP INDEX IF EXISTS public.idx_departments_status;
DROP INDEX IF EXISTS public.idx_departments_parent_id;
DROP INDEX IF EXISTS public.idx_departments_name;
DROP INDEX IF EXISTS public.idx_departments_deleted;
DROP INDEX IF EXISTS public.idx_department_members_user_id;
DROP INDEX IF EXISTS public.idx_department_members_department_id;
DROP INDEX IF EXISTS public.idx_department_members_deleted;
DROP INDEX IF EXISTS public.idx_clients_status;
DROP INDEX IF EXISTS public.idx_clients_stage;
DROP INDEX IF EXISTS public.idx_clients_product_id;
DROP INDEX IF EXISTS public.idx_clients_priority;
DROP INDEX IF EXISTS public.idx_clients_mobile;
DROP INDEX IF EXISTS public.idx_clients_deleted;
DROP INDEX IF EXISTS public.idx_clients_deal_date;
DROP INDEX IF EXISTS public.idx_clients_created_at;
DROP INDEX IF EXISTS public.idx_clients_client_name;
DROP INDEX IF EXISTS public.idx_client_queries_status;
DROP INDEX IF EXISTS public.idx_client_queries_query_type;
DROP INDEX IF EXISTS public.idx_client_queries_query_status;
DROP INDEX IF EXISTS public.idx_client_queries_priority;
DROP INDEX IF EXISTS public.idx_client_queries_department_id;
DROP INDEX IF EXISTS public.idx_client_queries_deleted;
DROP INDEX IF EXISTS public.idx_client_queries_created_at;
DROP INDEX IF EXISTS public.idx_client_queries_completed_at;
DROP INDEX IF EXISTS public.idx_client_queries_client_id;
DROP INDEX IF EXISTS public.idx_client_queries_assigned_user_id;
DROP INDEX IF EXISTS public.idx_client_manager_assignments_user_id;
DROP INDEX IF EXISTS public.idx_client_manager_assignments_status;
DROP INDEX IF EXISTS public.idx_client_manager_assignments_department_id;
DROP INDEX IF EXISTS public.idx_client_manager_assignments_deleted;
DROP INDEX IF EXISTS public.idx_client_manager_assignments_created_at;
DROP INDEX IF EXISTS public.idx_client_manager_assignments_close_date;
DROP INDEX IF EXISTS public.idx_client_manager_assignments_client_id;
DROP INDEX IF EXISTS public.idx_client_deadlines_status;
DROP INDEX IF EXISTS public.idx_client_deadlines_new_deadline;
DROP INDEX IF EXISTS public.idx_client_deadlines_department_id;
DROP INDEX IF EXISTS public.idx_client_deadlines_deleted;
DROP INDEX IF EXISTS public.idx_client_deadlines_current_deadline;
DROP INDEX IF EXISTS public.idx_client_deadlines_created_at;
DROP INDEX IF EXISTS public.idx_client_deadlines_client_id;
DROP INDEX IF EXISTS public.flyway_schema_history_s_idx;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS users_pkey;
ALTER TABLE IF EXISTS ONLY public.user_roles DROP CONSTRAINT IF EXISTS user_roles_pkey;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS uq_users_uuid;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS uq_users_email;
ALTER TABLE IF EXISTS ONLY public.user_roles DROP CONSTRAINT IF EXISTS uq_user_roles_uuid;
ALTER TABLE IF EXISTS ONLY public.roles DROP CONSTRAINT IF EXISTS uq_roles_uuid;
ALTER TABLE IF EXISTS ONLY public.roles DROP CONSTRAINT IF EXISTS uq_roles_name;
ALTER TABLE IF EXISTS ONLY public.roles DROP CONSTRAINT IF EXISTS uq_roles_code;
ALTER TABLE IF EXISTS ONLY public.role_permissions DROP CONSTRAINT IF EXISTS uq_role_permissions_uuid;
ALTER TABLE IF EXISTS ONLY public.refresh_tokens DROP CONSTRAINT IF EXISTS uq_refresh_tokens_uuid;
ALTER TABLE IF EXISTS ONLY public.refresh_tokens DROP CONSTRAINT IF EXISTS uq_refresh_tokens_token;
ALTER TABLE IF EXISTS ONLY public.products DROP CONSTRAINT IF EXISTS uq_products_uuid;
ALTER TABLE IF EXISTS ONLY public.products DROP CONSTRAINT IF EXISTS uq_products_code;
ALTER TABLE IF EXISTS ONLY public.product_types DROP CONSTRAINT IF EXISTS uq_product_types_uuid;
ALTER TABLE IF EXISTS ONLY public.product_names DROP CONSTRAINT IF EXISTS uq_product_names_uuid;
ALTER TABLE IF EXISTS ONLY public.permissions DROP CONSTRAINT IF EXISTS uq_permissions_uuid;
ALTER TABLE IF EXISTS ONLY public.permissions DROP CONSTRAINT IF EXISTS uq_permissions_code;
ALTER TABLE IF EXISTS ONLY public.payments DROP CONSTRAINT IF EXISTS uq_payments_uuid;
ALTER TABLE IF EXISTS ONLY public.leads DROP CONSTRAINT IF EXISTS uq_leads_uuid;
ALTER TABLE IF EXISTS ONLY public.follow_ups DROP CONSTRAINT IF EXISTS uq_follow_ups_uuid;
ALTER TABLE IF EXISTS ONLY public.expenses DROP CONSTRAINT IF EXISTS uq_expenses_uuid;
ALTER TABLE IF EXISTS ONLY public.departments DROP CONSTRAINT IF EXISTS uq_departments_uuid;
ALTER TABLE IF EXISTS ONLY public.departments DROP CONSTRAINT IF EXISTS uq_departments_code;
ALTER TABLE IF EXISTS ONLY public.department_members DROP CONSTRAINT IF EXISTS uq_department_members_uuid;
ALTER TABLE IF EXISTS ONLY public.clients DROP CONSTRAINT IF EXISTS uq_clients_uuid;
ALTER TABLE IF EXISTS ONLY public.client_queries DROP CONSTRAINT IF EXISTS uq_client_queries_uuid;
ALTER TABLE IF EXISTS ONLY public.client_manager_assignments DROP CONSTRAINT IF EXISTS uq_client_manager_assignments_uuid;
ALTER TABLE IF EXISTS ONLY public.client_deadlines DROP CONSTRAINT IF EXISTS uq_client_deadlines_uuid;
ALTER TABLE IF EXISTS ONLY public.roles DROP CONSTRAINT IF EXISTS roles_pkey;
ALTER TABLE IF EXISTS ONLY public.role_permissions DROP CONSTRAINT IF EXISTS role_permissions_pkey;
ALTER TABLE IF EXISTS ONLY public.refresh_tokens DROP CONSTRAINT IF EXISTS refresh_tokens_pkey;
ALTER TABLE IF EXISTS ONLY public.products DROP CONSTRAINT IF EXISTS products_pkey;
ALTER TABLE IF EXISTS ONLY public.product_types DROP CONSTRAINT IF EXISTS product_types_pkey;
ALTER TABLE IF EXISTS ONLY public.product_names DROP CONSTRAINT IF EXISTS product_names_pkey;
ALTER TABLE IF EXISTS ONLY public.permissions DROP CONSTRAINT IF EXISTS permissions_pkey;
ALTER TABLE IF EXISTS ONLY public.payments DROP CONSTRAINT IF EXISTS payments_pkey;
ALTER TABLE IF EXISTS ONLY public.leads DROP CONSTRAINT IF EXISTS leads_pkey;
ALTER TABLE IF EXISTS ONLY public.follow_ups DROP CONSTRAINT IF EXISTS follow_ups_pkey;
ALTER TABLE IF EXISTS ONLY public.flyway_schema_history DROP CONSTRAINT IF EXISTS flyway_schema_history_pk;
ALTER TABLE IF EXISTS ONLY public.expenses DROP CONSTRAINT IF EXISTS expenses_pkey;
ALTER TABLE IF EXISTS ONLY public.departments DROP CONSTRAINT IF EXISTS departments_pkey;
ALTER TABLE IF EXISTS ONLY public.department_members DROP CONSTRAINT IF EXISTS department_members_pkey;
ALTER TABLE IF EXISTS ONLY public.clients DROP CONSTRAINT IF EXISTS clients_pkey;
ALTER TABLE IF EXISTS ONLY public.client_queries DROP CONSTRAINT IF EXISTS client_queries_pkey;
ALTER TABLE IF EXISTS ONLY public.client_manager_assignments DROP CONSTRAINT IF EXISTS client_manager_assignments_pkey;
ALTER TABLE IF EXISTS ONLY public.client_deadlines DROP CONSTRAINT IF EXISTS client_deadlines_pkey;
ALTER TABLE IF EXISTS public.users ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.user_roles ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.roles ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.role_permissions ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.refresh_tokens ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.products ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.product_types ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.product_names ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.permissions ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.payments ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.leads ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.follow_ups ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.expenses ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.departments ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.department_members ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.clients ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.client_queries ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.client_manager_assignments ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.client_deadlines ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.users_id_seq;
DROP TABLE IF EXISTS public.users;
DROP SEQUENCE IF EXISTS public.user_roles_id_seq;
DROP TABLE IF EXISTS public.user_roles;
DROP SEQUENCE IF EXISTS public.roles_id_seq;
DROP TABLE IF EXISTS public.roles;
DROP SEQUENCE IF EXISTS public.role_permissions_id_seq;
DROP TABLE IF EXISTS public.role_permissions;
DROP SEQUENCE IF EXISTS public.refresh_tokens_id_seq;
DROP TABLE IF EXISTS public.refresh_tokens;
DROP SEQUENCE IF EXISTS public.products_id_seq;
DROP TABLE IF EXISTS public.products;
DROP SEQUENCE IF EXISTS public.product_types_id_seq;
DROP TABLE IF EXISTS public.product_types;
DROP SEQUENCE IF EXISTS public.product_names_id_seq;
DROP TABLE IF EXISTS public.product_names;
DROP SEQUENCE IF EXISTS public.permissions_id_seq;
DROP TABLE IF EXISTS public.permissions;
DROP SEQUENCE IF EXISTS public.payments_id_seq;
DROP TABLE IF EXISTS public.payments;
DROP SEQUENCE IF EXISTS public.leads_id_seq;
DROP TABLE IF EXISTS public.leads;
DROP SEQUENCE IF EXISTS public.follow_ups_id_seq;
DROP TABLE IF EXISTS public.follow_ups;
DROP TABLE IF EXISTS public.flyway_schema_history;
DROP SEQUENCE IF EXISTS public.expenses_id_seq;
DROP TABLE IF EXISTS public.expenses;
DROP SEQUENCE IF EXISTS public.departments_id_seq;
DROP TABLE IF EXISTS public.departments;
DROP SEQUENCE IF EXISTS public.department_members_id_seq;
DROP TABLE IF EXISTS public.department_members;
DROP SEQUENCE IF EXISTS public.clients_id_seq;
DROP TABLE IF EXISTS public.clients;
DROP SEQUENCE IF EXISTS public.client_queries_id_seq;
DROP TABLE IF EXISTS public.client_queries;
DROP SEQUENCE IF EXISTS public.client_manager_assignments_id_seq;
DROP TABLE IF EXISTS public.client_manager_assignments;
DROP SEQUENCE IF EXISTS public.client_deadlines_id_seq;
DROP TABLE IF EXISTS public.client_deadlines;
DROP EXTENSION IF EXISTS "uuid-ossp";
DROP EXTENSION IF EXISTS pgcrypto;
--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: client_deadlines; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.client_deadlines (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    client_id bigint NOT NULL,
    department_id bigint,
    current_deadline date NOT NULL,
    new_deadline date NOT NULL,
    reason character varying(1000) NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_client_deadlines_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE client_deadlines; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.client_deadlines IS 'Client project deadline change history';


--
-- Name: COLUMN client_deadlines.current_deadline; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.client_deadlines.current_deadline IS 'Previous / current deadline before change';


--
-- Name: COLUMN client_deadlines.new_deadline; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.client_deadlines.new_deadline IS 'Revised deadline after change';


--
-- Name: COLUMN client_deadlines.reason; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.client_deadlines.reason IS 'Reason for deadline extension or change';


--
-- Name: client_deadlines_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.client_deadlines_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: client_deadlines_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.client_deadlines_id_seq OWNED BY public.client_deadlines.id;


--
-- Name: client_manager_assignments; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.client_manager_assignments (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    client_id bigint NOT NULL,
    department_id bigint,
    user_id bigint NOT NULL,
    project_name character varying(200),
    close_date date,
    reward_amount numeric(14,2),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_client_manager_assignments_reward CHECK (((reward_amount IS NULL) OR (reward_amount >= (0)::numeric))),
    CONSTRAINT chk_client_manager_assignments_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE client_manager_assignments; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.client_manager_assignments IS 'Manager / team member assignments per client project';


--
-- Name: COLUMN client_manager_assignments.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.client_manager_assignments.user_id IS 'Assigned manager or team member';


--
-- Name: COLUMN client_manager_assignments.project_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.client_manager_assignments.project_name IS 'Optional project label for the assignment';


--
-- Name: COLUMN client_manager_assignments.close_date; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.client_manager_assignments.close_date IS 'Target project close date';


--
-- Name: COLUMN client_manager_assignments.reward_amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.client_manager_assignments.reward_amount IS 'Optional reward or incentive amount';


--
-- Name: client_manager_assignments_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.client_manager_assignments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: client_manager_assignments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.client_manager_assignments_id_seq OWNED BY public.client_manager_assignments.id;


--
-- Name: client_queries; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.client_queries (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    client_id bigint NOT NULL,
    title character varying(200) NOT NULL,
    query_type character varying(100),
    assigned_user_id bigint,
    department_id bigint,
    priority character varying(20) DEFAULT 'MEDIUM'::character varying NOT NULL,
    query_status character varying(30) DEFAULT 'PENDING'::character varying NOT NULL,
    remarks text,
    image_url character varying(500),
    completed_at timestamp with time zone,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_client_queries_priority CHECK (((priority)::text = ANY ((ARRAY['LOW'::character varying, 'MEDIUM'::character varying, 'HIGH'::character varying, 'URGENT'::character varying])::text[]))),
    CONSTRAINT chk_client_queries_query_status CHECK (((query_status)::text = ANY ((ARRAY['PENDING'::character varying, 'IN_PROGRESS'::character varying, 'NEEDS_ATTENTION'::character varying, 'COMPLETED'::character varying, 'SOLVED'::character varying])::text[]))),
    CONSTRAINT chk_client_queries_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE client_queries; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.client_queries IS 'Client queries and requirements';


--
-- Name: COLUMN client_queries.query_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.client_queries.query_type IS 'Optional type (REQUIREMENT, DOCUMENT, COMPLIANCE, OTHER, or free text)';


--
-- Name: COLUMN client_queries.query_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.client_queries.query_status IS 'Workflow status (PENDING, IN_PROGRESS, NEEDS_ATTENTION, COMPLETED, SOLVED)';


--
-- Name: COLUMN client_queries.image_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.client_queries.image_url IS 'Optional image path; file upload support planned later';


--
-- Name: client_queries_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.client_queries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: client_queries_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.client_queries_id_seq OWNED BY public.client_queries.id;


--
-- Name: clients; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.clients (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    client_name character varying(200) NOT NULL,
    contact_person character varying(150) NOT NULL,
    mobile character varying(20),
    email character varying(255) NOT NULL,
    gst_number character varying(20),
    client_type character varying(100) NOT NULL,
    product_id bigint NOT NULL,
    deal_date date,
    total_amount numeric(14,2) DEFAULT 0.00 NOT NULL,
    budget numeric(14,2),
    notes character varying(300),
    client_stage character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    priority character varying(20) DEFAULT 'MEDIUM'::character varying NOT NULL,
    requirements_completion_percentage numeric(5,2) DEFAULT 0.00 NOT NULL,
    state character varying(100),
    city character varying(100),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_clients_budget CHECK (((budget IS NULL) OR (budget >= (0)::numeric))),
    CONSTRAINT chk_clients_priority CHECK (((priority)::text = ANY ((ARRAY['LOW'::character varying, 'MEDIUM'::character varying, 'HIGH'::character varying, 'URGENT'::character varying])::text[]))),
    CONSTRAINT chk_clients_requirements_pct CHECK (((requirements_completion_percentage >= (0)::numeric) AND (requirements_completion_percentage <= (100)::numeric))),
    CONSTRAINT chk_clients_stage CHECK (((client_stage)::text = ANY ((ARRAY['NEW'::character varying, 'INTERESTED'::character varying, 'IN_PROGRESS'::character varying, 'ACTIVE'::character varying, 'ON_HOLD'::character varying, 'COMPLETED'::character varying, 'CHURNED'::character varying])::text[]))),
    CONSTRAINT chk_clients_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[]))),
    CONSTRAINT chk_clients_total_amount CHECK ((total_amount >= (0)::numeric))
);


--
-- Name: TABLE clients; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.clients IS 'CRM clients / companies';


--
-- Name: COLUMN clients.client_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.clients.client_type IS 'Business type (e.g. Catering Services)';


--
-- Name: COLUMN clients.client_stage; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.clients.client_stage IS 'Sales/delivery stage of the client engagement';


--
-- Name: COLUMN clients.requirements_completion_percentage; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.clients.requirements_completion_percentage IS 'Requirements completion percentage shown on client detail';


--
-- Name: clients_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.clients_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: clients_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.clients_id_seq OWNED BY public.clients.id;


--
-- Name: department_members; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.department_members (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    department_id bigint NOT NULL,
    user_id bigint NOT NULL,
    designation character varying(150) NOT NULL,
    is_lead boolean DEFAULT false NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_department_members_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE department_members; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.department_members IS 'Users assigned to departments with job designation';


--
-- Name: COLUMN department_members.designation; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.department_members.designation IS 'Job title within the department (e.g. Sales Manager)';


--
-- Name: COLUMN department_members.is_lead; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.department_members.is_lead IS 'Whether the member leads the department';


--
-- Name: department_members_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.department_members_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: department_members_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.department_members_id_seq OWNED BY public.department_members.id;


--
-- Name: departments; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.departments (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(150) NOT NULL,
    code character varying(50) NOT NULL,
    description character varying(500),
    parent_id bigint,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_departments_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE departments; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.departments IS 'Organizational departments / teams';


--
-- Name: COLUMN departments.parent_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.departments.parent_id IS 'Optional parent department for hierarchy';


--
-- Name: departments_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.departments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: departments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.departments_id_seq OWNED BY public.departments.id;


--
-- Name: expenses; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.expenses (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    client_id bigint,
    title character varying(200) NOT NULL,
    expense_type character varying(30) NOT NULL,
    member_user_id bigint,
    expense_date date NOT NULL,
    paid_date date,
    due_date date,
    amount numeric(14,2) NOT NULL,
    payment_mode character varying(50),
    from_city character varying(100),
    to_city character varying(100),
    from_date date,
    to_date date,
    km numeric(10,2),
    account_contact character varying(200),
    remarks character varying(1000),
    bill_url character varying(500),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_expenses_amount CHECK ((amount >= (0)::numeric)),
    CONSTRAINT chk_expenses_km CHECK (((km IS NULL) OR (km >= (0)::numeric))),
    CONSTRAINT chk_expenses_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[]))),
    CONSTRAINT chk_expenses_type CHECK (((expense_type)::text = ANY ((ARRAY['TRAVEL'::character varying, 'FOOD'::character varying, 'OFFICE'::character varying, 'OTHER'::character varying])::text[])))
);


--
-- Name: TABLE expenses; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.expenses IS 'Business expenses (travel, food, office, other)';


--
-- Name: COLUMN expenses.client_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.expenses.client_id IS 'Optional client link; null for office-wide expenses';


--
-- Name: COLUMN expenses.expense_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.expenses.expense_type IS 'Expense category (TRAVEL, FOOD, OFFICE, OTHER)';


--
-- Name: COLUMN expenses.member_user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.expenses.member_user_id IS 'Claimant / team member who incurred the expense';


--
-- Name: expenses_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.expenses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: expenses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.expenses_id_seq OWNED BY public.expenses.id;


--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


--
-- Name: follow_ups; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.follow_ups (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    client_id bigint,
    title character varying(200) NOT NULL,
    follow_up_type character varying(30) DEFAULT 'CALL'::character varying NOT NULL,
    assigned_user_id bigint,
    follow_up_date date NOT NULL,
    follow_up_time time without time zone,
    follow_up_status character varying(30) DEFAULT 'PENDING'::character varying NOT NULL,
    expected_budget numeric(14,2),
    remark text,
    next_follow_up_date date,
    next_follow_up_time time without time zone,
    reminder_minutes integer,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    lead_id bigint,
    CONSTRAINT chk_follow_ups_client_or_lead CHECK ((((client_id IS NOT NULL) AND (lead_id IS NULL)) OR ((client_id IS NULL) AND (lead_id IS NOT NULL)))),
    CONSTRAINT chk_follow_ups_expected_budget CHECK (((expected_budget IS NULL) OR (expected_budget >= (0)::numeric))),
    CONSTRAINT chk_follow_ups_follow_up_status CHECK (((follow_up_status)::text = ANY ((ARRAY['PENDING'::character varying, 'COMPLETED'::character varying, 'CANCELLED'::character varying, 'NO_RESPONSE'::character varying])::text[]))),
    CONSTRAINT chk_follow_ups_reminder_minutes CHECK (((reminder_minutes IS NULL) OR (reminder_minutes >= 0))),
    CONSTRAINT chk_follow_ups_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[]))),
    CONSTRAINT chk_follow_ups_type CHECK (((follow_up_type)::text = ANY ((ARRAY['CALL'::character varying, 'MEETING'::character varying, 'EMAIL'::character varying, 'VISIT'::character varying, 'OTHER'::character varying])::text[])))
);


--
-- Name: TABLE follow_ups; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.follow_ups IS 'Client follow-up activities (calls, meetings, emails, visits)';


--
-- Name: COLUMN follow_ups.follow_up_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.follow_ups.follow_up_type IS 'Type of follow-up (CALL, MEETING, EMAIL, VISIT, OTHER)';


--
-- Name: COLUMN follow_ups.follow_up_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.follow_ups.follow_up_status IS 'Outcome status (PENDING, COMPLETED, CANCELLED, NO_RESPONSE)';


--
-- Name: COLUMN follow_ups.reminder_minutes; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.follow_ups.reminder_minutes IS 'Reminder offset in minutes before scheduled follow-up';


--
-- Name: COLUMN follow_ups.lead_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.follow_ups.lead_id IS 'Meeting lead when follow-up is created before client conversion';


--
-- Name: follow_ups_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.follow_ups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: follow_ups_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.follow_ups_id_seq OWNED BY public.follow_ups.id;


--
-- Name: leads; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.leads (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    email character varying(255) NOT NULL,
    company_name character varying(200) NOT NULL,
    phone character varying(20) NOT NULL,
    state character varying(100) NOT NULL,
    city character varying(100) NOT NULL,
    approx_budget numeric(14,2) NOT NULL,
    product_id bigint,
    notes character varying(1000),
    lead_stage character varying(30) DEFAULT 'NEW'::character varying NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_leads_approx_budget CHECK ((approx_budget >= (0)::numeric)),
    CONSTRAINT chk_leads_stage CHECK (((lead_stage)::text = ANY ((ARRAY['NEW'::character varying, 'CONTACTED'::character varying, 'QUALIFIED'::character varying, 'CONVERTED'::character varying, 'LOST'::character varying])::text[]))),
    CONSTRAINT chk_leads_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE leads; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.leads IS 'Meeting leads / sales pipeline prospects';


--
-- Name: COLUMN leads.approx_budget; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leads.approx_budget IS 'Approximate budget indicated by the lead';


--
-- Name: COLUMN leads.lead_stage; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leads.lead_stage IS 'Sales pipeline stage (NEW, CONTACTED, QUALIFIED, CONVERTED, LOST)';


--
-- Name: leads_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.leads_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: leads_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.leads_id_seq OWNED BY public.leads.id;


--
-- Name: payments; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.payments (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    client_id bigint NOT NULL,
    product_id bigint,
    invoice_number character varying(50) NOT NULL,
    payment_date date NOT NULL,
    amount numeric(14,2) NOT NULL,
    bank_type character varying(50),
    payment_mode character varying(30) DEFAULT 'CASH'::character varying NOT NULL,
    remarks text,
    receipt_url character varying(500),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_payments_amount CHECK ((amount >= (0)::numeric)),
    CONSTRAINT chk_payments_mode CHECK (((payment_mode)::text = ANY ((ARRAY['CASH'::character varying, 'UPI'::character varying, 'BANK_TRANSFER'::character varying, 'CARD'::character varying, 'CHEQUE'::character varying, 'OTHER'::character varying])::text[]))),
    CONSTRAINT chk_payments_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE payments; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.payments IS 'Client payment receipts / invoices';


--
-- Name: COLUMN payments.invoice_number; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.payments.invoice_number IS 'Unique invoice or receipt number (e.g. INV-0231)';


--
-- Name: COLUMN payments.amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.payments.amount IS 'Paid amount for this receipt';


--
-- Name: COLUMN payments.bank_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.payments.bank_type IS 'Optional bank or channel label (e.g. GPay, NEFT, HDFC)';


--
-- Name: COLUMN payments.payment_mode; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.payments.payment_mode IS 'Payment mode (CASH, UPI, BANK_TRANSFER, CARD, CHEQUE, OTHER)';


--
-- Name: COLUMN payments.receipt_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.payments.receipt_url IS 'Optional uploaded receipt document URL';


--
-- Name: payments_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.payments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: payments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.payments_id_seq OWNED BY public.payments.id;


--
-- Name: permissions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.permissions (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(150) NOT NULL,
    code character varying(100) NOT NULL,
    module character varying(100) NOT NULL,
    description character varying(500),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_permissions_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE permissions; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.permissions IS 'Fine-grained permissions grouped by module';


--
-- Name: permissions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.permissions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: permissions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.permissions_id_seq OWNED BY public.permissions.id;


--
-- Name: product_names; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.product_names (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    product_type_id bigint NOT NULL,
    name character varying(150) NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_product_names_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE product_names; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.product_names IS 'Product names scoped to a product type for the Add Product Name dropdown';


--
-- Name: product_names_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.product_names_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: product_names_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.product_names_id_seq OWNED BY public.product_names.id;


--
-- Name: product_types; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.product_types (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(100) NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_product_types_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE product_types; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.product_types IS 'Product families/types for the Add Product Type dropdown';


--
-- Name: product_types_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.product_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: product_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.product_types_id_seq OWNED BY public.product_types.id;


--
-- Name: products; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.products (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(150) NOT NULL,
    code character varying(50) NOT NULL,
    product_type character varying(100) NOT NULL,
    description character varying(500),
    default_reward_amount numeric(12,2) DEFAULT 0.00 NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_products_reward_amount CHECK ((default_reward_amount >= (0)::numeric)),
    CONSTRAINT chk_products_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE products; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.products IS 'Sellable product catalog (Just Catering X, Pro, etc.)';


--
-- Name: COLUMN products.product_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.products.product_type IS 'Product family/type shown in Figma Type field';


--
-- Name: COLUMN products.default_reward_amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.products.default_reward_amount IS 'Default reward amount from Settings reward rules';


--
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;


--
-- Name: refresh_tokens; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.refresh_tokens (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id bigint NOT NULL,
    token character varying(500) NOT NULL,
    expires_at timestamp with time zone NOT NULL,
    revoked boolean DEFAULT false NOT NULL,
    revoked_at timestamp with time zone,
    replaced_by_token character varying(500),
    device_info character varying(255),
    ip_address character varying(45),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_refresh_tokens_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying, 'REVOKED'::character varying])::text[])))
);


--
-- Name: TABLE refresh_tokens; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.refresh_tokens IS 'JWT refresh tokens for session management';


--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.refresh_tokens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.refresh_tokens_id_seq OWNED BY public.refresh_tokens.id;


--
-- Name: role_permissions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.role_permissions (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    role_id bigint NOT NULL,
    permission_id bigint NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_role_permissions_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE role_permissions; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.role_permissions IS 'Many-to-many mapping between roles and permissions';


--
-- Name: role_permissions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.role_permissions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: role_permissions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.role_permissions_id_seq OWNED BY public.role_permissions.id;


--
-- Name: roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(100) NOT NULL,
    code character varying(50) NOT NULL,
    description character varying(500),
    is_system boolean DEFAULT false NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_roles_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE roles; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.roles IS 'System and custom roles for RBAC';


--
-- Name: COLUMN roles.is_system; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.roles.is_system IS 'System roles cannot be deleted';


--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_roles (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_user_roles_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


--
-- Name: TABLE user_roles; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.user_roles IS 'Many-to-many mapping between users and roles';


--
-- Name: user_roles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.user_roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: user_roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.user_roles_id_seq OWNED BY public.user_roles.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    uuid uuid DEFAULT gen_random_uuid() NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    email character varying(255) NOT NULL,
    password_hash character varying(255) NOT NULL,
    phone character varying(20),
    company_name character varying(255),
    profile_image_url character varying(500),
    email_verified boolean DEFAULT false NOT NULL,
    last_login_at timestamp with time zone,
    failed_login_attempts integer DEFAULT 0 NOT NULL,
    locked_until timestamp with time zone,
    password_changed_at timestamp with time zone,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by bigint,
    updated_by bigint,
    deleted boolean DEFAULT false NOT NULL,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT chk_users_failed_attempts CHECK ((failed_login_attempts >= 0)),
    CONSTRAINT chk_users_status CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'INACTIVE'::character varying, 'LOCKED'::character varying, 'PENDING'::character varying])::text[])))
);


--
-- Name: TABLE users; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.users IS 'Application users including Super Admin and staff';


--
-- Name: COLUMN users.password_hash; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.users.password_hash IS 'BCrypt hashed password';


--
-- Name: COLUMN users.locked_until; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.users.locked_until IS 'Account lock expiry after failed login attempts';


--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: client_deadlines id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_deadlines ALTER COLUMN id SET DEFAULT nextval('public.client_deadlines_id_seq'::regclass);


--
-- Name: client_manager_assignments id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_manager_assignments ALTER COLUMN id SET DEFAULT nextval('public.client_manager_assignments_id_seq'::regclass);


--
-- Name: client_queries id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_queries ALTER COLUMN id SET DEFAULT nextval('public.client_queries_id_seq'::regclass);


--
-- Name: clients id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.clients ALTER COLUMN id SET DEFAULT nextval('public.clients_id_seq'::regclass);


--
-- Name: department_members id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department_members ALTER COLUMN id SET DEFAULT nextval('public.department_members_id_seq'::regclass);


--
-- Name: departments id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.departments ALTER COLUMN id SET DEFAULT nextval('public.departments_id_seq'::regclass);


--
-- Name: expenses id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.expenses ALTER COLUMN id SET DEFAULT nextval('public.expenses_id_seq'::regclass);


--
-- Name: follow_ups id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.follow_ups ALTER COLUMN id SET DEFAULT nextval('public.follow_ups_id_seq'::regclass);


--
-- Name: leads id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.leads ALTER COLUMN id SET DEFAULT nextval('public.leads_id_seq'::regclass);


--
-- Name: payments id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.payments ALTER COLUMN id SET DEFAULT nextval('public.payments_id_seq'::regclass);


--
-- Name: permissions id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissions ALTER COLUMN id SET DEFAULT nextval('public.permissions_id_seq'::regclass);


--
-- Name: product_names id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_names ALTER COLUMN id SET DEFAULT nextval('public.product_names_id_seq'::regclass);


--
-- Name: product_types id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_types ALTER COLUMN id SET DEFAULT nextval('public.product_types_id_seq'::regclass);


--
-- Name: products id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);


--
-- Name: refresh_tokens id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_tokens ALTER COLUMN id SET DEFAULT nextval('public.refresh_tokens_id_seq'::regclass);


--
-- Name: role_permissions id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permissions ALTER COLUMN id SET DEFAULT nextval('public.role_permissions_id_seq'::regclass);


--
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- Name: user_roles id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles ALTER COLUMN id SET DEFAULT nextval('public.user_roles_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: client_deadlines; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.client_deadlines (id, uuid, client_id, department_id, current_deadline, new_deadline, reason, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
\.


--
-- Data for Name: client_manager_assignments; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.client_manager_assignments (id, uuid, client_id, department_id, user_id, project_name, close_date, reward_amount, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	9e6889aa-65eb-4dfc-a38d-2610abf67738	1	1	2	Tiptop — just catering pro	2026-07-19	25000.00	2026-07-19 12:29:33.578681+00	2026-07-19 12:29:33.578686+00	1	1	f	ACTIVE	0
2	ce2f2839-bc74-4325-b506-e5b85afc5dc3	2	2	3	royal catered — just catering x	2026-07-20	10000.00	2026-07-19 13:02:21.610921+00	2026-07-19 14:10:21.818367+00	1	1	f	ACTIVE	1
3	b5e0d169-b446-4ab0-bc85-ecc4425bbb76	2	1	2	royal catered — just catering x	2026-07-20	10000.00	2026-07-19 13:02:21.621674+00	2026-07-19 14:10:21.821789+00	1	1	f	ACTIVE	1
\.


--
-- Data for Name: client_queries; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.client_queries (id, uuid, client_id, title, query_type, assigned_user_id, department_id, priority, query_status, remarks, image_url, completed_at, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	f888d390-e670-4504-98cd-ed4d9199bb02	2	regret	COMPLIANCE	2	\N	HIGH	PENDING	\N	/api/files/3e786282-7156-4633-9275-e8615ceda02c.jpg	\N	2026-07-19 13:06:11.681311+00	2026-07-19 13:06:11.681317+00	1	1	f	ACTIVE	0
2	7776bfb7-8827-4c17-b3ab-e92c3de1da76	2	ewgweg	COMPLIANCE	2	\N	MEDIUM	PENDING	wegewg	/api/files/63a353e6-4a3d-42f2-96d2-8eecd45303f8.jpg	\N	2026-07-19 14:07:14.787681+00	2026-07-19 14:07:14.787688+00	1	1	f	ACTIVE	0
\.


--
-- Data for Name: clients; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.clients (id, uuid, client_name, contact_person, mobile, email, gst_number, client_type, product_id, deal_date, total_amount, budget, notes, client_stage, priority, requirements_completion_percentage, state, city, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	be636c8d-e135-431c-ac94-a83a6276cede	Tiptop	mr Singh	+9107923445654	tiptop@gmail.com	\N	just catering	1	2026-07-19	2500000.00	\N	jdrdrjjrddrj Rudr	ACTIVE	HIGH	0.00	\N	\N	2026-07-19 12:18:28.665633+00	2026-07-19 12:18:28.66565+00	1	1	f	ACTIVE	0
2	1f626d84-2bdd-4909-890c-2420f9ff9168	royal catered	mr.shah shah	+9194758383885	shah@gmail.com	\N	just catering	2	2026-07-20	2000000.00	\N	reh herbert	ACTIVE	URGENT	0.00	\N	\N	2026-07-19 12:58:15.203327+00	2026-07-19 12:58:15.203338+00	1	1	f	ACTIVE	0
\.


--
-- Data for Name: department_members; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.department_members (id, uuid, department_id, user_id, designation, is_lead, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
2	e1e1dd00-5b66-468a-b46a-c83dcf8f7405	2	3	Manager	t	2026-07-19 13:01:48.40434+00	2026-07-19 13:01:48.404344+00	1	1	f	ACTIVE	0
3	4d1b338c-6fee-4c1d-96ac-2bf0c8711415	1	4	Manager	t	2026-07-19 13:13:23.650717+00	2026-07-19 13:13:23.65073+00	1	1	f	ACTIVE	0
1	c35f47d6-306f-4c15-8562-a574054ff839	1	2	Admin	f	2026-07-19 10:53:55.081949+00	2026-07-19 13:13:23.661342+00	1	1	f	ACTIVE	1
\.


--
-- Data for Name: departments; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.departments (id, uuid, name, code, description, parent_id, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	46a2b790-79d9-4c78-ac07-54ea288bc22e	deveoper	DEVEOPER	\N	\N	2026-07-19 10:53:55.038249+00	2026-07-19 10:53:55.038253+00	1	1	f	ACTIVE	0
2	e385ca72-d554-4ce3-8682-e8af4da5ffb2	sales	SALES	\N	\N	2026-07-19 13:01:48.34741+00	2026-07-19 13:01:48.347414+00	1	1	f	ACTIVE	0
\.


--
-- Data for Name: expenses; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.expenses (id, uuid, client_id, title, expense_type, member_user_id, expense_date, paid_date, due_date, amount, payment_mode, from_city, to_city, from_date, to_date, km, account_contact, remarks, bill_url, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	a2b5ca7c-2ffa-47b3-ae6f-65451c6ae918	1	petrol	TRAVEL	2	2026-07-19	\N	\N	1500.00	G Pay	Ahmedabad	Ahmedabad	2026-07-19	2026-07-19	20.00	\N	Ohio Ohio	/api/files/1e3da497-b290-45d7-8791-96bf9b39b32b.jpg	2026-07-19 12:38:23.904834+00	2026-07-19 12:38:23.90484+00	1	1	f	ACTIVE	0
\.


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	create auth tables	SQL	V1__create_auth_tables.sql	1700049869	just_catering	2026-07-18 21:01:40.771691	192	t
2	2	seed roles permissions admin	SQL	V2__seed_roles_permissions_admin.sql	-1465181459	just_catering	2026-07-18 21:01:40.987578	12	t
3	3	partial unique join indexes	SQL	V3__partial_unique_join_indexes.sql	1406799624	just_catering	2026-07-18 21:06:04.010039	17	t
4	4	create department tables	SQL	V4__create_department_tables.sql	46522184	just_catering	2026-07-18 21:11:18.531193	46	t
5	5	seed departments	SQL	V5__seed_departments.sql	-1834405201	just_catering	2026-07-18 21:11:18.597707	2	t
6	6	create product tables	SQL	V6__create_product_tables.sql	-110267792	just_catering	2026-07-18 21:32:26.743336	32	t
7	7	seed products	SQL	V7__seed_products.sql	-471525044	just_catering	2026-07-18 21:32:26.79198	2	t
8	8	create client tables	SQL	V8__create_client_tables.sql	1191228820	just_catering	2026-07-18 21:35:11.802035	33	t
9	9	seed clients	SQL	V9__seed_clients.sql	-564258209	just_catering	2026-07-18 21:35:11.852	4	t
10	10	create lead tables	SQL	V10__create_lead_tables.sql	1631283870	just_catering	2026-07-18 21:43:29.295282	37	t
11	11	seed leads	SQL	V11__seed_leads.sql	-725868034	just_catering	2026-07-18 21:43:29.349341	5	t
12	12	create follow up tables	SQL	V12__create_follow_up_tables.sql	718234823	just_catering	2026-07-18 21:43:29.361783	29	t
13	13	seed follow ups	SQL	V13__seed_follow_ups.sql	750375656	just_catering	2026-07-18 21:43:29.40142	5	t
14	14	create client query tables	SQL	V14__create_client_query_tables.sql	-2008808949	just_catering	2026-07-18 21:43:29.412581	29	t
15	15	seed client queries	SQL	V15__seed_client_queries.sql	-791229153	just_catering	2026-07-18 21:43:29.448328	5	t
16	16	create payment tables	SQL	V16__create_payment_tables.sql	1192243431	just_catering	2026-07-18 21:43:29.463416	19	t
17	17	seed payments	SQL	V17__seed_payments.sql	1925330017	just_catering	2026-07-18 21:43:29.488051	3	t
18	18	create expense tables	SQL	V18__create_expense_tables.sql	1738754381	just_catering	2026-07-18 21:43:29.496368	19	t
19	19	seed expenses	SQL	V19__seed_expenses.sql	-1029375378	just_catering	2026-07-18 21:43:29.521283	4	t
20	20	create project ops tables	SQL	V20__create_project_ops_tables.sql	-1458836985	just_catering	2026-07-18 21:43:29.530207	27	t
21	21	seed project ops	SQL	V21__seed_project_ops.sql	-1775170853	just_catering	2026-07-18 21:43:29.563446	4	t
22	22	add new client stage	SQL	V22__add_new_client_stage.sql	-274539799	just_catering	2026-07-19 10:24:56.320459	37	t
23	23	alter client notes length	SQL	V23__alter_client_notes_length.sql	425346725	just_catering	2026-07-19 10:24:56.378751	41	t
24	24	add calendar permission	SQL	V24__add_calendar_permission.sql	1692524619	just_catering	2026-07-19 10:45:13.989136	24	t
25	25	clear sample data	SQL	V25__clear_sample_data.sql	1726287411	just_catering	2026-07-19 15:37:41.114945	216	t
26	26	create product type name tables	SQL	V26__create_product_type_name_tables.sql	1844388108	just_catering	2026-07-19 16:00:43.104557	77	t
27	27	follow ups support leads	SQL	V27__follow_ups_support_leads.sql	900067739	just_catering	2026-07-19 17:36:31.602017	47	t
\.


--
-- Data for Name: follow_ups; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.follow_ups (id, uuid, client_id, title, follow_up_type, assigned_user_id, follow_up_date, follow_up_time, follow_up_status, expected_budget, remark, next_follow_up_date, next_follow_up_time, reminder_minutes, created_at, updated_at, created_by, updated_by, deleted, status, version, lead_id) FROM stdin;
1	6a954b50-2fb3-4bc9-b6e8-b19780520269	\N	meeting	CALL	2	2026-07-19	05:30:00	PENDING	\N	rehen reh heeth	2026-07-22	10:00:00	30	2026-07-19 12:08:07.574973+00	2026-07-19 12:08:07.574987+00	1	1	f	ACTIVE	0	1
2	4b1932a5-b184-453c-ad84-baf9d42be678	\N	meeting	MEETING	2	2026-07-19	05:30:00	COMPLETED	\N	Herbert dheeth reh	2026-07-22	10:00:00	30	2026-07-19 12:57:23.790583+00	2026-07-19 12:57:23.790591+00	1	1	f	ACTIVE	0	2
\.


--
-- Data for Name: leads; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.leads (id, uuid, first_name, last_name, email, company_name, phone, state, city, approx_budget, product_id, notes, lead_stage, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	275acf30-c85b-42d6-9983-6e28ecd25dc1	mr	Singh	tiptop@gmail.com	Tiptop	+9107923445654	Maharashtra	Mumbai	2500000.00	\N	\N	CONVERTED	2026-07-19 11:14:29.590053+00	2026-07-19 12:18:28.722241+00	1	1	f	ACTIVE	1
2	bffa612d-9dec-4c91-af1a-e7cc079538f7	mr.shah	shah	shah@gmail.com	royal catered	+9194758383885	Maharashtra	Mumbai	2000000.00	2	\N	CONVERTED	2026-07-19 12:54:58.422027+00	2026-07-19 12:58:15.237901+00	1	1	f	ACTIVE	1
\.


--
-- Data for Name: payments; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.payments (id, uuid, client_id, product_id, invoice_number, payment_date, amount, bank_type, payment_mode, remarks, receipt_url, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	338b1843-529f-4860-bd81-03e1892ca86b	1	1	INV-1784464036927	2026-07-19	50000.00	Savings Account	CASH	Recorded by: Mihir Soni	/api/files/900e2b83-6130-4c90-8a0e-b301f35962b3.jpg	2026-07-19 12:27:17.061734+00	2026-07-19 12:27:17.061741+00	1	1	f	ACTIVE	0
2	9e5cab94-16a7-42b3-b23a-c1e82472adff	2	2	INV-1784472840418	2026-07-19	2000000.00	HDFC Bank	BANK_TRANSFER	Recorded by: Mihir Soni	/api/files/4468720f-b28b-470b-973f-1235b8293292.jpg	2026-07-19 14:54:00.72234+00	2026-07-19 14:54:00.722361+00	1	1	f	ACTIVE	0
\.


--
-- Data for Name: permissions; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.permissions (id, uuid, name, code, module, description, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	1c1eacf7-6adb-4975-9dba-07e7fe01c2fa	View Users	USER_VIEW	USER	View user list and details	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
2	983ab6ac-fb21-4d1a-943d-3da16e0a1a23	Create User	USER_CREATE	USER	Create new users	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
3	cb36e0a3-a966-46a7-908b-fc52f098183e	Update User	USER_UPDATE	USER	Update existing users	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
4	75c6d6f9-edc0-4ade-82c2-d9880d917164	Delete User	USER_DELETE	USER	Soft-delete users	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
5	104e166a-3aef-403d-97ba-7c01b2c36176	View Roles	ROLE_VIEW	ROLE	View roles and permissions	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
6	6fd1607a-e0b2-4972-94e6-0a0b088ed98b	Manage Roles	ROLE_MANAGE	ROLE	Create and update roles	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
7	0cbbb4e2-fbb0-47a1-8a23-cf0360402a0c	View Clients	CLIENT_VIEW	CLIENT	View client list and details	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
8	5562e2bc-422f-42e2-83a0-f4fb6aeb1855	Create Client	CLIENT_CREATE	CLIENT	Create clients	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
9	14c39e90-fbb2-4e63-b878-7f2b5b0a92c6	Update Client	CLIENT_UPDATE	CLIENT	Update clients	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
10	f2765c5e-689f-4868-8589-e1c95617d68c	Delete Client	CLIENT_DELETE	CLIENT	Soft-delete clients	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
11	333e6464-c5f9-49aa-8b5b-f1bdb18b2575	View Leads	LEAD_VIEW	LEAD	View meeting leads	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
12	628efb61-59de-451f-83ee-84959485739d	Create Lead	LEAD_CREATE	LEAD	Create meeting leads	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
13	8886a117-4a12-4d37-af35-5d8ba219b17e	Update Lead	LEAD_UPDATE	LEAD	Update meeting leads	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
14	9d1b2f36-3a04-4419-97cd-b9d94b632d51	Delete Lead	LEAD_DELETE	LEAD	Soft-delete meeting leads	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
15	c1d524c1-fe6d-4cbf-b1bc-122805839fc7	View Follow-ups	FOLLOWUP_VIEW	FOLLOWUP	View follow-ups	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
16	444aa39a-98bd-4864-91f3-09c48743df58	Manage Follow-ups	FOLLOWUP_MANAGE	FOLLOWUP	Create and update follow-ups	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
17	02ee7df7-0340-4955-b91e-959c4eb33c9e	View Queries	QUERY_VIEW	QUERY	View queries and requirements	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
18	028dea82-b337-4178-b329-16e2bf52d8ac	Manage Queries	QUERY_MANAGE	QUERY	Create and update queries	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
19	d061f12d-9ccf-45f3-817d-592e596deb04	View Payments	PAYMENT_VIEW	PAYMENT	View payments and receipts	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
20	fa2d56c5-afc1-4011-8dc6-ecb73e5e722e	Manage Payments	PAYMENT_MANAGE	PAYMENT	Create and update payments	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
21	b8ac6e06-1dfd-4f20-82a3-dd1ec0ee09dc	View Expenses	EXPENSE_VIEW	EXPENSE	View expenses	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
22	fdc1b59e-f263-4aa1-8739-6858b0d20e40	Manage Expenses	EXPENSE_MANAGE	EXPENSE	Create and update expenses	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
23	fc34ebac-2525-46d0-8b93-02066fbb365a	View Departments	DEPARTMENT_VIEW	DEPARTMENT	View departments	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
24	04a8c80f-3d71-415f-b646-550e9c8207f5	Manage Departments	DEPARTMENT_MANAGE	DEPARTMENT	Create and update departments	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
25	5c94f403-ac2e-4961-93fc-b69cd27ecc05	View Products	PRODUCT_VIEW	PRODUCT	View products	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
26	01ab1374-7279-49cc-983b-2e46f3106ac0	Manage Products	PRODUCT_MANAGE	PRODUCT	Create and update products	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
27	b11082c7-ddfe-4ca9-86a0-33cee3d5b914	View Dashboard	DASHBOARD_VIEW	DASHBOARD	View dashboard overview	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
28	ce242e29-6590-4b27-bb01-9923bcc96282	View Settings	SETTINGS_VIEW	SETTINGS	View system settings	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
29	1da50f84-d32b-4bc9-93f9-a1291072f7f0	Manage Settings	SETTINGS_MANAGE	SETTINGS	Update system settings	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
30	1cc9bb63-aea4-4110-9430-9cf0bba1d1f0	View Calendar	CALENDAR_VIEW	CALENDAR	View calendar summary and daily schedule	2026-07-19 05:15:13.998411+00	2026-07-19 05:15:13.998411+00	\N	\N	f	ACTIVE	0
\.


--
-- Data for Name: product_names; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.product_names (id, uuid, product_type_id, name, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	2c0412af-27fb-4347-9230-2f2e6e7c14d7	1	Just Catering Pro	2026-07-19 10:30:54.155357+00	2026-07-19 10:30:54.155368+00	1	1	f	ACTIVE	0
2	f14999a2-e56b-4dfa-9314-bf1fa799b2ea	2	just catering x	2026-07-19 10:40:28.213741+00	2026-07-19 10:40:28.213758+00	1	1	f	ACTIVE	0
3	2502ea8f-73ab-498e-8fc4-1908fb6e5e49	2	just catering pro	2026-07-19 10:40:36.654843+00	2026-07-19 10:40:36.65486+00	1	1	f	ACTIVE	0
4	0354031e-310d-467b-a766-cbd030a241b9	3	Just Tap lite	2026-07-19 13:11:44.961957+00	2026-07-19 13:11:44.961963+00	1	1	f	ACTIVE	0
5	d290dc38-8a51-44b1-a344-6fcaea62a62d	3	Just Tap Pro	2026-07-19 13:11:55.27968+00	2026-07-19 13:11:55.279699+00	1	1	f	ACTIVE	0
\.


--
-- Data for Name: product_types; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.product_types (id, uuid, name, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	39158ba8-0fc8-4cf9-a08f-aab3d51b9f56	sadhegi	2026-07-19 10:30:53.96755+00	2026-07-19 10:30:53.96761+00	1	1	f	ACTIVE	0
2	0eb57a4c-4ca4-4741-b8b7-b0bcf84938e5	just catering	2026-07-19 10:38:18.431945+00	2026-07-19 10:38:18.431951+00	1	1	f	ACTIVE	0
3	40e38947-0e5e-4f10-b8d8-a4dc066465a2	just Tap	2026-07-19 13:11:18.199155+00	2026-07-19 13:11:18.199162+00	1	1	f	ACTIVE	0
\.


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.products (id, uuid, name, code, product_type, description, default_reward_amount, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	83ed0554-7a84-49d3-95a3-2a8973da1586	just catering pro	JUST_CATERING_PRO	just catering	\N	5000.00	2026-07-19 10:40:43.334344+00	2026-07-19 10:40:43.334349+00	1	1	f	ACTIVE	0
2	74066fa5-e457-47fd-8a65-e3e38adadd56	just catering x	JUST_CATERING_X	just catering	\N	5000.00	2026-07-19 10:40:43.42498+00	2026-07-19 10:40:43.424991+00	1	1	f	ACTIVE	0
3	cad4aacc-0ca9-4454-b9b3-f737ae517fc2	Just Tap Pro	JUST_TAP_PRO	just Tap	\N	5000.00	2026-07-19 13:12:02.143981+00	2026-07-19 13:12:02.143986+00	1	1	f	ACTIVE	0
4	e67c2819-e0b8-4b24-95b3-f545efddb6d7	Just Tap lite	JUST_TAP_LITE	just Tap	\N	5000.00	2026-07-19 13:12:02.205898+00	2026-07-19 13:12:02.205901+00	1	1	f	ACTIVE	0
\.


--
-- Data for Name: refresh_tokens; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.refresh_tokens (id, uuid, user_id, token, expires_at, revoked, revoked_at, replaced_by_token, device_info, ip_address, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	e8c3f1ab-39a5-4d30-a4cc-f914e972bb23	1	eb9d56ec-2d1a-48ed-967d-6e1bd3265031	2026-07-26 10:07:49.530611+00	f	\N	\N	curl/8.7.1	0:0:0:0:0:0:0:1	2026-07-19 10:07:49.532716+00	2026-07-19 10:07:49.532719+00	\N	\N	f	ACTIVE	0
3	83df39f0-131b-4487-abaa-a999a806bf7c	1	efea172a-613b-430f-bf7e-b394574972c4	2026-07-26 10:30:53.799795+00	f	\N	\N	curl/8.7.1	0:0:0:0:0:0:0:1	2026-07-19 10:30:53.810847+00	2026-07-19 10:30:53.810858+00	\N	\N	f	ACTIVE	0
4	1af4c363-f789-4d7c-9a8b-833d829a1700	1	6d96776d-8fbc-451c-bad1-44dec9b92e17	2026-07-26 10:31:06.386733+00	f	\N	\N	curl/8.7.1	0:0:0:0:0:0:0:1	2026-07-19 10:31:06.392343+00	2026-07-19 10:31:06.39235+00	\N	\N	f	ACTIVE	0
2	e9866fe2-0a9a-4d89-9f85-66f85a08c314	1	36243fde-43b8-43fd-ac95-b52146890514	2026-07-26 10:16:25.981445+00	t	2026-07-19 10:35:03.547152+00	3c6076ad-f7c3-4688-b0b3-9ee5e31bea75	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 10:16:25.995172+00	2026-07-19 10:35:03.566168+00	\N	\N	f	ACTIVE	1
6	f0886e3a-7a80-4e3b-8ffb-c955f7ad4969	1	6eb65580-7f3c-4c35-9966-bdba8697887c	2026-07-26 10:44:41.216312+00	f	\N	\N	curl/8.7.1	0:0:0:0:0:0:0:1	2026-07-19 10:44:41.23372+00	2026-07-19 10:44:41.233735+00	\N	\N	f	ACTIVE	0
5	63881451-5176-49d0-9362-2b0b712ee03d	1	3c6076ad-f7c3-4688-b0b3-9ee5e31bea75	2026-07-26 10:35:03.548904+00	t	2026-07-19 10:52:37.467541+00	8f0872c1-e552-49e9-bb71-8e9654ed6445	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 10:35:03.551579+00	2026-07-19 10:52:37.572525+00	\N	\N	f	ACTIVE	1
7	73b34225-0913-4121-ac87-ca2c29d7a7f8	1	8f0872c1-e552-49e9-bb71-8e9654ed6445	2026-07-26 10:52:37.480891+00	t	2026-07-19 11:09:56.526454+00	45c4b321-a7cf-442c-b582-a84f36e0a63a	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 10:52:37.516551+00	2026-07-19 11:09:56.631781+00	\N	\N	f	ACTIVE	1
8	f1d741df-0cf0-45d0-8825-c7a9fbdfe513	1	45c4b321-a7cf-442c-b582-a84f36e0a63a	2026-07-26 11:09:56.553895+00	t	2026-07-19 11:39:29.171989+00	efb6c299-fb9c-4874-a68c-19dd7c583d9d	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 11:09:56.610366+00	2026-07-19 11:39:29.273754+00	\N	\N	f	ACTIVE	1
9	0500d68d-1d4a-4b4e-8407-a2c1694856b3	1	efb6c299-fb9c-4874-a68c-19dd7c583d9d	2026-07-26 11:39:29.179452+00	t	2026-07-19 11:55:38.392845+00	3f584a32-20a8-4a76-8828-ebb5435c53f9	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 11:39:29.243693+00	2026-07-19 11:55:38.473246+00	\N	\N	f	ACTIVE	1
10	8889a8e0-c66e-41ca-8b5b-a10bbca91fa9	1	3f584a32-20a8-4a76-8828-ebb5435c53f9	2026-07-26 11:55:38.401467+00	t	2026-07-19 12:10:53.256504+00	a8ae90bd-0ccc-4f26-8e34-839ff52c1ec1	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 11:55:38.435082+00	2026-07-19 12:10:53.453672+00	\N	\N	f	ACTIVE	1
12	736de782-df47-4972-a52a-9e71d289bd43	1	e7675431-0349-4bfc-ac9e-81965c3e5fb4	2026-07-26 12:25:27.86728+00	f	\N	\N	curl/8.7.1	0:0:0:0:0:0:0:1	2026-07-19 12:25:27.879048+00	2026-07-19 12:25:27.879069+00	\N	\N	f	ACTIVE	0
11	cca2cf14-f54f-49c0-9b96-02f9140dc701	1	a8ae90bd-0ccc-4f26-8e34-839ff52c1ec1	2026-07-26 12:10:53.30052+00	t	2026-07-19 12:26:12.429676+00	c291e199-4a3e-416e-a64c-6bbdf9b60c18	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 12:10:53.3126+00	2026-07-19 12:26:12.45161+00	\N	\N	f	ACTIVE	1
13	e780d12a-7056-4b41-8205-27434d9ecca0	1	c291e199-4a3e-416e-a64c-6bbdf9b60c18	2026-07-26 12:26:12.433761+00	t	2026-07-19 12:51:30.093965+00	dddbd017-c6a7-4344-8530-d1e2a7d28092	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 12:26:12.438624+00	2026-07-19 12:51:30.184192+00	\N	\N	f	ACTIVE	1
14	54714a23-9aef-4051-9459-fdc0b41e4add	1	dddbd017-c6a7-4344-8530-d1e2a7d28092	2026-07-26 12:51:30.09995+00	t	2026-07-19 13:06:43.445811+00	955c6451-8179-492c-bd3d-6cb49161f1c2	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 12:51:30.155494+00	2026-07-19 13:06:43.492263+00	\N	\N	f	ACTIVE	1
16	a6af4591-2281-4e85-8016-40ecfbc9a99d	1	70195c82-223b-46a0-a252-845a2f835933	2026-07-26 13:38:23.274434+00	f	\N	\N	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 13:38:23.302614+00	2026-07-19 13:38:23.302679+00	\N	\N	f	ACTIVE	0
15	eb56d831-f6a4-4bf0-a94e-ae7a35515490	1	955c6451-8179-492c-bd3d-6cb49161f1c2	2026-07-26 13:06:43.447434+00	t	2026-07-19 13:38:23.267172+00	70195c82-223b-46a0-a252-845a2f835933	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 13:06:43.466318+00	2026-07-19 13:38:23.334996+00	\N	\N	f	ACTIVE	1
17	13238670-5871-4eac-95ee-c067c6552525	1	406ddd34-db67-40d0-9e42-3627055a2f02	2026-07-26 13:39:10.851525+00	t	2026-07-19 13:54:32.166515+00	78798e80-75e5-4823-bb1a-ac9672c7a2b0	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 13:39:10.863551+00	2026-07-19 13:54:32.25245+00	\N	\N	f	ACTIVE	1
18	c87bc025-5815-473b-951d-e01f7611a447	1	78798e80-75e5-4823-bb1a-ac9672c7a2b0	2026-07-26 13:54:32.176324+00	t	2026-07-19 14:09:35.122125+00	90a6cf4f-41e2-48c5-9357-36b652f63ded	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 13:54:32.202127+00	2026-07-19 14:09:35.156704+00	\N	\N	f	ACTIVE	1
19	b253059d-e2b6-469b-9b63-57d899eed4ca	1	90a6cf4f-41e2-48c5-9357-36b652f63ded	2026-07-26 14:09:35.123436+00	t	2026-07-19 14:24:49.436133+00	b359b3d1-c65e-4e1d-a2ca-c42c67f355b9	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 14:09:35.127302+00	2026-07-19 14:24:49.668009+00	\N	\N	f	ACTIVE	1
21	150dda9a-170d-43b6-ac99-0df2418a1e01	1	832dc88c-8115-44c7-b176-c02e224523d6	2026-07-26 14:40:32.353335+00	f	\N	\N	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 14:40:32.441995+00	2026-07-19 14:40:32.442028+00	\N	\N	f	ACTIVE	0
20	07789d8e-c547-44e3-90d1-fff06ab2f24e	1	b359b3d1-c65e-4e1d-a2ca-c42c67f355b9	2026-07-26 14:24:49.437163+00	t	2026-07-19 14:40:32.301095+00	832dc88c-8115-44c7-b176-c02e224523d6	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 14:24:49.51826+00	2026-07-19 14:40:32.505534+00	\N	\N	f	ACTIVE	1
22	f2f975fc-8c15-435b-a144-863bddee4a05	1	bbd30964-66be-4265-be19-f6aa5f41f885	2026-07-26 14:58:37.19387+00	t	2026-07-19 17:31:52.182671+00	3ae76dbd-dbdf-48c4-ba3f-6f2caf69c660	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 14:58:37.208313+00	2026-07-19 17:31:52.24981+00	\N	\N	f	ACTIVE	1
24	93357721-04b3-4bc1-af7e-3411cd056bf7	1	89e95fcf-abd0-42b8-9476-ddd0256b85ce	2026-07-26 17:48:43.972949+00	f	\N	\N	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 17:48:44.040432+00	2026-07-19 17:48:44.040459+00	\N	\N	f	ACTIVE	0
23	884004f5-8ed0-4be3-a71c-4c75dbc75c5c	1	3ae76dbd-dbdf-48c4-ba3f-6f2caf69c660	2026-07-26 17:31:52.188466+00	t	2026-07-19 17:48:43.948898+00	89e95fcf-abd0-42b8-9476-ddd0256b85ce	JustCateringSuperAdmin/1.0	127.0.0.1	2026-07-19 17:31:52.202818+00	2026-07-19 17:48:44.13679+00	\N	\N	f	ACTIVE	1
\.


--
-- Data for Name: role_permissions; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.role_permissions (id, uuid, role_id, permission_id, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	bc70979f-d162-47e0-a4d2-b6e73eebfa0a	1	1	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
2	11bc19b1-fccf-47dc-a9be-0d07013bf5ca	1	2	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
3	ae697339-6201-4afc-a1cb-e888b23ccbe5	1	3	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
4	991858c1-48d5-4b4a-a7b3-e1a7468da2ab	1	4	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
5	7aae6aed-b959-4aac-a6bb-6f944d2aaf2b	1	5	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
6	fad54047-e1be-4b98-9632-2b1b291c7b16	1	6	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
7	4b355514-e703-4d02-ba1d-8355701855ef	1	7	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
8	1c618a3d-8f0d-4da6-8264-e97da5a80fd2	1	8	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
9	32f9c57a-0c98-4c65-97f7-26d6883d56d4	1	9	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
10	1b71bf8a-ae94-4f7c-871e-0a36b491dd3a	1	10	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
11	363b5c08-d772-4a80-8804-f0ce353e1ad4	1	11	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
12	7a828bdc-759e-4d77-af93-16db698d56c5	1	12	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
13	3207f19a-81ff-49c9-bcee-20dea17bb3dd	1	13	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
14	d1a8cf68-fcf2-4d27-b4a3-2a54859aee96	1	14	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
15	89c420e0-1c3a-4a7d-b37e-c4baa6eefba1	1	15	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
16	63b0fead-1037-46bb-8efd-f8ffc1aa13db	1	16	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
17	15106094-f35a-4af4-98aa-945b89eea5e0	1	17	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
18	f5dce1b3-9819-4b9e-9fc0-6c62a53227c6	1	18	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
19	838a73f6-804d-47ba-aef9-2e37d2fc9de1	1	19	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
20	85a861d6-8530-4596-be1b-4121f83e077d	1	20	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
21	26d62e76-c80b-4988-b0e5-33490079bc64	1	21	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
22	5511a947-c7c9-45f8-bd9a-219d30652ecf	1	22	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
23	5d090c47-c97a-4cd6-84b7-8c664d3df079	1	23	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
24	eb612bd0-47a0-4ded-84a6-a437881d970c	1	24	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
25	5cc55cff-a18f-4dba-b642-db3002f196b6	1	25	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
26	181b0e22-f9e4-4497-ba25-2d67628c7248	1	26	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
27	4d2feb13-dad8-4f39-8f40-f0d408e6bb49	1	27	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
28	e91f5c41-c313-49ab-93f5-ee815a28233c	1	28	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
29	6947db48-900e-415f-8153-e81bd0925332	1	29	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
30	e8322ec2-9eea-40c6-ae34-f67a87066bfb	2	1	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
31	b783ade4-807a-495d-9357-d4f7a5ace22e	2	2	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
32	326d4699-f7f5-4980-a195-aba0fdcf9d0d	2	3	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
33	b131b873-5083-4ccf-9b01-089537cd9395	2	4	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
34	be36fc47-86e7-4682-a61f-5ec70ea2e851	2	5	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
35	82edee76-bf86-4e90-b7be-9fd40f337ce0	2	7	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
36	25c097e6-e065-4fed-b8db-cd8b18be6ef0	2	8	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
37	81863cc0-9cf7-441e-b68c-bf820dffcc33	2	9	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
38	e6f67e78-1d42-449a-be51-8a951fad98af	2	10	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
39	d807e5b9-ce72-4b27-813d-dc7bba69d73f	2	11	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
40	06330b36-6d0d-43dc-a265-750a67da2111	2	12	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
41	2d171422-7fe4-4983-8364-30fb64f04d79	2	13	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
42	a71684ff-ddad-4e8c-9429-2244fab4b0aa	2	14	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
43	ea9bab5c-a637-4d24-9b8d-fea1bb6a087d	2	15	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
44	ce3fa118-38e9-4c41-9595-dd7d4ee7ebf2	2	16	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
45	d3446fe6-3db4-4194-92e7-79d0a8b6f273	2	17	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
46	e5683069-1b35-4744-9559-fdcdb821a385	2	18	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
47	a3e8359d-5d01-488c-a27b-3edfe7cddaca	2	19	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
48	ffab2a8a-6751-41d0-98ca-87d51141ab66	2	20	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
49	964d1f68-3c00-42fe-a615-9075ed241819	2	21	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
50	4eee4464-6770-4284-8031-09fac8cffe0b	2	22	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
51	18ec98ef-c213-441b-ae1e-4cdb4e739ac2	2	23	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
52	87765cf6-f200-4f4b-a7ea-d2dba087740c	2	24	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
53	3db97cfe-8a48-4220-9ce0-0f640d073b57	2	25	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
54	ee61bfd9-256f-4035-bfe5-dc38de7b0768	2	26	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
55	dc6cbd70-e210-463e-b913-450ddce6d94f	2	27	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
56	354a0ceb-1427-4c0e-ad05-b99ee1f8d601	2	28	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
57	56d4f42b-e934-4c8e-862e-b5f55441884f	3	1	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
58	44c52b97-e28f-40a4-a0c4-6f1328ec4eeb	3	7	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
59	7f0704f0-10ce-468e-8857-a576c42b2672	3	8	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
60	482bf5cc-7cb2-46ad-8511-ca35a7635937	3	9	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
61	216022c2-7133-4f9d-97fb-41053ea86f72	3	11	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
62	f01b17f4-b299-471b-ba74-b8352232f67c	3	12	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
63	e34f24a8-8d53-427a-8adf-7d067c7e36ce	3	13	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
64	6a801307-b37a-4d51-8c01-4dc8435a05ad	3	15	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
65	00747c62-bc74-4eda-801e-e4062344200f	3	16	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
66	9fdfd648-11a6-484b-ae13-2c5ad0811d7a	3	17	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
67	24d3f54b-c0f9-4c23-8b74-36e70c483fee	3	18	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
68	2e520024-ecad-4860-9bd6-1d18089e4355	3	19	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
69	cf9742ac-119b-4ac1-8778-147fcf440e10	3	20	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
70	aa9c242e-f22d-4fa8-b597-2244e1e95221	3	21	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
71	9d23f962-8143-44c2-960c-bc948d6ad174	3	22	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
72	d709bbe1-fa59-44f6-b37c-0966e22236ef	3	23	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
73	a21f1e67-0665-489f-8eb0-990eb6fbb66c	3	25	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
74	1177ee17-4431-4e26-a0d0-6bf7e1147733	3	27	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
75	99230d36-d597-4234-b342-73870277f59e	4	7	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
76	8122d72a-45b4-4728-a7d4-4d9a73cfaf82	4	11	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
77	a8f84fca-f6bd-44f7-8965-81e872728fd6	4	12	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
78	54679c12-126a-4756-ab2e-d12cdaa21024	4	15	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
79	4dc8ee13-a571-4017-a17e-6fbac80111ff	4	16	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
80	3a9eace0-ca36-407a-bb09-5e294e39bd86	4	17	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
81	47cd8cc5-8a4b-4ea0-bee3-b11d152c4703	4	18	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
82	8caa5ca6-590d-49b5-9a06-02d37fd34634	4	21	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
83	9cc47abc-e589-46e2-aacc-3f6dac0afa65	4	22	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
84	d079b921-978d-4fbf-a5f5-3f04b68c00ae	4	27	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
85	70bcb86a-5e31-4857-8f7a-c359cdfec560	5	8	2026-07-18 15:36:18.472313+00	2026-07-18 15:36:18.472316+00	1	1	f	ACTIVE	0
86	5c35ec1d-9657-4b0b-8ecf-089d9c9ac1b3	1	30	2026-07-19 05:15:13.998411+00	2026-07-19 05:15:13.998411+00	\N	\N	f	ACTIVE	0
87	bf9fdecd-5b21-464e-9a0d-2c141faaf111	2	30	2026-07-19 05:15:13.998411+00	2026-07-19 05:15:13.998411+00	\N	\N	f	ACTIVE	0
88	120580cf-70f6-4a59-8223-8fab24f2f1fb	3	30	2026-07-19 05:15:13.998411+00	2026-07-19 05:15:13.998411+00	\N	\N	f	ACTIVE	0
89	e0e8d354-3ddc-4ff7-b479-8a46e315c8d3	4	30	2026-07-19 05:15:13.998411+00	2026-07-19 05:15:13.998411+00	\N	\N	f	ACTIVE	0
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.roles (id, uuid, name, code, description, is_system, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	6b9eae22-3b44-4e8e-a26c-7c8d66db1597	Super Admin	SUPER_ADMIN	Full system access	t	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
2	54b8d42a-cf49-4067-aa34-1400bcdac8d1	Admin	ADMIN	Administrative access with limited system settings	t	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
3	074443d1-3b92-4e2f-83b2-4806c795437d	Manager	MANAGER	Department and project management access	t	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
4	af5f2b38-5ef9-4f62-b44f-9cbdd45939e5	Member	MEMBER	Standard staff member access	t	2026-07-18 15:31:40.992818+00	2026-07-18 15:31:40.992818+00	\N	\N	f	ACTIVE	0
5	2ab01946-ac05-4a52-a885-273bef9052be	Custom Viewer	CUSTOM_VIEWER	Read-only	f	2026-07-18 15:36:18.462762+00	2026-07-18 15:36:18.462765+00	1	1	f	ACTIVE	0
\.


--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.user_roles (id, uuid, user_id, role_id, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
1	2927f3ba-b59c-4f85-8a75-7f8bd1357807	1	1	2026-07-19 10:07:44.736628+00	2026-07-19 10:07:44.73666+00	\N	\N	f	ACTIVE	0
2	f0adbc90-8151-469a-92c6-f23a75b9ee9d	2	2	2026-07-19 10:53:45.984428+00	2026-07-19 10:53:45.984432+00	1	1	f	ACTIVE	0
3	f32d004d-1034-44ec-8d16-08e176c8cf47	3	3	2026-07-19 13:01:38.314585+00	2026-07-19 13:01:38.314589+00	1	1	f	ACTIVE	0
4	ecd0c1eb-a371-490c-a90d-ce5bbe259cb4	4	3	2026-07-19 13:13:15.660351+00	2026-07-19 13:13:15.660356+00	1	1	f	ACTIVE	0
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.users (id, uuid, first_name, last_name, email, password_hash, phone, company_name, profile_image_url, email_verified, last_login_at, failed_login_attempts, locked_until, password_changed_at, created_at, updated_at, created_by, updated_by, deleted, status, version) FROM stdin;
2	af10dfd1-eb50-4451-bb22-52f2f666f2a9	Mihir	Soni	mihir@justcatering.com	$2a$12$TImw.XQdlxLLY/bVAACweuXFhOAtmcTiqivl4/tAZWyrYyi/T/6J6	9876543210	\N	\N	f	\N	0	\N	2026-07-19 10:53:45.923332+00	2026-07-19 10:53:45.95378+00	2026-07-19 10:53:45.953788+00	1	1	f	ACTIVE	0
3	9efae162-d227-49ea-809a-8af09647add7	ritesh	shah	ritesh@justcatering.com	$2a$12$i8PhHjdkYA9X8Ps2fGcpfuNsCU8INgSw0iK5cVamVbcVK4N2TFdsK	94847575757	\N	\N	f	\N	0	\N	2026-07-19 13:01:38.261988+00	2026-07-19 13:01:38.281901+00	2026-07-19 13:01:38.281912+00	1	1	f	ACTIVE	0
4	8f75287f-ec2b-4d34-9371-b41ad3e0e044	vivek	vivek	vivek@gmail.com	$2a$12$2rpykbGCnf4GkDI1t2xKWuFzRNaTaqy7xNYZyo2CNdyTiFS3pJ3gG	98498437987	\N	\N	f	\N	0	\N	2026-07-19 13:13:15.622869+00	2026-07-19 13:13:15.642712+00	2026-07-19 13:13:15.642718+00	1	1	f	ACTIVE	0
1	5e295c3f-4f27-4eaf-ab2b-aa876e17043a	Super	Admin	admin@justcatering.com	$2a$12$Qwb/xSzH5QKCrbtfpVAHWewKcSQeFQte0HWs6bjc6qjU/.lIKk/EW	\N	Just Catering	\N	t	2026-07-19 14:58:37.176437+00	0	\N	\N	2026-07-19 10:07:44.710038+00	2026-07-19 14:58:37.293203+00	\N	\N	f	ACTIVE	8
\.


--
-- Name: client_deadlines_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.client_deadlines_id_seq', 1, false);


--
-- Name: client_manager_assignments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.client_manager_assignments_id_seq', 3, true);


--
-- Name: client_queries_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.client_queries_id_seq', 2, true);


--
-- Name: clients_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.clients_id_seq', 2, true);


--
-- Name: department_members_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.department_members_id_seq', 3, true);


--
-- Name: departments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.departments_id_seq', 2, true);


--
-- Name: expenses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.expenses_id_seq', 1, true);


--
-- Name: follow_ups_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.follow_ups_id_seq', 2, true);


--
-- Name: leads_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.leads_id_seq', 2, true);


--
-- Name: payments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.payments_id_seq', 2, true);


--
-- Name: permissions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.permissions_id_seq', 30, true);


--
-- Name: product_names_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.product_names_id_seq', 5, true);


--
-- Name: product_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.product_types_id_seq', 3, true);


--
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.products_id_seq', 4, true);


--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.refresh_tokens_id_seq', 24, true);


--
-- Name: role_permissions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.role_permissions_id_seq', 89, true);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.roles_id_seq', 5, true);


--
-- Name: user_roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.user_roles_id_seq', 4, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.users_id_seq', 4, true);


--
-- Name: client_deadlines client_deadlines_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_deadlines
    ADD CONSTRAINT client_deadlines_pkey PRIMARY KEY (id);


--
-- Name: client_manager_assignments client_manager_assignments_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_manager_assignments
    ADD CONSTRAINT client_manager_assignments_pkey PRIMARY KEY (id);


--
-- Name: client_queries client_queries_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_queries
    ADD CONSTRAINT client_queries_pkey PRIMARY KEY (id);


--
-- Name: clients clients_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT clients_pkey PRIMARY KEY (id);


--
-- Name: department_members department_members_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department_members
    ADD CONSTRAINT department_members_pkey PRIMARY KEY (id);


--
-- Name: departments departments_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT departments_pkey PRIMARY KEY (id);


--
-- Name: expenses expenses_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.expenses
    ADD CONSTRAINT expenses_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: follow_ups follow_ups_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.follow_ups
    ADD CONSTRAINT follow_ups_pkey PRIMARY KEY (id);


--
-- Name: leads leads_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.leads
    ADD CONSTRAINT leads_pkey PRIMARY KEY (id);


--
-- Name: payments payments_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (id);


--
-- Name: permissions permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissions
    ADD CONSTRAINT permissions_pkey PRIMARY KEY (id);


--
-- Name: product_names product_names_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_names
    ADD CONSTRAINT product_names_pkey PRIMARY KEY (id);


--
-- Name: product_types product_types_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_types
    ADD CONSTRAINT product_types_pkey PRIMARY KEY (id);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- Name: refresh_tokens refresh_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT refresh_tokens_pkey PRIMARY KEY (id);


--
-- Name: role_permissions role_permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT role_permissions_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: client_deadlines uq_client_deadlines_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_deadlines
    ADD CONSTRAINT uq_client_deadlines_uuid UNIQUE (uuid);


--
-- Name: client_manager_assignments uq_client_manager_assignments_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_manager_assignments
    ADD CONSTRAINT uq_client_manager_assignments_uuid UNIQUE (uuid);


--
-- Name: client_queries uq_client_queries_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_queries
    ADD CONSTRAINT uq_client_queries_uuid UNIQUE (uuid);


--
-- Name: clients uq_clients_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT uq_clients_uuid UNIQUE (uuid);


--
-- Name: department_members uq_department_members_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department_members
    ADD CONSTRAINT uq_department_members_uuid UNIQUE (uuid);


--
-- Name: departments uq_departments_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT uq_departments_code UNIQUE (code);


--
-- Name: departments uq_departments_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT uq_departments_uuid UNIQUE (uuid);


--
-- Name: expenses uq_expenses_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.expenses
    ADD CONSTRAINT uq_expenses_uuid UNIQUE (uuid);


--
-- Name: follow_ups uq_follow_ups_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.follow_ups
    ADD CONSTRAINT uq_follow_ups_uuid UNIQUE (uuid);


--
-- Name: leads uq_leads_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.leads
    ADD CONSTRAINT uq_leads_uuid UNIQUE (uuid);


--
-- Name: payments uq_payments_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT uq_payments_uuid UNIQUE (uuid);


--
-- Name: permissions uq_permissions_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissions
    ADD CONSTRAINT uq_permissions_code UNIQUE (code);


--
-- Name: permissions uq_permissions_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissions
    ADD CONSTRAINT uq_permissions_uuid UNIQUE (uuid);


--
-- Name: product_names uq_product_names_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_names
    ADD CONSTRAINT uq_product_names_uuid UNIQUE (uuid);


--
-- Name: product_types uq_product_types_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_types
    ADD CONSTRAINT uq_product_types_uuid UNIQUE (uuid);


--
-- Name: products uq_products_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT uq_products_code UNIQUE (code);


--
-- Name: products uq_products_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT uq_products_uuid UNIQUE (uuid);


--
-- Name: refresh_tokens uq_refresh_tokens_token; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT uq_refresh_tokens_token UNIQUE (token);


--
-- Name: refresh_tokens uq_refresh_tokens_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT uq_refresh_tokens_uuid UNIQUE (uuid);


--
-- Name: role_permissions uq_role_permissions_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT uq_role_permissions_uuid UNIQUE (uuid);


--
-- Name: roles uq_roles_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT uq_roles_code UNIQUE (code);


--
-- Name: roles uq_roles_name; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT uq_roles_name UNIQUE (name);


--
-- Name: roles uq_roles_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT uq_roles_uuid UNIQUE (uuid);


--
-- Name: user_roles uq_user_roles_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT uq_user_roles_uuid UNIQUE (uuid);


--
-- Name: users uq_users_email; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uq_users_email UNIQUE (email);


--
-- Name: users uq_users_uuid; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uq_users_uuid UNIQUE (uuid);


--
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: idx_client_deadlines_client_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_deadlines_client_id ON public.client_deadlines USING btree (client_id) WHERE (deleted = false);


--
-- Name: idx_client_deadlines_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_deadlines_created_at ON public.client_deadlines USING btree (created_at);


--
-- Name: idx_client_deadlines_current_deadline; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_deadlines_current_deadline ON public.client_deadlines USING btree (current_deadline) WHERE (deleted = false);


--
-- Name: idx_client_deadlines_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_deadlines_deleted ON public.client_deadlines USING btree (deleted);


--
-- Name: idx_client_deadlines_department_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_deadlines_department_id ON public.client_deadlines USING btree (department_id) WHERE (deleted = false);


--
-- Name: idx_client_deadlines_new_deadline; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_deadlines_new_deadline ON public.client_deadlines USING btree (new_deadline) WHERE (deleted = false);


--
-- Name: idx_client_deadlines_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_deadlines_status ON public.client_deadlines USING btree (status) WHERE (deleted = false);


--
-- Name: idx_client_manager_assignments_client_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_manager_assignments_client_id ON public.client_manager_assignments USING btree (client_id) WHERE (deleted = false);


--
-- Name: idx_client_manager_assignments_close_date; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_manager_assignments_close_date ON public.client_manager_assignments USING btree (close_date) WHERE (deleted = false);


--
-- Name: idx_client_manager_assignments_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_manager_assignments_created_at ON public.client_manager_assignments USING btree (created_at);


--
-- Name: idx_client_manager_assignments_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_manager_assignments_deleted ON public.client_manager_assignments USING btree (deleted);


--
-- Name: idx_client_manager_assignments_department_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_manager_assignments_department_id ON public.client_manager_assignments USING btree (department_id) WHERE (deleted = false);


--
-- Name: idx_client_manager_assignments_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_manager_assignments_status ON public.client_manager_assignments USING btree (status) WHERE (deleted = false);


--
-- Name: idx_client_manager_assignments_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_manager_assignments_user_id ON public.client_manager_assignments USING btree (user_id) WHERE (deleted = false);


--
-- Name: idx_client_queries_assigned_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_queries_assigned_user_id ON public.client_queries USING btree (assigned_user_id) WHERE (deleted = false);


--
-- Name: idx_client_queries_client_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_queries_client_id ON public.client_queries USING btree (client_id) WHERE (deleted = false);


--
-- Name: idx_client_queries_completed_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_queries_completed_at ON public.client_queries USING btree (completed_at) WHERE (deleted = false);


--
-- Name: idx_client_queries_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_queries_created_at ON public.client_queries USING btree (created_at);


--
-- Name: idx_client_queries_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_queries_deleted ON public.client_queries USING btree (deleted);


--
-- Name: idx_client_queries_department_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_queries_department_id ON public.client_queries USING btree (department_id) WHERE (deleted = false);


--
-- Name: idx_client_queries_priority; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_queries_priority ON public.client_queries USING btree (priority) WHERE (deleted = false);


--
-- Name: idx_client_queries_query_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_queries_query_status ON public.client_queries USING btree (query_status) WHERE (deleted = false);


--
-- Name: idx_client_queries_query_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_queries_query_type ON public.client_queries USING btree (query_type) WHERE (deleted = false);


--
-- Name: idx_client_queries_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_client_queries_status ON public.client_queries USING btree (status) WHERE (deleted = false);


--
-- Name: idx_clients_client_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_clients_client_name ON public.clients USING btree (client_name) WHERE (deleted = false);


--
-- Name: idx_clients_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_clients_created_at ON public.clients USING btree (created_at);


--
-- Name: idx_clients_deal_date; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_clients_deal_date ON public.clients USING btree (deal_date) WHERE (deleted = false);


--
-- Name: idx_clients_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_clients_deleted ON public.clients USING btree (deleted);


--
-- Name: idx_clients_mobile; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_clients_mobile ON public.clients USING btree (mobile) WHERE ((deleted = false) AND (mobile IS NOT NULL));


--
-- Name: idx_clients_priority; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_clients_priority ON public.clients USING btree (priority) WHERE (deleted = false);


--
-- Name: idx_clients_product_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_clients_product_id ON public.clients USING btree (product_id) WHERE (deleted = false);


--
-- Name: idx_clients_stage; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_clients_stage ON public.clients USING btree (client_stage) WHERE (deleted = false);


--
-- Name: idx_clients_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_clients_status ON public.clients USING btree (status) WHERE (deleted = false);


--
-- Name: idx_department_members_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_department_members_deleted ON public.department_members USING btree (deleted);


--
-- Name: idx_department_members_department_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_department_members_department_id ON public.department_members USING btree (department_id) WHERE (deleted = false);


--
-- Name: idx_department_members_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_department_members_user_id ON public.department_members USING btree (user_id) WHERE (deleted = false);


--
-- Name: idx_departments_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_departments_deleted ON public.departments USING btree (deleted);


--
-- Name: idx_departments_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_departments_name ON public.departments USING btree (name) WHERE (deleted = false);


--
-- Name: idx_departments_parent_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_departments_parent_id ON public.departments USING btree (parent_id) WHERE (deleted = false);


--
-- Name: idx_departments_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_departments_status ON public.departments USING btree (status) WHERE (deleted = false);


--
-- Name: idx_expenses_client_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_expenses_client_id ON public.expenses USING btree (client_id) WHERE (deleted = false);


--
-- Name: idx_expenses_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_expenses_created_at ON public.expenses USING btree (created_at);


--
-- Name: idx_expenses_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_expenses_deleted ON public.expenses USING btree (deleted);


--
-- Name: idx_expenses_due_date; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_expenses_due_date ON public.expenses USING btree (due_date) WHERE (deleted = false);


--
-- Name: idx_expenses_expense_date; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_expenses_expense_date ON public.expenses USING btree (expense_date) WHERE (deleted = false);


--
-- Name: idx_expenses_member_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_expenses_member_user_id ON public.expenses USING btree (member_user_id) WHERE (deleted = false);


--
-- Name: idx_expenses_paid_date; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_expenses_paid_date ON public.expenses USING btree (paid_date) WHERE (deleted = false);


--
-- Name: idx_expenses_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_expenses_status ON public.expenses USING btree (status) WHERE (deleted = false);


--
-- Name: idx_expenses_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_expenses_type ON public.expenses USING btree (expense_type) WHERE (deleted = false);


--
-- Name: idx_follow_ups_assigned_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_follow_ups_assigned_user_id ON public.follow_ups USING btree (assigned_user_id) WHERE (deleted = false);


--
-- Name: idx_follow_ups_client_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_follow_ups_client_id ON public.follow_ups USING btree (client_id) WHERE (deleted = false);


--
-- Name: idx_follow_ups_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_follow_ups_created_at ON public.follow_ups USING btree (created_at);


--
-- Name: idx_follow_ups_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_follow_ups_deleted ON public.follow_ups USING btree (deleted);


--
-- Name: idx_follow_ups_follow_up_date; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_follow_ups_follow_up_date ON public.follow_ups USING btree (follow_up_date) WHERE (deleted = false);


--
-- Name: idx_follow_ups_follow_up_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_follow_ups_follow_up_status ON public.follow_ups USING btree (follow_up_status) WHERE (deleted = false);


--
-- Name: idx_follow_ups_lead_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_follow_ups_lead_id ON public.follow_ups USING btree (lead_id) WHERE (deleted = false);


--
-- Name: idx_follow_ups_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_follow_ups_status ON public.follow_ups USING btree (status) WHERE (deleted = false);


--
-- Name: idx_follow_ups_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_follow_ups_type ON public.follow_ups USING btree (follow_up_type) WHERE (deleted = false);


--
-- Name: idx_leads_city; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_leads_city ON public.leads USING btree (city) WHERE (deleted = false);


--
-- Name: idx_leads_company_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_leads_company_name ON public.leads USING btree (company_name) WHERE (deleted = false);


--
-- Name: idx_leads_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_leads_created_at ON public.leads USING btree (created_at);


--
-- Name: idx_leads_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_leads_deleted ON public.leads USING btree (deleted);


--
-- Name: idx_leads_phone; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_leads_phone ON public.leads USING btree (phone) WHERE (deleted = false);


--
-- Name: idx_leads_product_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_leads_product_id ON public.leads USING btree (product_id) WHERE (deleted = false);


--
-- Name: idx_leads_stage; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_leads_stage ON public.leads USING btree (lead_stage) WHERE (deleted = false);


--
-- Name: idx_leads_state; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_leads_state ON public.leads USING btree (state) WHERE (deleted = false);


--
-- Name: idx_leads_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_leads_status ON public.leads USING btree (status) WHERE (deleted = false);


--
-- Name: idx_payments_client_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_payments_client_id ON public.payments USING btree (client_id) WHERE (deleted = false);


--
-- Name: idx_payments_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_payments_created_at ON public.payments USING btree (created_at);


--
-- Name: idx_payments_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_payments_deleted ON public.payments USING btree (deleted);


--
-- Name: idx_payments_invoice_number; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_payments_invoice_number ON public.payments USING btree (invoice_number) WHERE (deleted = false);


--
-- Name: idx_payments_payment_date; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_payments_payment_date ON public.payments USING btree (payment_date) WHERE (deleted = false);


--
-- Name: idx_payments_payment_mode; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_payments_payment_mode ON public.payments USING btree (payment_mode) WHERE (deleted = false);


--
-- Name: idx_payments_product_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_payments_product_id ON public.payments USING btree (product_id) WHERE (deleted = false);


--
-- Name: idx_payments_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_payments_status ON public.payments USING btree (status) WHERE (deleted = false);


--
-- Name: idx_permissions_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_permissions_deleted ON public.permissions USING btree (deleted);


--
-- Name: idx_permissions_module; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_permissions_module ON public.permissions USING btree (module) WHERE (deleted = false);


--
-- Name: idx_permissions_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_permissions_status ON public.permissions USING btree (status) WHERE (deleted = false);


--
-- Name: idx_product_names_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_product_names_deleted ON public.product_names USING btree (deleted);


--
-- Name: idx_product_names_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_product_names_status ON public.product_names USING btree (status) WHERE (deleted = false);


--
-- Name: idx_product_names_type_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_product_names_type_id ON public.product_names USING btree (product_type_id) WHERE (deleted = false);


--
-- Name: idx_product_types_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_product_types_deleted ON public.product_types USING btree (deleted);


--
-- Name: idx_product_types_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_product_types_status ON public.product_types USING btree (status) WHERE (deleted = false);


--
-- Name: idx_products_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_deleted ON public.products USING btree (deleted);


--
-- Name: idx_products_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_name ON public.products USING btree (name) WHERE (deleted = false);


--
-- Name: idx_products_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_status ON public.products USING btree (status) WHERE (deleted = false);


--
-- Name: idx_products_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_type ON public.products USING btree (product_type) WHERE (deleted = false);


--
-- Name: idx_refresh_tokens_expires_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_refresh_tokens_expires_at ON public.refresh_tokens USING btree (expires_at) WHERE ((revoked = false) AND (deleted = false));


--
-- Name: idx_refresh_tokens_token; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_refresh_tokens_token ON public.refresh_tokens USING btree (token) WHERE (deleted = false);


--
-- Name: idx_refresh_tokens_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_refresh_tokens_user_id ON public.refresh_tokens USING btree (user_id) WHERE (deleted = false);


--
-- Name: idx_role_permissions_permission_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_role_permissions_permission_id ON public.role_permissions USING btree (permission_id) WHERE (deleted = false);


--
-- Name: idx_role_permissions_role_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_role_permissions_role_id ON public.role_permissions USING btree (role_id) WHERE (deleted = false);


--
-- Name: idx_roles_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_roles_deleted ON public.roles USING btree (deleted);


--
-- Name: idx_roles_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_roles_status ON public.roles USING btree (status) WHERE (deleted = false);


--
-- Name: idx_user_roles_role_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_roles_role_id ON public.user_roles USING btree (role_id) WHERE (deleted = false);


--
-- Name: idx_user_roles_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_roles_user_id ON public.user_roles USING btree (user_id) WHERE (deleted = false);


--
-- Name: idx_users_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_users_created_at ON public.users USING btree (created_at);


--
-- Name: idx_users_deleted; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_users_deleted ON public.users USING btree (deleted);


--
-- Name: idx_users_email; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_users_email ON public.users USING btree (email) WHERE (deleted = false);


--
-- Name: idx_users_phone; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_users_phone ON public.users USING btree (phone) WHERE ((deleted = false) AND (phone IS NOT NULL));


--
-- Name: idx_users_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_users_status ON public.users USING btree (status) WHERE (deleted = false);


--
-- Name: uq_client_manager_assignments_client_user_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_client_manager_assignments_client_user_active ON public.client_manager_assignments USING btree (client_id, user_id) WHERE (deleted = false);


--
-- Name: uq_clients_email_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_clients_email_active ON public.clients USING btree (lower((email)::text)) WHERE (deleted = false);


--
-- Name: uq_department_members_dept_user_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_department_members_dept_user_active ON public.department_members USING btree (department_id, user_id) WHERE (deleted = false);


--
-- Name: uq_departments_name_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_departments_name_active ON public.departments USING btree (lower((name)::text)) WHERE (deleted = false);


--
-- Name: uq_leads_email_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_leads_email_active ON public.leads USING btree (lower((email)::text)) WHERE (deleted = false);


--
-- Name: uq_payments_invoice_number_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_payments_invoice_number_active ON public.payments USING btree (invoice_number) WHERE (deleted = false);


--
-- Name: uq_product_names_type_name_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_product_names_type_name_active ON public.product_names USING btree (product_type_id, lower((name)::text)) WHERE (deleted = false);


--
-- Name: uq_product_types_name_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_product_types_name_active ON public.product_types USING btree (lower((name)::text)) WHERE (deleted = false);


--
-- Name: uq_products_name_type_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_products_name_type_active ON public.products USING btree (lower((name)::text), lower((product_type)::text)) WHERE (deleted = false);


--
-- Name: uq_role_permissions_role_perm_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_role_permissions_role_perm_active ON public.role_permissions USING btree (role_id, permission_id) WHERE (deleted = false);


--
-- Name: uq_user_roles_user_role_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_user_roles_user_role_active ON public.user_roles USING btree (user_id, role_id) WHERE (deleted = false);


--
-- Name: client_deadlines fk_client_deadlines_client; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_deadlines
    ADD CONSTRAINT fk_client_deadlines_client FOREIGN KEY (client_id) REFERENCES public.clients(id);


--
-- Name: client_deadlines fk_client_deadlines_department; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_deadlines
    ADD CONSTRAINT fk_client_deadlines_department FOREIGN KEY (department_id) REFERENCES public.departments(id);


--
-- Name: client_manager_assignments fk_client_manager_assignments_client; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_manager_assignments
    ADD CONSTRAINT fk_client_manager_assignments_client FOREIGN KEY (client_id) REFERENCES public.clients(id);


--
-- Name: client_manager_assignments fk_client_manager_assignments_department; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_manager_assignments
    ADD CONSTRAINT fk_client_manager_assignments_department FOREIGN KEY (department_id) REFERENCES public.departments(id);


--
-- Name: client_manager_assignments fk_client_manager_assignments_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_manager_assignments
    ADD CONSTRAINT fk_client_manager_assignments_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: client_queries fk_client_queries_assigned_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_queries
    ADD CONSTRAINT fk_client_queries_assigned_user FOREIGN KEY (assigned_user_id) REFERENCES public.users(id);


--
-- Name: client_queries fk_client_queries_client; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_queries
    ADD CONSTRAINT fk_client_queries_client FOREIGN KEY (client_id) REFERENCES public.clients(id);


--
-- Name: client_queries fk_client_queries_department; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_queries
    ADD CONSTRAINT fk_client_queries_department FOREIGN KEY (department_id) REFERENCES public.departments(id);


--
-- Name: clients fk_clients_product; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT fk_clients_product FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- Name: department_members fk_department_members_department; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department_members
    ADD CONSTRAINT fk_department_members_department FOREIGN KEY (department_id) REFERENCES public.departments(id);


--
-- Name: department_members fk_department_members_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department_members
    ADD CONSTRAINT fk_department_members_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: departments fk_departments_parent; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT fk_departments_parent FOREIGN KEY (parent_id) REFERENCES public.departments(id);


--
-- Name: expenses fk_expenses_client; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.expenses
    ADD CONSTRAINT fk_expenses_client FOREIGN KEY (client_id) REFERENCES public.clients(id);


--
-- Name: expenses fk_expenses_member_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.expenses
    ADD CONSTRAINT fk_expenses_member_user FOREIGN KEY (member_user_id) REFERENCES public.users(id);


--
-- Name: follow_ups fk_follow_ups_assigned_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.follow_ups
    ADD CONSTRAINT fk_follow_ups_assigned_user FOREIGN KEY (assigned_user_id) REFERENCES public.users(id);


--
-- Name: follow_ups fk_follow_ups_client; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.follow_ups
    ADD CONSTRAINT fk_follow_ups_client FOREIGN KEY (client_id) REFERENCES public.clients(id);


--
-- Name: follow_ups fk_follow_ups_lead; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.follow_ups
    ADD CONSTRAINT fk_follow_ups_lead FOREIGN KEY (lead_id) REFERENCES public.leads(id);


--
-- Name: leads fk_leads_product; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.leads
    ADD CONSTRAINT fk_leads_product FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- Name: payments fk_payments_client; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT fk_payments_client FOREIGN KEY (client_id) REFERENCES public.clients(id);


--
-- Name: payments fk_payments_product; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT fk_payments_product FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- Name: product_names fk_product_names_type; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_names
    ADD CONSTRAINT fk_product_names_type FOREIGN KEY (product_type_id) REFERENCES public.product_types(id);


--
-- Name: refresh_tokens fk_refresh_tokens_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: role_permissions fk_role_permissions_permission; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT fk_role_permissions_permission FOREIGN KEY (permission_id) REFERENCES public.permissions(id);


--
-- Name: role_permissions fk_role_permissions_role; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: user_roles fk_user_roles_role; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: user_roles fk_user_roles_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- PostgreSQL database dump complete
--

\unrestrict MQz4VLcCygdZ4c4C2TcRbGPZGx6SDlpjIETxNWee0sRs5b1avcpRGoOqduxAP2b

