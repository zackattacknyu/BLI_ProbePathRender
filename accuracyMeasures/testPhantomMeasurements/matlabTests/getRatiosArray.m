function [ ratioArray ] = getRatiosArray(realCoords,virtualCoords)
%GETRATIOSARRAY outputs virtual/real distances
%   Detailed explanation goes here

sizeMatrix = size(realCoords);
N = sizeMatrix(1);

ratioMatrix = virtualCoords./realCoords;
index = 1;
numComparisons = (N*N - 3*N)/2; % (N choose 2) - N
ratioArray = zeros(1,numComparisons);
for i = 1:N
   for j = i+1:N
      ratioArray(index) = ratioMatrix(i,j);
      index = index + 1;
   end
end

end

