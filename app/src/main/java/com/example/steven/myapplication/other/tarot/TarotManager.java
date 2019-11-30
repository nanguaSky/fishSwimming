package com.example.steven.myapplication.other.tarot;

import android.content.Context;

import com.example.steven.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Steven on 16/12/6.
 * 用于随机生成塔罗牌
 */
public class TarotManager {

    private List<Integer> result;

    private int images [] ;     //塔罗牌的图片id
    private String names [] ;   //塔罗牌的名字
    private String zhengweis [] ;    //正位解释
    private String niweis [] ;       //逆位解释

    private Random mRandom;

    private Context mContext;

    public TarotManager(Context context){
        mContext = context;
        mRandom = new Random();
        result = new ArrayList<Integer>();

        names = context.getResources().getStringArray(R.array.tarot_name);
        zhengweis = context.getResources().getStringArray(R.array.tarot_zhengwei);
        niweis = context.getResources().getStringArray(R.array.tarot_niwei);

        images = new int[22];
        for (int i = 1; i <= 22; i++) {
            int id = context.getResources().getIdentifier("triangle_card_" + i,"drawable",context.getPackageName());
            images[i - 1] = id;
        }
    }


    /**
     * 创建塔罗牌
     * @return
     */
    public List<TarotCardView> createTarotCard(){
        List<TarotCardView> cards = new ArrayList<TarotCardView>();

        for(TarotBean bean : createTarot()){
            TarotCardView card = new TarotCardView(mContext);
            card.setCardBean(bean);
            cards.add(card);
        }

        return cards;
    }

    /**
     * 创建塔罗牌bean对象
     * @return
     */
    public List<TarotBean> createTarot(){
        List<TarotBean> tarots  = new ArrayList<TarotBean>();
        for(int i : randomArray(22)){
            int imgId = images[i];
            String name = names[i];
            String zhengwei = zhengweis[i];
            String niwei = niweis[i];

            int flag = mRandom.nextInt(100);
            boolean isZhengwei = flag % 2 == 0 ? true : false;

            TarotBean bean = new TarotBean(imgId,name,zhengwei,niwei,isZhengwei);
            tarots.add(bean);
        }

        return tarots;
    }



    /**
     * 随机生成牌顺序
     * @param n
     * @return
     */
    private List<Integer> randomArray(int n){
        List<Integer> result = new ArrayList<Integer>();
        int temp = mRandom.nextInt(n);
        while(result.size()!= n){
            if(result.size() == 0){
                result.add(temp);

            }else{

                boolean flag = false;
                for(int b : result){
                    if(b == temp){
                        flag = true;
                    }
                }
                if(!flag){
                    result.add(temp);
                }
            }
            temp = mRandom.nextInt(n);

        }

        return result;
    }

}
