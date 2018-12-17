package simpyo.simpyo.Setting;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontChange {
    Typeface typeface = null;
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FontChange(Context context, View view){

        this.context = context;

        // 폰트 변경
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(),"fonts/NanumSquareOTFRegular.otf");
        }
        setGlobalFont(view);
    }

    private void setGlobalFont(View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int vgCnt = viewGroup.getChildCount();
                for (int i = 0; i < vgCnt; i++) {
                    View v = viewGroup.getChildAt(i);
                    if (v instanceof TextView) {
                        ((TextView) v).setTypeface(typeface);
                    }
                    setGlobalFont(v);
                }
            }
        }
    }

    public void setBoldFont(View view){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/NanumSquareOTFBold.otf");
        ((TextView)view).setTypeface(typeface);
    }

    public void setExtraBoldFont(View view){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/NanumSquareOTFExtraBold.otf");
        ((TextView)view).setTypeface(typeface);
    }


}
