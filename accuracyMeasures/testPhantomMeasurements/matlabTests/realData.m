%length between points
len_O_1=25.5;
len_O_2=51;
len_O_3=50;
len_O_4=59;
len_O_5=56;
len_O_6=52.5;
len_O_7=25.5;
len_1_11=39.5;
len_1_12=40;
len_7_71=18.5;
len_7_72=41;
len_11_12=24.5;
len_12_2=26.5;
len_1_2=36;
len_2_3=49.5;
len_3_4=25.5;
len_4_5=25.5;
len_5_6=46.5;
len_6_7=32;
len_71_72=26.5;
len_6_71=26.5; %**********TODO: VERIFY THIS************

%all the angles we can now get
ang_1_O_2 = getAngleFromSSS(len_O_1,len_O_2,len_1_2);
ang_2_O_3 = getAngleFromSSS(len_O_2,len_O_3,len_2_3);
ang_3_O_4 = getAngleFromSSS(len_O_3,len_O_4,len_3_4);
ang_4_O_5 = getAngleFromSSS(len_O_4,len_O_5,len_4_5);
ang_5_O_6 = getAngleFromSSS(len_O_5,len_O_6,len_5_6);
ang_6_O_7 = getAngleFromSSS(len_O_6,len_O_7,len_6_7);
ang_72_7_71 = getAngleFromSSS(len_7_72,len_7_71,len_71_72);
ang_6_7_71 = getAngleFromSSS(len_6_7,len_7_71,len_6_71);