%{
This is the data for the Lola Mesh and its markers:

Real-world distances:
marker 1-2: 46 mm
marker 1-3: 39 mm
marker 1-4: 56 mm
marker 2-3: 62 mm
marker 2-4: 37 mm
marker 3-4: 46 mm

Virtual Coordinates:
Marker 1: (-0.45941067,-2.0322227,-14.933813)
Marker 2: (-4.1024504,-2.726573,-13.849037)
Marker 3: (-0.43226802,-4.228998,-17.187346)
Marker 4: (-4.0149,-4.7278976,-16.069866)

Conversion Ratio (RealLength/VirtualLength):
    Mean: 12.1569
    Standard Deviation: 0.2

Conversion Ratio 2 (Virtual Length/RealLength):
    Mean: 0.0823
    Standard Deviation: 0.0014

Code that achieved the result is below
%}


%the marker coordinates
mark1=[-0.45941067,-2.0322227,-14.933813];
mark2=[-4.1024504,-2.726573,-13.849037];
mark3=[-0.43226802,-4.228998,-17.187346];
mark4=[-4.0149,-4.7278976,-16.069866];

%length of vectors between markers
vec12Len = norm(mark1-mark2);
vec13Len = norm(mark1-mark3);
vec14Len = norm(mark1-mark4);
vec23Len = norm(mark2-mark3);
vec24Len = norm(mark2-mark4);
vec34Len = norm(mark3-mark4);

%length of vectors between markers in real world. units are mm
vec12real = 46;
vec13real = 39;
vec14real = 56;
vec23real = 62;
vec24real = 37;
vec34real = 46;

%conversion ratio of real to virtual coordinates for each of the vectors
%{
vec12ratio = vec12real/vec12Len;
vec13ratio = vec13real/vec13Len;
vec14ratio = vec14real/vec14Len;
vec23ratio = vec23real/vec23Len;
vec24ratio = vec24real/vec24Len;
vec34ratio = vec34real/vec34Len;
%}
vec12ratio = vec12Len/vec12real;
vec13ratio = vec13Len/vec13real;
vec14ratio = vec14Len/vec14real;
vec23ratio = vec23Len/vec23real;
vec24ratio = vec24Len/vec24real;
vec34ratio = vec34Len/vec34real;

%puts the data into a vector to get the mean and std
ratios = [vec12ratio,vec13ratio,vec14ratio,vec23ratio,vec24ratio,vec34ratio];
meanRatio = mean(ratios)
stdRatio = std(ratios)



%{
This is the data for the Ball Mesh and its markers:

Real-world distances:
marker 1-2: 75.5 mm
marker 1-3: 94 mm
marker 1-4: 122 mm
marker 2-3: 111 mm
marker 2-4: 92 mm
marker 3-4: 65 mm

Virtual Coordinates:
Marker 1: (-1.6921587,-3.2570353,22.765057)
Marker 2: (1.0623746,-2.9609601,23.383495)
Marker 3: (-1.928752,0.4531126,22.845892)
Marker 4: (0.5929389,0.97696793,23.470764)

Conversion Ratio (RealLength/VirtualLength):
    Mean: 24.8281
    Standard Deviation: 1.1370
Code that achieved the result is below
%}

%{
%the marker coordinates
mark1=[-1.6921587,-3.2570353,22.765057];
mark2=[1.0623746,-2.9609601,23.383495];
mark3=[-1.928752,0.4531126,22.845892];
mark4=[0.5929389,0.97696793,23.470764];

%length of vectors between markers
vec12Len = norm(mark1-mark2);
vec13Len = norm(mark1-mark3);
vec14Len = norm(mark1-mark4);
vec23Len = norm(mark2-mark3);
vec24Len = norm(mark2-mark4);
vec34Len = norm(mark3-mark4);

%length of vectors between markers in real world. units are mm
vec12real = 75.5;
vec13real = 94;
vec14real = 122;
vec23real = 111;
vec24real = 92;
vec34real = 65;

%conversion ratio of real to virtual coordinates for each of the vectors
vec12ratio = vec12real/vec12Len;
vec13ratio = vec13real/vec13Len;
vec14ratio = vec14real/vec14Len;
vec23ratio = vec23real/vec23Len;
vec24ratio = vec24real/vec24Len;
vec34ratio = vec34real/vec34Len;

%puts the data into a vector to get the mean and std
ratios = [vec12ratio,vec13ratio,vec14ratio,vec23ratio,vec24ratio,vec34ratio];
meanRatio = mean(ratios)
stdRatio = std(ratios)
%}