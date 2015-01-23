clc
clear all
I = imread('ocr1.png'); 
figure,imshow(I);
Igray = rgb2gray(I);
figure,imshow(Igray);
Ibw = im2bw(Igray,graythresh(Igray)); 
figure,imshow(Ibw);
Iedge = edge(Ibw,'canny');
% Iedge = edge(Ibw);
figure,imshow(Iedge);
se = strel('square',2);
Iedge2 = imdilate(Iedge, se); 
figure,imshow(Iedge2);
Ifill= imfill(Iedge2,'holes'); 
figure,imshow(Ifill);
[Ilabel num] = bwlabel(Ifill);
disp(num);
Iprops = regionprops(Ilabel); 
Ibox = [Iprops.BoundingBox];
Ibox = reshape(Ibox,[4 num]);
figure,imshow(Ibw);
hold on; 
for cnt = 1:num 
    rectangle('position',Ibox(:,cnt),'edgecolor','r');
end