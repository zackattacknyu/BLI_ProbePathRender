load 'fixedPointsCoords_testPhantom1.txt'
load 'fixedPointsCoords_testPhantom3.txt'

%virtual distances from scan 1
distsVirtual1 = getPairwise(fixedPointsCoords_testPhantom1);

%virtual distances from scan 3
distsVirtual3 = getPairwise(fixedPointsCoords_testPhantom3);

%{
This is used to verify the virtual distances. They should be nearly
identical so the mean percent error should be quite low.

Indeed the average percent error is 1.65% 
with a standard deviation of 1.69%
%}
percentErrorVirtualDists = getPercentErrorArray(distsVirtual1,distsVirtual3);
meanPercentVirtualDists = mean(percentErrorVirtualDists);
stdPercentVirtualDists = std(percentErrorVirtualDists);

%{
We had a series of measurements and needed to get all the pairwise
distances in the real world. This involved finding (x,y) coordinates for
each of the points. 

In getRealWorldData2, all the (x,y) coordinates were computed using the
two-circle method. 

In getRealWorldData, all the (x,y) coordinates are found using the law of
cosines to obtain polar coordinates for the points
%}
distsReal = getRealWorldData2();
distsReal2 = getRealWorldData();

%{
To ensure that our methods produced accurate distances, we made sure
    both methods produced all the same numbers approximately.

Here we computed the percent errors between the numbers computed and
ensured they were close to zero.

Indeed the mean percent error is 1.73 * 10^-16 
with a standard deviation of 2.0709 * 10^-16 and these numbers are likely
due to floating point errors

%}
percentErrorRealDists = getPercentErrorArray(distsReal,distsReal2);
meanPercentErrorRealDists = mean(percentErrorRealDists);
stdPercentErrorRealDists = std(percentErrorRealDists);

%{
The virtual/real ratio should all be the same. 
Thus we find the mean and standard deviation and the error
is standardDeviation/mean

With ratios1, the error is 1.54%
With ratios2, the error is 1.05%

Below the expected error is computed
%}

ratios1 = getRatiosArray(distsReal,distsVirtual1);
meanRatios1 = mean(ratios1);
stdRatios1 = std(ratios1);
errorRatios1 = stdRatios1/meanRatios1;

ratios3 = getRatiosArray(distsReal,distsVirtual3);
meanRatios3 = mean(ratios3);
stdRatios3 = std(ratios3);
errorRatios3 = stdRatios3/meanRatios3;



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
We then compute the mean and std of those expected errors. 

Using this method, the expected error is 6.53% and the 
    standard deviation is 3.73%
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
expectedError = mean(allErrors);
devFromError = std(allErrors);
