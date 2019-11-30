package com.example.steven.myapplication.other.fish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.steven.myapplication.R;
import com.example.steven.myapplication.bean.FishBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Steven on 16/11/29.
 */
public class FishManager {
    private final int FRAME_INTERVAL = 150;

    private final int MAX_COUNT = 17;  //最大数量

    private int width;
    private int height;

    private int fishWidth;
    private int fishHeight;

    private Context context;

    private Random random ;

    private List<FishModel> fishs ;

    public FishManager(Context context,int width,int height){
        this.width = width;
        this.height = height;
        this.context = context;
        random = new Random();
        fishs = new ArrayList<FishModel>();
    }


    /**
     * 创建一群鱼
     */
    public List<FishModel> createFishGroup(){
        for(int i = 0 ; i < MAX_COUNT ; i++){
            FishModel fishModel = new FishModel(context);
            fishModel.setAnim(getFishAnim());
            fishModel.setPath(getRandomPath());
            fishs.add(fishModel);

        }
        return fishs;
    }


    /**
     * 创建一只鱼
     */
    public FishModel createFish(FishBean fishBean){
        FishModel fishModel = new FishModel(context);
        fishModel.setAnim(getFishAnim());
        fishModel.setPath(getRandomPath());
        fishs.add(fishModel);
        return fishModel;
    }




    /**
     * 获取随机路线
     * @return
     */
    public Path getRandomPath(){
        Path path = new Path();
        List<Point> points = getRandomPoint();
        path.moveTo(points.get(0).x,points.get(0).y);
        path.cubicTo(points.get(1).x,points.get(1).y,points.get(2).x,points.get(2).y,points.get(3).x,points.get(3).y);
        return path;
    }


    //获取贝塞尔4个随机点
    private List<Point> getRandomPoint(){
        int num = getRandomInt(200,false);
        List<Point> points = null;
        switch (num % 4){
            case 0:
                points = fromeLeft2Right();
                break;

            case 1:
                points = fromeBottom2Top();
                break;

            case 2:
                points = fromeRight2Left();
                break;

            default:
                points = fromeTop2Bottom();
                break;
        }

        return points;
    }


    /**
     * 获取一个随机数
     * @param region
     * @param isMinus 是否是负数
     * @return
     */
    private int getRandomInt(int region, boolean isMinus){

        if(isMinus){
            int temp = random.nextInt(100);
            if(temp % 2 == 0){
                return random.nextInt(region);
            }
            return - random.nextInt(region);
        }else{
            return random.nextInt(region);
        }
    }


    //从上到下
    private List<Point> fromeTop2Bottom(){
        int temp;

        List<Point> points = new ArrayList<Point>();
        Point p1 = new Point();
        p1.x = getRandomInt(width,false);
        p1.y = 0;
        points.add(p1);

        Point p2 = new Point();
        temp = getRandomInt(width,true);
        p2.x = p1.x + temp;
        p2.y = height / 3;
        points.add(p2);

        Point p3 = new Point();
        temp = getRandomInt(width,true);
        p3.x = p1.x + temp;
        p3.y = height / 3 * 2 ;
        points.add(p3);

        Point p4 = new Point();
        p4.x = getRandomInt(width,false);
        p4.y = height + fishHeight;
        points.add(p4);
        return points;
    }

    //从下到上
    private List<Point> fromeBottom2Top(){
        int temp;

        List<Point> points = new ArrayList<Point>();
        Point p1 = new Point();
        p1.x = getRandomInt(width,false);
        p1.y = height + fishHeight;;
        points.add(p1);

        Point p2 = new Point();
        temp = getRandomInt(width,true);
        p2.x = p1.x + temp;
        p2.y = height / 3 * 2 ;
        points.add(p2);

        Point p3 = new Point();
        temp = getRandomInt(width,true);
        p3.x = p1.x + temp;
        p3.y = height / 3;
        points.add(p3);

        Point p4 = new Point();
        p4.x = getRandomInt(width,false);
        p4.y = 0;
        points.add(p4);
        return points;
    }


    //从左到右
    private List<Point> fromeLeft2Right(){
        int temp;

        List<Point> points = new ArrayList<Point>();
        Point p1 = new Point();
        p1.x = 0 - fishWidth;
        p1.y = getRandomInt(height,false);
        points.add(p1);

        Point p2 = new Point();
        temp = getRandomInt(height,true);
        p2.x = width / 3;
        p2.y = p1.y + temp;
        points.add(p2);

        Point p3 = new Point();
        temp = getRandomInt(height,true);
        p3.x = width / 3 * 2 ;
        p3.y = p1.y + temp;
        points.add(p3);

        Point p4 = new Point();
        p4.x = width + fishHeight;
        p4.y = getRandomInt(height,false);
        points.add(p4);
        return points;


    }


    //从右到左
    private List<Point> fromeRight2Left(){
        int temp;

        List<Point> points = new ArrayList<Point>();
        Point p1 = new Point();
        p1.x = width + fishHeight;
        p1.y = getRandomInt(height,false);
        points.add(p1);

        Point p2 = new Point();
        temp = getRandomInt(height,true);
        p2.x = width / 3 * 2 ;
        p2.y = p1.y + temp;
        points.add(p2);

        Point p3 = new Point();
        temp = getRandomInt(height,true);
        p3.x = width / 3;
        p3.y = p1.y + temp;
        points.add(p3);

        Point p4 = new Point();
        p4.x = 0 - fishWidth;
        p4.y = getRandomInt(height,false);
        points.add(p4);
        return points;
    }

    //将鱼的图片切割出来
    private AnimationDrawable getFishAnim(){
        Bitmap fishBit = BitmapFactory.decodeResource(context.getResources(), R.mipmap.fishs);
        fishWidth = fishBit.getWidth() / 7;
        fishHeight = fishBit.getHeight();
        AnimationDrawable anim = new AnimationDrawable();
        for(int i = 0 ;i < 7 ; i++){
            Bitmap btmap = Bitmap.createBitmap(fishBit,i * fishWidth,0,fishWidth,fishHeight);
            Drawable d = new BitmapDrawable(context.getResources(),btmap);

            anim.addFrame(d,FRAME_INTERVAL);
        }
        anim.setOneShot(false);
        return anim;
    }



    public List<FishModel> getFishs() {
        return fishs;
    }
}
