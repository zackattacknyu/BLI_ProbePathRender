%{

We need to get the conversion from probe unit to virtual unit
    which means probe units -> mm in real world -> virtual unit

This means probeUnit*(mm/probeU)*(virtualU/mm)

From calibProbe:
        mm/probeU = 0.00282
        virtualU/mm = 0.0823
%}

realToProbeFactor = 0.00025660;
virtualToRealFactor = 0.0823;

newScaleFactor = realToProbeFactor*virtualToRealFactor

%new scale Factor:
%       0.000021118