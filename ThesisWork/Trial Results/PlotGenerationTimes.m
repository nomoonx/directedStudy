clear; clc;

filenameBase1 = 'Times (larger populations)/TimesToCreateOrLoad_A_';
filenameBase2 = 'Times (larger populations)/TimesToCreateOrLoad_B_';
filenameBase3 = 'Times (larger populations)/TimesToCreateOrLoad_C_';
filenameBase4 = 'Times (larger populations)/TimesToCreateOrLoad_D_';
filenameBase5 = 'Times (larger populations)/TimesToCreateOrLoad_E_';
filenameBase6 = 'Times (larger populations)/TimesToCreateOrLoad_F_';
filenameBase7 = 'Times (larger populations)/TimesToCreateOrLoad_G_';
filenameBase8 = 'Times (larger populations)/TimesToCreateOrLoad_H_';
%filenameBase8 = 'Times/TimesToCreateOrLoad_H_';
%filenameBase9 = 'Times/TimesToCreateOrLoad_I_';
%allFiles = {filenameBase1, filenameBase2, filenameBase3, filenameBase4, filenameBase5, filenameBase6, filenameBase7, filenameBase8, filenameBase9};
allFiles = {filenameBase1, filenameBase2, filenameBase3, filenameBase4, filenameBase5, filenameBase6, filenameBase7, filenameBase8};

filenameExt = '.txt';

trialRange = [0, 9];
numTrials = trialRange(2)-trialRange(1)+1;
numYears = 1;
numDimensions = 3; % Population, TimeToGenerate, TimeToLoad
numGroups = size(allFiles,2); % Put all population-sets of trials into one large array.

AllYears = zeros(numYears,1);

AllDataGen = zeros(numYears, 2, (numTrials * numGroups)); % Multiply numTrials by numGroups since we want all in one big array.
AllDataLoad = zeros(numYears, 2, (numTrials * numGroups)); % Multiply numTrials by numGroups since we want all in one big array.

% ====================================================================
% Collect all data into global arrays.
% ====================================================================
trialEntry = 1;
for t = trialRange(1):trialRange(2)
    
    for f = 1:numGroups
        
        file = strcat(allFiles{f},int2str(t),filenameExt);    % Read 1st file.
        X = csvread(file);
    

        % Loop through years.
        for y = 1:numYears

            % Generated society times.
            AllDataGen(y,1,trialEntry) = X(y,2);   % X is from 1st file.
            AllDataGen(y,2,trialEntry) = X(y,3);   % X is from 1st file.
            
            % Loaded society times.
            AllDataLoad(y,1,trialEntry) = X(y,2);   % X is from 1st file.
            AllDataLoad(y,2,trialEntry) = X(y,4);   % X is from 1st file.


        end % end for y (years)
        
        trialEntry = trialEntry + 1; % Increment this trial index.
        
    end % end for f (groups/files)

end % end for t (trials)


% ====================================================================
% Aggregate data.
% ====================================================================

% ----------------------------------
% AVERAGE PER RACE PER YEAR.
% ----------------------------------
% AverageNumbers1 = zeros(numYears, numDimensions);
% AverageNumbers2 = zeros(numYears, numDimensions);
% 
% for t = trialRange(1)+1:trialRange(2)+1
%     AverageNumbers1(:,:) = AverageNumbers1(:,:) + AllData1(:,:,t);
%     AverageNumbers2(:,:) = AverageNumbers2(:,:) + AllData2(:,:,t);
% end % end for t (trials)
% 
% AverageNumbers1 = AverageNumbers1 ./ numTrials
% AverageNumbers2 = AverageNumbers2 ./ numTrials

% ====================================================================
% Plot data.
% ====================================================================
uwoPurple = [79,38,131] ./ 255;
uwoSilver = [128,127,131] ./ 255;
figure();
scatter(AllDataGen(1,1,:), AllDataGen(1,2,:), 'MarkerFaceColor', uwoPurple, 'MarkerEdgeColor', uwoPurple);
hold on;
scatter(AllDataLoad(1,1,:), AllDataLoad(1,2,:), 'MarkerFaceColor', uwoSilver, 'MarkerEdgeColor', uwoSilver);
hold off;
%set(gca, 'xtick', [1,11,21,31]);
%set(gca, 'xticklabel', {'2015';'2025';'2035';'2045'});
%ylim([-0.1, 9]);
legend('Generate Society', 'Load in Society','Location','NorthWest');
xlabel('Population Size');
ylabel('Execution Time (s)');
title('Generation and Loading Times for Various Populations');