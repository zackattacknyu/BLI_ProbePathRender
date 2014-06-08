newStart = [0.16555081 0.39918596];
newDir = [-0.38820514 -0.31145015];

edge12 = -newStart(2)/newDir(2);
edge13 = -newStart(1)/newDir(1);

matrix = [newDir(1) -1;newDir(2) 1];
vect = [-1*newStart(1) 0; 1-newStart(2) 0];
answerVec = matrix\vect;
edge23 = answerVec(1,1);