create or replace package PACK_TRFPUE0 is 
/*============================================================================ 
  Procedimiento: PACK_TRFPUE0
  Version: 1.0 
  Descripcion: Paquete para TRFPUE0


============================================================================*/

  -- Tipo de datos para pantalla PUE
  TYPE PAN_PUE IS RECORD
  {
       prueba1 VARCHAR2(2),
       prueba2 NUMBER(2),
       prueba3 DATE
  };

  -- Variables globales
  G_CAMPUE  PAN_PUE;  -- Campos de pantalla PUE

  -- Procedimientos de inicializacion 
  -- de variables globales
  PROCEDURE INI_CAMPUE;
  PROCEDURE INI_GLOBALES;

  -- Procedimientos de extraccion, actualizacion 
  -- y log de variables globales
  PROCEDURE SEL_CAMPUE(json IN OUT pljson); 
  PROCEDURE UPD_CAMPUE(json IN pljson); 
  PROCEDURE LOG_CAMPUE(P_TIPLOG NUMBER); 

  -- Procedimientos de logica de negocio
  /*SIN APLICAR*/

end PACK_TRFPUE0;
