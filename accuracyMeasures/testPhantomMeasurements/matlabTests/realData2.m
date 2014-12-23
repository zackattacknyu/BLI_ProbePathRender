%gets the real coordinates using circle-circle intersections
%       instead of polar to cartesian conversion
point_O = [0 0];
point_6 = [52.5 0]; %the positive x-axis

startPt = point_O;
otherPt = point_6;
distFromStart = 56;
distFromOther = 46.5;
[x_5,y_5] = circcirc(startPt(1),startPt(2),distFromStart,otherPt(1),otherPt(2),distFromOther);

possPoint1 = [x_5(1) y_5(1)];
possPoint2 = [x_5(2) y_5(2)];
vector_Start_1 = possPoint1-startPt;
vector_Start_2 = possPoint2-startPt;
vector_Start_other = otherPt-startPt;

leftFlag1 = getRightLeft(vector_Start_other,vector_Start_1);
leftFlag2 = getRightLeft(vector_Start_other,vector_Start_2);