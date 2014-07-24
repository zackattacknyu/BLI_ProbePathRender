
%this is the total number of probe units moved
%       the init scale is the one initially applied
initScale = 0.000009;
t = 3/initScale;

%the probe was moved 94 mm when moving 3 units in virtual world
xRealToProbe = 94/t;
yRealToProbe = 94/t;

%{
This is a second test done on the lola mesh itself

I moved in the y direction from point 1 to 3
I then moved in the x direction from point 1 to 2

%}
initScale2 = 0.00023209;

yChange = abs(-2.108-33.3);
xChange = abs(-0.25-41.2);

totalProbeChangeX = xChange/initScale2;
totalProbeChangeY = yChange/initScale2;

xChangeReal = 46;
yChangeReal = 39;

xConv = xChangeReal/totalProbeChangeX;
yConv = yChangeReal/totalProbeChangeY;

conversions = [xConv,yConv];
newConv = mean(conversions)
stdConv = std(conversions)

%Result is as follows:
%   mean: 0.00025660