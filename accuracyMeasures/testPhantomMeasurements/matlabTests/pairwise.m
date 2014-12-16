v1 = [0 2 3];
v2 = [3 4 5];
v3 = [5 6 7];
v4 = [7 8 9];

%n by 3 matrix containing point coordinates
points = [v1;v2;v3;v4];

dists = getPairwise(points);