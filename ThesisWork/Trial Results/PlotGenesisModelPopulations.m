clear; clc;

filenameBase1 = 'Model Populations/MonadicFate.txt';
filenameBase2 = 'Model Populations/DyadicFate.txt';
filenameBase3 = 'Model Populations/MonadicDyadicFate.txt';
filenameBase4 = 'Model Populations/GenesisFate.txt';
files = {filenameBase1, filenameBase2, filenameBase3, filenameBase4};


numYears = 1;
numDimensions = 1; % Population.





for f = 1:size(files,2)
    
    % ====================================================================
    % Read each file.
    % ====================================================================
    file = files{f};
    X = csvread(file);
    
    
    % ====================================================================
    % Calculate mean and std.
    % ====================================================================
    meanPop = mean(X(:,2),1);
    stdPop = std(X(:,2),1);
    fprintf('Q%i: %.4f %c %.4f\n', f, meanPop, 177, stdPop);
    
end