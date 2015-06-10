function [ x ] = ReadBinaryFile( fname )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here

    clear;
    clc;

    fname = 'societyInformation.bin';

    % Note that the endianness must be set to "ieee-be" because Java stores
    % it as big-endian which is not the default for Matlab to read in.
    fid = fopen(fname, 'rb', 'ieee-be');

    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % Read each value from the file.
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
    %x = ReadDouble(fid)
    
    % Global Society Information.
    % -------------------------------------------------
    soc_Size = ReadInt(fid);                % Society size
    soc_Name = ReadString(fid);             % Society name
    soc_Year = ReadInt(fid);                % Society year

    % Agent-Based Information.
    % -------------------------------------------------
    for p = 1:soc_Size
        
        % BASIC.
            per_Id = ReadInt(fid);                                          % Person ID
            per_Sex = ReadInt(fid);                                         % Person sex
            per_Age = ReadInt(fid);                                         % Person age
            per_BirthYear = ReadInt(fid);                                   % Person birth year

        % CULTURE.
            per_Race = ReadInt(fid);                                        % Person race
            per_Religion = ReadInt(fid);                                    % Person religion
            per_Nationality = ReadString(fid);                              % Person nationality

        % INTERESTS.
            per_NumInterests = ReadInt(fid);                                % Person interest number
            per_Interests = ReadDouble(fid, per_NumInterests);              % Person interest values
            per_NumIntWeights = ReadInt(fid);                               % Person interest weight number
            per_IntWeights = ReadDouble(fid, per_NumIntWeights);            % Person interest weight values

        % RELATIONSHIP.
            per_RelType = ReadInt(fid);                                     % Relationship type
            per_Partner = ReadInt(fid);                                     % Relationship partner
            per_RelIntSim = ReadInt(fid);                                   % Relationship interest similarity
            per_RelStrength = ReadDouble(fid);                              % Relationship strength
            per_RelStartYear = ReadInt(fid);                                % Relationship start year

        % PERSONALITY.
            per_NumTraits = ReadInt(fid);                                   % Person personality trait number
            per_Personality = ReadDouble(fid, per_NumTraits);               % Person personality trait values
            per_Intelligence = ReadDouble(fid);                             % Person intelligence value
            per_Athleticism = ReadDouble(fid);                              % Person athleticism value

        % CAREER.
            ReadString(fid);                                                %
            ReadInt(fid);                                                   %
            ReadString(fid);                                                %
            ReadInt(fid);                                                   %
            ReadBool(fid);                                                  %
            ReadInt(fid);                                                   %
            ReadInt(fid);                                                   %

        % HISTORY.
            per_NumHometowns = ReadInt(fid);                                %
            for i = 1:per_NumHometowns
                per_HometownName = ReadString(fid);                         % 
                per_HometownSYear = ReadInt(fid);                           % 
                per_HometownEYear = ReadInt(fid);                           %
            end % end i (loop through all hometown archive info)
            per_NumSchools = ReadInt(fid);                                  %
            for i = 1:per_NumSchools
                per_SchoolType = ReadString(fid);                           %
                per_SchoolName = ReadString(fid);                           %
                per_SchoolSYear = ReadInt(fid);                             %
                per_SchoolEYear = ReadInt(fid);                             %
            end % end i (loop through all school archive info)
            per_NumWorkplaces = ReadInt(fid);                               %
            for i = 1:per_NumWorkplaces
                per_WorkPlace = ReadString(fid);                            %
                per_WorkPos = ReadString(fid);                              %
                per_WorkSYear = ReadInt(fid);                               %
                per_WorkEYear = ReadInt(fid);                               %
            end % end i (loop through all work archive info)

        % SOCIETAL HISTORY.
            per_NumSocHometowns = ReadInt(fid);                             %
            for i = 1:per_NumSocHometowns
                per_SocHometownName = ReadString(fid);                      % 
                per_SocHometownSYear = ReadInt(fid);                        % 
                per_SocHometownEYear = ReadInt(fid);                        %
            end % end i (loop through all societal hometown archive info)
            per_NumSocSchools = ReadInt(fid);                               %
            for i = 1:per_NumSocSchools
                per_SocSchoolType = ReadString(fid);                        %
                per_SocSchoolName = ReadString(fid);                        %
                per_SocSchoolSYear = ReadInt(fid);                          %
                per_SocSchoolEYear = ReadInt(fid);                          %
            end % end i (loop through all societal school archive info)
            per_NumSocWorkplaces = ReadInt(fid);                            %
            for i = 1:per_NumSocWorkplaces
                per_SocWorkPlace = ReadString(fid);                         %
                per_SocWorkPos = ReadString(fid);                           %
                per_SocWorkSYear = ReadInt(fid);                            %
                per_SocWorkEYear = ReadInt(fid);                            %
            end % end i (loop through all societal work archive info)

        % FAMILY.
            per_FamilyID = ReadInt(fid);
            per_NumParents = ReadInt(fid);
            per_ParentIDs = ReadInt(fid,per_NumParents);
            per_NumChildren = ReadInt(fid);
            per_ChildIDs = ReadInt(fid,per_NumChildren);
            per_NumSibling = ReadInt(fid);
            per_SiblingIDs = ReadInt(fid,per_NumSibling);

        % CLUBS.
            per_NumClubs = ReadInt(fid);
            per_ClubIDs = ReadInt(fid,per_NumClubs);

        % GROUPS.
            per_NumGroups = ReadInt(fid);
            for i = 1:per_NumGroups
                per_GroupID = ReadString(fid);
                per_GroupRole = ReadString(fid);
            end % end i (loop through all groups)

        % FRIENDSHIP DETERMINATION.
            per_NumFriendProbs = ReadInt(fid);
            per_FriendProbIds = ReadInt(fid,per_NumFriendProbs);
            per_FriendProbabilities = ReadDouble(fid,per_NumFriendProbs);

        % FRIENDS.
            per_NumFriends = ReadInt(fid);
            for i = 1:per_NumFriends
                per_FriendID = ReadInt(fid);
                per_FriendType = ReadInt(fid);
                per_FriendStrength = ReadDouble(fid);
                per_FriendDesc = ReadString(fid);
                
            end % end i (loop through all groups)
            
        %disp('.....................................................')
        
    end % end for p (loop through population size)

    fclose(fid);

end % end ReadBinaryFile()






%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
%   INDIVIDUAL READERS OF PRIMITIVE TYPES
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



function [ int ] = ReadInt( fileID, numElements )
%ReadInt Read an integer from the binary file.

    if nargin < 2
        numElements = 1;
    end % end if (setting optional param numElements to 1)

    % Read in the uint value.
    int = fread(fileID, numElements, 'uint');

end % end ReadInt()



function [ dbl ] = ReadDouble( fileID, numElements )
%ReadInt Read a double from the binary file.

    if nargin < 2
        numElements = 1;
    end % end if (setting optional param numElements to 1)

    % Read in the uint value.
    dbl = fread(fileID, numElements, 'double');

end % end ReadInt()



function [ str ] = ReadString( fileID )
%ReadString Read a UTF-8 string from the binary file.

    % First two bits store the number of characters in the string.
    numChars = fread(fileID, 1, 'int16');
    % Now read in those following characters of the string.
    ascii = fread(fileID, numChars, 'bit8');
    str = char(ascii');

end % end ReadString()



function [ bool ] = ReadBool( fileID, numElements )
%ReadInt Read a boolean from the binary file.

    if nargin < 2
        numElements = 1;
    end % end if (setting optional param numElements to 1)

    % Read in the uint value.
    bool = fread(fileID, numElements, 'int8');

end % end ReadInt()


