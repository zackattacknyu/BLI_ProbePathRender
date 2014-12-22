function [ percentErrorArray ] = getPercentErrorArray(baseMatrix,actualMatrix)
%GETRATIOSARRAY outputs virtual/real distances
%   Detailed explanation goes here

sizeMatrix = size(baseMatrix);
N = sizeMatrix(1);

percentErrorMatrix = abs(actualMatrix-baseMatrix)./baseMatrix;
index = 1;
numComparisons = (N*N - 3*N)/2; % (N choose 2) - N
percentErrorArray = zeros(1,numComparisons);
for i = 1:N
   for j = i+1:N
      percentErrorArray(index) = percentErrorMatrix(i,j);
      index = index + 1;
   end
end

end

