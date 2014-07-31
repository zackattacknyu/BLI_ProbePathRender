%the following paths are all on the Lola Mesh

%{

%when rotating the path from top left to top right
quat1 = [0.25984886, -0.31837386, 0.5798652, 0.70347196];

%when rotating the top right to bottom right path
quat2 = [0.25196582, -0.41494852, 0.4853707, 0.7271494];

%when rotating the bottom right to bottom left
quat3 = [0.5005908, -0.4062252, 0.36785883, 0.6701268];

%when rotating the bottom left to top left
quat4 = [0.3130682, -0.5322426, 0.4044622, 0.67462325];

quat12 = [quat1;quat2];
meanQuat = mean(quat12);

%Result is as follows:
%   0.2559   -0.3667    0.5326    0.7153

%}

%These are the quaternions after using precise points in the virtual
%       world and precise positions of the probe
%Same descriptions for each variable
quat1 = [-0.42791983, -0.55609095, 0.6624931, -0.26220307];
quat2 = [0.015292523, -0.11193628, 0.8141645, 0.5695369];
quat3 = [0.013024371, 0.7474542, -0.56122136, -0.3552089];
quat4 = [-0.07789981, -0.24101731, 0.43595392, 0.86358917];