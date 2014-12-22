load 'fixedPointsCoords_testPhantom1.txt'

distsVirtual = getPairwise(fixedPointsCoords_testPhantom1);

distsReal = getRealWorldData();

ratios = distsReal./distsVirtual