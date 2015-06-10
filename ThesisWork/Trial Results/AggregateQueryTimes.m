clear; clc;

filenameBase1 = 'Queries/Q1_AddPerson.txt';
filenameBase2 = 'Queries/Q2_AddCouple.txt';
filenameBase3 = 'Queries/Q3_KillPerson.txt';
filenameBase4 = 'Queries/Q4_GetRaceNumbers.txt';
filenameBase5 = 'Queries/Q5_GetTotalFriendships.txt';
filenameBase6 = 'Queries/Q6_GetTotalLocalPeople.txt';
filenameBase7 = 'Queries/Q7_GetNumberOfSingleFriends.txt';

files = {filenameBase1, filenameBase2, filenameBase3, filenameBase4, filenameBase5, filenameBase6, filenameBase7};

numYears = 1;
numDimensions = 3; % Year, Population, QueryTime.
numQueries = 100;

format long g

for f = 1:size(files,2)
    
    % ====================================================================
    % Read each file.
    % ====================================================================
    file = files{f};
    X = csvread(file);
    
    
    % ====================================================================
    % Calculate mean and std.
    % ====================================================================
    meanTime = mean(X(:,3),1);
    stdTime = std(X(:,3),1);
    
    meanTime_ms = meanTime * 1000;
    stdTime_ms = stdTime * 1000;
    
    %disp(['Q',f,':',meanTime,' ',stdTime]);
    %fprintf('Q%i: %f %c %f\n', f, meanTime, 177, stdTime);
    fprintf('Q%i: %f %c %f\n', f, meanTime_ms, 177, stdTime_ms);
    
end









%mean(X(:,3),1)
%std(X(:,3),1)
