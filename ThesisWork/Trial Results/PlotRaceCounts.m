clear; clc;

filenameBase = 'Race/Results_Race_50YearSim_';
filenameExt = '.txt';

trialRange = [0, 9];
numTrials = trialRange(2)-trialRange(1)+1;
numYears = 11;
numDimensions = 6; % Races.

AllYears = zeros(numYears,1);
AllData = zeros(numYears,numDimensions,numTrials);

% ====================================================================
% Collect all data into global arrays.
% ====================================================================
for t = trialRange(1):trialRange(2)
    file = strcat(filenameBase,int2str(t),filenameExt);
    X = csvread(file);

    % Loop through years.
    for y = 1:numYears

        % Only need to store the years once.
        AllYears(y) = X(y,1);

        % Store the main data in the global array.
        for d = 1:numDimensions
            AllData(y,d,t+1) = X(y,d+1);
        end % end for d (dimensions)

    end % end for y (years)

end % end for t (trials)


% ====================================================================
% Aggregate data.
% ====================================================================

% ----------------------------------
% NORMALIZE TO DECIMALS.
% ----------------------------------
TotalPopulationPerYear = sum(AllData,2);

% Loop through dimensions.
for d = 1:numDimensions
    AllData(:,d,:) = AllData(:,d,:) ./ TotalPopulationPerYear;
end % end for d (dimensions)

% ----------------------------------
% AVERAGE PER RACE PER YEAR.
% ----------------------------------
AverageNumbers = zeros(numYears, numDimensions);

for t = trialRange(1)+1:trialRange(2)+1
    AverageNumbers(:,:) = AverageNumbers(:,:) + AllData(:,:,t);
end % end for t (trials)

AverageNumbers = AverageNumbers ./ numTrials;

% ====================================================================
% Get actual distribution.
% ====================================================================
raceDistribution = [0.40,0.15,0.05,0.10,0.20,0.10];


% ====================================================================
% Plot data.
% ====================================================================
% LINE PLOT.
% uwoPurple = [79,38,131] ./ 255;
% uwoSilver = [128,127,131] ./ 255;
% figure();
% %plot(AverageNumbers(1,:), 'Color', uwoPurple, 'LineWidth', 3);     % 1 => 2015.
% hold on;
% %plot(raceDistribution, '--k', 'LineWidth', 3);     % ACTUAL DISTRIBUTION.
% hold off;
% set(gca, 'xtick', [1:6]);
% set(gca, 'xticklabel', {'Caucasian';'African American';'Aboriginal';'Indian';'East Asian';'Hispanic'});
% ylim([0 1]);
% legend('Generated Population','Sample Data Distribution');
% title('Race Distribution in Society');
% xlabel('Race');
% ylabel('Percentage in Society');



% BAR GRAPH.

%AllData = [AverageNumbers(1,:), raceDistribution]
CombinedData(:,1) = AverageNumbers(1,:) * 100; % Multiply by 100 to show as percentage.
CombinedData(:,2) = raceDistribution * 100; % Multiply by 100 to show as percentage.

uwoPurple = [79,38,131] ./ 255;
uwoSilver = [128,127,131] ./ 255;
figure();
b = bar(CombinedData);     % 1 => 2015.
set(b(1), 'FaceColor', uwoPurple);
set(b(2), 'FaceColor', uwoSilver);

%hold on;
%bar(raceDistribution);     % ACTUAL DISTRIBUTION.
%hold off;
set(gca, 'xtick', 1:6);
set(gca, 'xticklabel', {'Caucasian';'African American';'Aboriginal';'Indian';'East Asian';'Hispanic'});
ylim([0 100]);
%legend('Generated Population','Sample Data Distribution');
title('Race Distribution in Society');
xlabel('Race');
ylabel('Percentage in Society');

AverageNumbers(1,:)