clear; clc;

filenameBase1 = 'Queries/Q1_AddPerson';
filenameBase2 = 'Queries/Q2_AddCouple';
filenameBase3 = 'Queries/Q3_KillPerson';
filenameBase4 = 'Queries/Q4_GetRaceNumbers';
filenameBase5 = 'Queries/Q5_GetTotalFriendships';
filenameBase6 = 'Queries/Q6_GetTotalLocalPeople';
filenameBase7 = 'Queries/Q7_GetNumberOfSingleFriends';

fileNameExt1 = '_100M.txt';
fileNameExt2 = '_200M.txt';
fileNameExt3 = '_300M.txt';
fileNameExt4 = '_400M.txt';
fileNameExt5 = '_500M.txt';

% ALL queries.
queries = {filenameBase1, filenameBase2, filenameBase3, filenameBase4, filenameBase5, filenameBase6, filenameBase7};
% All queries EXCEPT Q3.
%queries = {filenameBase1, filenameBase2, filenameBase4, filenameBase5, filenameBase6, filenameBase7};
populations = {fileNameExt1, fileNameExt2, fileNameExt3, fileNameExt4, fileNameExt5};

numQueryTypes = size(queries, 2);
numPopulations = size(populations, 2);
%numFiles = numQueries*numPopulations;

numDimensions = 3; % Year, Population, QueryTime.
numQueriesPerTrial = 10;

AllData = zeros(numQueryTypes, numPopulations);
format long g
for q = 1:numQueryTypes
    
    for p = 1:numPopulations
    
    
        % ====================================================================
        % Read each file.
        % ====================================================================
        file = strcat(queries{q}, populations{p});
        X = csvread(file);
    
    
        % ====================================================================
        % Calculate mean and std.
        % ====================================================================
        meanTime = mean(X(:,3),1);
        stdTime = std(X(:,3),1);
        
        meanTime_ms = meanTime * 1000;
        stdTime_ms = stdTime * 1000;

        %fprintf('Q%i for N=%iM: %d %c %f\n', q, p*100, meanTime, 177, stdTime);
        fprintf('Q%i for N=%iM: %f %c %f\n', q, p*100, meanTime_ms, 177, stdTime_ms);
        
        AllData(q,p) = meanTime;
    
    end % end for p (population sizes)
    
    fprintf('\n');
    
end % end for q (queries)





% plot(AllData');
% title('Query Execution Times for Various Populations');
% xlabel('Population Base (M = Marriages)');
% ylabel('Time (s)');
% set(gca, 'xtick', [1,2,3,4,5]);
% set(gca, 'xticklabel', {'100M';'200M';'300M';'400M';'500M'});
% 
% legend('Q1','Q2','Q3','Q4','Q5','Q6','Q7', 'Location','NorthWest'); % ALL queries.
% %legend('Q1','Q2','Q4','Q5','Q6','Q7'); % Without Q3.



bar(AllData);
title('Query Execution Times for Various Populations');
xlabel('Query');
ylabel('Time (s)');
set(gca, 'xtick', 1:7);
set(gca, 'xticklabel', {'Q1','Q2','Q3','Q4','Q5','Q6','Q7'});

leg = legend({'200';'400';'600';'800';'1000'}, 'Location', 'East'); % ALL queries.
legTitle = get(leg, 'Title');
set(legTitle, 'String', 'Base Population');
%legend('Q1','Q2','Q4','Q5','Q6','Q7'); % Without Q3.

