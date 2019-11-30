package com.example.steven.myapplication.other.tarot;

/**
 * Created by Steven on 16/12/6.
 */
public class TarotBean {

    private int imageId;

    private String cardName;

    private String zhengwei;    //正位解释

    private String niwei;    //逆位解释

    private String paiyi;

    private boolean isZhengwei;     //是否是正位


    public TarotBean(int imageId, String cardName, String zhengwei, String niwei, boolean isZhengwei) {
        this.imageId = imageId;
        this.cardName = cardName;
        this.zhengwei = zhengwei;
        this.niwei = niwei;
        this.isZhengwei = isZhengwei;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getZhengwei() {
        return zhengwei;
    }

    public void setZhengwei(String zhengwei) {
        this.zhengwei = zhengwei;
    }

    public String getNiwei() {
        return niwei;
    }

    public void setNiwei(String niwei) {
        this.niwei = niwei;
    }

    public boolean isZhengwei() {
        return isZhengwei;
    }

    public void setZhengwei(boolean zhengwei) {
        isZhengwei = zhengwei;
    }

    public String getPaiyi() {
        return paiyi;
    }

    public void setPaiyi(String paiyi) {
        this.paiyi = paiyi;
    }
}
