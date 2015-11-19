package com.wander.danmu.Utils.emoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

import com.wander.danmu.PixelTools;
import com.wander.danmu.R;
import com.wander.danmu.Utils.BitmapTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 表情转换工具
 *
 * @author DQ
 */
public class FaceConversionUtil {

    private int pageSize = 20;

    private static FaceConversionUtil mFaceConversionUtil;

    private HashMap<String, String> emojiMap = new HashMap<String, String>();

    /**
     * 所有表情
     */
    private List<ChatEmoji> chatEmojis = new ArrayList<ChatEmoji>();
    private Context context;
    /**
     * 分页表情集
     */
    public List<List<ChatEmoji>> pageLists = new ArrayList<List<ChatEmoji>>();

    public static List<String> getEmojiFile(Context context) {
        try {
            List<String> list = new ArrayList<String>();
            InputStream in = context.getResources().getAssets().open("emoji.config");
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private FaceConversionUtil(Context context) {
        this.context = context;
        ParseData(getEmojiFile(context), context);
    }

    public static FaceConversionUtil getInstace(Context context) {
        if (mFaceConversionUtil == null) {
            mFaceConversionUtil = new FaceConversionUtil(context);
        }
        return mFaceConversionUtil;
    }

    /**
     * 使用表情退出时需要调用的方法
     */
    public void destoryWhenExit() {
        if (null != emojiMap) {
            emojiMap.clear();
            emojiMap = null;
        }
        if (null != chatEmojis) {
            chatEmojis.clear();
            chatEmojis = null;
        }
        if (null != pageLists) {
            pageLists.clear();
            pageLists = null;
        }
        mFaceConversionUtil = null;
    }

    public SpannableString getExpressionString(String str) {
        SpannableString spannableString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = "\\[[^\\]]+\\]";
        //忽略大小写
        Pattern patten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(context, spannableString, patten, 0);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return spannableString;
    }

    /**
     * 添加表情
     *
     * @param context
     * @param imgId
     * @param spannableString
     * @return
     */
    public SpannableString addFace(Context context, int imgId,
                                   String spannableString) {
        if (TextUtils.isEmpty(spannableString)) {
            return null;
        }
        try {
//			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
//					imgId);
            Bitmap bitmap = BitmapTools.readBitMap(context, imgId);
            bitmap = Bitmap.createScaledBitmap(bitmap, 35, 35, true);
            ImageSpan imageSpan = new ImageSpan(context, bitmap);
            SpannableString spannable = new SpannableString(spannableString);
            spannable.setSpan(imageSpan, 0, spannableString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannable;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private void dealExpression(Context context, SpannableString spannableString, Pattern patten, int start)
            throws Exception {
        int width = PixelTools.dip2px(context, 22);
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            String value = emojiMap.get(key);
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            int resId = context.getResources().getIdentifier(value, "drawable",
                    context.getPackageName());
            if (resId != 0) {
                int end = 0;
                try {
                    Bitmap bitmap = BitmapTools.readBitMap(context, resId);
//					Bitmap bitmap = BitmapFactory.decodeResource(
//							context.getResources(), resId);
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, width, true);
                    ImageSpan imageSpan = new ImageSpan(context, bitmap);
                    end = matcher.start() + key.length();
                    spannableString.setSpan(imageSpan, matcher.start(), end,
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } catch (OutOfMemoryError e) {
                    // 如果上面的代码在 end = end = matcher.start() + key.length();  这个语句执行之前出现了异常，
                    // 那么就会无限递归，引发 StackOverflowError 异常, 添加返回语句。
                    return;
                }
                if (end < spannableString.length()) {
                    dealExpression(context, spannableString, patten, end);
                }
                break;
            }
        }
    }

    /**
     * 解析数据
     * @param data
     * @param context
     */
    private void ParseData(List<String> data, Context context) {
        if (data == null) {
            return;
        }
        ChatEmoji emojEentry;
        try {
            for (String str : data) {
                String[] text = str.split(",");
                String fileName = text[0]
                        .substring(0, text[0].lastIndexOf("."));
                emojiMap.put(text[1], fileName);
                int resID = context.getResources().getIdentifier(fileName,
                        "drawable", context.getPackageName());

                if (resID != 0) {
                    emojEentry = new ChatEmoji();
                    emojEentry.setId(resID);
                    emojEentry.setCharacter(text[1]);
                    emojEentry.setFaceName(fileName);
                    chatEmojis.add(emojEentry);
                }
            }
            int pageCount = (int) Math.ceil(chatEmojis.size() / 20 + 0.1);

            for (int i = 0; i < pageCount; i++) {
                pageLists.add(getData(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 分页
     * @param page
     * @return
     */
    private List<ChatEmoji> getData(int page) {
        int startIndex = page * pageSize;
        int endIndex = startIndex + pageSize;

        if (endIndex > chatEmojis.size()) {
            endIndex = chatEmojis.size();
        }
        List<ChatEmoji> list = new ArrayList<ChatEmoji>();
        list.addAll(chatEmojis.subList(startIndex, endIndex));
        if (list.size() < pageSize) {
            for (int i = list.size(); i < pageSize; i++) {
                ChatEmoji object = new ChatEmoji();
                list.add(object);
            }
        }
        if (list.size() == pageSize) {
            ChatEmoji object = new ChatEmoji();
            object.setId(R.drawable.emoji_1);
            list.add(object);
        }
        return list;
    }
}