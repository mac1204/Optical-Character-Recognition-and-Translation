import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class ocr_main {
	
	
	Mat I; 
	String ans = "";
	String ans2 = "";
	
	public ocr_main() {
		// TODO Auto-generated constructor stub
	}




	public String ocr_main(String args ) throws Exception
	{
		File file = new File(args);
		if (file.exists()) { // This is true
			System.loadLibrary("opencv_java246");
			//Mat I = Highgui.imread("D:/ocr/training.png", Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		    I = Highgui.imread(file.getAbsolutePath(),Highgui.CV_LOAD_IMAGE_COLOR);  //file.getPath() also same
		    Mat Igray = new Mat (I.width(), I.height(), CvType.CV_8UC1);
		    //Utils.bitmapToMat(b, tmp);
		    Imgproc.cvtColor(I, Igray, Imgproc.COLOR_RGB2GRAY);
		    //Highgui.imwrite("D:/ocr/training1.png",Igray);
		    Mat Ibw = new Mat (I.width(), I.height(), CvType.CV_8UC1);
		    Imgproc.threshold(Igray, Ibw, 127, 255, Imgproc.THRESH_BINARY);
		    Highgui.imwrite("D:/ocr/training4.png",Ibw);
		    Mat Iedge = new Mat(I.width(), I.height(), CvType.CV_8UC1);
		    Imgproc.Canny(Ibw, Iedge, 0, 1);
		    Highgui.imwrite("D:/ocr/training1.png",Iedge);
		    Mat Iedge2 = new Mat(I.width(), I.height(), CvType.CV_8UC1);
		    Imgproc.dilate(Iedge, Iedge2,Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));
		    Highgui.imwrite("D:/ocr/training2.png",Iedge2);
		    Mat Ifill = new Mat(I.height(), I.width(), CvType.CV_8UC1);
		    //there could be some processing
		    //Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_GRAY2RGB, 4);
		    
		    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		    Mat mHierarchy = new Mat();
		    Imgproc.findContours(Iedge2, contours, mHierarchy, Imgproc.RETR_EXTERNAL , Imgproc.CHAIN_APPROX_SIMPLE);
		    //Iterator<MatOfPoint> each = contours.iterator();
		    //while(each.hasNext())
		    //{
		    	
		    	Scalar color = new Scalar(255,255,255);
		    	
				Imgproc.drawContours(Ifill, contours, -1, color, -1);
				
				//Imgproc
		   // }
				Highgui.imwrite("D:/ocr/training3.png",Ifill);
				//System.out.println(contours.size());
				
				//MatOfPoint each1[] = new MatOfPoint[contours.size()];
				MatOfPoint each[] = new MatOfPoint[contours.size()];
				Rect[] rect1 = new Rect[contours.size()];
				Rect[] rect = new Rect[contours.size()];
				for(int i=0;i<contours.size();i++){
					
					each[i] = contours.get(i);
					rect1[i] = Imgproc.boundingRect(each[i]);
					//Imgproc.boundingRect(each);
					//Mat img1 = new Mat(each.height(), each.width(), CvType.CV_8UC1);
				}
				rect = count_sort(rect1);
				
				
				for(int i=0;i<contours.size();i++){
					
		            //System.out.println(rect.height + " " + rect.width + " " + rect.x + " " + rect.y);
		            //if (rect.height > 28){
		            //System.out.println(rect.x +","+rect.y+","+rect.height+","+rect.width);
		            Core.rectangle(I, new Point(rect[i].x,rect[i].y), new Point((rect[i].x+rect[i].width),(rect[i].y+rect[i].height)),new Scalar(255,0,255));
					//each.convertTo(img1, CvType.CV_8UC1);
					//System.out.println(each.height() + " " + each.width());
					//Highgui.imwrite("D:/ocr/img111.png",img1);
		            Mat crop = new Mat(Ibw, rect[i]);
		            Mat rescrop = new Mat(12, 8, CvType.CV_8UC1);
		            Size dsize = new Size(8,12);
					Imgproc.resize(crop, rescrop, dsize );
					
					
					for(int j=0;j<37;j++){
						
						
						String data_file = "letter/" + j +".png";
						
						Mat data = Highgui.imread(data_file,Highgui.CV_LOAD_IMAGE_COLOR);
						Mat datagray = new Mat (data.width(), data.height(), CvType.CV_8UC1);
						Imgproc.cvtColor(data, datagray, Imgproc.COLOR_RGB2GRAY);
						Mat databw = new Mat (data.width(), data.height(), CvType.CV_8UC1);
					    Imgproc.threshold(datagray, databw, 127, 255, Imgproc.THRESH_BINARY);
						int count = 0;
						int totcount = 0;
					    for(int k=0;k<data.width();k++){
					    	for(int l=0;l<data.height();l++){
					    		
					    		double dataval[] = data.get(l, k);
					    		double rescropval[] = rescrop.get(l, k);
					    		if(dataval[0] < 128){
						    		
					    			totcount++;
					    			if(rescropval[0] < 128){
					    				count++;
					    			}
						    		//System.out.println("mac" + aaa[0] + " " + aaa[1] + " " + aaa[2] + " ");
						    		/*if(rescrop.get(k, l) < 128){
						    			
						    		}*/
					    		}
					    	}
					    }
					    if((count/totcount) > 0.9){
					    	//System.out.println(j);
					    	switch (j){
					    	
					    	case 0:  ans  = ans + "0";
		                    break;
		                    
					    	case 1:  ans  = ans + "9";
		                    break;
		                    
					    	case 2:  ans  = ans + "8";
		                    break;
		                    
		                    
					    	case 3:  ans  = ans + "7";
		                    break;
		                    
					    	case 4:  ans  = ans + "6";
		                    break;
		                    
					    	case 5:  ans  = ans + "5";
		                    break;
		                    
					    	case 6:  ans  = ans + "4";
		                    break;
		                    
					    	case 7:  ans  = ans + "3";
		                    break;
		                    
					    	case 8:  ans  = ans + "2";
		                    break;
		                    
					    	case 9:  ans  = ans + "1";
		                    break;
		                    
					    	case 10:  ans  = ans + "0";
		                    break;
		                    
					    	case 11:  ans  = ans + "Z";
		                    break;
		                    
					    	case 12:  ans  = ans + "Y";
		                    break;
		                    
					    	case 13:  ans  = ans + "X";
		                    break;
		                    
					    	case 14:  ans  = ans + "W";
		                    break;
		                    
					    	case 15:  ans  = ans + "V";
		                    break;
		                    
					    	case 16:  ans  = ans + "U";
		                    break;
		                    
					    	case 17:  ans  = ans + "T";
					    	//System.out.println("T");
		                    break;
		                    
		                    
					    	case 18:  ans  = ans + "S";
		                    break;
		                    
					    	case 19:  ans  = ans + "R";
		                    break;
		                    
					    	case 20:  ans  = ans + "Q";
		                    break;
		                    
					    	case 21:  ans  = ans + "P";
		                    break;
		                    
					    	case 22:  ans  = ans + "O";
		                    break;
		                    
					    	case 23:  ans  = ans + "N";
		                    break;
		                    
					    	case 24:  ans  = ans + "M";
		                    break;
		                    
					    	case 25:  ans  = ans + "L";
		                    break;
		                    
					    	case 26:  ans  = ans + "K";
		                    break;
					    	
					    	case 27:  ans  = ans + "J";
		                    break;
					    	
					    	case 28:  ans  = ans + "I";
		                    break;
					    	
					    	case 29:  ans  = ans + "H";
		                    break;
		                    
					    	case 30:  ans  = ans + "G";
		                    break;
		                    
					    	case 31:  ans  = ans + "F";
		                    break;
		                    
		                    
					    	case 32:  ans  = ans + "E";
		                    break;
		                    
					    	case 33:  ans  = ans + "D";
		                    break;
		                    
					    	case 34:  ans  = ans + "C";
					    	//System.out.println("c");
		                    break;
		                    
					    	case 35:  ans  = ans + "B";
		                    break;
		                    
		                    
					    	case 36:  ans = ans + "A";
					    	//System.out.println("A");
		                    break;
		                    
					    	default: ans  = ans + "%";
		                    break;
		                    
		                    
					    	}
					    	
					    	
					    	
					    }
					    
					    
					    
						
					}
					
					File dir = new File("new_letter");
					dir.mkdir();
		            String filename = "new_letter/" + i + ".png";
		            Highgui.imwrite(filename, rescrop);
				}
				//System.out.println(ans);
				StringBuffer ans1 = new StringBuffer(ans);
				
				ans2 = (ans1.reverse()).toString();
		    
		    
		    
		    //Utils.matToBitmap(tmp, b);
			Highgui.imwrite("D:/ocr/img111.png",I);
		    
		    
		    
		}
		return ans2;
	}

	

	
	private Rect[] count_sort(Rect[] rect1) {
		// TODO Auto-generated method stub
		//int max = I.width();
		
		//Rect a[] = new Rect[rect1.length];
		Rect temp = new Rect();
		boolean flag = true;
		while(flag){
			flag = false;
			for(int j=0;j<rect1.length-1;j++){
				
				if( rect1[ j ].x < rect1[j+1].x ){
					
					 temp = rect1[ j ];                //swap elements
                     rect1[ j ] = rect1[ j+1 ];
                     //System.out.println("mac");
                     rect1[ j+1 ] = temp;
                    flag = true;
				}
				
			}
		}
		
		
		return rect1;
	}
}
