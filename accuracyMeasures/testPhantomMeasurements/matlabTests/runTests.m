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

percentError13 = getPercentErrorArray(distsVirtual1,distsVirtual3);
meanPercentError13 = mean(percentError13);
stdPercentError13 = std(percentError13);

percentError31 = getPercentErrorArray(distsVirtual3,distsVirtual1);
meanPercentError31 = mean(percentError31);
stdPercentError31 = std(percentError31);

distsReal2 = getRealWorldData();
percentErrorDists = getPercentErrorArray(distsReal,distsReal2);
meanPercentErrorDists = mean(percentErrorDists);
stdPercentErrorDists = std(percentErrorDists);