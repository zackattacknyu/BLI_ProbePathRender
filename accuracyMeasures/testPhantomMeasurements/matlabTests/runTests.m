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

%{
Here is the logic used to get the expected error:
    the margin of error for the virtual measurement is x
    the margin of error for the real world measurement is y
    
We take each of the measurements, compute the ratio, and compute the 
    max percent error for the ratio with that measurement
Let A be the virtual measurement, B be the real measurement, R=A/B
The min possible ratio given our measurement and margin of error is MIN=(A-x)/(B+y)
The max possible ratio given our measurement and margin of error is MAX=(A+x)/(B-y)
To get the max percent error P we then compute max( (R-MIN)/R , (MAX-R)/R )

We compute P for all the measurements and find the mean of P
    to get the expected error. 
We then compute the median of those measurements. 
Using the median we can eliminate any values that could be due to noise

%}
errorReal = 2; % there is a 2 mm error for measuring
errorVirtual = 0.12; %rough diameter in virtual units for the circle
distsRealArray = getArray(distsReal);
distsVirtualArray = getArray(distsVirtual1);
distsRealErrored = distsRealArray - errorReal;
distsVirtualErrored = distsVirtualArray + errorVirtual;
ratiosErrored = distsVirtualErrored./distsRealErrored;
ratiosFound = distsVirtualArray./distsRealArray;
percentErrors = abs(ratiosFound-ratiosErrored)./ratiosFound;
distsRealErrored2 = distsRealArray + errorReal;
distsVirtualErrored2 = distsVirtualArray - errorVirtual;
ratiosErrored2 = distsVirtualErrored2./distsRealErrored2;
percentErrors2 = abs(ratiosFound-ratiosErrored2)./ratiosFound;
allErrors = [percentErrors percentErrors2];
expectedError = median(allErrors);
