package com.mzth.tangerinepoints.util;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;

/**
 * Created by Administrator on 2017/5/13.
 */

public class DialogUtil {
    public interface ReshActivity {
        void reshActivity();
    }
    /**
     *
     * @param context
     * @param title 标题
     * @param message 提示消息
     * @param positiveTitle  确定文字
     * @param negativeTitle 取消文字
     * @param isNegative false没有取消按钮
     */
    public static void alertDialog(final Context context, String title, String message,
                                   String positiveTitle, String negativeTitle, boolean isNegative,
                                   final ReshActivity reshActivity) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.alert_dialog, null);
        final Dialog dialog = new Dialog(context, R.style.alert_dialog);
        dialog.setContentView(view);
        ((TextView) view.findViewById(R.id.message)).setText(message);
        TextView txtTitle=(TextView) view.findViewById(R.id.title);
        if (title!=null&&!title.equals("")) {
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle.setText(title);
        } else {
            txtTitle.setVisibility(View.GONE);
        }
        Button positiveButton = (Button) view.findViewById(R.id.positiveButton);
        Button negativeButton = (Button) view.findViewById(R.id.negativeButton);

        if (!TextUtils.isEmpty(positiveTitle)) {
            positiveButton.setText(positiveTitle);
        }
        if (negativeTitle != null && !negativeTitle.equals("")) {
            negativeButton.setText(negativeTitle);
        }

        View view_div = view.findViewById(R.id.view_div);
        if (!isNegative) {
            negativeButton.setVisibility(View.GONE);
            view_div.setVisibility(View.GONE);
        }
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (reshActivity != null) {
                    reshActivity.reshActivity();
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
