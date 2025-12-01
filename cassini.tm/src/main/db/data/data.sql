DO $$
DECLARE
    taskSource  INTEGER;
    personRole  INTEGER;
    pendingReason INTEGER;
BEGIN
    taskSource      := nextval('AUTONUMBER_ID_SEQ');

    /* Autonumbers */
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (taskSource, 'Default Task Number Source', 'Auto task number', 5, 1, 1, 1, '0', 'T', '');

    INSERT INTO PERSONTYPE (TYPE_ID, NAME, DESCRIPTION)
    VALUES (nextval('PERSONTYPE_ID_SEQ'), 'Emergency Contact', 'Emergency Contact');

    /*Insertion Of Shifts */

     INSERT INTO SHIFT (SHIFT_ID, NAME, START_TIME, END_TIME)
     VALUES (nextval('SHIFT_ID_SEQ'), 'Morning Shift', '08:00:00', '14:00:00');

     INSERT INTO SHIFT (SHIFT_ID, NAME, START_TIME, END_TIME)
     VALUES (nextval('SHIFT_ID_SEQ'), 'Evening Shift', '14:00:00', '22:00:00');

     INSERT INTO SHIFT (SHIFT_ID, NAME, START_TIME, END_TIME)
     VALUES (nextval('SHIFT_ID_SEQ'), 'Night Shift', '22:00:00', '06:00:00');

    /* LOVs */
      personRole   := nextval('LOV_ID_SEQ');
      INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
      VALUES (personRole, 'Person Roles', 'Default Person Roles', 'Default Person Roles',
            '{Administrator,Staff,Supervisor,Officer}', 'Staff');

      INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
       VALUES (nextval('LOV_ID_SEQ'), 'Work Locations', 'Default Work Location', 'Default Work Locations',
            '{Hut-1,Hut-2,Hut-3,Hut-4,Hut-5,Hut-6,Hut-7,Hut-8,Hut-9,Hut-10,Goomty-1,Goomty-2,Goomty-3,Goomty-4,Goomty-5,Goomty-6,Goomty-7,Goomty-8,Goomty-9,Goomty-10,Goomty-11,Goomty-12,Goomty-13,Goomty-14,Relay Room,Panel Room,G Cabin,F Cabin,VNC,NWBH,D Cabin,WI Cabin,Bulb Cabin,Power/IPS,Points - SZ,Track Circuits South Zone,Track Circuits North Zone,Rescue Team Control Center,Logistics,Telecom Arrangements,CT Rack,Tower Room,KCC,Points-SZ,Points-NZ,Track Circuits SZ,Track Circuits NZ,Signals South,Signals North,NI Signals,Rescue Team Control Centre,Engg. works Road 1 - 4}', 'Hut-1');

      /*pendingReason   := nextval('LOV_ID_SEQ');

      INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
      VALUES (pendingReason, 'Pending Reasons', 'Default Pending Reasons', 'Default Pending Reasons',
            '{Required Equipment not available ,This is not Necessary now, Important Task is there, Task is not Ready(before task is not completed}', 'Required Equipment not available');*/

END $$;