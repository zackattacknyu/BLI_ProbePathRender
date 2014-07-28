%{
I calculated the corner points of each of the calibration points.
    The average can then be used as an approximation to the center point
%}

%these are the corners of calibration marker 1
markers1 = [-0.12615025,-1.8313336,-14.786117;
-0.7709585,-1.8037424,-14.630627;
-0.8793626,-2.2411194,-15.014034;
-0.13293967,-2.2036781,-15.158154];
centerPt1 = mean(markers1);

%these are the corners of calibration marker 2
markers2 = [-3.7385745,-2.891327,-14.198236;
-3.7258704,-2.3863523,-13.803908;
-4.487812,-2.5324132,-13.479865;
-4.5428977,-3.1304235,-13.88509];
centerPt2 = mean(markers2);

%these are the corners of calibration marker 3
markers3 = [-0.15281808,-4.4425535,-17.407621;
-0.82023793,-4.393738,-17.326061;
-0.7550099,-3.9451566,-16.81427;
-0.075701356,-3.9918041,-16.946493];
centerPt3 = mean(markers3);

%these are the corners of calibration marker 4
markers4 = [-3.6409163,-4.458033,-15.96539;
-4.2430634,-4.6502686,-15.60878;
-4.4113774,-5.029074,-16.09138;
-3.8218813,-4.8107505,-16.460886];
centerPt4 = mean(markers4);

%{

Results:
centerPt1 = [-0.4774,-2.0200,-14.8972]
centerPt2 = [-4.1238,-2.7351,-13.8418]
centerPt3 = [-0.4509,-4.1933,-17.1236]
centerPt4 = [-4.0293,-4.7370,-16.0316]

%}

%{
These are the proposed center points achieved by clicking
        on the point in the mesh that seemed
        like the center.
%}
marker1Mesh = [-0.51208466, -2.015924, -14.905227];
marker2Mesh = [-4.103265, -2.7216837, -13.844656];
marker3Mesh = [-0.42267808, -4.185834, -17.14265];
marker4Mesh = [-4.0077333, -4.704981, -16.033361];

mark1Diff = norm(centerPt1-marker1Mesh);
mark2Diff = norm(centerPt2-marker2Mesh);
mark3Diff = norm(centerPt3-marker3Mesh);
mark4Diff = norm(centerPt4-marker4Mesh);

%{

Results:

mark1Diff = 0.0359
mark2Diff = 0.0247
mark3Diff = 0.0349
mark4Diff = 0.0387

Due to these results, the proposed center points
        will be good for use with the application

%}
