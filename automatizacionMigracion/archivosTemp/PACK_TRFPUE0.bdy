create or replace package body PACK_TRFPUE0 is

V_CODPACK VARCHAR(20) := 'PACK_TRFPUE0-';

-- Procedimientos de inicializacion
PROCEDURE INI_CAMPUE IS
BEGIN
  G_CAMPUE.prueba1 := null;
  G_CAMPUE.prueba2 := -1;
  G_CAMPUE.prueba3 := null;
END INI_CAMPUE;
----------------------------------------------------------------------------------
PROCEDURE INI_GLOBALES IS
BEGIN
  INI_CAMPUE;
END INI_GLOBALES;
----------------------------------------------------------------------------------
-- 
-- Procedimientos de extraccion y actualizacion 
-- de variables globales 
--
PROCEDURE SEL_CAMPUE(json IN OUT pljson) IS 
  json_aux pljson; 
 BEGIN 
  json_aux := pljson('{}'); 

  if G_CAMPUE.prueba1 is not null then 
    json_aux.put('prueba1', G_CAMPUE.prueba1); 
  else 
    json_aux.put('prueba1', '' );

  if G_CAMPUE.prueba2 is not null then 
    json_aux.put('prueba2', G_CAMPUE.prueba2); 
  else 
    json_aux.put('prueba2', 0 );

  if G_CAMPUE.prueba3 is not null then 
    json_aux.put('prueba3', G_CAMPUE.prueba3); 
  else 
    json_aux.put('prueba3', '' );

  json.put('PAN_PUE',json_aux); 
END SEL_CAMPUE;
----------------------------------------------------------------------------------
PROCEDURE UPD_CAMPUE(json IN pljson) IS 
  json_aux pljson; 
BEGIN 
  INI_CAMPUE; 
  
  json_aux := pljson_ext.get_json(json,'PAN_PUE');  
G_CAMPUE.prueba1 := pljson_ext.get_string(json_aux, 'prueba1');
G_CAMPUE.prueba2 := pljson_ext.get_number(json_aux, 'prueba2');
G_CAMPUE.prueba3 := to_date(pljson_ext.get_string(json_aux, 'prueba3'), 'DD/MM/YY HH24:MI:SS');

END UPD_CAMPUE;
----------------------------------------------------------------------------------
PROCEDURE LOG_CAMPUE(P_TIPLOG NUMBER) IS 
  V_LOG CLOB := EMPTY_CLOB(); 
  V_MENSA VARCHAR2(1000); 
BEGIN 

  -- Las trazas estan activadas 
  IF PACK_TRF.HAYLOG THEN 
    -- Construimos el mensaje con los valores de los campos de la variable global 
    V_MENSA := 'G_CAMPUE.prueba1 := '; 
    IF G_CAMPUE.prueba1 IS NULL THEN 
       V_MENSA := V_MENSA ||'NULL' || ';' || CHR(13); 
    ELSE 
       V_MENSA := V_MENSA ||'''' || G_CAMPUE.prueba1 ||''';' || CHR(13); 
    END IF;
    V_LOG := V_MENSA;

    V_MENSA := 'G_CAMPUE.prueba2 := ' || NVL(TO_CHAR(G_CAMPUE.prueba2),-1) || ';' || CHR(13);
    DBMS_LOB.WRITEAPPEND(V_LOG,LENGTH(V_MENSA),V_MENSA);

    V_MENSA := 'G_CAMPUE.prueba3 := '; 
    IF G_CAMPUE.prueba3 IS NULL THEN 
       V_MENSA := V_MENSA || '0/0/0' || ';' || CHR(13); 
    ELSE 
       V_MENSA := V_MENSA || '''' || to_char(G_CAMPUE.prueba3,'DD-MM-RRRR HH24:MI:SS') ||''';' || CHR(13);
    END IF;
    DBMS_LOB.WRITEAPPEND(V_LOG,LENGTH(V_MENSA),V_MENSA);

    -- Grabamos la traza 
    PACK_TRF.LOG_DATOSBD(PACK_TRF.G_OPERAR,PACK_TRF.G_ERROR,V_LOG,P_TIPLOG); 
  END IF; 
END LOG_CAMPUE;
----------------------------------------------------------------------------------
-- 
-- Procedimientos de logica de negocio 
--
  /*SIN APLICAR*/

end PACK_TRFPUE0;
