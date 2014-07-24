%the following paths are all on the Lola Mesh

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

%other quat
%0.2654651f, -0.32326236f, 0.61058617f, 0.67246884f

meanQuat2 = [0.2654651, -0.32326236, 0.61058617, 0.67246884];

mult1 = quatmultiply(meanQuat,meanQuat2)
mult2 = quatmultiply(meanQuat2,meanQuat)