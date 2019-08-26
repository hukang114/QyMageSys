/*
package com.qymage.sys.common.allpay.ylpay;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.unionpay.UPPayAssistEx;

*/
/**
 * Created by HK on 2018/4/24.
 *//*


public class YLPay {

    private Context mContext;
    private String Tag="YLPay";

    public YLPay(Context context) {
        mContext = context;
    }

    public static YLPay getInstance(Context context) {
        return new YLPay(context);
    }

    // 支付传入服务起生成的tn 参数即可
    public void startPay(String tn){
        // mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
        // 0 - 启动银联正式环境
        // 1 - 连接银联测试环境
        Log.d(Tag,tn);
        try {
            int ret = UPPayAssistEx.startPay(mContext, null, null, tn, "00");
            if (ret == 2 || ret == -1) {
                // 需要重新安装控件
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提示!");
                builder.setMessage("完成购买需要安装银联支付控件，是否安装？");
                builder.setNegativeButton("确定",
                        (dialog, which) -> {
                            UPPayAssistEx.installUPPayPlugin(mContext);
                            dialog.dismiss();
                        });
                builder.setPositiveButton("取消",
                        (dialog, which) -> dialog.dismiss());
                builder.create().show();

            }
        }catch (Exception e){
            Toast.makeText(mContext, "请先安装银联支付控件", Toast.LENGTH_SHORT).show();
        }
    }

//    // 支付结果处理
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//            // 步骤3：处理银联手机支付控件返回的支付结果
//            if (data == null) {
//                return;
//            }
//            startActivity(new Intent().putExtra("payname","银联").putExtra("resultStatus",data.getExtras().getString("pay_result")).putExtra("data",data).setClass(mContext, PaymentResultActivity.class));
//        if (resultStatus.equalsIgnoreCase("success")) {
//            paymentresultTv.setText("付款成功，我们将尽快为您处理！");
//        } else if (resultStatus.equalsIgnoreCase("fail")) {
//            paymentresultImg.setImageResource(R.mipmap.fail);
//            paymentresultTv.setText("原因:错误返回码" + resultStatus);
//        } else if (resultStatus.equalsIgnoreCase("cancel")) {
//            paymentresultImg.setImageResource(R.mipmap.fail);
//            paymentresultTv.setText("原因:您已取消支付");
//        }
//    }


}
*/
