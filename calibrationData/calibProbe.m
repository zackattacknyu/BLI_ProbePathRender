
%y calib points
y1 = [-4.3042345,-2.7086217,-13.825281];
y2 = [-8.575257,-5.8396406,-13.703053];

%x calib points
x1 = [-0.45548248,-2.0219383,-14.924328];
x2 = [0.8652695,-8.470175,-14.686081];

%calib data
xdist = norm(x1-x2)
ydist = norm(y1-y2)

%TODO: The above are too different. 
%   Need to use arc length of recorded
%       paths