% CW 2D Visualization Program
% Kyle Cutler 7/31/2014

%% Load in Data
clear
cd C:\Users\BLI\Desktop\Downloads; % path folder
load cw_tracker2.mat; % import data
data = data3; % renaming probe output
peaks = peaks3; % renaming fft output
colordata = [peaks(:,1),peaks(:,3)]; % fft data to color output
%% Plot raw x,y path with color
% colordata(:,1) = 11k peak
% colordata(:,2) = 20k peak

% plot integrated x,y output with color from peak data
% scatter(cumsum(data(:,2)),cumsum(data(:,3)),150,-colordata(:,1)), colorbar, axis square

%% Correct for rotation changes of probe
x_raw = data(:,2); % raw x data
y_raw = data(:,3); % raw y data
yaw = deg2rad(data(:,4)); % convert yaw to rads
yaw2 = yaw - yaw(1); % offset angle to zero
x_location = 0; % initial x location
y_location = 0; % initial y location
x_location2 = 0; % initial x with no offset
y_location2 = 0; %initial y with no offset

% No angle offset integration
for i=1:length(x_raw)
    % Calculate rotation matrix
    correction_factor = [ cos(yaw(i)) -sin(yaw(i)); sin(yaw(i)) cos(yaw(i))];
    % Matrix for raw x and y
    path_matrix = [x_raw(i),y_raw(i)];
    % Multiply x,y matrix and rotation matrix
    path_temp = path_matrix*correction_factor;
    % Add temp path to previous location
    corrected_path(i,:) = [x_location + path_temp(1) , y_location + path_temp(2)];
    % Update Location of x and y
    x_location = corrected_path(i,1);
    y_location = corrected_path(i,2);
end

% Angle offset integration
for i=1:length(x_raw)
    % Calculate rotation matrix 
    correction_factor = [ cos(yaw2(i)) -sin(yaw2(i)); sin(yaw2(i)) cos(yaw2(i))];
    % Matrix for raw x and y
    path_matrix = [x_raw(i),y_raw(i)];
    % Multiply x,y matrix and rotation matrix
    path_temp = path_matrix*correction_factor;
    % Add temp path to previous location
    corrected_path2(i,:) = [x_location2 + path_temp(1) , y_location2 + path_temp(2)];
    % Update Location of x and y 
    x_location2 = corrected_path2(i,1);
    y_location2 = corrected_path2(i,2);
end

%% Plotting 
markerSize = 150;
% axis([xmin xmax ymin ymax]
newaxis = ([-10000 10000 -10000 10000]);
% Plot x y raw path
close all
scatter(cumsum(data(:,2)),cumsum(data(:,3)),markerSize,-colordata(:,1)), colorbar;
% axis(newaxis); 
axis equal
title('2D Raw X and Y Output');

% Plot x y angle corrected path
figure;
scatter(corrected_path(:,1),corrected_path(:,2),markerSize,-colordata(:,1)), colorbar;
title('2D Path Angle Corrected');
% axis(newaxis);
axis equal

% Plot x y angle corrected path with offset
figure;
scatter(corrected_path2(:,1),corrected_path2(:,2),markerSize,-colordata(:,1)), colorbar;
title('2D Path Angle Corrected Offset');
% axis(newaxis); 
axis equal

%% Plot overlay 
% 
% close all
% hold on
% title('2D Path Angle Corrected')
% xlabel('X Axis in pixels')
% ylabel('Y Axis in pixels')
% scatter(cumsum(data(:,2)),cumsum(data(:,3)),markerSize,-colordata(:,1)), colorbar;
% scatter(corrected_path2(:,1),corrected_path2(:,2),markerSize, -colordata(:,1)), colorbar;
% legend('original path','corrected path')
% % axis(newaxis); 
% axis equal
