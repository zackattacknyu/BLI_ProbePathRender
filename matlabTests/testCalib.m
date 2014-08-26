%Probe Coordinate System Points:
initPt=[-3.6939867,-3.0091875,-1.4006405];

probe1=[-7.1960483,0.8735572,-1.6233621];
probe2=[-5.0879474,2.9170935,-1.7947036];
probe3=[-1.4111998,-0.77802604,-1.59815];

virtual1=[-7.9311867,-0.6427157,-4.3220825];
virtual2=[-5.5991817,0.874146,-6.53154];
virtual3=[-1.5390437,-1.4859414,-3.586031];

probe1adj = probe1-initPt;
probe2adj = probe2-initPt;
probe3adj = probe3-initPt;

virtual1adj = virtual1-initPt;
virtual2adj = virtual2-initPt;
virtual3adj = virtual3-initPt;

probeMat = [probe1adj;probe2adj;probe3adj]';
virtualMat = [virtual1adj;virtual2adj;virtual3adj]';

calibMat = virtualMat/probeMat;

probe1unit = probe1adj/norm(probe1adj);
probe2unit = probe2adj/norm(probe2adj);
probe3unit = probe3adj/norm(probe3adj);

virtual1unit = virtual1adj/norm(virtual1adj);
virtual2unit = virtual2adj/norm(virtual2adj);
virtual3unit = virtual3adj/norm(virtual3adj);

probeMatUnit = [probe1unit;probe2unit;probe3unit]';
virtualMatUnit = [virtual1unit;virtual2unit;virtual3unit]';

calibMatUnit = virtualMatUnit/probeMatUnit;