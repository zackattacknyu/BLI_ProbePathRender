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
%}

mark1=[-0.45941067,-2.0322227,-14.933813];
mark2=[-4.1024504,-2.726573,-13.849037];
mark3=[-0.43226802,-4.228998,-17.187346];
mark4=[-4.0149,-4.7278976,-16.069866];
vec12Len = norm(mark1-mark2);
vec13Len = norm(mark1-mark3);
vec14Len = norm(mark1-mark4);
vec23Len = norm(mark2-mark3);
vec24Len = norm(mark2-mark4);
vec34Len = norm(mark3-mark4);


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

%}