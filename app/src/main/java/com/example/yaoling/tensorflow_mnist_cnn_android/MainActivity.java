package com.example.yaoling.tensorflow_mnist_cnn_android;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.yaoling.tensorflow_mnist_cnn_android.DrawView;
import android.content.res.AssetManager;


public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    private TextView textViewProbability;
    private Button buttonClear;
    private Button buttonPredict;
    private DrawView drawView;
    private TFClassifier tfClassifier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.textViewResult);
        textViewProbability = findViewById(R.id.textViewProbability);
        buttonClear = findViewById(R.id.buttonClear);
        buttonPredict = findViewById(R.id.buttonPredict);
        drawView = findViewById(R.id.drawView);
        tfClassifier =new TFClassifier(getAssets(), "file:///android_asset/model.pb");//输入模型存放路径，并加载TensoFlow模型


        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.clearCanvas();
                textViewResult.setText("Model Prediction: ");
                textViewProbability.setText("Confidence: ");
            }
        });

        buttonPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //得到pixel的数据
                float[] pixels = drawView.getPixels();
                System.out.print("Pixels is : " + pixels);
                //得到结果数组
                float[] result = tfClassifier.predict(pixels);
                float max = -1;
                int answer = -1;
                //找到最大值即为预测结果
                for (int i = 0; i < result.length; i++) {
                    if (result[i] > max) {
                        max = result[i];
                        answer = i;
                    }
                }
                //在textView里展示结果
                textViewResult.setText("Model Prediction: " + answer);
                textViewProbability.setText("Confidence: " + max);
            }
        });
    }
}
