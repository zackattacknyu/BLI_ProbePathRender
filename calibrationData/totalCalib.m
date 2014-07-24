%{

We need to get the conversion from probe unit to virtual unit
    which means probe units -> mm in real world -> virtual unit

This means probeUnit*(mm/probeU)*(virtualU/mm)

From calibProbe:
        mm/probeU = 0.00282
        virtualU/mm = 0.0823 (lola model)
        virtualU/mm = 0.0418 (ball Model)
%}

realToProbeFactor = 0.00025660;
%virtualToRealFactor = 0.0823;
virtualToRealFactor = 0.0418;

newScaleFactor = realToProbeFactor*virtualToRealFactor

%new scale Factor:
%       0.000021118 (lola Model)
%       0.000010726 (ball Model)