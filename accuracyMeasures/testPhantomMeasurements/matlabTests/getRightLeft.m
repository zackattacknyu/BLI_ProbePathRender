function [ leftFlag ] = getRightLeft( baseVector,inputVector )
%GETRIGHTLEFT Given a base vector, tells where new vector is right or left
%                   turn from that base vector.
%                   Input must be two vectors
%   This finds the cross product of the two vectors in the x,y,z space
%           where z=0 for both vectors
%   If the z coordinate of the cross product is positive, then leftFlag=1.
%           Otherwise, leftFlat=0

baseVecXYZ = [baseVector 0];
inputVecXYZ = [inputVector 0];

crossProd = cross(baseVecXYZ,inputVecXYZ);

if(crossProd(3)>0)
   leftFlag = 1;
elseif(crossProd(3) < 0)
    leftFlag = -1;
else
    leftFlag=0;
end


end

