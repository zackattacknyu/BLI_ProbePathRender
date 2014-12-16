function [ point ] = getXYFromRTheta( rValue,thetaValue )
%GETXYFROMRTHETA Summary of this function goes here
%   Detailed explanation goes here

point = [rValue*cos(thetaValue) rValue*sin(thetaValue)];
end

