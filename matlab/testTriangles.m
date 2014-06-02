%Vertices of a Triangle
vert1 = [-0.18751788, -2.2151814, -15.160053];
vert2 = [-0.3124779, -2.0962791, -15.027809];
vert3 = [-0.1875179, -2.1149406, -15.06823];

%Point that is supposedly inside triangle. This needs to be tested.
testPoint = [-0.20191753, -1.9489088, -15.11458];

triangle = [vert1;vert2;vert3];
hold on
plot3(triangle(:,1),triangle(:,2),triangle(:,3));
plot3(testPoint(1),testPoint(2),testPoint(3),'r.');
hold off
