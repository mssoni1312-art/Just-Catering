PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "flyway_schema_history" (
    "installed_rank" INT NOT NULL PRIMARY KEY,
    "version" VARCHAR(50),
    "description" VARCHAR(200) NOT NULL,
    "type" VARCHAR(20) NOT NULL,
    "script" VARCHAR(1000) NOT NULL,
    "checksum" INT,
    "installed_by" VARCHAR(100) NOT NULL,
    "installed_on" TEXT NOT NULL DEFAULT (strftime('%Y-%m-%d %H:%M:%f','now')),
    "execution_time" INT NOT NULL,
    "success" BOOLEAN NOT NULL
);
INSERT INTO flyway_schema_history VALUES(1,'1','create auth tables','SQL','V1__create_auth_tables.sql',604039330,'','2026-07-19 18:01:56.504',2,1);
INSERT INTO flyway_schema_history VALUES(2,'2','seed roles permissions admin','SQL','V2__seed_roles_permissions_admin.sql',-1757045132,'','2026-07-19 18:01:56.509',0,1);
INSERT INTO flyway_schema_history VALUES(3,'3','partial unique join indexes','SQL','V3__partial_unique_join_indexes.sql',1584459401,'','2026-07-19 18:01:56.511',0,1);
INSERT INTO flyway_schema_history VALUES(4,'4','create department tables','SQL','V4__create_department_tables.sql',-1950975175,'','2026-07-19 18:01:56.514',1,1);
INSERT INTO flyway_schema_history VALUES(5,'5','seed departments','SQL','V5__seed_departments.sql',-1834405201,'','2026-07-19 18:01:56.516',0,1);
INSERT INTO flyway_schema_history VALUES(6,'6','create product tables','SQL','V6__create_product_tables.sql',878090202,'','2026-07-19 18:01:56.518',0,1);
INSERT INTO flyway_schema_history VALUES(7,'7','seed products','SQL','V7__seed_products.sql',-471525044,'','2026-07-19 18:01:56.520',0,1);
INSERT INTO flyway_schema_history VALUES(8,'8','create client tables','SQL','V8__create_client_tables.sql',269689542,'','2026-07-19 18:01:56.522',0,1);
INSERT INTO flyway_schema_history VALUES(9,'9','seed clients','SQL','V9__seed_clients.sql',-1746969031,'','2026-07-19 18:01:56.524',0,1);
INSERT INTO flyway_schema_history VALUES(10,'10','create lead tables','SQL','V10__create_lead_tables.sql',1246419160,'','2026-07-19 18:01:56.526',0,1);
INSERT INTO flyway_schema_history VALUES(11,'11','seed leads','SQL','V11__seed_leads.sql',1728000455,'','2026-07-19 18:01:56.528',0,1);
INSERT INTO flyway_schema_history VALUES(12,'12','create follow up tables','SQL','V12__create_follow_up_tables.sql',1781448037,'','2026-07-19 18:01:56.530',0,1);
INSERT INTO flyway_schema_history VALUES(13,'13','seed follow ups','SQL','V13__seed_follow_ups.sql',1891700271,'','2026-07-19 18:01:56.532',0,1);
INSERT INTO flyway_schema_history VALUES(14,'14','create client query tables','SQL','V14__create_client_query_tables.sql',1462894655,'','2026-07-19 18:01:56.534',0,1);
INSERT INTO flyway_schema_history VALUES(15,'15','seed client queries','SQL','V15__seed_client_queries.sql',-230569463,'','2026-07-19 18:01:56.536',0,1);
INSERT INTO flyway_schema_history VALUES(16,'16','create payment tables','SQL','V16__create_payment_tables.sql',-250046906,'','2026-07-19 18:01:56.538',0,1);
INSERT INTO flyway_schema_history VALUES(17,'17','seed payments','SQL','V17__seed_payments.sql',914930273,'','2026-07-19 18:01:56.540',0,1);
INSERT INTO flyway_schema_history VALUES(18,'18','create expense tables','SQL','V18__create_expense_tables.sql',-591091329,'','2026-07-19 18:01:56.542',0,1);
INSERT INTO flyway_schema_history VALUES(19,'19','seed expenses','SQL','V19__seed_expenses.sql',384147739,'','2026-07-19 18:01:56.544',0,1);
INSERT INTO flyway_schema_history VALUES(20,'20','create project ops tables','SQL','V20__create_project_ops_tables.sql',-1979920766,'','2026-07-19 18:01:56.547',0,1);
INSERT INTO flyway_schema_history VALUES(21,'21','seed project ops','SQL','V21__seed_project_ops.sql',1300943096,'','2026-07-19 18:01:56.549',0,1);
INSERT INTO flyway_schema_history VALUES(22,'22','add new client stage','SQL','V22__add_new_client_stage.sql',-188324424,'','2026-07-19 18:01:56.550',0,1);
INSERT INTO flyway_schema_history VALUES(23,'23','alter client notes length','SQL','V23__alter_client_notes_length.sql',-1141204618,'','2026-07-19 18:01:56.551',0,1);
INSERT INTO flyway_schema_history VALUES(24,'24','add calendar permission','SQL','V24__add_calendar_permission.sql',1692524619,'','2026-07-19 18:01:56.552',0,1);
INSERT INTO flyway_schema_history VALUES(25,'25','clear sample data','SQL','V25__clear_sample_data.sql',-1550354904,'','2026-07-19 18:01:56.554',0,1);
INSERT INTO flyway_schema_history VALUES(26,'26','create product type name tables','SQL','V26__create_product_type_name_tables.sql',-240490515,'','2026-07-19 18:01:56.556',0,1);
INSERT INTO flyway_schema_history VALUES(27,'27','follow ups support leads','SQL','V27__follow_ups_support_leads.sql',17667970,'','2026-07-19 18:01:56.557',0,1);
CREATE TABLE roles (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    name            VARCHAR(100)    NOT NULL,
    code            VARCHAR(50)     NOT NULL,
    description     VARCHAR(500),
    is_system       INTEGER         NOT NULL DEFAULT 0,
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_roles_uuid UNIQUE (uuid),
    CONSTRAINT uq_roles_code UNIQUE (code),
    CONSTRAINT uq_roles_name UNIQUE (name),
    CONSTRAINT chk_roles_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);
INSERT INTO roles VALUES(1,'b6c378f4-1ca2-4757-a619-ee594ab4dbd0','Super Admin','SUPER_ADMIN','Full system access',1,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO roles VALUES(2,'87866519-ea6b-43a8-9a0b-bc98adca395a','Admin','ADMIN','Administrative access with limited system settings',1,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO roles VALUES(3,'649b290d-51b4-4721-8d98-c4ad677aad94','Manager','MANAGER','Department and project management access',1,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO roles VALUES(4,'f08b87b2-5b41-4399-b6ea-409f052e2130','Member','MEMBER','Standard staff member access',1,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
CREATE TABLE permissions (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    name            VARCHAR(150)    NOT NULL,
    code            VARCHAR(100)    NOT NULL,
    module          VARCHAR(100)    NOT NULL,
    description     VARCHAR(500),
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_permissions_uuid UNIQUE (uuid),
    CONSTRAINT uq_permissions_code UNIQUE (code),
    CONSTRAINT chk_permissions_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);
INSERT INTO permissions VALUES(1,'9c8922f6-af55-4e63-91f3-9d1a7ecea28a','View Users','USER_VIEW','USER','View user list and details','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(2,'b4c716ef-8cab-4084-8390-9ac32d824215','Create User','USER_CREATE','USER','Create new users','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(3,'45b52041-fa4d-4d29-a301-96739878b5f3','Update User','USER_UPDATE','USER','Update existing users','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(4,'6fc63687-0f24-4752-831d-d58fe987ebea','Delete User','USER_DELETE','USER','Soft-delete users','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(5,'39dd4b9a-0296-4a9c-b64b-98537456a698','View Roles','ROLE_VIEW','ROLE','View roles and permissions','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(6,'85c11732-943c-4427-8756-287a4dabed4c','Manage Roles','ROLE_MANAGE','ROLE','Create and update roles','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(7,'1dba4b27-9b14-4239-a8a3-514833cac9ea','View Clients','CLIENT_VIEW','CLIENT','View client list and details','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(8,'9c970136-1662-41c3-a1c2-7934f74dd561','Create Client','CLIENT_CREATE','CLIENT','Create clients','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(9,'8a5d932d-1f46-451d-b89f-03ab61ad1629','Update Client','CLIENT_UPDATE','CLIENT','Update clients','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(10,'57d5d51d-8530-424c-83b1-53ab9b538f41','Delete Client','CLIENT_DELETE','CLIENT','Soft-delete clients','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(11,'ae0f02c7-8d45-4657-a2ca-4d0e7f590de9','View Leads','LEAD_VIEW','LEAD','View meeting leads','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(12,'2bbbe73c-2d3d-4f88-a114-33a7929beef6','Create Lead','LEAD_CREATE','LEAD','Create meeting leads','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(13,'8a6a129a-2831-4a5b-9b80-a5b970ce195f','Update Lead','LEAD_UPDATE','LEAD','Update meeting leads','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(14,'284e504f-f627-404e-9b43-358cd5ba6242','Delete Lead','LEAD_DELETE','LEAD','Soft-delete meeting leads','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(15,'caa5e2b3-eaeb-4130-90a9-f4ed80bc88db','View Follow-ups','FOLLOWUP_VIEW','FOLLOWUP','View follow-ups','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(16,'ef474a75-0898-4c3e-a56a-6da5eec98909','Manage Follow-ups','FOLLOWUP_MANAGE','FOLLOWUP','Create and update follow-ups','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(17,'c50e95b9-c502-43fe-9249-246e22d39892','View Queries','QUERY_VIEW','QUERY','View queries and requirements','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(18,'c1043d2b-f244-4722-8580-f3e77b77230e','Manage Queries','QUERY_MANAGE','QUERY','Create and update queries','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(19,'757a10c1-497a-4d05-822b-86763b33e05c','View Payments','PAYMENT_VIEW','PAYMENT','View payments and receipts','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(20,'c944c668-79a9-4df7-b0d8-ec35b8741431','Manage Payments','PAYMENT_MANAGE','PAYMENT','Create and update payments','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(21,'4c452be2-76f7-4d39-97ff-688dffdcf084','View Expenses','EXPENSE_VIEW','EXPENSE','View expenses','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(22,'a76116fc-164d-49f8-9f20-1632f220211b','Manage Expenses','EXPENSE_MANAGE','EXPENSE','Create and update expenses','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(23,'7391149e-90b5-46f0-ac12-6c561656ddca','View Departments','DEPARTMENT_VIEW','DEPARTMENT','View departments','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(24,'f50091ed-0cd6-4e66-930c-558c03db2e27','Manage Departments','DEPARTMENT_MANAGE','DEPARTMENT','Create and update departments','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(25,'8e7c9e42-39ba-45e0-b81a-5af65eb78ba8','View Products','PRODUCT_VIEW','PRODUCT','View products','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(26,'9fe2d0f0-27e3-43a1-94a0-cb081788fd0c','Manage Products','PRODUCT_MANAGE','PRODUCT','Create and update products','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(27,'36d82a05-a0dc-4fdd-9ab0-07a33f06ae72','View Dashboard','DASHBOARD_VIEW','DASHBOARD','View dashboard overview','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(28,'a0728603-0ae5-4d63-a838-78c8d48dce82','View Settings','SETTINGS_VIEW','SETTINGS','View system settings','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(29,'cd829c92-59b1-4ecd-a717-35ffe0161695','Manage Settings','SETTINGS_MANAGE','SETTINGS','Update system settings','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO permissions VALUES(30,'d5b0c339-b9f2-4a1b-b7f3-d2683666dc7c','View Calendar','CALENDAR_VIEW','CALENDAR','View calendar summary and daily schedule','2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
CREATE TABLE role_permissions (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    role_id         BIGINT          NOT NULL,
    permission_id   BIGINT          NOT NULL,
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_role_permissions_uuid UNIQUE (uuid),
    CONSTRAINT fk_role_permissions_role
        FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT fk_role_permissions_permission
        FOREIGN KEY (permission_id) REFERENCES permissions (id),
    CONSTRAINT chk_role_permissions_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);
INSERT INTO role_permissions VALUES(1,'2ad9eda8-3d26-4beb-b7d8-c81c0add9617',1,1,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(2,'e66b1eba-fd0f-452e-a587-6883585b4859',1,2,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(3,'461b2fda-873c-4c8d-86ec-e2510f7c6bfe',1,3,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(4,'d2704e43-18b7-4991-b489-05106a82ce19',1,4,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(5,'8b01ba59-1c18-4b2f-8b1a-60758f1cf5c9',1,5,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(6,'d15b67cb-edec-4b94-a27e-a468b63671dd',1,6,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(7,'d424869e-a54b-4ad2-ba98-b19d83c458c3',1,7,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(8,'70dcde68-a6cc-4a3c-aa4e-f8808323f834',1,8,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(9,'9ccc251d-6f3f-472b-88dc-fa6d3902883f',1,9,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(10,'e0e09171-448c-45dc-85e3-0e3518ac2e79',1,10,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(11,'1d17893a-c67f-4032-931f-b76dd9f5abbb',1,11,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(12,'69af1fdf-70fa-4d58-9cd1-1e3640db54c2',1,12,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(13,'7bb1b9c5-f05d-4755-b44c-0822104e59e5',1,13,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(14,'1c8fa813-fef7-40f7-a01f-82d2d3eec9e0',1,14,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(15,'931cda51-02a1-4431-9a60-5096174dda55',1,15,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(16,'ec87c14a-973b-4196-b7ff-f8cbf35cd496',1,16,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(17,'efc53529-d61f-496e-8741-8afe6e0f20cf',1,17,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(18,'9d457ac1-a74a-46b8-b9e1-4430103f5dbe',1,18,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(19,'bb1ff1c7-fe9d-4074-a5e1-668c3a21b5c5',1,19,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(20,'c1c20c41-93fa-4ac0-b1d3-f2101f9e872d',1,20,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(21,'5d65f4d1-8ca3-4494-82eb-9bf08957c2ac',1,21,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(22,'8fc5f129-1384-44f3-ac45-249db1225032',1,22,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(23,'0622f382-6888-4b57-aa93-4d155cb35874',1,23,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(24,'de91dde7-9ca3-4c84-ad09-0a51551022c8',1,24,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(25,'93f248d5-9551-4c43-a314-595da86bada5',1,25,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(26,'416d3b3e-cfa6-46d3-a341-26fc33afa988',1,26,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(27,'5e2bbd90-517e-4ef1-ab3e-8f6e2c6156b6',1,27,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(28,'4cd91bd3-b5c1-445f-b732-42627dd87582',1,28,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(29,'fbad5fbf-c6a5-4f12-8e61-3509a7918b2f',1,29,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(30,'40f1a0f5-2096-4948-96a0-19586dbf5c5f',2,1,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(31,'946fbfa8-71f7-4440-b244-26027091986d',2,2,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(32,'8fa19a97-b2f9-457c-94d0-565a93549948',2,3,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(33,'202a279b-391d-4d17-ba9a-f8a5d92442f9',2,4,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(34,'e64a1a01-3b66-4793-bd42-9514347c0e88',2,5,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(35,'49a97e54-d76e-457d-a21f-058aa6496e11',2,7,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(36,'a6a234d3-f533-4ae1-b59b-bd15b1bde847',2,8,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(37,'693c5042-3f90-493a-815d-82bd02a8a3d6',2,9,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(38,'84d6a8a0-1fa1-4ece-9d37-6edc2c8fe696',2,10,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(39,'9322b2f2-4b90-4d91-b470-29caec39f312',2,11,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(40,'355b615a-68fd-43d5-82e1-87a6af3fade2',2,12,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(41,'d5e1b069-b6b1-4695-abdd-b9e0c51b4954',2,13,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(42,'8b5c2a31-7beb-4d7c-8a81-c6c76f91cb75',2,14,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(43,'9561dc5d-eb2e-4359-bc75-688eb619fd56',2,15,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(44,'57872c35-068c-4846-82cf-934df08c368e',2,16,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(45,'4a749343-8498-4f56-be0e-1c8978667ede',2,17,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(46,'262f2564-e9e1-46bd-897a-924a85644d62',2,18,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(47,'6cae4fbe-4ffa-4986-8b86-15f2238bd25b',2,19,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(48,'e6a86600-4c58-4549-81a0-55181f86e24d',2,20,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(49,'a45f0624-8c73-4794-84ec-1ee17222108e',2,21,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(50,'2f1ab6ae-76e6-4fee-be5d-763a8fabe051',2,22,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(51,'0e1c801f-9c1b-4b29-9533-e429044bf61b',2,23,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(52,'906a8450-608b-464b-a09d-624bd86dfc46',2,24,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(53,'1fa530fb-1331-4ea7-8821-8dd72790b999',2,25,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(54,'6fecd569-d555-43af-9046-97cebef609cd',2,26,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(55,'34a743b4-a63e-4e67-ae2f-ccbbcf5f2460',2,27,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(56,'7c8d7008-a4da-4eae-8e19-193cb7c459f4',2,28,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(57,'7f511ae1-941b-4b24-9bc6-f47e444bbcb0',3,1,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(58,'52d0a542-9b86-40a0-b74e-afa7fcc0c910',3,7,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(59,'0f09f9cb-380a-4fb1-a199-d57cad46000a',3,8,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(60,'b66d6e12-8ab3-422f-9456-ef98e25fbd92',3,9,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(61,'11006232-6578-4faa-9a93-621d7526ddec',3,11,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(62,'e9327f4b-89ce-43d4-a59d-0b358d19f605',3,12,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(63,'c1e4e8e7-3321-4e63-beaf-ab5c682e7462',3,13,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(64,'7e90654a-86d1-4cb1-b674-e207f282fa64',3,15,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(65,'ca78406c-c74c-4258-8dba-fb9d1a5725fa',3,16,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(66,'391c1c71-353c-4c2a-9103-c0c2c523f66b',3,17,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(67,'16c2a4c8-067a-49fa-9c76-5d497e1e1311',3,18,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(68,'5d04afc6-75c1-4487-be28-d54eae2840ee',3,19,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(69,'86fee4ef-c8bf-4af8-94f4-ad1c9d1a99e5',3,20,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(70,'590da7ab-9e28-42ea-87ea-c7915492c5cc',3,21,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(71,'2a6103e6-721a-4d0b-b9ff-448398fb1210',3,22,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(72,'94a890fd-82db-439f-8ee3-7672370cfcd9',3,23,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(73,'ed75ceaa-540f-49bf-a8d7-a975422a021e',3,25,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(74,'f677fe9d-c641-4058-899e-3fd3c0df1000',3,27,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(75,'b492f797-5e83-4d37-8257-0ba261a2dc5a',4,7,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(76,'31fb47fb-fa56-4f4b-81a4-0de23508ec21',4,11,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(77,'e2a70544-6835-4182-b04d-f8545f2dda24',4,12,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(78,'b8018e79-c792-40ba-8815-e2ee32696df0',4,15,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(79,'f889a78c-4aeb-48b4-8b00-27e8c8ce8551',4,16,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(80,'47980772-4763-40bf-8862-5187c7431ff5',4,17,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(81,'270b6b8a-ae2f-4e97-a97a-7218ea5917e5',4,18,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(82,'d73150e0-f500-4d8e-8a29-f5c6bf9287fc',4,21,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(83,'8bed09a9-8eb0-4800-8600-26092718b197',4,22,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(84,'1ecf14df-5fbf-4e41-b3a7-c066df84fcdd',4,27,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(85,'719fcf73-b678-4201-848c-f65c99356364',2,30,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(86,'324d5130-2008-41cd-9316-897c3b2ec677',3,30,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(87,'69d8babe-7b10-4788-b75a-16b9578ee72b',4,30,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
INSERT INTO role_permissions VALUES(88,'febc568b-b8d0-45e4-a38c-a840a6b9c233',1,30,'2026-07-19 18:01:56','2026-07-19 18:01:56',NULL,NULL,0,'ACTIVE',0);
CREATE TABLE users (
    id                      INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    first_name              VARCHAR(100)    NOT NULL,
    last_name               VARCHAR(100)    NOT NULL,
    email                   VARCHAR(255)    NOT NULL,
    password_hash           VARCHAR(255)    NOT NULL,
    phone                   VARCHAR(20),
    company_name            VARCHAR(255),
    profile_image_url       VARCHAR(500),
    email_verified          INTEGER         NOT NULL DEFAULT 0,
    last_login_at           TEXT,
    failed_login_attempts   INTEGER         NOT NULL DEFAULT 0,
    locked_until            TEXT,
    password_changed_at     TEXT,
    created_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_by              BIGINT,
    deleted                 INTEGER         NOT NULL DEFAULT 0,
    status                  VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version                 BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_users_uuid UNIQUE (uuid),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'LOCKED', 'PENDING')),
    CONSTRAINT chk_users_failed_attempts CHECK (failed_login_attempts >= 0)
);
INSERT INTO users VALUES(1,X'5cf023adc5174fba8dd42b886da19458','Super','Admin','admin@justcatering.com','$2a$12$oqcWBQp.FXjTLW7ZkPcoU.GbYaNjIUgD8C7mOMWliHesCATNlJ./6',NULL,'Just Catering',NULL,1,NULL,0,NULL,NULL,'1784484120404','1784484120404',NULL,NULL,0,'ACTIVE',0);
CREATE TABLE user_roles (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    user_id         BIGINT          NOT NULL,
    role_id         BIGINT          NOT NULL,
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_user_roles_uuid UNIQUE (uuid),
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT chk_user_roles_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);
INSERT INTO user_roles VALUES(1,X'4f3bac5dba36487a8610a4385becb6fc',1,1,'1784484120416','1784484120416',NULL,NULL,0,'ACTIVE',0);
CREATE TABLE refresh_tokens (
    id                  INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    user_id             BIGINT          NOT NULL,
    token               VARCHAR(500)    NOT NULL,
    expires_at          TEXT     NOT NULL,
    revoked             INTEGER         NOT NULL DEFAULT 0,
    revoked_at          TEXT,
    replaced_by_token   VARCHAR(500),
    device_info         VARCHAR(255),
    ip_address          VARCHAR(45),
    created_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          BIGINT,
    updated_by          BIGINT,
    deleted             INTEGER         NOT NULL DEFAULT 0,
    status              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version             BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_refresh_tokens_uuid UNIQUE (uuid),
    CONSTRAINT uq_refresh_tokens_token UNIQUE (token),
    CONSTRAINT fk_refresh_tokens_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT chk_refresh_tokens_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'REVOKED'))
);
CREATE TABLE departments (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    name            VARCHAR(150)    NOT NULL,
    code            VARCHAR(50)     NOT NULL,
    description     VARCHAR(500),
    parent_id       BIGINT,
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_departments_uuid UNIQUE (uuid),
    CONSTRAINT uq_departments_code UNIQUE (code),
    CONSTRAINT fk_departments_parent
        FOREIGN KEY (parent_id) REFERENCES departments (id),
    CONSTRAINT chk_departments_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);
CREATE TABLE department_members (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    department_id   BIGINT          NOT NULL,
    user_id         BIGINT          NOT NULL,
    designation     VARCHAR(150)    NOT NULL,
    is_lead         INTEGER         NOT NULL DEFAULT 0,
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_department_members_uuid UNIQUE (uuid),
    CONSTRAINT fk_department_members_department
        FOREIGN KEY (department_id) REFERENCES departments (id),
    CONSTRAINT fk_department_members_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT chk_department_members_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);
CREATE TABLE products (
    id                      INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    name                    VARCHAR(150)    NOT NULL,
    code                    VARCHAR(50)     NOT NULL,
    product_type            VARCHAR(100)    NOT NULL,
    description             VARCHAR(500),
    default_reward_amount   NUMERIC(12, 2)  NOT NULL DEFAULT 0.00,
    created_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_by              BIGINT,
    deleted                 INTEGER         NOT NULL DEFAULT 0,
    status                  VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version                 BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_products_uuid UNIQUE (uuid),
    CONSTRAINT uq_products_code UNIQUE (code),
    CONSTRAINT chk_products_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_products_reward_amount CHECK (default_reward_amount >= 0)
);
CREATE TABLE clients (
    id                                  INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_name                         VARCHAR(200)    NOT NULL,
    contact_person                      VARCHAR(150)    NOT NULL,
    mobile                              VARCHAR(20),
    email                               VARCHAR(255)    NOT NULL,
    gst_number                          VARCHAR(20),
    client_type                         VARCHAR(100)    NOT NULL,
    product_id                          BIGINT          NOT NULL,
    deal_date                           DATE,
    total_amount                        NUMERIC(14, 2)  NOT NULL DEFAULT 0.00,
    budget                              NUMERIC(14, 2),
    notes                               VARCHAR(300),
    client_stage                        VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    priority                            VARCHAR(20)     NOT NULL DEFAULT 'MEDIUM',
    requirements_completion_percentage  NUMERIC(5, 2)   NOT NULL DEFAULT 0.00,
    state                               VARCHAR(100),
    city                                VARCHAR(100),
    created_at                          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by                          BIGINT,
    updated_by                          BIGINT,
    deleted                             INTEGER         NOT NULL DEFAULT 0,
    status                              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version                             BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_clients_uuid UNIQUE (uuid),
    CONSTRAINT fk_clients_product
        FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT chk_clients_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_clients_stage CHECK (client_stage IN (
        'NEW', 'INTERESTED', 'IN_PROGRESS', 'ACTIVE', 'ON_HOLD', 'COMPLETED', 'CHURNED'
    )),
    CONSTRAINT chk_clients_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'URGENT')),
    CONSTRAINT chk_clients_total_amount CHECK (total_amount >= 0),
    CONSTRAINT chk_clients_budget CHECK (budget IS NULL OR budget >= 0),
    CONSTRAINT chk_clients_requirements_pct CHECK (
        requirements_completion_percentage >= 0
        AND requirements_completion_percentage <= 100
    )
);
CREATE TABLE leads (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    first_name      VARCHAR(100)    NOT NULL,
    last_name       VARCHAR(100)    NOT NULL,
    email           VARCHAR(255)    NOT NULL,
    company_name    VARCHAR(200)    NOT NULL,
    phone           VARCHAR(20)     NOT NULL,
    state           VARCHAR(100)    NOT NULL,
    city            VARCHAR(100)    NOT NULL,
    approx_budget   NUMERIC(14, 2)  NOT NULL,
    product_id      BIGINT,
    notes           VARCHAR(1000),
    lead_stage      VARCHAR(30)     NOT NULL DEFAULT 'NEW',
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_leads_uuid UNIQUE (uuid),
    CONSTRAINT fk_leads_product
        FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT chk_leads_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_leads_stage CHECK (lead_stage IN (
        'NEW', 'CONTACTED', 'QUALIFIED', 'CONVERTED', 'LOST'
    )),
    CONSTRAINT chk_leads_approx_budget CHECK (approx_budget >= 0)
);
CREATE TABLE follow_ups (
    id                      INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_id               BIGINT,
    lead_id                 BIGINT,
    title                   VARCHAR(200)    NOT NULL,
    follow_up_type          VARCHAR(30)     NOT NULL DEFAULT 'CALL',
    assigned_user_id        BIGINT,
    follow_up_date          DATE            NOT NULL,
    follow_up_time          TIME,
    follow_up_status        VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
    expected_budget         NUMERIC(14, 2),
    remark                  TEXT,
    next_follow_up_date     DATE,
    next_follow_up_time     TIME,
    reminder_minutes        INTEGER,
    created_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_by              BIGINT,
    deleted                 INTEGER         NOT NULL DEFAULT 0,
    status                  VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version                 BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_follow_ups_uuid UNIQUE (uuid),
    CONSTRAINT fk_follow_ups_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_follow_ups_lead
        FOREIGN KEY (lead_id) REFERENCES leads (id),
    CONSTRAINT fk_follow_ups_assigned_user
        FOREIGN KEY (assigned_user_id) REFERENCES users (id),
    CONSTRAINT chk_follow_ups_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_follow_ups_type CHECK (follow_up_type IN (
        'CALL', 'MEETING', 'EMAIL', 'VISIT', 'OTHER'
    )),
    CONSTRAINT chk_follow_ups_follow_up_status CHECK (follow_up_status IN (
        'PENDING', 'COMPLETED', 'CANCELLED', 'NO_RESPONSE'
    )),
    CONSTRAINT chk_follow_ups_expected_budget CHECK (
        expected_budget IS NULL OR expected_budget >= 0
    ),
    CONSTRAINT chk_follow_ups_reminder_minutes CHECK (
        reminder_minutes IS NULL OR reminder_minutes >= 0
    ),
    CONSTRAINT chk_follow_ups_client_or_lead CHECK (
        (client_id IS NOT NULL AND lead_id IS NULL)
        OR (client_id IS NULL AND lead_id IS NOT NULL)
    )
);
CREATE TABLE client_queries (
    id                  INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_id           BIGINT          NOT NULL,
    title               VARCHAR(200)    NOT NULL,
    query_type          VARCHAR(100),
    assigned_user_id    BIGINT,
    department_id       BIGINT,
    priority            VARCHAR(20)     NOT NULL DEFAULT 'MEDIUM',
    query_status        VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
    remarks             TEXT,
    image_url           VARCHAR(500),
    completed_at        TEXT,
    created_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          BIGINT,
    updated_by          BIGINT,
    deleted             INTEGER         NOT NULL DEFAULT 0,
    status              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version             BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_client_queries_uuid UNIQUE (uuid),
    CONSTRAINT fk_client_queries_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_client_queries_assigned_user
        FOREIGN KEY (assigned_user_id) REFERENCES users (id),
    CONSTRAINT fk_client_queries_department
        FOREIGN KEY (department_id) REFERENCES departments (id),
    CONSTRAINT chk_client_queries_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_client_queries_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'URGENT')),
    CONSTRAINT chk_client_queries_query_status CHECK (query_status IN (
        'PENDING', 'IN_PROGRESS', 'NEEDS_ATTENTION', 'COMPLETED', 'SOLVED'
    ))
);
CREATE TABLE payments (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_id       BIGINT          NOT NULL,
    product_id      BIGINT,
    invoice_number  VARCHAR(50)     NOT NULL,
    payment_date    DATE            NOT NULL,
    amount          NUMERIC(14, 2)  NOT NULL,
    bank_type       VARCHAR(50),
    payment_mode    VARCHAR(30)     NOT NULL DEFAULT 'CASH',
    remarks         TEXT,
    receipt_url     VARCHAR(500),
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_payments_uuid UNIQUE (uuid),
    CONSTRAINT fk_payments_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_payments_product
        FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT chk_payments_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_payments_amount CHECK (amount >= 0),
    CONSTRAINT chk_payments_mode CHECK (payment_mode IN (
        'CASH', 'UPI', 'BANK_TRANSFER', 'CARD', 'CHEQUE', 'OTHER'
    ))
);
CREATE TABLE expenses (
    id                      INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_id               BIGINT,
    title                   VARCHAR(200)    NOT NULL,
    expense_type            VARCHAR(30)     NOT NULL,
    member_user_id          BIGINT,
    expense_date            DATE            NOT NULL,
    paid_date               DATE,
    due_date                DATE,
    amount                  NUMERIC(14, 2)  NOT NULL,
    payment_mode            VARCHAR(50),
    from_city               VARCHAR(100),
    to_city                 VARCHAR(100),
    from_date               DATE,
    to_date                 DATE,
    km                      NUMERIC(10, 2),
    account_contact         VARCHAR(200),
    remarks                 VARCHAR(1000),
    bill_url                VARCHAR(500),
    created_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_by              BIGINT,
    deleted                 INTEGER         NOT NULL DEFAULT 0,
    status                  VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version                 BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_expenses_uuid UNIQUE (uuid),
    CONSTRAINT fk_expenses_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_expenses_member_user
        FOREIGN KEY (member_user_id) REFERENCES users (id),
    CONSTRAINT chk_expenses_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_expenses_type CHECK (expense_type IN (
        'TRAVEL', 'FOOD', 'OFFICE', 'OTHER'
    )),
    CONSTRAINT chk_expenses_amount CHECK (amount >= 0),
    CONSTRAINT chk_expenses_km CHECK (km IS NULL OR km >= 0)
);
CREATE TABLE client_manager_assignments (
    id                  INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_id           BIGINT          NOT NULL,
    department_id       BIGINT,
    user_id             BIGINT          NOT NULL,
    project_name        VARCHAR(200),
    close_date          DATE,
    reward_amount       NUMERIC(14, 2),
    created_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          BIGINT,
    updated_by          BIGINT,
    deleted             INTEGER         NOT NULL DEFAULT 0,
    status              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version             BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_client_manager_assignments_uuid UNIQUE (uuid),
    CONSTRAINT fk_client_manager_assignments_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_client_manager_assignments_department
        FOREIGN KEY (department_id) REFERENCES departments (id),
    CONSTRAINT fk_client_manager_assignments_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT chk_client_manager_assignments_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_client_manager_assignments_reward CHECK (reward_amount IS NULL OR reward_amount >= 0)
);
CREATE TABLE client_deadlines (
    id                  INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_id           BIGINT          NOT NULL,
    department_id       BIGINT,
    current_deadline    DATE            NOT NULL,
    new_deadline        DATE            NOT NULL,
    reason              VARCHAR(1000)   NOT NULL,
    created_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          BIGINT,
    updated_by          BIGINT,
    deleted             INTEGER         NOT NULL DEFAULT 0,
    status              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version             BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_client_deadlines_uuid UNIQUE (uuid),
    CONSTRAINT fk_client_deadlines_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_client_deadlines_department
        FOREIGN KEY (department_id) REFERENCES departments (id),
    CONSTRAINT chk_client_deadlines_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);
CREATE TABLE product_types (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    name            VARCHAR(100)    NOT NULL,
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_product_types_uuid UNIQUE (uuid),
    CONSTRAINT chk_product_types_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);
CREATE TABLE product_names (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    product_type_id BIGINT          NOT NULL,
    name            VARCHAR(150)    NOT NULL,
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_product_names_uuid UNIQUE (uuid),
    CONSTRAINT fk_product_names_type
        FOREIGN KEY (product_type_id) REFERENCES product_types (id),
    CONSTRAINT chk_product_names_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);
INSERT INTO sqlite_sequence VALUES('roles',4);
INSERT INTO sqlite_sequence VALUES('permissions',30);
INSERT INTO sqlite_sequence VALUES('role_permissions',88);
INSERT INTO sqlite_sequence VALUES('users',1);
INSERT INTO sqlite_sequence VALUES('user_roles',1);
CREATE INDEX "flyway_schema_history_s_idx" ON "flyway_schema_history" ("success");
CREATE INDEX idx_roles_status ON roles (status) WHERE deleted = 0;
CREATE INDEX idx_roles_deleted ON roles (deleted);
CREATE INDEX idx_permissions_module ON permissions (module) WHERE deleted = 0;
CREATE INDEX idx_permissions_status ON permissions (status) WHERE deleted = 0;
CREATE INDEX idx_permissions_deleted ON permissions (deleted);
CREATE INDEX idx_role_permissions_role_id ON role_permissions (role_id) WHERE deleted = 0;
CREATE INDEX idx_role_permissions_permission_id ON role_permissions (permission_id) WHERE deleted = 0;
CREATE INDEX idx_users_email ON users (email) WHERE deleted = 0;
CREATE INDEX idx_users_status ON users (status) WHERE deleted = 0;
CREATE INDEX idx_users_phone ON users (phone) WHERE deleted = 0 AND phone IS NOT NULL;
CREATE INDEX idx_users_deleted ON users (deleted);
CREATE INDEX idx_users_created_at ON users (created_at);
CREATE INDEX idx_user_roles_user_id ON user_roles (user_id) WHERE deleted = 0;
CREATE INDEX idx_user_roles_role_id ON user_roles (role_id) WHERE deleted = 0;
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens (user_id) WHERE deleted = 0;
CREATE INDEX idx_refresh_tokens_expires_at ON refresh_tokens (expires_at) WHERE revoked = 0 AND deleted = 0;
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens (token) WHERE deleted = 0;
CREATE UNIQUE INDEX uq_role_permissions_role_perm_active
    ON role_permissions (role_id, permission_id)
    WHERE deleted = 0;
CREATE UNIQUE INDEX uq_user_roles_user_role_active
    ON user_roles (user_id, role_id)
    WHERE deleted = 0;
CREATE UNIQUE INDEX uq_departments_name_active
    ON departments (LOWER(name))
    WHERE deleted = 0;
CREATE INDEX idx_departments_status ON departments (status) WHERE deleted = 0;
CREATE INDEX idx_departments_parent_id ON departments (parent_id) WHERE deleted = 0;
CREATE INDEX idx_departments_deleted ON departments (deleted);
CREATE INDEX idx_departments_name ON departments (name) WHERE deleted = 0;
CREATE UNIQUE INDEX uq_department_members_dept_user_active
    ON department_members (department_id, user_id)
    WHERE deleted = 0;
CREATE INDEX idx_department_members_department_id
    ON department_members (department_id) WHERE deleted = 0;
CREATE INDEX idx_department_members_user_id
    ON department_members (user_id) WHERE deleted = 0;
CREATE INDEX idx_department_members_deleted ON department_members (deleted);
CREATE UNIQUE INDEX uq_products_name_type_active
    ON products (LOWER(name), LOWER(product_type))
    WHERE deleted = 0;
CREATE INDEX idx_products_status ON products (status) WHERE deleted = 0;
CREATE INDEX idx_products_type ON products (product_type) WHERE deleted = 0;
CREATE INDEX idx_products_name ON products (name) WHERE deleted = 0;
CREATE INDEX idx_products_deleted ON products (deleted);
CREATE UNIQUE INDEX uq_clients_email_active
    ON clients (LOWER(email))
    WHERE deleted = 0;
CREATE INDEX idx_clients_status ON clients (status) WHERE deleted = 0;
CREATE INDEX idx_clients_stage ON clients (client_stage) WHERE deleted = 0;
CREATE INDEX idx_clients_product_id ON clients (product_id) WHERE deleted = 0;
CREATE INDEX idx_clients_client_name ON clients (client_name) WHERE deleted = 0;
CREATE INDEX idx_clients_mobile ON clients (mobile) WHERE deleted = 0 AND mobile IS NOT NULL;
CREATE INDEX idx_clients_priority ON clients (priority) WHERE deleted = 0;
CREATE INDEX idx_clients_deal_date ON clients (deal_date) WHERE deleted = 0;
CREATE INDEX idx_clients_deleted ON clients (deleted);
CREATE INDEX idx_clients_created_at ON clients (created_at);
CREATE UNIQUE INDEX uq_leads_email_active
    ON leads (LOWER(email))
    WHERE deleted = 0;
CREATE INDEX idx_leads_status ON leads (status) WHERE deleted = 0;
CREATE INDEX idx_leads_stage ON leads (lead_stage) WHERE deleted = 0;
CREATE INDEX idx_leads_product_id ON leads (product_id) WHERE deleted = 0;
CREATE INDEX idx_leads_company_name ON leads (company_name) WHERE deleted = 0;
CREATE INDEX idx_leads_phone ON leads (phone) WHERE deleted = 0;
CREATE INDEX idx_leads_state ON leads (state) WHERE deleted = 0;
CREATE INDEX idx_leads_city ON leads (city) WHERE deleted = 0;
CREATE INDEX idx_leads_deleted ON leads (deleted);
CREATE INDEX idx_leads_created_at ON leads (created_at);
CREATE INDEX idx_follow_ups_status ON follow_ups (status) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_client_id ON follow_ups (client_id) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_lead_id ON follow_ups (lead_id) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_assigned_user_id ON follow_ups (assigned_user_id) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_type ON follow_ups (follow_up_type) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_follow_up_status ON follow_ups (follow_up_status) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_follow_up_date ON follow_ups (follow_up_date) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_deleted ON follow_ups (deleted);
CREATE INDEX idx_follow_ups_created_at ON follow_ups (created_at);
CREATE INDEX idx_client_queries_status ON client_queries (status) WHERE deleted = 0;
CREATE INDEX idx_client_queries_client_id ON client_queries (client_id) WHERE deleted = 0;
CREATE INDEX idx_client_queries_assigned_user_id ON client_queries (assigned_user_id) WHERE deleted = 0;
CREATE INDEX idx_client_queries_department_id ON client_queries (department_id) WHERE deleted = 0;
CREATE INDEX idx_client_queries_priority ON client_queries (priority) WHERE deleted = 0;
CREATE INDEX idx_client_queries_query_status ON client_queries (query_status) WHERE deleted = 0;
CREATE INDEX idx_client_queries_query_type ON client_queries (query_type) WHERE deleted = 0;
CREATE INDEX idx_client_queries_completed_at ON client_queries (completed_at) WHERE deleted = 0;
CREATE INDEX idx_client_queries_deleted ON client_queries (deleted);
CREATE INDEX idx_client_queries_created_at ON client_queries (created_at);
CREATE UNIQUE INDEX uq_payments_invoice_number_active
    ON payments (invoice_number)
    WHERE deleted = 0;
CREATE INDEX idx_payments_status ON payments (status) WHERE deleted = 0;
CREATE INDEX idx_payments_client_id ON payments (client_id) WHERE deleted = 0;
CREATE INDEX idx_payments_product_id ON payments (product_id) WHERE deleted = 0;
CREATE INDEX idx_payments_payment_mode ON payments (payment_mode) WHERE deleted = 0;
CREATE INDEX idx_payments_payment_date ON payments (payment_date) WHERE deleted = 0;
CREATE INDEX idx_payments_invoice_number ON payments (invoice_number) WHERE deleted = 0;
CREATE INDEX idx_payments_deleted ON payments (deleted);
CREATE INDEX idx_payments_created_at ON payments (created_at);
CREATE INDEX idx_expenses_status ON expenses (status) WHERE deleted = 0;
CREATE INDEX idx_expenses_client_id ON expenses (client_id) WHERE deleted = 0;
CREATE INDEX idx_expenses_member_user_id ON expenses (member_user_id) WHERE deleted = 0;
CREATE INDEX idx_expenses_type ON expenses (expense_type) WHERE deleted = 0;
CREATE INDEX idx_expenses_expense_date ON expenses (expense_date) WHERE deleted = 0;
CREATE INDEX idx_expenses_paid_date ON expenses (paid_date) WHERE deleted = 0;
CREATE INDEX idx_expenses_due_date ON expenses (due_date) WHERE deleted = 0;
CREATE INDEX idx_expenses_deleted ON expenses (deleted);
CREATE INDEX idx_expenses_created_at ON expenses (created_at);
CREATE UNIQUE INDEX uq_client_manager_assignments_client_user_active
    ON client_manager_assignments (client_id, user_id)
    WHERE deleted = 0;
CREATE INDEX idx_client_manager_assignments_status ON client_manager_assignments (status) WHERE deleted = 0;
CREATE INDEX idx_client_manager_assignments_client_id ON client_manager_assignments (client_id) WHERE deleted = 0;
CREATE INDEX idx_client_manager_assignments_department_id ON client_manager_assignments (department_id) WHERE deleted = 0;
CREATE INDEX idx_client_manager_assignments_user_id ON client_manager_assignments (user_id) WHERE deleted = 0;
CREATE INDEX idx_client_manager_assignments_close_date ON client_manager_assignments (close_date) WHERE deleted = 0;
CREATE INDEX idx_client_manager_assignments_deleted ON client_manager_assignments (deleted);
CREATE INDEX idx_client_manager_assignments_created_at ON client_manager_assignments (created_at);
CREATE INDEX idx_client_deadlines_status ON client_deadlines (status) WHERE deleted = 0;
CREATE INDEX idx_client_deadlines_client_id ON client_deadlines (client_id) WHERE deleted = 0;
CREATE INDEX idx_client_deadlines_department_id ON client_deadlines (department_id) WHERE deleted = 0;
CREATE INDEX idx_client_deadlines_current_deadline ON client_deadlines (current_deadline) WHERE deleted = 0;
CREATE INDEX idx_client_deadlines_new_deadline ON client_deadlines (new_deadline) WHERE deleted = 0;
CREATE INDEX idx_client_deadlines_deleted ON client_deadlines (deleted);
CREATE INDEX idx_client_deadlines_created_at ON client_deadlines (created_at);
CREATE UNIQUE INDEX uq_product_types_name_active
    ON product_types (LOWER(name))
    WHERE deleted = 0;
CREATE INDEX idx_product_types_status ON product_types (status) WHERE deleted = 0;
CREATE INDEX idx_product_types_deleted ON product_types (deleted);
CREATE UNIQUE INDEX uq_product_names_type_name_active
    ON product_names (product_type_id, LOWER(name))
    WHERE deleted = 0;
CREATE INDEX idx_product_names_type_id ON product_names (product_type_id) WHERE deleted = 0;
CREATE INDEX idx_product_names_status ON product_names (status) WHERE deleted = 0;
CREATE INDEX idx_product_names_deleted ON product_names (deleted);
COMMIT;
