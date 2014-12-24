load 'fixedPointsCoords_testPhantom1.txt'
load 'fixedPointsCoords_testPhantom3.txt'

distsVirtual1 = getPairwise(fixedPointsCoords_testPhantom1);
distsVirtual3 = getPairwise(fixedPointsCoords_testPhantom3);

distsReal = getRealWorldData2();

ratios1 = getRatiosArray(distsReal,distsVirtual1);
meanRatios1 = mean(ratios1);
stdRatios1 = std(ratios1);

ratios3 = getRatiosArray(distsReal,distsVirtual3);
meanRatios3 = mean(ratios3);
stdRatios3 = std(ratios3);

percentErrorVirtualDists = getPercentErrorArray(distsVirtual1,distsVirtual3);
meanPercentVirtualDists = mean(percentErrorVirtualDists);
stdPercentVirtualDists = std(percentErrorVirtualDists);

distsReal2 = getRealWorldData();
percentErrorRealDists = getPercentErrorArray(distsReal,distsReal2);
meanPercentErrorRealDists = mean(percentErrorRealDists);
stdPercentErrorRealDists = std(percentErrorRealDists);

errorReal = 2; % there is a 2 mm error for measuring
errorVirtual = 0.2; %GUESS FOR NOW. TODO: FIND MORE PRECISE ERROR LATER
distsRealArray = getArray(distsReal);
distsVirtualArray = getArray(distsVirtual1);
distsRealErrored = distsRealArray - errorReal;
distsVirtualErrored = distsVirtualArray + errorVirtual;
ratiosErrored = distsVirtualErrored./distsRealErrored;
ratiosFound = distsVirtualArray./distsRealArray;
percentErrors = abs(ratiosFound-ratiosErrored)./ratiosFound;
maxError = max(percentErrors);
