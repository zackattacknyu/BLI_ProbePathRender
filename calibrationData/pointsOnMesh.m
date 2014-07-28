%{
I calculated the corner points of each of the calibration points.
    The average can then be used as the "center point 
%}

%these are the corners of calibration marker 1
markers1 = [-0.12615025,-1.8313336,-14.786117;
-0.7709585,-1.8037424,-14.630627;
-0.8793626,-2.2411194,-15.014034;
-0.13293967,-2.2036781,-15.158154];
centerPt1 = mean(markers1)

%these are the corners of calibration marker 2