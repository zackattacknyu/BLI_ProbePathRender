%{

We need to get the conversion from probe unit to virtual unit
    which means probe units -> mm in real world -> virtual unit

This means probeUnit*(mm/probeU)*(virtualU/mm)

From calibProbe:
        mm/probeU = 3.4815*10^6
        virtualU/mm = 0.0823
%}

realToProbeFactor = 3481500;
virtualToRealFactor = 0.0823;

newScaleFactor = realToProbeFactor*virtualToRealFactor

