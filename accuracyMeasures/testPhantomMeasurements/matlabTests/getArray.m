function [ outputArray ] = getArray( inputMatrix )
%GETARRAY Summary of this function goes here
%   Detailed explanation goes here

sizeMatrix = size(inputMatrix);
N = sizeMatrix(1);
index = 1;
numComparisons = (N*N - 3*N)/2; % (N choose 2) - N
outputArray = zeros(1,numComparisons);
for i = 1:N
   for j = i+1:N
      outputArray(index) = inputMatrix(i,j);
      index = index + 1;
   end
end

end

