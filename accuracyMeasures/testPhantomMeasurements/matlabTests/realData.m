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
len_6_71=24.5;

%all the angles we can now get
ang_1_O_2 = getAngleFromSSS(len_O_1,len_O_2,len_1_2);
ang_2_O_3 = getAngleFromSSS(len_O_2,len_O_3,len_2_3);
ang_3_O_4 = getAngleFromSSS(len_O_3,len_O_4,len_3_4);
ang_4_O_5 = getAngleFromSSS(len_O_4,len_O_5,len_4_5);
ang_5_O_6 = getAngleFromSSS(len_O_5,len_O_6,len_5_6);
ang_6_O_7 = getAngleFromSSS(len_O_6,len_O_7,len_6_7);
ang_72_7_71 = getAngleFromSSS(len_7_72,len_7_71,len_71_72);
ang_6_7_71 = getAngleFromSSS(len_6_7,len_7_71,len_6_71);
ang_2_1_O = getAngleFromSSS(len_1_2,len_O_1,len_O_2);
ang_6_7_O = getAngleFromSSS(len_O_7,len_6_7,len_O_6);

%used to get x,y coordinates for points where we can use the
%   following coordinate system:
%   O is origin. line_O_1 is positive x-axis line
r_1=len_O_1;
theta_1 = 0;
r_2=len_O_2;
theta_2 = ang_1_O_2;
r_3=len_O_3;
theta_3 = ang_2_O_3 + theta_2;
r_4=len_O_4;
theta_4 = ang_3_O_4 + theta_3;
r_5=len_O_5;
theta_5 = ang_4_O_5 + theta_4;
r_6=len_O_6;
theta_6 = ang_5_O_6 + theta_5;
r_7=len_O_7;
theta_7 = ang_6_O_7 + theta_6;

%gets x,y coordinates from above
point_1=getXYFromRTheta(r_1,theta_1);
point_2=getXYFromRTheta(r_2,theta_2);
point_3=getXYFromRTheta(r_3,theta_3);
point_4=getXYFromRTheta(r_4,theta_4);
point_5=getXYFromRTheta(r_5,theta_5);
point_6=getXYFromRTheta(r_6,theta_6);
point_7=getXYFromRTheta(r_7,theta_7);

points = [point_1;point_2;point_3;point_4;point_5;point_6;point_7];
dists = getPairwise(points);




