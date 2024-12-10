/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     9/12/2024 22:39:55                           */
/*==============================================================*/


drop index RELATIONSHIP_3_FK;

drop index RELATIONSHIP_2_FK;

drop index ASIGNACION_MENU_RECETA_PK;

drop table ASIGNACION_MENU_RECETA;

drop index INGREDIENTE_PK;

drop table INGREDIENTE;

drop index MENU_SEMANAL_PK;

drop table MENU_SEMANAL;

drop index RECETA_PK;

drop table RECETA;

drop index RELATIONSHIP_1_FK;

drop index RELATIONSHIP_4_FK;

drop index RECETA_INGREDIENTE_PK;

drop table RECETA_INGREDIENTE;

/*==============================================================*/
/* Table: ASIGNACION_MENU_RECETA                                */
/*==============================================================*/
create table ASIGNACION_MENU_RECETA (
   ID_MENU              INT4                 not null,
   ID_RECETA            INT4                 not null,
   ID_ASIGNACION        INT4                 not null,
   TIPO_COMIDA          TEXT                 not null,
   constraint PK_ASIGNACION_MENU_RECETA primary key (ID_MENU, ID_RECETA, ID_ASIGNACION)
);

/*==============================================================*/
/* Index: ASIGNACION_MENU_RECETA_PK                             */
/*==============================================================*/
create unique index ASIGNACION_MENU_RECETA_PK on ASIGNACION_MENU_RECETA (
ID_MENU,
ID_RECETA,
ID_ASIGNACION
);

/*==============================================================*/
/* Index: RELATIONSHIP_2_FK                                     */
/*==============================================================*/
create  index RELATIONSHIP_2_FK on ASIGNACION_MENU_RECETA (
ID_RECETA
);

/*==============================================================*/
/* Index: RELATIONSHIP_3_FK                                     */
/*==============================================================*/
create  index RELATIONSHIP_3_FK on ASIGNACION_MENU_RECETA (
ID_MENU
);

/*==============================================================*/
/* Table: INGREDIENTE                                           */
/*==============================================================*/
create table INGREDIENTE (
   ID_INGREDIENTE       INT4                 not null,
   NOMBRE               TEXT                 not null,
   CANTIDAD             TEXT                 not null,
   constraint PK_INGREDIENTE primary key (ID_INGREDIENTE)
);

/*==============================================================*/
/* Index: INGREDIENTE_PK                                        */
/*==============================================================*/
create unique index INGREDIENTE_PK on INGREDIENTE (
ID_INGREDIENTE
);

/*==============================================================*/
/* Table: MENU_SEMANAL                                          */
/*==============================================================*/
create table MENU_SEMANAL (
   ID_MENU              INT4                 not null,
   FECHA                DATE                 not null,
   constraint PK_MENU_SEMANAL primary key (ID_MENU)
);

/*==============================================================*/
/* Index: MENU_SEMANAL_PK                                       */
/*==============================================================*/
create unique index MENU_SEMANAL_PK on MENU_SEMANAL (
ID_MENU
);

/*==============================================================*/
/* Table: RECETA                                                */
/*==============================================================*/
create table RECETA (
   ID_RECETA            INT4                 not null,
   NOMBRE               TEXT                 not null,
   CATEGORIA            TEXT                 not null,
   PROCESO              TEXT                 null,
   constraint PK_RECETA primary key (ID_RECETA)
);

/*==============================================================*/
/* Index: RECETA_PK                                             */
/*==============================================================*/
create unique index RECETA_PK on RECETA (
ID_RECETA
);

/*==============================================================*/
/* Table: RECETA_INGREDIENTE                                    */
/*==============================================================*/
create table RECETA_INGREDIENTE (
   ID_RECETA            INT4                 not null,
   ID_INGREDIENTE       INT4                 not null,
   constraint PK_RECETA_INGREDIENTE primary key (ID_RECETA, ID_INGREDIENTE)
);

/*==============================================================*/
/* Index: RECETA_INGREDIENTE_PK                                 */
/*==============================================================*/
create unique index RECETA_INGREDIENTE_PK on RECETA_INGREDIENTE (
ID_RECETA,
ID_INGREDIENTE
);

/*==============================================================*/
/* Index: RELATIONSHIP_4_FK                                     */
/*==============================================================*/
create  index RELATIONSHIP_4_FK on RECETA_INGREDIENTE (
ID_INGREDIENTE
);

/*==============================================================*/
/* Index: RELATIONSHIP_1_FK                                     */
/*==============================================================*/
create  index RELATIONSHIP_1_FK on RECETA_INGREDIENTE (
ID_RECETA
);

alter table ASIGNACION_MENU_RECETA
   add constraint FK_ASIGNACI_RELATIONS_RECETA foreign key (ID_RECETA)
      references RECETA (ID_RECETA)
      on delete restrict on update restrict;

alter table ASIGNACION_MENU_RECETA
   add constraint FK_ASIGNACI_RELATIONS_MENU_SEM foreign key (ID_MENU)
      references MENU_SEMANAL (ID_MENU)
      on delete restrict on update restrict;

alter table RECETA_INGREDIENTE
   add constraint FK_RECETA_I_RELATIONS_RECETA foreign key (ID_RECETA)
      references RECETA (ID_RECETA)
      on delete restrict on update restrict;

alter table RECETA_INGREDIENTE
   add constraint FK_RECETA_I_RELATIONS_INGREDIE foreign key (ID_INGREDIENTE)
      references INGREDIENTE (ID_INGREDIENTE)
      on delete restrict on update restrict;

