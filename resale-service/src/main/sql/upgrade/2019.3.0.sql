--------------------------------------------------------
--  DDL for Table POPUP
--------------------------------------------------------
ALTER TABLE "POPUP"
  ADD (
    "URL" VARCHAR2(2000)
  )

COMMENT ON COLUMN POPUP.URL IS '링크 URL'
