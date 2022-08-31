package hmd.example.firebaseprojectstudyenglish.singletonpattern;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import hmd.example.firebaseprojectstudyenglish.R;

public class MessageObject {
    private static MessageObject instance = new MessageObject();

    //make the constructor private so that this class cannot be
    //instantiated
    private MessageObject(){}

    //Get the only object available
    public static MessageObject getInstance(){
        return instance;
    }

    public void ShowDialogMessage(int gravity, Context context, String message, int type){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.thongbao);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM == gravity)
        {
            dialog.setCancelable(true);
        }
        else {
            dialog.setCancelable(false);
        }

        TextView txt_name = (TextView) dialog.findViewById(R.id.dialogError2_name);
        TextView txt_message = (TextView) dialog.findViewById(R.id.dialogError2_content);
        Button btn_oke = (Button) dialog.findViewById(R.id.btn_dialogError_Oke);

        if(type == 1) txt_name.setText("SUCCESS");
        else txt_name.setText("ERROR");
        txt_message.setText(message);

        btn_oke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
