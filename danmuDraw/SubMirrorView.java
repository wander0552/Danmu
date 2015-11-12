package cn.kuwo.sing.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 *
 * <pre>
 * 业务名:
 * 功能说明:
 * 编写日期:	2015-2-2
 * 作者:	ChenShuai
 *
 * </pre>
 */
class SubMirrorView extends LinearLayout
{
    private CircleImageView image;
    private TextView textView;

    public SubMirrorView(Context context)
    {
        super(context);

        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER_VERTICAL);
        image = new CircleImageView(context);
        int imageSize = (int) getResources().getDimension(R.dimen.barrage_avatar_size);
        image.setLayoutParams(new LayoutParams(imageSize, imageSize));
        this.addView(image);
        textView = new TextView(context);
        textView.setTextSize(15);
        textView.setTextColor(Color.WHITE);
        int padding = (int) getResources().getDimension(R.dimen.submirror_padding);
        textView.setPadding(padding, 0, 0, 0);
        this.addView(textView);
    }

    public void setImage(Bitmap bimtap)
    {
        image.setImageBitmap(bimtap);
        image.setVisibility(View.VISIBLE);
    }

    public void setHtmlText(String htmlText)
    {
        textView.setText(Html.fromHtml(htmlText));
    }

    public void reset()
    {
        textView.setText("");
        image.setVisibility(View.GONE);
    }

    public Bitmap getDrawMirror()
    {
        return BitmapUtil.fromView(this);
    }
}
