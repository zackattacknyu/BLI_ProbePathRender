clear;
data = importdata('gridtest1.txt');
time = data(2:end,1);
x = -data(2:end,2);
xsum = cumsum(x);
y = data(2:end,3);
ysum = cumsum(y);
button = data(2:end,7);

a = button<1;
%%                       

figure;
scatter(xsum,ysum,'filled')
hold on
scatter(xsum(a),ysum(a),'LineWidth',10)
axis('equal')
%%
VtoMMscaleFactor = 0.013484741;
originGrid = [0 0 0 0 1 1 1 1; 0 -1 -2 -3 -3 -2 -1 0];
gridScale = 19;
figure;
scatter(originGrid(1,:)*gridScale,originGrid(2,:)*gridScale,'b','LineWidth',10);
hold on
scatter(xsum(a)*VtoMMscaleFactor,ysum(a)*VtoMMscaleFactor,'LineWidth',10)
scatter(xsum*VtoMMscaleFactor,ysum*VtoMMscaleFactor,'filled')
axis('equal')