% interpRange2 = zeros(length(Vertices3),1);
% for i=1:length(Vertices3)
%     for j = 1:length(PostcompressedVert)
%     distance(:,1) = (Vertices3(:,1)-PostcompressedVert(j,1)).^2;
%     distance(:,2) = (Vertices3(:,2)-PostcompressedVert(j,2)).^2;
%     distance(:,3) = (Vertices3(:,3)-PostcompressedVert(j,3)).^2;
%     distance = sqrt(distance(:,1)+distance(:,2)+distance(:,3));
%     [temp1,temp2] = find(distance < .1);
%     
%     end
% end
% scatter3(Vertices3(interpRange2,1),Vertices3(interpRange2,2),Vertices3(interpRange2,3))
% 

interpRange2 = zeros(length(Vertices3),1);
for i = 1:length(PostcompressedVert)
    distance(:,1) = (Vertices3(:,1)-PostcompressedVert(i,1)).^2;
    distance(:,2) = (Vertices3(:,2)-PostcompressedVert(i,2)).^2;
    distance(:,3) = (Vertices3(:,3)-PostcompressedVert(i,3)).^2;
    distance = sqrt(distance(:,1)+distance(:,2)+distance(:,3));          
    [temp1,temp2] = find(distance < .6);
    interpRange2(temp1) = 1  ;  
end
interpRange2 = logical(interpRange2);
scatter3(Vertices3(interpRange2,1),Vertices3(interpRange2,2),Vertices3(interpRange2,3))