probeNormal=[0.07973032, 0.8208056, 0.56561625];
probeX=[-0.7769739, 0.4066394, -0.48057938];
probeY=[-0.624464, -0.40115222, 0.67016584];

currentCoord = [probeNormal' probeX' probeY'];

probeNormalNeg = -probeNormal;

desiredCoord = [probeNormalNeg' probeX' probeY'];

conversionMatrix = currentCoord/desiredCoord;