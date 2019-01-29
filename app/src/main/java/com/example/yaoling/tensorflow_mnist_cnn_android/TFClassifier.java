package com.example.yaoling.tensorflow_mnist_cnn_android;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import static android.support.constraint.Constraints.TAG;


public class TFClassifier {

    private DrawView drawView;
    static {
        System.loadLibrary("tensorflow_inference");
        Log.e(TAG,"libtensorflow_inference.so库加载成功");

    }

    private static final int SIZE = 28 * 28;

    private final TensorFlowInferenceInterface inferenceInterface;
    private static final String MODEL_FILE = "model.pb";
    private static final String INPUT_NODE = "input/x_input";
    private static final String[] OUTPUT_NODES = {"logits_eval"};
    private static final String OUTPUT_NODE = "logits_eval";
    private static final long[] INPUT_SIZE = {1,SIZE};
    private static final int OUTPUT_SIZE = 10;

    
    public TFClassifier(AssetManager assetManager, String modePath) {
        //初始化TensorFlowInferenceInterface对象
        inferenceInterface = new TensorFlowInferenceInterface(assetManager,modePath);
        Log.e(TAG,"TensoFlow模型文件加载成功");
    }

    public float[] predict(float[] data) {
        float[] result = new float[OUTPUT_SIZE];
        inferenceInterface.feed(INPUT_NODE, data, INPUT_SIZE);
        inferenceInterface.run(OUTPUT_NODES);
        inferenceInterface.fetch(OUTPUT_NODE, result);
        return result;
    }
}
