clear; clc;

filenameBase = 'NetworkSeparation/DegreesOfSeparation_';
filenameExt = '.txt';

trialRange = [0, 9];
numTrials = trialRange(2)-trialRange(1)+1;
numYears = 1;
numDimensions = 2; % Population, DegreesOfSeparation.
numQueries = 10;


AllData = zeros(numTrials,1);



% ====================================================================
% Collect all data into global arrays.
% ====================================================================
for t = trialRange(1):trialRange(2)
    file = strcat(filenameBase,int2str(t),filenameExt);
    X = csvread(file)

    AvgQueriesPerTrial = sum(X(:,3)) / numQueries


    AllData(t+1) = AvgQueriesPerTrial;



end % end for t (trials)



% ====================================================================
% Aggregate data.
% ====================================================================

% ----------------------------------
% NORMALIZE TO DECIMALS.
% ----------------------------------
% TotalPopulationPerYear = sum(AllData,2);
% 
% % Loop through dimensions.
% for d = 1:numDimensions
%     AllData(:,d,:) = AllData(:,d,:) ./ TotalPopulationPerYear;
% end % end for d (dimensions)

% ----------------------------------
% AVERAGE PER RACE PER YEAR.
% ----------------------------------
% AverageNumbers = zeros(numYears, numDimensions);
% 
% for t = trialRange(1)+1:trialRange(2)+1
%     AverageNumbers(:,:) = AverageNumbers(:,:) + AllData(:,:,t);
% end % end for t (trials)
% 
% AverageNumbers = AverageNumbers ./ numTrials;



% ====================================================================
% Plot data.
% ====================================================================
% uwoPurple = [79,38,131] ./ 255;
% uwoSilver = [128,127,131] ./ 255;
% figure();
% plot(AverageNumbers(1,:), 'Color', uwoPurple, 'LineWidth', 3);     % 1 => 2015.
% hold on;
% %plot(AverageNumbers(11,:), 'Color', uwoSilver, 'LineWidth', 2);     % 2 => 2025.
% plot(raceDistribution, '--k', 'LineWidth', 3);     % ACTUAL DISTRIBUTION.
% hold off;
% set(gca, 'xtick', [1:6]);
% set(gca, 'xticklabel', {'Caucasian';'African American';'Aboriginal';'Indian';'East Asian';'Hispanic'});
% ylim([0 1]);
% %legend('Initial Population (2015)','After 10 Years (2025)','Sample Data Distribution');
% legend('Generated Population','Sample Data Distribution');
% title('Race Distribution in Society');