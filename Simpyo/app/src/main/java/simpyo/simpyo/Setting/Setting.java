package simpyo.simpyo.Setting;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

public class Setting {
    public static final String Tag = "SiriusKp"; // Log 에 띄울 태그
    public static  String postParameter = "";
    public static final int loadingSecond = 1500; // 로딩시간

    public static final int SMALL_SIZE = 100; // 작은 아이콘 크기
    public static final int BIG_SIZE = 200; // 큰 아이콘 크기

    public static int SMOKE_SWITCH = 0;

    public static void transAnimation(boolean bool, View view) {
        AnimationSet aniInSet = new AnimationSet(true);
        AnimationSet aniOutSet = new AnimationSet(true);
        aniInSet.setInterpolator(new AccelerateInterpolator());
        Animation transInAni = new TranslateAnimation(0, 0, 100.0f, 0);
        Animation transOutAni = new TranslateAnimation(0, 0, 0, 100.0f);
        transInAni.setDuration(200);
        transOutAni.setDuration(200);
        aniInSet.addAnimation(transInAni);
        aniOutSet.addAnimation(transOutAni);
        if (bool) {
            view.setAnimation(aniInSet);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setAnimation(aniOutSet);
            view.setVisibility(View.GONE);
        }
    }

    private static Bitmap resize(Bitmap bitmap, int resize){
        int resizeWidth = resize;
        double aspectRatio = (double) bitmap.getHeight() / (double) bitmap.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);

        bitmap = Bitmap.createScaledBitmap(bitmap, resizeWidth, targetHeight, false);

        return bitmap;
    }

}
