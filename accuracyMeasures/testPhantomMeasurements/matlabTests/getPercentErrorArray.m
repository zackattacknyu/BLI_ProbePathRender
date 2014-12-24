function [ percentErrorArray ] = getPercentErrorArray(baseMatrix,actualMatrix)
%GETRATIOSARRAY outputs virtual/real distances
%   Detailed explanation goes here

percentErrorMatrix = abs(actualMatrix-baseMatrix)./baseMatrix;

percentErrorArray = getArray(percentErrorMatrix);
end

