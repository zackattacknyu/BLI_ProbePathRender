function [ distances ] = getPairwise( points )
%GETPAIRWISE Summary of this function goes here
%   points is n by m matrix 
%       there are n points of m dimensions each
%       distances is the pairwise Euclidean distances between them

sizePts = size(points);
n = sizePts(1);

distances = zeros(n,n);

for i = 1:n
   for j = 1:n
       if i ~= j
          distances(i,j) = norm( points(i,:) - points(j,:) ) ;
       end
   end
end


end

