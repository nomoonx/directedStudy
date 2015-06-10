clear; clc;

filenameBase1 = 'Population from Child Distr/Results_Popn_OneChildPolicy_';
filenameBase2 = 'Population from Child Distr/Results_Popn_RegDistr_';
filenameExt = '.txt';

trialRange = [0, 9];
numTrials = trialRange(2)-trialRange(1)+1;
numYears = 31;
numDimensions = 1; % Population.

AllYears = zeros(numYears,1);
AllData1 = zeros(numYears,numDimensions,numTrials);
AllData2 = zeros(numYears,numDimensions,numTrials);

% ====================================================================
% Collect all data into global arrays.
% ====================================================================
for t = trialRange(1):trialRange(2)
    file = strcat(filenameBase1,int2str(t),filenameExt);    % Read 1st file.
    X = csvread(file);
    file = strcat(filenameBase2,int2str(t),filenameExt);    % Read 2nd file.
    Y = csvread(file);

    % Loop through years.
    for y = 1:numYears

        % Only need to store the years once.
        AllYears(y) = X(y,1);

        % Store the main data in the global array.
        for d = 1:numDimensions
            AllData1(y,d,t+1) = X(y,d+1);   % X is from 1st file.
            AllData2(y,d,t+1) = Y(y,d+1);   % Y is from 2nd file.
        end % end for d (dimensions)

    end % end for y (years)

end % end for t (trials)


% ====================================================================
% Aggregate data.
% ====================================================================

% ----------------------------------
% AVERAGE PER RACE PER YEAR.
% ----------------------------------
AverageNumbers1 = zeros(numYears, numDimensions);
AverageNumbers2 = zeros(numYears, numDimensions);

for t = trialRange(1)+1:trialRange(2)+1
    AverageNumbers1(:,:) = AverageNumbers1(:,:) + AllData1(:,:,t);
    AverageNumbers2(:,:) = AverageNumbers2(:,:) + AllData2(:,:,t);
end % end for t (trials)

AverageNumbers1 = AverageNumbers1 ./ numTrials
AverageNumbers2 = AverageNumbers2 ./ numTrials

% ====================================================================
% Plot data.
% ====================================================================
uwoPurple = [79,38,131] ./ 255;
uwoSilver = [128,127,131] ./ 255;
figure();
plot(AverageNumbers1, 'Color', uwoPurple, 'LineWidth', 3);     % 1 => One-Child Policy.
hold on;
plot(AverageNumbers2, 'Color', uwoSilver, 'LineWidth', 3);     % 2 => N(2,1.4) clipped to [1,5].
hold off;
%set(gca, 'xtick', [1:6]);
%set(gca, 'xticklabel', {'Caucasian';'African American';'Aboriginal';'Indian';'East Asian';'Hispanic'});
%ylim([0 1]);
xlim([1 31])
set(gca, 'xtick', [1,11,21,31]);
set(gca, 'xticklabel', {'2015';'2025';'2035';'2045'});
legend('One-Child Policy', 'Regular  Distribution','Location','NorthWest');
title('Society Population from Childbirth Distributions');
xlabel('Year');
ylabel('Population');