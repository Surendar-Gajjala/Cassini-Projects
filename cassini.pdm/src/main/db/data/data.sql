DO $$
DECLARE
    partSource  INTEGER;
    docSource   INTEGER;
    dwgSource   INTEGER;
    assySource  INTEGER;
    batchSource INTEGER;
    oemSource   INTEGER;

    revSequence INTEGER;
    lifeCycle   INTEGER;
    numSequence INTEGER;
    alnumSequence   INTEGER;
    alpSequence INTEGER;

    assyType    INTEGER;
    partType    INTEGER;
    docType     INTEGER;
    dwgType     INTEGER;
    mechType    INTEGER;
    elecType    INTEGER;
    softType    INTEGER;

    objtypeId   INTEGER;
    suppNumSource   INTEGER;
    suppRevSequence INTEGER;
BEGIN

    partSource      := nextval('AUTONUMBER_ID_SEQ');
    docSource       := nextval('AUTONUMBER_ID_SEQ');
    dwgSource       := nextval('AUTONUMBER_ID_SEQ');
    assySource      := nextval('AUTONUMBER_ID_SEQ');
    batchSource     := nextval('AUTONUMBER_ID_SEQ');
    oemSource       := nextval('AUTONUMBER_ID_SEQ');

    /* Autonumbers */
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (partSource, 'Default Part Number Source', 'Auto part number', 5, 1, 1, 1, '0', 'P', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (docSource, 'Default Document Number Source', 'Auto document number', 5, 1, 1, 1, '0', 'DOC-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (dwgSource, 'Default Drawing Number Source', 'Auto drawing number', 5, 1, 1, 1, '0', 'DWG-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (assySource, 'Default Assembly Number Source', 'Auto assembly number', 5, 1, 1, 1, '0', 'ASSY-', '');


    /* LOVs */
    revSequence     := nextval('LOV_ID_SEQ');
    lifeCycle       := nextval('LOV_ID_SEQ');
    numSequence     := nextval('LOV_ID_SEQ');
    alnumSequence   := nextval('LOV_ID_SEQ');
    alpSequence     := nextval('LOV_ID_SEQ');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (revSequence, 'Revision Sequence', 'Default Revision Sequence', 'Default revision sequence',
            '{A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z}', 'A');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (numSequence, 'Numeric Sequence', 'Default Numeric Sequence', 'Default numeric sequence',
            '{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40}', '0');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (alnumSequence, 'Alpha Numeric Sequence', 'Default AlphaNumeric Sequence', 'Default alphaNumeric Sequence',
            '{A1,A2,A3,A4,A5,A6,A7,A8,A9,B1,B2,B3,B4,B5,B6,B7,B8,B9,C1,C2,C3,C4,C5,C6,C7,C8,C9,D1,D2,D3,D4,D5,D6,D7,D8,D9}', 'A1');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (alpSequence, 'Alphabet Sequence', 'Default Alphabet Sequence', 'Default alphabet sequence',
            '{AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,AY,AZ,BA,BB,BC,BD,BE,BF,BG,BH,BI,BJ,BK,BL,BM,BN,BO,BP,BQ,BR,BS,BT,BU,BV,BW,BX,BY,BZ}', 'AA');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (lifeCycle, 'Lifecycle States', 'Default Lifecycle States', 'Default lifecycle states',
            '{Preliminary,Prototype,Released,Obsolete}', 'Preliminary');

    INSERT INTO APPLICATION_DETAILS (ID, OPTION_KEY, OPTION_NAME, CREATED_DATE, MODIFIED_DATE, VALUE)
    VALUES (NEXTVAL('APPLICATION_SEQUENCE'), 1, 'NAME',CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'CASSINI_PDM');

    INSERT INTO APPLICATION_DETAILS (ID, OPTION_KEY, OPTION_NAME, CREATED_DATE, MODIFIED_DATE, VALUE)
    VALUES (NEXTVAL('APPLICATION_SEQUENCE'), 6, 'SHOW_LANGUAGE',CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'true');

END $$;