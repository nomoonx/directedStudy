clear; clc;

filenameBase1 = 'Memory/MemoryUsage_100Mar';
filenameBase2 = 'Memory/MemoryUsage_200Mar';
filenameBase3 = 'Memory/MemoryUsage_300Mar';
filenameBase4 = 'Memory/MemoryUsage_400Mar';
filenameBase5 = 'Memory/MemoryUsage_500Mar';
filenameBase6 = 'Memory/MemoryUsage_600Mar';
filenameBase7 = 'Memory/MemoryUsage_700Mar';
filenameBase8 = 'Memory/MemoryUsage_800Mar';
filenameBase9 = 'Memory/MemoryUsage_900Mar';
filenameBase10 = 'Memory/MemoryUsage_1000Mar';
%filenameBase8 = 'Times/TimesToCreateOrLoad_H_';
%filenameBase9 = 'Times/TimesToCreateOrLoad_I_';
%allFiles = {filenameBase1, filenameBase2, filenameBase3, filenameBase4, filenameBase5, filenameBase6, filenameBase7, filenameBase8, filenameBase9};
allFiles = {filenameBase1, filenameBase2, filenameBase3, filenameBase4, filenameBase5, filenameBase6, filenameBase7, filenameBase8, filenameBase9, filenameBase10};
numFiles = size(allFiles, 2);

filenameExt = '.txt';


numYears = 1;
numDimensions = 3; % Population, TimeToGenerate, MemoryUsed
numGroups = 10; % Number of trials recorded in each file.

AllData = zeros(numGroups*numFiles, numDimensions); % 2 is for {Memory, Time}.


% ====================================================================
% Collect all data into global arrays.
% ====================================================================
trialEntry = 1;

for f = 1:numFiles
    %for t = 1:numGroups
        
        file = strcat(allFiles{f},filenameExt);    % Read 1st file.
        
        X = csvread(file, 1)
    

        % Loop through years.
        for t = 1:numGroups

            % Generated society times.
            AllData(trialEntry, 1) = X(t,1);   % Population
            AllData(trialEntry, 2) = X(t,2);   % Execution time (s)
            AllData(trialEntry, 3) = X(t,3);   % Memory usage (mb)

            trialEntry = trialEntry + 1; % Increment this trial index.
            
        %end % end for y (years)
        end % end for t (groups/trials per file)

    %end % end for t (groups/trials per file)

end % end for f (files)


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
scatter(AllData(:,1), AllData(:,3), 'MarkerFaceColor', uwoPurple, 'MarkerEdgeColor', uwoPurple); % Memory.
%hold on;
%scatter(AllData(:,1), AllData(:,2), 'MarkerFaceColor', uwoSilver, 'MarkerEdgeColor', uwoSilver);
%hold off;
%set(gca, 'xtick', [1,11,21,31]);
%set(gca, 'xticklabel', {'2015';'2025';'2035';'2045'});
%ylim([-0.1, 9]);
%legend('Memory (mB)', 'Execution Time (s)','Location','NorthWest');
xlabel('Population Size');
ylabel('Memory (MB)');
title('Memory Usage for Various Populations');