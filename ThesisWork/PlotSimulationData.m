filepath = 'Output/Results/';
%filename = 'population30Years.txt';
filename = 'population50Years_Comparison.txt';



file = strcat(filepath,filename);

% Read CSV file.
X = csvread(file);

figure();
plot(X(:,1), X(:,2), 'g');
hold on;
plot(X(:,1), X(:,3), 'b');
hold off;