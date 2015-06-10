clear; clc;

filenameBase = 'IV - Locals/PeopleInSocietyTrials';
filenameExt = '.txt';

%trialRange = [0, 9];
%numTrials = trialRange(2)-trialRange(1)+1;
numYears = 1;
numDimensions = 2; % Population.
numQueries = 100;







file = strcat(filenameBase,filenameExt);
X = csvread(file);


percentOfPopulationInSociety = X(:,3) ./ X(:,2);

%AvgQueriesPerTrial = sum(X(:,2)) / numQueries

% ====================================================================
% Calculate mean and std.
% ====================================================================
mean(percentOfPopulationInSociety)
std(percentOfPopulationInSociety)

%mean(X(:,3),1)
%std(X(:,3),1)
