vertex1 = [7.96118 -11.102819 -11.102819];
vertex2 = [7.96118 -11.102819 11.102819];
vertex3 = [-14.24446 -11.102819 -11.102839];

vertices = zeros(3,3);
vertices(1,:) = vertex1;
vertices(2,:) = vertex2;
vertices(3,:) = vertex3;

initPt = [-0.9029999 -11.102818 -7.426666];
initEndPt = [6.012951 -9.872749 -16.047003];

points = zeros(2,3);
points(1,:) = initPt;
points(2,:) = initEndPt;

hold on;
plot3(points(:,1),points(:,2),points(:,3),'r.');
plot3(vertices(:,1),vertices(:,2),vertices(:,3),'g+');
hold off;