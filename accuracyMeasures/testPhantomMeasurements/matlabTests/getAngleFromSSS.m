function [ angleC ] = getAngleFromSSS( sideA,sideB,sideC )
%GETANGLEFROMSSS gets angle of triangle from Side-Side-Side using law of
%       cosines
%   sideA, sideB are the sides adjacent to angle
%   sideC is side opposite angle

cosC = (sideA*sideA + sideB*sideB - sideC*sideC)/(2*sideA*sideB);
angleC = acos(cosC);

end

