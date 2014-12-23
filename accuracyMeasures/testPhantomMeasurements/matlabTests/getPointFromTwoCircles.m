function [ outputPt ] = getPointFromTwoCircles( startPt, distFromStart, otherPt, distFromOther , leftFlag)
%GETPOINTFROMTWOCIRCLES Summary of this function goes here
%   Detailed explanation goes here

[x_poss,y_poss] = circcirc(startPt(1),startPt(2),distFromStart,otherPt(1),otherPt(2),distFromOther);

possPoint1 = [x_poss(1) y_poss(1)];
possPoint2 = [x_poss(2) y_poss(2)];
vector_Start_1 = possPoint1-startPt;
vector_Start_2 = possPoint2-startPt;
vector_Start_other = otherPt-startPt;

leftFlag1 = getRightLeft(vector_Start_other,vector_Start_1);
leftFlag2 = getRightLeft(vector_Start_other,vector_Start_2);

if(leftFlag1==0 || leftFlag2 == 0)
    
    %there was only 1 point, output it
    outputPt = possPoint1;
    
elseif(leftFlag == 1)
    
    %leftFlag = 1 so output positive direction
    if(leftFlag1==1 && leftFlag2==-1)
        outputPt=possPoint1;
    elseif(leftFlag1==-1 && leftFlag2==1)
       outputPt=possPoint2; 
    else
        outputPt = [0 0]; %error in calculation
    end
    
else
    
    %leftFlat=-1 or 0, meaning output negative direction
    if(leftFlag1==1 && leftFlag2==-1)
        outputPt=possPoint2;
    elseif(leftFlag1==-1 && leftFlag2==1)
       outputPt=possPoint1; 
    else
        outputPt = [0 0]; %error in calculation
    end
    
end

end

