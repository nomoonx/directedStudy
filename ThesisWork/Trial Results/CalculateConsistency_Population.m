clear; clc;

filenameBase = 'IV - Population/PopulationTrials';
filenameExt = '.txt';

%trialRange = [0, 9];
%numTrials = trialRange(2)-trialRange(1)+1;
numYears = 1;
numDimensions = 1; % Population.
numQueries = 100;







file = strcat(filenameBase,filenameExt);
X = csvread(file);

%AvgQueriesPerTrial = sum(X(:,2)) / numQueries

% ====================================================================
% Calculate mean and std.
% ====================================================================
mean(X(:,2),1)
std(X(:,2),1)
%std(X,2)