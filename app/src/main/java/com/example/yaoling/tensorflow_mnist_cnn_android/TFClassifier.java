package com.example.yaoling.tensorflow_mnist_cnn_android;

import android.content.Context;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class TFClassifier {
    static {
        System.loadLibrary("tensorflow_inference");
    }

    private static final int SIZE = 28 * 28;

    private TensorFlowInferenceInterface inferenceInterface;
    private static final String MODEL_FILE = "model.pb";
    private static final String INPUT_NODE = "x";
    private static final String[] OUTPUT_NODES = {"logits_eval"};
    private static final String OUTPUT_NODE = "logits_eval";
    private static final long[] INPUT_SIZE = {1,SIZE};
    private static final int OUTPUT_SIZE = 10;

    public TFClassifier(Context context) {
        inferenceInterface = new TensorFlowInferenceInterface(context.getAssets(), MODEL_FILE);

    }

    public float[] predict(float[] data) {
        float[] result = new float[OUTPUT_SIZE];
        inferenceInterface.feed(INPUT_NODE, data, INPUT_SIZE);
        inferenceInterface.run(OUTPUT_NODES);
        inferenceInterface.fetch(OUTPUT_NODE, result);
        return result;
    }

}
