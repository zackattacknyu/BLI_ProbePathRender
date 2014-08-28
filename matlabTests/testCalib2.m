probeNormal=[-0.07499722;-0.8226994;-0.5635085];
probeX=[0.5684231;-0.49957043;0.6537013];
probeY=[-0.81931156;-0.27128536;0.5051075];

probeXneg = -probeX;

actualCoords = [probeX probeY probeNormal];
desiredCoords = [probeXneg probeY probeNormal];

conversionMatrix = actualCoords/desiredCoords;

