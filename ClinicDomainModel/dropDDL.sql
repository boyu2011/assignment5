ALTER TABLE PATIENT DROP CONSTRAINT PATIENT_clinic_fk
ALTER TABLE TREATMENT DROP CONSTRAINT TREATMENTpatientfk
ALTER TABLE TREATMENT DROP CONSTRAINT TRATMENTproviderfk
ALTER TABLE DRUGTREATMENT DROP CONSTRAINT DRUGTREATMENT_ID
ALTER TABLE PROVIDER DROP CONSTRAINT PROVIDER_clinic_fk
DROP TABLE PATIENT
DROP TABLE CLINIC
DROP TABLE TREATMENT
DROP TABLE DRUGTREATMENT
DROP TABLE PROVIDER
DELETE FROM SEQUENCE WHERE SEQ_NAME = 'SEQ_GEN'
