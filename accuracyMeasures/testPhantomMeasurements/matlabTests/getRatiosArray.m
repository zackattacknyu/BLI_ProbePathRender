function [ ratioArray ] = getRatiosArray(realCoords,virtualCoords)
%GETRATIOSARRAY outputs virtual/real distances
%   Detailed explanation goes here

ratioMatrix = virtualCoords./realCoords;

ratioArray = getArray(ratioMatrix);

end

