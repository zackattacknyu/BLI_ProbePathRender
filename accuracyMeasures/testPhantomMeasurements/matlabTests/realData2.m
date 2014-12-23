%gets the real coordinates using circle-circle intersections
%       instead of polar to cartesian conversion
point_O = [0 0];
point_6 = [52.5 0]; %the positive x-axis

dist_O_5 = 56;
dist_5_6 = 46.5;
point_5 = getPointFromTwoCircles(point_O,dist_O_5,point_6,dist_5_6,1);

